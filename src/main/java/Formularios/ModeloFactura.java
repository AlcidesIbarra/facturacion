/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;
import Controlador.ControladorImpresion;
import Modelos.Administrador;
import com.mysql.cj.protocol.Resultset;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrintQuality;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author ALCIDES
 */

/*
 public static void ajustarAlturaTable(JTable tabla) {
        // 1. Obtener la altura del encabezado
        int alturaHeader = tabla.getTableHeader().getPreferredSize().height;
        
        // 2. Calcular la altura de todas las filas
        // Multiplicamos la cantidad de filas por el alto de celda definido
        int alturaFilas = tabla.getRowCount() * tabla.getRowHeight();
        
        // 3. Sumar ambas alturas
        int alturaTotal = alturaHeader + alturaFilas;
        
        // 4. Aplicar el nuevo tamaño preferido
        // Mantenemos el ancho actual y solo cambiamos el alto
        Dimension dimensionActual = tabla.getPreferredSize();
        tabla.setPreferredScrollableViewportSize(new Dimension(dimensionActual.width, alturaTotal));
        
        // Forzar a que el contenedor (como un JScrollPane) se entere del cambio
        tabla.revalidate();
    }
 */
public class ModeloFactura extends javax.swing.JInternalFrame {

  public ModeloFactura(Integer numero, int codigoImpresion) {
    initComponents();
    ControladorAdministracion ca = new ControladorAdministracion();

    // 1. Configuración de la Tabla
    tablaProductos.setFillsViewportHeight(true);
    tablaProductos.setRowHeight(24);

    // 2. Carga de datos (Kg, Bultos, Precios)
    jLabel24.setText(ca.buscarVencimiento(numero));
    jLabel21.setText(ca.buscarSaldo(numero));
    String metodo = ca.buscarDatosFactura(tablaProductos, numero, nombre, domicilio, numFactura, fecha, cuit, hora, observaciones, total, jLabel26, jLabel29);

    
    JTableHeader header = tablaProductos.getTableHeader();

// Crear una nueva fuente (Nombre, Estilo, Tamaño)
Font fuenteNueva = new Font("Tahoma", Font.BOLD, 16); 

// Aplicar la fuente al encabezado
header.setFont(fuenteNueva);
    
    
    
    switch (metodo) {
        case "Cuenta Corriente": corriente.setSelected(true); break;
        case "Tarjeta":       tarjeta.setSelected(true); break;
case "Transferencia": jCheckBox1.setSelected(true); break;
        default: contado.setSelected(true);
    }

    // 3. Logo e Interfaz
    java.net.URL urlLogo = getClass().getResource("/main/Imagenes/logo.png");
    if (urlLogo != null) {
        Image imgLogo = new ImageIcon(urlLogo).getImage().getScaledInstance(116, 116, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(imgLogo));
    }

    // Bordes y Datos Admin
    Border borde = BorderFactory.createLineBorder(new Color(0, 0, 0), 2, true);
    panelLogos.setBorder(borde); datosFecha.setBorder(borde); datosCliente.setBorder(borde);
    facturacion.setBorder(borde); saldos.setBorder(borde); totales.setBorder(borde);

    Administrador admni = ca.obtenerDatosAdministrador();
    jLabel4.setText(admni.getTelefono1()); jLabel6.setText(admni.getTelefono2());
    jLabel8.setText(admni.getCelular()); 
    


    Font fuenteG = new Font("Arial", Font.PLAIN, 18);
    cambiarFuenteContenedor(this, fuenteG);
    
String mensajeOriginal = admni.getMensaje();
// Ajustamos el ancho (podes probar con 200px o 250px según tu diseño)
jLabel23.setText("<html><body style='width: 460px; text-align: center;'>" + mensajeOriginal + "</body></html>");

// 2. Configurar la fuente (Serif Italic 26)
jLabel23.setFont(new Font("Serif", Font.ITALIC, 28));

// 3. Alinear el bloque de texto al centro del componente
jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

// 4. Color para que no canse la vista (Gris oscuro)
jLabel23.setForeground(new java.awt.Color(60, 60, 60));

    // ============================================================
    // 4. EL SECRETO PARA EL PDF: IMAGEN "LAVADA" (MARCA DE AGUA)
    // ============================================================
    if (urlLogo != null) {
        final Image imgOriginal = new ImageIcon(urlLogo).getImage();
        
        // CREAMOS UNA IMAGEN FÍSICAMENTE CLARA (No depende de transparencia)
        BufferedImage imgLavada = new BufferedImage(550, 550, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gImg = imgLavada.createGraphics();
        
        // Pintamos un fondo blanco semi-transparente sobre el logo para "lavarlo"
        gImg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        gImg.drawImage(imgOriginal, 0, 0, 550, 550, null);
        
        // Aplicamos una capa blanca encima para aclarar los colores (Efecto Bleach)
        gImg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f)); // 92% de blanco
        gImg.setColor(Color.WHITE);
        gImg.fillRect(0, 0, 550, 550);
        gImg.dispose();

        // Configuración de Tabla
        tablaProductos.setOpaque(false);
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);

