/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;

import DAO.DaoProducto;
import Controlador.EscritorioFondo;
import static Controlador.PaintMenu.color;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import static java.nio.file.Files.size;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import configuracion.Conexion;





/**
 *
 * @author ALCIDES
 */
public class MenuPrincipal extends javax.swing.JFrame {

    private EscritorioFondo panelNuevo;
    private Image imagen;
    private String rol;

    // 1. ELEGIMOS UN GRIS OSCURO BASE
    private final Color COLOR_BASE = new Color(40, 40, 40);
    // 2. DEFINIMOS LA OPACIDAD (0.2f es un 20%)
    private final float OPACIDAD = 0.2f;

    public MenuPrincipal() {

        // 1. Configurar el panel de fondo antes de iniciar componentes
      
        panelNuevo = new EscritorioFondo(obtenerNombreImagenBD());
        this.setContentPane(panelNuevo);

        initComponents();
        this.setLocationRelativeTo(null);

        this.setBackground(Color.WHITE);
        
        try {
    // Busca la imagen en tu carpeta de recursos
    Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/main/imagenes/logo.png"));
    this.setIconImage(icono);
} catch (Exception e) {
    System.out.println("No se pudo cargar el ícono: " + e.getMessage());
}
        // 2. Cargar datos desde tu ControladorAdministracion actual
        ControladorAdministracion ca = new ControladorAdministracion();
        String direc = "/main/Imagenes";
        Color colorMenu = ca.retornatexFondoMenu();
        Color letraMenu = ca.retornaletraMenu();

        // 3. Configurar Iconos de Botones (Usando método auxiliar para limpiar el código)
        btnFacturar.setIcon(generarIcono(direc + "/facturar.png"));
        btnCliente.setIcon(generarIcono(direc + "/usuario.png"));
        btnConsultar.setIcon(generarIcono(direc + "/factura.png"));
        btnArt.setIcon(generarIcono(direc + "/articulo.png"));
        btnEditArt.setIcon(generarIcono(direc + "/editar.png"));

        // 4. Aplicar transparencia a botones (Cristal)
        ca.BotonesCrisital(btnFacturar);
        ca.BotonesCrisital(btnCliente);
        ca.BotonesCrisital(btnConsultar);
        ca.BotonesCrisital(btnArt);
        ca.BotonesCrisital(btnEditArt);

        // Quitar márgenes molestos
        javax.swing.AbstractButton[] botones = {btnFacturar, btnCliente, btnConsultar, btnArt, btnEditArt};
        for (javax.swing.AbstractButton b : botones) {
            b.setMargin(new java.awt.Insets(0, 0, 0, 0));
        }

        try {
            java.net.URL urlLogo = getClass().getResource("/main/Imagenes/logo.png");

            if (urlLogo != null) {
                // 1. Cargamos el icono (esto espera a que la imagen esté lista)
                ImageIcon iconoProvisorio = new ImageIcon(urlLogo);

                // 2. Escalamos y nos aseguramos de que el escalado termine
                Image imgEscalada = iconoProvisorio.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

                // 3. Forzamos la espera del escalado creando otro ImageIcon
                ImageIcon iconoFinal = new ImageIcon(imgEscalada);

                // 4. Ahora sí, pasamos la imagen REAL (ya cargada) al método circular
                logo.setIcon(circular(iconoFinal.getImage()));

            } else {
                System.err.println("No se encontró el logo en la ruta especificada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 6. AUTOMATIZACIÓN DE MENÚS (Cristaliza todos los JMenuItem sin listarlos uno por uno)
        JMenuBar barra = this.getJMenuBar();
        if (barra != null) {
            for (int i = 0; i < barra.getMenuCount(); i++) {
                JMenu menu = barra.getMenu(i);
                // Hacer el popup invisible para que solo se vea el cristal
                menu.getPopupMenu().setOpaque(false);
                menu.getPopupMenu().setBackground(new Color(0, 0, 0, 0));
                menu.getPopupMenu().setBorder(javax.swing.BorderFactory.createEmptyBorder());

                for (java.awt.Component item : menu.getMenuComponents()) {
                    if (item instanceof JMenuItem) {
                        cristalizar((JMenuItem) item, colorMenu, letraMenu);
                    }
                }
            }
        }

        // 7. Texto de bienvenida
        bienvenida.setForeground(new Color(0, 255, 255, 180));
    }
    
    //recibo el rol
public MenuPrincipal(String rol) {
    this(); // 👈 ESTO LLAMA AL CONSTRUCTOR PRINCIPAL

    this.rol = rol;

    // CONTROL DE PERMISOS
    if (!rol.equals("Administrador")) {
        btnEditArt1.setVisible(false);
    }
}

    // --- MÉTODOS AUXILIARES ---
public class EscritorioFondo extends javax.swing.JDesktopPane {
    private Image imagen;
    
    // ============================================================
    // 1. AJUSTÁ ESTOS VALORES PARA EL FONDO DE AFUERA (0 a 255)
    // ============================================================
private final int ROJO_FONDO  = 128; 
private final int VERDE_FONDO = 128; 
private final int AZUL_FONDO  = 128; 
    
    private final Color COLOR_PANEL = new Color(ROJO_FONDO, VERDE_FONDO, AZUL_FONDO);
    
    // 2. AJUSTÁ QUÉ TAN OSCURA QUERÉS LA IMAGEN (0.0 a 1.0)
    private final float OSCURIDAD_IMAGEN = 0.50f; 
    // ============================================================

public EscritorioFondo(String nombreArchivo) {
    this.setBackground(COLOR_PANEL);
    this.setOpaque(true);
    try {
        // Buscamos la ruta con el nombre que pases (ej: "fondo.jpg" o "logo.png")
        java.net.URL url = getClass().getResource("/main/Imagenes/" + nombreArchivo);
        
        if (url != null) {
            imagen = new ImageIcon(url).getImage();
        } else {
            System.err.println("No se encontró el archivo: " + nombreArchivo);
                java.net.URL url1 = getClass().getResource("/main/Imagenes/fondo_defecto.png");
                 imagen = new ImageIcon(url1).getImage();
        }
    } catch (Exception e) {
        System.err.println("Error al cargar imagen: " + e.getMessage());
    }
}


    @Override
    protected void paintComponent(Graphics g) {
        // PINTAR EL FONDO MANUAL (El que rodea la imagen)
        g.setColor(COLOR_PANEL);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (imagen != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Nitidez máxima
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Escala y posición
            int imgW = imagen.getWidth(this);
            int imgH = imagen.getHeight(this);
            double scale = Math.min((double) getWidth() / imgW, (double) getHeight() / imgH);
            int newW = (int) (imgW * scale);
            int newH = (int) (imgH * scale);
            int x = (getWidth() - newW) / 2;
            int y = (getHeight() - newH) / 2;

            // DIBUJAR LA IMAGEN NITIDA
            g2d.drawImage(imagen, x, y, newW, newH, this);

            // OSCURECER SOLO LA IMAGEN
            // Usamos un negro transparente para "apagar" el blanco de la imagen
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, OSCURIDAD_IMAGEN));
            g2d.setColor(Color.BLACK); 
            g2d.fillRect(x, y, newW, newH);

            g2d.dispose();
        }
    }
}

    private ImageIcon generarIcono(String ruta) {
        try {
            // Buscamos dentro de src/main/resources
            java.net.URL url = getClass().getResource(ruta);

            if (url != null) {
                // Cargamos y escalamos
                ImageIcon iconoOriginal = new ImageIcon(url);
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                return new ImageIcon(imgEscalada);
            } else {
                // Esto te dirá en la consola exactamente qué ruta está fallando
                System.err.println("ERROR: No se encontró el archivo en: " + ruta);
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    
public String obtenerNombreImagenBD() {

    Conexion objetoConexion = new Conexion();
    String nombreImagen = "fondo_defecto.png";

    String sql = "SELECT nombre_img FROM administrador WHERE id = 1";

    try (Connection conn = objetoConexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            String valorBD = rs.getString("nombre_img");
            if (valorBD != null && !valorBD.isEmpty()) {
                nombreImagen = valorBD;
            }
        }

    } catch (Exception e) {
        System.err.println("Error al buscar imagen en BD: " + e.getMessage());
    }

    return nombreImagen;
}
    
    public static void cristalizar(JMenuItem item, Color color, Color letra) {
        item.setOpaque(false);
        item.setContentAreaFilled(false);
        item.setForeground(letra);
        item.setBorderPainted(false);

        item.setUI(new javax.swing.plaf.basic.BasicMenuItemUI() {
            @Override
            public void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int r = color.getRed();
                int gr = color.getGreen();
                int b = color.getBlue();
                int alpha = (menuItem.getModel().isArmed() || menuItem.getModel().isSelected()) ? 220 : 180;

                g2.setColor(new Color(r, gr, b, alpha));
                g2.fillRoundRect(2, 1, menuItem.getWidth() - 4, menuItem.getHeight() - 2, 8, 8);
                g2.dispose();
            }
        });
    }

    public Icon circular(Image img) {
        int d = Math.min(img.getWidth(null), img.getHeight(null));
        BufferedImage bi = new BufferedImage(d, d, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.white);
        g2.fill(new java.awt.geom.Ellipse2D.Double(0, 0, d, d));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, .6f));
        g2.drawImage(img, 0, 0, d, d, null);
        g2.dispose();
        return new ImageIcon(bi);
    }

