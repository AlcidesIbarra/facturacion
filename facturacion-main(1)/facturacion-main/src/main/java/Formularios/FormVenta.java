/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;
import Controlador.ControladorAsignarPrecio;
import Controlador.ControladorCliente;
import Controlador.ControladorVenta;
import Modelos.Mensaje;
import com.sun.java.accessibility.util.AWTEventMonitor;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
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
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Locale;
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
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author ALCIDES
 */
public class FormVenta extends javax.swing.JInternalFrame {

    /*
    
    CREATE TABLE stock_detalle (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    producto_id INTEGER,
    kg_individual REAL NOT NULL, estado VARCHAR(20) DEFAULT 'DISPONIBLE', fecha DATE,
    FOREIGN KEY (producto_id) REFERENCES productos(id)
)
    
    
    ALTER TABLE administrador ADD COLUMN nombre_img VARCHAR(250) DEFAULT 'fondo_defecto.png';
    
   ALTER TABLE factura ADD COLUMN estado VARCHAR(50) DEFAULT 'ACTIVA';
    
CREATE TABLE stock_detalle ( id INTEGER PRIMARY KEY AUTOINCREMENT, 
    producto_id INTEGER,
    kg_individual REAL NOT NULL, 
    estado VARCHAR(20) DEFAULT 'DISPONIBLE', 
    FOREIGN KEY (producto_id) REFERENCES productos(id) )); 
    
    
    ALTER TABLE detalle ADD COLUMN fk_stock_detalle INTEGER REFERENCES stock_detalle(id);
    
    ALTER TABLE stock_detalle ADD COLUMN fecha DATE;


     */
    private boolean bloqueadorListener = false;
    private String idPesoTemporal = "0";

    public FormVenta() {
        initComponents();
        configurarTablaVenta();
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        descripcionProd.setVisible(false);
      
        mensajeador.setVisible(false);
        labelCodCliente.setVisible(false);
        // 1. Configuración de visibilidad inicial
        contenedorTabla.setVisible(false);
        contenedorClientes.setVisible(false);
        descripcionProd.setVisible(false);

        // 2. Lógica de Venta
        Controlador.ControladorVenta objetoVenta = new ControladorVenta();
        objetoVenta.buscarNumeroFactura(factura, fechaFactura);
    
        if (!editaFacturar.isSelected()) {
            objetoVenta.bloquearEdicionPrecios(1,texfieldStock, prVenta, limiteCompra);
            btnSave.setEnabled(false);
            autosave.setSelected(true);
            autosave.setEnabled(false);
        }

        // 3. Alineación y Focus
        fechaFactura.setHorizontalAlignment(SwingConstants.CENTER);
        factura.setHorizontalAlignment(SwingConstants.CENTER);
        SwingUtilities.invokeLater(() -> this.buscaCliente.requestFocusInWindow());

        // 4. ESTILOS Y COLORES (Optimizado)
        ControladorAdministracion ca = new ControladorAdministracion();

        // Aplicamos el UI global primero
        ca.cambiacolor(this);

        // Obtenemos el color de fondo una sola vez para no consultar la BD en cada línea
        Color fondoBase = ca.retornatexFieldF();

        // Aplicamos el color a todos los campos de texto de un tirón
        javax.swing.JTextField[] camposFondo = {
            codigoProducto, factura,  prVenta, texfieldStock, fechaFactura
        };
        for (javax.swing.JTextField tf : camposFondo) {
            tf.setBackground(fondoBase);
        }

        paneObservaciones.setOpaque(false);

        // 5. Botones con Estilo Cristal (Agrupados para legibilidad)
        ca.BotonesCrisitalPrim(btnAgregar, Color.green, Color.black, Color.black);
        ca.BotonesCrisitalPrim(btnFacturar, Color.blue, Color.black, Color.black);
        ca.BotonesCrisitalPrim(btnSave, Color.YELLOW, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton1, Color.red, Color.black, Color.black);

        ca.BotonesCrisitalPrim(jButton2, Color.orange, Color.black, Color.black);

        // Aplicamos el UI una sola vez
        ca.BotonesCrisitalTogle(autosave, Color.CYAN, Color.BLACK, Color.BLACK);

        // Lo apagamos. Al ejecutarse esto, el paint dirá: "¡Ah! isEnabled es false, pinto gris"
        autosave.setEnabled(false);
        Dimension d = new Dimension(100, 40); // El tamaño que quieras
        btnFacturar.setPreferredSize(d);
        btnFacturar.setMinimumSize(d);
        btnFacturar.setMaximumSize(d);
        codigoProducto.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent e) {
                // Al cerrar la ventana interna, liberamos los pesos 'RESERVADOS'
              
            }
        });
        this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent e) {
                // 1. Limpiamos la base de datos (RESERVADO -> DISPONIBLE)
              

                // 2. AHORA SÍ: Cerramos y destruimos la ventana
                dispose();
            }
        });
