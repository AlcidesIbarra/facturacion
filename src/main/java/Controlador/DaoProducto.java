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
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DaoProducto {
    // Definimos colores para reutilizar
    private final Color COLOR_EXITO = new Color(40, 167, 69);
    private final Color COLOR_ERROR = new Color(220, 53, 69);

    public void agregarProducto(Producto pro, int verifAux, JTable tab, JLabel menj) {
        boolean resultado;
        
        if (verifAux == 1) {
            resultado = registrar(pro);
        } else {
            resultado = modificar(pro);
        }

        // Si la operación fue exitosa y pasaste una tabla, la refrescamos
        if (resultado && tab != null) {
            listarEnTabla(tab);
        }
        
        // Si pasaste un label, podemos poner un texto breve
        if (menj != null) {
            menj.setText(resultado ? "Operación exitosa" : "Error en base de datos");
        }
    }

    private boolean registrar(Producto pro) {
        String sql = "INSERT INTO productos (codigo_barras, nombre, precio_venta, precio_compra, stock, id_categoria, id_proveedor) VALUES (?,?,?,?,?,?,?)";
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

            ps.execute();
            new Alerta("¡Producto Registrado!", COLOR_EXITO);
            return true;
        } catch (SQLException e) {
            new Alerta("Error al registrar: " + e.getMessage(), COLOR_ERROR);
            return false;
        }
    }

    private boolean modificar(Producto pro) {
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
                new Alerta("¡Producto Actualizado!", COLOR_EXITO);
                return true;
            }
            return false;
        } catch (SQLException e) {
            new Alerta("Error al modificar: " + e.getMessage(), COLOR_ERROR);
            return false;
        }
    }

    public void listarEnTabla(JTable tabla) {
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
            new Alerta("Error al listar: " + e.getMessage(), COLOR_ERROR);
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
}