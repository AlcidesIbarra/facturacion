/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Formularios.FormPagos;
import Modelos.Administrador;
import com.toedter.components.JLocaleChooser;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import jdk.jfr.Timespan;
import configuracion.Conexion;

/**
 *
 * @author ALCIDES
 */
public class ControladorAdministracion {

    
     public void ImprimirTabla(String titulo1, String titulo2,JTable tablaFacturas) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Reporte Facturas");

            // 1. ATRIBUTOS A4
            PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
            attr.add(new MediaPrintableArea(15, 10, 180, 277, MediaPrintableArea.MM));
            attr.add(OrientationRequested.PORTRAIT);

            // --- CORRECCIÓN AQUÍ ---
            // Obtenemos el PageFormat que coincida con nuestros atributos (A4)
            PageFormat pf = job.getPageFormat(attr);

            // Definimos el Printable (tu lógica de títulos y tabla)
            Printable miPrintable = new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    // ... (aquí va tu lógica de g2d.drawString y tablePrintable.print) ...
                    // Nota: Asegurate de que este bloque sea igual al anterior

                    MessageFormat footer = new MessageFormat("Página {0}");
                    Printable tablePrintable = tablaFacturas.getPrintable(JTable.PrintMode.FIT_WIDTH, null, footer);

                    PageFormat tablaFormat = (PageFormat) pageFormat.clone();
                    Paper papel = tablaFormat.getPaper();
                    double espacioTitulos = 35.0;
                    papel.setImageableArea(papel.getImageableX(), papel.getImageableY() + espacioTitulos,
                            papel.getImageableWidth(), papel.getImageableHeight() - espacioTitulos);
                    tablaFormat.setPaper(papel);

                    int status = tablePrintable.print(graphics, tablaFormat, pageIndex);
                    if (status == NO_SUCH_PAGE) {
                        return NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                    g2d.setFont(new Font("Arial", Font.BOLD, 21));
                    g2d.drawString(titulo1, ((int) pageFormat.getImageableWidth() - g2d.getFontMetrics().stringWidth(titulo1)) / 2, 15);
                    g2d.setFont(new Font("Arial", Font.BOLD, 16));
                    g2d.drawString(titulo2, ((int) pageFormat.getImageableWidth() - g2d.getFontMetrics().stringWidth(titulo2)) / 2, 30);

                    return PAGE_EXISTS;
                }
            };

            // 2. CONTEO DE PÁGINAS REALES
            Book libro = new Book();
            int totalPaginas = 0;
            // Usamos una imagen temporal para simular el renderizado
            java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_RGB);
            Graphics g = img.getGraphics();

            while (miPrintable.print(g, pf, totalPaginas) == Printable.PAGE_EXISTS) {
                libro.append(miPrintable, pf);
                totalPaginas++;
            }

            // 3. ASIGNAR RESULTADO
            job.setPageable(libro);

            if (job.printDialog()) {
                job.print(attr);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    
public String traerRutaImg() {

    String consulta = "select ruta from administrador where id=1;";
    String ruta = "";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(consulta);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            ruta = rs.getString("ruta");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.toString());
    }

    return ruta;
}

 public void guardarRuta(String ruta) {

    String consulta = "update administrador set ruta=? where id=1;";

    try (Connection conn = Conexion.getConexion();
         CallableStatement cs2 = conn.prepareCall(consulta)) {

        cs2.setString(1, ruta);
        cs2.execute();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al guardar: " + e.toString());
    }
}

   public void GuardarColores(int rgb, JComboBox editar) {

    String dato = editar.getSelectedItem().toString();
    String sql = "";

    switch (dato) {
        case "FONDO":
            sql = "update administrador set color_fondo=? where id=1;";
            break;
        case "LETRAS":
            sql = "update administrador set color_letras=? where id=1;";
            break;
        case "FORMULARIOS":
            sql = "update administrador set color_formulario=? where id=1;";
            break;
        case "MENU":
            sql = "update administrador set color_menu=? where id=1;";
            break;
        case "SELECT MENU":
            sql = "update administrador set color_select_menu=? where id=1;";
            break;
        case "LETRAS MENU":
            sql = "update administrador set color_letras_menu=? where id=1;";
            break;
        case "FORMULARIO DESHABILITADO":
            sql = "update administrador set color_deshabilitado=? where id=1;";
            break;
        default:
            JOptionPane.showMessageDialog(null, "Opción inválida");
            return;
    }

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, rgb);
        ps.executeUpdate();

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "ERROR: " + ex.toString());
    }
}
   public Color retornatexField() {

    Color color = new Color(0, 0, 0);
    String sql = "select * from administrador where id=1;";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            int rgb = rs.getInt("color_formulario");
            color = new Color(rgb, true);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.toString());
    }

    return color;
}

