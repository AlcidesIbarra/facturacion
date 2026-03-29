/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;
import Controlador.ControladorVenta;
import java.awt.AWTEvent;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.DesktopPaneUI;
import javax.swing.text.Document;

/**
 *
 * @author ALCIDES
 */
public class FormFacturar extends javax.swing.JInternalFrame {

    /**
     * Creates new form FormFacturar
     */
    public FormFacturar() {

        initComponents();
        labelCodCliente.setVisible(false);

        nombreFacturar.setHorizontalAlignment(SwingConstants.CENTER);
        ControladorAdministracion ca = new ControladorAdministracion();
        ca.BotonesCrisitalPrim(btnConfirmar, Color.green, Color.black, Color.black);

        ca.BotonesCrisitalPrim(jButton1, Color.red, Color.black, Color.black);

        Dimension d = new Dimension(80, 24); // El tamaño que quieras
        btnConfirmar.setPreferredSize(d);
        btnConfirmar.setMinimumSize(d);
        btnConfirmar.setMaximumSize(d);

        jButton1.setPreferredSize(d);
        jButton1.setMinimumSize(d);
        jButton1.setMaximumSize(d);
    }
    private Timer alertaTimer = null;
    private AWTEventListener tecladoGlobalListener = null;

    public void mostrarAlerta(String palabra, Integer codigo) {
        final JPanel glass = (JPanel) this.getGlassPane();

        // 1. Limpieza de procesos anteriores
        if (alertaTimer != null && alertaTimer.isRunning()) {
            alertaTimer.stop();
        }
        if (tecladoGlobalListener != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(tecladoGlobalListener);
        }

        glass.removeAll();
        glass.setLayout(new GridBagLayout()); // Usamos GridBag para posicionar arriba

        // 2. Configuración de Posición (Arriba)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH; // Lo pega al techo
        gbc.insets = new Insets(20, 0, 0, 0);  // Margen de 20px desde arriba
        gbc.weighty = 1.0; // Empuja el resto del espacio hacia abajo

        // 3. Crear el Popup
        JPanel popup = new JPanel();
        popup.setLayout(new BoxLayout(popup, BoxLayout.Y_AXIS));
        // Borde redondeado original de tu código
        popup.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 8, true));

        // 4. Cargar Icono (Ruta relativa corregida)
        // Cambiá "advertencia.png" por el nombre exacto de tu archivo en src
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/main/Imagenes/advertencia.png"));
        Image imgEscalada = iconoOriginal.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
        ImageIcon icono = new ImageIcon(imgEscalada);

        String txtTitulo = "";
        Color colorFondo;
        switch (codigo) {
            case 1:
                txtTitulo = "EXITO:";
                colorFondo = new Color(154, 205, 50, 220);
                break;
            case 2:
                txtTitulo = "ATENCION:";
                colorFondo = new Color(255, 120, 0, 220);
                break;
            case 3:
                txtTitulo = "ADVERTENCIA:";
                colorFondo = new Color(255, 0, 0, 220);
                break;
            case 4:
                txtTitulo = "ERROR:";
                colorFondo = new Color(150, 150, 150, 220);
                break;
            default:
                txtTitulo = "AVISO:";
                colorFondo = new Color(100, 100, 100, 220);
        }
        popup.setBackground(colorFondo);

        // 5. Etiquetas con tu fuente original "consolass"
        JLabel titulo = new JLabel(txtTitulo, icono, JLabel.LEFT);
        titulo.setFont(new Font("consolass", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

   JLabel contenido = new JLabel("<html><center>"+palabra+"</center></html>");
    contenido.setFont(new Font("consolass", Font.BOLD, 16));
    popup.setMinimumSize(new Dimension(320,80));
        contenido.setAlignmentX(Component.CENTER_ALIGNMENT);

        popup.add(titulo);
        popup.add(contenido);

        glass.add(popup, gbc); // Agregamos con la configuración de posición
        glass.setVisible(true);
        glass.revalidate();
        glass.repaint();

        // 6. Lógica de Cierre (Click, Tecla o Tiempo)
        Runnable cerrarAccion = () -> {
            glass.setVisible(false);
            glass.removeAll();
            if (alertaTimer != null) {
                alertaTimer.stop();
            }
            if (tecladoGlobalListener != null) {
                Toolkit.getDefaultToolkit().removeAWTEventListener(tecladoGlobalListener);
            }
        };

        glass.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                cerrarAccion.run();
            }
        });

        tecladoGlobalListener = event -> {
            if (event instanceof KeyEvent && event.getID() == KeyEvent.KEY_PRESSED) {
                cerrarAccion.run();
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(tecladoGlobalListener, AWTEvent.KEY_EVENT_MASK);

        alertaTimer = new Timer(5000, e -> cerrarAccion.run());
        alertaTimer.setRepeats(false);
        alertaTimer.start();
    }

    JTable tbrespaldo = new JTable();
    JDesktopPane conten = new JDesktopPane();
    BigDecimal cuentaCliente = null;
    JLabel mensajeador1=null;

    public void probarDos(BigDecimal total, String FormaPago, String nombre,
            BigDecimal CuentaCorriente, String Observaciones, Integer codCliente,
            JTable listaProductos, JDesktopPane contenedor,JLabel mensajeador) {
        cuentaCliente = CuentaCorriente;
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        conten = contenedor;
        tbrespaldo = listaProductos;
        mensajeador1=mensajeador;
        paneObservaciones.setContentType("text/html");
        paneObservaciones.setText("<html><body><p style='margin:2px; text-align: center;"
                + "line-height:1; font-size:9px; line-height:1; font-family:consolass; '>" + Observaciones
                + "</p></body></html>");
        if (codCliente != null && codCliente!=1) {
            labelCodCliente.setText(codCliente.toString());
        } else {
            metodoPago.removeItem("Cuenta Corriente");
        }

        montoPagar.setText(formatoMoneda.format(total));
        metodoPago.setSelectedItem(FormaPago);
        if (!nombre.equals("")) {
            nombreFacturar.setText(nombre);
        }
        System.out.println(FormaPago);
        switch (FormaPago) {
            case "Contado":
                pagoCliente.setText(formatoMoneda.format(total));
                pagoCliente.requestFocus();
                pagoCliente.selectAll();
                break;
            case "Cuenta Corriente":

                textAbona.setText("Seña / Adelanto");
                pagoCliente.requestFocus();
                pagoCliente.selectAll();
                LabelVuelto1.setText("Nuevo Saldo:");

                labelCuentaCorr.setText(formatoMoneda.format(CuentaCorriente));

                Double nuevaCuenta
                        = Double.parseDouble(labelCuentaCorr.getText().substring(2).replace(".", "").replace(",", "."))
                        + Double.parseDouble(montoPagar.getText().substring(2).replace(".", "").replace(",", "."));
                labelVuelto.setText(formatoMoneda.format(nuevaCuenta));

                break;
            case "Tarjeta":

                pagoCliente.setText(formatoMoneda.format(total));

                break;
            case "Transferencia":

                pagoCliente.setText(formatoMoneda.format(total));

                break;
            default:
                throw new AssertionError();
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contenedorObs = new javax.swing.JScrollPane();
        paneObservaciones1 = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        montoPagar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        metodoPago = new javax.swing.JComboBox<>();
        textAbona = new javax.swing.JLabel();
        pagoCliente = new javax.swing.JTextField();
        LabelVuelto1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        labelCuentaCorr = new javax.swing.JLabel();
        nombreFacturar = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        labelCodCliente = new javax.swing.JLabel();
        btnConfirmar = new javax.swing.JButton();
        labelVuelto = new javax.swing.JLabel();
        paneObservaciones = new javax.swing.JTextPane();

        contenedorObs.setViewportView(paneObservaciones1);

        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        jLabel1.setText("IMPORTE:");

        jLabel2.setText("forma de pago:");

        metodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Contado", "Transferencia", "Cuenta Corriente", "Tarjeta" }));
        metodoPago.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                metodoPagoItemStateChanged(evt);
            }
        });
        metodoPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metodoPagoActionPerformed(evt);
            }
        });

        textAbona.setText("Abona con:");

        pagoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pagoClienteKeyReleased(evt);
            }
        });

        LabelVuelto1.setText("Su Vuelto:");

        jLabel6.setText("Cuenta Corriente:");

        labelCuentaCorr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelCuentaCorr.setText("----");

        nombreFacturar.setText("Consumidor Final");

        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnConfirmar.setText("Confirmar");
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });

        labelVuelto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelVuelto.setText("----");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paneObservaciones)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(metodoPago, 0, 134, Short.MAX_VALUE))
                    .addComponent(nombreFacturar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textAbona)
                            .addComponent(LabelVuelto1))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(pagoCliente))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(labelVuelto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(26, 26, 26)
                        .addComponent(montoPagar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(labelCuentaCorr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnConfirmar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(nombreFacturar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(montoPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(metodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textAbona)
                    .addComponent(pagoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(labelCuentaCorr))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelVuelto1)
                    .addComponent(labelVuelto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paneObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(btnConfirmar)))
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void metodoPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metodoPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_metodoPagoActionPerformed

    private void pagoClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pagoClienteKeyReleased
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnConfirmar.requestFocus();
        }
        char c = evt.getKeyChar();
        String cadena = pagoCliente.getText();

        if (!cadena.contains("$")) {

            cadena = "$ " + cadena;
            pagoCliente.setText(cadena);
        }
        if (!cadena.contains(" ")) {
            if (cadena.length() > 1) {
                StringBuilder sb = new StringBuilder(cadena);
                sb.insert(1, " ");
                pagoCliente.setText(sb.toString());
            }

        }
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {
            if (Character.isDigit(c) || c == '.') {
                cadena = cadena.substring(0, cadena.length() - 1);

                if (c == '.' && cadena.contains(".")) {

                    pagoCliente.setText(cadena);
                }
            } else {
                mostrarAlerta("solo se aceptan numeros para este campo", 2);
                cadena = pagoCliente.getText();
                if (cadena.length() > 1) {
                    cadena = cadena.substring(0, cadena.length() - 1);
                } else {
                    cadena = "";
                }
                pagoCliente.setText(cadena);
                pagoCliente.requestFocus();
            }
        }
        BigDecimal abonaCliente = new BigDecimal("0.0");
        try {

            abonaCliente = new BigDecimal(pagoCliente.getText().substring(2).replace(".", "").replace(",", "."));
        } catch (Exception e) {
        }

        BigDecimal totalAbonar = new BigDecimal(montoPagar.getText().substring(2).replace(".", "").replace(",", "."));
        System.out.println(abonaCliente + "            " + totalAbonar);

        int numero = 0;
        numero = abonaCliente.compareTo(totalAbonar);
        boolean verificadorSsaldo = false;

        if (numero < 0) {
            verificadorSsaldo = true;
            labelVuelto.setText("----");
        } else {
            if (metodoPago.getSelectedItem().toString().equals("Contado")) {
                verificadorSsaldo = false;

                BigDecimal vuelto = abonaCliente.subtract(totalAbonar);
                labelVuelto.setText(formatoMoneda.format(vuelto));
            }
        }
        System.out.println(numero);

        if (metodoPago.getSelectedItem().toString().equals("Cuenta Corriente")) {

            Double pagoCliented = 0d;
            try {
                pagoCliented = Double.parseDouble(pagoCliente.getText().substring(2).replace(".", "").replace(",", "."));
            } catch (Exception e) {
            }

            Double nuevaCuenta
                    = Double.parseDouble(labelCuentaCorr.getText().substring(2).replace(".", "").replace(",", ".")) - pagoCliented
                    + Double.parseDouble(montoPagar.getText().substring(2).replace(".", "").replace(",", "."));
            System.out.println(nuevaCuenta + "cuenta nueva");
            labelVuelto.setText(formatoMoneda.format(nuevaCuenta));
        }

    }//GEN-LAST:event_pagoClienteKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            this.setClosed(true);        // TODO add your handling code here:
        } catch (PropertyVetoException ex) {
            Logger.getLogger(FormFacturar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        try {
            Double cuenta = 0d;
            try {
                cuenta = Double.parseDouble(labelCuentaCorr.getText().substring(2).replace(".", "").replace(",", "."));

            } catch (Exception e) {
                cuenta = null;
            }
            Double pagocliente1 = 0d;
            try {
                pagocliente1 = Double.parseDouble(pagoCliente.getText().substring(2).replace(".", "").replace(",", "."));

            } catch (Exception e) {
                pagocliente1 = 0d;
            }

            ControladorVenta cv = new ControladorVenta();
            Document doc = paneObservaciones.getDocument();
            String obs = "";
            try {
                obs = doc.getText(0, doc.getLength());
            } catch (Exception e) {
            }
            int cliente = 0;
            try {
                cliente = Integer.parseInt(labelCodCliente.getText());
            } catch (Exception e) {
                cliente = 1;
            }
         if (pagocliente1 >= Double.parseDouble(montoPagar.getText().substring(2).replace(".", "").replace(",", ".")) 
    || metodoPago.getSelectedItem().toString().equalsIgnoreCase("CUENTA CORRIENTE")) {
    
    // 1. Guardamos los datos de la factura como ya lo hacías
    cv.guardarDatosConfirmar(metodoPago.getSelectedItem().toString(), pagocliente1,
            Double.parseDouble(montoPagar.getText().substring(2).replace(".", "").replace(",", ".")), obs,
            cuenta, cliente, tbrespaldo, conten, mensajeador1);

    // 2. ¡CLAVE! Pasamos todos los pesos de la tabla de 'RESERVADO' a 'VENDIDO'
    // Usamos 'tbrespaldo' que es tu JTable con los productos
    cv.finalizarPesosFacturados(tbrespaldo); 

    conten = null;
    tbrespaldo = null;
    
    // 3. Cerramos la ventana de facturación
    this.setClosed(true);
    
} else {
    mostrarAlerta("El pago es menor que la factura", 3);
}

// TODO add your handling code here:
        } catch (PropertyVetoException ex) {
            Logger.getLogger(FormFacturar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void metodoPagoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_metodoPagoItemStateChanged
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        String FormaPago = metodoPago.getSelectedItem().toString();
        System.out.println(FormaPago);
        switch (FormaPago) {
            case "Contado":
                textAbona.setText("Abona con:");
                LabelVuelto1.setText("Su Vuelto:");
                pagoCliente.setText(montoPagar.getText());
                pagoCliente.requestFocus();
                pagoCliente.selectAll();

                BigDecimal abonaCliente = new BigDecimal("0.0");
                try {

                    abonaCliente = new BigDecimal(pagoCliente.getText().substring(2).replace(".", "").replace(",", "."));
                } catch (Exception e) {
                }

                BigDecimal totalAbonar = new BigDecimal(montoPagar.getText().substring(2).replace(".", "").replace(",", "."));
                System.out.println(abonaCliente + "            " + totalAbonar);

                int numero = 0;
                numero = abonaCliente.compareTo(totalAbonar);
                boolean verificadorSsaldo = false;

                if (numero < 0) {
                    verificadorSsaldo = true;
                    labelVuelto.setText("----");
                } else {
                    verificadorSsaldo = false;

                    BigDecimal vuelto = abonaCliente.subtract(totalAbonar);
                    labelVuelto.setText(formatoMoneda.format(vuelto));
                }

                break;

            case "Cuenta Corriente":

                textAbona.setText("Seña / Adelanto");
                pagoCliente.setText("");
                pagoCliente.requestFocus();
                pagoCliente.selectAll();
                LabelVuelto1.setText("Nuevo Saldo:");

                labelCuentaCorr.setText(formatoMoneda.format(cuentaCliente));

                Double nuevaCuenta1
                        = Double.parseDouble(labelCuentaCorr.getText().substring(2).replace(".", "").replace(",", "."))
                        + Double.parseDouble(montoPagar.getText().substring(2).replace(".", "").replace(",", "."));
                labelVuelto.setText(formatoMoneda.format(nuevaCuenta1));

                break;
            case "Tarjeta":
                LabelVuelto1.setText("Su Vuelto:");
                labelCuentaCorr.setText(formatoMoneda.format(cuentaCliente));
                pagoCliente.setText(montoPagar.getText());

                break;

            case "Transferencia":
                LabelVuelto1.setText("Su Vuelto:");
                labelCuentaCorr.setText(formatoMoneda.format(cuentaCliente));
                pagoCliente.setText(montoPagar.getText());

                break;
            default:
                throw new AssertionError();
        }

    }//GEN-LAST:event_metodoPagoItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelVuelto1;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JScrollPane contenedorObs;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelCodCliente;
    private javax.swing.JLabel labelCuentaCorr;
    private javax.swing.JLabel labelVuelto;
    private javax.swing.JComboBox<String> metodoPago;
    private javax.swing.JTextField montoPagar;
    private javax.swing.JLabel nombreFacturar;
    private javax.swing.JTextField pagoCliente;
    private javax.swing.JTextPane paneObservaciones;
    private javax.swing.JTextPane paneObservaciones1;
    private javax.swing.JLabel textAbona;
    // End of variables declaration//GEN-END:variables
}
