package Formularios;
import util.EmailService;
import DAO.UsuarioDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class FormRecuperar extends JFrame {

    private JTextField txtEmail, txtCodigo;
    private JPasswordField txtNuevaPass;
    private JButton btnEnviarCodigo, btnRecuperar, btnVolver;

    private String codigoGenerado = "";

    public FormRecuperar() {
        setTitle("Recuperar Contraseña - FACTURACION");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        getContentPane().setBackground(Color.BLACK);

        JLabel titulo = new JLabel("RECUPERAR CLAVE");
        titulo.setBounds(80, 20, 250, 30);
        titulo.setForeground(Color.CYAN);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 80, 100, 25);
        lblEmail.setForeground(Color.WHITE);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(150, 80, 180, 25);
        add(txtEmail);

        btnEnviarCodigo = new JButton("Enviar Código");
        btnEnviarCodigo.setBounds(100, 120, 180, 30);
        estiloBoton(btnEnviarCodigo);
        add(btnEnviarCodigo);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(50, 170, 100, 25);
        lblCodigo.setForeground(Color.WHITE);
        add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(150, 170, 180, 25);
        add(txtCodigo);

        JLabel lblNueva = new JLabel("Nueva Pass:");
        lblNueva.setBounds(50, 210, 100, 25);
        lblNueva.setForeground(Color.WHITE);
        add(lblNueva);

        txtNuevaPass = new JPasswordField();
        txtNuevaPass.setBounds(150, 210, 180, 25);
        add(txtNuevaPass);

        btnRecuperar = new JButton("Recuperar");
        btnRecuperar.setBounds(50, 280, 130, 35);
        estiloBoton(btnRecuperar);
        add(btnRecuperar);

        btnVolver = new JButton("Volver");
        btnVolver.setBounds(200, 280, 130, 35);
        estiloBoton(btnVolver);
        add(btnVolver);

        btnEnviarCodigo.addActionListener(e -> enviarCodigo());
        btnRecuperar.addActionListener(e -> recuperarPassword());
        btnVolver.addActionListener(e -> volverLogin());
    }

    private void estiloBoton(JButton boton) {
        boton.setBackground(new Color(0, 255, 255));
        boton.setForeground(Color.BLACK);
        boton.setFocusPainted(false);
    }

    // 🎲 GENERAR Y ENVIAR CÓDIGO
    private void enviarCodigo() {

        String email = txtEmail.getText();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un email");
            return;
        }

        // Generar código
        codigoGenerado = String.valueOf((int)(Math.random() * 999999));

        UsuarioDAO dao = new UsuarioDAO();
        dao.guardarCodigo(email, codigoGenerado);

        // ✅ ENVÍO REAL DE EMAIL
        EmailService emailService = new EmailService();
        emailService.enviarCodigo(email, codigoGenerado);

        JOptionPane.showMessageDialog(this, "Código enviado al email");
    }

    private void recuperarPassword() {

        String email = txtEmail.getText();
        String codigoIngresado = txtCodigo.getText();
        String nuevaPass = txtNuevaPass.getText();

        if (email.isEmpty() || codigoIngresado.isEmpty() || nuevaPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completar todos los campos");
            return;
        }

        if (!codigoIngresado.equals(codigoGenerado)) {
            JOptionPane.showMessageDialog(this, "Código incorrecto");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        dao.cambiarPassword(email, nuevaPass);

        JOptionPane.showMessageDialog(this, "Contraseña actualizada");

        new FormLogin().setVisible(true);
        this.dispose();
    }

    private void volverLogin() {
        new FormLogin().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        new FormRecuperar().setVisible(true);
    }
}