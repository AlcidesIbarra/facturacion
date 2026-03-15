/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;
import Controlador.ControladorCliente;
import Modelos.Mensaje;
import Modelos.ModeloCliente;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 *
 * @author ALCIDES
 */
public class FormAgregarCliente extends javax.swing.JInternalFrame {

    /**
     * Creates new form formAgregarProducto
     */
    public FormAgregarCliente(Integer verificador1) {
        initComponents();
        verificador.setVisible(false);
        ControladorAdministracion ca = new ControladorAdministracion();
        System.out.println(verificador1+"LO QUE VIENE");
        ca.cambiacolor(this);
        ca.BotonesCrisitalPrim(btnGuardarCliente, Color.green, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton2, Color.RED, Color.black, Color.black);
        buscaCliente.setVisible(false);
        contenedorClientes.setVisible(false);
        ControladorCliente cc = new ControladorCliente();
        cc.verCodigoDisponlible(codigoCliente);
        jLabel11.setHorizontalAlignment(SwingConstants.CENTER);
        
        if (verificador1 == 2 || verificador1==4) {
            System.out.println("entraverificador");
            buscaCliente.setVisible(true);
            contenedorClientes.setVisible(false);
            codigoCliente.setVisible(false);
         
            jLabel11.setText("EDITAR CLIENTE");
        }
           verificador.setText(verificador1.toString());
           
           
                   Dimension d = new Dimension(80, 26); // El tamaño que quieras
 jButton2.setPreferredSize(d);
 jButton2.setMinimumSize(d);
 jButton2.setMaximumSize(d);
 
btnGuardarCliente.setPreferredSize(d);
btnGuardarCliente.setMinimumSize(d);
btnGuardarCliente.setMaximumSize(d);
    }
    
    JTable tab = new JTable();
    JLabel lblP;
    
    public void editarCliente(Integer codigo, JTable tabla,JLabel lbl) {
        tab = tabla;
        lblP=lbl;
       // verificador.setText("2");
        ControladorCliente cc = new ControladorCliente();
        ModeloCliente cliente = cc.obtenerCliente(codigo);
        if (cliente != null) {
            buscaCliente.setText(cliente.getCodigo().toString());
            codigoCliente.setText(cliente.getCodigo().toString());
            nombreCliente.setText(cliente.getNombre());
            direccionCliente.setText(cliente.getDireccion());
            DNIcliente.setText(cliente.getCuitDni().toString());
            int indice = 0;
            if (cliente.getMetodoPago() != null) {
                
                switch (cliente.getMetodoPago()) {
                    case "Contado":
                        indice = 0;
                        break;
                    case "Cuenta Corriente":
                        indice = 1;
                        break;
                    case "Tarjeta":
                        indice = 2;
                        break;
                    default:
                        indice = 0;
                        throw new AssertionError();
                }
            }
            metodoPago.setSelectedIndex(indice);
            telefonoCliente.setText(cliente.getTelefono().toString());
            LimiteSaldo.setText(cliente.getLimiteCompra().toString());
            saldoCliente.setText(cliente.getSaldoAcumulado().toString());
            observaciones.setText(cliente.getObservaciones());
            diaspagar.setText(cliente.getDiasPago().toString());
            contenedorClientes.setVisible(false);
            buscaCliente.transferFocus();
        } else {
            limpiarCampos();
        }
    }
    
