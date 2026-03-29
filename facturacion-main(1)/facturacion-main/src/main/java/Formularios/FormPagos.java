/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;
import Controlador.ControladorCliente;
import Controlador.ControladorPagos;
import Controlador.ControladorVenta;
import Modelos.Mensaje;
import java.awt.AWTEvent;
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
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ALCIDES
 */
public class FormPagos extends javax.swing.JInternalFrame {

    /**
     * Creates new form pagos
     */
    public FormPagos() {
        initComponents();

        //  contenedorTabla.setVisible(false);
        contenedorClientes.setVisible(false);
        this.requestFocus();
        buscaCliente.requestFocus();
//chkAnterior.setVisible(false);
        ControladorAdministracion ca = new ControladorAdministracion();

        // Aplicamos el UI global primero
        ca.cambiacolor(this);
        ca.BotonesCrisitalPrim(jButton2, Color.blue, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton3, Color.red, Color.black, Color.black);
        ca.BotonesCrisitalPrim(btnConfirmar, Color.green, Color.black, Color.black);

        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tabla.getColumnModel().getColumn(0).setPreferredWidth(55);

        tabla.getColumnModel().getColumn(1).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(133);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(133);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(133);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(133);
        DefaultTableCellRenderer cent = new DefaultTableCellRenderer();

        cent.setHorizontalAlignment(JLabel.CENTER);

        tabla.getColumnModel().getColumn(0).setCellRenderer(cent);

        //  DefaultTableCellRenderer cent1=new  DefaultTableCellRenderer();
        //    cent1.setHorizontalAlignment(SwingConstants.RIGHT);
        //    cent1.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
        tabla.getColumnModel().getColumn(3).setCellRenderer(new paddingRig());
        tabla.getColumnModel().getColumn(4).setCellRenderer(new paddingRig());
        tabla.getColumnModel().getColumn(5).setCellRenderer(new paddingRig());
        tabla.getColumnModel().getColumn(6).setCellRenderer(new paddingRig());

        Dimension d = new Dimension(80, 26); // El tamaño que quieras
        btnConfirmar.setPreferredSize(d);
        btnConfirmar.setMinimumSize(d);
        btnConfirmar.setMaximumSize(d);

        jButton2.setPreferredSize(d);
        jButton2.setMinimumSize(d);
        jButton2.setMaximumSize(d);

        jButton3.setPreferredSize(d);
        jButton3.setMinimumSize(d);
        jButton3.setMaximumSize(d);
    }

