package Formularios;
import DAO.UsuarioDAO;
import Formularios.FormLogin;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import Modelos.Usuario;
import javax.swing.JFrame;



public class FormRegistro extends JFrame {

    private JTextField txtEmail, txtTelefono;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRol;
    private JButton btnDniFrente, btnDniDorso, btnRegistrar, btnVolver;
private JButton btnUsuarios;
    private byte[] dniFrente;
    private byte[] dniDorso;
    private String rol;
    private MenuPrincipal menu;

public FormRegistro(String rol, MenuPrincipal menu) {
    this.rol = rol;
    this.menu = menu;
        setTitle("Registro de Usuario - FACTURACION");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
   this.setResizable(false);
        getContentPane().setBackground(Color.BLACK);

        // Titulo
        JLabel titulo = new JLabel("REGISTRO");
        titulo.setBounds(130, 20, 200, 30);
        titulo.setForeground(Color.CYAN);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(titulo);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 80, 100, 25);
        lblEmail.setForeground(Color.WHITE);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(150, 80, 180, 25);
        add(txtEmail);

        // Password
        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(50, 120, 100, 25);
        lblPass.setForeground(Color.WHITE);
        add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 120, 180, 25);
        add(txtPassword);

        // Teléfono
        JLabel lblTel = new JLabel("Teléfono:");
        lblTel.setBounds(50, 160, 100, 25);
        lblTel.setForeground(Color.WHITE);
        add(lblTel);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(150, 160, 180, 25);
        add(txtTelefono);

        // Rol
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setBounds(50, 200, 100, 25);
        lblRol.setForeground(Color.WHITE);
        add(lblRol);

        comboRol = new JComboBox<>(new String[]{"Empleado", "Administrador"});
        comboRol.setBounds(150, 200, 180, 25);
        add(comboRol);

        // Botón DNI Frente
        btnDniFrente = new JButton("DNI Frente");
        btnDniFrente.setBounds(50, 250, 130, 30);
        estiloBoton(btnDniFrente);
        add(btnDniFrente);

        // Botón DNI Dorso
        btnDniDorso = new JButton("DNI Dorso");
        btnDniDorso.setBounds(200, 250, 130, 30);
        estiloBoton(btnDniDorso);
        add(btnDniDorso);

        // Botón Registrar
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(50, 320, 130, 35);
        estiloBoton(btnRegistrar);
        add(btnRegistrar);
        
        btnUsuarios = new JButton("Usuarios");
btnUsuarios.setBounds(120, 380, 150, 35);
estiloBoton(btnUsuarios);
add(btnUsuarios);

        // Botón Volver
        btnVolver = new JButton("Volver");
        btnVolver.setBounds(200, 320, 130, 35);
        estiloBoton(btnVolver);
        add(btnVolver);

        // EVENTOS

        btnDniFrente.addActionListener(e -> cargarImagenFrente());
        btnDniDorso.addActionListener(e -> cargarImagenDorso());
        btnRegistrar.addActionListener(e -> registrarUsuario());
      btnVolver.addActionListener(e -> volverMenu());
       btnUsuarios.addActionListener(e -> {
    new FormUsuarios().setVisible(true);
    this.dispose();
});
        
//obligo a que se admin el primero
        UsuarioDAO dao = new UsuarioDAO();

if (!dao.hayUsuarios()) {
    comboRol.setSelectedItem("Administrador");
    comboRol.setEnabled(false);
}
    }
private void volverMenu() {
    if (menu != null) {
        menu.setVisible(true);
    } else {
        new MenuPrincipal(rol).setVisible(true);
    }
    this.dispose();
}

    // Estilo futurista simple
    private void estiloBoton(JButton boton) {
        boton.setBackground(new Color(0, 255, 255));
        boton.setForeground(Color.BLACK);
        boton.setFocusPainted(false);
    }

    //  Cargar DNI Frente
    private void cargarImagenFrente() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(this);
            File archivo = fc.getSelectedFile();

            if (archivo != null) {
                FileInputStream fis = new FileInputStream(archivo);
                dniFrente = fis.readAllBytes();
                JOptionPane.showMessageDialog(this, "DNI Frente cargado");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar imagen");
        }
    }

    //  Cargar DNI Dorso
    private void cargarImagenDorso() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(this);
            File archivo = fc.getSelectedFile();

            if (archivo != null) {
                FileInputStream fis = new FileInputStream(archivo);
                dniDorso = fis.readAllBytes();
                JOptionPane.showMessageDialog(this, "DNI Dorso cargado");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar imagen");
        }
    }

    //  Registrar usuario
    private void registrarUsuario() {

        if (txtEmail.getText().isEmpty() ||
            txtPassword.getText().isEmpty() ||
            txtTelefono.getText().isEmpty() ||
            dniFrente == null ||
            dniDorso == null) {

            JOptionPane.showMessageDialog(this, "Completar todos los campos");
            return;
        }

        Usuario u = new Usuario();
        u.setEmail(txtEmail.getText());
        u.setPassword(txtPassword.getText());
        u.setTelefono(txtTelefono.getText());
        u.setRol(comboRol.getSelectedItem().toString());
        u.setDniFrente(dniFrente);
        u.setDniDorso(dniDorso);

        UsuarioDAO dao = new UsuarioDAO();

if (dao.registrar(u)) {
    JOptionPane.showMessageDialog(this, "Usuario registrado correctamente");

    if (menu != null) {
        menu.setVisible(true);
    } else {
        // 👇 fallback inteligente
        new MenuPrincipal(rol).setVisible(true);
    }

    this.dispose();
}else {
            JOptionPane.showMessageDialog(this, "Error al registrar");
        }

    }

    //  Volver al login
    private void volverLogin() {
        new FormLogin().setVisible(true);
        this.dispose();
    }

public static void main(String[] args) {

    UsuarioDAO dao = new UsuarioDAO();

    if (!dao.hayUsuarios()) {
        JOptionPane.showMessageDialog(null, "Debe crear un usuario ADMINISTRADOR");
new FormRegistro("Administrador", null).setVisible(true);    } else {
        new FormLogin().setVisible(true);
    }
}
}