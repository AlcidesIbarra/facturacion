package DAO;

import DAO.UsuarioDAO;
import Formularios.FormLogin;
import Formularios.FormRegistro;
import javax.swing.JOptionPane;

public class main {
    public static void main(String[] args) {

        UsuarioDAO dao = new UsuarioDAO();

        if (!dao.hayUsuarios()) {
            JOptionPane.showMessageDialog(null, "Debe crear un usuario ADMINISTRADOR");
new FormRegistro("Administrador", null).setVisible(true);
        } else {
            new FormLogin().setVisible(true);
        }
    }
}