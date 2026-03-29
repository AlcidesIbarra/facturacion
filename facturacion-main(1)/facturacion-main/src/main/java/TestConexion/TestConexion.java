package testconexion; // Siempre en minúsculas para evitar problemas en Linux/Mac

import java.sql.Connection;
import configuracion.Conexion; // Importar tu clase de conexión correctamente

public class TestConexion {
    public static void main(String[] args) {
        try (Connection cn = Conexion.getConexion()) {
            if (cn != null) {
                System.out.println("Conexión exitosa!");
            } else {
                System.out.println("No se pudo conectar.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}