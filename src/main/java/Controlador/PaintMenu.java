/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

/**
 *
 * @author ALCIDES
 */
public class PaintMenu extends BasicMenuItemUI {

    public static Color color;  
     public static Color letra;

    public static ComponentUI createUI(JComponent c) {
        return new PaintMenu();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);}
      //  c.setBorder(BorderFactory.createEmptyBorder(3, 6, 2, 3));
  @Override
           protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
                Graphics2D g2 = (Graphics2D) g.create();
  g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  
  int a= color.getRed();
  int b= color.getGreen();
  int c= color.getBlue();
  
                if (menuItem.getModel().isArmed() || menuItem.getModel().isSelected()) {
                    g2.setColor(new Color(a,b, c, 220));

                } else {
                    g2.setColor(new Color(a, b,c, 180));
                }
                g2.fillRoundRect(4, 1, menuItem.getWidth() - 4, menuItem.getHeight() - 2, 8, 8);
                g2.dispose();
            }
    }