    public class paddingRig extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.RIGHT);
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            return this;
        }
    }

    public class paddingleft extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            return this;
        }
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        contenedorClientes = new javax.swing.JScrollPane();
        listaClientes = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        buscaCliente = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        nombre = new javax.swing.JLabel();
        cuit = new javax.swing.JLabel();
        deuda = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        pagoCliente = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        labelVuelto = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        btnConfirmar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cliente = new javax.swing.JLabel();
        metodoPago = new javax.swing.JComboBox<>();
        chkAnterior = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        deuda1 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Ingresar Pagos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 294, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        contenedorClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                contenedorClientesKeyPressed(evt);
            }
        });

        listaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        listaClientes.setAutoscrolls(false);
        listaClientes.setRowMargin(0);
        listaClientes.setShowHorizontalLines(false);
        listaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaClientesMouseClicked(evt);
            }
        });
        listaClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                listaClientesKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                listaClientesKeyReleased(evt);
            }
        });
        contenedorClientes.setViewportView(listaClientes);

        jPanel3.add(contenedorClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 170, 20));

        jLabel17.setText("Nombre:");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel19.setText("Buscar:");
        jPanel3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel18.setText("CUIT / DNI:");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        buscaCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                buscaClienteFocusLost(evt);
            }
        });
        buscaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscaClienteActionPerformed(evt);
            }
        });
        buscaCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                buscaClienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscaClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                buscaClienteKeyTyped(evt);
            }
        });
        jPanel3.add(buscaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 170, -1));

        jButton2.setText("Buscar");
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, -1, 30));

        jLabel11.setText("Saldo:");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 40, -1, -1));

        nombre.setText(" ");
        jPanel3.add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 220, -1));

        cuit.setText(" ");
        jPanel3.add(cuit, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 160, -1));

        deuda.setText(" ");
        jPanel3.add(deuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 40, 190, -1));

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "FACT", "FECHA", "VENCIMIENTO", "IMPORTE", "ADELANTO", "PAGOS", "SALDO TOTAL"
            }
        ));
        jScrollPane2.setViewportView(tabla);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 780, 290));

        jLabel15.setText("Abona con:");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, -1, -1));

        pagoCliente.setText(" ");
        pagoCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pagoClienteFocusGained(evt);
            }
        });
        pagoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pagoClienteActionPerformed(evt);
            }
        });
        pagoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pagoClienteKeyReleased(evt);
            }
        });
        jPanel3.add(pagoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, 220, -1));

        jLabel16.setText("Su vuelto:");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 380, -1, -1));

        labelVuelto.setText(" ");
        jPanel3.add(labelVuelto, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 380, 140, -1));

        jButton3.setText("Cancelar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, -1, -1));

        btnConfirmar.setText("Aceptar");
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });
        jPanel3.add(btnConfirmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 410, -1, -1));

        jLabel1.setText("Codigo:");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, -1, -1));

        cliente.setText("  ");
        jPanel3.add(cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, 40, -1));

        metodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Transferecia", "Tarjeta" }));
        metodoPago.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                metodoPagoItemStateChanged(evt);
            }
        });
        jPanel3.add(metodoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 10, -1, -1));

        chkAnterior.setText("Ingresar Saldo anterior");
        jPanel3.add(chkAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 410, -1, -1));

        jLabel2.setText("Saldo Facturas:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, -1, -1));

        deuda1.setText("----");
        jPanel3.add(deuda1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 60, 190, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buscaClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyTyped
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            listaClientes.requestFocus();
        }
    }//GEN-LAST:event_buscaClienteKeyTyped

    private void buscaClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyReleased

        Controlador.ControladorCliente objetoCliente = new ControladorCliente();
        boolean returno = objetoCliente.buscarCliente(buscaCliente, listaClientes, contenedorClientes);

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (returno == false) {
                mostrarAlerta("No se encontraron coincidencias", 2);
            }

            if (returno == false || buscaCliente.getText().equals("")) {
                contenedorClientes.setVisible(false);
                String codigo = buscaCliente.getText();
                nombre.setText("----");
                deuda.setText("----");
                cliente.setText("----");
                cuit.setText("----");

                buscaCliente.setText(codigo);
                DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
                modelo.setRowCount(0);
            }

            objetoCliente.buscarCliente(buscaCliente,
                    listaClientes, contenedorClientes);
            listaClientes.changeSelection(0, 0, false, false);
            listaClientes.requestFocus();
        }
        if (buscaCliente.getText().equals("")) {
            // Controlador.ControladorCliente cc = new ControladorCliente();
            contenedorClientes.setVisible(false);
        }

    }//GEN-LAST:event_buscaClienteKeyReleased

    private void buscaClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyPressed

    }//GEN-LAST:event_buscaClienteKeyPressed

    private void buscaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscaClienteActionPerformed

    }//GEN-LAST:event_buscaClienteActionPerformed

    private void contenedorClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contenedorClientesKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_contenedorClientesKeyPressed

    private void listaClientesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaClientesKeyReleased

    }//GEN-LAST:event_listaClientesKeyReleased

    private void listaClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaClientesKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            Controlador.ControladorPagos objetoCliente = new ControladorPagos();
            objetoCliente.SeleccionarCliente(contenedorClientes, 
                    listaClientes, nombre, cuit, deuda,
                    tabla, pagoCliente, cliente,buscaCliente,deuda1);
        }
    }//GEN-LAST:event_listaClientesKeyPressed

    private void listaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaClientesMouseClicked
        Controlador.ControladorPagos objetoCliente = new ControladorPagos();
        objetoCliente.SeleccionarCliente(contenedorClientes, 
                listaClientes, nombre, cuit, 
                deuda, tabla, pagoCliente, cliente,buscaCliente,deuda1);

    }//GEN-LAST:event_listaClientesMouseClicked

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed

        double valorAbona = 0.0;
        double valorDeuda = 0.0;

        try {
            // 1. Limpiamos y convertimos el PAGO
            String pagoLimpio = pagoCliente.getText().replaceAll("[^0-9,]", "").replace(",", ".");
            if (!pagoLimpio.isEmpty()) {
                valorAbona = Double.parseDouble(pagoLimpio);
            }

            // 2. Limpiamos y convertimos la DEUDA
            String deudaLimpia = deuda.getText().replaceAll("[^0-9,]", "").replace(",", ".");
            if (!deudaLimpia.isEmpty() && !deudaLimpia.equals("----")) {
                valorDeuda = Double.parseDouble(deudaLimpia);
            }

            // 3. VALIDACIÓN DE TOPE: No permitir que supere la deuda
            if (valorAbona > valorDeuda) {
                mostrarAlerta("El pago no puede ser mayor a la deuda ($ " + valorDeuda + ")", 2);

                // Seteamos el valor al máximo permitido
                valorAbona = valorDeuda;

                // Opcional: Actualizar el campo de texto visualmente para que coincida con el tope
                NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
                pagoCliente.setText(formato.format(valorAbona));
            }

        } catch (Exception e) {
            System.out.println("Error en validación: " + e.getMessage());
        }

        ControladorPagos cp = new ControladorPagos();
        Mensaje mj = cp.realizarPago(valorAbona, tabla, deuda, 
                cliente,metodoPago.getSelectedItem().toString(),chkAnterior,labelVuelto);
             Double facturasDeuda=  cp.listarDeudaCliente(Integer.parseInt(cliente.getText()), tabla);
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
           
             deuda1.setText(formatoMoneda.format(facturasDeuda));
        if (mj != null) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
     
        }
      
            pagoCliente.setText("");
            labelVuelto.setText("----");
      
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void buscaClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscaClienteFocusLost
        
        // 1. Verificamos si hay texto y si la lista tiene resultados
        if (!buscaCliente.getText().trim().equals("") && listaClientes.getRowCount() > 0) {

            // 2. Si no hay nada seleccionado, forzamos la selección de la primera fila (índice 0)
            if (listaClientes.getSelectedRow() == -1) {
                listaClientes.setRowSelectionInterval(0, 0);
            }

            // 3. Ahora que hay una fila seleccionada, llamamos a tu método original
            Controlador.ControladorPagos objetoPagos = new Controlador.ControladorPagos();
            objetoPagos.SeleccionarCliente(contenedorClientes, 
                    listaClientes, nombre, cuit, deuda, tabla, 
                    pagoCliente, cliente,buscaCliente,deuda1);

            // El contenedor se oculta dentro de SeleccionarCliente, así que no hace falta ponerlo acá
        } else {
            // Si sale del campo y está vacío, limpiamos y cerramos
            contenedorClientes.setVisible(false);
            nombre.setText("----");
            cuit.setText("----");
            deuda.setText("----");
            cliente.setText("----");
            ((DefaultTableModel) tabla.getModel()).setRowCount(0);
        }
        
    }//GEN-LAST:event_buscaClienteFocusLost

    private void pagoClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pagoClienteKeyReleased
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnConfirmar.requestFocus();
        }

        char c = evt.getKeyChar();
        String cadena = pagoCliente.getText();