public Color retornatexFieldF() {

    Color color = new Color(0, 0, 0);
    String sql = "select color_fondo from administrador where id=1;";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            int rgb = rs.getInt("color_fondo");
            color = new Color(rgb, true);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.toString());
    }

    return color;
}

   public Color retornatexFondoMenu() {

    Color color = new Color(0, 0, 0);
    String sql = "select color_menu from administrador where id=1;";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            int rgb = rs.getInt("color_menu");
            color = new Color(rgb, true);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.toString());
    }

    return color;
}

   public Color retornaletraMenu() {

    Color color = new Color(0, 0, 0);
    String sql = "select color_letras_menu from administrador where id=1;";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            int rgb = rs.getInt("color_letras_menu");
            color = new Color(rgb, true);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.toString());
    }

    return color;
}
   

public void cambiacolor(Container con) {

    Color colorFondo = new Color(0, 0, 0);
    Color colorTextField = new Color(0, 0, 0);
    Color colorMenu = new Color(0, 0, 0);

    String sql = "select * from administrador where id=1;";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            int rgbFondo = rs.getInt("color_fondo");
            int rgbTextField = rs.getInt("color_formulario");

            colorFondo = new Color(rgbFondo, true);
            colorTextField = new Color(rgbTextField, true);
            colorMenu = new Color(rs.getInt("color_menu"), true);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "5 ERROR: CONTACTE AL ADMINISTRADOR: " + e.toString());
    }

    // 🎨 Aplicar colores
    UIManager.put("Panel.background", colorFondo);
    UIManager.put("InternalFrame.background", colorFondo);
    UIManager.put("CheckBox.background", colorFondo);
    UIManager.put("TextField.background", colorTextField);
    UIManager.put("TextPane.background", colorTextField);

    RoundTextoUI.color = colorTextField;
    RoundTextoUI.radio = 6;

    RoundTextPane.radio = 6;

    UIManager.put("TextPaneUI", RoundTextPane.class.getName());
    UIManager.put("TextFieldUI", RoundTextoUI.class.getName());

    Color fondoSeleccion = new Color(150, 150, 150);
    Color textoSeleccion = Color.black;

    UIManager.put("TextField.selectionBackground", fondoSeleccion);
    UIManager.put("TextField.selectionForeground", textoSeleccion);

    UIManager.put("PasswordField.selectionBackground", fondoSeleccion);
    UIManager.put("PasswordField.selectionForeground", textoSeleccion);

    UIManager.put("FormattedTextField.selectionBackground", fondoSeleccion);
    UIManager.put("FormattedTextField.selectionForeground", textoSeleccion);

    UIManager.put("TextArea.selectionBackground", fondoSeleccion);
    UIManager.put("TextArea.selectionForeground", textoSeleccion);

    UIManager.put("TextField.caretForeground", Color.black);

    UIManager.put("Table.selectionBackground", new Color(0, 120, 215));
    UIManager.put("Table.selectionForeground", Color.WHITE);

    // 🔄 refrescar UI
    SwingUtilities.updateComponentTreeUI(con);
}

    public void habilitarCheckBox(JCheckBox elemento) {
        elemento.setUI(new javax.swing.plaf.basic.BasicCheckBoxUI() {
            @Override
            protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
                g.setColor(Color.red);
                super.paintText(g, b, textRect, text);
            }
        });

    }

    public void BotonesCrisital(JButton boton) {
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setForeground(Color.white);
        //margen interno agregar

        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int ancho = c.getWidth();
                int alto = c.getHeight();
                int arco = 16;

                int a = 104;
                int b = 195;
                int ca = 183;

                if (boton.getModel().isArmed()) {
                    g2.setColor(new Color(a, b, ca, 200));

                } else if (boton.getModel().isRollover()) {
                    g2.setColor(new Color(a, b, ca, 130));
                } else {
                    g2.setColor(new Color(a, b, ca, 100));
                }

                g2.fill(new RoundRectangle2D.Double(0, 0, ancho, alto, arco, arco));
                g2.setColor(new Color(a, b, ca, 200));
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Double(0, 0, ancho - 1, alto - 1, arco, arco));
                g2.dispose();
                super.paint(g, c);
            }

        });

    }

    public void BotonesCrisitalPrim(JButton boton, Color fondo, Color letra, Color borde) {
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setForeground(letra);
        boton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        // Ajuste de tamaño automático
        String textoLimpio = boton.getText().replaceAll("\\<.*?\\>", "");
        java.awt.FontMetrics fm = boton.getFontMetrics(boton.getFont());
        //boton.setPreferredSize(new java.awt.Dimension(fm.stringWidth(textoLimpio) + 50, 40));

        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

                int w = c.getWidth();
                int h = c.getHeight();
                int arco = 15;

                // --- LÓGICA DE COLORES SEGÚN ESTADO (Habilitado / Deshabilitado) ---
                Color cBase;
                int alpha;

                if (!c.isEnabled()) {
                    // MODO GRIS CRISTAL (Para cuando está deshabilitado)
                    cBase = new Color(180, 180, 180); // Gris neutro
                    alpha = 80; // Transparencia tipo cristal para que no tape el fondo
                } else {
                    // MODO COLOR (Para cuando está habilitado)
                    cBase = fondo;
                    alpha = boton.getModel().isPressed() ? 240 : (boton.getModel().isRollover() ? 210 : 180);
                }

                // 1. CUERPO DEL CRISTAL (Degradado)
                Color colTop = new Color(cBase.getRed(), cBase.getGreen(), cBase.getBlue(), alpha);
                java.awt.GradientPaint cuerpo = new java.awt.GradientPaint(0, 0, colTop, 0, h, colTop.darker());
                g2.setPaint(cuerpo);
                g2.fill(new java.awt.geom.RoundRectangle2D.Double(1, 1, w - 2, h - 2, arco, arco));

                // 2. REFLEJO DE CRISTAL (Brillo superior igual que en el Toggle)
                int alphaBrillo = c.isEnabled() ? 80 : 40; // Brillo más tenue si está apagado
                java.awt.GradientPaint brillo = new java.awt.GradientPaint(0, 0, new Color(255, 255, 255, alphaBrillo), 0, h / 2, new Color(255, 255, 255, 0));
                g2.setPaint(brillo);
                g2.fill(new java.awt.geom.RoundRectangle2D.Double(3, 2, w - 6, h / 2.2, arco, arco));

                // 3. BORDE (Gris si está apagado, Color si está encendido)
                if (!c.isEnabled()) {
                    g2.setColor(new Color(130, 130, 130, 100));
                } else {
                    g2.setColor(new Color(borde.getRed(), borde.getGreen(), borde.getBlue(), 220));
                }
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.draw(new java.awt.geom.RoundRectangle2D.Double(1, 1, w - 2, h - 2, arco, arco));

                // 4. TEXTO (Centrado)
                g2.setColor(c.isEnabled() ? boton.getForeground() : Color.GRAY);
                java.awt.FontMetrics metrics = g2.getFontMetrics();
                int x = (w - metrics.stringWidth(textoLimpio)) / 2;
                int y = ((h - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString(textoLimpio, x, y);

                g2.dispose();
            }
        });
    }

    public void BotonesCrisitalTogle(JToggleButton boton, Color fondo, Color letra, Color borde) {
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setForeground(letra);
        boton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Ajuste de tamaño automático (Limpia HTML para el cálculo)
        String textoLimpio = boton.getText().replaceAll("\\<.*?\\>", "");
        java.awt.FontMetrics fm = boton.getFontMetrics(boton.getFont());
        boton.setPreferredSize(new java.awt.Dimension(fm.stringWidth(textoLimpio) + 50, 38));

        boton.setUI(new javax.swing.plaf.basic.BasicToggleButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

                int w = c.getWidth();
                int h = c.getHeight();
                int arco = 15;

                // --- LÓGICA DE ESTADOS (ESTO CORRIGE EL CYAN AL INICIO) ---
                Color cBase;
                int alpha;

                if (!c.isEnabled()) {
                    // Si el botón está deshabilitado (setEnabled(false)), ignoramos el Cyan
                    cBase = new Color(180, 180, 180); // Gris cristalino
                    alpha = 80;
                } else {
                    // Si está habilitado, usamos el color que pasaste (ej. Cyan)
                    cBase = fondo;
                    // Brillo dinámico: Seleccionado > Hover > Normal
                    alpha = boton.isSelected() ? 240 : (boton.getModel().isRollover() ? 210 : 180);
                }

                // 1. CUERPO DEL CRISTAL (Degradado)
                Color colTop = new Color(cBase.getRed(), cBase.getGreen(), cBase.getBlue(), alpha);
                // Si está seleccionado, invertimos el degradado para efecto "presionado"
                java.awt.GradientPaint cuerpo = new java.awt.GradientPaint(0, 0,
                        boton.isSelected() ? colTop.darker() : colTop,
                        0, h,
                        boton.isSelected() ? colTop : colTop.darker());

                g2.setPaint(cuerpo);
                g2.fill(new java.awt.geom.RoundRectangle2D.Double(1, 1, w - 2, h - 2, arco, arco));

                // 2. REFLEJO DE CRISTAL PULIDO (Brillo superior)
                int alphaBrillo = c.isEnabled() ? (boton.isSelected() ? 120 : 70) : 40;
                java.awt.GradientPaint brillo = new java.awt.GradientPaint(0, 0, new Color(255, 255, 255, alphaBrillo), 0, h / 2, new Color(255, 255, 255, 0));
                g2.setPaint(brillo);
                g2.fill(new java.awt.geom.RoundRectangle2D.Double(3, 2, w - 6, h / 2.2, arco, arco));

                // 3. BORDE (Reacciona al estado)
                if (!c.isEnabled()) {
                    g2.setColor(new Color(130, 130, 130, 100)); // Borde gris apagado
                } else if (boton.isSelected()) {
                    g2.setColor(Color.WHITE); // Borde blanco brillante si está activo
                    g2.setStroke(new java.awt.BasicStroke(2.2f));
                } else {
                    g2.setColor(new Color(borde.getRed(), borde.getGreen(), borde.getBlue(), 200));
                    g2.setStroke(new java.awt.BasicStroke(1.2f));
                }
                g2.draw(new java.awt.geom.RoundRectangle2D.Double(1, 1, w - 2, h - 2, arco, arco));

                // 4. TEXTO NÍTIDO Y CENTRADO
                g2.setColor(c.isEnabled() ? boton.getForeground() : Color.GRAY);
                java.awt.FontMetrics metrics = g2.getFontMetrics();
                int x = (w - metrics.stringWidth(textoLimpio)) / 2;
                int y = ((h - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString(textoLimpio, x, y);

                g2.dispose();
            }
        });
    }

    public void botonDeshabilitado(JButton boton) {
        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = c.getWidth();
                int h = c.getHeight();

                if (!c.isEnabled()) {
                    // --- DIBUJO ESTADO DESHABILITADO (GRIS) ---
                    // Fondo gris suave con bordes redondeados (arco 14 para matchear tus otros botones)
                    g2.setColor(new Color(200, 200, 200, 50));
                    g2.fillRoundRect(1, 1, w - 2, h - 2, 14, 14);

                    // Borde gris definido
                    g2.setColor(new Color(150, 150, 150, 100));
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(1, 1, w - 2, h - 2, 14, 14);

                    // Texto en gris nítido y centrado
                    g2.setColor(Color.GRAY);
                    g2.setFont(c.getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    String texto = boton.getText().replaceAll("\\<.*?\\>", "");
                    int x = (w - fm.stringWidth(texto)) / 2;
                    int y = ((h - fm.getHeight()) / 2) + fm.getAscent();
                    g2.drawString(texto, x, y);

                } else {
                    // Si está habilitado, NO hace nada aquí (debería usar su UI de Cristal)
                    // Nota: Si usás dos UIs distintos, recordá que el último setUI sobreescribe al anterior.
                    super.paint(g, c);
                }
                g2.dispose();
            }
        });
    }

