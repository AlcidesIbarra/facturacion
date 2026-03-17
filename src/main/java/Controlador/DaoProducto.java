/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Configuracion.CConexion;
import Modelos.ItemSeleccionable;
import Modelos.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author ALCIDES
 */
import java.sql.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class DaoProducto {
    // Definimos colores para reutilizar
    private final Color COLOR_EXITO = new Color(40, 167, 69);
    private final Color COLOR_ERROR = new Color(220, 53, 69);

   public boolean agregarProducto(Producto pro, int verifAux, JTable tab, JLabel menj, JInternalFrame internal) {
    boolean resultado;
    
    // 1. Ejecutamos la operación y capturamos el éxito o fallo
    if (verifAux == 1) {
        resultado = registrar(pro, internal);
    } else {
        resultado = modificar(pro, internal);
    }

    // 2. Si fue exitosa y hay una tabla, refrescamos la vista
    if (resultado && tab != null) {
        listarEnTabla(tab, internal);
    }
    
    // 3. Actualizamos el mensaje visual si existe el label
    if (menj != null) {
        menj.setText(resultado ? "Operación exitosa" : "Error en base de datos");
    }

    // 4. RETORNAMOS el resultado para que el botón sepa si limpiar o no los campos
    return resultado;
}

private boolean registrar(Producto pro, JInternalFrame internal) {
    // 1. Validar si el código de barras ya existe
    if (existeCodigoBarras(pro.getCodigoBarras())) {
        new Alerta("El código de barras ya está registrado", new Color(231, 76, 60), internal);
        return false;
    }

    String sql = "INSERT INTO productos (codigo_barras, nombre, precio_venta, precio_compra, stock, id_categoria, id_proveedor) VALUES (?,?,?,?,?,?,?)";
    CConexion conexion = new CConexion();
    
    try (Connection con = conexion.estableceConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, pro.getCodigoBarras());
        ps.setString(2, pro.getNombre().toUpperCase());
        ps.setBigDecimal(3, pro.getPrecioVenta());
        ps.setBigDecimal(4, pro.getPrecioCompra());
        ps.setBigDecimal(5, pro.getStock());
        ps.setInt(6, pro.getIdCategoria());
        ps.setInt(7, pro.getIdProveedor());

        ps.execute();
        new Alerta("¡Producto Registrado!", new Color(46, 204, 113), internal);
        return true;
    } catch (SQLException e) {
        new Alerta("Error al registrar: " + e.getMessage(), new Color(231, 76, 60), internal);
        return false;
    }
}