    // --- CLASE INTERNA PARA EL FONDO ---
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        alertas = new javax.swing.JDialog();
        btnFacturar = new javax.swing.JButton();
        btnCliente = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        btnArt = new javax.swing.JButton();
        btnEditArt = new javax.swing.JButton();
        bienvenida = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        btnEditArt1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();

        jMenuItem11.setText("jMenuItem11");

        jMenuItem13.setText("jMenuItem13");

        jMenuItem19.setText("jMenuItem19");

        javax.swing.GroupLayout alertasLayout = new javax.swing.GroupLayout(alertas.getContentPane());
        alertas.getContentPane().setLayout(alertasLayout);
        alertasLayout.setHorizontalGroup(
            alertasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        alertasLayout.setVerticalGroup(
            alertasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 106, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FRIGORIFICO FENA - FENA");
        setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        btnFacturar.setText("FACTURAR");
        btnFacturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturarActionPerformed(evt);
            }
        });

        btnCliente.setText("<html>AGREGAR<br>CLIENTE</html>");
        btnCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteActionPerformed(evt);
            }
        });

        btnConsultar.setText("<html>CONSULTAR<br> &nbsp;&nbsp;&nbsp;&nbsp; VENTA</html>");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        btnArt.setText("ARTICULOS");
        btnArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArtActionPerformed(evt);
            }
        });

        btnEditArt.setText("<html>&nbsp; &nbsp;   EDITAR<br> ARTICULOS</html>");
        btnEditArt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditArtActionPerformed(evt);
            }
        });

        bienvenida.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        bienvenida.setForeground(new java.awt.Color(51, 51, 255));
        bienvenida.setText("BIENVENIDO: SUPER CARNES FENA - FENA");

        btnEditArt1.setText("CREAR USUARIO");
        btnEditArt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditArt1ActionPerformed(evt);
            }
        });

        jMenu1.setText("Venta");
        jMenu1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem20.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem20.setText("Facturar");
        jMenuItem20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem20MousePressed(evt);
            }
        });
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem20);

        jMenuItem21.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem21.setText("Consultar Venta");
        jMenuItem21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem21MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem21MousePressed(evt);
            }
        });
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem21);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Clientes");
        jMenu2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem24.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem24.setText("Nuevo");
        jMenuItem24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem24MouseClicked(evt);
            }
        });
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem24);

        jMenuItem7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem7.setText("Editar");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem26.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem26.setText("Asignar Precios");
        jMenuItem26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem26MouseClicked(evt);
            }
        });
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem26);

        jMenuItem2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem2.setText("Lista Clientes");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Productos");
        jMenu3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenu3MousePressed(evt);
            }
        });
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        jMenuItem18.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem18.setText("Ingreso Factura");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem18);

        jMenuItem6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem6.setText("Agregar");
        jMenuItem6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem6MousePressed(evt);
            }
        });
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem4.setText("Editar");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem16.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem16.setText("Ajuste Stock");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem16);

        jMenuItem17.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem17.setText("Actualizar Precios");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem17);

        jMenuItem23.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem23.setText("Stock Disponible");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem23);

        jMenuItem5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem5.setText("Lista Productos");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem8.setText("Actualizacion Masiva");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenuItem34.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem34.setText("Importacion Masiva");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem34);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Reportes");
        jMenu4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        jMenuItem3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem3.setText("Venta Diaria");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem12.setText("Venta Semanal");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem12);

        jMenuItem31.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem31.setText("Venta Mensual");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem31);

        jMenuItem32.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem32.setText("Venta Anual");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem32);

        jMenuItem14.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem14.setText("Ventas por Fecha");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem14);

        jMenuItem25.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem25.setText("Venta Detalle - Ganancia");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem25);

        jMenuItem27.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem27.setText("Venta por Cliente por fecha");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem27);

        jMenuItem33.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem33.setText("Venta detallada por Cliente");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem33);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Cuentas");
        jMenu5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenu5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenu5MousePressed(evt);
            }
        });
        jMenu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu5ActionPerformed(evt);
            }
        });

        jMenuItem9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem9.setText("Ingresar Pago");
        jMenuItem9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem9MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem9MousePressed(evt);
            }
        });
        jMenu5.add(jMenuItem9);

        jMenuItem10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem10.setText("Saldos Totales");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem10);

        jMenuItem15.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem15.setText("Detalle Pagos");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem15);

        jMenuItem22.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem22.setText("Editar Limite");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem22);

        jMenuItem29.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem29.setText("Saldos por Cliente");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem29);

        jMenuItem30.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem30.setText("Saldos Vencidos");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem30);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("Configuracion");
        jMenu6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenu6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu6ActionPerformed(evt);
            }
        });

        jMenuItem28.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem28.setText("Colores");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem28);

        jMenuItem1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem1.setText("Datos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem1);

        jMenuBar1.add(jMenu6);

        jMenu7.setText("Salir");
        jMenu7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenu7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu7MouseClicked(evt);
            }
        });
        jMenu7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu7ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu7);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnFacturar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bienvenida, javax.swing.GroupLayout.PREFERRED_SIZE, 736, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditArt, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnArt, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditArt1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(179, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(bienvenida, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnArt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditArt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditArt1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(235, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        ListaFacturas objetoFormularioProducto = new ListaFacturas(6);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana         // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        FormAjustes objetoFormularioProducto = new FormAjustes(2);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        FormAgregarCliente objetoFormularioAgregarCliente = new FormAgregarCliente(1);
        panelNuevo.add(objetoFormularioAgregarCliente);
        objetoFormularioAgregarCliente.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioAgregarCliente.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioAgregarCliente.setLocation(x - 10, 10);
        objetoFormularioAgregarCliente.toFront(); // Trae al frente la nueva ventana


    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
        FormVenta objetoFormularioVentas = new FormVenta();
        panelNuevo.add(objetoFormularioVentas);
        objetoFormularioVentas.setVisible(true);

    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked

    }//GEN-LAST:event_jMenu1MouseClicked

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        this.requestFocusInWindow();
        FormVenta objetoFormularioVentas = new FormVenta();
        panelNuevo.add(objetoFormularioVentas);

        objetoFormularioVentas.setVisible(true);  // TODO add your handling code here:

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioVentas.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioVentas.setLocation(x - 10, 10);
        objetoFormularioVentas.toFront(); // Trae al frente la nueva ventana    objetoFormularioVentas.requestFocusInWindow();

    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed

     //   System.out.println("probando");
    }//GEN-LAST:event_jMenu3ActionPerformed

    private void jMenuItem20MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem20MousePressed


    }//GEN-LAST:event_jMenuItem20MousePressed

    private void jMenu3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MousePressed

    }//GEN-LAST:event_jMenu3MousePressed

    private void jMenuItem6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem6MousePressed

    }//GEN-LAST:event_jMenuItem6MousePressed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        //no andas  
        FormProducto objetoFormularioProducto = new FormProducto(1);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        FormAgregarProducto objetoFormularioAgregarProducto = new FormAgregarProducto(1);
        panelNuevo.add(objetoFormularioAgregarProducto);
        objetoFormularioAgregarProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioAgregarProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioAgregarProducto.setLocation(x - 10, 10);
        objetoFormularioAgregarProducto.toFront(); // Trae al frente la nueva ventana         // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem24MouseClicked

    }//GEN-LAST:event_jMenuItem24MouseClicked

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked

    }//GEN-LAST:event_jMenu2MouseClicked

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed

    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenuItem26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem26MouseClicked

    }//GEN-LAST:event_jMenuItem26MouseClicked

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        FormAsignarPreciosCliente objetoFormularioCliente = new FormAsignarPreciosCliente();
        panelNuevo.add(objetoFormularioCliente);
        objetoFormularioCliente.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioCliente.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioCliente.setLocation(x - 10, 10);
        objetoFormularioCliente.toFront(); // Trae al frente la nueva ventana        

// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem21MouseClicked

        /*ModeloFactura objetoFormularioCliente = new ModeloFactura();
        panelNuevo.add(objetoFormularioCliente);
        objetoFormularioCliente.setVisible(true);    
         */
    }//GEN-LAST:event_jMenuItem21MouseClicked

    private void jMenuItem21MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem21MousePressed

    }//GEN-LAST:event_jMenuItem21MousePressed

    private void jMenu5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu5MousePressed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        ListaFacturas objetoFormularioProducto = new ListaFacturas(1);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        FormAjustes objetoFormularioProducto = new FormAjustes(1);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        FormClientes objetoFormularioProducto = new FormClientes();
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem9MouseClicked

    }//GEN-LAST:event_jMenuItem9MouseClicked

    private void jMenuItem9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem9MousePressed
        FormPagos objetoFormularioProducto = new FormPagos();
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_jMenuItem9MousePressed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        ListaFacturas objetoFormularioProducto = new ListaFacturas(2);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        ListaFacturas objetoFormularioProducto = new ListaFacturas(3);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        ListaFacturas objetoFormularioProducto = new ListaFacturas(4);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        ListaFacturas objetoFormularioProducto = new ListaFacturas(5);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        FormAdministracion objetoFormularioProducto = new FormAdministracion();
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void btnFacturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturarActionPerformed

        this.requestFocusInWindow();
        FormVenta objetoFormularioVentas = new FormVenta();
        panelNuevo.add(objetoFormularioVentas);

        objetoFormularioVentas.setVisible(true);  // TODO add your handling code here:

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioVentas.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioVentas.setLocation(x - 10, 10);
        objetoFormularioVentas.toFront(); // Trae al frente la nueva ventana
    }//GEN-LAST:event_btnFacturarActionPerformed

    private void jMenu6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu6ActionPerformed

    }//GEN-LAST:event_jMenu6ActionPerformed

    private void jMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu5ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        ListaFacturas objetoFormularioProducto = new ListaFacturas(7);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront();
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        ListaFacturas objetoFormularioProducto = new ListaFacturas(8);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront();
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        FormReporteUnico objetoFormularioProducto = new FormReporteUnico(1);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        FormReporteUnico objetoFormularioProducto = new FormReporteUnico(2);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        FormReporteUnico objetoFormularioProducto = new FormReporteUnico(4);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront();
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        FormReporteUnico objetoFormularioProducto = new FormReporteUnico(3);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront();
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        FormAgregarCliente objetoFormularioAgregarCliente = new FormAgregarCliente(2);
        panelNuevo.add(objetoFormularioAgregarCliente);
        objetoFormularioAgregarCliente.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioAgregarCliente.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioAgregarCliente.setLocation(x - 10, 10);
        objetoFormularioAgregarCliente.toFront(); // Trae al frente la nueva ventana
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        FormAgregarProducto objetoFormularioAgregarProducto = new FormAgregarProducto(2);
        panelNuevo.add(objetoFormularioAgregarProducto);
        objetoFormularioAgregarProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioAgregarProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioAgregarProducto.setLocation(x - 10, 10);
        objetoFormularioAgregarProducto.toFront();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        ListaFacturas objetoFormularioProducto = new ListaFacturas(1);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteActionPerformed
        FormAgregarCliente objetoFormularioAgregarCliente = new FormAgregarCliente(1);
        panelNuevo.add(objetoFormularioAgregarCliente);
        objetoFormularioAgregarCliente.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioAgregarCliente.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioAgregarCliente.setLocation(x - 10, 10);
        objetoFormularioAgregarCliente.toFront(); // Trae al frente la nueva ventana
    }//GEN-LAST:event_btnClienteActionPerformed

    private void btnArtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArtActionPerformed
        FormProducto objetoFormularioProducto = new FormProducto(1);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront();
    }//GEN-LAST:event_btnArtActionPerformed

    private void btnEditArtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditArtActionPerformed
        FormAjustes objetoFormularioProducto = new FormAjustes(1);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_btnEditArtActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        FormAjustes objetoFormularioProducto = new FormAjustes(3);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        FormAgregarCliente objetoFormularioAgregarCliente = new FormAgregarCliente(2);
        panelNuevo.add(objetoFormularioAgregarCliente);
        objetoFormularioAgregarCliente.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioAgregarCliente.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioAgregarCliente.setLocation(x - 10, 10);
        objetoFormularioAgregarCliente.toFront(); // Trae al frente la nueva ventana
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        FormDatosAdmin objetoFormularioProducto = new FormDatosAdmin();
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        FormActualizacionMasiva objetoFormularioProducto = new FormActualizacionMasiva();
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
             FormProducto objetoFormularioProducto = new FormProducto(2);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront();
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
  ListaFacturas objetoFormularioProducto = new ListaFacturas(9);
        panelNuevo.add(objetoFormularioProducto);
        objetoFormularioProducto.setVisible(true);

        Dimension dpSize = this.getSize();
        Dimension frSize = objetoFormularioProducto.getSize();
        int x = dpSize.width / 2 - (frSize.width / 2); // Añade al escritorio padre   
        int y = dpSize.height / 2 - (frSize.height / 2);
        objetoFormularioProducto.setLocation(x - 10, 10);
        objetoFormularioProducto.toFront(); // Trae al frente la nueva ventana 
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenu7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu7ActionPerformed
  
    }//GEN-LAST:event_jMenu7ActionPerformed

    private void jMenu7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu7MouseClicked
                                
                             

    int respuesta = JOptionPane.showConfirmDialog(null, 
        "¿Estás seguro que querés salir del sistema?", 
        "Confirmar Salida", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE);

    if (respuesta == JOptionPane.YES_OPTION) {
        new FormLogin().setVisible(true); // 👈 abre login
        this.dispose(); // 👈 cierra menú actual
    
}
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu7MouseClicked

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        System.out.println("probando");
        DaoProducto dp = new DaoProducto();
