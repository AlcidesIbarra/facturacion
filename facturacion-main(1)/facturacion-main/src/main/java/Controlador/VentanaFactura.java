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
    
    // Almacena el nombre para seleccionarlo en el combo al cerrar
    private String nombreGuardado = "";

    public VentanaFactura(Frame parent, boolean modal, JInternalFrame parentFrame, String tipo) {
        super(parent, "Registrar " + tipo.toUpperCase(), modal);
        this.parentFrame = parentFrame;
        this.tablaDestino = tipo.toLowerCase();
        
        setLayout(new BorderLayout(5, 5)); 
        setResizable(false);

        configurarCampoID();
        completarIDInicial();

        JPanel pnlCampos = new JPanel(new GridBagLayout());
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(15, 30, 5, 30)); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5); 

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        pnlCampos.add(new JLabel("ID " + tipo + ":"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlCampos.add(txtID, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        pnlCampos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlCampos.add(txtNombre, gbc);

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

        this.setPreferredSize(new Dimension(270, 150)); 
        pack(); 
        setLocationRelativeTo(parent);
        
        SwingUtilities.invokeLater(() -> txtNombre.requestFocusInWindow());
    }

    private void configurarCampoID() {
        ((AbstractDocument) txtID.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) { 
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    new Alerta("Solo se permiten números", new Color(231, 76, 60), parentFrame);
                }
            }
        });
    }

    private void completarIDInicial() {
        try {
            // Usamos el método de tu DAO para sugerir el ID
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
            if (nombre.isEmpty()) throw new Exception("El nombre no puede estar vacío");

            DaoProducto dao = new DaoProducto();
            
            // 1. Validar si el ID ya existe en la tabla (rubro o proveedor)
            if (dao.existeID(tablaDestino, id)) {
                new Alerta("El ID " + id + " ya existe", new Color(231, 76, 60), parentFrame);
                completarIDInicial(); // Refrescamos sugerencia
                return;
            }

            // 2. Intentar la inserción
            if (dao.insertar(tablaDestino, id, nombre)) {
                this.nombreGuardado = nombre; // Guardamos para el foco posterior
                this.aceptado = true;
                new Alerta(tablaDestino.toUpperCase() + " guardado con éxito", new Color(46, 204, 113), parentFrame);
                dispose();
            } else {
                throw new Exception("No se pudo insertar el registro");
            }
            
        } catch (NumberFormatException e) {
            new Alerta("ID debe ser numérico", new Color(231, 76, 60), parentFrame);
        } catch (Exception e) {
            new Alerta(e.getMessage(), new Color(231, 76, 60), parentFrame);
        }
    }

    public String getNombreGuardado() {
        return nombreGuardado;
    }

    public boolean isAceptado() { 
        return aceptado; 
    }
}