// 1. Si no tiene el signo $, lo agregamos con un solo espacio
        if (!cadena.startsWith("$ ")) {
            // Quitamos cualquier $ o espacio previo para evitar duplicados y rearmamos
            String limpia = cadena.replace("$", "").trim();
            cadena = "$ " + limpia;
            pagoCliente.setText(cadena);
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

        BigDecimal totalAbonar = new BigDecimal(deuda.getText().substring(2).replace(".", "").replace(",", "."));
        System.out.println(abonaCliente + "            " + totalAbonar);

        int numero = 0;
        numero = abonaCliente.compareTo(totalAbonar);
        boolean verificadorSsaldo = false;

        if (numero < 0) {
            verificadorSsaldo = true;
            labelVuelto.setText("----");
        } else {
            if (metodoPago.getSelectedItem().toString().equals("Efectivo")) {
                BigDecimal vuelto = abonaCliente.subtract(totalAbonar);
                labelVuelto.setText(formatoMoneda.format(vuelto));
            }else{
            pagoCliente.setText(formatoMoneda.format(totalAbonar));
                  labelVuelto.setText("----");
            }
        }
        System.out.println(numero);


    }//GEN-LAST:event_pagoClienteKeyReleased

    private void pagoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pagoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pagoClienteActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        labelVuelto.setText("----");
        nombre.setText("----");
        cuit.setText("----");
        deuda.setText("----");
        cliente.setText("----");
        pagoCliente.setText("");
        buscaCliente.setText("");
        ((DefaultTableModel) tabla.getModel()).setRowCount(0);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void metodoPagoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_metodoPagoItemStateChanged
    if(!metodoPago.getSelectedItem().equals("Efectivo")){
    
           NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
     
          
        BigDecimal abonaCliente = new BigDecimal("0.0");

        try {

            abonaCliente = new BigDecimal(pagoCliente.getText().substring(2).replace(".", "").replace(",", "."));
        } catch (Exception e) {
        }

        BigDecimal totalAbonar = new BigDecimal(deuda.getText().substring(2).replace(".", "").replace(",", "."));
        System.out.println(abonaCliente + "            " + totalAbonar);

        int numero = 0;
        numero = abonaCliente.compareTo(totalAbonar);
        boolean verificadorSsaldo = false;

        if (numero < 0) {
            verificadorSsaldo = true;
            labelVuelto.setText("----");
        } else {
            if (metodoPago.getSelectedItem().toString().equals("Efectivo")) {
                BigDecimal vuelto = abonaCliente.subtract(totalAbonar);
                labelVuelto.setText(formatoMoneda.format(vuelto));
            }else{
            pagoCliente.setText(formatoMoneda.format(totalAbonar));
             labelVuelto.setText("----");
            }
        }
        System.out.println(numero);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
    }//GEN-LAST:event_metodoPagoItemStateChanged

    private void pagoClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pagoClienteFocusGained
   pagoCliente.selectAll();
    }//GEN-LAST:event_pagoClienteFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JTextField buscaCliente;
    private javax.swing.JCheckBox chkAnterior;
    private javax.swing.JLabel cliente;
    private javax.swing.JScrollPane contenedorClientes;
    private javax.swing.JLabel cuit;
    private javax.swing.JLabel deuda;
    private javax.swing.JLabel deuda1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelVuelto;
    private javax.swing.JTable listaClientes;
    private javax.swing.JComboBox<String> metodoPago;
    private javax.swing.JLabel nombre;
    private javax.swing.JTextField pagoCliente;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