dp.importarConReporte((java.awt.Component) this);
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void btnEditArt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditArt1ActionPerformed
  new FormRegistro(rol, this).setVisible(true);
    this.setVisible(false);
    }//GEN-LAST:event_btnEditArt1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuPrincipal().setVisible(true);
            }
        });
    }
    /*
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AplicacionPrincipal().setVisible(true);
        });
    }
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JDialog alertas;
    public static javax.swing.JLabel bienvenida;
    public static javax.swing.JButton btnArt;
    public static javax.swing.JButton btnCliente;
    public static javax.swing.JButton btnConsultar;
    public static javax.swing.JButton btnEditArt;
    public static javax.swing.JButton btnEditArt1;
    public static javax.swing.JButton btnFacturar;
    public static javax.swing.JMenu jMenu1;
    public static javax.swing.JMenu jMenu2;
    public static javax.swing.JMenu jMenu3;
    public static javax.swing.JMenu jMenu4;
    public static javax.swing.JMenu jMenu5;
    public static javax.swing.JMenu jMenu6;
    public static javax.swing.JMenu jMenu7;
    public static javax.swing.JMenuBar jMenuBar1;
    public static javax.swing.JMenuItem jMenuItem1;
    public static javax.swing.JMenuItem jMenuItem10;
    public static javax.swing.JMenuItem jMenuItem11;
    public static javax.swing.JMenuItem jMenuItem12;
    public static javax.swing.JMenuItem jMenuItem13;
    public static javax.swing.JMenuItem jMenuItem14;
    public static javax.swing.JMenuItem jMenuItem15;
    public static javax.swing.JMenuItem jMenuItem16;
    public static javax.swing.JMenuItem jMenuItem17;
    public static javax.swing.JMenuItem jMenuItem18;
    public static javax.swing.JMenuItem jMenuItem19;
    public static javax.swing.JMenuItem jMenuItem2;
    public static javax.swing.JMenuItem jMenuItem20;
    public static javax.swing.JMenuItem jMenuItem21;
    public static javax.swing.JMenuItem jMenuItem22;
    public static javax.swing.JMenuItem jMenuItem23;
    public static javax.swing.JMenuItem jMenuItem24;
    public static javax.swing.JMenuItem jMenuItem25;
    public static javax.swing.JMenuItem jMenuItem26;
    public static javax.swing.JMenuItem jMenuItem27;
    public static javax.swing.JMenuItem jMenuItem28;
    public static javax.swing.JMenuItem jMenuItem29;
    public static javax.swing.JMenuItem jMenuItem3;
    public static javax.swing.JMenuItem jMenuItem30;
    public static javax.swing.JMenuItem jMenuItem31;
    public static javax.swing.JMenuItem jMenuItem32;
    public static javax.swing.JMenuItem jMenuItem33;
    public static javax.swing.JMenuItem jMenuItem34;
    public static javax.swing.JMenuItem jMenuItem4;
    public static javax.swing.JMenuItem jMenuItem5;
    public static javax.swing.JMenuItem jMenuItem6;
    public static javax.swing.JMenuItem jMenuItem7;
    public static javax.swing.JMenuItem jMenuItem8;
    public static javax.swing.JMenuItem jMenuItem9;
    public static javax.swing.JLabel logo;
    // End of variables declaration//GEN-END:variables
}