contenedorTabla.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
contenedorTabla.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

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

    public void hacerrEditable(int num, JTextField fieldNombre,
            JTextField fieldDireccion, JTextField fieldDni, JTextField fiedTelefono,
            JTextField dias, JTextField textFieldStock,
            JTextField prVenta, JTextField limiteCompra, JTextPane paneObservaciones) {
        Boolean editar = true;
        if (num == 1) {
            editar = false;
        }
        fieldNombre.setEditable(editar);
        fieldDireccion.setEditable(editar);
        fieldDni.setEditable(editar);
        fieldTelefono.setEditable(editar);
        dias.setEditable(editar);
        paneObservaciones.setEditable(editar);

        texfieldStock.setEditable(editar);
        prVenta.setEditable(editar);
        limiteCompra.setEditable(editar);
        if (editar == false) {
            ControladorAdministracion ca = new ControladorAdministracion();
            texfieldStock.setBackground(ca.retornatexFieldF());
            prVenta.setBackground(ca.retornatexFieldF());
        }
    }

    public void abrirvistaFactura(Integer numero, JDesktopPane contenedor, int veridicador) {

        Dimension dpSize = contenedor.getSize();

        ModeloFactura frameB = new ModeloFactura(numero, veridicador);
        //   Dimension dpSize = this.getDesktopPane().getSize();
        Dimension frSize = frameB.getSize();
        contenedor.add(frameB); // Añade al escritorio padre

        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);

        frameB.setLocation(x - 10, y + 136);
        contenedor.setLayer(frameB, JLayeredPane.DRAG_LAYER);
        frameB.setVisible(false);
        //  frameB.toFront(); // Trae al frente la nueva ventana
        //   frameB.moveToFront();
        // frameB.verDetalleFactura(numero,0);
    }

    public void abrirPago() {

        FormFacturar frameB = new FormFacturar();
        Dimension dpSize = this.getDesktopPane().getSize();
        Dimension frSize = frameB.getSize();
        this.getDesktopPane().add(frameB); // Añade al escritorio padre

        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);

        frameB.setLocation(x - 10, y);
        frameB.setVisible(true);
        frameB.toFront(); // Trae al frente la nueva ventana

        BigDecimal deudaenvia = new BigDecimal("0.0");
        try {
            deudaenvia = new BigDecimal(deuda.getText().substring(2).replace(".", "").replace(",", "."));

            //   cuenta = Double.parseDouble(deuda.getText());
        } catch (Exception e) {
            System.out.println("            la deuda es nulllaaaaaaaaaaa");
        }
        Integer codcliente = null;
        try {
            codcliente = Integer.parseInt(buscaCliente.getText());
        } catch (Exception e) {
        }

        BigDecimal totalEnviar = new BigDecimal(total.getText().substring(2).replace(".", "").replace(",", "."));
        System.out.println(totalEnviar);

        frameB.probarDos(totalEnviar, metodoPago.getSelectedItem().toString(),
                fieldNombre.getText().toString(),
                deudaenvia, fieldObservaciones.getText(), codcliente,
                tbResumenVenta, this.getDesktopPane(), mensajeador);

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
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        deuda = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        saldoDisponible = new javax.swing.JLabel();
        limiteCompra = new javax.swing.JTextField();
        negativo = new javax.swing.JCheckBox();
        sobreSaldo = new javax.swing.JCheckBox();
        editaFacturar = new javax.swing.JCheckBox();
        saldoVencido = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        contenedorTabla = new javax.swing.JScrollPane();
        listaProducto = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        fieldCantidad = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        codigoProducto = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        contenedorClientes = new javax.swing.JScrollPane();
        listaClientes = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        prVenta = new javax.swing.JTextField();
        texfieldStock = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtBuscarProducto = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        fieldDireccion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        fieldDni = new javax.swing.JTextField();
        buscaCliente = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbResumenVenta = new javax.swing.JTable();
        fieldNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        factura = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        fieldObservaciones = new javax.swing.JTextField();
        fieldTelefono = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        fechaFactura = new javax.swing.JTextField();
        dias = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        metodoPago = new javax.swing.JComboBox<>();
        btnSave = new javax.swing.JButton();
        descripcionProd = new javax.swing.JLabel();
        autosave = new javax.swing.JToggleButton();
        labelCodCliente = new javax.swing.JLabel();
        labelVerificador = new javax.swing.JLabel();
        paneObservaciones = new javax.swing.JTextPane();
        mensajeador = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Facturar");
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        btnFacturar.setText("Facturar");
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

        jLabel29.setText("Cuenta Cte:");

        jLabel30.setText("Limite:");

        deuda.setText("----");

        jLabel25.setText("Saldo Disponible:");

        jLabel26.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel26.setText("TOTAL:");

        total.setText("----");

        saldoDisponible.setText("----");

        limiteCompra.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                limiteCompraFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                limiteCompraFocusLost(evt);
            }
        });
        limiteCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                limiteCompraKeyPressed(evt);
            }
        });

        negativo.setText("Facturar en Negativo");

        sobreSaldo.setText("Facturar sobre limite");

        editaFacturar.setText("Editar al Facturar");
        editaFacturar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                editaFacturarStateChanged(evt);
            }
        });
        editaFacturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editaFacturarActionPerformed(evt);
            }
        });

        saldoVencido.setText("Facturar sobre saldo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(negativo)
                    .addComponent(saldoVencido))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editaFacturar)
                    .addComponent(sobreSaldo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFacturar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(limiteCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deuda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saldoDisponible, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(negativo)
                            .addComponent(jLabel29)
                            .addComponent(deuda)
                            .addComponent(editaFacturar)
                            .addComponent(saldoDisponible)
                            .addComponent(jLabel25))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(limiteCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel30)
                                    .addComponent(jLabel26)
                                    .addComponent(total)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(saldoVencido)
                                    .addComponent(sobreSaldo))))))
                .addGap(0, 9, Short.MAX_VALUE))
        );

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

        jPanel1.add(contenedorTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 290, 20));

        jLabel9.setText("Buscar:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        fieldCantidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldCantidadFocusGained(evt);
            }
        });
        fieldCantidad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fieldCantidadMouseClicked(evt);
            }
        });
        fieldCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldCantidadKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fieldCantidadKeyReleased(evt);
            }
        });
        jPanel1.add(fieldCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 140, 110, -1));

        jLabel11.setText("Cantidad:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 140, -1, -1));

        codigoProducto.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        codigoProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        codigoProducto.setFocusable(false);
        codigoProducto.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                codigoProductoPropertyChange(evt);
            }
        });
        codigoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codigoProductoKeyPressed(evt);
            }
        });
        jPanel1.add(codigoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, 160, 20));

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 140, 90, 50));

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

        jPanel1.add(contenedorClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 170, 20));

        jLabel13.setText("Precio Venta:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 170, -1, -1));

        prVenta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                prVentaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                prVentaFocusLost(evt);
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
        jPanel1.add(prVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, 110, -1));

        texfieldStock.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                texfieldStockFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                texfieldStockFocusLost(evt);
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
        jPanel1.add(texfieldStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, 70, -1));

        jButton1.setText("Eliminar");
        jButton1.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 140, 80, 20));

        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 170, 80, 20));

        jLabel15.setText("Stock:");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, -1, -1));

        jLabel12.setText("Codigo:");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel1.setText("Factura:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 0, -1, -1));

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
        jPanel1.add(txtBuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 290, -1));

        jLabel16.setText("Dias a Pagar:");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 60, -1, 20));

        jLabel6.setText("Condicion Pago:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        fieldDireccion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldDireccionFocusLost(evt);
            }
        });
        fieldDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldDireccionActionPerformed(evt);
            }
        });
        fieldDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldDireccionKeyPressed(evt);
            }
        });
        jPanel1.add(fieldDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 30, 240, -1));

        jLabel7.setText("Observaciones:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 90, -1, -1));

        fieldDni.setAutoscrolls(false);
        fieldDni.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldDniFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldDniFocusLost(evt);
            }
        });
        fieldDni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldDniActionPerformed(evt);
            }
        });
        fieldDni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldDniKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fieldDniKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fieldDniKeyTyped(evt);
            }
        });
        jPanel1.add(fieldDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 170, 20));

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
        jPanel1.add(buscaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 0, 170, -1));

        jLabel5.setText("Direccion:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, -1, -1));

        tbResumenVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Descripcion", "Cantidad", "Precio venta", "Subtotal"
            }
        ));
        tbResumenVenta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tbResumenVentaFocusLost(evt);
            }
        });
        tbResumenVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbResumenVentaMouseClicked(evt);
            }
        });
        tbResumenVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbResumenVentaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbResumenVentaKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tbResumenVenta);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 850, 280));

        fieldNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldNombreFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldNombreFocusLost(evt);
            }
        });
        fieldNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldNombreKeyPressed(evt);
            }
        });
        jPanel1.add(fieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 170, -1));

        jLabel3.setText("Codigo Cliente:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        jLabel10.setText("Telefono:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 60, -1, -1));

        jLabel4.setText("Nombre:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        factura.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        factura.setFocusable(false);
        jPanel1.add(factura, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 100, -1));

        jLabel8.setText("CUIT / DNI:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        fieldObservaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldObservacionesActionPerformed(evt);
            }
        });
        jPanel1.add(fieldObservaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 90, 450, -1));

        fieldTelefono.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldTelefonoFocusLost(evt);
            }
        });
        fieldTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fieldTelefonoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fieldTelefonoKeyReleased(evt);
            }
        });
        jPanel1.add(fieldTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, 100, -1));

        jLabel2.setText("Fecha:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 0, -1, -1));

        fechaFactura.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        fechaFactura.setFocusable(false);
        jPanel1.add(fechaFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 0, 330, -1));

        dias.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                diasFocusLost(evt);
            }
        });
        dias.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                diasKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                diasKeyReleased(evt);
            }
        });
        jPanel1.add(dias, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 60, 40, -1));

        jLabel17.setText("    _______________________________________________________________________________________________________________________");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 860, 20));

        metodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Contado", "Transferencia", "Tarjeta" }));
        metodoPago.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                metodoPagoFocusLost(evt);
            }
        });
        metodoPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metodoPagoActionPerformed(evt);
            }
        });
        jPanel1.add(metodoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 140, -1));

        btnSave.setText("Guardar");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 140, 90, 20));
        jPanel1.add(descripcionProd, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 180, 30, 20));

        autosave.setText("Autoguardado");
        autosave.setMargin(new java.awt.Insets(2, 1, 2, 1));
        autosave.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                autosaveItemStateChanged(evt);
            }
        });
        jPanel1.add(autosave, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 170, 90, 20));
        jPanel1.add(labelCodCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 20, 20));
        jPanel1.add(labelVerificador, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 140, 20, 20));
        jPanel1.add(paneObservaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 30, 240, 50));

        mensajeador.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                mensajeadorPropertyChange(evt);
            }
        });
        jPanel1.add(mensajeador, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 170, 20, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buscaClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyTyped
    }//GEN-LAST:event_buscaClienteKeyTyped

    private void buscaClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyReleased

        Controlador.ControladorCliente objetoCliente = new ControladorCliente();
        boolean returno = objetoCliente.buscarCliente(buscaCliente, listaClientes, contenedorClientes);
        abreSlide = true;
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (returno == false) {
                if (!buscaCliente.getText().equals("")) {
                    mostrarAlerta("No se encontraron coincidencias", 2);
                }
            }

            fucuseado = false;

            if (returno == false && !buscaCliente.getText().equals("")) {
                contenedorClientes.setVisible(false);

                if (!editaFacturar.isSelected()) {

                    mostrarAlerta("No se encontraron coincidencias.", 2);
                    buscaCliente.requestFocus();
                    buscaCliente.selectAll();
                }

                String codigo = buscaCliente.getText();
                objetoCliente.limpiarCampos(fieldNombre, fieldDni,
                        fieldDireccion, fieldTelefono, buscaCliente,
                        limiteCompra, deuda, txtBuscarProducto, metodoPago,
                        paneObservaciones, dias, null, texfieldStock, prVenta, saldoDisponible, 1, labelCodCliente);
                buscaCliente.setText(codigo);
            }
            // JOptionPane.showMessageDialog(null, "enmterdetectado");
            if (buscaCliente.getText().equals("")) {

                objetoCliente.limpiarCampos(fieldNombre, fieldDni,
                        fieldDireccion, fieldTelefono, buscaCliente,
                        limiteCompra, deuda, txtBuscarProducto, metodoPago,
                        paneObservaciones, dias, null, texfieldStock, prVenta, saldoDisponible, 1, labelCodCliente);
                txtBuscarProducto.requestFocus();

                ControladorVenta cv = new ControladorVenta();
                cv.actualizarTablaCliente(tbResumenVenta, 0, prVenta);
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
    boolean abreSlide = false;
    private void buscaClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyPressed
    }//GEN-LAST:event_buscaClienteKeyPressed

    private void buscaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscaClienteActionPerformed

    }//GEN-LAST:event_buscaClienteActionPerformed

    private void fieldDniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldDniKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDniKeyTyped

    private void fieldDniKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldDniKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            editarClienteUnid("CUIT / DNI");
            this.requestFocusInWindow();
        } else {
            if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                    && evt.getKeyCode() != KeyEvent.VK_DELETE) {
                char c = evt.getKeyChar();
                String cadena = fieldDni.getText();

                if (Character.isDigit(c)) {
                    cadena = cadena.substring(0, cadena.length() - 1);

                } else {
                    mostrarAlerta("solo se aceptan numeros para este campo", 2);

                    cadena = fieldDni.getText();

                    if (cadena.length() > 1) {
                        cadena = cadena.substring(0, cadena.length() - 1);
                    } else {
                        cadena = "";
                    }
                    fieldDni.setText(cadena);
                    fieldDni.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_fieldDniKeyReleased

    private void fieldDniKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldDniKeyPressed


    }//GEN-LAST:event_fieldDniKeyPressed

    private void fieldDniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDniActionPerformed

    private void fieldDniFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDniFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDniFocusGained

    private void metodoPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metodoPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_metodoPagoActionPerformed

    private void contenedorClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contenedorClientesKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_contenedorClientesKeyPressed

    private void listaClientesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaClientesKeyReleased

    }//GEN-LAST:event_listaClientesKeyReleased

    private void listaClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaClientesKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            Controlador.ControladorCliente objetoCliente = new ControladorCliente();
            Mensaje mj = objetoCliente.SeleccionarCliente(listaClientes, fieldNombre, fieldDni, fieldDireccion,
                    fieldTelefono, buscaCliente, contenedorClientes,
                    limiteCompra, deuda, txtBuscarProducto, metodoPago, paneObservaciones, dias,
                    null, texfieldStock, prVenta,
                    saldoDisponible, 0, tbResumenVenta, total, labelCodCliente, codigoProducto,
                    codigoProducto, contenedorTabla, descripcionProd, editaFacturar, null, null, null);

            if (mj != null) {
                mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                contenedorClientes.setVisible(false);

            }
            txtBuscarProducto.requestFocus();
        } else {
            ///   txtBuscarProducto.requestFocus();
        }

    }//GEN-LAST:event_listaClientesKeyPressed

    private void listaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaClientesMouseClicked
        Mensaje mj = null;
        Controlador.ControladorCliente objetoCliente = new ControladorCliente();
        mj = objetoCliente.SeleccionarCliente(listaClientes, fieldNombre, fieldDni, fieldDireccion,
                fieldTelefono, buscaCliente, contenedorClientes,
                limiteCompra, deuda, txtBuscarProducto, metodoPago, paneObservaciones, dias,
                null, texfieldStock, prVenta, saldoDisponible,
                0, tbResumenVenta, total, labelCodCliente, codigoProducto,
                codigoProducto, contenedorTabla, descripcionProd, editaFacturar, null, null, null);
        if (mj != null) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
    }//GEN-LAST:event_listaClientesMouseClicked

    private void btnFacturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturarActionPerformed
        if (!descripcionProd.getText().equals("")) {
            Mensaje mj = null;
            Controlador.ControladorVenta objetoVenta = new ControladorVenta();

            if (!fieldCantidad.getText().equals("")) {
                // PASAMOS EL ID: Agregamos 'idPesoTemporal' como último parámetro
                if (indiceSeleccionadoEliminar != -1) {
                mj = objetoVenta.pasarPreciosGuardar(
    tbResumenVenta,           // JTable
    codigoProducto,           // JTextField (Código de barras)
    descripcionProd,          // JLabel o JTextField (Descripción)
    null,                  // JTextField (Costo - solo para cálculo interno si fuera necesario)
    prVenta,                  // JTextField (Precio Venta)
    fieldCantidad,            // JTextField (Cantidad)
    txtBuscarProducto,        // JTextField (Buscador para limpiar)
    texfieldStock,            // JTextField (Stock para validar)
    negativo,                 // JCheckBox
    sobreSaldo,               // JCheckBox
    deuda,                    // JLabel (Saldo actual del cliente)
    limiteCompra,             // JTextField (Límite de crédito)
    total,                    // JLabel (Total acumulado en la factura)
    buscaCliente,             // JTextField (Código de cliente)
    saldoVencido,             // JCheckBox (Cuenta vencida)
    indiceSeleccionadoEliminar, // int (Para editar fila si no es -1)
    metodoPago.getSelectedItem().toString() // String (Método de pago)
);

                    indiceSeleccionadoEliminar = -1;
                } else {
                      mj = objetoVenta.pasarPreciosGuardar(
    tbResumenVenta,           // JTable
    codigoProducto,           // JTextField (Código de barras)
    descripcionProd,          // JLabel o JTextField (Descripción)
    null,                  // JTextField (Costo - solo para cálculo interno si fuera necesario)
    prVenta,                  // JTextField (Precio Venta)
    fieldCantidad,            // JTextField (Cantidad)
    txtBuscarProducto,        // JTextField (Buscador para limpiar)
    texfieldStock,            // JTextField (Stock para validar)
    negativo,                 // JCheckBox
    sobreSaldo,               // JCheckBox
    deuda,                    // JLabel (Saldo actual del cliente)
    limiteCompra,             // JTextField (Límite de crédito)
    total,                    // JLabel (Total acumulado en la factura)
    buscaCliente,             // JTextField (Código de cliente)
    saldoVencido,             // JCheckBox (Cuenta vencida)
    indiceSeleccionadoEliminar, // int (Para editar fila si no es -1)
    metodoPago.getSelectedItem().toString() // String (Método de pago)
);// <--- ID AQUÍ
                }

                // REINICIO: Una vez agregado, volvemos el ID a "0" para el siguiente producto
                idPesoTemporal = "0";

                if (mj != null) {
                    mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                }

                objetoVenta.calcularTotalAPagar(tbResumenVenta, saldoDisponible, total, limiteCompra, deuda, metodoPago.getSelectedItem().toString());
            } else {
                mostrarAlerta("Ingresa una cantidad para agregar.", 2);
                fieldCantidad.requestFocus();
            }
        }

