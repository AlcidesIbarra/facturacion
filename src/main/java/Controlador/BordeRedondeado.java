/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author ALCIDES
 */
public class BordeRedondeado extends AbstractBorder{
    private Color color;
    private int radio;

    public BordeRedondeado(Color color, int radio) {
        this.color = color;
        this.radio = radio;
    }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y,int whidth,int height){
            Graphics2D g2 = (Graphics2D )g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
              
                        g2.setColor(color);
                        g2.drawRoundRect(x, y, whidth-1, height-1, radio, radio);
        } 
         @Override
        public Insets getBorderInsets(Component c){
        return new Insets(radio/2, radio/2,radio/2,radio/2);
        }
}
