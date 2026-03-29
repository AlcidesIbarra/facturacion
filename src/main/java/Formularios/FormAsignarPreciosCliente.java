/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;
import Controlador.ControladorAsignarPrecio;
import Controlador.ControladorCliente;
import Controlador.ControladorFacturas;
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
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ALCIDES
 */
public class FormAsignarPreciosCliente extends javax.swing.JInternalFrame {

    /**
     * Creates new form FormVenta
     */
    public FormAsignarPreciosCliente() {
        initComponents();

        
        ColorMargenRenderer render = new ColorMargenRenderer();
for (int i = 0; i < tbResumenVenta.getColumnCount(); i++) {
    tbResumenVenta.getColumnModel().getColumn(i).setCellRenderer(render);
}
        
        contenedorTabla.setVisible(false);
        contenedorClientes.setVisible(false);

        ControladorAdministracion ca = new ControladorAdministracion();

        fieldAnterior.setEditable(false);

        fieldCodigo.setEditable(false);
        fieldCosto.setEditable(false);
        fieldDireccion.setEditable(false);
        fieldNombre.setEditable(false);
        fieldAnterior.setEditable(false);
        Color color = ca.retornatexFieldF();
        fieldCodigo.setBackground(color);
        fieldCosto.setBackground(color);
        fieldDireccion.setBackground(color);
        fieldNombre.setBackground(color);
        fieldAnterior.setBackground(color);
        ca.cambiacolor(this);
        ca.BotonesCrisitalPrim(btnAgregar, Color.green, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton3, Color.red, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton4, Color.yellow, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton2, Color.red, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton5, Color.cyan, Color.black, Color.black);

        tbResumenVenta.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbResumenVenta.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbResumenVenta.getColumnModel().getColumn(1).setPreferredWidth(242);
        tbResumenVenta.getColumnModel().getColumn(2).setPreferredWidth(130);
        tbResumenVenta.getColumnModel().getColumn(3).setPreferredWidth(130);
        tbResumenVenta.getColumnModel().getColumn(4).setPreferredWidth(130);
        tbResumenVenta.getColumnModel().getColumn(5).setPreferredWidth(75);

        DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
        cent.setHorizontalAlignment(JLabel.CENTER);
        tbResumenVenta.getColumnModel().getColumn(0).setCellRenderer(cent);
        tbResumenVenta.getColumnModel().getColumn(5).setCellRenderer(cent);
        tbResumenVenta.getColumnModel().getColumn(3).setCellRenderer(new paddingRig());
        tbResumenVenta.getColumnModel().getColumn(2).setCellRenderer(new paddingRig());
        tbResumenVenta.getColumnModel().getColumn(4).setCellRenderer(new paddingRig());

        contenedorClientes.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL) {
            @Override
            public boolean isVisible() {
                return false;
            }
        });
        JScrollBar vertical
                = contenedorClientes.getVerticalScrollBar();
        vertical.setPreferredSize(new Dimension(0, 0));
        vertical.setMinimumSize(new Dimension(0, 0));
        vertical.setMaximumSize(new Dimension(0, 0));
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        fieldAsignarPrecio = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        contenedorClientes = new javax.swing.JScrollPane();
        listaClientes = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        fieldAnterior = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        fieldCodigo = new javax.swing.JTextField();
        fieldCosto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        fieldNombre = new javax.swing.JTextField();
        contenedorTabla = new javax.swing.JScrollPane();
        listaProducto = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbResumenVenta = new javax.swing.JTable();
        codigoCliente = new javax.swing.JTextField();
        txtBuscarProducto = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        fieldDireccion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        nombreLabel = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        codigoLabel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jToggleButton1.setText("jToggleButton1");

        setClosable(true);
        setIconifiable(true);
        setTitle("Asignar Precio");
        setToolTipText("");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setText("Codigo:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        fieldAsignarPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldAsignarPrecioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fieldAsignarPrecioKeyReleased(evt);
            }
        });
        jPanel1.add(fieldAsignarPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, 100, -1));

        jLabel1.setText("Pr. Nuevo:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, -1, -1));

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

        jPanel1.add(contenedorClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 170, 20));

        jLabel10.setText("Costo:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 80, -1, -1));

        jLabel11.setText("Anterior:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, -1));
        jPanel1.add(fieldAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 100, -1));

        jLabel12.setText("Codigo:");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, -1, -1));
        jPanel1.add(fieldCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 100, -1));
        jPanel1.add(fieldCosto, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 100, -1));

        jLabel4.setText("Nombre:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, -1, -1));
        jPanel1.add(fieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, 160, -1));

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

        jPanel1.add(contenedorTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 170, 20));

        tbResumenVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Descripcion", "Precio costo", "Precio Cliente", "Diferencia", "Margen"
            }
        ));
        tbResumenVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbResumenVentaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbResumenVenta);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 770, 260));

        codigoCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                codigoClienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                codigoClienteFocusLost(evt);
            }
        });
        codigoCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                codigoClienteMouseClicked(evt);
            }
        });
        codigoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigoClienteActionPerformed(evt);
            }
        });
        codigoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codigoClienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codigoClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codigoClienteKeyTyped(evt);
            }
        });
        jPanel1.add(codigoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 170, -1));

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
        jPanel1.add(txtBuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 170, -1));

        jLabel17.setText("    __________________________________________________________________________________________________________");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 760, 20));

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 50, 90, 50));

        jButton3.setText("Eliminar");
        jButton3.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 50, 80, 20));

        jButton4.setText("Editar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 80, 80, 20));

        jLabel7.setText("Direccion:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 0, -1, -1));
        jPanel1.add(fieldDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, 220, -1));

        jLabel8.setText("Buscar:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        jLabel2.setText("Total Productos:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 380, -1, -1));

        jLabel5.setText("Cliente:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 400, -1, -1));

        jButton2.setText("Limpiar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 85, 38));

        jLabel3.setText("0");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 380, 150, -1));

        nombreLabel.setText("-----");
        jPanel1.add(nombreLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 400, 263, -1));

        jButton5.setText("Guardar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 380, 85, 38));

        jLabel14.setText("Codigo:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 400, -1, -1));

        codigoLabel.setText("----");
        jPanel1.add(codigoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 400, -1, -1));

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel13.setText("ASIGNAR PRECIO CLIENTE");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 770, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(262, 262, 262)
                        .addComponent(jLabel13)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbResumenVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbResumenVentaMouseClicked

    }//GEN-LAST:event_tbResumenVentaMouseClicked

    private void fieldAsignarPrecioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldAsignarPrecioKeyPressed


    }//GEN-LAST:event_fieldAsignarPrecioKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DefaultTableModel modeloTabla = (DefaultTableModel) tbResumenVenta.getModel();
        modeloTabla.setRowCount(0);
                  Controlador.ControladorAsignarPrecio objetoCliente = new ControladorAsignarPrecio();
                    objetoCliente.limpiarCampos(fieldNombre, fieldDireccion, codigoCliente, nombreLabel, codigoLabel);
            jLabel3.setText("0");
    }//GEN-LAST:event_jButton2ActionPerformed
    boolean fucuseado = false;
    private void codigoClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_codigoClienteFocusGained

        codigoCliente.selectAll();
    }//GEN-LAST:event_codigoClienteFocusGained

    private void codigoClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_codigoClienteFocusLost

        Mensaje mj = null;

        Controlador.ControladorAsignarPrecio objetoCliente = new ControladorAsignarPrecio();
        if (fucuseado == true) {
            if (!listaClientes.isColumnSelected(0)) {
                contenedorClientes.setVisible(false);
                if (!codigoCliente.getText().equals("")) {
                    System.out.println("entra al focussloss");

                    mj = objetoCliente.SeleccionarCliente(listaClientes, fieldNombre, fieldDireccion,
                            codigoCliente, contenedorClientes,
                            txtBuscarProducto, nombreLabel, codigoLabel, 1);
                    fucuseado = false;
                } else {
                    fucuseado = false;
                    //VER SI SIRVE ESSTA PARTE SINO BORRAR
                    /*
                if (fucuseadoprod == true) {
                    mj = cv.actualizarTablaCliente(tbResumenVenta, 0, prVenta);
                    fucuseadoprod = false;
                }*/
                    
                    objetoCliente.limpiarCampos(fieldNombre, fieldDireccion, codigoCliente, nombreLabel, codigoLabel);
                }
            } else {
                System.out.println("entralsesse");
            }

            if (codigoCliente.getText().equals("")) {
                contenedorClientes.setVisible(false);
  objetoCliente.limpiarCampos(fieldNombre, fieldDireccion, codigoCliente, nombreLabel, codigoLabel);
            }

            if (mj != null) {
                mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            }

        }
    }//GEN-LAST:event_codigoClienteFocusLost

    private void codigoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoClienteActionPerformed

    }//GEN-LAST:event_codigoClienteActionPerformed

    private void codigoClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoClienteKeyPressed

    }//GEN-LAST:event_codigoClienteKeyPressed

    private void codigoClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoClienteKeyReleased

        Controlador.ControladorAsignarPrecio objetoCliente = new ControladorAsignarPrecio();
        boolean returno = objetoCliente.buscarCliente(codigoCliente, listaClientes, contenedorClientes, 1);

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            fucuseado = false;

            if (returno == false && !codigoCliente.getText().equals("")) {
                contenedorClientes.setVisible(false);

                String codigo = codigoCliente.getText();
                objetoCliente.limpiarCampos(fieldNombre, fieldDireccion, codigoCliente, nombreLabel, codigoLabel);
            }
            // JOptionPane.showMessageDialog(null, "enmterdetectado");
            if (codigoCliente.getText().equals("")) {

                objetoCliente.limpiarCampos(fieldNombre, fieldDireccion, codigoCliente, nombreLabel, codigoLabel);
                txtBuscarProducto.requestFocus();

                // ver ssi se usa
                /*
                ControladorVenta cv = new ControladorVenta();
                cv.actualizarTablaCliente(tbResumenVenta, 0, prVenta);
                 */
            } else {
                listaClientes.changeSelection(0, 0, false, false);
                listaClientes.requestFocus();
            }
        }
        if (codigoCliente.getText().equals("")) {
            // Controlador.ControladorCliente cc = new ControladorCliente();
            contenedorClientes.setVisible(false);
        }

    }//GEN-LAST:event_codigoClienteKeyReleased

    private void codigoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoClienteKeyTyped

    }//GEN-LAST:event_codigoClienteKeyTyped

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
                    fieldTelefono, codigoCliente, contenedorClientes,
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
        Mensaje mj = null;
        Controlador.ControladorAsignarPrecio objetoCliente = new ControladorAsignarPrecio();
        mj = objetoCliente.SeleccionarCliente(listaClientes, fieldNombre, fieldDireccion,
                codigoCliente, contenedorClientes,
                txtBuscarProducto, nombreLabel, codigoLabel, 0);
        if (mj != null) {
            //   mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
    }//GEN-LAST:event_listaClientesMouseClicked

    private void listaClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaClientesKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            Mensaje mj = null;
            Controlador.ControladorAsignarPrecio objetoCliente = new ControladorAsignarPrecio();
            mj = objetoCliente.SeleccionarCliente(listaClientes, fieldNombre, fieldDireccion,
                    codigoCliente, contenedorClientes,
                    txtBuscarProducto, nombreLabel, codigoLabel, 0);
            if (mj != null) {
                //   mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            }
            txtBuscarProducto.requestFocus();
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

    private void txtBuscarProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarProductoFocusGained
        //  fucuseadoprod = true;
        txtBuscarProducto.selectAll();
        /*
        if (!editaFacturar.isSelected()) {
            texfieldStock.setEditable(false);
            prVenta.setEditable(false);
        }
         */
    }//GEN-LAST:event_txtBuscarProductoFocusGained

    private void txtBuscarProductoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarProductoFocusLost
        if (txtBuscarProducto.getText().equals("")) {
            fieldCodigo.setText("");
            fieldAsignarPrecio.setText("");
            fieldAnterior.setText("");
            fieldCosto.setText("");

            contenedorTabla.setVisible(false);
        }

        if (!listaProducto.isColumnSelected(0)) {
            contenedorTabla.setVisible(false);
            if (!txtBuscarProducto.getText().equals("")) {

                Controlador.ControladorAsignarPrecio objetoVenta = new ControladorAsignarPrecio();
                Mensaje mj = objetoVenta.SeleccionarProductosVenta(listaProducto, codigoCliente,
                        fieldCodigo, fieldAsignarPrecio, fieldCosto, fieldAnterior,
                        txtBuscarProducto, contenedorTabla, 1);
                if (mj != null) {
                    // mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                }
            } else {
                fieldCodigo.setText("");
                fieldAsignarPrecio.setText("");
                fieldAnterior.setText("");
                fieldCosto.setText("");
            }

            if (txtBuscarProducto.getText().equals("")) {
                contenedorTabla.setVisible(false);
            }

        }

    }//GEN-LAST:event_txtBuscarProductoFocusLost

    private void txtBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProductoActionPerformed

    }//GEN-LAST:event_txtBuscarProductoActionPerformed

    private void txtBuscarProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyPressed

    }//GEN-LAST:event_txtBuscarProductoKeyPressed
    Boolean fucuseadoprod = false;
    private void txtBuscarProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyReleased
        Controlador.ControladorVenta objetoVenta = new ControladorVenta();
        boolean returno = objetoVenta.buscarProducto(txtBuscarProducto, listaProducto, contenedorTabla, "ACTIVOS");

        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !txtBuscarProducto.getText().equals("")) {
            fucuseadoprod = false;
            listaProducto.changeSelection(0, 0, false, false);
            listaProducto.requestFocus();

            if (returno == false) {
                contenedorTabla.setVisible(false);
                mostrarAlerta("No se encontraron coincidencias.", 2);
                fieldCodigo.setText("");
                fieldAsignarPrecio.setText("");
                fieldAnterior.setText("");
                fieldCosto.setText("");

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
             Controlador.ControladorAsignarPrecio objetoVenta = new ControladorAsignarPrecio();
            Mensaje mj = objetoVenta.SeleccionarProductosVenta(listaProducto, codigoCliente,
                    fieldCodigo, fieldAsignarPrecio, fieldCosto, fieldAnterior,
                    txtBuscarProducto, contenedorTabla,0);

            if (mj != null) {
                mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            }

        }
        fucuseadoprod = false;
         */
    }//GEN-LAST:event_listaProductoFocusLost

    private void listaProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaProductoMouseClicked
        Controlador.ControladorAsignarPrecio objetoVenta = new ControladorAsignarPrecio();
        Mensaje mj = objetoVenta.SeleccionarProductosVenta(listaProducto, codigoCliente,
                fieldCodigo, fieldAsignarPrecio, fieldCosto, fieldAnterior,
                txtBuscarProducto, contenedorTabla, 0);
        if (mj != null) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
    }//GEN-LAST:event_listaProductoMouseClicked
    int contadorerror = 0;
    private void listaProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !txtBuscarProducto.getText().equals("")) {
            contadorerror++;
            System.out.println(contadorerror);
            Controlador.ControladorAsignarPrecio objetoVenta = new ControladorAsignarPrecio();
            Mensaje mj = objetoVenta.SeleccionarProductosVenta(listaProducto, codigoCliente,
                    fieldCodigo, fieldAsignarPrecio, fieldCosto, fieldAnterior,
                    txtBuscarProducto, contenedorTabla, 0);

            if (mj != null) {
                mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            }

        }

        // TODO add your handling code here:
    }//GEN-LAST:event_listaProductoKeyPressed

    private void listaProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_listaProductoKeyReleased

    private void listaProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_listaProductoKeyTyped

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed

        Mensaje mj = null;

        if (!fieldCodigo.getText().equals("")) {

            if (!fieldAsignarPrecio.getText().equals("")) {

                if (indiceSeleccionadoEliminar != -1) {
                    DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
                    modelo.removeRow(indiceSeleccionadoEliminar);
                    indiceSeleccionadoEliminar = -1;
                }

                if (Integer.parseInt(fieldAsignarPrecio.getText()) != 0) {
                    Controlador.ControladorAsignarPrecio objetoPrecio = new ControladorAsignarPrecio();
                    mj = objetoPrecio.pasarPreciosGuardar(tbResumenVenta, fieldCodigo, txtBuscarProducto,
                            fieldCosto, fieldAsignarPrecio, fieldAnterior, txtBuscarProducto);
                } else {
                    mostrarAlerta("Ingresa un numero distinto de '0'", 2);
                }

                if (mj != null) {
                    mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                }

            } else {
                mostrarAlerta("Ingresa una precio para agregar.", 2);
                fieldAsignarPrecio.requestFocus();
            }
        } else {
            fieldAsignarPrecio.setText("");
            mostrarAlerta("Ingresa un producto para agregar.", 2);
            txtBuscarProducto.requestFocus();
        }
        Integer filas = tbResumenVenta.getRowCount();
        jLabel3.setText(filas.toString());
    
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Controlador.ControladorVenta objetoVenta = new ControladorVenta();
        Mensaje mj = objetoVenta.eliminarProductoSeleccionado(tbResumenVenta);
        if (mj != null) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
        Integer filas = tbResumenVenta.getRowCount();
        if (filas == 0)
            jLabel3.setText(filas.toString());
    }//GEN-LAST:event_jButton3ActionPerformed

    
    
    
    public class ColorMargenRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Asumimos que el margen está en la columna 5
        Object valMargen = table.getValueAt(row, 5);
        String texto = (valMargen != null) ? valMargen.toString() : "";

        if (texto.contains("ERROR") || texto.contains("PÉRDIDA") || texto.contains("-")) {
            c.setForeground(Color.RED);
            c.setFont(c.getFont().deriveFont(Font.BOLD));
        } else {
            c.setForeground(new Color(0, 100, 0)); // Un verde oscuro para ganancias
            c.setFont(c.getFont().deriveFont(Font.PLAIN));
        }

        if (isSelected) c.setBackground(table.getSelectionBackground());
        else c.setBackground(Color.WHITE);

        return c;
    }
}
    
    
    
    
    
    
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Controlador.ControladorAsignarPrecio objetoVenta = new ControladorAsignarPrecio();
        Mensaje mj = objetoVenta.editarProductoSeleccionado(tbResumenVenta,
                fieldCodigo, txtBuscarProducto, fieldCosto, fieldAsignarPrecio,
                codigoCliente, contenedorTabla, fieldAnterior);

        if (mj != null && mj.getCodigo() == 2) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        } else {
            indiceSeleccionadoEliminar = mj.getCodigo();
            fieldAsignarPrecio.requestFocus();
            fieldAsignarPrecio.selectAll();
        }

    }//GEN-LAST:event_jButton4ActionPerformed
    int indiceSeleccionadoEliminar = -1;
    private void codigoClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_codigoClienteMouseClicked
        fucuseado = true;
    }//GEN-LAST:event_codigoClienteMouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Mensaje mj = null;
        if (Integer.parseInt(jLabel3.getText()) == 0) {
            mostrarAlerta("No hay productos agregados para guardar", 3);
        } else {
            Controlador.ControladorAsignarPrecio objetoPrecio = new ControladorAsignarPrecio();
            if (codigoLabel.getText().equals("----")) {

                mostrarAlerta("Ingresa un cliente para guardar", 3);
            } else {
                mj = objetoPrecio.guardarDatos(tbResumenVenta, Integer.parseInt(codigoLabel.getText()));
            }
        }
        if (mj != null) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
        codigoCliente.requestFocusInWindow();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void fieldAsignarPrecioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldAsignarPrecioKeyReleased
        Mensaje mj = null;

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!fieldCodigo.getText().equals("")) {

                if (!fieldAsignarPrecio.getText().equals("")) {
                  //  NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
                 //   fieldAsignarPrecio.setText(formatoMoneda.format(Double.parseDouble(fieldAsignarPrecio.getText())));
                    if (indiceSeleccionadoEliminar != -1) {
                        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
                        modelo.removeRow(indiceSeleccionadoEliminar);
                        indiceSeleccionadoEliminar = -1;
                    }

                    if (Integer.parseInt(fieldAsignarPrecio.getText()) != 0) {

                        Controlador.ControladorAsignarPrecio objetoPrecio = new ControladorAsignarPrecio();
                        mj = objetoPrecio.pasarPreciosGuardar(tbResumenVenta, fieldCodigo,
                                txtBuscarProducto, fieldCosto,
                                fieldAsignarPrecio, fieldAnterior, txtBuscarProducto);

                    } else {
                        mostrarAlerta("Ingresa un numero distinto de '0'", 2);
                        fieldAsignarPrecio.requestFocusInWindow();
                    }

                    if (mj != null) {
                        mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                    }

                } else {
                    //  mostrarAlerta("Ingresa un precio para agregar.", 2);
                    //   fieldAsignarPrecio.requestFocus();
                }
            } else {
                fieldAsignarPrecio.setText("");
                mostrarAlerta("Ingresa un producto para agregar.", 2);
                txtBuscarProducto.requestFocus();
            }
        } else {
                    if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {
            char c = evt.getKeyChar();
            String cadena = fieldAsignarPrecio.getText();

            if (Character.isDigit(c) || c == '.') {
                cadena = cadena.substring(0, cadena.length() - 1);

                if (c == '.' && cadena.contains(".")) {

                    fieldAsignarPrecio.setText(cadena);
                }
            } else {
                mostrarAlerta("solo se aceptan numeros para este campo", 2);

                cadena = fieldAsignarPrecio.getText();

                if (cadena.length() > 1) {
                    cadena = cadena.substring(0, cadena.length() - 1);
                } else {
                    cadena = "";
                }
                fieldAsignarPrecio.setText(cadena);
                fieldAsignarPrecio.requestFocus();
            }}
        }
        Integer filas = tbResumenVenta.getRowCount();
        jLabel3.setText(filas.toString());
    }//GEN-LAST:event_fieldAsignarPrecioKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JTextField codigoCliente;
    private javax.swing.JLabel codigoLabel;
    private javax.swing.JScrollPane contenedorClientes;
    private javax.swing.JScrollPane contenedorTabla;
    private javax.swing.JTextField fieldAnterior;
    private javax.swing.JTextField fieldAsignarPrecio;
    private javax.swing.JTextField fieldCodigo;
    private javax.swing.JTextField fieldCosto;
    private javax.swing.JTextField fieldDireccion;
    private javax.swing.JTextField fieldNombre;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTable listaClientes;
    private javax.swing.JTable listaProducto;
    private javax.swing.JLabel nombreLabel;
    private javax.swing.JTable tbResumenVenta;
    private javax.swing.JTextField txtBuscarProducto;
    // End of variables declaration//GEN-END:variables
}
