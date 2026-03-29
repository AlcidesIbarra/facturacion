/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;


import Formularios.FormVenta;
import Modelos.Mensaje;
import Modelos.ModeloProducto;
import configuracion.Conexion;
import java.awt.Component;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ALCIDES
 */
public class ControladorProducto {

    public void muestraProductos(JTable tablaTotalProductos, Integer num, String palabra, String activos) {
     Conexion objetoConexion = new Conexion();
        Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();
        DefaultTableModel modelo = new DefaultTableModel();
        String sql = "";
        modelo.addColumn("COD.");
        modelo.addColumn("DESCRIPCION");
        modelo.addColumn("COMPRA");
        modelo.addColumn("VENTA");
        modelo.addColumn("STOCK");
        modelo.addColumn("KGxBTO");
        modelo.addColumn("PROVEEDOR");
        modelo.addColumn("RUBRO");
        tablaTotalProductos.setModel(modelo);

        boolean act = false;
        switch (activos) {
            case "ACTIVOS":
                act = false;
                break;
            case "INACTIVOS":
                act = true;
                break;

            default:
                act = false;
        }

        Boolean numeroLetra = false;
        Integer numero;
        try {
            numero = Integer.parseInt(palabra);
            numeroLetra = true;
        } catch (Exception e) {
            numeroLetra = false;
        }
        String consulta = null;
        //   if (!palabra.equals("")) {

        if (numeroLetra == false) {
            consulta = "select * from producto where"
                    + " producto.descripcion like concat('%',?,'%') and  producto.activo!=" + act + ";";

        } else {
            consulta = "select * from producto where producto.codigoproducto"
                    + " like concat('%',?,'%') and  producto.activo!=" + act + ";";
        }
        try {

            PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
            ps.setString(1, palabra);
            System.out.println("estement" + ps);

            ResultSet rs = ps.executeQuery();
            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
            while (rs.next()) {
                objetoProducto.setCodigo(rs.getLong("codigoproducto"));
                objetoProducto.setDescripcion(rs.getString("descripcion"));
                String compraP = (formatoMoneda.format(rs.getDouble("precio_compra")));
                String ventaP = (formatoMoneda.format(rs.getDouble("precio_venta")));
                objetoProducto.setStock(rs.getDouble("stock"));
                objetoProducto.setProveedor(rs.getString("proveedor"));
                objetoProducto.setKgbulto(rs.getDouble("kgbulto"));
                objetoProducto.setRubro(rs.getString("rubro"));
                modelo.addRow(new Object[]{objetoProducto.getCodigo(), objetoProducto.getDescripcion(),
                    compraP, ventaP, objetoProducto.getStock(), objetoProducto.getKgbulto(), objetoProducto.getProveedor(),
                    objetoProducto.getRubro()
                });
            }

            tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(55);
            tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(190);
            tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(70);
            tablaTotalProductos.getColumnModel().getColumn(5).setPreferredWidth(70);
            tablaTotalProductos.getColumnModel().getColumn(6).setPreferredWidth(138);
            tablaTotalProductos.getColumnModel().getColumn(7).setPreferredWidth(138);
            DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
            cent.setHorizontalAlignment(JLabel.CENTER);
            tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(5).setCellRenderer(cent);

            tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(new paddingRig());
            tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(new paddingRig());

            for (int i = 0; i < tablaTotalProductos.getColumnCount(); i++) {
                Class<?> colClas = tablaTotalProductos.getColumnClass(i);
                tablaTotalProductos.setDefaultEditor(colClas, null);
            }

            tablaTotalProductos.setModel(modelo);
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "23 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
       } finally {
            objetoConexion.cerrarConexion();
        }

    }
public void muestraProductosKg(JTable tablaTotalProductos, Integer num, String palabra, String activos) {
     Conexion objetoConexion = new Conexion();
    DefaultTableModel modelo = new DefaultTableModel();

    // 1. Estructura de columnas (Agregamos "ID_STOCK" al final o donde prefieras)
    modelo.addColumn("COD.");
    modelo.addColumn("DESCRIPCION");
    modelo.addColumn("STOCK");
    modelo.addColumn("FECHA INGRESO");
    modelo.addColumn("COMPRA");
    modelo.addColumn("VENTA");
    modelo.addColumn("PROVEEDOR");
    modelo.addColumn("RUBRO");
    modelo.addColumn("ID_STOCK"); // Nueva columna oculta (índice 8)

    tablaTotalProductos.setModel(modelo);

    // ... (Tu lógica de validación numeroLetra y baseSql se mantiene igual) ...
    boolean numeroLetra = false;
    try { Integer.parseInt(palabra); numeroLetra = true; } catch (Exception e) { numeroLetra = false; }
    
    String consulta = "SELECT s.id, p.codigoproducto, p.descripcion, s.kg_individual, s.fecha, p.precio_compra, p.precio_venta, p.proveedor, p.rubro " +
                     "FROM stock_detalle s JOIN producto p ON s.producto_id = p.codigoproducto " +
                     "WHERE s.estado = 'DISPONIBLE'  " + 
                     (numeroLetra ? "AND p.codigoproducto LIKE ? " : "AND p.descripcion LIKE ? and p.activo = 1 ") + "ORDER BY s.id DESC";

    try {
        PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
        ps.setString(1, "%" + palabra + "%");
        ResultSet rs = ps.executeQuery();

        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00");
        SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfSalida = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        while (rs.next()) {
            String fechaFormateada = "";
            try {
                String fechaRaw = rs.getString("fecha");
                if(fechaRaw != null) fechaFormateada = sdfSalida.format(sdfEntrada.parse(fechaRaw));
            } catch (Exception e) { fechaFormateada = rs.getString("fecha"); }

            // 2. Añadir el s.id en la última posición
            modelo.addRow(new Object[]{
                rs.getInt("codigoproducto"),
                rs.getString("descripcion"),
                formatoDecimal.format(rs.getDouble("kg_individual")),
                fechaFormateada,
                formatoMoneda.format(rs.getDouble("precio_compra")),
                formatoMoneda.format(rs.getDouble("precio_venta")),
                rs.getString("proveedor"),
                rs.getString("rubro"),
                rs.getInt("id") // El ID oculto
            });
        }
          for (int i = 0; i < tablaTotalProductos.getColumnCount(); i++) {
                Class<?> colClas = tablaTotalProductos.getColumnClass(i);
                tablaTotalProductos.setDefaultEditor(colClas, null);
            }
        // 3. Diseño y Ocultamiento de la columna ID
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // ... (tus otros anchos se mantienen) ...
        tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(55);
        tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(170);
        tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaTotalProductos.getColumnModel().getColumn(5).setPreferredWidth(100);
        tablaTotalProductos.getColumnModel().getColumn(6).setPreferredWidth(120);
        tablaTotalProductos.getColumnModel().getColumn(7).setPreferredWidth(120);

        // OCULTAR COLUMNA ID (Índice 8)
        tablaTotalProductos.getColumnModel().getColumn(8).setMinWidth(0);
        tablaTotalProductos.getColumnModel().getColumn(8).setPreferredWidth(0);
        tablaTotalProductos.getColumnModel().getColumn(8).setMaxWidth(0);

        // 4. Renderers y Bloqueo (Igual que antes)
        // ... 
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.toString());
    } finally {
        objetoConexion.cerrarConexion();
    }
}

