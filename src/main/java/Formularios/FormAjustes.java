/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;
import Controlador.ControladorAjustes;
import Controlador.ControladorAsignarPrecio;
import Controlador.ControladorCliente;
import Controlador.ControladorProducto;
import Controlador.ControladorVenta;
import Modelos.Mensaje;
import com.sun.java.accessibility.util.AWTEventMonitor;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author ALCIDES
 */
public class FormAjustes extends javax.swing.JInternalFrame {

    public FormAjustes(int num) {

        initComponents();

        //this.setAlignmentX(CENTER_ALIGNMENT);
        ///   this.setAlignmentY(CENTER_ALIGNMENT);
        contenedorTabla.setVisible(false);

        Controlador.ControladorVenta objetoVenta = new ControladorVenta();
   
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
   
        ControladorAdministracion ca = new ControladorAdministracion();
        ca.cambiacolor(this);

        ca.BotonesCrisitalPrim(btnAgregar, Color.green, Color.black, Color.black);
        // btnAgregar.setBackground(Color.green,Color.black);
        ca.BotonesCrisitalPrim(btnFacturar, Color.blue, Color.black, Color.black);

        ca.BotonesCrisitalPrim(jButton3, Color.red, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton2, Color.orange, Color.black, Color.black);


        codigoProducto.setEditable(false);
        codigoProducto.setBackground(ca.retornatexFieldF());

        Dimension d = new Dimension(80, 26); // El tamaño que quieras
        btnFacturar.setPreferredSize(d);
        btnFacturar.setMinimumSize(d);
        btnFacturar.setMaximumSize(d);

        descProducto.setEditable(false);
        rubro.setEditable(false);
        proveedor.setEditable(false);

        Color color = ca.retornatexFieldF();
        descProducto.setBackground(color);
        rubro.setBackground(color);
        proveedor.setBackground(color);

        System.out.println(num + "NUMERO QUE VINE"
                + "");
        switch (num) {
            case 1:
                negativo.setSelected(true);

                break;
            case 2:
                sobreSaldo.setSelected(true);
                break;
            case 3:
                jCheckBox1.setSelected(true);
                break;
            default:
                throw new AssertionError();
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
    // ... El resto de tus métodos (abrirvistaFactura, etc) ...

    public void abrirvistaFactura(Integer numero, JDesktopPane contenedor) {

        Dimension dpSize = contenedor.getSize();

        ModeloFactura frameB = new ModeloFactura(numero, 1);
        //   Dimension dpSize = this.getDesktopPane().getSize();
        Dimension frSize = frameB.getSize();
        contenedor.add(frameB); // Añade al escritorio padre

        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);

        frameB.setLocation(x - 10, y + 136);
        frameB.setVisible(true);
        frameB.toFront(); // Trae al frente la nueva ventana

        //   frameB.verDetalleFactura(numero, 1);
    }


    /*
    public void confirmarPago(String metodoPago, Double DineroPagado, Double montoFactura,
            String observaciones, Double saldoCliente, Integer codCliente,JTable tablaProductos) {
        ControladorVenta cv = new ControladorVenta();

        cv.guardarDatosConfirmar(tbResumenVenta, codCliente, metodoPago,
                DineroPagado, montoFactura, observaciones, saldoCliente);
    }
     */
 /*    
iraotro.addActionListener(e -> {
    FormFacturar frameB = new  FormFacturar();
    this.getDesktopPane().add(frameB); // Añade al escritorio padre
    frameB.setVisible(true);
    frameB.toFront(); // Trae al frente la nueva ventana
});
    
    }
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        btnFacturar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtBuscarProducto = new javax.swing.JTextField();
        contenedorTabla = new javax.swing.JScrollPane();
        listaProducto = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbResumenVenta = new javax.swing.JTable();
        texfieldStock = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        codigoProducto = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        prCosto = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        prVenta = new javax.swing.JTextField();
        proveedor = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        rubro = new javax.swing.JTextField();
        descProducto = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        negativo = new javax.swing.JCheckBox();
        sobreSaldo = new javax.swing.JCheckBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        total = new javax.swing.JLabel();
        txtTotal = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Ingresar Producto / Stock / precio");

        btnFacturar.setText("Guardar");
        btnFacturar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFacturarMouseClicked(evt);
            }
        });
        btnFacturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturarActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setText("Buscar:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

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
        jPanel1.add(txtBuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 170, -1));

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
        listaProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaProductoMouseClicked(evt);
            }
        });
        listaProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                listaProductoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                listaProductoKeyTyped(evt);
            }
        });
        contenedorTabla.setViewportView(listaProducto);

        jPanel1.add(contenedorTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 170, 20));

        tbResumenVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "COD", "Descripcion", "Precio costo", "Precio venta", "Diferencia", "Stock", "Rubro", "Proveedor", "Subtotal"
            }
        ));
        tbResumenVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbResumenVentaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbResumenVenta);
        if (tbResumenVenta.getColumnModel().getColumnCount() > 0) {
            tbResumenVenta.getColumnModel().getColumn(0).setMinWidth(50);
            tbResumenVenta.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbResumenVenta.getColumnModel().getColumn(0).setMaxWidth(50);
            tbResumenVenta.getColumnModel().getColumn(1).setMinWidth(200);
            tbResumenVenta.getColumnModel().getColumn(1).setPreferredWidth(200);
            tbResumenVenta.getColumnModel().getColumn(1).setMaxWidth(200);
            tbResumenVenta.getColumnModel().getColumn(3).setResizable(false);
            tbResumenVenta.getColumnModel().getColumn(4).setResizable(false);
            tbResumenVenta.getColumnModel().getColumn(5).setMinWidth(70);
            tbResumenVenta.getColumnModel().getColumn(5).setPreferredWidth(70);
            tbResumenVenta.getColumnModel().getColumn(5).setMaxWidth(70);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 870, 250));

        texfieldStock.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                texfieldStockFocusGained(evt);
            }
        });
        texfieldStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                texfieldStockMouseClicked(evt);
            }
        });
        texfieldStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                texfieldStockActionPerformed(evt);
            }
        });
        texfieldStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                texfieldStockKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                texfieldStockKeyReleased(evt);
            }
        });
        jPanel1.add(texfieldStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 65, -1));

        jLabel11.setText("Stock:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        jLabel12.setText("Codigo:");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        codigoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codigoProductoKeyPressed(evt);
            }
        });
        jPanel1.add(codigoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 30, -1));

        jLabel16.setText("Compra:");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, -1, -1));

        prCosto.setText(" ");
        prCosto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                prCostoFocusGained(evt);
            }
        });
        prCosto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                prCostoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                prCostoKeyReleased(evt);
            }
        });
        jPanel1.add(prCosto, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 85, -1));

        jLabel17.setText("Venta:");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, -1, -1));

        prVenta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                prVentaFocusGained(evt);
            }
        });
        prVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prVentaActionPerformed(evt);
            }
        });
        prVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                prVentaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                prVentaKeyReleased(evt);
            }
        });
        jPanel1.add(prVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 90, -1));

        proveedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                proveedorFocusGained(evt);
            }
        });
        proveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                proveedorKeyPressed(evt);
            }
        });
        jPanel1.add(proveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 40, 110, -1));

        jLabel2.setText("Proveedor:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 40, -1, 20));

        rubro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rubroFocusGained(evt);
            }
        });
        rubro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rubroKeyPressed(evt);
            }
        });
        jPanel1.add(rubro, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 40, 104, -1));

        descProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                descProductoFocusGained(evt);
            }
        });
        descProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descProductoKeyPressed(evt);
            }
        });
        jPanel1.add(descProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 40, 160, -1));

        jLabel18.setText("Descripcion:");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, -1, -1));

        jLabel1.setText("Rubro:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, -1, -1));

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 10, 90, 50));

        jButton2.setText("Editar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 40, 80, 20));

        jButton3.setText("Eliminar");
        jButton3.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 10, 80, 20));

        negativo.setText("Ingresar Factura");
        negativo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                negativoItemStateChanged(evt);
            }
        });
        negativo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                negativoStateChanged(evt);
            }
        });

        sobreSaldo.setText("Solo Stock");
        sobreSaldo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sobreSaldoItemStateChanged(evt);
            }
        });

        jCheckBox1.setText("Solo Precio");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Editar Descripcion");
        jCheckBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox2ItemStateChanged(evt);
            }
        });
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        total.setText("----");

        txtTotal.setText("TOTAL:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(negativo)
                            .addComponent(sobreSaldo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox2)
                            .addComponent(jCheckBox1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(txtTotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btnFacturar)
                                .addContainerGap())))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(negativo)
                            .addComponent(jCheckBox1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sobreSaldo)
                            .addComponent(jCheckBox2)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(total)
                            .addComponent(txtTotal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFacturar))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyReleased

        Controlador.ControladorVenta objetoVenta = new ControladorVenta();
        boolean returno = objetoVenta.buscarProducto(txtBuscarProducto, listaProducto, contenedorTabla, "ACTIVOS");
texfieldStock.setText("");
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !txtBuscarProducto.getText().equals("")) {
            //  fucuseadoprod = false;
            listaProducto.changeSelection(0, 0, false, false);
            listaProducto.requestFocus();

            if (returno == false) {
                contenedorTabla.setVisible(false);
                mostrarAlerta("No se encontraron coincidencias.", 2);
                txtBuscarProducto.requestFocus();
                txtBuscarProducto.selectAll();
                codigoProducto.setText("");
                prCosto.setText("");
                prVenta.setText("");
                texfieldStock.setText("");
                rubro.setText("");
                proveedor.setText("");

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

    private void tbResumenVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbResumenVentaMouseClicked

    }//GEN-LAST:event_tbResumenVentaMouseClicked

    private void txtBuscarProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyPressed

    }//GEN-LAST:event_txtBuscarProductoKeyPressed

    private void txtBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProductoActionPerformed


    }//GEN-LAST:event_txtBuscarProductoActionPerformed

    private void texfieldStockKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_texfieldStockKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ControladorAjustes cap = new ControladorAjustes();
            
            System.out.println("el check es "+check);
            switch (check) {
                case "factura":
                    prCosto.selectAll();
                    prCosto.requestFocus();
                    prCosto.requestFocusInWindow();
                    break;
                case "stock":
                    if (jCheckBox2.isSelected()) {
                        descProducto.selectAll();
                        descProducto.requestFocus();
                        descProducto.requestFocusInWindow();
                    } else {

                        verificarAntesDeAgregar();
                        
                    }
                    break;

                default:
                    prCosto.selectAll();
                    prCosto.requestFocus();
                    prCosto.requestFocusInWindow();
            }
        }
    }//GEN-LAST:event_texfieldStockKeyPressed

    private void codigoProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoProductoKeyPressed

    }//GEN-LAST:event_codigoProductoKeyPressed

    private void texfieldStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_texfieldStockMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_texfieldStockMouseClicked
    Double stockAnterior = 0d;
    private void listaProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ControladorProducto objetoVenta = new ControladorProducto();
            objetoVenta.SeleccionarProductosVenta(listaProducto, texfieldStock,
                    codigoProducto, txtBuscarProducto, prCosto, prVenta, descProducto,
                    contenedorTabla, texfieldStock, rubro, proveedor, check);
            stockAnterior = Double.parseDouble(texfieldStock.getText());
            descRespaldo = descProducto.getText();
            texfieldStock.setText("");
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_listaProductoKeyPressed


    private void listaProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaProductoMouseClicked
        ControladorProducto objetoVenta = new ControladorProducto();
        objetoVenta.SeleccionarProductosVenta(listaProducto, texfieldStock,
                codigoProducto, txtBuscarProducto, prCosto, prVenta, descProducto,
                contenedorTabla, texfieldStock, rubro, proveedor, check);
        stockAnterior = Double.parseDouble(texfieldStock.getText());
        descRespaldo = descProducto.getText();
        texfieldStock.setText("");
    }//GEN-LAST:event_listaProductoMouseClicked

    private void txtBuscarProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarProductoFocusGained
        txtBuscarProducto.selectAll();
    }//GEN-LAST:event_txtBuscarProductoFocusGained

    private void btnFacturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturarActionPerformed
        if (tbResumenVenta.getRowCount() == 0) {

            mostrarAlerta("No hay datos para guardar", 2);
        } else {
            ControladorProducto objetoVenta = new ControladorProducto();
            Mensaje mj = objetoVenta.guardaAjustes(tbResumenVenta);
            if (mj != null) {
                mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                total.setText("----");
            }
        }

    }//GEN-LAST:event_btnFacturarActionPerformed

    private void listaProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_listaProductoKeyTyped

    private void btnFacturarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFacturarMouseClicked

    }//GEN-LAST:event_btnFacturarMouseClicked

    private void prVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prVentaActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void texfieldStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_texfieldStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_texfieldStockActionPerformed

    public void verificarAntesDeAgregar() {
        if (!texfieldStock.getText().equals("")) {
            if (!prCosto.getText().equals("")) {
                if (!prVenta.getText().equals("")) {
                    if (!descProducto.getText().equals("")) {

                        ControladorAjustes cap = new ControladorAjustes();
                        if (indiceEditar < 0) {

                            cap.pasarPreciosGuardar(tbResumenVenta, txtBuscarProducto, codigoProducto, descProducto,
                                    prCosto, prVenta, texfieldStock, stockAnterior, proveedor, rubro, sumar, check, total);
   txtBuscarProducto.requestFocusInWindow();
                        } else {
                            cap.pasarPreciosEditar(tbResumenVenta, txtBuscarProducto, codigoProducto, descProducto,
                                    prCosto, prVenta, texfieldStock, stockAnterior, proveedor, rubro, sumar, check, total, indiceEditar);
                            indiceEditar = -1;
                        }

                    } else {
                        mostrarAlerta("La descripcion del producto no puede estar vacia", 3);
                        descProducto.setText(descRespaldo);

                    }
                } else {
                    mostrarAlerta("El precio de venta no puede esstar vacio", 3);
                }
            } else {
                mostrarAlerta("El precio de costo no puede estar vacio", 3);
            }
        } else {
            mostrarAlerta("El stock no puede estar vacio", 3);
        }
        
     
              if(check.equals("stock")){ 
                  if (!jCheckBox2.isSelected()) {
                                   txtBuscarProducto.requestFocusInWindow();
                    }}
                  
              
    }
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed

        verificarAntesDeAgregar();
    }//GEN-LAST:event_btnAgregarActionPerformed
    int indiceEditar = -1;
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        int indiceSelleccionado = tbResumenVenta.getSelectedRow();

        if (indiceSelleccionado != -1) {
            indiceEditar = indiceSelleccionado;
            Integer idSelecte = Integer.parseInt(tbResumenVenta.getValueAt(indiceSelleccionado, 0).toString());
            txtBuscarProducto.setText(tbResumenVenta.getValueAt(indiceSelleccionado, 1).toString());
            contenedorTabla.setVisible(false);
            codigoProducto.setText(idSelecte.toString());
            descProducto.setText(tbResumenVenta.getValueAt(indiceSelleccionado, 1).toString());
            prCosto.setText(tbResumenVenta.getValueAt(indiceSelleccionado, 2).toString());
            prVenta.setText(tbResumenVenta.getValueAt(indiceSelleccionado, 3).toString());
            texfieldStock.setText(tbResumenVenta.getValueAt(indiceSelleccionado, 5).toString());
            rubro.setText(tbResumenVenta.getValueAt(indiceSelleccionado, 6).toString());
            proveedor.setText(tbResumenVenta.getValueAt(indiceSelleccionado, 7).toString());
            if (negativo.isSelected() || sobreSaldo.isSelected()) {
                texfieldStock.requestFocusInWindow();
            } else {
                if (jCheckBox1.isSelected()) {
                    prCosto.requestFocusInWindow();
                }
            }
        } else {

            mostrarAlerta("Seleccione una fila para editar", 2);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Controlador.ControladorVenta objetoVenta = new ControladorVenta();
        Mensaje mj = objetoVenta.eliminarProductoSeleccionado(tbResumenVenta);
        if (mj != null) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
        if (negativo.isSelected()) {
            ControladorAjustes ca = new ControladorAjustes();
            ca.calcularMontoFactura(tbResumenVenta, total);
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void negativoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_negativoStateChanged

    }//GEN-LAST:event_negativoStateChanged
    String check = "factura";
    private void negativoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_negativoItemStateChanged
        if (negativo.isSelected()) {
            check = "factura";
            jCheckBox1.setSelected(false);
            sobreSaldo.setSelected(false);

            //  descProducto.setEditable(false);
            //     rubro.setEditable(false);
            //  proveedor.setEditable(false);
            prCosto.setEditable(true);
            prVenta.setEditable(true);
            texfieldStock.setEditable(true);

            ControladorAdministracion ca = new ControladorAdministracion();

            Color color = ca.retornatexFieldF();
            //   descProducto.setBackground(color);
            //  rubro.setBackground(color);
            //  proveedor.setBackground(color);
            texfieldStock.setBackground(ca.retornatexField());
            prCosto.setBackground(ca.retornatexField());
            prVenta.setBackground(ca.retornatexField());
        }
    }//GEN-LAST:event_negativoItemStateChanged

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        if (jCheckBox1.isSelected()) {
            check = "precio";
            negativo.setSelected(false);
            sobreSaldo.setSelected(false);

            //    descProducto.setEditable(false);
            //    rubro.setEditable(false);
            //   proveedor.setEditable(false);
            texfieldStock.setEditable(false);

            prCosto.setEditable(true);
            prVenta.setEditable(true);

            ControladorAdministracion ca = new ControladorAdministracion();

            Color color = ca.retornatexFieldF();
            //  descProducto.setBackground(color);
            //   rubro.setBackground(color);
            //  proveedor.setBackground(color);
            texfieldStock.setBackground(color);

            prCosto.setBackground(ca.retornatexField());
            prVenta.setBackground(ca.retornatexField());
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void sobreSaldoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sobreSaldoItemStateChanged
        if (sobreSaldo.isSelected()) {
            check = "stock";
            negativo.setSelected(false);
            jCheckBox1.setSelected(false);
            prCosto.setEditable(false);
            prVenta.setEditable(false);
            //  descProducto.setEditable(false);
            //   rubro.setEditable(false);
            //   proveedor.setEditable(false);

            texfieldStock.setEditable(true);
            ControladorAdministracion ca = new ControladorAdministracion();

            Color color = ca.retornatexFieldF();
            //   descProducto.setBackground(color);
            //    rubro.setBackground(color);
            //    proveedor.setBackground(color);
            prVenta.setBackground(color);
            prCosto.setBackground(color);

            texfieldStock.setBackground(ca.retornatexField());
        }
    }//GEN-LAST:event_sobreSaldoItemStateChanged

    private void txtBuscarProductoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarProductoFocusLost
        if (txtBuscarProducto.getText().equals("")) {
            codigoProducto.setText("");
            prCosto.setText("");
            prVenta.setText("");
            texfieldStock.setText("");
            descProducto.setText("");
            rubro.setText("");
            proveedor.setText("");
            contenedorTabla.setVisible(false);
        }

        if (!listaProducto.isColumnSelected(0)) {
            contenedorTabla.setVisible(false);
            if (!txtBuscarProducto.getText().equals("")) {
                /*
                Controlador.ControladorVenta objetoVenta = new ControladorVenta();
                Mensaje mj = objetoVenta.SeleccionarProductosVenta(listaProducto, fieldCantidad,
                        codigoProducto, txtBuscarProducto, prCosto, prVenta,
                        buscaCliente, contenedorTabla, texfieldStock, descripcionProd, 1);
                if (mj != null) {
                    mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                }
                 */
            } else {
                codigoProducto.setText("");
                prCosto.setText("");
                prVenta.setText("");
                texfieldStock.setText("");
                descProducto.setText("");
                rubro.setText("");
                proveedor.setText("");
            }

