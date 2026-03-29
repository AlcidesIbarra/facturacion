package configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class Conexion {
  
    private Connection conectar = null;
    // Asegurate que la ruta sea correcta. Si es relativa al proyecto:
    private final  String url = "jdbc:sqlite:Facturacion.db"; 
    public Connection estableceConexion() {
        try {
            // Verificamos si ya hay una conexión abierta y válida antes de crear otra
            if (conectar == null || conectar.isClosed()) {
                conectar = DriverManager.getConnection(url);
                
                // Optimizaciones para evitar que la BD se "trabe"
                try (Statement st = conectar.createStatement()) {
                    st.execute("PRAGMA journal_mode=WAL;");      // Permite leer mientras otro escribe
                    st.execute("PRAGMA busy_timeout=5000;");    // Espera 5 seg si la BD está ocupada
                    st.execute("PRAGMA synchronous=NORMAL;");   // Mejora velocidad de escritura
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
        }
        return conectar;
    }

    public void cerrarConexion() {
        try {
            if (conectar != null && !conectar.isClosed()) {
                conectar.close();
            }
        } catch (Exception e) {
            System.err.println("Error al cerrar: " + e.getMessage());
        }
    }

    
    private static final String URL = 
// "jdbc:sqlite:C:\\Users\\user\\Desktop\\Facturacion2\\facturacion-main(1)\\facturacion-main\\Facturacion.db";   
            "jdbc:sqlite:Facturacion.db";   
public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}