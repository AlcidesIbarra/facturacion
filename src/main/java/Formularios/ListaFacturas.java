/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;
import Controlador.ControladorCliente;
import Controlador.ControladorEstadisticas;
import Controlador.ControladorFacturas;
import Controlador.ControladorProducto;
import Controlador.ControladorVenta;
import Controlador.RoundTextoUI;
import Modelos.Mensaje;
import Modelos.ModeloCliente;
import Modelos.ModeloProducto;
import com.toedter.calendar.JCalendar;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ALCIDES
 */
public class ListaFacturas extends javax.swing.JInternalFrame {

    /**
     * Creates new form ListaFacturas
     */
    java.sql.Date desde1 = null;
    java.sql.Date hasta1 = null;
    Integer cliente = null;
    Integer factura = null;
    private JTable tabla;
    private DefaultTableModel modelo;

    public ListaFacturas(Integer num) {

        initComponents();
        contenedorClientes.setVisible(false);

       verificador.setVisible(false);
        ControladorFacturas objetoProducto = new ControladorFacturas();
        ControladorEstadisticas ce = new ControladorEstadisticas();
        jButton6.setVisible(false);

        objetoProducto.ponerMaxFecha(hasta);
        objetoProducto.ponerMaxFecha(desde);

        ControladorAdministracion ca = new ControladorAdministracion();
        ca.cambiacolor(this);
        ca.BotonesCrisitalPrim(jButton5, Color.blue, Color.black, Color.black);
                ca.BotonesCrisitalPrim(delete, Color.gray, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton4, Color.RED, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton6, Color.RED, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton1, Color.green, Color.black, Color.black);
        ca.BotonesCrisitalPrim(printBtn, Color.orange, Color.black, Color.black);
         ca.BotonesCrisitalPrim(jButton2, Color.cyan, Color.black, Color.black);
        JTextField editor = ((JTextField) desde.getDateEditor().getUiComponent());
        JTextField editor1 = ((JTextField) hasta.getDateEditor().getUiComponent());
        RoundTextoUI.color = ca.retornatexFieldF();
        RoundTextoUI.radio = 6;
        editor.setUI(new RoundTextoUI());
        editor1.setUI(new RoundTextoUI());

        identificador.setHorizontalAlignment(JLabel.CENTER);

        identificador.setFont(new java.awt.Font("Arial Black", java.awt.Font.BOLD, 24));
        identificador.setOpaque(false);

        identificador.setUI(new javax.swing.plaf.basic.BasicLabelUI() {
            @Override
            public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();

                // Calidad de dibujo profesional
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                JLabel lbl = (JLabel) c;
                String texto = lbl.getText() != null ? lbl.getText().toUpperCase() : "";
                java.awt.FontMetrics fm = g2.getFontMetrics(lbl.getFont());

                int x = (lbl.getWidth() - fm.stringWidth(texto)) / 2;
                int y = (lbl.getHeight() + fm.getAscent()) / 2 - 3;

                // --- A. FONDO "HIELO AZULADO" (CRISTAL FRÍO) ---
                // Azul oscuro translúcido (Contrasta con cualquier fondo claro u oscuro)
                g2.setColor(new java.awt.Color(0, 40, 80, 200));
                g2.fillRoundRect(2, 2, lbl.getWidth() - 5, lbl.getHeight() - 5, 12, 12);

                // Borde de Escarcha (Blanco brillante tipo vidrio)
                g2.setStroke(new java.awt.BasicStroke(2f));
                g2.setColor(new java.awt.Color(200, 240, 255, 180));
                g2.drawRoundRect(2, 2, lbl.getWidth() - 5, lbl.getHeight() - 5, 12, 12);

                // --- B. RESPLANDOR ÁRTICO (GLOW CIAN) ---
                g2.setColor(new java.awt.Color(0, 255, 255, 80));
                g2.drawString(texto, x + 1, y + 1);

                // --- C. TEXTO PRINCIPAL (DEGRADADO DE HIELO) ---
                // De Blanco Puro (arriba) a Cian/Azul Claro (abajo)
                java.awt.GradientPaint gradienteHielo = new java.awt.GradientPaint(
                        x, y - fm.getAscent(), java.awt.Color.WHITE,
                        x, y, new java.awt.Color(0, 200, 255)
                );
                g2.setPaint(gradienteHielo);

                // Dibujamos el texto final
                g2.drawString(texto, x, y);

                g2.dispose();
            }
        });
delete.setVisible(false);
// Configuración de texto
        verificador.setText(num.toString());
        switch (num) {
            case 1:
                jButton2.setVisible(false);
                identificador.setText("LISTA DE FACTURAS:");
                objetoProducto.muestraFacturas(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                filtrador.removeAllItems();
                filtrador.addItem("TODOS");
                filtrador.addItem("CONTADO");
                filtrador.addItem("TARJETA");
                 filtrador.addItem("TRANSFERENCIA");
                filtrador.addItem("CUENTA CORRIENTE");
                break;
            case 2:
                identificador.setText("LISTA DE PAGOS:");
                filtrador.setVisible(false);
                filtrador.removeAllItems();
                filtrador.addItem("TODOS");
                filtrador.addItem("PAGA SALDO");
                filtrador.addItem("PAGA FACTURA DEL DIA");

                ce.consultaPagos(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                modelo = (DefaultTableModel) tablaFacturas.getModel();
                tabla = tablaFacturas;
                configurarCambios();
                break;
            case 3:
                identificador.setText("VENTAS POR PRODUCTO POR FECHA:");
                filtrador.setVisible(false);
                ce.consultaindividual(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                break;

            case 4:
                identificador.setText("VENTAS TOTALES POR FECHA / GANANCIAS:");
                printBtn.setVisible(false);
                jButton5.setVisible(false);
                jButton4.setVisible(false);
                jTextField1.setVisible(false);
                filtrador.setVisible(false);
                jButton6.setVisible(true);
                jLabel4.setVisible(false);
                filtrador.setVisible(false);
                ce.consultaTotales(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                break;
            case 5:
                identificador.setText("VENTAS POR CLIENTE POR FECHA:");
                printBtn.setVisible(false);
                jButton5.setVisible(false);
                jButton4.setVisible(false);
                jTextField1.setVisible(false);
                filtrador.setVisible(false);
                jButton6.setVisible(true);
                jLabel4.setVisible(false);
                ce.consultaTotalesCliente(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                break;
            case 6:
                identificador.setText("LISTA DE SALDOS TOTALES:");
                filtrador.setVisible(false);
                jLabel2.setVisible(false);
                jLabel3.setVisible(false);
                desde.setVisible(false);
                hasta.setVisible(false);
                printBtn.setVisible(false);
                jButton5.setVisible(false);
                jButton4.setVisible(false);
                jButton6.setVisible(true);
                jLabel4.setVisible(false);
                jTextField1.setVisible(false);
                ce.consultaDeudaTotal(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                break;
            case 7:
                identificador.setText("LISTA DE SALDOS POR CLIENTE:");
                filtrador.setVisible(false);
                ce.consultaDeudaporCliente(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                break;
            case 8:
                identificador.setText("SALDOS VENCIDOS POR CLIENTE:");
                filtrador.setVisible(false);
                ce.consultaDeudaporCliente(tablaFacturas, "VENCIDO", desde1, hasta1, cliente, factura);
                break;
                            case 9:
                identificador.setText("VENTAS POR CLIENTE DETALLAD POR FECHA:");
                printBtn.setVisible(false);
                jButton5.setVisible(false);
                jButton4.setVisible(false);
                jTextField1.setVisible(false);
                filtrador.setVisible(false);
                jButton6.setVisible(true);
                jLabel4.setVisible(false);
                ce.consultaDetallePesosPorCliente(tablaFacturas, desde1, hasta1, cliente);
                break;
                
            default:
                throw new AssertionError();

        }
        // objetoProducto.muestraFacturas(tablaFacturas, "TODOS", desde1, hasta1, cliente);

        Dimension d = new Dimension(100, 22); // El tamaño que quieras
        jButton1.setPreferredSize(d);
        jButton1.setMinimumSize(d);
        jButton1.setMaximumSize(d);

                jButton2.setPreferredSize(d);
        jButton2.setMinimumSize(d);
        jButton2.setMaximumSize(d);
        
        jButton4.setPreferredSize(d);
        jButton4.setMinimumSize(d);
        jButton4.setMaximumSize(d);

        printBtn.setPreferredSize(d);
        printBtn.setMinimumSize(d);
        printBtn.setMaximumSize(d);

        jButton6.setPreferredSize(d);
        jButton6.setMinimumSize(d);
        jButton6.setMaximumSize(d);

        jButton5.setPreferredSize(d);
        jButton5.setMinimumSize(d);
        jButton5.setMaximumSize(d);
        
             jButton1.setPreferredSize(d);
        jButton1.setMinimumSize(d);
        jButton1.setMaximumSize(d);
        
          delete.setPreferredSize(d);
         delete.setMinimumSize(d);
       delete.setMaximumSize(d);

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

    private void configurarCambios() {
        modelo.addTableModelListener(e -> {
            int fila = e.getFirstRow();
            int column = e.getColumn();
            if (column == 4) {
                Object valor = modelo.getValueAt(fila, column);
                abrirvistaFactura(Integer.parseInt(valor.toString()), 1);
            }
        });

    }

    public void abrirvistaFactura(Integer numero, int printable) {

        ModeloFactura frameB = new ModeloFactura(numero, printable);
        Dimension dpSize = this.getDesktopPane().getSize();
        Dimension frSize = frameB.getSize();
        this.getDesktopPane().add(frameB); // Añade al escritorio padre

        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);

        frameB.setLocation(x - 10, y + 136);
        frameB.setVisible(true);
        frameB.toFront(); // Trae al frente la nueva ventana

        // frameB.verDetalleFactura(numero, printable);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        buscaCliente = new javax.swing.JTextField();
        contenedorClientes = new javax.swing.JScrollPane();
        listaClientes = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        filtrador = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        desde = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        hasta = new com.toedter.calendar.JDateChooser();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaFacturas = new javax.swing.JTable();
        verificador = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        printBtn = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        identificador = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        delete = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Buscar:");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        buscaCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscaClienteFocusGained(evt);
            }
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
        jPanel2.add(buscaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 170, -1));

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

        jPanel2.add(contenedorClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 170, 20));

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 110, -1));

        filtrador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                filtradorItemStateChanged(evt);
            }
        });
        filtrador.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                filtradorFocusGained(evt);
            }
        });
        filtrador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filtradorMouseClicked(evt);
            }
        });
        jPanel2.add(filtrador, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 70, 173, -1));

        jButton4.setText("Limpiar Filtros");
        jButton4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, 110, -1));

        desde.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                desdeHierarchyChanged(evt);
            }
        });
        desde.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                desdeFocusGained(evt);
            }
        });
        desde.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                desdePropertyChange(evt);
            }
        });
        jPanel2.add(desde, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 126, 20));

        jLabel2.setText("Desde:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jLabel3.setText("Hasta:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, -1, -1));

        hasta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                hastaFocusGained(evt);
            }
        });
        hasta.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                hastaPropertyChange(evt);
            }
        });
        jPanel2.add(hasta, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 70, 121, 20));

        jButton5.setText("VER");
        jButton5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 40, 70, -1));

        tablaFacturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaFacturasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaFacturas);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 690, 490));
        jPanel2.add(verificador, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, 30, 20));

        jLabel4.setText("Factura:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, -1, -1));

        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, 60, -1));

        printBtn.setText("Imprimir");
        printBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        printBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBtnActionPerformed(evt);
            }
        });
        jPanel2.add(printBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 40, 90, -1));

        jButton6.setText("Limpiar Filtros");
        jButton6.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 40, 110, -1));

        identificador.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        identificador.setText("LISTA FACTURAS");
        identificador.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel2.add(identificador, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 40));

        jButton2.setText("Imprimir Tabla");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 70, 120, -1));

        delete.setText("Anular");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        jPanel2.add(delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 110, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 9, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void filtradorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_filtradorMouseClicked
    }//GEN-LAST:event_filtradorMouseClicked

    int contador = 0;
    private void filtradorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filtradorItemStateChanged

        contador++;
        if (contador == 2) {

        }

        int codVer = Integer.parseInt(verificador.getText());
        switch (codVer) {
            case 1:

                ControladorFacturas objetoProducto = new ControladorFacturas();
                objetoProducto.muestraFacturas(tablaFacturas,
                        filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);
                contador = 0;
                break;
            case 2:
                ControladorEstadisticas ce = new ControladorEstadisticas();
                ce.consultaPagos(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);
                contador = 0;
                break;

            default:
                throw new AssertionError();
        }


    }//GEN-LAST:event_filtradorItemStateChanged

    private void tablaFacturasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaFacturasMouseClicked
   
        if(Integer.parseInt(verificador.getText())==1){
            delete.setVisible(true);
        jButton1.setVisible(false);
    }

        
        ControladorEstadisticas ce = new ControladorEstadisticas();
        if (evt.getClickCount() == 2) {

            int codVer = Integer.parseInt(verificador.getText());
            switch (codVer) {
                case 1:

                    ControladorFacturas objetoProducto = new ControladorFacturas();
                    int numero = objetoProducto.verDetalle(tablaFacturas);
                    abrirvistaFactura(numero, 1);
                    break;
                case 2:

                    int numero2 = ce.verDetalle(tablaFacturas, 4);
                    abrirvistaFactura(numero2, 1);
                    break;
                case 3:

                    int numero1 = ce.verDetalle(tablaFacturas, 5);
                    abrirvistaFactura(numero1, 1);
                    break;
                    
              
                 
                        case 7:

                    int numero7 = ce.verDetalle(tablaFacturas, 0);
                    abrirvistaFactura(numero7, 1);
                    break;
                        case 8:

                    int numero8 = ce.verDetalle(tablaFacturas, 0);
                    abrirvistaFactura(numero8, 1);
                    break;
    case 9:

                    int numero9 = ce.verDetalle(tablaFacturas, 0);
                    abrirvistaFactura(numero9, 1);
                    break;
                default:
               
            }

        }
    }//GEN-LAST:event_tablaFacturasMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        resttodo();
            
    }//GEN-LAST:event_jButton4ActionPerformed

    private void resttodo() {
            jButton1.setVisible(true);
        delete.setVisible(false);
        restablecido = true;
        desde1 = null;
        hasta1 = null;
        cliente = null;
        factura = null;
        ControladorFacturas objetoProducto = new ControladorFacturas();
        ControladorEstadisticas ce = new ControladorEstadisticas();
        hasta.setDate(null);
        desde.setDate(null);
        hasta.setMinSelectableDate(null);
        desde.setMaxSelectableDate(null);
        if (filtrador.isVisible()) {
            filtrador.setSelectedIndex(0);
        }
        buscaCliente.setText("");
        jTextField1.setText("");
        objetoProducto.ponerMaxFecha(hasta);
        objetoProducto.ponerMaxFecha(desde);

        int codVer = Integer.parseInt(verificador.getText());
        switch (codVer) {
            case 1:
                objetoProducto.muestraFacturas(tablaFacturas, "TODOS", null, null, null, null);
                break;
            case 2:

                ce.consultaPagos(tablaFacturas, "TODOS", null, null, null, factura);
                modelo = (DefaultTableModel) tablaFacturas.getModel();
                tabla = tablaFacturas;
                configurarCambios();
                break;
            case 3:
                filtrador.setVisible(false);
                ce.consultaindividual(tablaFacturas, "TODOS", null, null, null, factura);
                break;

            case 4:
                filtrador.setVisible(false);
                ce.consultaTotales(tablaFacturas, "TODOS", null, null, null);
                break;
            case 5:
                filtrador.setVisible(false);
                ce.consultaTotalesCliente(tablaFacturas, "TODOS", null, null, null);
                break;
            case 6:
                ce.consultaDeudaTotal(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                break;
            case 7:
                ce.consultaDeudaporCliente(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                break;
            case 8:
                ce.consultaDeudaporCliente(tablaFacturas, "VENCIDO", desde1, hasta1, cliente, factura);
                break;
                     case 9:
                filtrador.setVisible(false);
                ce.consultaDetallePesosPorCliente(tablaFacturas, null, null, null);
                break;
                
            default:
                throw new AssertionError();
        }
        restablecido = false;
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        ControladorFacturas objetoProducto = new ControladorFacturas();
        ControladorEstadisticas ce = new ControladorEstadisticas();

        int indiceSelleccionado = tablaFacturas.getSelectedRow();
        if (indiceSelleccionado != -1) {

            int codVer = Integer.parseInt(verificador.getText());
            switch (codVer) {
                case 1:
                    int numero = objetoProducto.verDetalle(tablaFacturas);
                    abrirvistaFactura(numero, 1);
                    break;
                case 2:

                    int numero2 = ce.verDetalle(tablaFacturas, 4);
                    abrirvistaFactura(numero2, 1);
                    break;
                case 3:

                    int numero1 = ce.verDetalle(tablaFacturas, 5);
                    abrirvistaFactura(numero1, 1);
                    break;

                default:
                    numero = objetoProducto.verDetalle(tablaFacturas);
                    abrirvistaFactura(numero, 1);
            }
        } else {
            mostrarAlerta("Selecciona una fila para ver", 2);
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void buscaClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscaClienteFocusGained
        fucuseado = true;
        buscaCliente.selectAll();
    jButton1.setVisible(true);
        delete.setVisible(false);
    }//GEN-LAST:event_buscaClienteFocusGained

    private void buscaClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscaClienteFocusLost

        int verProd = Integer.parseInt(verificador.getText());

        if (verProd == 3 || verProd == 4) {
            /*
            ControladorProducto cp = new ControladorProducto();

            Integer codigo = null;
            try {
                codigo = Integer.parseInt(buscaCliente.getText());
            } catch (Exception e) {
            }
            ModeloProducto producto = cp.obtenerproducto(codigo.toString());

            if (entroSlide == false) {
                if (!listaClientes.isColumnSelected(0)) {
                    //  System.out.println(cliente1.toString()); 
                    if (producto != null) {
                        contenedorClientes.setVisible(false);
                        cliente = producto.getCodigo();
                        buscaCliente.setText(producto.getDescripcion());
                        contenedorClientes.setVisible(false);
                        cliente = producto.getCodigo();
                        buscaCliente.setText(producto.getDescripcion());
                        redireccionar();
                        //  cf.muestraFacturas(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);

                        entroSlide = false;
                    } else {
                        cliente = null;

                        //   cf.muestraFacturas(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);
                        redireccionar();
                        buscaCliente.setText("");
                    }
                }
            }
             */
        } else {

            Controlador.ControladorCliente cc = new ControladorCliente();

            if (entroSlide == false) {
                if (!listaClientes.isColumnSelected(0)) {

                    if (verProd == 3 || verProd == 4) {
                        ControladorProducto cp = new ControladorProducto();
                        ModeloProducto producto = cp.obtenerproducto(buscaCliente.getText());
                        if (producto != null) {
                            contenedorClientes.setVisible(false);
                            cliente = producto.getCodigo();
                            buscaCliente.setText(producto.getDescripcion());
                            redireccionar();
                            entroSlide = false;
                        } else {
                            cliente = null;
                        }
                    } else {
                        ModeloCliente cliente1 = cc.retornaUnCliente(buscaCliente.getText());
                        if (cliente1 != null) {
                            contenedorClientes.setVisible(false);
                            cliente = cliente1.getCodigo();
                            buscaCliente.setText(cliente1.getNombre());
                            redireccionar();
                            entroSlide = false;
                        } else {
                            cliente = null;
                        }
                    }

                    redireccionar();
                    buscaCliente.setText("");
                }
            }
        }

    }//GEN-LAST:event_buscaClienteFocusLost

    private void buscaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscaClienteActionPerformed

    }//GEN-LAST:event_buscaClienteActionPerformed

    private void buscaClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyPressed
    }//GEN-LAST:event_buscaClienteKeyPressed
    boolean fucuseado = false;
    private void buscaClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyReleased
        int verProd = Integer.parseInt(verificador.getText());
        boolean returno;
        if (verProd == 3 || verProd == 4) {
            Controlador.ControladorVenta objetoVenta = new ControladorVenta();
            returno = objetoVenta.buscarProducto(buscaCliente, listaClientes, contenedorClientes, "ACTIVOS");

        } else {
            Controlador.ControladorCliente objetoCliente = new ControladorCliente();
            returno = objetoCliente.buscarCliente(buscaCliente,
                    listaClientes, contenedorClientes);
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            fucuseado = false;

            if (returno == false && !buscaCliente.getText().equals("")) {
                contenedorClientes.setVisible(false);
                String codigo = buscaCliente.getText();
                buscaCliente.setText(codigo);
            }
            if (buscaCliente.getText().equals("")) {
            } else {
                listaClientes.changeSelection(0, 0, false, false);
                listaClientes.requestFocus();
            }
        }
        if (buscaCliente.getText().equals("")) {
            contenedorClientes.setVisible(false);
        }
        entroSlide = false;
    }//GEN-LAST:event_buscaClienteKeyReleased

    private void buscaClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyTyped
    }//GEN-LAST:event_buscaClienteKeyTyped

    private void listaClientesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listaClientesFocusGained
        // fucuseado=true;
    }//GEN-LAST:event_listaClientesFocusGained

    private void listaClientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listaClientesFocusLost

        if (fucuseado == false) {
            // listaClientes.setVisible(false);
            contenedorClientes.setVisible(false);
            /*
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
             */
        }
        fucuseado = false;
    }//GEN-LAST:event_listaClientesFocusLost

    private void listaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaClientesMouseClicked
        ControladorFacturas objetoProducto = new ControladorFacturas();
        cliente = Integer.parseInt(listaClientes.getValueAt(listaClientes.getSelectedRow(), 1).toString());

        // objetoProducto.muestraFacturas(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);
        redireccionar();

        contenedorClientes.setVisible(false);
        buscaCliente.setText(listaClientes.getValueAt(listaClientes.getSelectedRow(), 0).toString());
        buscaCliente.transferFocus();

    }//GEN-LAST:event_listaClientesMouseClicked
    boolean entroSlide = true;
    private void listaClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaClientesKeyPressed

        ControladorEstadisticas ce = new ControladorEstadisticas();

        int codVer = Integer.parseInt(verificador.getText());

        System.out.println("    el numero enviado es:" + listaClientes.getValueAt(listaClientes.getSelectedRow(), 0));

        switch (codVer) {
            case 1:

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    ControladorFacturas objetoProducto = new ControladorFacturas();

                    cliente = Integer.parseInt(listaClientes.getValueAt(listaClientes.getSelectedRow(), 1).toString());

                    //   objetoProducto.muestraFacturas(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);
                    redireccionar();

                    contenedorClientes.setVisible(false);
                    buscaCliente.setText(listaClientes.getValueAt(listaClientes.getSelectedRow(), 0).toString());
                    buscaCliente.transferFocus();
                }

            /*
                cliente = Integer.parseInt(listaClientes.getValueAt(listaClientes.getSelectedRow(), 1).toString());

                //  ce.consultaPagos(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente);
                redireccionar();

                contenedorClientes.setVisible(false);
                buscaCliente.setText(listaClientes.getValueAt(listaClientes.getSelectedRow(), 0).toString());
                break;
             */
            default:
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    cliente = Integer.parseInt(listaClientes.getValueAt(listaClientes.getSelectedRow(), 1).toString());
                    redireccionar();
                    contenedorClientes.setVisible(false);
                    buscaCliente.setText(listaClientes.getValueAt(listaClientes.getSelectedRow(), 0).toString());
                    buscaCliente.transferFocus();
                }
        }
        entroSlide = false;
    }//GEN-LAST:event_listaClientesKeyPressed

    private void listaClientesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaClientesKeyReleased

    }//GEN-LAST:event_listaClientesKeyReleased

    private void contenedorClientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contenedorClientesFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_contenedorClientesFocusLost

    private void contenedorClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contenedorClientesKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_contenedorClientesKeyPressed

    private void desdeHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_desdeHierarchyChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_desdeHierarchyChanged

    private void desdePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_desdePropertyChange
        if (evt.getPropertyName().equals("date")) {
            if (restablecido == false) {

                desde1 = new java.sql.Date(desde.getDate().getTime());
                ControladorFacturas cf = new ControladorFacturas();
                ControladorEstadisticas ce = new ControladorEstadisticas();
                cf.ponerMinFecha(desde, hasta);

                int num = Integer.parseInt(verificador.getText());
                switch (num) {
                    case 1:
                        cf.muestraFacturas(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);
                        break;
                    case 2:
                        ce.consultaPagos(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                        break;
                    case 3:
                        ce.consultaindividual(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                        break;
                    case 4:
                        ce.consultaTotales(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                        break;
                    case 5:
                        ce.consultaTotalesCliente(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                        break;
                    case 6:
                        ce.consultaDeudaTotal(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                        break;
                    case 7:
                        ce.consultaDeudaporCliente(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                        break;
                    case 8:
                        ce.consultaDeudaporCliente(tablaFacturas, "VENCIDO", desde1, hasta1, cliente, factura);
                        break;
                           case 9:
                        ce.consultaDetallePesosPorCliente(tablaFacturas, desde1, hasta1, cliente);
                        break;
                    default:
                        throw new AssertionError();
                }

            }
        }
    }//GEN-LAST:event_desdePropertyChange
    boolean restablecido = false;
    private void hastaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_hastaPropertyChange
        if (evt.getPropertyName().equals("date")) {
            if (restablecido == false) {
                System.out.println("se changio el hasta");

                hasta1 = new java.sql.Date(hasta.getDate().getTime());
                ControladorEstadisticas ce = new ControladorEstadisticas();
                ControladorFacturas cf = new ControladorFacturas();
                cf.ponerMaximaFecha(desde, hasta);
                //      cf.muestraFacturas(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente);

                int num = Integer.parseInt(verificador.getText());
                switch (num) {
                    case 1:
                        cf.muestraFacturas(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);
                        break;
                    case 2:
                        ce.consultaPagos(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                        break;
                    case 3:
                        ce.consultaindividual(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                        break;
                    case 4:
                        ce.consultaTotales(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                        break;
                    case 5:
                        ce.consultaTotalesCliente(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                        break;
                    case 6:
                        ce.consultaDeudaTotal(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                        break;
                    case 7:
                        ce.consultaDeudaporCliente(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                        break;
                    case 8:
                        ce.consultaDeudaporCliente(tablaFacturas, "VENCIDO", desde1, hasta1, cliente, factura);
                        break;
                              case 9:
                        ce.consultaDetallePesosPorCliente(tablaFacturas, desde1, hasta1, cliente);
                        break;
                    default:
                        throw new AssertionError();
                }

            }

        }
    }//GEN-LAST:event_hastaPropertyChange

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked


    }//GEN-LAST:event_jTextField1MouseClicked

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased

        ControladorEstadisticas ce = new ControladorEstadisticas();

        int codVer = Integer.parseInt(verificador.getText());
        switch (codVer) {
            case 1:
                //   if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                ControladorFacturas objetoProducto = new ControladorFacturas();
                /*
                    try {
                        cliente = Integer.parseInt(listaClientes.getValueAt(listaClientes.getSelectedRow(), 1).toString());

                    } catch (Exception e) {
                        cliente = null;
                    }
                 */
                try {
                    factura = Integer.parseInt(jTextField1.getText());
                } catch (Exception e) {
                    factura = null;
                }

                objetoProducto.muestraFacturas(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);
                contenedorClientes.setVisible(false);
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    //  jTextField1.transferFocus();

                }

                break;
            case 2:
                try {

                    cliente = Integer.parseInt(listaClientes.getValueAt(listaClientes.getSelectedRow(), 1).toString());

                } catch (Exception e) {
                    cliente = null;
                }
                try {
                    factura = Integer.parseInt(jTextField1.getText());
                } catch (Exception e) {
                    factura = null;
                }

                ce.consultaPagos(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);
                contenedorClientes.setVisible(false);
                //    jTextField1.transferFocus();
                break;

            default:
                System.out.println("    entroldefaulc");
                try {
                    factura = Integer.parseInt(jTextField1.getText());
                } catch (Exception e) {
                    factura = null;
                }
                redireccionar();
                break;
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed

    }//GEN-LAST:event_jTextField1KeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ControladorFacturas cf = new ControladorFacturas();

        if (!jTextField1.getText().equals("")) {
            factura = Integer.parseInt(jTextField1.getText());
        } else {
            factura = null;
        }

        cf.muestraFacturas(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);
        contenedorClientes.setVisible(false);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        /*
        ControladorFacturas cf = new ControladorFacturas();
        if (!jTextField1.getText().equals("")) {
            factura = Integer.parseInt(jTextField1.getText());
            cf.muestraFacturas(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);
        } else {
            factura = null;
            cf.muestraFacturas(tablaFacturas, filtrador.getSelectedItem().toString(), desde1, hasta1, cliente, factura);

        }
         */
    }//GEN-LAST:event_jTextField1FocusLost

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
        ControladorFacturas objetoProducto = new ControladorFacturas();
        ControladorEstadisticas ce = new ControladorEstadisticas();

        int indiceSelleccionado = tablaFacturas.getSelectedRow();
        if (indiceSelleccionado != -1) {

            int codVer = Integer.parseInt(verificador.getText());
            switch (codVer) {
                case 1:
                    int numero = objetoProducto.verDetalle(tablaFacturas);
                    abrirvistaFactura(numero, 0);
                    break;
                case 2:

                    int numero2 = ce.verDetalle(tablaFacturas, 4);
                    abrirvistaFactura(numero2, 0);
                    break;
                case 3:

                    int numero1 = ce.verDetalle(tablaFacturas, 5);
                    abrirvistaFactura(numero1, 0);
                    break;

                default:
                    numero = objetoProducto.verDetalle(tablaFacturas);
                    abrirvistaFactura(numero, 0);
            }
        } else {

            mostrarAlerta("Selecciona una fila para Imprimir", 2);
        }
    }//GEN-LAST:event_printBtnActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        resttodo();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String fechaFormateada = "--";
        if (desde.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormateada = sdf.format(desde.getDate());
        }

        String fechaFormateada1 = "--";
        if (hasta.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormateada1 = sdf.format(hasta.getDate());
        }
ControladorAdministracion ca= new ControladorAdministracion();
        switch (Integer.parseInt(verificador.getText())) {
            case 3:
           ca. ImprimirTabla("Reporte de ventas de producto:",
                buscaCliente.getText() + "    Desde: " + fechaFormateada + " hasta: " + fechaFormateada1,tablaFacturas);
            break;

            case 4:
           ca.    ImprimirTabla("Reporte de ventas Totales, costos y ganancia:",
                buscaCliente.getText() + "    Desde: " + fechaFormateada + " hasta: " + fechaFormateada1,tablaFacturas);
            break;
            case 5:
            ca.   ImprimirTabla("Reporte de ventas Totales por Cliente:",
                buscaCliente.getText() + "    Desde: " + fechaFormateada + " hasta: " + fechaFormateada1,tablaFacturas);
            break;

            case 8:
           ca.    ImprimirTabla("Reporte de Saldos vencidos por Cliente:",
                buscaCliente.getText() + "    Desde: " + fechaFormateada + " hasta: " + fechaFormateada1,tablaFacturas);
            break;

            case 7:
            ca.   ImprimirTabla("Reporte de Saldos Detallado por Cliente:",
                buscaCliente.getText() + "    Desde: " + fechaFormateada + " hasta: " + fechaFormateada1,tablaFacturas);
            break;
            case 6:
             ca.  ImprimirTabla("Reporte de Saldos totales por Cliente:",
                buscaCliente.getText() + "    Desde: " + fechaFormateada + " hasta: " + fechaFormateada1,tablaFacturas);
            break;
            case 2:
           ca.    ImprimirTabla("Reporte pagos por Cliente:",
                buscaCliente.getText() + "    Desde: " + fechaFormateada + " hasta: " + fechaFormateada1,tablaFacturas);
            break;
                 case 9:
            ca.   ImprimirTabla("Reporte de ventas Detallada por Cliente:",
                buscaCliente.getText() + "    Desde: " + fechaFormateada + " hasta: " + fechaFormateada1,tablaFacturas);
            break;
            default:

            throw new AssertionError();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
     int fila = tablaFacturas.getSelectedRow();

    if (fila == -1) {
          mostrarAlerta("Por favor, seleccioná una factura de la lista.",2);   return;
    }

    // 1. Obtener datos de la tabla (Columna 0: Código, Columna 1: Cliente)
    String idFactura = tablaFacturas.getValueAt(fila, 0).toString();
    String nombreCliente = tablaFacturas.getValueAt(fila, 1).toString();

    // 2. Preguntar confirmación con datos personalizados
    int respuesta = JOptionPane.showConfirmDialog(null, 
        "¿Estás seguro que querés ANULAR la factura Nº " + idFactura + "?\n" +
        "Cliente: " + nombreCliente + "\n\n" +
        "Esta acción restablecerá el stock y los saldos del cliente.", 
        "Confirmar Anulación", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.WARNING_MESSAGE);

    // 3. Si confirma, llamar al método de anulación
    if (respuesta == JOptionPane.YES_OPTION) {
        // Asumiendo que tu método anularFactura recibe el ID como int
        int id = Integer.parseInt(idFactura);
        
        // Llamada al método (pasando el contenedor si lo necesitás para la vista)
        ControladorVenta cv = new ControladorVenta();
        Mensaje mj=cv. anularFactura(id); 
        mostrarAlerta(mj.getMensaje(),mj.getCodigo());
            jButton1.setVisible(true);
        delete.setVisible(false);
        // 4. Refrescar la tabla de facturas para ver el cambio de estado
        // mostrarFacturas(tablaFacturas); 
         ControladorFacturas objetoProducto = new ControladorFacturas();
             objetoProducto.muestraFacturas(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
           
    }

    }//GEN-LAST:event_deleteActionPerformed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
             jButton1.setVisible(true);
        delete.setVisible(false);
    }//GEN-LAST:event_jTextField1FocusGained

    private void desdeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_desdeFocusGained
             jButton1.setVisible(true);
        delete.setVisible(false);
    }//GEN-LAST:event_desdeFocusGained

    private void hastaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_hastaFocusGained
        jButton1.setVisible(true);
        delete.setVisible(false);        // TODO add your handling code here:
    }//GEN-LAST:event_hastaFocusGained

    private void filtradorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_filtradorFocusGained
              jButton1.setVisible(true);
        delete.setVisible(false);
    }//GEN-LAST:event_filtradorFocusGained
   
    public void redireccionar() {

        ControladorFacturas objetoProducto = new ControladorFacturas();
        ControladorEstadisticas ce = new ControladorEstadisticas();

        int num = Integer.parseInt(verificador.getText());
        switch (num) {
            case 1:

                objetoProducto.muestraFacturas(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                filtrador.addItem("TODOS");
                filtrador.addItem("CONTADO");
                filtrador.addItem("TARJETA");
                filtrador.addItem("CUENTA CORRIENTE");
                break;
            case 2:
                filtrador.addItem("TODOS");
                filtrador.addItem("PAGA SALDO");
                filtrador.addItem("PAGA FACTURA DEL DIA");

                ce.consultaPagos(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                modelo = (DefaultTableModel) tablaFacturas.getModel();
                tabla = tablaFacturas;
                configurarCambios();
                break;
            case 3:
                filtrador.setVisible(false);
                ce.consultaindividual(tablaFacturas, "TODOS", desde1, hasta1, cliente, factura);
                break;

            case 4:
                filtrador.setVisible(false);
                ce.consultaTotales(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                break;
            case 5:
                filtrador.setVisible(false);
                ce.consultaTotalesCliente(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                break;
            case 6:
                ce.consultaDeudaTotal(tablaFacturas, "TODOS", desde1, hasta1, cliente);
                break;
            case 7:
                ce.consultaDeudaporCliente(tablaFacturas,
                        "TODOS", desde1, hasta1, cliente, factura);
                break;
            case 8:
                ce.consultaDeudaporCliente(tablaFacturas,
                        "VENCIDO", desde1, hasta1, cliente, factura);
                break;
            case 9:
                filtrador.setVisible(false);
                ce.consultaDetallePesosPorCliente(tablaFacturas, desde1, hasta1, cliente);
                break;
            default:
                throw new AssertionError();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField buscaCliente;
    private javax.swing.JScrollPane contenedorClientes;
    private javax.swing.JButton delete;
    private com.toedter.calendar.JDateChooser desde;
    private javax.swing.JComboBox<String> filtrador;
    private com.toedter.calendar.JDateChooser hasta;
    private javax.swing.JLabel identificador;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable listaClientes;
    private javax.swing.JButton printBtn;
    private javax.swing.JTable tablaFacturas;
    private javax.swing.JLabel verificador;
    // End of variables declaration//GEN-END:variables
}