public String buscarVencimiento(int factura) {
    String vencimiento = "----";

    String sqlCliente = "SELECT cod_cliente FROM factura WHERE codigo = ?;";
    String sql = "SELECT "
            + "(julianday('now') - julianday(f.fechaFactura, '+' || c.diaspago || ' day')) as diasvencido, "
            + "date(f.fechaFactura, '+' || c.diaspago || ' day') as vencimiento, "
            + "(f.montototal - f.adelanto - f.posteriores) as saldo "
            + "FROM factura as f "
            + "INNER JOIN cliente as c ON f.cod_cliente = c.codigocliente "
            + "WHERE f.pagado = 0 AND f.cod_cliente = ? "
            + "ORDER BY diasvencido DESC LIMIT 1;";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement psCliente = conn.prepareStatement(sqlCliente)) {

        int cliente = 0;

        // 1. Obtener cliente
        psCliente.setInt(1, factura);
        try (ResultSet rs = psCliente.executeQuery()) {
            if (rs.next()) {
                cliente = rs.getInt("cod_cliente");
            }
        }

        // 2. Validar cliente
        if (cliente != 0 && cliente != 1) {

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, cliente);

                try (ResultSet rs1 = ps.executeQuery()) {

                    if (rs1.next()) {
                        String fechaSql = rs1.getString("vencimiento");

                        SimpleDateFormat sqliteSdf = new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date fechaConsulta = sqliteSdf.parse(fechaSql);

                        SimpleDateFormat formatoFecha = new SimpleDateFormat(
                                "EEEE, dd 'de' MMMM 'de' yyyy",
                                new Locale("es", "AR")
                        );

                        String fechaFormateada = formatoFecha.format(fechaConsulta);

                        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
                        double saldo = rs1.getDouble("saldo");

                        vencimiento = fechaFormateada + " (" + formatoMoneda.format(saldo) + ")";
                    }
                }
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error en buscarVencimiento: " + e.getMessage());
        e.printStackTrace();
    }

    return vencimiento;
}

 public String buscarSaldo(int factura) {
    String saldoFormateado = "----";

    String sqlCliente = "SELECT cod_cliente FROM factura WHERE codigo = ?;";
    String sqlSaldo = "SELECT saldo_acumulado FROM cliente WHERE codigocliente = ?;";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement psCliente = conn.prepareStatement(sqlCliente)) {

        int cliente = 0;

        // 1. Obtener cliente
        psCliente.setInt(1, factura);
        try (ResultSet rs = psCliente.executeQuery()) {
            if (rs.next()) {
                cliente = rs.getInt("cod_cliente");
            }
        }

        // 2. Obtener saldo
        if (cliente != 0) {
            try (PreparedStatement psSaldo = conn.prepareStatement(sqlSaldo)) {
                psSaldo.setInt(1, cliente);

                try (ResultSet rs1 = psSaldo.executeQuery()) {
                    if (rs1.next()) {
                        NumberFormat formatoMoneda =
                                NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

                        saldoFormateado = formatoMoneda.format(rs1.getDouble("saldo_acumulado"));
                    }
                }
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
                "9 ERROR: CONTACTE AL ADMINISTRADOR: " + e.toString());
    }

    return saldoFormateado;
}