public Mensaje eliminarProductoPorId(JTable tablaTotalProductos) {
    // 1. Verificar si hay una fila seleccionada
    int filaSeleccionada = tablaTotalProductos.getSelectedRow();
    Mensaje mj= new Mensaje();
    if (filaSeleccionada == -1) {
       // JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila para eliminar.");
        mj.setCodigo(2);
       mj.setMensaje( "Por favor, seleccione una fila para eliminar.");
        return mj;
    }

    // 2. Obtener el ID de la columna oculta (índice 8)
    // Usamos el modelo para asegurarnos de traer el dato real aunque la tabla esté filtrada
    String idEliminar = tablaTotalProductos.getModel().getValueAt(filaSeleccionada, 8).toString();
    String descripcion = tablaTotalProductos.getModel().getValueAt(filaSeleccionada, 1).toString();

    // 3. Confirmación del usuario
    int confirmar = JOptionPane.showConfirmDialog(null, 
        "¿Está seguro de eliminar: " + descripcion + "?", "Confirmar eliminación", 
        JOptionPane.YES_NO_OPTION);

    if (confirmar == JOptionPane.YES_OPTION) {
         Conexion objetoConexion = new Conexion();
        
        // En tu caso, como usas 'estado = DISPONIBLE', quizás prefieras hacer un borrado lógico:
      String sql = "UPDATE stock_detalle SET estado = 'ELIMINADO' WHERE id = ?;";
      //  String sql = "DELETE FROM stock_detalle WHERE id = ?;";

        try {
            PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idEliminar));
            ps.executeUpdate();

          //  JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");
                    mj.setCodigo(1);
       mj.setMensaje( "Producto eliminado correctamente.");
            // 4. Refrescar la tabla (llamando al método anterior con los parámetros actuales)
            // muestraProductosKg(tablaTotalProductos, ...); 

        } catch (Exception e) {
                                mj.setCodigo(4);
       mj.setMensaje( "Error al eliminar: " + e.toString());
            
         //   JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
    }
    return mj;
}
    
    
    public Mensaje editarKgSeleccionado(JTable tablaTotalProductos) {
    int filaSeleccionada = tablaTotalProductos.getSelectedRow();
Mensaje mj = new Mensaje();
    if (filaSeleccionada == -1) {
       // JOptionPane.showMessageDialog(null, "Seleccione un producto de la lista.");
        mj.setCodigo(2);
       mj.setMensaje( "Seleccione un producto de la lista.");
        return mj;
    }

    // 1. Obtener datos actuales (ID es la col 8, Stock/Kg es la col 2)
    String idStock = tablaTotalProductos.getModel().getValueAt(filaSeleccionada, 8).toString();
    String descripcion = tablaTotalProductos.getModel().getValueAt(filaSeleccionada, 1).toString();
    String valorActual = tablaTotalProductos.getModel().getValueAt(filaSeleccionada, 2).toString();

    // 2. Ventana emergente para pedir el nuevo peso
    String nuevoPesoStr = JOptionPane.showInputDialog(null, 
        "Producto: " + descripcion + "\nIngrese los nuevos Kg:", 
        valorActual);

    // Si el usuario no cancela y escribe algo
    if (nuevoPesoStr != null && !nuevoPesoStr.isEmpty()) {
        try {
            // Reemplazar coma por punto por si el usuario usa formato regional
            double nuevoPeso = Double.parseDouble(nuevoPesoStr.replace(",", "."));

            // 3. Actualizar en Base de Datos
             Conexion objetoConexion = new Conexion();
            String consulta = "UPDATE stock_detalle SET kg_individual = ? WHERE id = ?;";

            try {
                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
                ps.setDouble(1, nuevoPeso);
                ps.setInt(2, Integer.parseInt(idStock));
                
                int exito = ps.executeUpdate();
                if (exito > 0) {
                         mj.setCodigo(1);
       mj.setMensaje( "Peso actualizado correctamente.");
        return mj;
                 //   JOptionPane.showMessageDialog(null, "Peso actualizado correctamente.");
                    // Aquí deberías llamar a tu método muestraProductosKg() para refrescar la tabla
                }
            } catch (Exception e) {
                               mj.setCodigo(4);
       mj.setMensaje( "Error al actualizar: " + e.toString());
               // JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.toString());
            } finally {
                objetoConexion.cerrarConexion();
            }

        } catch (NumberFormatException e) {
                              mj.setCodigo(4);
       mj.setMensaje( "Por favor, ingrese un número válido para los kilogramos.");
          //  JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido para los kilogramos.");
        }
    }
    return mj;
}
    
    
    public class paddingRig extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.RIGHT);
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
            return this;
        }
    }

    public Integer verCodigoDisponlible(JTextField tex) {
         Conexion objetoConexion = new Conexion();
        String sql = " select (t1.codigoproducto+1) as disponible from productos "
                + "as t1 left join productos as t2 on t1.codigoproducto+1=t2.codigoproducto"
                + " where t2.codigoproducto is null order by t1.codigoproducto limit 1;";
        Integer disponible = 0;
        try {
            PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            int contador = 0;
            while (rs.next()) {
                tex.setText(rs.getString("disponible"));
                disponible = rs.getInt("disponible");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "24 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
       } finally {
            objetoConexion.cerrarConexion();
        }
        return disponible;
    }

    public Mensaje agregarProducto(JTextField codigoProducto, JTextField descripcionProducto,
            JTextField costoProducto, JTextField VentaProducto, JTextField stockProducto,
            JTextField proveedorProducto, JTextField rubroProducto,
            Integer verificadorAux, JTable tabla, JLabel menj, JTextField kgBulto) {
        Mensaje mj = null;
         Conexion objetoConexion = new Conexion();
        Modelos.ModeloProducto objetoProducto = obtenerproducto(codigoProducto.getText());

        if (descripcionProducto.getText().equals("")) {
            mj = new Mensaje();
            mj.setCodigo(3);
            mj.setMensaje("El nombre no puede estar vacio");
            return mj;
        } else {
            int verificador = verificadorAux;
            if (verificador == 4) {
                verificador = 2;
            }
            switch (verificador) {
                case 1:

                    if (objetoProducto != null) {
                        mj = new Mensaje();
                        mj.setCodigo(3);
                        int num = verCodigoDisponlible(codigoProducto);
                        mj.setMensaje("El codigo, ya esta asignado, prueba el: " + num);

                        return mj;
                    }
                    String consulta = "insert into Producto(codigoproducto,descripcion ,precio_compra,"
                            + "precio_venta,stock,proveedor,rubro,activo,kgbulto) "
                            + "values(?,?,?,?,?,?,?,?,?)";
                    try {
                        // System.out.println("            hasta aqui andar1");
                        objetoProducto = new ModeloProducto();
     objetoProducto.setCodigo(Long.parseLong(codigoProducto.getText()));
                        objetoProducto.setDescripcion(descripcionProducto.getText());

                        try {
                            objetoProducto.setPrecioCompra(Double.valueOf(costoProducto.getText()));
                        } catch (Exception e) {
                            objetoProducto.setPrecioCompra(null);
                        }
                        try {
                            objetoProducto.setPrecioVenta(Double.valueOf(VentaProducto.getText()));
                        } catch (Exception e) {
                            objetoProducto.setPrecioVenta(null);
                        }
                        try {
                            objetoProducto.setStock(Double.valueOf(stockProducto.getText()));
                        } catch (Exception e) {
                            objetoProducto.setStock(null);
                        }

                        try {
                            objetoProducto.setKgbulto(Double.valueOf(kgBulto.getText()));
                        } catch (Exception e) {
                            objetoProducto.setKgbulto(null);
                        }

                        objetoProducto.setProveedor(proveedorProducto.getText());
                        objetoProducto.setRubro(rubroProducto.getText());

                        PreparedStatement cs = objetoConexion.estableceConexion().prepareStatement(consulta);

                        cs.setLong(1, objetoProducto.getCodigo());
                        cs.setString(2, objetoProducto.getDescripcion());

                        try {
                            cs.setDouble(3, objetoProducto.getPrecioCompra());
                        } catch (Exception e) {
                            cs.setString(3, null);
                        }
                        try {
                            cs.setDouble(4, objetoProducto.getPrecioVenta());
                        } catch (Exception e) {
                            cs.setString(4, null);
                        }
                        try {
                            cs.setDouble(5, objetoProducto.getStock());
                        } catch (Exception e) {
                            cs.setString(5, null);
                        }

                        try {
                            cs.setDouble(9, objetoProducto.getKgbulto());
                        } catch (Exception e) {
                            cs.setString(9, null);
                        }

                        cs.setString(6, objetoProducto.getProveedor());
                        cs.setString(7, objetoProducto.getRubro());
                        cs.setBoolean(8, true);

                        cs.execute();
                        mj = new Mensaje();
                        mj.setCodigo(1);

                        mj.setMensaje("Se registro el nuevo producto con el codigo: " + objetoProducto.getCodigo());
                    } catch (Exception e) {
                        mj = new Mensaje();
                        mj.setCodigo(4);

                        mj.setMensaje("Error al guardar, contacta al administrador: " + e.toString());
                        //  JOptionPane.showMessageDialog(null, "no anda we13" + e.toString());
                    } finally {
                        objetoConexion.cerrarConexion();
                    }

                    break;
                case 2:
                    System.out.println("    entralcaso 2 de prueba");
                    String consulta1 = "update producto set descripcion=?,precio_compra=?, "
                            + "precio_venta=?,stock=?,proveedor=?,rubro=?,kgbulto=? "
                            + "where producto.codigoproducto=? ";
                    try {

                 objetoProducto.setCodigo(Long.parseLong(codigoProducto.getText()));
                        objetoProducto.setDescripcion(descripcionProducto.getText());
                        try {
                            objetoProducto.setPrecioCompra(Double.parseDouble(costoProducto.getText()));
                        } catch (Exception e) {
                            objetoProducto.setPrecioCompra(null);
                        }
                        try {
                            objetoProducto.setPrecioVenta(Double.parseDouble(VentaProducto.getText()));
                        } catch (Exception e) {
                            objetoProducto.setPrecioVenta(null);
                        }
                        try {
                            objetoProducto.setStock(Double.parseDouble(stockProducto.getText()));
                        } catch (Exception e) {
                            objetoProducto.setStock(null);
                        }

                        try {
                            objetoProducto.setKgbulto(Double.valueOf(kgBulto.getText()));
                        } catch (Exception e) {
                            objetoProducto.setKgbulto(null);
                        }

                        objetoProducto.setProveedor(proveedorProducto.getText());
                        objetoProducto.setRubro(rubroProducto.getText());

                        PreparedStatement cs = objetoConexion.estableceConexion().prepareStatement(consulta1);

                        cs.setString(1, objetoProducto.getDescripcion());
                        try {
                            cs.setDouble(2, objetoProducto.getPrecioCompra());
                        } catch (Exception e) {
                            cs.setString(2, null);
                        }
                        try {
                            cs.setDouble(3, objetoProducto.getPrecioVenta());
                        } catch (Exception e) {
                            cs.setString(3, null);
                        }
                        try {
                            cs.setDouble(4, objetoProducto.getStock());
                        } catch (Exception e) {
                            cs.setString(4, null);
                        }
                        try {
                            cs.setDouble(7, objetoProducto.getKgbulto());
                        } catch (Exception e) {
                            cs.setString(7, null);
                        }

                        cs.setString(5, objetoProducto.getProveedor());
                        cs.setString(6, objetoProducto.getRubro());
                        cs.setLong(8, objetoProducto.getCodigo());
                        cs.execute();
                        mj = new Mensaje();
                        mj.setCodigo(1);
                        System.out.println("codigo producot: "+objetoProducto.getCodigo());
                        System.out.println(cs.toString()+"SENTENCIA");
                        mj.setMensaje("Se actualizo el producto: " + objetoProducto.getCodigo() + " correctamente");
                    } catch (Exception e) {
                        mj = new Mensaje();
                        mj.setCodigo(4);

                        mj.setMensaje("Error al guardar, contacta al administrador: " + e.toString());
                        //  JOptionPane.showMessageDialog(null, "no anda we13" + e.toString());
                    } finally {
                        objetoConexion.cerrarConexion();
                    }

                    if (verificadorAux == 4) {
                        try {
                            muestraProductos(tabla, 1, "", "ACTIVOS");

                        } catch (Exception e) {
                        }

                    }

                    break;

                default:
                    throw new AssertionError();
            }
        }
        if (menj != null) {
            menj.setText(mj.getMensaje());
        }

        return mj;
    }

    public int eliminarProducto(JTable tbResumenVenta) {

        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        int indiceSelleccionado = tbResumenVenta.getSelectedRow();
        try {

            if (indiceSelleccionado != -1) {
                Integer codigoCliente = Integer.parseInt(modelo.getValueAt(indiceSelleccionado, 0).toString());
                 Conexion objetoConexion = new Conexion();
                String consulta = "update producto set activo=FALSE where producto.codigoproducto=?; ";

                PreparedStatement cs = objetoConexion.estableceConexion().prepareStatement(consulta);

                cs.setInt(1, codigoCliente);
                cs.execute();
                muestraProductos(tbResumenVenta, 1, "", "ACTIVOS");
            } else {
                JOptionPane.showMessageDialog(null, "seleccione una fila para eliminar");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error a seleccionar " + e.toString());
        }
        return indiceSelleccionado;
    }

    public Mensaje activarProductoSeleccionado(JTable tbResumenVenta) {
        Mensaje mj = null;
        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        try {
            int indiceSelleccionado = tbResumenVenta.getSelectedRow();
            if (indiceSelleccionado != -1) {
                Integer codigoCliente = Integer.parseInt(modelo.getValueAt(indiceSelleccionado, 0).toString());
                 Conexion objetoConexion = new Conexion();
                String consulta = "update producto set activo=TRUE where producto.codigoproducto=?; ";
                PreparedStatement cs = objetoConexion.estableceConexion().prepareStatement(consulta);

                cs.setInt(1, codigoCliente);
                cs.execute();
                muestraProductos(tbResumenVenta, 1, "", "INACTIVOS");
                mj = new Mensaje();
                mj.setCodigo(1);
                mj.setMensaje("Se restauró con exito el producto: " + codigoCliente);
            } else {
                mj = new Mensaje();
                mj.setCodigo(2);
                mj.setMensaje("Seleccione una fila para activar");
                //  JOptionPane.showMessageDialog(null, "seleccione una fila para eliminar");
            }
        } catch (Exception e) {
            mj = new Mensaje();
            mj.setCodigo(4);
            mj.setMensaje("error a seleccionar " + e.toString());
            //  JOptionPane.showMessageDialog(null, "error a seleccionar " + e.toString());
        }
        return mj;
    }

    public void SeleccionarProductosVenta(JTable listaProducto, JTextField fieldCantidad,
            JTextField codigoProducto, JTextField txtBuscarProducto,
            JTextField prCosto, JTextField prVenta, JTextField descProducto,
            JScrollPane contenedorTabla, JTextField texfieldStock,
            JTextField rubro, JTextField proveedor, String seleccionador) {
        int fila = listaProducto.getSelectedRow();

        Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();
        DefaultTableModel modelo = new DefaultTableModel();
        //     modelo.addColumn("descripcion");
        //     modelo.addColumn("id");
         Conexion objetoConexion = new Conexion();

        Integer idSelecte;
        String consulta = "";
        try {
            if (fila >= 0) {
                idSelecte = Integer.parseInt(listaProducto.getValueAt(fila, 1).toString());

                codigoProducto.setText(idSelecte.toString());

                consulta = "select producto.codigoproducto, producto.descripcion, producto.precio_compra,"
                        + " producto.precio_venta, producto.stock, "
                        + " producto.proveedor, producto.rubro from producto "
                        + " where codigoproducto=? and producto.activo!=FALSE;";
                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
                ps.setString(1, codigoProducto.getText());
                ResultSet rs = ps.executeQuery();

                rs = ps.executeQuery();

                Double diferido = null;
                Double prCostoVar = null;
                String descripcion = null;
                Double Stock = null;
                String rubrors = null;
                String proveedorrs = null;

                while (rs.next()) {

                    diferido = rs.getDouble("precio_venta");
                    prCostoVar = rs.getDouble("precio_compra");
                    descripcion = rs.getString("descripcion");
                    Stock = rs.getDouble("stock");
                    rubrors = rs.getString("rubro");
                    proveedorrs = rs.getString("proveedor");
                }

                prCosto.setText(prCostoVar.toString());
                prVenta.setText(diferido.toString());
                texfieldStock.setText(Stock.toString());
                descProducto.setText(descripcion);
                rubro.setText(rubrors);
                proveedor.setText(proveedorrs);

                txtBuscarProducto.setText(descripcion);
                contenedorTabla.setVisible(false);
                System.out.println("        " + seleccionador);

                switch (seleccionador) {
                    case "factura":
                        texfieldStock.selectAll();
                        texfieldStock.requestFocus();
                        texfieldStock.requestFocusInWindow();
                        break;
                    case "stock":
                        texfieldStock.selectAll();
                        texfieldStock.requestFocus();
                        texfieldStock.requestFocusInWindow();
                        break;
                    case "precio":
                        prCosto.selectAll();
                        prCosto.requestFocus();
                        prCosto.requestFocusInWindow();
                        break;

                    default:
                        texfieldStock.requestFocus();
                        texfieldStock.requestFocus();
                        texfieldStock.requestFocusInWindow();
                }

                diferido = null;
                prCosto = null;
                descripcion = null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error, contacte al administrador" + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }

    }

    public void pasarPreciosGuardar(JTable tbResumenVenta, JTextField codigoProducto, JTextField descripcion,
            JTextField costo, JTextField venta, JTextField fieldBuscador,
            JTextField Stock, JLabel total) {

        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        String codProducto = codigoProducto.getText();
        boolean verificadorDuplicado = false;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            String idExistente = (String) modelo.getValueAt(i, 0);
            if (idExistente.equals(codProducto)) {
                JOptionPane.showMessageDialog(null, "el producto ya esta registrado");
                verificadorDuplicado = true;
                return;
            }
        }

        double stocknm = Double.parseDouble(Stock.getText());

        Double ingreso = 0d;
        Double subtotal = 0d;
        String descripcionProd = descripcion.getText();
        Double prCosto = Double.parseDouble(costo.getText());
        Double prVenta = Double.parseDouble(venta.getText());
        Double diferencia = prVenta - prCosto;

        modelo.addRow(new Object[]{codProducto, descripcion.getText(), prCosto, prVenta, Stock.getText(), ingreso, subtotal});
        fieldBuscador.requestFocus();

        codigoProducto.setText("");
        descripcion.setText("");
        costo.setText("");
        venta.setText("");
        //     cantidad.setText("");
        Stock.setText("");

    }

public Mensaje guardaAjustes(JTable tbResumenVenta) {
    Mensaje mj = null;
     Conexion objetoConexion = new Conexion();
    Connection conn = objetoConexion.estableceConexion();

    // 1. Insertar el pesaje individual incluyendo la FECHA ACTUAL
    String sqlInsertDetalle = "INSERT INTO stock_detalle (producto_id, kg_individual, fecha) "
                            + "VALUES (?, ?, datetime('now', 'localtime'));";
    
    // 2. Actualizar el stock maestro y datos generales
    String sqlUpdateMaestro = "UPDATE producto SET descripcion=?, precio_compra=?, precio_venta=?, "
            + "proveedor=?, rubro=?, "
            + "stock = (SELECT SUM(kg_individual) FROM stock_detalle WHERE producto_id = ?) "
            + "WHERE codigoproducto = ?;";

    try {
        conn.setAutoCommit(false); 

        PreparedStatement psInsert = conn.prepareStatement(sqlInsertDetalle);
        PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateMaestro);

        int filas = tbResumenVenta.getRowCount();
        for (int i = 0; i < filas; i++) {
            // Extraer ID y KG
            int idProd = Integer.parseInt(tbResumenVenta.getValueAt(i, 0).toString());
            double kgNuevo = Double.parseDouble(tbResumenVenta.getValueAt(i, 5).toString().replace(",", "."));

            // PASO A: Guardar el nuevo registro con la fecha automática de la BD
            if(kgNuevo!=0){
            psInsert.setInt(1, idProd);
            psInsert.setDouble(2, kgNuevo);
            psInsert.executeUpdate();}

            // PASO B: Actualizar datos generales
            psUpdate.setString(1, tbResumenVenta.getValueAt(i, 1).toString());
            psUpdate.setDouble(2, Double.parseDouble(tbResumenVenta.getValueAt(i, 2).toString().replace(",", ".")));
            psUpdate.setDouble(3, Double.parseDouble(tbResumenVenta.getValueAt(i, 3).toString().replace(",", ".")));
            psUpdate.setString(4, tbResumenVenta.getValueAt(i, 6).toString());
            psUpdate.setString(5, tbResumenVenta.getValueAt(i, 7).toString());
            psUpdate.setInt(6, idProd);
            psUpdate.setInt(7, idProd);
            psUpdate.executeUpdate();
        }

        conn.commit();
        
        mj = new Mensaje();
        mj.setCodigo(1);
        mj.setMensaje("¡Stock actualizado y fecha registrada!");
        ((DefaultTableModel) tbResumenVenta.getModel()).setRowCount(0);

    } catch (Exception e) {
        try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        mj = new Mensaje();
        mj.setCodigo(4);
        mj.setMensaje("Error al guardar: " + e.getMessage());
    } finally {
        objetoConexion.cerrarConexion();
    }
    return mj;
}




    public Integer editarProducto(JTable tbResumenVenta) {
        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        Integer codigoProducto = 0;
        try {
            int indiceSelleccionado = tbResumenVenta.getSelectedRow();
            if (indiceSelleccionado != -1) {
                codigoProducto = Integer.parseInt(modelo.getValueAt(indiceSelleccionado, 0).toString());

            } else {
                codigoProducto = indiceSelleccionado;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error a seleccionar " + e.toString());
        }

        return codigoProducto;

    }

    public ModeloProducto obtenerproducto(String numero1) {
        ModeloProducto producto = null;
        Integer numero = 0;
        try {
            numero = Integer.parseInt(numero1);
        } catch (Exception e) {
        }

        try {
             Conexion objetoConexion = new Conexion();
            String consulta = "select * from producto where producto.codigoproducto=? and producto.activo!=FALSE; ";

            PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
            ps.setInt(1, numero);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                producto = new ModeloProducto();
                producto.setCodigo(rs.getLong("codigoproducto"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecioCompra(rs.getDouble("precio_compra"));
                producto.setPrecioVenta(rs.getDouble("precio_venta"));
                producto.setStock(rs.getDouble("stock"));
                producto.setProveedor(rs.getString("proveedor"));
                producto.setRubro(rs.getString("rubro"));
                producto.setKgbulto(rs.getDouble("kgbulto"));
            }
            //   System.out.println("producto"+producto.getDescripcion());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error a seleccionar " + e.toString());
        }
        return producto;
    }

    public List<Object[]> obtenerProductosParaTabla() {
        List<Object[]> listaProductos = new ArrayList<>();
         Conexion objetoConexion = new Conexion();

        // Consulta SQL filtrando por activos (asumiendo que la columna de estado es la última)
        String sql = "SELECT * FROM producto WHERE activo= 1";

        try (Connection conn = objetoConexion.estableceConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Creamos el array de 9 posiciones (Índices 0 al 8)
                Object[] fila = new Object[9];

                fila[0] = false; // SEL: Checkbox
                fila[1] = rs.getInt("codigoproducto"); // Codigo
                fila[2] = rs.getString("descripcion"); // Descripcion

                // --- MANEJO DE COSTO ---
                double costoBase = rs.getDouble("precio_compra");
                double costoRedondeado = Math.round(costoBase * 100.0) / 100.0;

                fila[3] = costoRedondeado; // [RESPALDO COSTO] - No se toca nunca
                fila[4] = costoRedondeado; // [COSTO NUEVO] - Este cambiará con el %

                // --- MANEJO DE PRECIO DE VENTA ---
                double precioBase = rs.getDouble("precio_venta");
                double precioRedondeado = Math.round(precioBase * 100.0) / 100.0;

                fila[5] = precioRedondeado; // [PRECIO NUEVO] - Este cambiará con el %

                fila[6] = rs.getString("proveedor");
                fila[7] = rs.getString("rubro");

                // --- EL RESPALDO CRÍTICO ---
                fila[8] = precioRedondeado; // [RESPALDO PRECIO] - Oculto, sirve para el reset

                listaProductos.add(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar productos: " + e.getMessage());
        }
        return listaProductos;
    }
    
    public Mensaje guardarCambiosMasivos(List<Object[]> listaDatos) {
        Mensaje mj = new Mensaje();
     Conexion objetoConexion = new Conexion();
    // SQL para actualizar los precios por código
    String sql = "UPDATE producto SET precio_compra = ?, precio_venta = ? WHERE codigoproducto = ?";

    try (Connection conn = objetoConexion.estableceConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        // 1. Desactivamos el auto-commit para mayor velocidad y seguridad
        conn.setAutoCommit(false); 

        int contador = 0;
        for (Object[] fila : listaDatos) {
            // 2. SOLO procesamos los que tienen el Checkbox (pos 0) en TRUE
            if (fila[0] != null && (Boolean) fila[0]) {
                
                ps.setDouble(1, (Double) fila[4]); // Costo Nuevo
                ps.setDouble(2, (Double) fila[5]); // Precio Nuevo
                ps.setInt(3, (Integer) fila[1]);    // Código del Producto
                
                ps.addBatch(); // Agregamos a la cola de ejecución [Oracle Docs](https://docs.oracle.com)
                contador++;
            }
        }

        // 3. Si hay productos marcados, ejecutamos el lote
        if (contador > 0) {
            ps.executeBatch();
            conn.commit(); // Impactamos los cambios en el archivo .db
            mj.setCodigo(1);
            mj.setMensaje( "¡Éxito! Se actualizaron " + contador + " productos.");
         //  JOptionPane.showMessageDialog(null, "¡Éxito! Se actualizaron " + contador + " productos.");
        } else {
                mj.setCodigo(3);
            mj.setMensaje( "No hay productos seleccionados para guardar.");
           // JOptionPane.showMessageDialog(null, "No hay productos seleccionados para guardar.");
        }

    } catch (Exception e) {
             mj.setCodigo(4);
            mj.setMensaje( "Error al guardar en SQLite: " + e.getMessage());
       // JOptionPane.showMessageDialog(null, "Error al guardar en SQLite: " + e.getMessage());
    }
    return mj;
}
    
public void ordenarTablaProductos(JTable tabla, int indiceColumna, boolean deMayorAMenor) {
    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    int filas = modelo.getRowCount();
    if (filas == 0) return;

    List<Object[]> listaTemporal = new ArrayList<>();
    for (int i = 0; i < filas; i++) {
        Object[] fila = new Object[modelo.getColumnCount()];
        for (int j = 0; j < modelo.getColumnCount(); j++) {
            fila[j] = modelo.getValueAt(i, j);
        }
        listaTemporal.add(fila);
    }

    // Usamos Lambda ( -> ) para evitar el error de NoClassDefFoundError
    Collections.sort(listaTemporal, (o1, o2) -> {
        String v1 = (o1[indiceColumna] != null) ? o1[indiceColumna].toString().trim() : "";
        String v2 = (o2[indiceColumna] != null) ? o2[indiceColumna].toString().trim() : "";

        int resultado;
        try {
            // Caso Fechas
            if (v1.matches("\\d{2}/\\d{2}/\\d{4}.*")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date d1 = v1.isEmpty() ? new Date(0) : sdf.parse(v1);
                Date d2 = v2.isEmpty() ? new Date(0) : sdf.parse(v2);
                resultado = d1.compareTo(d2);
            } 
            // Caso Números/Moneda
            else if (v1.contains("$") || v1.matches("-?\\d+([.,]\\d+)?") || v1.contains(",")) {
                double n1 = limpiarNumero(v1);
                double n2 = limpiarNumero(v2);
                resultado = Double.compare(n1, n2);
            } 
            // Caso Texto
            else {
                resultado = v1.compareToIgnoreCase(v2);
            }
        } catch (Exception e) {
            resultado = v1.compareToIgnoreCase(v2);
        }

        return deMayorAMenor ? -resultado : resultado;
    });

    modelo.setRowCount(0);
    for (Object[] fila : listaTemporal) {
        modelo.addRow(fila);
    }
    tabla.repaint();
}

/**
 * Convierte formatos complejos como "$ 12.333,00" o "1.500,50" a double
 */
private double limpiarNumero(String texto) {
    if (texto == null || texto.isEmpty() || texto.equals("-")) return 0.0;
    try {
        // Quitamos $, espacios y puntos de miles (formato AR)
        String limpio = texto.replace("$", "").replace(" ", "");
        
        // Si hay puntos y comas, el punto es de miles y se elimina
        if (limpio.contains(".") && limpio.contains(",")) {
            limpio = limpio.replace(".", "");
        }
        
        // La coma decimal se pasa a punto para Double.parseDouble
        limpio = limpio.replace(",", ".");
        
        // Limpieza final de caracteres no numéricos residuales
        limpio = limpio.replaceAll("[^\\d.-]", "");
        
        return Double.parseDouble(limpio);
    } catch (Exception e) {
        return 0.0;
    }
}}