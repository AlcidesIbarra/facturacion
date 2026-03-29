/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;
import Controlador.ControladorProducto;
import Controlador.ControladorVenta;
import Modelos.Mensaje;
import java.awt.AWTEvent;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 *
 * @author ALCIDES
 */
public class FormProducto extends javax.swing.JInternalFrame {

    /**
     * Creates new form FormProducto
     */
    public FormProducto(Integer numero) {

        initComponents();
        Controlador.ControladorProducto cp = new Controlador.ControladorProducto();
        lblVerificador.setText(numero.toString());
lblVerificador.setVisible(false);

        if (numero == 2) {
            jButton2.setVisible(false);
            activos.setEnabled(false);
            cp.muestraProductosKg(jTable1, 1,
                    txtBuscarProducto.getText(), activos.getSelectedItem().toString());
            comboOrdenProductos1.setVisible(false);
        } else {
              comboOrdenProductos.setVisible(false);
            cp.muestraProductos(jTable1, 1,
                    txtBuscarProducto.getText(), activos.getSelectedItem().toString());
        }

        activar.setVisible(false);
        contenedorTabla.setVisible(false);

        ControladorAdministracion ca = new ControladorAdministracion();
        jLabel2.setVisible(false);
        ca.cambiacolor(this);
        ca.BotonesCrisitalPrim(jButton1, Color.blue, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton2, Color.green, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton4, Color.RED, Color.black, Color.black);
        ca.BotonesCrisitalPrim(editar, Color.blue, Color.black, Color.black);
        ca.BotonesCrisitalPrim(activar, Color.yellow, Color.black, Color.black);

        Dimension d = new Dimension(80, 26); // El tamaño que quieras
        jButton1.setPreferredSize(d);
        jButton1.setMinimumSize(d);
        jButton1.setMaximumSize(d);

        jButton2.setPreferredSize(d);
        jButton2.setMinimumSize(d);
        jButton2.setMaximumSize(d);

        jButton4.setPreferredSize(d);
        jButton4.setMinimumSize(d);
        jButton4.setMaximumSize(d);

        editar.setPreferredSize(d);
        editar.setMinimumSize(d);
        editar.setMaximumSize(d);

        activar.setPreferredSize(d);
        activar.setMinimumSize(d);
        activar.setMaximumSize(d);

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

        JLabel contenido = new JLabel("<html><center>" + palabra + "</center></html>");
        contenido.setFont(new Font("consolass", Font.BOLD, 16));
        popup.setMinimumSize(new Dimension(320, 80));
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

    public void abrirEditarProducto(int numer) {

        Controlador.ControladorProducto objetoProducto = new Controlador.ControladorProducto();
        Integer cliente = objetoProducto.editarProducto(jTable1);

        if (numer == 2 || numer == 4) {
            cliente = objetoProducto.editarProducto(jTable1);
        } else {
            cliente = 0;
        }
        if (cliente == -1) {
            mostrarAlerta("Selecciona un producto para Editar", 2);
        } else {

            FormAgregarProducto frameB = new FormAgregarProducto(numer);
            Dimension dpSize = this.getDesktopPane().getSize();
            Dimension frSize = frameB.getSize();
            this.getDesktopPane().add(frameB); // Añade al escritorio padre

            int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
            int y = dpSize.height / 2 - (frSize.height / 2);

            frameB.setLocation(x - 10, y);
            frameB.setVisible(true);
            frameB.toFront(); // Trae al frente la nueva ventana
            frameB.editarProducto(cliente, jTable1, jLabel2);
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

        jPanel1 = new javax.swing.JPanel();
        contenedorTabla = new javax.swing.JScrollPane();
        listaProducto = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        editar = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        activos = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        txtBuscarProducto = new javax.swing.JTextField();
        activar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboOrdenProductos = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        lblVerificador = new javax.swing.JLabel();
        comboOrdenProductos1 = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listaProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        listaProducto.setAutoscrolls(false);
        listaProducto.setRowMargin(0);
        listaProducto.setShowHorizontalLines(false);
        listaProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                listaProductoFocusLost(evt);
            }
        });
        listaProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaProductoMouseClicked(evt);
            }
        });
        listaProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                listaProductoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                listaProductoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                listaProductoKeyTyped(evt);
            }
        });
        contenedorTabla.setViewportView(listaProducto);

        jPanel1.add(contenedorTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 170, 20));

        jButton2.setText("Nuevo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, -1, -1));

        editar.setText("Editar");
        editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarActionPerformed(evt);
            }
        });
        jPanel1.add(editar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 350, -1, -1));

        jButton4.setText("Eliminar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 350, -1, -1));

        jLabel2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLabel2PropertyChange(evt);
            }
        });
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, 110, 20));

        activos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVOS", "INACTIVOS" }));
        activos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                activosItemStateChanged(evt);
            }
        });
        jPanel1.add(activos, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 10, 130, -1));

        jButton1.setText("Buscar");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, -1, -1));

        txtBuscarProducto.setAutoscrolls(false);
        txtBuscarProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarProductoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarProductoFocusLost(evt);
            }
        });
        txtBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarProductoActionPerformed(evt);
            }
        });
        txtBuscarProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarProductoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarProductoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarProductoKeyTyped(evt);
            }
        });
        jPanel1.add(txtBuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 170, -1));

        activar.setText("Activar");
        activar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activarActionPerformed(evt);
            }
        });
        jPanel1.add(activar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 350, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 43, 890, 300));

        jLabel1.setText("Buscar:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setText("LISTA PRODUCTOS");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, 230, -1));

        comboOrdenProductos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Codigo", "Descripcion", "Stock", "Proveedor", "Rubro", "Precio Venta", "Mas Antiguo", "Mas Nuevo" }));
        comboOrdenProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboOrdenProductosActionPerformed(evt);
            }
        });
        jPanel1.add(comboOrdenProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 10, 100, -1));

        jLabel4.setText("ordenar:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, -1));

        lblVerificador.setText("1");
        jPanel1.add(lblVerificador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 20, -1));

        comboOrdenProductos1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Codigo", "Descripcion", "Stock", "Proveedor", "Rubro", "Precio Venta" }));
        comboOrdenProductos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboOrdenProductos1ActionPerformed(evt);
            }
        });
        jPanel1.add(comboOrdenProductos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 10, 100, -1));

        jButton3.setText("Imprimir");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarActionPerformed

        if (Integer.parseInt(lblVerificador.getText()) == 2) {
            ControladorProducto cp = new ControladorProducto();
            Mensaje mj = cp.editarKgSeleccionado(jTable1);
            if (mj != null) {
                if(mj.getMensaje()!=null){
             mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            } }
            cp.muestraProductosKg(jTable1, 1,
                    txtBuscarProducto.getText(), activos.getSelectedItem().toString());
        } else {
            abrirEditarProducto(4);
        }
    }//GEN-LAST:event_editarActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

               if (Integer.parseInt(lblVerificador.getText()) == 2) {
            ControladorProducto cp = new ControladorProducto();
            Mensaje mj = cp.eliminarProductoPorId(jTable1);
            if (mj != null) {
                if(mj.getMensaje()!=null){
             mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            } }
            cp.muestraProductosKg(jTable1, 1,
                    txtBuscarProducto.getText(), activos.getSelectedItem().toString());
        } else {
                
        Controlador.ControladorProducto objetoProducto = new Controlador.ControladorProducto();
        int codigo = objetoProducto.eliminarProducto(jTable1);
        mostrarAlerta("Se elimino con exito el producto: " + codigo, 1);
        }
        
        
        
  


    }//GEN-LAST:event_jButton4ActionPerformed

    private void jLabel2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLabel2PropertyChange
        if (!jLabel2.getText().equals("")) {
            mostrarAlerta(jLabel2.getText(), 1);
            Controlador.ControladorProducto cp = new Controlador.ControladorProducto();
            if (Integer.parseInt(lblVerificador.getText()) == 2) {
                cp.muestraProductosKg(jTable1, 1,
                        txtBuscarProducto.getText(), activos.getSelectedItem().toString());
            } else {
                cp.muestraProductos(jTable1, 1,
                        txtBuscarProducto.getText(), activos.getSelectedItem().toString());
            }
        }
    }//GEN-LAST:event_jLabel2PropertyChange

    private void activosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_activosItemStateChanged

        System.out.println("    entral ");
        ControladorProducto cp = new ControladorProducto();

        if (Integer.parseInt(lblVerificador.getText()) == 2) {
            cp.muestraProductosKg(jTable1, 1,
                    txtBuscarProducto.getText(), activos.getSelectedItem().toString());
        } else {
            cp.muestraProductos(jTable1, 1,
                    txtBuscarProducto.getText(), activos.getSelectedItem().toString());
        }

        if (activos.getSelectedItem().toString().equals("INACTIVOS")) {
            activar.setVisible(true);
            editar.setVisible(false);
        } else {
            activar.setVisible(false);
            editar.setVisible(true);
        }
    }//GEN-LAST:event_activosItemStateChanged

    private void listaProductoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listaProductoFocusLost
        /*
        if (fucuseadoprod == false) {

            contenedorTabla.setVisible(false);
            Controlador.ControladorVenta objetoVenta = new ControladorVenta();
            Mensaje mj = objetoVenta.SeleccionarProductosVenta(listaProducto, fieldCantidad,
                codigoProducto, txtBuscarProducto, prCosto, prVenta,
                buscaCliente, contenedorTabla, texfieldStock, descripcionProd, 2);
            if (mj != null) {
                mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            }

        }
        fucuseadoprod = false;
         */
    }//GEN-LAST:event_listaProductoFocusLost

    private void listaProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaProductoMouseClicked
        ControladorProducto cp = new ControladorProducto();

   
        contenedorTabla.setVisible(false);
        txtBuscarProducto.setText(listaProducto.getValueAt(listaProducto.getSelectedRow(), 0).toString());
        txtBuscarProducto.transferFocus();
        
             if (Integer.parseInt(lblVerificador.getText()) == 2) {
            cp.muestraProductosKg(jTable1, 1,
                    txtBuscarProducto.getText(), activos.getSelectedItem().toString());
        } else {
            cp.muestraProductos(jTable1, 1,
                    txtBuscarProducto.getText(), activos.getSelectedItem().toString());
        }

    }//GEN-LAST:event_listaProductoMouseClicked

    private void listaProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !txtBuscarProducto.getText().equals("")) {

            ControladorProducto cp = new ControladorProducto();
                 contenedorTabla.setVisible(false);
            txtBuscarProducto.setText(listaProducto.getValueAt(listaProducto.getSelectedRow(), 0).toString());
            txtBuscarProducto.transferFocus();
            if (Integer.parseInt(lblVerificador.getText()) == 2) {
                cp.muestraProductosKg(jTable1, 1,
                        txtBuscarProducto.getText(), activos.getSelectedItem().toString());
            } else {
                cp.muestraProductos(jTable1, 1,
                        txtBuscarProducto.getText(), activos.getSelectedItem().toString());
            }
       
        }

        System.out.println("    aqui piertde ");
    }//GEN-LAST:event_listaProductoKeyPressed

    private void listaProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_listaProductoKeyReleased

    private void listaProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_listaProductoKeyTyped

    private void txtBuscarProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarProductoFocusGained
        // fucuseadoprod = true;
        txtBuscarProducto.selectAll();
    }//GEN-LAST:event_txtBuscarProductoFocusGained

    private void txtBuscarProductoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarProductoFocusLost
        /*
        if (txtBuscarProducto.getText().equals("")) {
            codigoProducto.setText("");
            prCosto.setText("");
            prVenta.setText("");
            texfieldStock.setText("");
            descripcionProd.setText("");
            fieldCantidad.setText("");

            contenedorTabla.setVisible(false);
        }

        if (!listaProducto.isColumnSelected(0)) {
            contenedorTabla.setVisible(false);
            if (!txtBuscarProducto.getText().equals("")) {

                Controlador.ControladorVenta objetoVenta = new ControladorVenta();

                Mensaje mj = objetoVenta.SeleccionarProductosVentaFocusLost(listaProducto, fieldCantidad,
                    codigoProducto, txtBuscarProducto, prCosto, prVenta,
                    buscaCliente, contenedorTabla, texfieldStock, descripcionProd, 1);
                if (mj != null) {
                    mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                }

            } else {
                codigoProducto.setText("");
                prCosto.setText("");
                prVenta.setText("");
                texfieldStock.setText("");
                descripcionProd.setText("");
                fieldCantidad.setText("");
            }

            if (txtBuscarProducto.getText().equals("")) {
                contenedorTabla.setVisible(false);
            }
        }
         */
    }//GEN-LAST:event_txtBuscarProductoFocusLost

    private void txtBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProductoActionPerformed

    }//GEN-LAST:event_txtBuscarProductoActionPerformed

    private void txtBuscarProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyPressed

    }//GEN-LAST:event_txtBuscarProductoKeyPressed

    private void txtBuscarProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyReleased
        jPanel1.setComponentZOrder(contenedorTabla, 0);
        jPanel1.repaint();
        Controlador.ControladorVenta objetoVenta = new ControladorVenta();
        boolean returno = objetoVenta.buscarProducto(txtBuscarProducto, listaProducto, contenedorTabla, activos.getSelectedItem().toString());
        ControladorProducto cp = new ControladorProducto();

        if (Integer.parseInt(lblVerificador.getText()) == 2) {
            cp.muestraProductosKg(jTable1, 1,
                    txtBuscarProducto.getText(), activos.getSelectedItem().toString());
        } else {
            cp.muestraProductos(jTable1, 1,
                    txtBuscarProducto.getText(), activos.getSelectedItem().toString());
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !txtBuscarProducto.getText().equals("")) {
            //    fucuseadoprod = false;
            listaProducto.changeSelection(0, 0, false, false);
            listaProducto.requestFocus();

            if (returno == false) {
                contenedorTabla.setVisible(false);
                mostrarAlerta("No se encontraron coincidencias.", 2);
                txtBuscarProducto.requestFocus();
                txtBuscarProducto.selectAll();

            }
            if (txtBuscarProducto.getText().equals("")) {
                txtBuscarProducto.requestFocus();
            } else {
                listaProducto.changeSelection(0, 0, false, false);
                listaProducto.requestFocus();
            }

        }

        if (txtBuscarProducto.getText().equals("")) {
            contenedorTabla.setVisible(false);
        }
    }//GEN-LAST:event_txtBuscarProductoKeyReleased

    private void txtBuscarProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyTyped

    }//GEN-LAST:event_txtBuscarProductoKeyTyped

    private void activarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activarActionPerformed
        Controlador.ControladorProducto objetoProducto = new Controlador.ControladorProducto();
        Mensaje mj = objetoProducto.activarProductoSeleccionado(jTable1);

        if (mj != null) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
    }//GEN-LAST:event_activarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        abrirEditarProducto(3);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void comboOrdenProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboOrdenProductosActionPerformed
        String sel = comboOrdenProductos.getSelectedItem().toString();
        ControladorProducto cp = new ControladorProducto();
        switch (sel) {
            case "Codigo":
                cp.ordenarTablaProductos(jTable1, 0, false); // Menor a Mayor
                break;
            case "Descripcion":
                cp.ordenarTablaProductos(jTable1, 1, false); // A - Z
                break;
            case "Stock":
                cp.ordenarTablaProductos(jTable1, 2, true);  // Mayor Stock arriba
                break;
            case "Proveedor":
                cp.ordenarTablaProductos(jTable1, 6, false); // A - Z
                break;
            case "Rubro":
                cp.ordenarTablaProductos(jTable1, 7, false); // A - Z
                break;
            case "Precio Venta":
                cp.ordenarTablaProductos(jTable1, 4, true);  // Más caro arriba
                break;
                      case "Mas Antiguo":
                cp.ordenarTablaProductos(jTable1, 3, false);  // Más caro arriba
                break;
                      case "Mas Nuevo":
                cp.ordenarTablaProductos(jTable1, 3, true);  // Más caro arriba
                break;
        }
    }//GEN-LAST:event_comboOrdenProductosActionPerformed

    private void comboOrdenProductos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboOrdenProductos1ActionPerformed
           String sel = comboOrdenProductos1.getSelectedItem().toString();
        ControladorProducto cp = new ControladorProducto();
        switch (sel) {
            case "Codigo":
                cp.ordenarTablaProductos(jTable1, 0, false); // Menor a Mayor
                break;
            case "Descripcion":
                cp.ordenarTablaProductos(jTable1, 1, false); // A - Z
                break;
            case "Stock":
                cp.ordenarTablaProductos(jTable1, 4, true);  // Mayor Stock arriba
                break;
            case "Proveedor":
                cp.ordenarTablaProductos(jTable1, 6, false); // A - Z
                break;
            case "Rubro":
                cp.ordenarTablaProductos(jTable1, 7, false); // A - Z
                break;
            case "Precio Venta":
                cp.ordenarTablaProductos(jTable1, 3, true);  // Más caro arriba
                break;
                   
        }
    }//GEN-LAST:event_comboOrdenProductos1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    // 1. Obtener fecha actual
Date fechaActual = new Date();

// 2. Definir formato
SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

// 3. Convertir a String y setear en el JLabel
jLabel1.setText(formato.format(fechaActual));
        ControladorAdministracion ca = new ControladorAdministracion();
        
              if (Integer.parseInt(lblVerificador.getText())==2) {

     ca.ImprimirTabla("LISTA DE STOCK A LA FECHA:", formato.format(fechaActual), jTable1);
     
        } else {
 
     ca.ImprimirTabla("LISTA DE PRODUTOS A LA FECHA:", formato.format(fechaActual), jTable1);
     
        }

     
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton activar;
    private javax.swing.JComboBox<String> activos;
    private javax.swing.JComboBox<String> comboOrdenProductos;
    private javax.swing.JComboBox<String> comboOrdenProductos1;
    private javax.swing.JScrollPane contenedorTabla;
    private javax.swing.JButton editar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblVerificador;
    private javax.swing.JTable listaProducto;
    private javax.swing.JTextField txtBuscarProducto;
    // End of variables declaration//GEN-END:variables
}
