package Formularios;

import DAO.UsuarioDAO;
import Modelos.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormUsuarios extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnEliminar, btnEditar, btnVolver;

    public FormUsuarios() {
        setTitle("Usuarios registrados");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Color.BLACK);

        // TABLA
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Email");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Rol");

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // BOTONES
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);

        btnEliminar = new JButton("Eliminar");
        btnEditar = new JButton("Editar");
        btnVolver = new JButton("Volver");

        estilo(btnEliminar);
        estilo(btnEditar);
        estilo(btnVolver);

        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnVolver);

        add(panel, BorderLayout.SOUTH);

        cargarUsuarios();

        // EVENTOS
        btnEliminar.addActionListener(e -> eliminarUsuario());

        btnVolver.addActionListener(e -> {
new FormRegistro("Administrador", null).setVisible(true);
this.dispose();
        });

        btnEditar.addActionListener(e -> editarUsuario());
    }

    private void estilo(JButton b) {
        b.setBackground(new Color(0, 255, 255));
        b.setForeground(Color.BLACK);
    }

    //  Cargar usuarios en la tabla
    private void cargarUsuarios() {
        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> lista = dao.listarUsuarios();

        modelo.setRowCount(0);

        for (Usuario u : lista) {
            modelo.addRow(new Object[]{
                    u.getId(),
                    u.getEmail(),
                    u.getTelefono(),
                    u.getRol()
            });
        }
    }

    //  Eliminar usuario
    private void eliminarUsuario() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccionar usuario");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        String email = modelo.getValueAt(fila, 1).toString(); // 👈 IMPORTANTE

        UsuarioDAO dao = new UsuarioDAO();
        dao.eliminar(id);

        eliminarEmailGuardado(email); //  BORRA DEL LOGIN

        JOptionPane.showMessageDialog(this, "Usuario eliminado");
        cargarUsuarios();
    }

    // ️ Editar usuario
    private void editarUsuario() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccionar usuario");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        String email = modelo.getValueAt(fila, 1).toString();
        String telefono = modelo.getValueAt(fila, 2).toString();
        String rol = modelo.getValueAt(fila, 3).toString();

        String nuevoEmail = JOptionPane.showInputDialog(this, "Email:", email);
        String nuevoTel = JOptionPane.showInputDialog(this, "Teléfono:", telefono);

        Usuario u = new Usuario();
        u.setId(id);
        u.setEmail(nuevoEmail);
        u.setTelefono(nuevoTel);
        u.setRol(rol);

        UsuarioDAO dao = new UsuarioDAO();
        dao.actualizar(u);

        JOptionPane.showMessageDialog(this, "Usuario actualizado");
        cargarUsuarios();
    }

    // ? MÉTODO NUEVO (elimina email del archivo)
    private void eliminarEmailGuardado(String email) {
        try {
            java.io.File file = new java.io.File("emails.txt");

            if (!file.exists()) return;

            java.util.List<String> lista = new java.util.ArrayList<>();

            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file));
            String linea;

            while ((linea = br.readLine()) != null) {
                if (!linea.equals(email)) {
                    lista.add(linea);
                }
            }
            br.close();

            java.io.FileWriter fw = new java.io.FileWriter(file, false);
            for (String e : lista) {
                fw.write(e + "\n");
            }
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}