            if (txtBuscarProducto.getText().equals("")) {
                contenedorTabla.setVisible(false);
            }
        }


    }//GEN-LAST:event_txtBuscarProductoFocusLost
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
            activadorselectAll = false;
        }

    }
    boolean activadorselectAll = true;
    private void prCostoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prCostoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            prVenta.selectAll();
            prVenta.requestFocus();
            prVenta.requestFocusInWindow();
        }
    }//GEN-LAST:event_prCostoKeyPressed
    boolean sumar = true;
    private void prVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prVentaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            ControladorAjustes cap = new ControladorAjustes();
            if (jCheckBox2.isSelected()) {
                descProducto.selectAll();
                descProducto.requestFocus();
                descProducto.requestFocusInWindow();
            } else {
                verificarAntesDeAgregar();
                
            }

        }
    }//GEN-LAST:event_prVentaKeyPressed
    String descRespaldo = "";
    private void descProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (descProducto.getText().equals("")) {
                descProducto.setText(descRespaldo);

                mostrarAlerta("La desripcion del producto no puede estar vacia", 2);
            }
            if (jCheckBox2.isSelected()) {
                rubro.selectAll();
                rubro.requestFocus();
                rubro.requestFocusInWindow();
            }
        }
    }//GEN-LAST:event_descProductoKeyPressed

    private void proveedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_proveedorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ControladorAjustes cap = new ControladorAjustes();

            verificarAntesDeAgregar();
        }
    }//GEN-LAST:event_proveedorKeyPressed

    private void rubroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rubroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (jCheckBox2.isSelected()) {
                proveedor.selectAll();
                proveedor.requestFocus();
                proveedor.requestFocusInWindow();
            }
        }
    }//GEN-LAST:event_rubroKeyPressed

    private void texfieldStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_texfieldStockKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {

            verificarIngreso(texfieldStock, evt.getKeyChar());

        }
    }//GEN-LAST:event_texfieldStockKeyReleased

    private void prCostoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prCostoKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {

            verificarIngreso(prCosto, evt.getKeyChar());

        }        // TODO add your handling code here:
    }//GEN-LAST:event_prCostoKeyReleased

    private void prVentaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prVentaKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {

            verificarIngreso(prVenta, evt.getKeyChar());

        }
    }//GEN-LAST:event_prVentaKeyReleased

    private void texfieldStockFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_texfieldStockFocusGained
        if (activadorselectAll == true) {
            texfieldStock.selectAll();
        }
        activadorselectAll = true;
    }//GEN-LAST:event_texfieldStockFocusGained

    private void prCostoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_prCostoFocusGained
        if (activadorselectAll == true) {
            prCosto.selectAll();
        }
        activadorselectAll = true;
    }//GEN-LAST:event_prCostoFocusGained

    private void prVentaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_prVentaFocusGained
        if (activadorselectAll == true) {
            prVenta.selectAll();
        }
        activadorselectAll = true;
    }//GEN-LAST:event_prVentaFocusGained

    private void descProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descProductoFocusGained
        descProducto.selectAll();
    }//GEN-LAST:event_descProductoFocusGained

    private void rubroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rubroFocusGained
        rubro.selectAll();
    }//GEN-LAST:event_rubroFocusGained

    private void proveedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_proveedorFocusGained
        proveedor.selectAll();
    }//GEN-LAST:event_proveedorFocusGained

    private void jCheckBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox2ItemStateChanged
        ControladorAdministracion ca = new ControladorAdministracion();
        if (jCheckBox2.isSelected()) {

            descProducto.setEditable(true);
            rubro.setEditable(true);
            proveedor.setEditable(true);

            Color color = ca.retornatexField();
            descProducto.setBackground(color);
            rubro.setBackground(color);
            proveedor.setBackground(color);
        } else {

            descProducto.setEditable(false);
            rubro.setEditable(false);
            proveedor.setEditable(false);

            Color color = ca.retornatexFieldF();
            descProducto.setBackground(color);
            rubro.setBackground(color);
            proveedor.setBackground(color);

        }
    }//GEN-LAST:event_jCheckBox2ItemStateChanged

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnFacturar;
    private javax.swing.JTextField codigoProducto;
    private javax.swing.JScrollPane contenedorTabla;
    private javax.swing.JTextField descProducto;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable listaProducto;
    private javax.swing.JCheckBox negativo;
    private javax.swing.JTextField prCosto;
    private javax.swing.JTextField prVenta;
    private javax.swing.JTextField proveedor;
    private javax.swing.JTextField rubro;
    private javax.swing.JCheckBox sobreSaldo;
    private javax.swing.JTable tbResumenVenta;
    private javax.swing.JTextField texfieldStock;
    private javax.swing.JLabel total;
    private javax.swing.JTextField txtBuscarProducto;
    private javax.swing.JLabel txtTotal;
    // End of variables declaration//GEN-END:variables
}
