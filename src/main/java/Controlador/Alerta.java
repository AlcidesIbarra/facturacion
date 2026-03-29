/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author ALCIDES
 */
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.plaf.basic.BasicTextPaneUI;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class Alerta extends JPanel {
    private AWTEventListener clickGlobal;

    /**
     * Constructor Universal de Alerta
     * @param mensaje Texto a mostrar
     * @param colorFondo Color base (se le aplicará transparencia)
     * @param parent Contenedor padre (JFrame, JInternalFrame o JDialog)
     */
    public Alerta(String mensaje, Color colorFondo, final Container parent) {
        // 1. Configuración de Estilo y Transparencia
        Color colorTrans = new Color(colorFondo.getRed(), colorFondo.getGreen(), colorFondo.getBlue(), 200);
        setOpaque(false);
        setBackground(colorTrans);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // 2. Texto Centrado con JTextPane (Ignora estilos globales de TextArea)
        JTextPane txt = new JTextPane();
        txt.setUI(new javax.swing.plaf.basic.BasicTextPaneUI()); 
        txt.setText(mensaje);
        txt.setFont(new Font("SansSerif", Font.BOLD, 14));
        txt.setForeground(Color.WHITE);
        txt.setOpaque(false);
        txt.setEditable(false);
        txt.setFocusable(false);
        txt.setHighlighter(null);

        StyledDocument doc = txt.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        add(txt, BorderLayout.CENTER);

        // 3. Obtener el LayeredPane de forma genérica
        JLayeredPane tempLP = null;
        if (parent instanceof JInternalFrame) {
            tempLP = ((JInternalFrame) parent).getLayeredPane();
        } else if (parent instanceof RootPaneContainer) {
            tempLP = ((RootPaneContainer) parent).getLayeredPane();
        }

        if (tempLP == null) return; 

        // Definimos la variable FINAL para que sea accesible desde hilos y lambdas
        final JLayeredPane layeredPane = tempLP;

        // 4. Dimensiones y Posición (Arriba Centrado)
        setSize(new Dimension(300, getPreferredSize().height));
        setLocation((parent.getWidth() - getWidth()) / 2, 20);

        // 5. ESCUCHA DE CLIC GLOBAL: Cierra al tocar cualquier parte de la app
        clickGlobal = event -> {
            if (event instanceof MouseEvent && event.getID() == MouseEvent.MOUSE_PRESSED) {
                cerrarAlerta(parent, layeredPane);
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(clickGlobal, AWTEvent.MOUSE_EVENT_MASK);

        // 6. Agregar al panel de capas y mostrar
        layeredPane.add(this, JLayeredPane.POPUP_LAYER);
        
        // 7. Temporizador de auto-cierre (3.5 segundos)
        new Thread(() -> {
            try {
                Thread.sleep(3500);
                SwingUtilities.invokeLater(() -> cerrarAlerta(parent, layeredPane));
            } catch (InterruptedException e) {
                // Hilo interrumpido
            }
        }).start();
    }

    /**
     * Limpia los recursos y elimina la alerta de la pantalla
     */
    private synchronized void cerrarAlerta(Container parent, JLayeredPane lp) {
        if (clickGlobal != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(clickGlobal);
            clickGlobal = null;
        }
        this.setVisible(false);
        if (lp != null) {
            lp.remove(this);
        }
        if (parent != null) {
            parent.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujamos el fondo redondeado
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
        
        // Borde fino tipo "cristal"
        g2.setColor(new Color(255, 255, 255, 60));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 25, 25));
        
        g2.dispose();
    }
}