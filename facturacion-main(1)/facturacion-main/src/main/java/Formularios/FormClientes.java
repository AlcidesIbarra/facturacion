/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;
import Controlador.ControladorCliente;
import Controlador.ControladorFacturas;
import Controlador.ControladorVenta;
import Modelos.Mensaje;
import Modelos.ModeloCliente;
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
public class FormClientes extends javax.swing.JInternalFrame {

    /**
     * Creates new form FormProducto
     */
    public FormClientes() {

        initComponents();
        Controlador.ControladorCliente objetoProducto = new Controlador.ControladorCliente();
        objetoProducto.ListaClientes(jTable2, 1, 
                null,activos.getSelectedItem().toString());
        ControladorAdministracion ca = new ControladorAdministracion();
jLabel2.setVisible(false);
        ca.cambiacolor(this);
        // ca.BotonesCrisitalPrim(btnGuardarCliente, Color.green, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton4, Color.RED, Color.black, Color.black);
          ca.BotonesCrisitalPrim(jButton2, Color.GREEN, Color.black, Color.black);
            ca.BotonesCrisitalPrim(editar, Color.BLUE, Color.black, Color.black);
                        ca.BotonesCrisitalPrim(jButton1, Color.BLUE, Color.black, Color.black);
             ca.BotonesCrisitalPrim(activar, Color.yellow, Color.black, Color.black);
        contenedorClientes.setVisible(false);
        activar.setVisible(false);
    jPanel1.setComponentZOrder(contenedorClientes, 0);
    jPanel1.repaint();
    
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
    if (alertaTimer != null && alertaTimer.isRunning()) alertaTimer.stop();
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
        case 1: txtTitulo = "EXITO:"; colorFondo = new Color(154, 205, 50, 220); break;
        case 2: txtTitulo = "ATENCION:"; colorFondo = new Color(255, 120, 0, 220); break;
        case 3: txtTitulo = "ADVERTENCIA:"; colorFondo = new Color(255, 0, 0, 220); break;
        case 4: txtTitulo = "ERROR:"; colorFondo = new Color(150, 150, 150, 220); break;
        default: txtTitulo = "AVISO:"; colorFondo = new Color(100, 100, 100, 220);
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
        if (alertaTimer != null) alertaTimer.stop();
        if (tecladoGlobalListener != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(tecladoGlobalListener);
        }
    };

