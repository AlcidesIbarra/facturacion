package Formularios;

import javax.swing.*;
import java.awt.*;
import DAO.UsuarioDAO;

public class FormLogin extends JFrame {

   private JComboBox<String> cmbEmail;
    private JPasswordField txtPassword;
private JButton btnIngresar, btnRecuperar;

    public FormLogin() {
        setTitle("FACTURACIÓN");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        getContentPane().setBackground(Color.BLACK);

        // TITULO
        JLabel titulo = new JLabel("FACTURACIÓN");
        titulo.setBounds(120, 60, 290, 35);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Color.CYAN);
        add(titulo);

        // LOGO 
ImageIcon icono = new ImageIcon(
    getClass().getResource("/main/Imagenes/logo5.png")
);

JLabel logo = new JLabel(icono);
logo.setBounds(80, -40, 180, 140);   // pegado al borde izquierdo
add(logo);

        //  EFECTO BRILLO
        Timer efecto = new Timer(500, e -> {
            if (logo.getForeground().equals(Color.CYAN)) {
                logo.setForeground(Color.WHITE);
            } else {
                logo.setForeground(Color.CYAN);
            }
        });
        efecto.start();

        //  EFECTO MOVIMIENTO
  //  (agranda / achica)
Timer zoom = new Timer(80, new java.awt.event.ActionListener() {

    int ancho = 160;
    int alto = 90;
    int direccion = 1;

    public void actionPerformed(java.awt.event.ActionEvent e) {

        ancho += direccion * 2;
        alto += direccion * 1;

        if (ancho > 300 || ancho < 160) {
            direccion *= -1;
        }

        // centrado horizontal (ajusta X automáticamente)
        int x = (450 - ancho) / 2;

        logo.setBounds(x, 65, ancho, alto);

        // escalar imagen
        Image img = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(img));
    }
});
zoom.start();

        // EMAIL
        JLabel lblEmail = new JLabel("Email:");
       lblEmail.setBounds(90, 190, 100, 25);
        lblEmail.setForeground(Color.WHITE);
        add(lblEmail);

     cmbEmail = new JComboBox<>();
cmbEmail.setEditable(true); // permite escribir
cmbEmail.setBounds(155, 190, 180, 25);
add(cmbEmail);

        // PASSWORD
        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(90, 230, 100, 25);
        lblPass.setForeground(Color.WHITE);
        add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(155, 230, 180, 25);
        add(txtPassword);

        // BOTONES
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(70, 270, 280, 35);
        estilo(btnIngresar);
        add(btnIngresar);

     

        btnRecuperar = new JButton("Recuperar");
btnRecuperar.setBounds(70, 320, 280, 35);
estilo(btnRecuperar);
        add(btnRecuperar);

        // EVENTOS
        btnIngresar.addActionListener(e -> login());

   

        btnRecuperar.addActionListener(e -> {
            new FormRecuperar().setVisible(true);
            this.dispose();
        });
        
        cargarEmails();
      cmbEmail.setSelectedIndex(-1);
  
    }

    private void estilo(JButton b) {
        b.setBackground(new Color(0, 255, 255));
        b.setForeground(Color.BLACK);
    }

    private void login() {
        UsuarioDAO dao = new UsuarioDAO();

String email = cmbEmail.getSelectedItem().toString();
String pass = txtPassword.getText();

        if (dao.login(email, pass)) {
              guardarEmail(email);
            JOptionPane.showMessageDialog(this, "Bienvenido");

        String rol = dao.obtenerRol(email); // tenés que tener este método

new MenuPrincipal(rol).setVisible(true);
            this.dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Datos incorrectos");
        }
    }

  private void guardarEmail(String email) {
    try {
        java.io.File file = new java.io.File("emails.txt");

        java.util.List<String> lista = new java.util.ArrayList<>();

        if (file.exists()) {
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file));
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(linea);
            }
            br.close();
        }

        if (!lista.contains(email)) {
            java.io.FileWriter fw = new java.io.FileWriter(file, true);
            fw.write(email + "\n");
            fw.close();
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
  
  private void cargarEmails() {
    try {
        java.io.File file = new java.io.File("emails.txt");

        if (file.exists()) {
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file));
            String linea;
            while ((linea = br.readLine()) != null) {
                cmbEmail.addItem(linea);
            }
            br.close();
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}