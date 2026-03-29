/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;
import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 *
 * @author ALCIDES
 */
public class ControladorAlertas {

    int contador = 0;
    boolean encendidoAlert = false;
    JPanel glass ;
    JPanel popup = new JPanel();

    public void ocultarPopup() {
      
            glass.setVisible(false);
            popup.setVisible(false);
            popup.removeAll();
            popup.revalidate();
            popup.repaint();
       
    }

    public void mostrarAlerta(String palabra, Integer codigo,JInternalFrame gls) {
        glass=  (JPanel)  gls.getGlassPane();
        ocultarPopup();
        //   glass.setBorder(bordeRedondeado);
//glass.setLayout(new GridBagLayout()); // Para centrar el popup
        glass.setVisible(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                popup.setVisible(false);
                glass.setVisible(false);
                contador = 0;
            }
        });

// Crear el contenido del popup
        popup.setLayout(new BoxLayout(popup, BoxLayout.Y_AXIS));
        ControladorAdministracion ca = new ControladorAdministracion();
        String ruta = ca.traerRutaImg();
        //  ImageIcon advertenciaprim = new ImageIcon(ruta+"/advertencia.png");
        //   Image imgEscalada = new ImageIcon(ruta+"/advertencia.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
        //   ImageIcon advertencia = new ImageIcon(imgEscalada);
        JLabel titulo = new JLabel();

        Border bordeRedondeado = BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 8, true);

        switch (codigo) {

            case 1:
                Image imgEscalada = new ImageIcon(ruta + "/exito.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
                ImageIcon advertencia = new ImageIcon(imgEscalada);
                titulo = new JLabel("EXITO:", advertencia, JLabel.LEFT);

                popup.setBackground(new Color(154, 205, 50, 220)); // Blanco semitransparente
                break;
            case 2:
                Image imgEscalada1 = new ImageIcon(ruta + "/advertencia.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
                ImageIcon advertencia1 = new ImageIcon(imgEscalada1);
                titulo = new JLabel("ATENCION:", advertencia1, JLabel.LEFT);

                popup.setBackground(new Color(255, 120, 0, 220)); // Blanco semitransparente

                break;
            case 3:
                Image imgEscalada3 = new ImageIcon(ruta + "/error.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
                ImageIcon advertencia3 = new ImageIcon(imgEscalada3);
                titulo = new JLabel("ADVERTENCIA:", advertencia3, JLabel.LEFT);

                popup.setBackground(new Color(255, 0, 0, 220)); // Blanco semitransparente
                break;
            case 4:
                Image imgEscalada4 = new ImageIcon(ruta + "/sistema.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
                ImageIcon advertencia4 = new ImageIcon(imgEscalada4);
                titulo = new JLabel("ERROR:", advertencia4, JLabel.LEFT);

                popup.setBackground(new Color(150, 150, 150, 220)); // Blanco semitransparente
                break;
            default:
                throw new AssertionError();
        }

//JLabel titulo= new JLabel("ADVERTENCIA:");
        Font fuente = new Font("consolass", Font.BOLD, 21);
        Font fuente1 = new Font("consolass", Font.BOLD, 16);

        JLabel contenido = new JLabel(palabra);

        titulo.setFont(fuente);
        contenido.setFont(fuente1);

        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenido.setAlignmentX(Component.CENTER_ALIGNMENT);

//popup.add(Box.createVerticalGlue());
        popup.add(titulo, BorderLayout.CENTER);
//popup.add(Box.createRigidArea(new Dimension(0,0)));
        popup.add(contenido, BorderLayout.CENTER);
//popup.add(Box.createVerticalGlue());

        glass.add(popup);

        popup.setBorder(bordeRedondeado);
        popup.setVisible(true);

// Añadir el Listener al Frame
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popup.setVisible(false);
                contador = 0;
            }
        });

        // Temporizador para cerrar automáticamente
        Timer timer = new Timer(8000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contador = 0;
                popup.setVisible(false); // Cierra el JDialog
                ((Timer) e.getSource()).stop(); // Detiene el timer
            }
        });
        timer.setRepeats(false); // Se ejecuta una sola vez
        timer.start();

       // this.setFocusable(true);
      //  this.requestFocusInWindow();
    //    this.requestFocus();

        //  contador = 0;
    }
}