     private Timer alertaTimer = null;
    private AWTEventListener tecladoGlobalListener = null;
  
public void mostrarAlerta(String palabra, Integer codigo) {
    final JPanel glass = (JPanel) this.getGlassPane();
    
    // 1. Limpieza de procesos anteriores
    if (alertaTimer != null && alertaTimer.isRunning()) alertaTimer.stop();
    if (tecladoGlobalListener != null) {
        Toolkit.getDefaultToolkit().removeAWTEventListener(tecladoGlobalListener);
    }
    
    glass.removeAll();
    glass.setLayout(new GridBagLayout()); // Usamos GridBag para posicionar arriba

    // 2. Configuración de Posición (Arriba)
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.NORTH; // Lo pega al techo
    gbc.insets = new Insets(20, 0, 0, 0);  // Margen de 20px desde arriba
    gbc.weighty = 1.0; // Empuja el resto del espacio hacia abajo

    // 3. Crear el Popup
    JPanel popup = new JPanel();
    popup.setLayout(new BoxLayout(popup, BoxLayout.Y_AXIS));
    // Borde redondeado original de tu código
    popup.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 8, true));

    // 4. Cargar Icono (Ruta relativa corregida)
    // Cambiá "advertencia.png" por el nombre exacto de tu archivo en src
    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/main/Imagenes/advertencia.png"));
    Image imgEscalada = iconoOriginal.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
    ImageIcon icono = new ImageIcon(imgEscalada);

    String txtTitulo = "";
    Color colorFondo;
    switch (codigo) {
        case 1: txtTitulo = "EXITO:"; colorFondo = new Color(154, 205, 50, 220); break;
        case 2: txtTitulo = "ATENCION:"; colorFondo = new Color(255, 120, 0, 220); break;
        case 3: txtTitulo = "ADVERTENCIA:"; colorFondo = new Color(255, 0, 0, 220); break;
        case 4: txtTitulo = "ERROR:"; colorFondo = new Color(150, 150, 150, 220); break;
        default: txtTitulo = "AVISO:"; colorFondo = new Color(100, 100, 100, 220);
    }
    popup.setBackground(colorFondo);

    // 5. Etiquetas con tu fuente original "consolass"
    JLabel titulo = new JLabel(txtTitulo, icono, JLabel.LEFT);
    titulo.setFont(new Font("consolass", Font.BOLD, 22));
    titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel contenido = new JLabel("<html><center>"+palabra+"</center></html>");
    contenido.setFont(new Font("consolass", Font.BOLD, 16));
    popup.setMinimumSize(new Dimension(320,80));
    contenido.setAlignmentX(Component.CENTER_ALIGNMENT);

    popup.add(titulo);
    popup.add(contenido);

    glass.add(popup, gbc); // Agregamos con la configuración de posición
    glass.setVisible(true);
    glass.revalidate();
    glass.repaint();

    // 6. Lógica de Cierre (Click, Tecla o Tiempo)
    Runnable cerrarAccion = () -> {
        glass.setVisible(false);
        glass.removeAll();
        if (alertaTimer != null) alertaTimer.stop();
        if (tecladoGlobalListener != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(tecladoGlobalListener);
        }
    };

    glass.addMouseListener(new MouseAdapter() {
        @Override public void mousePressed(MouseEvent e) { cerrarAccion.run(); }
    });

    tecladoGlobalListener = event -> {
        if (event instanceof KeyEvent && event.getID() == KeyEvent.KEY_PRESSED) {
            cerrarAccion.run();
        }
    };
    Toolkit.getDefaultToolkit().addAWTEventListener(tecladoGlobalListener, AWTEvent.KEY_EVENT_MASK);

    alertaTimer = new Timer(5000, e -> cerrarAccion.run());
    alertaTimer.setRepeats(false);
    alertaTimer.start();
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPasswordField1 = new javax.swing.JPasswordField();
        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        contenedorClientes = new javax.swing.JScrollPane();
        listaClientes = new javax.swing.JTable();
        buscaCliente = new javax.swing.JTextField();
        codigoCliente = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nombreCliente = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        direccionCliente = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        DNIcliente = new javax.swing.JTextField();
        metodoPago = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        telefonoCliente = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        LimiteSaldo = new javax.swing.JTextField();
        saldoCliente = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        diaspagar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        observaciones = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        btnGuardarCliente = new javax.swing.JButton();
        verificador = new javax.swing.JLabel();

        jPasswordField1.setText("jPasswordField1");

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel11.setText("AGREGAR CLIENTE");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 0, 270, -1));

        contenedorClientes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                contenedorClientesFocusLost(evt);
            }
        });
        contenedorClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                contenedorClientesKeyPressed(evt);
            }
        });

        listaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        listaClientes.setAutoscrolls(false);
        listaClientes.setRowMargin(0);
        listaClientes.setShowHorizontalLines(false);
        listaClientes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                listaClientesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                listaClientesFocusLost(evt);
            }
        });
        listaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaClientesMouseClicked(evt);
            }
        });
        listaClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                listaClientesKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                listaClientesKeyReleased(evt);
            }
        });
        contenedorClientes.setViewportView(listaClientes);

        jPanel1.add(contenedorClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 170, 20));

        buscaCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscaClienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                buscaClienteFocusLost(evt);
            }
        });
        buscaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buscaClienteMouseClicked(evt);
            }
        });
        buscaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscaClienteActionPerformed(evt);
            }
        });
        buscaCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                buscaClienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscaClienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                buscaClienteKeyTyped(evt);
            }
        });
        jPanel1.add(buscaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 170, -1));

        codigoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigoClienteActionPerformed(evt);
            }
        });
        codigoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codigoClienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codigoClienteKeyReleased(evt);
            }
        });
        jPanel1.add(codigoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 43, -1));

        jLabel1.setText("codigo:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel2.setText("Nombre:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        nombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nombreClienteKeyPressed(evt);
            }
        });
        jPanel1.add(nombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 170, -1));

        jLabel3.setText("Direccion:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        direccionCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                direccionClienteKeyPressed(evt);
            }
        });
        jPanel1.add(direccionCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 170, -1));

        jLabel5.setText("CUIT / DNI:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        DNIcliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DNIclienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNIclienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNIclienteKeyTyped(evt);
            }
        });
        jPanel1.add(DNIcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 140, -1));

        metodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Contado", "Cuenta Corriente", "Tarjeta" }));
        metodoPago.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                metodoPagoMouseClicked(evt);
            }
        });
        metodoPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metodoPagoActionPerformed(evt);
            }
        });
        metodoPago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                metodoPagoKeyPressed(evt);
            }
        });
        jPanel1.add(metodoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 150, 140, -1));

        jLabel8.setText("Metodo Pago:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));

        telefonoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                telefonoClienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                telefonoClienteKeyReleased(evt);
            }
        });
        jPanel1.add(telefonoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, 117, -1));

        jLabel4.setText("Telefono:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        jLabel6.setText("Limite Saldo:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        LimiteSaldo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LimiteSaldoActionPerformed(evt);
            }
        });
        LimiteSaldo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LimiteSaldoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                LimiteSaldoKeyReleased(evt);
            }
        });
        jPanel1.add(LimiteSaldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 91, -1));

        saldoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saldoClienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saldoClienteKeyReleased(evt);
            }
        });
        jPanel1.add(saldoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 240, 91, -1));

        jLabel7.setText("Saldo Acum:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        jLabel10.setText("Dias a Pagar:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        diaspagar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                diaspagarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                diaspagarKeyReleased(evt);
            }
        });
        jPanel1.add(diaspagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 270, 89, -1));

        jLabel9.setText("Observaciones:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, -1, -1));

        observaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                observacionesKeyPressed(evt);
            }
        });
        jPanel1.add(observaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 300, 170, -1));

        jButton2.setText("Limpiar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, -1, -1));

        btnGuardarCliente.setText("Guardar");
        btnGuardarCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGuardarClienteMouseClicked(evt);
            }
        });
        btnGuardarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarClienteActionPerformed(evt);
            }
        });
        btnGuardarCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGuardarClienteKeyPressed(evt);
            }
        });
        jPanel1.add(btnGuardarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 330, -1, -1));

        verificador.setText("1");
        jPanel1.add(verificador, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 19, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 320, 370));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void codigoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoClienteActionPerformed

    private void LimiteSaldoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LimiteSaldoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LimiteSaldoActionPerformed

    private void btnGuardarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarClienteActionPerformed
        Mensaje mj = null;
        Controlador.ControladorCliente objetoCliente = new Controlador.ControladorCliente();
        Double limiteSaldoPrim = null;
        Double SaldoClientePrim = null;
        Integer codigoClientePrim = null;
        try {
            limiteSaldoPrim = Double.parseDouble(LimiteSaldo.getText());
        } catch (Exception e) {
        }
        try {
            SaldoClientePrim = Double.parseDouble(saldoCliente.getText());
        } catch (Exception e) {
        }
        try {
            codigoClientePrim = Integer.parseInt(codigoCliente.getText());
        } catch (Exception e) {
            codigoClientePrim = 1;
        }
        int verifAux=Integer.parseInt(verificador.getText());
        if(verifAux==4){
            verifAux=2;
        }
           if(verifAux==3){
            verifAux=1;
        }
        
        mj = objetoCliente.agregarCliente("RENDER", nombreCliente, direccionCliente,
                telefonoCliente, DNIcliente, limiteSaldoPrim,
                SaldoClientePrim, metodoPago, observaciones.getText(), verifAux, tab,
                diaspagar, codigoClientePrim, codigoCliente,lblP);
        if (mj.getCodigo() == 1) {
            limpiarCampos();
        }
        tab = null;

       if(Integer.parseInt(verificador.getText())==4||Integer.parseInt(verificador.getText())==3){
        try {
            this.setClosed(true);

        } catch (PropertyVetoException ex) {
            Logger.getLogger(FormFacturar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       }
        System.out.println(mj + " mensaje");
        if (mj != null) {
            System.out.println("el mensaje no es nullo");
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
                ControladorCliente cc = new ControladorCliente();
        cc.verCodigoDisponlible(codigoCliente);
    }//GEN-LAST:event_btnGuardarClienteActionPerformed
   
    public void limpiarCampos() {
        buscaCliente.setText("");
        nombreCliente.setText("");
        direccionCliente.setText("");
        DNIcliente.setText("");
        metodoPago.setSelectedItem("Contado");
        telefonoCliente.setText("");
        saldoCliente.setText("");
        LimiteSaldo.setText("");
        diaspagar.setText("");
        observaciones.setText("");
        
    }
    private void codigoClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            nombreCliente.requestFocus();
        }      // TODO add your handling code here:
    }//GEN-LAST:event_codigoClienteKeyPressed

    private void nombreClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            direccionCliente.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_nombreClienteKeyPressed

    private void direccionClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_direccionClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            DNIcliente.requestFocus();
        }
    }//GEN-LAST:event_direccionClienteKeyPressed

    private void DNIclienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DNIclienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            metodoPago.requestFocus();
            metodoPago.setSelectedIndex(0);
            metodoPago.setPopupVisible(true);
            
        }
    }//GEN-LAST:event_DNIclienteKeyPressed
    
    private void verificarIngreso(JTextField texto, char c) {
        
        String cadena = texto.getText();
        
        if (Character.isDigit(c) || c == '.') {
            cadena = cadena.substring(0, cadena.length() - 1);
            
            if (c == '.' && cadena.contains(".")) {
                
                texto.setText(cadena);
            }
        } else {
            mostrarAlerta("solo se aceptan numeros para este campo", 2);
            
            cadena = texto.getText();
            
            if (cadena.length() > 1) {
                cadena = cadena.substring(0, cadena.length() - 1);
            } else {
                cadena = "";
            }
            texto.setText(cadena);
            //  texto.requestFocus();
            texto.setCaretPosition(texto.getText().length());
            texto.requestFocusInWindow();
            
        }
        
    }

    private void LimiteSaldoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LimiteSaldoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            saldoCliente.requestFocus();
        }

    }//GEN-LAST:event_LimiteSaldoKeyPressed

    private void saldoClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saldoClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            diaspagar.requestFocus();
        }
    }//GEN-LAST:event_saldoClienteKeyPressed

    private void btnGuardarClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGuardarClienteKeyPressed
        Mensaje mj = null;
        Controlador.ControladorCliente objetoCliente = new Controlador.ControladorCliente();
        Double limiteSaldoPrim = null;
        Double SaldoClientePrim = null;
        Integer codigoClientePrim = null;
        try {
            limiteSaldoPrim = Double.parseDouble(LimiteSaldo.getText());
        } catch (Exception e) {
        }
        try {
            SaldoClientePrim = Double.parseDouble(saldoCliente.getText());
        } catch (Exception e) {
        }
        try {
            codigoClientePrim = Integer.parseInt(codigoCliente.getText());
        } catch (Exception e) {
            codigoClientePrim = 1;
        }
        
        mj = objetoCliente.agregarCliente("RENDER", nombreCliente, direccionCliente,
                telefonoCliente, DNIcliente, limiteSaldoPrim,
                SaldoClientePrim, metodoPago, observaciones.getText(),
                Integer.parseInt(verificador.getText()), tab,
                diaspagar, codigoClientePrim, codigoCliente,null);
        objetoCliente.verCodigoDisponlible(DNIcliente);
        
        tab = null;

        /*
        try {
            this.setClosed(true);

        } catch (PropertyVetoException ex) {
            Logger.getLogger(FormFacturar.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
        System.out.println(mj + " mensaje");
        if (mj != null) {
            System.out.println("el mensaje no es nullo");
            mostrarAlerta(mj.getMensaje(), mj.getCodigo());
        }
        
    }//GEN-LAST:event_btnGuardarClienteKeyPressed

    private void telefonoClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telefonoClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            LimiteSaldo.requestFocus();
            
        }
    }//GEN-LAST:event_telefonoClienteKeyPressed

    private void btnGuardarClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarClienteMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarClienteMouseClicked

    private void metodoPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metodoPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_metodoPagoActionPerformed

    private void metodoPagoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_metodoPagoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            telefonoCliente.requestFocus();
        }           // TODO add your handling code here:
    }//GEN-LAST:event_metodoPagoKeyPressed

    private void metodoPagoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_metodoPagoMouseClicked
//telefonoCliente.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_metodoPagoMouseClicked

    private void observacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_observacionesKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnGuardarCliente.requestFocus();
        }            // TODO add your handling code here:
    }//GEN-LAST:event_observacionesKeyPressed

    private void diaspagarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_diaspagarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            observaciones.requestFocus();
        }
    }//GEN-LAST:event_diaspagarKeyPressed
    boolean fucuseado = false;
    private void buscaClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscaClienteFocusGained
        fucuseado = true;
        buscaCliente.selectAll();
        System.out.println("gano foco");
    }//GEN-LAST:event_buscaClienteFocusGained

    private void buscaClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscaClienteFocusLost
        System.out.println("abreslide" + abreSlide);        
        
        if (abreSlide == true) {            
            if (!listaClientes.isColumnSelected(0)) {
                
                Integer cod = null;
                try {
                    cod = Integer.parseInt(buscaCliente.getText());
                } catch (Exception e) {
                }                
                editarCliente(cod, null,lblP);                
                contenedorClientes.setVisible(false);
                
            }
        }
        

    }//GEN-LAST:event_buscaClienteFocusLost

    private void buscaClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buscaClienteMouseClicked
        buscaCliente.setFocusable(true);
        boolean ejecutarFocusLostCliente = true;
        System.out.println("     sse activo " + ejecutarFocusLostCliente);
    }//GEN-LAST:event_buscaClienteMouseClicked

    private void buscaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscaClienteActionPerformed

    }//GEN-LAST:event_buscaClienteActionPerformed

    private void buscaClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyPressed

    }//GEN-LAST:event_buscaClienteKeyPressed
    
    boolean abreSlide = false;
    private void buscaClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyReleased
        abreSlide = true;
        Controlador.ControladorCliente objetoCliente = new ControladorCliente();
        boolean returno = objetoCliente.buscarClientesinCF(buscaCliente, listaClientes, contenedorClientes,"ACTIVOS");
        
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
            fucuseado = false;
            
            if (returno == false && !buscaCliente.getText().equals("")) {
                contenedorClientes.setVisible(false);
                
                mostrarAlerta("No se encontraron coincidencias.", 2);
                buscaCliente.requestFocus();
                buscaCliente.selectAll();
                
                String codigo = buscaCliente.getText();
                
                buscaCliente.setText(codigo);
            }
            // JOptionPane.showMessageDialog(null, "enmterdetectado");
            if (buscaCliente.getText().equals("")) {
                
            } else {
                listaClientes.changeSelection(0, 0, false, false);
                listaClientes.requestFocus();
            }
        }
        if (buscaCliente.getText().equals("")) {
            // Controlador.ControladorCliente cc = new ControladorCliente();
            contenedorClientes.setVisible(false);
        }

    }//GEN-LAST:event_buscaClienteKeyReleased

    private void buscaClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaClienteKeyTyped

    }//GEN-LAST:event_buscaClienteKeyTyped

    private void listaClientesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listaClientesFocusGained
        abreSlide = false;
    }//GEN-LAST:event_listaClientesFocusGained

    private void listaClientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_listaClientesFocusLost

        /*
        if (fucuseado == false) {
            // listaClientes.setVisible(false);
            contenedorClientes.setVisible(false);
            ControladorCliente cc = new ControladorCliente();
            Mensaje mj = cc.SeleccionarCliente(listaClientes, fieldNombre, fieldDni, fieldDireccion,
                fieldTelefono, buscaCliente, contenedorClientes,
                limiteCompra, deuda, txtBuscarProducto, metodoPago, paneObservaciones, dias,
                prCosto, texfieldStock, prVenta, saldoDisponible, 1,
                tbResumenVenta, total, labelCodCliente, codigoProducto,
                codigoProducto, contenedorTabla, descripcionProd, editaFacturar);

            if (mj != null) {
                mostrarAlerta(mj.getMensaje(), mj.getCodigo());
            }

        }
        fucuseado = false;
         */
    }//GEN-LAST:event_listaClientesFocusLost

    private void listaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaClientesMouseClicked
        
        abreSlide = false;
        
        int fila = listaClientes.getSelectedRow();
        editarCliente(Integer.parseInt(listaClientes.getValueAt(fila, 1).toString()), null,lblP);
        

    }//GEN-LAST:event_listaClientesMouseClicked
    boolean cerrarSlide = false;
    private void listaClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaClientesKeyPressed
        
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int fila = listaClientes.getSelectedRow();
            editarCliente(Integer.parseInt(listaClientes.getValueAt(fila, 1).toString()), null,lblP);
            cerrarSlide = true;
        } else {
            ///   txtBuscarProducto.requestFocus();
        }
    }//GEN-LAST:event_listaClientesKeyPressed

    private void listaClientesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listaClientesKeyReleased

    }//GEN-LAST:event_listaClientesKeyReleased

    private void contenedorClientesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contenedorClientesFocusLost

    }//GEN-LAST:event_contenedorClientesFocusLost

    private void contenedorClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contenedorClientesKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_contenedorClientesKeyPressed

    private void codigoClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoClienteKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {
            
            verificarIngreso(codigoCliente, evt.getKeyChar());
            
        }
    }//GEN-LAST:event_codigoClienteKeyReleased

    private void DNIclienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DNIclienteKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {
            
            verificarIngreso(DNIcliente, evt.getKeyChar());
            
        }
    }//GEN-LAST:event_DNIclienteKeyReleased

    private void telefonoClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telefonoClienteKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {
            
            verificarIngreso(telefonoCliente, evt.getKeyChar());
            
        }
    }//GEN-LAST:event_telefonoClienteKeyReleased

    private void DNIclienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DNIclienteKeyTyped

    }//GEN-LAST:event_DNIclienteKeyTyped

    private void LimiteSaldoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LimiteSaldoKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {
            
            verificarIngreso(LimiteSaldo, evt.getKeyChar());
            
        }         // TODO add your handling code here:
    }//GEN-LAST:event_LimiteSaldoKeyReleased

    private void saldoClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saldoClienteKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {
            
            verificarIngreso(saldoCliente, evt.getKeyChar());
            
        }
    }//GEN-LAST:event_saldoClienteKeyReleased

    private void diaspagarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_diaspagarKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE) {
            
            verificarIngreso(diaspagar, evt.getKeyChar());
            
        }
    }//GEN-LAST:event_diaspagarKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DNIcliente;
    private javax.swing.JTextField LimiteSaldo;
    private javax.swing.JButton btnGuardarCliente;
    private javax.swing.JTextField buscaCliente;
    private javax.swing.JTextField codigoCliente;
    private javax.swing.JScrollPane contenedorClientes;
    private javax.swing.JTextField diaspagar;
    private javax.swing.JTextField direccionCliente;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTable listaClientes;
    private javax.swing.JComboBox<String> metodoPago;
    private javax.swing.JTextField nombreCliente;
    private javax.swing.JTextField observaciones;
    private javax.swing.JTextField saldoCliente;
    private javax.swing.JTextField telefonoCliente;
    private javax.swing.JLabel verificador;
    // End of variables declaration//GEN-END:variables
}
