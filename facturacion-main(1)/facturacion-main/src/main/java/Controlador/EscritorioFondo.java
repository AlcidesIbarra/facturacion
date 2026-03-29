/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

/**
 *
 * @author ALCIDES
 */
public class EscritorioFondo extends JDesktopPane {

    private Image imagen;

    public EscritorioFondo() {
        try {
            ControladorAdministracion ca = new ControladorAdministracion();
            String direc = ca.traerRutaImg();

            URL ruta = getClass().getResource(direc + "/fondo frigorifico");
            if (ruta != null) {
                imagen = new ImageIcon(ruta).getImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
