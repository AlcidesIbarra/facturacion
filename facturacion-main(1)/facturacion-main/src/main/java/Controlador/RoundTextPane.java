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
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.plaf.metal.MetalTextFieldUI;

/**
 *
 * @author ALCIDES
 */
public class RoundTextPane  extends BasicTextPaneUI{
    
     public static Color color;
   public static int  radio;
            
   public static ComponentUI createUI(JComponent c){
   return new RoundTextPane();
   }
   
    @Override
    public void installUI(JComponent c){
        super.installUI(c);
        c.setOpaque(false);
        c.setBorder(BorderFactory.createEmptyBorder(3,6,2,3));
        
    }
    
      @Override
     protected void paintSafely(Graphics g){
         JComponent c=getComponent();
            Graphics2D g2 = (Graphics2D )g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
              
                        g2.setColor(c.getBackground());
                        g2.fillRoundRect(0,0, c.getWidth()-1, c.getHeight()-1, radio, radio);
                        
                        g2.setColor(Color.BLACK);
                        g2.drawRoundRect(0,0, c.getWidth()-1, c.getHeight()-1, radio, radio);
                        g2.dispose();
                        super.paintSafely(g);
                        
        } 
    
    
}