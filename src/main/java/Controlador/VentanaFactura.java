/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author ALCIDES
 */
import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;


import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaFactura extends JDialog {
    private JTextField txtID = new JTextField(10); 
    private JTextField txtNombre = new JTextField(20);  
    private boolean aceptado = false;
    private JInternalFrame parentFrame;
    private String tablaDestino;

    public VentanaFactura(Frame parent, boolean modal, JInternalFrame parentFrame, String tipo) {
        super(parent, "Registrar " + tipo.toUpperCase(), modal);
        this.parentFrame = parentFrame;
        this.tablaDestino = tipo.toLowerCase();
        
        setLayout(new BorderLayout(5, 5)); // Espaciado externo menor
        setResizable(false);

        // 1. RESTRICCIÓN DE ID (Solo números + Alerta)
        configurarCampoID();

        // 2. BUSCAR PRIMER ID DISPONIBLE
        completarIDInicial();

        // --- Panel de Campos (Más juntos) ---
        JPanel pnlCampos = new JPanel(new GridBagLayout());
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(15, 30, 5, 30)); // Márgenes reducidos
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5); // Espaciado vertical reducido de 8 a 2

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        pnlCampos.add(new JLabel("ID " + tipo + ":"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlCampos.add(txtID, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        pnlCampos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlCampos.add(txtNombre, gbc);

        // --- Botones ---
        JPanel pnlBotones = new JPanel(new BorderLayout());
        pnlBotones.setBorder(BorderFactory.createEmptyBorder(5, 30, 15, 30));
        JButton btnAceptar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> ejecutarGuardado());
        btnCancelar.addActionListener(e -> dispose());

        pnlBotones.add(btnCancelar, BorderLayout.WEST);
        pnlBotones.add(btnAceptar, BorderLayout.EAST);

        add(pnlCampos, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);

        this.setPreferredSize(new Dimension(270, 150)); // Altura reducida
        pack(); 
        setLocationRelativeTo(parent);
        
        // Foco al nombre si el ID ya está autocompletado
        SwingUtilities.invokeLater(() -> txtNombre.requestFocusInWindow());
    }

    private void configurarCampoID() {
        ((AbstractDocument) txtID.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) { // Solo dígitos
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    new Alerta("Solo se permiten números en el campo ID", new Color(231, 76, 60), parentFrame);
                }
            }
        });
    }

    private void completarIDInicial() {
        try {
            DaoProducto dao = new DaoProducto();
            int siguienteID = dao.obtenerSiguienteIDDisponible(tablaDestino);
            txtID.setText(String.valueOf(siguienteID));
        } catch (Exception e) {
            txtID.setText(""); 
        }
    }

    private void ejecutarGuardado() {
        try {
            if (txtID.getText().trim().isEmpty()) throw new Exception("ID inválido");
            int id = Integer.parseInt(txtID.getText().trim());
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) throw new Exception("Nombre vacío");

            DaoProducto dao = new DaoProducto();
            
            // Validar si el ID ya existe antes de insertar
            if (dao.existeID(tablaDestino, id)) {
                new Alerta("El ID " + id + " ya está asignado", new Color(231, 76, 60), parentFrame);
                completarIDInicial(); // Sugerir el siguiente disponible
                return;
            }

            if (dao.insertar(tablaDestino, id, nombre)) {
                aceptado = true;
                new Alerta(tablaDestino.toUpperCase() + " guardado con éxito", new Color(46, 204, 113), parentFrame);
                dispose();
            }
        } catch (Exception e) {
            new Alerta("Error: " + e.getMessage(), new Color(231, 76, 60), parentFrame);
        }
    }

    public boolean isAceptado() { return aceptado; }
}