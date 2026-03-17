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

    public Alerta(String mensaje, Color colorFondo, JInternalFrame parent) {
        // 1. Color con transparencia (Glassmorphism)
        Color colorTrans = new Color(colorFondo.getRed(), colorFondo.getGreen(), colorFondo.getBlue(), 200);
        
        setOpaque(false);
        setBackground(colorTrans);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // 2. TEXTO: Limpieza profunda de estilos globales
        JTextPane txt = new JTextPane();
        
        // Forzamos un UI básico para ignorar bordes negros o estilos del TextArea global
        txt.setUI(new BasicTextPaneUI()); 
        txt.setEditable(false);
        txt.setFocusable(false);
        txt.setOpaque(false);
        txt.setBackground(new Color(0, 0, 0, 0));
        txt.setBorder(null); // Elimina tu borde negro global en este componente
        txt.setHighlighter(null); 
        
        // Estilo del texto
        txt.setText(mensaje);
        txt.setFont(new Font("SansSerif", Font.BOLD, 14));
        txt.setForeground(Color.WHITE);

        // Centrado de texto (Párrafo)
        StyledDocument doc = txt.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        
        txt.setSize(new Dimension(260, Short.MAX_VALUE));
        add(txt, BorderLayout.CENTER);

        // 3. Dimensiones y Posición (Arriba Centrado)
        Dimension d = getPreferredSize();
        setSize(new Dimension(300, d.height));
        int x = (parent.getWidth() - getWidth()) / 2;
        setLocation(x, 15);

        // 4. ESCUCHA GLOBAL: Cierra al hacer clic en CUALQUIER componente (TextFields, Botones, etc.)
        clickGlobal = event -> {
            if (event instanceof MouseEvent && event.getID() == MouseEvent.MOUSE_PRESSED) {
                cerrarAlerta(parent);
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(clickGlobal, AWTEvent.MOUSE_EVENT_MASK);

        // 5. Agregar al LayeredPane para que flote
        parent.getLayeredPane().add(this, JLayeredPane.POPUP_LAYER);
        iniciarTemporizador(parent);
    }

    private synchronized void cerrarAlerta(JInternalFrame parent) {
        if (clickGlobal != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(clickGlobal);
            clickGlobal = null;
        }
        setVisible(false);
        parent.getLayeredPane().remove(this);
        parent.repaint();
    }

    private void iniciarTemporizador(JInternalFrame parent) {
        new Thread(() -> {
            try {
                Thread.sleep(3500); // Duración de la alerta
                SwingUtilities.invokeLater(() -> cerrarAlerta(parent));
            } catch (InterruptedException e) {
                // Hilo interrumpido
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fondo redondeado con el color transparente
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
        
        // Borde blanco sutil (efecto cristal)
        g2.setColor(new Color(255, 255, 255, 60));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 25, 25));
        
        g2.dispose();
    }
}
