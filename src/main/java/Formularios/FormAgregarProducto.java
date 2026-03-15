/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.Alerta;
import Controlador.ControladorAdministracion;
import Controlador.ControladorCliente;
import Controlador.ControladorProducto;
import Controlador.ControladorVenta;
import Controlador.DaoProducto;
import Modelos.Mensaje;
import Modelos.ModeloCliente;
import Modelos.ModeloProducto;
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
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import Modelos.ItemSeleccionable;

/**
 *
 * @author ALCIDES
 */
public class FormAgregarProducto extends javax.swing.JInternalFrame {

    /**
     * Creates new form formAgregarProducto
     */
    public FormAgregarProducto(Integer verificador1) {
        initComponents();
        ControladorAdministracion ca = new ControladorAdministracion();
verificador.setVisible(false);
        ca.cambiacolor(this);
        ca.BotonesCrisitalPrim(btnGuardarProducto, Color.green, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton2, Color.RED, Color.black, Color.black);
        contenedorTabla.setVisible(false);
        txtBuscarProducto.setVisible(false);
        jLabel8.setHorizontalAlignment(SwingConstants.CENTER);

       DaoProducto dp = new DaoProducto();
        dp.cargarComboGenerico(cbxRubro, 1);

// Cargar Proveedores (Tipo 2)
dp.cargarComboGenerico(cbxProveedor, 2);
        
        ControladorProducto cc = new ControladorProducto();
        cc.verCodigoDisponlible(codigoProducto);
        if (verificador1 == 2 || verificador1 == 4) {
            verificador.setText(verificador1.toString());
            jLabel8.setText("EDITAR PRODUCTO");
            txtBuscarProducto.setVisible(true);
            codigoProducto.setVisible(false);
        }
        verificador.setText(verificador1.toString());
        
        
          Dimension d = new Dimension(80, 26); // El tamaño que quieras
jButton2.setPreferredSize(d);
jButton2.setMinimumSize(d);
jButton2.setMaximumSize(d);

btnGuardarProducto.setPreferredSize(d);
btnGuardarProducto.setMinimumSize(d);
btnGuardarProducto.setMaximumSize(d);
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

    JTable tab = new JTable();
    JLabel menj = null;

    public void editarProducto(Integer codigo, JTable tabla, JLabel mensaje) {
        tab = tabla;
        //  verificador.setText("2");
        menj = mensaje;
        ControladorProducto cc = new ControladorProducto();
        ModeloProducto producto = cc.obtenerproducto(codigo.toString());
        txtBuscarProducto.setText(producto.getCodigo().toString());
        codigoProducto.setText(producto.getCodigo().toString());
        descripcionProducto.setText(producto.getDescripcion());
        costoProducto.setText(producto.getPrecioCompra().toString());
        VentaProducto.setText(producto.getPrecioVenta().toString());
        stockProducto.setText(producto.getStock().toString());
        proveedorProducto.setText(producto.getProveedor());
        rubroProducto.setText(producto.getRubro());
      
        contenedorTabla.setVisible(false);
        txtBuscarProducto.transferFocus();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPasswordField1 = new javax.swing.JPasswordField();
        jPanel1 = new javax.swing.JPanel();
        contenedorTabla = new javax.swing.JScrollPane();
        listaProducto = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        stockProducto = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        VentaProducto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        costoProducto = new javax.swing.JTextField();
        descripcionProducto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnGuardarProducto = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txtBuscarProducto = new javax.swing.JTextField();
        codigoProducto = new javax.swing.JTextField();
        verificador = new javax.swing.JLabel();
        txtId = new javax.swing.JLabel();
        cbxProveedor = new javax.swing.JComboBox<>();
        cbxRubro = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();

        jPasswordField1.setText("jPasswordField1");

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jPanel1.add(contenedorTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 170, 20));

        jLabel6.setText("Proveedor:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));

        jLabel7.setText("Rubro:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        stockProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                stockProductoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                stockProductoKeyReleased(evt);
            }
        });
        jPanel1.add(stockProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 80, -1));

        jLabel5.setText("Stock:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        jLabel4.setText("precio Venta:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        VentaProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                VentaProductoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                VentaProductoKeyReleased(evt);
            }
        });
        jPanel1.add(VentaProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 80, -1));

        jLabel3.setText("Precio Costo:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        costoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                costoProductoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                costoProductoKeyReleased(evt);
            }
        });
        jPanel1.add(costoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 80, -1));

        descripcionProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descripcionProductoKeyPressed(evt);
            }
        });
        jPanel1.add(descripcionProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 170, -1));

        jLabel2.setText("Descripcion:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel1.setText("Codigo:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        btnGuardarProducto.setText("Guardar");
        btnGuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProductoActionPerformed(evt);
            }
        });
        btnGuardarProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGuardarProductoKeyPressed(evt);
            }
        });
        jPanel1.add(btnGuardarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 210, -1, -1));

        jButton2.setText("Limpiar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

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
        jPanel1.add(txtBuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 170, -1));

        codigoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigoProductoActionPerformed(evt);
            }
        });
        codigoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codigoProductoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codigoProductoKeyReleased(evt);
            }
        });
        jPanel1.add(codigoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 170, -1));

        verificador.setText("1");
        jPanel1.add(verificador, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, -1, -1));
        jPanel1.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 0, 30, 20));

        jPanel1.add(cbxProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 150, -1));

        jPanel1.add(cbxRubro, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 180, 150, -1));

        jButton1.setText("+");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, -1, -1));

        jButton3.setText("+");
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 180, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 320, 280));

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel8.setText("AGREGAR PRODUCTO");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 250, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void codigoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoProductoActionPerformed

    private void btnGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProductoActionPerformed
         try {
        // 1. Crear el objeto Producto y cargarle los datos de tus JTextFields
        Modelos.Producto pro = new Modelos.Producto();
        
        // Si el campo ID no está vacío (para modificaciones), lo asignamos
        if (!txtId.getText().isEmpty()){
            pro.setId(Integer.parseInt(txtId.getText()));
        }
        
        // Mapeo de tus campos específicos
        pro.setCodigoBarras(codigoProducto.getText());
        pro.setNombre(descripcionProducto.getText());
        pro.setPrecioCompra(new java.math.BigDecimal(costoProducto.getText()));
        pro.setPrecioVenta(new java.math.BigDecimal(VentaProducto.getText()));
        pro.setStock(new java.math.BigDecimal(stockProducto.getText()));
        pro.setIdProveedor(Integer.parseInt(proveedorProducto.getText()));
        pro.setIdCategoria(Integer.parseInt(rubroProducto.getText()));

        // 2. Lógica de tus verificadores (mantengo tu estructura original)
        int verifAux = Integer.parseInt(verificador.getText());
        int modoEjecucion = verifAux;
        
        if (verifAux == 4) modoEjecucion = 2; // Caso modificación
        if (verifAux == 3) modoEjecucion = 1; // Caso nuevo desde facturación

        // 3. Llamar al DAO pasándole el objeto empaquetado
        Controlador.DaoProducto dao = new Controlador.DaoProducto();
        dao.agregarProducto(pro, modoEjecucion, tab, menj);

        // 4. Limpiar referencia de tabla
        tab = null;

        // 5. Lógica de cierre de ventana para casos 3 y 4
        int vFinal = Integer.parseInt(verificador.getText());
        if (vFinal == 4 || vFinal == 3) {
            try {
                this.setClosed(true);
            } catch (java.beans.PropertyVetoException ex) {
                java.util.logging.Logger.getLogger(FormFacturar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        } else {
            // Si no se cierra la ventana, podrías llamar a un método para limpiar los campos
            limpiarCampos();
        }

    } catch (NumberFormatException e) {
        // Alerta personalizada si el usuario ingresa letras en campos numéricos
        new Alerta("Error: Verifique precios, stock e IDs", new java.awt.Color(220, 53, 69));
    }
        

    }//GEN-LAST:event_btnGuardarProductoActionPerformed

    private void codigoProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            descripcionProducto.requestFocus();
        }      // TODO add your handling code here:
    }//GEN-LAST:event_codigoProductoKeyPressed

    private void descripcionProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descripcionProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            costoProducto.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_descripcionProductoKeyPressed

    private void costoProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_costoProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            VentaProducto.requestFocus();
        }
    }//GEN-LAST:event_costoProductoKeyPressed

    private void VentaProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_VentaProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            stockProducto.requestFocus();
        }
    }//GEN-LAST:event_VentaProductoKeyPressed

    private void stockProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stockProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            proveedorProducto.requestFocus();
        }
    }//GEN-LAST:event_stockProductoKeyPressed

    private void btnGuardarProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGuardarProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
         int verifAux=Integer.parseInt(verificador.getText());
        if(verifAux==4){
            verifAux=2;
        }
           if(verifAux==3){
            verifAux=1;
        }
        
        Controlador.ControladorProducto objetoProducto = new Controlador.ControladorProducto();
        Mensaje mj = objetoProducto.agregarProducto(codigoProducto, descripcionProducto,
                costoProducto, VentaProducto, stockProducto, proveedorProducto, rubroProducto,
                verifAux, tab, menj,null);

        tab = null;

        if (Integer.parseInt(verificador.getText()) == 4||Integer.parseInt(verificador.getText()) == 3) {
            try {
                this.setClosed(true);

            } catch (PropertyVetoException ex) {
                Logger.getLogger(FormFacturar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
          if (mj.getCodigo() == 1) {
            limpiarCampos();
                    ControladorProducto cc = new ControladorProducto();
        cc.verCodigoDisponlible(codigoProducto);
        }
        if (mj != null) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
        }
    }//GEN-LAST:event_btnGuardarProductoKeyPressed
    boolean fucuseadoprod = false;
    private void txtBuscarProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarProductoFocusGained
        fucuseadoprod = true;
        txtBuscarProducto.selectAll();

    }//GEN-LAST:event_txtBuscarProductoFocusGained
    boolean abreSlide = false;
    private void txtBuscarProductoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarProductoFocusLost
      
             System.out.println("abreslide" + abreSlide);        
        
        if (abreSlide == true) {            
            if (!listaProducto.isColumnSelected(0)) {
                
                Integer cod = null;
                try {
                    cod = Integer.parseInt(txtBuscarProducto.getText());
                } catch (Exception e) {
                }                
                editarProducto(cod, null,menj);                
                contenedorTabla.setVisible(false);
                
            }
        }
        

     
    }//GEN-LAST:event_txtBuscarProductoFocusLost

    private void txtBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProductoActionPerformed

    }//GEN-LAST:event_txtBuscarProductoActionPerformed

    private void txtBuscarProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyPressed

    }//GEN-LAST:event_txtBuscarProductoKeyPressed

    private void txtBuscarProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyReleased
        Controlador.ControladorVenta objetoVenta = new ControladorVenta();
                abreSlide = true;
        boolean returno = objetoVenta.buscarProducto(txtBuscarProducto, listaProducto, contenedorTabla, "ACTIVOS");

        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !txtBuscarProducto.getText().equals("")) {
            fucuseadoprod = false;
            listaProducto.changeSelection(0, 0, false, false);
            listaProducto.requestFocus();

            if (returno == false) {
                contenedorTabla.setVisible(false);
                mostrarAlerta("No se encontraron coincidencias.", 2);
                txtBuscarProducto.requestFocus();
                txtBuscarProducto.selectAll();
                codigoProducto.setText("");
                costoProducto.setText("");
                VentaProducto.setText("");
                stockProducto.setText("");
                proveedorProducto.setText("");
                rubroProducto.setText("");

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

        int fila = listaProducto.getSelectedRow();
        editarProducto(Integer.parseInt(listaProducto.getValueAt(fila, 1).toString()), null, menj);

    }//GEN-LAST:event_listaProductoMouseClicked

    private void listaProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !txtBuscarProducto.getText().equals("")) {

            Controlador.ControladorVenta objetoVenta = new ControladorVenta();
            int fila = listaProducto.getSelectedRow();
            editarProducto(Integer.parseInt(listaProducto.getValueAt(fila, 1).toString()), null, menj);

        }

        System.out.println("    aqui piertde ");
    }//GEN-LAST:event_listaProductoKeyPressed
    private void verificarIngreso(JTextField texto, char c) {

        String cadena = texto.getText();

        if (Character.isDigit(c) || c == '.') {
            cadena = cadena.substring(0, cadena.length() - 1);

            if (c == '.' && cadena.contains(".")) {

                texto.setText(cadena);
            }
        } else {
            mostrarAlerta("solo se aceptan numeros para este campo", 2);

            cadena = texto.getText();

            if (cadena.length() > 1) {
                cadena = cadena.substring(0, cadena.length() - 1);
            } else {
                cadena = "";
            }
            texto.setText(cadena);
            //  texto.requestFocus();
            texto.setCaretPosition(texto.getText().length());
            texto.requestFocusInWindow();

        }

    }
    private void listaProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_listaProductoKeyReleased

    private void listaProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_listaProductoKeyTyped

    private void costoProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_costoProductoKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {

            verificarIngreso(costoProducto, evt.getKeyChar());

        }
    }//GEN-LAST:event_costoProductoKeyReleased

    private void VentaProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_VentaProductoKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {

            verificarIngreso(VentaProducto, evt.getKeyChar());

        }
    }//GEN-LAST:event_VentaProductoKeyReleased

    private void stockProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stockProductoKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {

            verificarIngreso(stockProducto, evt.getKeyChar());

        }
    }//GEN-LAST:event_stockProductoKeyReleased

    private void codigoProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoProductoKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {

            verificarIngreso(codigoProducto, evt.getKeyChar());

        }
    }//GEN-LAST:event_codigoProductoKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
   limpiarCampos();
    }//GEN-LAST:event_jButton2ActionPerformed

  private void   limpiarCampos(){
  descripcionProducto.setText("");
  costoProducto.setText("");
  VentaProducto.setText("");
  stockProducto.setText("");
  proveedorProducto.setText("");
  rubroProducto.setText("");

  
  };
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField VentaProducto;
    private javax.swing.JButton btnGuardarProducto;
    private javax.swing.JComboBox<Modelos.ItemSeleccionable> cbxProveedor;
    private javax.swing.JComboBox<Modelos.ItemSeleccionable> cbxRubro;
    private javax.swing.JTextField codigoProducto;
    private javax.swing.JScrollPane contenedorTabla;
    private javax.swing.JTextField costoProducto;
    private javax.swing.JTextField descripcionProducto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTable listaProducto;
    private javax.swing.JTextField stockProducto;
    private javax.swing.JTextField txtBuscarProducto;
    private javax.swing.JLabel txtId;
    private javax.swing.JLabel verificador;
    // End of variables declaration//GEN-END:variables
}
