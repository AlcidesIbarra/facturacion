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
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Alerta extends JWindow {
    public Alerta(String mensaje, Color colorFondo) {
        setAlwaysOnTop(true);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(colorFondo);
        setShape(new RoundRectangle2D.Double(0, 0, 300, 50, 20, 20));

        JLabel label = new JLabel(mensaje);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(label);

        setSize(300, 50);
        
        // Posicionar en la esquina inferior derecha
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(scrSize.width - 320, scrSize.height - 100);

        // Hilo para que desaparezca sola
        new Thread(() -> {
            try {
                setVisible(true);
                Thread.sleep(3000); // Se muestra 3 segundos
                dispose();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