        // Renderers: Unificamos Transparencia + Alineación + Márgenes
        for (int i = 0; i < tablaProductos.getColumnCount(); i++) {
            final int colIndex = i;
            tablaProductos.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    ((JLabel) c).setOpaque(false); 
                    switch (colIndex) {
                        case 0: case 1: setHorizontalAlignment(SwingConstants.CENTER); break;
                        case 2: setHorizontalAlignment(SwingConstants.LEFT); setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); break;
                        case 3: case 4: setHorizontalAlignment(SwingConstants.RIGHT); setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); break;
                    }
                    return c;
                }
            });
        }

        // Viewport que dibuja la imagen ya aclarada
        JViewport viewportPersonalizado = new JViewport() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = (getWidth() - 550) / 2;
                int y = (getHeight() - 550) / 2;
                g.drawImage(imgLavada, x, y, this);
            }
        };
        viewportPersonalizado.setOpaque(false);
        jScrollPane1.setViewport(viewportPersonalizado);
        jScrollPane1.setViewportView(tablaProductos);
    }

    // 5. Visualización y Captura para PDF
    this.setVisible(true);
    panelContenedor.revalidate();
    panelContenedor.repaint();

    SwingUtilities.invokeLater(() -> {
        if (this.getDesktopPane() != null) {
            // Esto es lo que genera el PDF/Imagen
            BufferedImage imge = panelAImagen(panelContenedor);
            mostrarEnInternal(imge, this.getDesktopPane());
            this.dispose();
            if (codigoImpresion == 0) {
                imprimirMediaHoja(panelContenedor);
            }
        }
    });
}


    public void cambiarFuenteContenedor(Component componente, Font nuevaFuente) {
        // Aplicamos la fuente al componente actual
        componente.setFont(nuevaFuente);

        // Si es un contenedor (como un JPanel o la JTable), recorremos sus hijos [Oracle Docs](https://docs.oracle.com)
        if (componente instanceof Container) {
            for (Component hijo : ((Container) componente).getComponents()) {
                cambiarFuenteContenedor(hijo, nuevaFuente);
            }
        }
    }

    public void mostrarEnInternal(BufferedImage img, JDesktopPane desktop) {
        if (desktop == null) {
            return;
        }

        // JInternalFrame sin bordes ni barra (igual que antes)
        JInternalFrame internal = new JInternalFrame("", false, false, false, false);

        // 1. Escalar la imagen
        int anchoDeseado = 410;
        int altoProporcional = (anchoDeseado * img.getHeight()) / img.getWidth();
        Image imagenChica = img.getScaledInstance(anchoDeseado, altoProporcional, Image.SCALE_SMOOTH);

        // 2. JLayeredPane: Es la clave para que nada se recorte y el botón flote
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new java.awt.Dimension(anchoDeseado, altoProporcional));

        // 3. Etiqueta con la imagen (Fondo)
        JLabel etiqueta = new JLabel(new ImageIcon(imagenChica));
        etiqueta.setBounds(0, 0, anchoDeseado, altoProporcional);

        // 4. Tu Botón Original (Recuperamos tus estilos exactos)
        JButton btnCerrar = new JButton("X");
        btnCerrar.setMargin(new java.awt.Insets(0, 0, 0, 0));

        btnCerrar.setBounds(anchoDeseado - 25, 2, 22, 20);
        btnCerrar.setBackground(new java.awt.Color(200, 50, 50));
        btnCerrar.setForeground(java.awt.Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(null);
        btnCerrar.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 10));
        btnCerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> internal.dispose());

        // 5. Agregar al LayeredPane con capas explícitas
        layeredPane.add(etiqueta, JLayeredPane.DEFAULT_LAYER); // Capa 0 (fondo)
        layeredPane.add(btnCerrar, JLayeredPane.PALETTE_LAYER); // Capa 100 (arriba)

        internal.setContentPane(layeredPane);

        // 6. Eliminar decoración de Look and Feel (Evita que el JAR agregue bordes grises)
        ((javax.swing.plaf.basic.BasicInternalFrameUI) internal.getUI()).setNorthPane(null);
        internal.setBorder(javax.swing.BorderFactory.createEmptyBorder()); // Forzamos borde vacío

        internal.pack();

        // Posicionamiento centrado
        int x = (desktop.getWidth() - internal.getWidth()) / 2;
        internal.setLocation(x, 0);

        desktop.add(internal);
        internal.setVisible(true);
    }

    public BufferedImage panelAImagen(JPanel panel) {
        // Forzamos dimensiones para evitar capturas incompletas
        panel.setSize(panel.getPreferredSize());
        panel.doLayout();

        BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();

        // PINTAR FONDO BLANCO (Esto elimina el gris por defecto de Swing)
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, panel.getWidth(), panel.getHeight());

        panel.printAll(g2d);
        g2d.dispose();
        return img;
    }

   public void imprimirMediaHoja(JPanel panel) {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setJobName("Factura A5 - " + panel.getName());

    // 1. CONFIGURACIÓN DEL PAPEL (A5)
    PageFormat pf = job.defaultPage();
    Paper papel = new Paper();
    papel.setSize(419.5, 595.3); // Medidas A5 en puntos
    double m = 11.3; // Margen de seguridad
    papel.setImageableArea(m, m, 419.5 - (m * 2), 595.3 - (m * 2));
    pf.setPaper(papel);
    pf.setOrientation(PageFormat.PORTRAIT);

    // 2. DEFINICIÓN DEL PRINTABLE
    Printable miPrintable = (Graphics g, PageFormat pageFormat, int pageIndex) -> {
        // Doble validación de página (aunque el Book ya lo controla)
        if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // ESCALADO PROPORCIONAL
        double escalaX = pageFormat.getImageableWidth() / panel.getWidth();
        double escalaY = pageFormat.getImageableHeight() / panel.getHeight();
        double escalaFinal = Math.min(escalaX, escalaY);

        double xCentrado = (pageFormat.getImageableWidth() - (panel.getWidth() * escalaFinal)) / 2;
        g2d.translate(xCentrado, 0);
        g2d.scale(escalaFinal, escalaFinal);

        // FORZAR TÍTULOS DE TABLA
        forzarEncabezados(panel);

        // IMPRIMIR
        panel.printAll(g2d);

        return Printable.PAGE_EXISTS;
    };

    // 3. LA SOLUCIÓN AL ERROR DE 999 PÁGINAS: El objeto Book
    Book libro = new Book();
    libro.append(miPrintable, pf, 1); // Forzamos 1 sola página
    job.setPageable(libro);

    // 4. DIÁLOGO NATIVO (Sin atributos para mantener tu diseño)
    if (job.printDialog()) {
        try {
            // Atributos finales para la cola de impresión
            PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
            attr.add(MediaSizeName.ISO_A5);
            attr.add(OrientationRequested.PORTRAIT);
            attr.add(PrintQuality.HIGH);

            job.print(attr);
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}

// Método recursivo para encontrar la tabla y sus títulos
    private void forzarEncabezados(Container contenedor) {
        for (Component c : contenedor.getComponents()) {
            if (c instanceof JScrollPane) {
                JScrollPane sp = (JScrollPane) c;
                if (sp.getViewport().getView() instanceof JTable) {
                    JTable tabla = (JTable) sp.getViewport().getView();
                    sp.setColumnHeaderView(tabla.getTableHeader());
                    tabla.getTableHeader().setSize(tabla.getWidth(), tabla.getTableHeader().getHeight());
                }
            } else if (c instanceof Container) {
                forzarEncabezados((Container) c);
            }
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
        jScrollPane2 = new javax.swing.JScrollPane();
        panelContenedor = new javax.swing.JPanel();
        panelLogos = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        datosFecha = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        numFactura = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        fecha = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        hora = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        datosCliente = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        nombre = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        domicilio = new javax.swing.JLabel();
        cuit = new javax.swing.JLabel();
        observaciones = new javax.swing.JLabel();
        facturacion = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        tarjeta = new javax.swing.JCheckBox();
        corriente = new javax.swing.JCheckBox();
        contado = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jCheckBox1 = new javax.swing.JCheckBox();
        saldos = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        totales = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setPreferredSize(new java.awt.Dimension(790, 902));

        panelLogos.setBackground(new java.awt.Color(255, 255, 255));
        panelLogos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Avenida Independencia 1023");
        panelLogos.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 240, -1));

        jLabel2.setText("Telefono:  ");
        panelLogos.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 90, -1));

        jLabel3.setText("4400-Salta");
        panelLogos.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 100, -1));

        jLabel4.setText("0387 - 4261043");
        panelLogos.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 130, -1));

        jLabel6.setText("0387 - 4230600");
        panelLogos.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, 140, -1));

        jLabel7.setText("Cel:");
        panelLogos.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, -1, -1));

        jLabel8.setText("154204215    ");
        panelLogos.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, -1, -1));
        panelLogos.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 130, 120));

        datosFecha.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setText("COMPROBANTE Nº:");

        numFactura.setText(" ");

        jLabel12.setText("FECHA:");

        jLabel13.setText("Documento no valido como Factura");

        fecha.setText(" ");

        jLabel10.setText("HORA:");

        hora.setText(" ");

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setText("ORIGINAL");

        javax.swing.GroupLayout datosFechaLayout = new javax.swing.GroupLayout(datosFecha);
        datosFecha.setLayout(datosFechaLayout);
        datosFechaLayout.setHorizontalGroup(
            datosFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datosFechaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel12)
                .addGap(34, 34, 34)
                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(datosFechaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel10)
                .addGap(50, 50, 50)
                .addGroup(datosFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(datosFechaLayout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(hora, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(datosFechaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(numFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(datosFechaLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        datosFechaLayout.setVerticalGroup(
            datosFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datosFechaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(datosFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fecha))
                .addGap(10, 10, 10)
                .addGroup(datosFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(hora))
                .addGap(10, 10, 10)
                .addGroup(datosFechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numFactura))
                .addGap(10, 10, 10)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        datosCliente.setBackground(new java.awt.Color(255, 255, 255));

        jLabel15.setText("Señor/es:");

        nombre.setText(" ");

        jLabel17.setText("Domicilio:");

        jLabel18.setText("Observaciones:");

        jLabel19.setText("CUIT / DNI:");

        domicilio.setText(" ");

        cuit.setText(" ");

        observaciones.setText(" ");

        javax.swing.GroupLayout datosClienteLayout = new javax.swing.GroupLayout(datosCliente);
        datosCliente.setLayout(datosClienteLayout);
        datosClienteLayout.setHorizontalGroup(
            datosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datosClienteLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(datosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(datosClienteLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(observaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, datosClienteLayout.createSequentialGroup()
                        .addGroup(datosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(datosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(datosClienteLayout.createSequentialGroup()
                                .addComponent(domicilio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(268, 268, 268))
                            .addGroup(datosClienteLayout.createSequentialGroup()
                                .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cuit, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        datosClienteLayout.setVerticalGroup(
            datosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datosClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(datosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(nombre)
                    .addComponent(cuit)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(datosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(domicilio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(datosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(observaciones))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        facturacion.setBackground(new java.awt.Color(255, 255, 255));
        facturacion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        facturacion.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 920, 850, 80));

        jLabel22.setText("CONDICIONES DE VENTA:");
        facturacion.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 4, 250, -1));

        tarjeta.setBackground(new java.awt.Color(255, 255, 255));
        tarjeta.setText("Tarjeta");
        tarjeta.setFocusable(false);
        tarjeta.setRequestFocusEnabled(false);
        facturacion.add(tarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(379, 4, -1, 20));

        corriente.setBackground(new java.awt.Color(255, 255, 255));
        corriente.setText("Cuenta Corriente");
        corriente.setFocusable(false);
        corriente.setRequestFocusEnabled(false);
        corriente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                corrienteActionPerformed(evt);
            }
        });
        facturacion.add(corriente, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 4, 173, 20));

        contado.setBackground(new java.awt.Color(255, 255, 255));
        contado.setText("Contado");
        contado.setFocusPainted(false);
        contado.setFocusable(false);
        contado.setRequestFocusEnabled(false);
        facturacion.add(contado, new org.netbeans.lib.awtextra.AbsoluteConstraints(262, 4, -1, 20));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tablaProductos.setBackground(new java.awt.Color(255, 255, 255));
        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "KG", "BULTO", "DETALLE", "PRECIO UNIT", "IMPORTE"
            }
        ));
        jScrollPane1.setViewportView(tablaProductos);
        if (tablaProductos.getColumnModel().getColumnCount() > 0) {
            tablaProductos.getColumnModel().getColumn(0).setMinWidth(100);
            tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(100);
            tablaProductos.getColumnModel().getColumn(0).setMaxWidth(100);
            tablaProductos.getColumnModel().getColumn(1).setMinWidth(80);
            tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(80);
            tablaProductos.getColumnModel().getColumn(1).setMaxWidth(80);
            tablaProductos.getColumnModel().getColumn(2).setMinWidth(260);
            tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(260);
            tablaProductos.getColumnModel().getColumn(2).setMaxWidth(260);
            tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(60);
            tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(80);
        }

        facturacion.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 860, 970));

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Transferencia");
        facturacion.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(659, 4, -1, 20));

        saldos.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setText("RECIBI CONFORME:");

        jLabel20.setText("Saldo anterior a la fecha:");

        jLabel21.setText(" ");

        jLabel24.setText(" ");

        jLabel25.setText("Proximo vencimiento:");

        totales.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setText("TOTAL:");

        total.setText(" ");

        jLabel5.setText("Total Kg:");

        jLabel26.setText("----");

        jLabel29.setText("----");

        jLabel27.setText("Total Bultos:");

        javax.swing.GroupLayout totalesLayout = new javax.swing.GroupLayout(totales);
        totales.setLayout(totalesLayout);
        totalesLayout.setHorizontalGroup(
            totalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(totalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(totalesLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(totalesLayout.createSequentialGroup()
                        .addGroup(totalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(totalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        totalesLayout.setVerticalGroup(
            totalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, totalesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(totalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(totalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(totalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(total))
                .addContainerGap())
        );

        javax.swing.GroupLayout saldosLayout = new javax.swing.GroupLayout(saldos);
        saldos.setLayout(saldosLayout);
        saldosLayout.setHorizontalGroup(
            saldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saldosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(saldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(saldosLayout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
                    .addGroup(saldosLayout.createSequentialGroup()
                        .addGroup(saldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        saldosLayout.setVerticalGroup(
            saldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saldosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(saldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(saldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addContainerGap(14, Short.MAX_VALUE))
            .addComponent(totales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        facturacion.add(saldos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1001, 860, -1));

        javax.swing.GroupLayout panelContenedorLayout = new javax.swing.GroupLayout(panelContenedor);
        panelContenedor.setLayout(panelContenedorLayout);
        panelContenedorLayout.setHorizontalGroup(
            panelContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContenedorLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(panelContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelContenedorLayout.createSequentialGroup()
                        .addComponent(panelLogos, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(datosFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(datosCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(facturacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        panelContenedorLayout.setVerticalGroup(
            panelContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContenedorLayout.createSequentialGroup()
                .addGroup(panelContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(datosFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelLogos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(datosCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(facturacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(panelContenedor);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void corrienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_corrienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_corrienteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox contado;
    private javax.swing.JCheckBox corriente;
    private javax.swing.JLabel cuit;
    private javax.swing.JPanel datosCliente;
    private javax.swing.JPanel datosFecha;
    private javax.swing.JLabel domicilio;
    private javax.swing.JPanel facturacion;
    private javax.swing.JLabel fecha;
    private javax.swing.JLabel hora;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel logo;
    private javax.swing.JLabel nombre;
    private javax.swing.JLabel numFactura;
    private javax.swing.JLabel observaciones;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JPanel panelLogos;
    private javax.swing.JPanel saldos;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JCheckBox tarjeta;
    private javax.swing.JLabel total;
    private javax.swing.JPanel totales;
    // End of variables declaration//GEN-END:variables
}
/*
respaldo imprimir media hoja
public void imprimirMediaHoja(JPanel panel) {
    PrinterJob job = PrinterJob.getPrinterJob();
    
    // 1. Configuración de Atributos
    PrintRequestAttributeSet atributos = new HashPrintRequestAttributeSet();
    atributos.add(DialogTypeSelection.NATIVE); 
    atributos.add(new PageRanges(1, 1)); 
    atributos.add(javax.print.attribute.standard.MediaSizeName.ISO_A4);

    float anchoA4 = 210.0f; 
    float altoMediaA4 = 148.5f; 
    float mFisico = 5.0f; 

    atributos.add(new MediaPrintableArea(mFisico, mFisico, anchoA4 - (mFisico * 2), altoMediaA4 - (mFisico * 2), MediaPrintableArea.MM));

    // 2. Definir el Printable
    job.setPrintable((Graphics g, PageFormat pageFormat, int pageIndex) -> {
        if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) g;
        
        // --- CONFIGURACIÓN CRÍTICA PARA LÍNEAS FINAS ---
        // Antialiasing para suavizar pero con control de normalización
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // FORZAR que las líneas se ajusten a píxeles enteros (evita que desaparezcan)
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        // Calidad máxima de renderizado
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Posicionamiento y Rotación
        g2d.translate(pageFormat.getImageableX() + pageFormat.getImageableWidth(), pageFormat.getImageableY());
        g2d.rotate(Math.toRadians(90));

        // Margen de seguridad para bordes exteriores
        int paddingBorde = 8; // Aumentamos un poquito para asegurar
        double anchoDisponible = pageFormat.getImageableHeight() - (paddingBorde * 2);
        double altoDisponible = pageFormat.getImageableWidth() - (paddingBorde * 2);

        double scale = Math.min(anchoDisponible / (double) panel.getWidth(), 
                                altoDisponible / (double) panel.getHeight());
        
        g2d.translate(paddingBorde, paddingBorde);
        g2d.scale(scale, scale);

        // --- TRUCO FINAL: Grosor de línea mínimo ---
        // Esto asegura que incluso con el escalado, las líneas tengan un grosor visible
        g2d.setStroke(new BasicStroke(1.2f)); 

        panel.printAll(g2d);

        return Printable.PAGE_EXISTS;
    }); 

    if (job.printDialog(atributos)) {
        try {
            job.print(atributos); 
        } catch (PrinterException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
 */