// MÉTODO AUXILIAR PARA VALIDAR DUPLICADOS
private boolean existeCodigoBarras(String codigo) {
    String sql = "SELECT COUNT(*) FROM productos WHERE codigo_barras = ?";
    CConexion conexion = new CConexion();
    try (Connection con = conexion.estableceConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, codigo);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    private boolean modificar(Producto pro,JInternalFrame internal) {
        String sql = "UPDATE productos SET codigo_barras=?, nombre=?, precio_venta=?, precio_compra=?, stock=?, id_categoria=?, id_proveedor=? WHERE id_producto=?";
        CConexion conexion = new CConexion();
        try (Connection con = conexion.estableceConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pro.getCodigoBarras());
            ps.setString(2, pro.getNombre());
            ps.setBigDecimal(3, pro.getPrecioVenta());
            ps.setBigDecimal(4, pro.getPrecioCompra());
            ps.setBigDecimal(5, pro.getStock());
            ps.setInt(6, pro.getIdCategoria());
            ps.setInt(7, pro.getIdProveedor());
            ps.setInt(8, pro.getId());

            int res = ps.executeUpdate();
            if (res > 0) {
                new Alerta("¡Producto Actualizado!", COLOR_EXITO,internal);
                return true;
            }
            return false;
        } catch (SQLException e) {
            new Alerta("Error al modificar: " + e.getMessage(), COLOR_ERROR,internal);
            return false;
        }
    }

    public void listarEnTabla(JTable tabla,JInternalFrame internal) {
        String sql = "SELECT * FROM productos ORDER BY id_producto DESC";
        CConexion conexion = new CConexion();
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); // Limpiar tabla antes de cargar

        try (Connection con = conexion.estableceConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            Object[] fila = new Object[8];
            while (rs.next()) {
                fila[0] = rs.getInt("id_producto");
                fila[1] = rs.getString("codigo_barras");
                fila[2] = rs.getString("nombre");
                fila[3] = rs.getBigDecimal("precio_venta");
                fila[4] = rs.getBigDecimal("precio_compra");
                fila[5] = rs.getBigDecimal("stock");
                fila[6] = rs.getInt("id_categoria");
                fila[7] = rs.getInt("id_proveedor");
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            new Alerta("Error al listar: " + e.getMessage(), COLOR_ERROR,internal);
        }
    }
    
    public List<String> obtenerTodosLosRubros() {
    List<String> lista = new ArrayList<>();
    String sql = "SELECT nombre FROM rubro";
       CConexion conexion = new CConexion();
         try (Connection con = conexion.estableceConexion();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            lista.add(rs.getString("nombre"));
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return lista;
}

public void cargarComboGenerico(JComboBox<ItemSeleccionable> combo, int tipo) {
    String sql = "";
    String colId = "";
    
    // Configuramos la consulta según el parámetro 'tipo'
    switch (tipo) {
        case 1: // Rubro
            sql = "SELECT id_rubro, nombre FROM rubro";
            colId = "id_rubro";
            break;
        case 2: // Proveedor
            sql = "SELECT id_proveedor, nombre FROM proveedor";
            colId = "id_proveedor";
            break;
        default:
            System.out.println("Tipo no válido");
            return;
    }

         CConexion conexion = new CConexion();
         try (Connection con = conexion.estableceConexion();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        combo.removeAllItems();
        // Agregamos una opción por defecto (opcional)
        combo.addItem(new ItemSeleccionable(0, "-- Seleccione --"));

        while (rs.next()) {
            combo.addItem(new ItemSeleccionable(
                rs.getInt(colId), 
                rs.getString("nombre")
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public boolean insertar(String tabla, int id, String nombre) throws SQLException {
        // Usamos el nombre de la tabla de forma dinámica
        String sql = "INSERT INTO " + tabla + " (id_" + tabla + ", nombre) VALUES (?, ?)";
       CConexion conexion = new CConexion();
         try (Connection con = conexion.estableceConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.setString(2, nombre.trim().toUpperCase());
            
            return pstmt.executeUpdate() > 0;
        }
    }
public boolean existeID(String tabla, int id) {
    String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE id_" + tabla + " = ?";
       CConexion conexion = new CConexion();
         try (Connection con = conexion.estableceConexion();
         PreparedStatement pstmt = con.prepareStatement(sql)) {
        
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1) > 0; // Si es mayor a 0, el ID ya existe
        }
    } catch (SQLException e) {
        System.out.println("Error al verificar ID: " + e.getMessage());
    }
    return false;
}
public int obtenerSiguienteIDDisponible(String tabla) {
    String sql = "SELECT MAX(id_" + tabla + ") FROM " + tabla;
         CConexion conexion = new CConexion();
         try (Connection con = conexion.estableceConexion();
         PreparedStatement pstmt = con.prepareStatement(sql)) {
        
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            int maxId = rs.getInt(1);
            return maxId + 1; // Retorna el siguiente
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener ID sugerido: " + e.getMessage());
    }
    return 1; // Si la tabla está vacía, empieza en 1
}

public Integer verCodigoDisponible(JTextField tex) {
    Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
    
    // Esta consulta verifica si el 1 existe; si no, lo devuelve. 
    // Si el 1 existe, busca el primer número faltante en la secuencia.
    String sql = "SELECT CASE " +
                 "  WHEN (SELECT COUNT(*) FROM productos WHERE CAST(codigo_barras AS INTEGER) = 1) = 0 THEN 1 " +
                 "  ELSE (SELECT CAST(t1.codigo_barras AS INTEGER) + 1 " +
                 "        FROM productos AS t1 " +
                 "        LEFT JOIN productos AS t2 ON CAST(t1.codigo_barras AS INTEGER) + 1 = CAST(t2.codigo_barras AS INTEGER) " +
                 "        WHERE t2.codigo_barras IS NULL " +
                 "        ORDER BY CAST(t1.codigo_barras AS INTEGER) ASC LIMIT 1) " +
                 "END AS disponible";

    Integer disponible = 1;

    try (Connection con = objetoConexion.estableceConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            disponible = rs.getInt("disponible");
            if (disponible <= 0) disponible = 1;
            
            if (tex != null) {
                tex.setText(String.valueOf(disponible));
            }
        }
    } catch (Exception e) {
        new Alerta("Error al buscar Código disponible", new java.awt.Color(231, 76, 60), null);
        e.printStackTrace();
    }
    
    return disponible;
}

public void seleccionarItemPorId(JComboBox<ItemSeleccionable> combo, int idBuscado) {
    for (int i = 0; i < combo.getItemCount(); i++) {
        ItemSeleccionable item = combo.getItemAt(i);
        if (item.getId() == idBuscado) {
            combo.setSelectedIndex(i);
            break;
        }
    }
}
public Producto obtenerproducto(Integer codigo) {
    // 1. SQL para buscar por ID de producto (o codigo_barras si preferís)
    String sql = "SELECT id_producto, codigo_barras, nombre, precio_venta, precio_compra, stock, id_categoria, id_proveedor " +
                 "FROM productos WHERE id_producto = ?";
    
    Producto pro = new Producto();
    CConexion conexion = new CConexion();

    try (Connection con = conexion.estableceConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, codigo);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            // 2. Mapeo de la base de datos al objeto Producto
            pro.setId(rs.getInt("id_producto"));
            pro.setCodigoBarras(rs.getString("codigo_barras"));
            pro.setNombre(rs.getString("nombre"));
            pro.setPrecioVenta(rs.getBigDecimal("precio_venta"));
            pro.setPrecioCompra(rs.getBigDecimal("precio_compra"));
            pro.setStock(rs.getBigDecimal("stock"));
            
            // Estos IDs son la clave para que tus ComboBox se seleccionen solos
            pro.setIdCategoria(rs.getInt("id_categoria"));
            pro.setIdProveedor(rs.getInt("id_proveedor"));
        }

    } catch (SQLException e) {
        System.out.println("Error al obtener producto: " + e.getMessage());
    }
    
    return pro;
}
}