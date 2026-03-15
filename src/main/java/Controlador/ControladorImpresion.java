/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import javax.swing.JTable;
import Formularios.ModeloFactura;
import com.mysql.cj.protocol.Resultset;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.imageio.ImageIO;
import java.io.File;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PageRanges;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ALCIDES
 */
public class ControladorImpresion implements Printable {

    private JPanel panelAImprimir;
  

    // ESTO ES LO QUE TE FALTA: El constructor con el parámetro
    public ControladorImpresion(JPanel panelRecibido) {
        this.panelAImprimir = panelRecibido;
    }
   public ControladorImpresion() {
     
    }
    
    private BufferedImage fotoFactura;
    private int anchoOriginal;
    private int altoOriginal;

public void guardarCaptura(JPanel frameImprimir) {
    // 1. CAPTURA DE IMAGEN (Congelamos antes de cualquier diálogo)
    this.anchoOriginal = frameImprimir.getWidth();
    this.altoOriginal = frameImprimir.getHeight();
    
    // Si el panel no tiene tamaño aún (pasa en el JAR), le damos uno base
    if (anchoOriginal <= 0) anchoOriginal = 600;
    if (altoOriginal <= 0) altoOriginal = 800;

    this.fotoFactura = new BufferedImage(anchoOriginal * 2, altoOriginal * 2, BufferedImage.TYPE_INT_RGB);
    Graphics2D gImg = fotoFactura.createGraphics();
    gImg.setColor(Color.WHITE);
    gImg.fillRect(0, 0, fotoFactura.getWidth(), fotoFactura.getHeight());
    gImg.scale(2.0, 2.0);
    frameImprimir.printAll(gImg);
    gImg.dispose();

    // 2. CONFIGURACIÓN DEL TRABAJO
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setJobName("Factura_Frigorifico");

    // Atributos básicos para FORZAR 1 SOLA PÁGINA
    PrintRequestAttributeSet atributos = new HashPrintRequestAttributeSet();
    atributos.add(new PageRanges(1, 1)); 

    job.setPrintable(this); // Le pasamos la clase actual

    // 3. MOSTRAR DIÁLOGO Y EJECUTAR
    // Usamos el diálogo nativo que es más rápido y no falla en el JAR
    if (job.printDialog(atributos)) { 
        try {
            // PASAMOS LOS ATRIBUTOS AQUÍ TAMBIÉN
            job.print(atributos); 
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(null, "Error al imprimir: " + e.getMessage());
        }
    }
}

@Override
public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
    // SEGUNDO BLOQUEO: Solo permitimos la página 0 (la primera)
    if (pageIndex > 0 || fotoFactura == null) {
        return NO_SUCH_PAGE;
    }

    Graphics2D g2d = (Graphics2D) graphics;

    // ESCALADO PARA OCUPAR TODA LA HOJA
    double pgAncho = pageFormat.getImageableWidth();
    double pgAlto = pageFormat.getImageableHeight();
    
    double escalaX = pgAncho / anchoOriginal;
    double escalaY = pgAlto / altoOriginal;
    double escalaFinal = Math.min(escalaX, escalaY);

    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
    g2d.scale(escalaFinal, escalaFinal);

    // Dibujamos la imagen "foto" (el mouse ya no afecta)
    g2d.drawImage(fotoFactura, 0, 0, null);

    return PAGE_EXISTS;
}

}