public String buscarDatosFactura(JTable tablaProductos, int numFactura, JLabel nombre, JLabel domicilio,
        JLabel numfactura, JLabel fecha, JLabel cuit, JLabel hora,
        JLabel observaciones, JLabel total, JLabel totalKg, JLabel totalBultos) {

    String metodo = "";

    // CONSULTA AJUSTADA A TU NUEVA TABLA 'productos'
    String consulta = "SELECT d.cantidad, d.fkfactura, d.fkproducto, d.precioventa, f.fechaFactura, f.metodopago, "
            + "f.montototal, f.observaciones, f.cod_cliente, c.cuit, c.direccion, c.nombre as cliente_nombre, "
            + "p.nombre as producto_nombre FROM detalle as d "
            + "INNER JOIN factura as f ON d.fkfactura = f.codigo "
            + "INNER JOIN cliente as c ON f.cod_cliente = c.codigocliente "
            + "INNER JOIN productos as p ON d.fkproducto = p.id_producto "
            + "WHERE d.fkfactura = ?;";

    DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
    modelo.setRowCount(0);

    SimpleDateFormat formatoFecha = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy");
    DecimalFormat dfKg = new DecimalFormat("#,##0.00");
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    double totalFacturaAcumulado = 0;
    double totalFacturaConsulta = 0;
    double acumuladorKg = 0;
    int acumuladorBultos = 0;

    Conexion objetoConexion = new Conexion();

    try (Connection conn = objetoConexion.estableceConexion();
         PreparedStatement ps = conn.prepareStatement(consulta)) {

        ps.setInt(1, numFactura);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                totalFacturaConsulta = rs.getDouble("montototal");

                double cantidad = rs.getDouble("cantidad");
                String detalle = rs.getString("producto_nombre"); // Ajustado a 'nombre'
                double unitario = rs.getDouble("precioventa");
                double subtotal = cantidad * unitario;

                totalFacturaAcumulado += subtotal;
                acumuladorKg += cantidad;

                // Como 'kgbulto' no existe en tu tabla, usamos 1 o lógica personalizada
                int cantidadbulto = (int) Math.ceil(cantidad); 
                acumuladorBultos += cantidadbulto;

                // Datos cliente/factura
                nombre.setText(rs.getString("cliente_nombre"));
                domicilio.setText(rs.getString("direccion"));
                observaciones.setText(rs.getString("observaciones"));
                metodo = rs.getString("metodopago");
                numfactura.setText(String.format("%08d", rs.getInt("fkfactura")));

                String numDNI = rs.getString("cuit");
                cuit.setText((numDNI == null || numDNI.equals("0")) ? "" : numDNI);

                Timestamp ts = rs.getTimestamp("fechaFactura");
                if (ts != null) {
                    fecha.setText(formatoFecha.format(new java.util.Date(ts.getTime())));
                    hora.setText(ts.toLocalDateTime().toLocalTime().toString());
                }

                // Agregar fila al modelo de 5 columnas
                modelo.addRow(new Object[]{
                        cantidad,
                        cantidadbulto,
                        detalle,
                        formatoMoneda.format(unitario),
                        formatoMoneda.format(subtotal)
                });
            }
        }

        // Totales
        totalKg.setText(dfKg.format(acumuladorKg) + " Un/Kg");
        totalBultos.setText(String.valueOf(acumuladorBultos));

        if (Math.abs(totalFacturaAcumulado - totalFacturaConsulta) < 0.01) {
            total.setText(formatoMoneda.format(totalFacturaAcumulado));
        } else {
            total.setText(formatoMoneda.format(totalFacturaConsulta));
        }

        // Renderers (Asegúrate de que las clases paddingleft y paddingRig existan)
        DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
        cent.setHorizontalAlignment(SwingConstants.CENTER);
        
        tablaProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
        tablaProductos.getColumnModel().getColumn(1).setCellRenderer(cent);
        // tablaProductos.getColumnModel().getColumn(2).setCellRenderer(new paddingleft()); // Descomenta si existen
        // tablaProductos.getColumnModel().getColumn(3).setCellRenderer(new paddingRig());
        // tablaProductos.getColumnModel().getColumn(4).setCellRenderer(new paddingRig());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al recuperar factura: " + e.getMessage());
        e.printStackTrace();
    }

    return metodo;
}