    glass.addMouseListener(new MouseAdapter() {
        @Override public void mousePressed(MouseEvent e) { cerrarAccion.run(); }
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

    public void abrirEditarCliente(int numer) {
        Integer cliente = -1;
        Controlador.ControladorCliente objetoProducto = new Controlador.ControladorCliente();
        if (numer == 2 || numer==4) {
            cliente = objetoProducto.editarCliente(jTable2);
        } else {
            cliente = 0;
        }
        if (cliente == -1) {
            mostrarAlerta("Selecciona un cliente para Editar", 2);
        } else {

            FormAgregarCliente frameB = new FormAgregarCliente(numer);
            Dimension dpSize = this.getDesktopPane().getSize();
            Dimension frSize = frameB.getSize();
            this.getDesktopPane().add(frameB); // Añade al escritorio padre

            int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
            int y = dpSize.height / 2 - (frSize.height / 2);

            frameB.setLocation(x - 10, y);
            frameB.setVisible(true);
            frameB.toFront(); // Trae al frente la nueva ventana
            frameB.editarCliente(cliente, jTable2,jLabel2);
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
        contenedorClientes = new javax.swing.JScrollPane();
        listaClientes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        buscaCliente = new javax.swing.JTextField();
        activos = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        editar = new javax.swing.JButton();
        activar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        comboOrden = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        contenedorClientes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                contenedorClientesFocusLost(evt);
            }
        });
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
        listaClientes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                listaClientesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                listaClientesFocusLost(evt);
            }
        });
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

        jPanel1.add(contenedorClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 170, 20));

        jLabel1.setText("Buscar:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jButton1.setText("Buscar");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 80, -1));

        jButton2.setText("Nuevo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, -1, -1));

        jButton4.setText("Eliminar");
        jButton4.setMaximumSize(new java.awt.Dimension(67, 32));
        jButton4.setMinimumSize(new java.awt.Dimension(67, 32));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 390, -1, -1));

        buscaCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscaClienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                buscaClienteFocusLost(evt);
            }
        });
        buscaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buscaClienteMouseClicked(evt);
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
        jPanel1.add(buscaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 170, -1));

        activos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVOS", "INACTIVOS" }));
        activos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                activosItemStateChanged(evt);
            }
        });
        jPanel1.add(activos, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 10, 130, -1));

        jLabel2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLabel2PropertyChange(evt);
            }
        });
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 40, 20));

        jScrollPane2.setPreferredSize(new java.awt.Dimension(453, 400));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 980, 350));

        editar.setText("Editar");
        editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarActionPerformed(evt);
            }
        });
        jPanel1.add(editar, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 390, -1, -1));

        activar.setText("Activar");
        activar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activarActionPerformed(evt);
            }
        });
        jPanel1.add(activar, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 390, -1, -1));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setText("LISTA CLIENTES");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 200, -1));

        comboOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Codigo", "Nombre", "Saldo", "Cuit", "Metodo Pago" }));
        comboOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboOrdenActionPerformed(evt);
            }
        });
        jPanel1.add(comboOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, 130, -1));

        jButton3.setText("Restablecer");
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 100, -1));

        jLabel4.setText("ordenar:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 10, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Controlador.ControladorCliente objetoProducto = new Controlador.ControladorCliente();
        Mensaje mj = objetoProducto.eliminarClienteSeleccionado(jTable2);

        if (mj != null) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        abrirEditarCliente(3);
    }//GEN-LAST:event_jButton2ActionPerformed
    boolean fucuseado = false;
    private void buscaClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscaClienteFocusGained
        fucuseado = true;
        buscaCliente.selectAll();
        System.out.println("gano foco");
    }//GEN-LAST:event_buscaClienteFocusGained

    private void buscaClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscaClienteFocusLost

      Controlador.ControladorCliente cc = new ControladorCliente();
       ModeloCliente cliente1 = cc.retornaUnCliente(buscaCliente.getText());
 

        if (abreSlide == true) {
                     
                if (cliente1 != null) {                             
                    buscaCliente.setText(cliente1.getNombre());
                }
                  
                if (!listaClientes.isColumnSelected(0)) {
                   contenedorClientes.setVisible(false);
                    if (!buscaCliente.getText().equals("")) {
                        fucuseado = false;
                     cc.ListaClientes(jTable2, 2, buscaCliente.getText(),activos.getSelectedItem().toString());

                    } else {
                        buscaCliente.setText("");
                    }
                }        
           
            abreSlide = false;
        }
       
    }//GEN-LAST:event_buscaClienteFocusLost

    private void buscaClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buscaClienteMouseClicked
        buscaCliente.setFocusable(true);
        boolean ejecutarFocusLostCliente = true;
        System.out.println("     se activo " + ejecutarFocusLostCliente);
    }//GEN-LAST:event_buscaClienteMouseClicked

    private void buscaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscaClienteActionPerformed

    }//GEN-LAST:event_buscaClienteActionPerformed

    private void buscaClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyPressed

    }//GEN-LAST:event_buscaClienteKeyPressed
    boolean abreSlide = false;
    private void buscaClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyReleased
           jPanel1.setComponentZOrder(contenedorClientes, 0);
    jPanel1.repaint();
        abreSlide = true;
        Controlador.ControladorCliente objetoCliente = new ControladorCliente();
        boolean returno = objetoCliente.buscarClientesinCF(buscaCliente, listaClientes,
                contenedorClientes,activos.getSelectedItem().toString());
        objetoCliente.ListaClientes(jTable2, 2, buscaCliente.getText(),activos.getSelectedItem().toString());

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
  contenedorClientes.setVisible(true);
            if (returno == false) {
                mostrarAlerta("No se encontraron coincidencias", 2);
                  contenedorClientes.setVisible(false);
            }

            if (returno == false && !buscaCliente.getText().equals("")) {
                //  contenedorClientes.setVisible(false);

                mostrarAlerta("No se encontraron coincidencias.", 2);
                buscaCliente.requestFocus();
                buscaCliente.selectAll();

                String codigo = buscaCliente.getText();
                buscaCliente.setText(codigo);
                  contenedorClientes.setVisible(false);
                
            }
            // JOptionPane.showMessageDialog(null, "enmterdetectado");
            if (buscaCliente.getText().equals("")) {

                ControladorVenta cv = new ControladorVenta();

            } else {
                listaClientes.changeSelection(0, 0, false, false);
                listaClientes.requestFocus();
            }
        }
        if (buscaCliente.getText().equals("")) {
            // Controlador.ControladorCliente cc = new ControladorCliente();
            contenedorClientes.setVisible(false);
        }

    }//GEN-LAST:event_buscaClienteKeyReleased

    private void buscaClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyTyped

    }//GEN-LAST:event_buscaClienteKeyTyped

    private void listaClientesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listaClientesFocusGained
        // fucuseado=true;
    }//GEN-LAST:event_listaClientesFocusGained

    private void listaClientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listaClientesFocusLost
        /*
        if (fucuseado == false) {
            // listaClientes.setVisible(false);
            contenedorClientes.setVisible(false);
            ControladorCliente cc = new ControladorCliente();
            Mensaje mj = cc.SeleccionarCliente(listaClientes, fieldNombre, fieldDni, fieldDireccion,
                fieldTelefono, buscaCliente, contenedorClientes,
                limiteCompra, deuda, txtBuscarProducto, metodoPago, paneObservaciones, dias,
                prCosto, texfieldStock, prVenta, saldoDisponible, 1,
                tbResumenVenta, total, labelCodCliente, codigoProducto,
                codigoProducto, contenedorTabla, descripcionProd, editaFacturar);

            if (mj != null) {
                mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            }

        }
        fucuseado = false;
         */
    }//GEN-LAST:event_listaClientesFocusLost

    private void listaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaClientesMouseClicked
        ControladorCliente objetoCliente = new ControladorCliente();
        objetoCliente.ListaClientes(jTable2, 2,
                listaClientes.getValueAt(listaClientes.getSelectedRow(), 1).toString(),
                activos.getSelectedItem().toString());
        contenedorClientes.setVisible(false);
        buscaCliente.setText(listaClientes.getValueAt(listaClientes.getSelectedRow(), 0).toString());
        buscaCliente.transferFocus();
    }//GEN-LAST:event_listaClientesMouseClicked

    private void listaClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaClientesKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            ControladorCliente objetoCliente = new ControladorCliente();
            objetoCliente.ListaClientes(jTable2, 2,
                    listaClientes.getValueAt(listaClientes.getSelectedRow(), 1).toString(),
                    activos.getSelectedItem().toString());
            contenedorClientes.setVisible(false);
            buscaCliente.setText(listaClientes.getValueAt(listaClientes.getSelectedRow(), 0).toString());
            buscaCliente.transferFocus();
        } else {
            ///   txtBuscarProducto.requestFocus();
        }
    }//GEN-LAST:event_listaClientesKeyPressed

    private void listaClientesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaClientesKeyReleased

    }//GEN-LAST:event_listaClientesKeyReleased

    private void contenedorClientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contenedorClientesFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_contenedorClientesFocusLost

    private void contenedorClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contenedorClientesKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_contenedorClientesKeyPressed

    private void activosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_activosItemStateChanged
     
        System.out.println("    entral ");
        ControladorCliente cc = new ControladorCliente();
        cc.ListaClientes(jTable2, 1, buscaCliente.getText(),activos.getSelectedItem().toString());