// Lógica de finalización de factura (abrir pago)
        if (!total.getText().equals("----")) {
            if (!total.getText().equals("$ 0,00")) {
                guardaNuevoCliente();
                abrirPago();
            } else {
                mostrarAlerta("La factura esta Vacia!!", 3);
            }
        } else {
            mostrarAlerta("La factura esta Vacia!!", 3);
        }
    }//GEN-LAST:event_btnFacturarActionPerformed

    private void btnFacturarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFacturarMouseClicked

    }//GEN-LAST:event_btnFacturarMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Controlador.ControladorVenta objetoVenta = new ControladorVenta();

        // El método ahora busca el ID en la col 7 y libera el stock_detalle
        Mensaje mj = objetoVenta.eliminarProductoSeleccionado(tbResumenVenta);

        if (mj != null) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }

        // Recalculamos el total de la venta
        objetoVenta.calcularTotalAPagar(tbResumenVenta,
                saldoDisponible, total, limiteCompra, deuda,
                metodoPago.getSelectedItem().toString());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        Mensaje mj = null;
        Controlador.ControladorVenta objetoVenta = new ControladorVenta();

        if (!codigoProducto.getText().equals("")) {
            if (!fieldCantidad.getText().equals("")) {

                // Llamamos al método pasando el idPesoTemporal al final
        mj = objetoVenta.pasarPreciosGuardar(
    tbResumenVenta,           // JTable
    codigoProducto,           // JTextField (Código de barras)
    descripcionProd,          // JLabel o JTextField (Descripción)
    null,                  // JTextField (Costo - solo para cálculo interno si fuera necesario)
    prVenta,                  // JTextField (Precio Venta)
    fieldCantidad,            // JTextField (Cantidad)
    txtBuscarProducto,        // JTextField (Buscador para limpiar)
    texfieldStock,            // JTextField (Stock para validar)
    negativo,                 // JCheckBox
    sobreSaldo,               // JCheckBox
    deuda,                    // JLabel (Saldo actual del cliente)
    limiteCompra,             // JTextField (Límite de crédito)
    total,                    // JLabel (Total acumulado en la factura)
    buscaCliente,             // JTextField (Código de cliente)
    saldoVencido,             // JCheckBox (Cuenta vencida)
    indiceSeleccionadoEliminar, // int (Para editar fila si no es -1)
    metodoPago.getSelectedItem().toString() // String (Método de pago)
);

                // IMPORTANTE: Después de agregar, reseteamos el ID a "0" 
                // para que el próximo producto no herede el ID anterior
                idPesoTemporal = "0";
                indiceSeleccionadoEliminar = -1;

                if (mj != null) {
                    mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                }
                txtBuscarProducto.requestFocusInWindow();
                objetoVenta.calcularTotalAPagar(tbResumenVenta, saldoDisponible, total, limiteCompra, deuda, metodoPago.getSelectedItem().toString());
            } else {
                mostrarAlerta("Ingresa una cantidad para agregar.", 2);
                fieldCantidad.requestFocus();
            }
        } else {
            fieldCantidad.setText("");
            mostrarAlerta("Ingresa una producto para agregar.", 2);
            txtBuscarProducto.requestFocus();
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void fieldCantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldCantidadKeyPressed
        Mensaje mj;
        Controlador.ControladorVenta objetoVenta = new ControladorVenta();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!codigoProducto.getText().equals("")) {

                if (!fieldCantidad.getText().equals("")) {

         mj = objetoVenta.pasarPreciosGuardar(
    tbResumenVenta,           // JTable
    codigoProducto,           // JTextField (Código de barras)
    descripcionProd,          // JLabel o JTextField (Descripción)
    null,                  // JTextField (Costo - solo para cálculo interno si fuera necesario)
    prVenta,                  // JTextField (Precio Venta)
    fieldCantidad,            // JTextField (Cantidad)
    txtBuscarProducto,        // JTextField (Buscador para limpiar)
    texfieldStock,            // JTextField (Stock para validar)
    negativo,                 // JCheckBox
    sobreSaldo,               // JCheckBox
    deuda,                    // JLabel (Saldo actual del cliente)
    limiteCompra,             // JTextField (Límite de crédito)
    total,                    // JLabel (Total acumulado en la factura)
    buscaCliente,             // JTextField (Código de cliente)
    saldoVencido,             // JCheckBox (Cuenta vencida)
    indiceSeleccionadoEliminar, // int (Para editar fila si no es -1)
    metodoPago.getSelectedItem().toString() // String (Método de pago)
);

                    indiceSeleccionadoEliminar = -1;
                    if (mj != null) {
                        mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                    }
                    txtBuscarProducto.requestFocusInWindow();
                    objetoVenta.calcularTotalAPagar(tbResumenVenta, saldoDisponible, total, limiteCompra, deuda, metodoPago.getSelectedItem().toString());
                } else {
                    mostrarAlerta("Ingresa una cantidad para agregar.", 2);
                    fieldCantidad.requestFocus();
                }
            } else {
                fieldCantidad.setText("");
                mostrarAlerta("Ingresa una producto para agregar.", 2);
                txtBuscarProducto.requestFocus();
            }
        }

    }//GEN-LAST:event_fieldCantidadKeyPressed

    private void fieldCantidadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fieldCantidadMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldCantidadMouseClicked

    private void listaProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_listaProductoKeyTyped
    int contadorerror = 0;
    private void listaProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !txtBuscarProducto.getText().equals("")) {
            evt.consume();

            Controlador.ControladorVenta objetoVenta = new ControladorVenta();
            objetoVenta.SeleccionarProductosVenta(listaProducto, fieldCantidad,
                    codigoProducto, txtBuscarProducto, prVenta,
                    buscaCliente, contenedorTabla, texfieldStock, descripcionProd, 0);

          
        }
    }//GEN-LAST:event_listaProductoKeyPressed

    private void listaProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaProductoMouseClicked
        Controlador.ControladorVenta objetoVenta = new ControladorVenta();
        objetoVenta.SeleccionarProductosVenta(listaProducto, fieldCantidad,
                codigoProducto, txtBuscarProducto,  prVenta,
                buscaCliente, contenedorTabla, texfieldStock, descripcionProd, 0);

    
    }//GEN-LAST:event_listaProductoMouseClicked

    private void txtBuscarProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyTyped
    }//GEN-LAST:event_txtBuscarProductoKeyTyped

    private void txtBuscarProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyReleased
        Controlador.ControladorVenta objetoVenta = new ControladorVenta();
    String texto = txtBuscarProducto.getText().trim();
    int tecla = evt.getKeyCode();
    Mensaje mj;

    // 1. SI ES ESCRITURA: Buscamos y limpiamos basura previa
    if (tecla != java.awt.event.KeyEvent.VK_ENTER && tecla != java.awt.event.KeyEvent.VK_DOWN) {
        objetoVenta.buscarProducto(txtBuscarProducto, listaProducto, contenedorTabla, "ACTIVOS");
        
        if (texto.isEmpty()) {
            contenedorTabla.setVisible(false);
            // RESETEO PREVENTIVO: Si el buscador está vacío, nada debe estar cargado
            codigoProducto.setText("");
            descripcionProd.setText("");
            indiceSeleccionadoEliminar = -1; 
        }
        return; 
    }

    // 2. SI PRESIONÓ ENTER
    if (tecla == java.awt.event.KeyEvent.VK_ENTER &&  !txtBuscarProducto.getText().equalsIgnoreCase("") ) {
        
        // Forzamos actualización de la lista de sugerencias
        objetoVenta.buscarProducto(txtBuscarProducto, listaProducto, contenedorTabla, "ACTIVOS");
        int cantidadProductos = listaProducto.getRowCount();

        if (cantidadProductos == 1) {
            // Aseguramos que la fila 0 sea la elegida
            listaProducto.setRowSelectionInterval(0, 0);
            fieldCantidad.setText("1");

            // Cargamos los datos del producto (esto llena codigoProducto, descripcionProd, prVenta)
            objetoVenta.SeleccionarProductosVenta(listaProducto, fieldCantidad,
                    codigoProducto, txtBuscarProducto, prVenta,
                    buscaCliente, contenedorTabla, texfieldStock, descripcionProd, 0);

            // VALIDACIÓN DE SEGURIDAD: Solo si el código recién cargado coincide con algo real
            if (!codigoProducto.getText().trim().isEmpty() && !codigoProducto.getText().equals("0")) {
                
                mj = objetoVenta.pasarPreciosGuardar(
                    tbResumenVenta, 
                    codigoProducto, 
                    descripcionProd, 
                    null, 
                    prVenta, 
                    fieldCantidad, 
                    txtBuscarProducto, 
                    texfieldStock, 
                    negativo, 
                    sobreSaldo, 
                    deuda, 
                    limiteCompra, 
                    total, 
                    buscaCliente, 
                    saldoVencido, 
                    indiceSeleccionadoEliminar, 
                    metodoPago.getSelectedItem().toString()
                );

                // --- RESETEO TOTAL DE VARIABLES TRAS AGREGAR ---
                idPesoTemporal = "0";
                indiceSeleccionadoEliminar = -1; // CRÍTICO: Para que no crea que sigue editando la misma fila

                if (mj != null) {
                    mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                }

                // LIMPIEZA FÍSICA DE CAMPOS (Para que el próximo Enter no encuentre datos viejos)
                txtBuscarProducto.setText("");
                codigoProducto.setText(""); 
                descripcionProd.setText("");
                prVenta.setText("");
                texfieldStock.setText("");
                contenedorTabla.setVisible(false);
                   fieldCantidad.setText("");
                
                // Actualizar cálculos
                objetoVenta.calcularTotalAPagar(tbResumenVenta, saldoDisponible, total, limiteCompra, deuda, metodoPago.getSelectedItem().toString());
                txtBuscarProducto.requestFocusInWindow();
            }

        } else if (cantidadProductos > 1) {
            // Si hay varios, obligamos al usuario a elegir con las flechas
            listaProducto.setRowSelectionInterval(0, 0);
            listaProducto.requestFocus();
        } else {
            // Si no hay productos, reseteamos todo para evitar que un ENTER fantasma agregue algo
            contenedorTabla.setVisible(false);
            codigoProducto.setText("");
            indiceSeleccionadoEliminar = -1;
        }
    }
    }//GEN-LAST:event_txtBuscarProductoKeyReleased
    private void procesarSeleccionUnica() {
        listaProducto.setRowSelectionInterval(0, 0);
        // AgregarProducto(); // Tu método para sumar a la venta
        txtBuscarProducto.setText("");
        contenedorTabla.setVisible(false);
        txtBuscarProducto.requestFocus();
    }
    private void txtBuscarProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyPressed
    }//GEN-LAST:event_txtBuscarProductoKeyPressed

    private void txtBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProductoActionPerformed

    }//GEN-LAST:event_txtBuscarProductoActionPerformed

    private void txtBuscarProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarProductoFocusGained

        fucuseadoprod = true;
        /*
        txtBuscarProducto.selectAll();
        if (!editaFacturar.isSelected()) {
            texfieldStock.setEditable(false);
            prVenta.setEditable(false);
            ControladorAdministracion ca = new ControladorAdministracion();
            texfieldStock.setBackground(ca.retornatexFieldF());
            prVenta.setBackground(ca.retornatexFieldF());
        }*/
    }//GEN-LAST:event_txtBuscarProductoFocusGained
    int indiceSeleccionadoEliminar = -1;
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Desea cancelar la venta actual?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            Controlador.ControladorVenta objetoVenta = new ControladorVenta();

            // 1. Liberamos todos los pesos de la base de datos
          

            // 2. Limpiamos la tabla visualmente
            DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
            modelo.setRowCount(0);

            // 3. Limpiamos totales y buscadores
            total.setText("$ 0,00");
            txtBuscarProducto.setText("");
            txtBuscarProducto.requestFocus();

            mostrarAlerta("Venta cancelada y productos liberados", 1);
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    int contadorfocusltos = 0;
    private void buscaClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscaClienteFocusLost
        System.out.println(abreSlide + "abreslide");
        if (abreSlide == true) {
            Mensaje mj = null;
            ControladorVenta cv = new ControladorVenta();
            Controlador.ControladorCliente objetoCliente = new ControladorCliente();
            if (!listaClientes.isColumnSelected(0)) {
                contenedorClientes.setVisible(false);
                if (!buscaCliente.getText().equals("")) {
                    fucuseado = false;
                    mj = objetoCliente.SeleccionarCliente(listaClientes, fieldNombre, fieldDni, fieldDireccion,
                            fieldTelefono, buscaCliente, contenedorClientes,
                            limiteCompra, deuda, txtBuscarProducto, metodoPago, paneObservaciones, dias,
                            null, texfieldStock, prVenta, saldoDisponible, 1,
                            tbResumenVenta, total, labelCodCliente, codigoProducto,
                            codigoProducto, contenedorTabla, descripcionProd, editaFacturar, null, null,null);
                    ejecutarFocusLostCliente = false;
                } else {
                    if (fucuseadoprod == true) {
                        mj = cv.actualizarTablaCliente(tbResumenVenta, 0, prVenta);
                        fucuseadoprod = false;
                    }

                    objetoCliente.limpiarCampos(fieldNombre, fieldDni, fieldDireccion,
                            fieldTelefono, buscaCliente, limiteCompra, deuda,
                            txtBuscarProducto, metodoPago, paneObservaciones,
                            dias, null, texfieldStock, prVenta, saldoDisponible, 1, labelCodCliente);
                    pintarLabel();
                }
            }

            if (buscaCliente.getText().equals("")) {
                contenedorClientes.setVisible(false);
                metodoPago.removeItem("Cuenta Corriente");
                //nuevo
                /*   mj = cv.actualizarTablaCliente(tbResumenVenta, 0, prVenta);
            objetoCliente.limpiarCampos(fieldNombre, fieldDni, fieldDireccion,
                    fieldTelefono, buscaCliente, limiteCompra, deuda,
                    txtBuscarProducto, metodoPago, paneObservaciones,
                    dias, null, texfieldStock, prVenta, saldoDisponible, 1, labelCodCliente);*/
            }

            if (mj != null) {
                mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            }
            abreSlide = false;
        }
    }//GEN-LAST:event_buscaClienteFocusLost
    public void pintarLabel() {
        ControladorAdministracion ca = new ControladorAdministracion();
        Color color = ca.retornatexField();
        fieldNombre.setBackground(color);
        fieldDireccion.setBackground(color);
        fieldTelefono.setBackground(color);
        fieldObservaciones.setBackground(color);
        fieldDni.setBackground(color);
        dias.setBackground(color);
        limiteCompra.setBackground(color);
    }

    boolean abreslideProducto = false;
    private void txtBuscarProductoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarProductoFocusLost

        if (abreslideProducto == true) {
            if (txtBuscarProducto.getText().equals("")) {
                codigoProducto.setText("");
             
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
                            codigoProducto, txtBuscarProducto, null, prVenta,
                            buscaCliente, contenedorTabla, texfieldStock, descripcionProd, 1, null);
                    if (mj != null) {
                        mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                    }

                } else {
                    codigoProducto.setText("");
                 
                    prVenta.setText("");
                    texfieldStock.setText("");
                    descripcionProd.setText("");
                    fieldCantidad.setText("");
                }

                if (txtBuscarProducto.getText().equals("")) {
                    contenedorTabla.setVisible(false);
                }
            }
            abreslideProducto = false;
        }

    }//GEN-LAST:event_txtBuscarProductoFocusLost

    private void fieldCantidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldCantidadKeyReleased

        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {
            char c = evt.getKeyChar();
            String cadena = fieldCantidad.getText();

            if (Character.isDigit(c) || c == '.') {
                cadena = cadena.substring(0, cadena.length() - 1);

                if (c == '.' && cadena.contains(".")) {

                    fieldCantidad.setText(cadena);
                }

                ControladorVenta cv = new ControladorVenta();
                //     boolean tienePesos = cv.buscarDetallesPesos(codigoProducto, tablaPesos, scrollPesos);
                //     scrollPesos.setVisible(true);

            } else {
                mostrarAlerta("solo se aceptan numeros para este campo", 2);

                cadena = fieldCantidad.getText();

                if (cadena.length() > 1) {
                    cadena = cadena.substring(0, cadena.length() - 1);
                } else {
                    cadena = "";
                }
                fieldCantidad.setText(cadena);
                fieldCantidad.requestFocus();
            }
        }

    }//GEN-LAST:event_fieldCantidadKeyReleased

    private void listaProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaProductoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_listaProductoKeyReleased

    private void editaFacturarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_editaFacturarStateChanged

    }//GEN-LAST:event_editaFacturarStateChanged

    private void editaFacturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editaFacturarActionPerformed
        boolean editable = editaFacturar.isSelected();

        // Al poner false, el método paint detectará (!c.isEnabled()) y se pondrá gris
        btnSave.setEnabled(editable);
        autosave.setEnabled(editable);

        ControladorCliente cc = new ControladorCliente();
        cc.hacerrEditable(editable ? 4 : 3, fieldNombre, fieldDireccion, fieldDni,
                fieldTelefono, dias, null, texfieldStock,
                prVenta, limiteCompra, paneObservaciones, metodoPago);
    }//GEN-LAST:event_editaFacturarActionPerformed

    private void prVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prVentaKeyPressed

    }//GEN-LAST:event_prVentaKeyPressed

    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    private void prVentaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prVentaKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                prVenta.setText(formatoMoneda.format(Double.parseDouble(prVenta.getText())));
            } catch (Exception e) {
            }

            if (editaFacturar.isSelected()) {
                if (autosave.isSelected()) {
                    if (!codigoProducto.getText().equals("")) {
                        ControladorAsignarPrecio cap = new ControladorAsignarPrecio();
                        cap.ActualizarUnPrecio(prVenta, null, labelCodCliente, codigoProducto);
                        //    fieldCantidad.requestFocus();
                    } else {
                        mostrarAlerta("Selecciona un producto para editar.", 3);
                        prVenta.setText("");
                        txtBuscarProducto.requestFocus();
                    }
                }
            }

        } else {
            if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                    && evt.getKeyCode() != KeyEvent.VK_DELETE) {
                char c = evt.getKeyChar();
                String cadena = prVenta.getText();

                if (Character.isDigit(c) || c == '.') {
                    cadena = cadena.substring(0, cadena.length() - 1);

                    if (c == '.' && cadena.contains(".")) {

                        prVenta.setText(cadena);
                    }
                } else {
                    mostrarAlerta("solo se aceptan numeros para este campo", 2);

                    cadena = prVenta.getText();

                    if (cadena.length() > 1) {
                        cadena = cadena.substring(0, cadena.length() - 1);
                    } else {
                        cadena = "";
                    }
                    prVenta.setText(cadena);
                    prVenta.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_prVentaKeyReleased
    String precio = "";
    private void prVentaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_prVentaFocusGained
        prVenta.selectAll();
        precio = prVenta.getText();
        habilitaedicion = true;
    }//GEN-LAST:event_prVentaFocusGained
    boolean habilitaedicion = false;
    private void prVentaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_prVentaFocusLost
        System.out.println(habilitaedicion);
        Mensaje mj = null;
        if (editaFacturar.isSelected() || habilitaedicion == true) {
            if (autosave.isSelected()) {

                if (!codigoProducto.getText().equals("")) {
                    try {
                        prVenta.setText(formatoMoneda.format(Double.parseDouble(prVenta.getText())));
                    } catch (Exception e) {
                    }
                    // System.out.println(precio);

                    if (!precio.equals(prVenta.getText())) {
                        ControladorAsignarPrecio cap = new ControladorAsignarPrecio();
                        mj = cap.ActualizarUnPrecio(prVenta, null, labelCodCliente, codigoProducto);
                        //  fieldCantidad.requestFocus();
                        if (!editaFacturar.isSelected()) {
                            prVenta.setEditable(false);
                            habilitaedicion = false;
                            ControladorAdministracion ca = new ControladorAdministracion();
                            texfieldStock.setBackground(ca.retornatexFieldF());
                            prVenta.setBackground(ca.retornatexFieldF());
                        }
                    }

                } else {
                    if (prVenta.isEditable()) {
                        mostrarAlerta("Selecciona un producto para editar.", 3);
                        prVenta.setText("");
                        txtBuscarProducto.requestFocus();
                    }
                }
            }
        }
        if (mj != null) {
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
    }//GEN-LAST:event_prVentaFocusLost

    private void listaClientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listaClientesFocusLost
        /*
        if (fucuseado == false) {
            // listaClientes.setVisible(false);
            contenedorClientes.setVisible(false);
            ControladorCliente cc = new ControladorCliente();
            Mensaje mj = cc.SeleccionarCliente(listaClientes, fieldNombre, fieldDni, fieldDireccion,
                    fieldTelefono, buscaCliente, contenedorClientes,
                    limiteCompra, deuda, txtBuscarProducto, metodoPago, paneObservaciones, dias,
                    null, texfieldStock, prVenta, saldoDisponible, 1,
                    tbResumenVenta, total, labelCodCliente, codigoProducto,
                    codigoProducto, contenedorTabla, descripcionProd, editaFacturar);

            if (mj != null) {
                mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            }

        }
        fucuseado = false;
         */
    }//GEN-LAST:event_listaClientesFocusLost

    private void contenedorClientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contenedorClientesFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_contenedorClientesFocusLost
    boolean fucuseado = false;
    private void listaClientesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listaClientesFocusGained
        // fucuseado=true;
    }//GEN-LAST:event_listaClientesFocusGained

    private void buscaClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscaClienteFocusGained
        fucuseado = true;
        buscaCliente.selectAll();
        System.out.println("gano foco");
    }//GEN-LAST:event_buscaClienteFocusGained
    boolean fucuseadoprod = false;

    private void listaProductoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listaProductoFocusLost
        /*
        if (fucuseadoprod == false) {

            contenedorTabla.setVisible(false);
            Controlador.ControladorVenta objetoVenta = new ControladorVenta();
            Mensaje mj = objetoVenta.SeleccionarProductosVenta(listaProducto, fieldCantidad,
                    codigoProducto, txtBuscarProducto, null, prVenta,
                    buscaCliente, contenedorTabla, texfieldStock, descripcionProd, 2);
            if (mj != null) {
                mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            }

        }
        fucuseadoprod = false;
         */
    }//GEN-LAST:event_listaProductoFocusLost
    boolean habilitaedicionStock = false;
    private void texfieldStockFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_texfieldStockFocusGained

        if (texfieldStock.isEditable()) {
            texfieldStock.selectAll();
            habilitaedicionStock = true;
        }
        // TODO add your handling code here:

    }//GEN-LAST:event_texfieldStockFocusGained

    private void texfieldStockFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_texfieldStockFocusLost
        if (editaFacturar.isSelected() || habilitaedicionStock == true) {

            if (autosave.isSelected()) {

                if (!codigoProducto.getText().equals("")) {
                    //  texfieldStock.setText(formatoMoneda.format(Double.parseDouble(prVenta.getText())));
                    ControladorAsignarPrecio cap = new ControladorAsignarPrecio();
                    Mensaje mj = cap.ActualizarUnStock(texfieldStock, codigoProducto);
                    if (mj != null) {
                        mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                    }

                    fieldCantidad.requestFocus();

                    if (!editaFacturar.isSelected()) {
                        texfieldStock.setEditable(false);
                        habilitaedicionStock = false;
                        ControladorAdministracion ca = new ControladorAdministracion();
                        texfieldStock.setBackground(ca.retornatexFieldF());
                    }
                } else {
                    if (prVenta.isEditable()) {
                        mostrarAlerta("Selecciona un producto para editar.", 3);
                        texfieldStock.setText("");
                        txtBuscarProducto.requestFocus();
                    }
                }
            }
        }

    }//GEN-LAST:event_texfieldStockFocusLost

    private void limiteCompraFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_limiteCompraFocusGained
        if (limiteCompra.isEditable()) {
            limiteCompra.selectAll();
            habilitaedicionlimite = true;
        }

    }//GEN-LAST:event_limiteCompraFocusGained

    private void limiteCompraFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_limiteCompraFocusLost
        editarClienteUnid("LIMITE DE COMPRA");
    }//GEN-LAST:event_limiteCompraFocusLost
    boolean habilitaedicionlimite = false;
    boolean habilitaedicionnombre = false;
    boolean habilitaedicionCuit = false;
    boolean habilitaediciondireccion = false;
    boolean habilitaediciontelefono = false;
    boolean habilitaedicionDias = false;
    boolean habilitaedicionobs = false;
    boolean habilitaedicionCondicion = false;

    private void editarClienteUnid(String parametro) {
        boolean verificador = false;
        switch (parametro) {
            case "LIMITE DE COMPRA":
                verificador = habilitaedicionlimite;
                break;
            case "NOMBRE":
                verificador = habilitaedicionnombre;
                break;
            case "CUIT / DNI":
                verificador = habilitaedicionCuit;
                break;
            case "DIRECCION":
                verificador = habilitaediciondireccion;
                break;
            case "TELEFONO":
                verificador = habilitaediciontelefono;
                break;
            case "DIAS A PAGAR":
                verificador = habilitaedicionDias;
                break;
            case "OBSERVACIONES":
                verificador = habilitaedicionobs;
                break;
            case "METODO DE PAGO":
                verificador = habilitaedicionCondicion;
                break;
            default:
                throw new AssertionError();
        }

        if (editaFacturar.isSelected() || verificador == true) {
            if (autosave.isSelected()) {
                if (!labelCodCliente.getText().equals("")) {
                    //  texfieldStock.setText(formatoMoneda.format(Double.parseDouble(prVenta.getText())));
                    ControladorCliente cc = new ControladorCliente();
                    Document doc = paneObservaciones.getDocument();
                    String obs = "";
                    try {
                        obs = doc.getText(0, doc.getLength());
                    } catch (Exception e) {
                    }
                    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
                    Double limite = 0d;
                    try {
                        limite = Double.parseDouble(limiteCompra.getText());
                        limiteCompra.setText(formatoMoneda.format(limite));
                    } catch (Exception e) {
                        limite = Double.parseDouble(limiteCompra.getText().substring(2).replace(".", "").replace(",", "."));
                    }
                    Mensaje mj = cc.agregarCliente(parametro, fieldNombre, fieldDireccion,
                            fieldTelefono, fieldDni, limite,
                            Double.parseDouble(deuda.getText().substring(2).replace(".", "").replace(",", ".")),
                            metodoPago, obs, 3, null, dias, Integer.parseInt(labelCodCliente.getText()), null, null);
                    // fieldCantidad.requestFocus();
                    if (mj != null) {
                        mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                    }

                    falsearTodas();

                } else {

                    txtBuscarProducto.requestFocus();

                    switch (parametro) {
                        case "LIMITE DE COMPRA":
                            if (prVenta.isEditable()) {
                                mostrarAlerta("Selecciona un Cliente para editar el " + parametro + ".", 3);
                                limiteCompra.setText("");
                            }
                            break;
                        case "NOMBRE":
                            if (fieldNombre.isEditable()) {
                                mostrarAlerta("Selecciona un Cliente para editar el " + parametro + ".", 3);
                                fieldNombre.setText("");
                            }
                            break;
                        case "CUIT / DNI":
                            if (fieldDni.isEditable()) {
                                mostrarAlerta("Selecciona un Cliente para editar el " + parametro + ".", 3);
                                fieldDni.setText("");
                            }
                            break;
                        case "DIRECCION":
                            if (fieldDireccion.isEditable()) {
                                mostrarAlerta("Selecciona un Cliente para editar la " + parametro + ".", 3);
                                fieldDireccion.setText("");
                            }
                            break;
                        case "TELEFONO":
                            if (fieldTelefono.isEditable()) {
                                mostrarAlerta("Selecciona un Cliente para editar el " + parametro + ".", 3);
                                fieldTelefono.setText("");
                            }
                            break;
                        case "DIAS A PAGAR":
                            if (dias.isEditable()) {
                                mostrarAlerta("Selecciona un Cliente para editar los " + parametro + ".", 3);
                                dias.setText("");
                            }
                            break;
                        case "OBSERVACIONES":
                            if (paneObservaciones.isEditable()) {
                                mostrarAlerta("Selecciona un Cliente para editar las " + parametro + ".", 3);
                                paneObservaciones.setText("");
                            }
                            break;
                        case "METODO DE PAGO":
                            if (codigoProducto.isEditable()) {
                                mostrarAlerta("Selecciona un Cliente para editar la " + parametro + ".", 3);
                                codigoProducto.setText("");
                            }
                            break;
                        default:
                            throw new AssertionError();
                    }

                }
            }
        }

    }

    private void falsearTodas() {
        if (!editaFacturar.isSelected()) {
            limiteCompra.setEditable(false);
            habilitaedicionlimite = false;
            fieldNombre.setEditable(false);
            habilitaedicionnombre = false;
            fieldDni.setEditable(false);
            habilitaedicionCuit = false;
            fieldDireccion.setEditable(false);
            habilitaediciondireccion = false;
            fieldTelefono.setEditable(false);
            habilitaediciontelefono = false;
            dias.setEditable(false);
            habilitaedicionDias = false;
            paneObservaciones.setEditable(false);
            habilitaedicionobs = false;
            metodoPago.setEditable(false);
            habilitaedicionCondicion = false;
            metodoPago.setEnabled(false);
            metodoPago.setFocusable(false);
        }
    }


    private void fieldNombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldNombreFocusLost

        if (!fieldNombre.getText().equals("")) {
            editarClienteUnid("NOMBRE");

        } else {
            if (!editaFacturar.isSelected()) {
                btnSave.setEnabled(false);
            }
            if (!labelCodCliente.equals("")) {
                try {
                    metodoPago.removeItem("Cuenta Corriente");
                } catch (Exception e) {
                }

            }

            if (editaFacturar.isSelected()) {
                fieldNombre.setText(nombreReserva);
                nombreReserva = "";
                mostrarAlerta("Debes Ingresar un nombre para actualizar el cliente", 2);
            }
        }


    }//GEN-LAST:event_fieldNombreFocusLost

    private void fieldDniFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDniFocusLost
        if (!fieldDni.getText().equals("")) {
            editarClienteUnid("CUIT / DNI");
        }      // TODO add your handling code here:
    }//GEN-LAST:event_fieldDniFocusLost

    private void fieldNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldNombreKeyPressed
        if (!fieldNombre.equals("")) {
            btnSave.setEnabled(true);
            Boolean existe = false;
            for (int i = 0; i < metodoPago.getItemCount(); i++) {
                if (metodoPago.getItemAt(i).equals("Cuenta Corriente")) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                metodoPago.addItem("Cuenta Corriente");
            }

        } else {
            metodoPago.removeItem("Cuenta Corriente");

        }

        if (evt.getKeyCode()
                == KeyEvent.VK_ENTER) {
            if (!fieldNombre.getText().equals("")) {
                editarClienteUnid("NOMBRE");
                this.requestFocusInWindow();
            } else {
                fieldNombre.setText(nombreReserva);
                nombreReserva = "";
                mostrarAlerta("Debes Ingresar un nombre para actualizar el cliente", 2);
            }
        }
    }//GEN-LAST:event_fieldNombreKeyPressed

    private void limiteCompraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_limiteCompraKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            editarClienteUnid("LIMITE DE COMPRA");
            this.requestFocusInWindow();
        }


    }//GEN-LAST:event_limiteCompraKeyPressed

    private void metodoPagoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_metodoPagoFocusLost
        editarClienteUnid("METODO DE PAGO");
    }//GEN-LAST:event_metodoPagoFocusLost
    private boolean verSiHaynuevo() {
        boolean verif = true;
        if (!fieldDni.getText().equals("")) {
            verif = false;
        }
        if (!fieldDireccion.getText().equals("")) {
            verif = false;
        }
        if (!fieldObservaciones.getText().equals("")) {
            verif = false;
        }
        if (!fieldTelefono.getText().equals("")) {
            verif = false;
        }
        if (!dias.getText().equals("")) {
            verif = false;
        }
        if (!limiteCompra.getText().equals("")) {
            verif = false;
        }

        return verif;
    }

    public void guardaNuevoCliente() {
        if (!labelCodCliente.getText().equals("1")) {
            if (!editaFacturar.isSelected()) {
                btnSave.setEnabled(false);
            }

            ControladorCliente cc = new ControladorCliente();
            Double limite = 0d;
            Document doc = paneObservaciones.getDocument();
            String obs = "";
            Mensaje mj = null;
            try {
                obs = doc.getText(0, doc.getLength());
            } catch (Exception e) {
            }

            try {
                limite = Double.parseDouble(limiteCompra.getText());
                limiteCompra.setText(formatoMoneda.format(limite));
            } catch (Exception e) {
                if (!limiteCompra.getText().equals("")) {
                    limite = Double.parseDouble(limiteCompra.getText().substring(2).replace(".", "").replace(",", "."));
                }
            }

            //   if (!labelCodCliente.getText().equals("") || !codigoProducto.getText().equals("")) {
            if (!labelCodCliente.getText().equals("") || !codigoProducto.getText().equals("")) {
                NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
                ControladorAsignarPrecio cap = new ControladorAsignarPrecio();

                if (!labelCodCliente.getText().equals("")) {
                    if (editaFacturar.isSelected()) {
                        mj = cc.agregarCliente("CLIENTE", fieldNombre, fieldDireccion,
                                fieldTelefono, fieldDni, limite,
                                Double.parseDouble(deuda.getText().substring(2).replace(".", "").replace(",", ".")),
                                metodoPago, obs, 3, null, dias, Integer.parseInt(labelCodCliente.getText()), null, null);
                    }

                } else {
                    mj = cc.agregarCliente("CLIENTE", fieldNombre, fieldDireccion,
                            fieldTelefono, fieldDni, limite,
                            Double.parseDouble(deuda.getText().substring(2).replace(".", "").replace(",", ".")),
                            metodoPago, obs, 3, null, dias, Integer.parseInt(labelCodCliente.getText()), null, null);

                }

                try {
                    prVenta.setText(formatoMoneda.format(Double.parseDouble(prVenta.getText())));
                } catch (Exception e) {
                }
                // System.out.println(precio);

                if (!codigoProducto.getText().equals("")) {

                    Mensaje mj1 = cap.ActualizarUnPrecio(prVenta, null, labelCodCliente, codigoProducto);

                    Mensaje mj2 = cap.ActualizarUnStock(texfieldStock, codigoProducto);
                    if (mj != null) {
                        if (mj.getCodigo() == 1) {
                            mj.setMensaje(mj.getMensaje() + ", " + mj1.getMensaje());
                        }
                    } else {
                        if (mj1 != null) {
                            mj = mj1;
                        }
                    }
                    if (mj != null) {
                        if (mj2.getCodigo() == 1) {
                            mj.setMensaje(mj.getMensaje() + ", " + mj2.getMensaje());
                        }
                    } else {
                        if (mj2 != null) {
                            mj = mj2;
                        }
                    }

                }

            } else {

                boolean verif = verSiHaynuevo();
                // if (verif == false) {

                if (!fieldNombre.getText().equals("")) {

                    System.out.println("    entrales");
                    int idCliente = 0;
                    try {
                        if (labelCodCliente.getText().equals("")) {
                            idCliente = Integer.parseInt(buscaCliente.getText());
                        }
                    } catch (Exception e) {
                        idCliente = 0;
                    }
                    System.out.println(idCliente + "anteriro idcleinet");
                    //    if (idCliente > 0) {
                    //         System.out.println("    entra a ifcleinte");
                    mj = cc.agregarClientePrim("NUEVO CLIENTE", fieldNombre, fieldDireccion,
                            fieldTelefono, fieldDni, limite,
                            0d, metodoPago, obs, 1, null, dias, idCliente, buscaCliente);
                    //     }else{
                    //       System.out.println("            asignaautommaticammente un codigo al cliente");
                    //    }

                } else {
                    if (verif == false) {
                        mostrarAlerta("Debes Ingresar un nombre para guardar un nuevo cliente", 2);
                    }
                }
                //   }

            }
            if (mj != null) {
                mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            }
        }
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        guardaNuevoCliente();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void texfieldStockKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_texfieldStockKeyPressed

    }//GEN-LAST:event_texfieldStockKeyPressed

    private void texfieldStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_texfieldStockKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (editaFacturar.isSelected() || habilitaedicionStock == true) {
                if (autosave.isSelected()) {

                    if (!codigoProducto.getText().equals("")) {
                        //  texfieldStock.setText(formatoMoneda.format(Double.parseDouble(prVenta.getText())));
                        ControladorAsignarPrecio cap = new ControladorAsignarPrecio();
                        Mensaje mj = cap.ActualizarUnStock(texfieldStock, codigoProducto);
                        if (mj != null) {
                            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
                        }

                        fieldCantidad.requestFocus();

                        if (!editaFacturar.isSelected()) {
                            texfieldStock.setEditable(false);
                            habilitaedicionStock = false;
                            ControladorAdministracion ca = new ControladorAdministracion();
                            texfieldStock.setBackground(ca.retornatexFieldF());
                        }
                    } else {
                        if (prVenta.isEditable()) {
                            mostrarAlerta("Selecciona un producto para editar.", 3);
                            texfieldStock.setText("");
                            txtBuscarProducto.requestFocus();
                        }
                    }
                }
            }
        } else {
            if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                    && evt.getKeyCode() != KeyEvent.VK_DELETE) {
                char c = evt.getKeyChar();
                String cadena = texfieldStock.getText();

                if (Character.isDigit(c) || c == '.') {
                    cadena = cadena.substring(0, cadena.length() - 1);

                    if (c == '.' && cadena.contains(".")) {

                        texfieldStock.setText(cadena);
                    }
                } else {
                    mostrarAlerta("solo se aceptan numeros para este campo", 2);

                    cadena = texfieldStock.getText();

                    if (cadena.length() > 1) {
                        cadena = cadena.substring(0, cadena.length() - 1);
                    } else {
                        cadena = "";
                    }
                    texfieldStock.setText(cadena);
                    texfieldStock.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_texfieldStockKeyReleased
    String nombreReserva = "";
    private void fieldNombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldNombreFocusGained
        nombreReserva = fieldNombre.getText();
    }//GEN-LAST:event_fieldNombreFocusGained

    private void tbResumenVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbResumenVentaKeyTyped

    }//GEN-LAST:event_tbResumenVentaKeyTyped

    private void tbResumenVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbResumenVentaKeyPressed

    }//GEN-LAST:event_tbResumenVentaKeyPressed

    private void tbResumenVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbResumenVentaMouseClicked

    }//GEN-LAST:event_tbResumenVentaMouseClicked

    private void tbResumenVentaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tbResumenVentaFocusLost

    }//GEN-LAST:event_tbResumenVentaFocusLost
    boolean ejecutarFocusLostCliente = true;
    private void buscaClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buscaClienteMouseClicked
        buscaCliente.setFocusable(true);
        boolean ejecutarFocusLostCliente = true;
        System.out.println("     sse activo " + ejecutarFocusLostCliente);
    }//GEN-LAST:event_buscaClienteMouseClicked

    private void autosaveItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_autosaveItemStateChanged
        /*
        ControladorAdministracion ca = new ControladorAdministracion();
        if (evt.getStateChange() == ItemEvent.SELECTED) {

            ca.BotonesCrisitalTogle(autosave, Color.cyan, Color.BLACK, Color.black);

        } else {
            ca.BotonesCrisitalTogle(autosave, Color.gray, Color.black, Color.black);

        } // TODO add your handling code here:
         */
    }//GEN-LAST:event_autosaveItemStateChanged

    private void mensajeadorPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_mensajeadorPropertyChange
        if (!mensajeador.getText().equals("")) {

            mostrarAlerta(mensajeador.getText(), 1);
            mensajeador.setText("");

            Controlador.ControladorCliente objetoCliente = new ControladorCliente();
            objetoCliente.SeleccionarCliente(listaClientes, fieldNombre, fieldDni, fieldDireccion,
                    fieldTelefono, buscaCliente, contenedorClientes,
                    limiteCompra, deuda, txtBuscarProducto, metodoPago, paneObservaciones, dias,
                    null, texfieldStock, prVenta, saldoDisponible, 1,
                    tbResumenVenta, total, labelCodCliente, codigoProducto,
                    codigoProducto, contenedorTabla, descripcionProd, editaFacturar, null, null,null);
            ejecutarFocusLostCliente = false;
            total.setText("----");
            Controlador.ControladorVenta objetoVenta = new ControladorVenta();
            objetoVenta.buscarNumeroFactura(factura, fechaFactura);
        }
    }//GEN-LAST:event_mensajeadorPropertyChange

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

        // Si el usuario hace click en el fondo de la ventana
    

        // También podés aprovechar para cerrar otras sugerencias
        if (contenedorTabla.isVisible()) {
            contenedorTabla.setVisible(false);
        }
        if (contenedorClientes.isVisible()) {
            contenedorTabla.setVisible(false);
        }

    }//GEN-LAST:event_formMouseClicked

    private void codigoProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoProductoKeyPressed

    }//GEN-LAST:event_codigoProductoKeyPressed

    private void codigoProductoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_codigoProductoPropertyChange

    }//GEN-LAST:event_codigoProductoPropertyChange

    private void fieldObservacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldObservacionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldObservacionesActionPerformed

    private void fieldDireccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldDireccionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            editarClienteUnid("DIRECCION");
            this.requestFocusInWindow();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDireccionKeyPressed

    private void fieldDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldDireccionActionPerformed

    private void fieldDireccionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDireccionFocusLost
        if (!fieldDireccion.getText().equals("")) {
            editarClienteUnid("DIRECCION");
        }
    }//GEN-LAST:event_fieldDireccionFocusLost

    private void fieldTelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldTelefonoKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            editarClienteUnid("TELEFONO");
            this.requestFocusInWindow();
        } else {
            if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                    && evt.getKeyCode() != KeyEvent.VK_DELETE) {
                char c = evt.getKeyChar();
                String cadena = fieldTelefono.getText();

                if (Character.isDigit(c)) {
                    cadena = cadena.substring(0, cadena.length() - 1);

                } else {
                    mostrarAlerta("solo se aceptan numeros para este campo", 2);

                    cadena = fieldTelefono.getText();

                    if (cadena.length() > 1) {
                        cadena = cadena.substring(0, cadena.length() - 1);
                    } else {
                        cadena = "";
                    }
                    fieldTelefono.setText(cadena);
                    fieldTelefono.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_fieldTelefonoKeyReleased

    private void fieldTelefonoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldTelefonoKeyPressed

    }//GEN-LAST:event_fieldTelefonoKeyPressed

    private void fieldTelefonoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldTelefonoFocusLost
        if (!fieldTelefono.getText().equals("")) {
            editarClienteUnid("TELEFONO");
        }
    }//GEN-LAST:event_fieldTelefonoFocusLost

    private void diasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_diasKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            editarClienteUnid("DIAS A PAGAR");
            this.requestFocusInWindow();
        } else {
            if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                    && evt.getKeyCode() != KeyEvent.VK_DELETE) {
                char c = evt.getKeyChar();
                String cadena = dias.getText();

                if (Character.isDigit(c)) {
                    cadena = cadena.substring(0, cadena.length() - 1);

                } else {
                    mostrarAlerta("solo se aceptan numeros para este campo", 2);

                    cadena = dias.getText();

                    if (cadena.length() > 1) {
                        cadena = cadena.substring(0, cadena.length() - 1);
                    } else {
                        cadena = "";
                    }
                    dias.setText(cadena);
                    dias.requestFocus();
                }
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_diasKeyReleased

    private void diasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_diasKeyPressed

    }//GEN-LAST:event_diasKeyPressed

    private void diasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_diasFocusLost
        if (!dias.getText().equals("")) {
            editarClienteUnid("DIAS A PAGAR");
        }
    }//GEN-LAST:event_diasFocusLost

    private void fieldCantidadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCantidadFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldCantidadFocusGained
private void configurarTablaVenta() {
    String[] titulos = {"Código de Barras", "Descripción", "Cantidad", "Precio Venta", "Subtotal"};
    DefaultTableModel modelo = new DefaultTableModel(null, titulos) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    };
    tbResumenVenta.setModel(modelo);

    // 1. Configuramos los anchos de columna
    tbResumenVenta.getColumnModel().getColumn(0).setPreferredWidth(90);
    tbResumenVenta.getColumnModel().getColumn(1).setPreferredWidth(310);
    tbResumenVenta.getColumnModel().getColumn(2).setPreferredWidth(80); 
    tbResumenVenta.getColumnModel().getColumn(3).setPreferredWidth(100);
    tbResumenVenta.getColumnModel().getColumn(4).setPreferredWidth(100);

    // 2. Definimos el Renderer con margen (Padding)
    // Creamos una clase anónima que añade un borde vacío de 10px a la derecha
    DefaultTableCellRenderer rendererConMargen = new DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Añadimos el margen derecho (Top, Left, Bottom, Right)
            setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
            return this;
        }
    };

    // 3. Aplicamos alineaciones y el margen
    
    // Cantidad: Centrado con margen (aunque al estar centrado el margen lo desplazará levemente a la izquierda)
    // Si prefieres que esté a la derecha con margen, cambia a SwingConstants.RIGHT
    rendererConMargen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    tbResumenVenta.getColumnModel().getColumn(2).setCellRenderer(rendererConMargen);

    // Precio Venta: Derecha (Sin margen específico si no lo pediste)
    DefaultTableCellRenderer derechaSimple = new DefaultTableCellRenderer();
    derechaSimple.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    tbResumenVenta.getColumnModel().getColumn(3).setCellRenderer(derechaSimple);

    // Subtotal: Derecha con margen de 10px
    // Usamos otro renderer con margen pero alineado a la derecha
    DefaultTableCellRenderer subtotalRenderer = new DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
            setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            return this;
        }
    };
    tbResumenVenta.getColumnModel().getColumn(4).setCellRenderer(subtotalRenderer);
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton autosave;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnFacturar;
    private javax.swing.JButton btnSave;
    private javax.swing.JTextField buscaCliente;
    private javax.swing.JTextField codigoProducto;
    private javax.swing.JScrollPane contenedorClientes;
    private javax.swing.JScrollPane contenedorTabla;
    private javax.swing.JLabel descripcionProd;
    private javax.swing.JLabel deuda;
    private javax.swing.JTextField dias;
    private javax.swing.JCheckBox editaFacturar;
    private javax.swing.JTextField factura;
    private javax.swing.JTextField fechaFactura;
    private javax.swing.JTextField fieldCantidad;
    private javax.swing.JTextField fieldDireccion;
    private javax.swing.JTextField fieldDni;
    private javax.swing.JTextField fieldNombre;
    private javax.swing.JTextField fieldObservaciones;
    private javax.swing.JTextField fieldTelefono;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelCodCliente;
    private javax.swing.JLabel labelVerificador;
    private javax.swing.JTextField limiteCompra;
    private javax.swing.JTable listaClientes;
    private javax.swing.JTable listaProducto;
    private javax.swing.JLabel mensajeador;
    private javax.swing.JComboBox<String> metodoPago;
    private javax.swing.JCheckBox negativo;
    private javax.swing.JTextPane paneObservaciones;
    private javax.swing.JTextField prVenta;
    private javax.swing.JLabel saldoDisponible;
    private javax.swing.JCheckBox saldoVencido;
    private javax.swing.JCheckBox sobreSaldo;
    private javax.swing.JTable tbResumenVenta;
    private javax.swing.JTextField texfieldStock;
    private javax.swing.JLabel total;
    private javax.swing.JTextField txtBuscarProducto;
    // End of variables declaration//GEN-END:variables
}
