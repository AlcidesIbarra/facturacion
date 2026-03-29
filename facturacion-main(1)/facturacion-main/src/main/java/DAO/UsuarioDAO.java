package DAO;

import configuracion.Conexion;
import Modelos.Usuario;

import java.sql.*;
import java.security.MessageDigest;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.ArrayList;

public class UsuarioDAO {

    // 🔐 MÉTODO PARA ENCRIPTAR
    private String encriptar(String pass) {
        try {
            if (pass == null) return "";
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (Exception e) {
            return pass;
        }
    }

    // 🧾 REGISTRAR USUARIO
    public boolean registrar(Usuario u) {

        // ✅ VALIDACIONES (esto evita los errores)
        if (u == null) {
            System.out.println("Usuario nulo");
            return false;
        }

        if (u.getEmail() == null || u.getEmail().trim().isEmpty()) {
            System.out.println("Email vacío");
            return false;
        }

        String sql = "INSERT INTO usuario(email,password,telefono,rol,dni_frente,dni_dorso) VALUES(?,?,?,?,?,?)";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, u.getEmail());

            // 🔐 ENCRIPTAMOS AQUÍ
            ps.setString(2, encriptar(u.getPassword()));

            ps.setString(3, u.getTelefono() != null ? u.getTelefono() : "");
            ps.setString(4, u.getRol() != null ? u.getRol() : "");

            // ✅ EVITA ERROR DE NULL EN BLOB
            ps.setBytes(5, u.getDniFrente() != null ? u.getDniFrente() : new byte[0]);
            ps.setBytes(6, u.getDniDorso() != null ? u.getDniDorso() : new byte[0]);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error registrar: " + e.getMessage());
            return false;
        }
    }

    // 🔐 LOGIN
    public boolean login(String email, String pass) {

        String sql = "SELECT * FROM usuario WHERE email=? AND password=?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, encriptar(pass));

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            return false;
        }
    }

    // 🔁 GUARDAR CÓDIGO DE RECUPERACIÓN
    public void guardarCodigo(String email, String codigo) {

        String sql = "UPDATE usuario SET codigo_recuperacion=? WHERE email=?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setString(2, email);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔄 CAMBIAR CONTRASEÑA
    public void cambiarPassword(String email, String pass) {

        String sql = "UPDATE usuario SET password=? WHERE email=?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, encriptar(pass));
            ps.setString(2, email);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   public boolean hayUsuarios() {
    boolean hay = false;

    Connection con = null;

    try {
        con = Conexion.getConexion();  // 👈 VA ACÁ

        String sql = "SELECT COUNT(*) FROM usuario";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            hay = rs.getInt(1) > 0;
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.toString());
    }

    return hay;
}
   
   
public List<Usuario> listarUsuarios() {
    List<Usuario> lista = new ArrayList<>();

    String sql = "SELECT * FROM usuario";

    try (Connection cn = Conexion.getConexion();
         PreparedStatement ps = cn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Usuario u = new Usuario();

            u.setId(rs.getInt("id"));
            u.setEmail(rs.getString("email"));
            u.setTelefono(rs.getString("telefono"));
            u.setRol(rs.getString("rol"));

            lista.add(u);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error listar: " + e.toString());
    }

    return lista;
}
public void eliminar(int id) {
    String sql = "DELETE FROM usuario WHERE id=?";

    try (Connection cn = Conexion.getConexion();
         PreparedStatement ps = cn.prepareStatement(sql)) {

        ps.setInt(1, id);

        int filas = ps.executeUpdate();

        if (filas > 0) {
            System.out.println("Usuario eliminado correctamente");
        } else {
            System.out.println("No se encontró el usuario");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error eliminar: " + e.toString());
    }
}
public void actualizar(Usuario u) {
    
      if (u.getRol().equals("Administrador")) {
        JOptionPane.showMessageDialog(null, "No se puede modificar un administrador");
        return;
    }
      
      
    String sql = "UPDATE usuario SET email=?, telefono=?, rol=? WHERE id=?";

    try (Connection cn = Conexion.getConexion();
         PreparedStatement ps = cn.prepareStatement(sql)) {

        ps.setString(1, u.getEmail());
        ps.setString(2, u.getTelefono());
        ps.setString(3, u.getRol());
        ps.setInt(4, u.getId());

        int filas = ps.executeUpdate();

        if (filas > 0) {
            System.out.println("Usuario actualizado correctamente");
        } else {
            System.out.println("No se pudo actualizar");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error actualizar: " + e.toString());
    }
}
   
   
   //obtengo el rol
   public String obtenerRol(String email) {
    String rol = "";

    try {
        Connection con = Conexion.getConexion();
        String sql = "SELECT rol FROM usuario WHERE email=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, email);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            rol = rs.getString("rol");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.toString());
    }

    return rol;
}
   
}