// --- CLASES CORREGIDAS PARA FORZAR EL MARGEN ---

public class paddingleft extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setHorizontalAlignment(SwingConstants.LEFT);
        this.setOpaque(false); // Transparente para ver el fondo
        // Forzamos el margen con un borde vacío interno
        this.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); 
        return this;
    }
}

public class paddingRig extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setHorizontalAlignment(SwingConstants.RIGHT);
        this.setOpaque(false); // Transparente para ver el fondo
        // Forzamos el margen con un borde vacío interno
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); 
        return this;
    }
}
    
  public String ejecutarSentencia(String consulta) {

    try (Connection conn = Conexion.getConexion();
         Statement st = conn.createStatement()) {

        // Configuración SQLite
        st.execute("PRAGMA journal_mode=WAL;");
        st.execute("PRAGMA busy_timeout=5000;");
        st.execute("PRAGMA synchronous=NORMAL;");

        conn.setAutoCommit(false);

        st.executeUpdate(consulta);

        conn.commit();

        return "Sentencia ejecutada con éxito!!!";

    } catch (Exception e) {
        return "<html>Error: " + e.toString() + "</html>";
    }
}

    public void guardarConfiguracionEmpresa(JTextField lblTel1, JTextField lblTel2, JTextField lblCel,
        JTextField lblRuta, JTextField lblMsg) {

    String sql = "UPDATE administrador SET telefono1 = ?, telefono2 = ?, "
            + "celular = ?, ruta = ?, mensaje = ? WHERE id = 1;";

    try (Connection con = Conexion.getConexion();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, lblTel1.getText().trim());
        pst.setString(2, lblTel2.getText().trim());
        pst.setString(3, lblCel.getText().trim());
        pst.setString(4, lblRuta.getText().trim());
        pst.setString(5, lblMsg.getText().trim());

        int resultado = pst.executeUpdate();

        if (resultado > 0) {
            JOptionPane.showMessageDialog(null, "Configuración guardada con éxito.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        e.printStackTrace();
    }
}

   public Administrador obtenerDatosAdministrador() {

    Administrador admin = new Administrador();

    String sql = "SELECT id, telefono1, telefono2, celular, ruta, mensaje "
            + "FROM administrador WHERE id = 1;";

    try (Connection con = Conexion.getConexion();
         PreparedStatement pst = con.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        if (rs.next()) {
            admin.setId(rs.getInt("id"));
            admin.setTelefono1(rs.getString("telefono1"));
            admin.setTelefono2(rs.getString("telefono2"));
            admin.setCelular(rs.getString("celular"));
            admin.setRutaImagenes(rs.getString("ruta"));
            admin.setMensaje(rs.getString("mensaje"));
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        e.printStackTrace();
    }

    return admin;
}
   }