if(activos.getSelectedItem().toString().equals("INACTIVOS")){
activar.setVisible(true);
editar.setVisible(false);
}else{
    activar.setVisible(false);
editar.setVisible(true);
        }
    }//GEN-LAST:event_activosItemStateChanged

    private void jLabel2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLabel2PropertyChange
    if(!jLabel2.getText().equals("")){
     mostrarAlerta(jLabel2.getText(), 1);
                 ControladorCliente objetoCliente = new ControladorCliente();
          objetoCliente .ListaClientes(jTable2, 1, 
                null,activos.getSelectedItem().toString());
    }
    }//GEN-LAST:event_jLabel2PropertyChange

    private void editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarActionPerformed
        abrirEditarCliente(4);
    }//GEN-LAST:event_editarActionPerformed

    private void activarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activarActionPerformed
                Controlador.ControladorCliente cc = new ControladorCliente();
        Mensaje mj = cc.activarClienteSeleccionado(jTable2);

        if (mj != null) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
    }//GEN-LAST:event_activarActionPerformed

    private void comboOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboOrdenActionPerformed
   String sel = comboOrden.getSelectedItem().toString();
ControladorCliente cc = new ControladorCliente();
    // Ejemplo según tus requerimientos:
    if (sel.equals("Codigo")) {
       cc. ordenarTablaDesdeInterfaz(jTable2, 0, false); // Col 0, Menor a Mayor
    } else if (sel.equals("Nombre")) {
         cc. ordenarTablaDesdeInterfaz(jTable2, 1, false); // Col 1, A-Z
    } else if (sel.equals("Saldo")) {
        cc.  ordenarTablaDesdeInterfaz(jTable2, 3, true);  // Col 3, Mayor a Menor
    } else if (sel.equals("Cuit")) {
        cc.  ordenarTablaDesdeInterfaz(jTable2, 6, false); // Col 6, Menor a Mayor
    } else if (sel.equals("Metodo Pago")) {
        cc.  ordenarTablaDesdeInterfaz(jTable2, 8, false); // Col 6, Menor a Mayor
    }
    }//GEN-LAST:event_comboOrdenActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton activar;
    private javax.swing.JComboBox<String> activos;
    private javax.swing.JTextField buscaCliente;
    private javax.swing.JComboBox<String> comboOrden;
    private javax.swing.JScrollPane contenedorClientes;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable listaClientes;
    // End of variables declaration//GEN-END:variables
}
