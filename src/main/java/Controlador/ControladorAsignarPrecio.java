/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelos.Mensaje;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ALCIDES
 */
public class ControladorAsignarPrecio {

    public void buscarProducto(JTextField datoBuscar, JTable listaSugerencias, JScrollPane contenedorTabla) {
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("descripcion");
        modelo.addColumn("id");

        Boolean numeroLetra = false;
        Integer numero;
        try {
            numero = Integer.parseInt(datoBuscar.getText());
            numeroLetra = true;
        } catch (Exception e) {
            numeroLetra = false;
        }

        try {

            String consulta = null;
            if (numeroLetra == false) {
                consulta = "select producto.codigoproducto, producto.descripcion,"
                        + " producto.precio_compra, producto.precio_venta,"
                        + " producto.stock, producto.proveedor,"
                        + " producto.rubro from producto where "
                        + "producto.descripcion like concat('%',?,'%') and  producto.activo!=FALSE;";

            } else {
                consulta = "select producto.codigoproducto, producto.descripcion,"
                        + " producto.precio_compra, producto.precio_venta, "
                        + "producto.stock, producto.proveedor, "
                        + "producto.rubro from producto where "
                        + "producto.codigoproducto like concat('%',?,'%') and  producto.activo!=FALSE;";

            }

            if (!datoBuscar.getText().equalsIgnoreCase("")) {

                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
                ps.setString(1, datoBuscar.getText());
                ResultSet rs = ps.executeQuery();

                          int contador = 0;            
              int tamaño= listaSugerencias.getRowHeight();    
               //     contador = contador + tamaño;
               //         contenedorTabla.setSize(170, contador+6);
                while (rs.next()) {
                    objetoProducto.setCodigo(rs.getInt("codigoproducto"));
                    objetoProducto.setDescripcion(rs.getString("descripcion"));
                    objetoProducto.setPrecioCompra(rs.getDouble("precio_compra"));
                    objetoProducto.setPrecioVenta(rs.getDouble("precio_venta"));
                    objetoProducto.setStock(rs.getDouble("stock"));
                    objetoProducto.setProveedor(rs.getString("proveedor"));
                    objetoProducto.setRubro(rs.getString("rubro"));
                    modelo.addRow(new Object[]{objetoProducto.getDescripcion(), objetoProducto.getCodigo()});
                  contador = contador + tamaño;
                }

//AQUI DIMENSIONO LAS SELECCIONES
                contenedorTabla.setVisible(true);
                listaSugerencias.setModel(modelo);
              //  listaSugerencias.setRowHeight(18);
              contenedorTabla.setSize(170, contador+6);

                listaSugerencias.getTableHeader().setPreferredSize(new Dimension(180, 0));
                listaSugerencias.setRowMargin(0);

                for (int i = 0; i < listaSugerencias.getColumnCount(); i++) {
                    Class<?> colClas = listaSugerencias.getColumnClass(i);
                    listaSugerencias.setDefaultEditor(colClas, null);
                }

                //oculto la segunda columna
                // TableColumnModel tcm= listaSugerencias.getTableHeader().getColumnModel();
                // tcm.removeColumn(tcm.getColumn(1));
                listaSugerencias.getColumn("id").setMinWidth(0);
                listaSugerencias.getColumn("id").setMaxWidth(0);
                listaSugerencias.getColumn("id").setWidth(0);
            }
//contenedorTabla
        } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "11 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }

    }

    public Boolean buscarCliente(JTextField codCliente, JTable listaSugerencias, JScrollPane contenedorTabla,int sinCF) {
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloCliente objetoCliente = new Modelos.ModeloCliente();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("descripcion");
        modelo.addColumn("id");
        Boolean returno = false;
        Boolean numeroLetra = false;
        Integer numero;
        try {
            numero = Integer.parseInt(codCliente.getText());
            numeroLetra = true;
        } catch (Exception e) {
            numeroLetra = false;
        }

        try {

            String consulta = null;
            if (numeroLetra == false) {
                consulta = "select cliente.codigocliente, cliente.nombre,cliente.telefono,cliente.cuit,"
                        + "cliente.direccion, cliente.saldo_acumulado,cliente.limite_compra from "
                        + "cliente where cliente.nombre like concat('%',?,'%') and cliente.activo!=FALSE ";
                     if (sinCF==1){
                      consulta=consulta+ "and cliente.codigocliente!=1";
                     }
                     
                  
 consulta=consulta+";";
            } else {
                consulta = "select cliente.codigocliente, cliente.nombre,cliente.telefono,cliente.cuit,"
                        + "cliente.direccion, cliente.saldo_acumulado,cliente.limite_compra"
                        + " from cliente  where cliente.codigocliente like "
                        + "concat('%',?,'%') and  cliente.activo!=FALSE ";
                                                if (sinCF==1){
                      consulta=consulta+ "and cliente.codigocliente!=1";
                     }
                  
 consulta=consulta+";";

            }
            System.out.println(consulta);

            if (!codCliente.getText().equalsIgnoreCase("")) {

                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
                ps.setString(1, codCliente.getText());
                ResultSet rs = ps.executeQuery();

                              int contador = 0;            
              int tamaño= listaSugerencias.getRowHeight();    
               //     contador = contador + tamaño;
               //         contenedorTabla.setSize(170, contador+6);
                
                while (rs.next()) {
                    returno = true;
                    objetoCliente.setCodigo(rs.getInt("codigocliente"));
                    objetoCliente.setNombre(rs.getString("nombre"));
                    objetoCliente.setTelefono(rs.getInt("telefono"));
                    objetoCliente.setCuitDni(rs.getInt("cuit"));
                    objetoCliente.setLimiteCompra(rs.getDouble("limite_compra"));
                    objetoCliente.setSaldoAcumulado(rs.getDouble("saldo_acumulado"));
                    objetoCliente.setDireccion(rs.getString("direccion"));

                    modelo.addRow(new Object[]{objetoCliente.getNombre(), objetoCliente.getCodigo()});
                   contador = contador + tamaño;
                }

//AQUI DIMENSIONO LAS SELECCIONES
                listaSugerencias.setModel(modelo);
           //     listaSugerencias.setRowHeight(18);
                 contenedorTabla.setSize(170, contador+6);

                listaSugerencias.getTableHeader().setPreferredSize(new Dimension(180, 0));
                listaSugerencias.setRowMargin(0);
//no modificar
                for (int i = 0; i < listaSugerencias.getColumnCount(); i++) {
                    Class<?> colClas = listaSugerencias.getColumnClass(i);
                    listaSugerencias.setDefaultEditor(colClas, null);
                }

                //oculto la segunda columna
                // TableColumnModel tcm= listaSugerencias.getTableHeader().getColumnModel();
                // tcm.removeColumn(tcm.getColumn(1));
                listaSugerencias.getColumn("id").setMinWidth(0);
                listaSugerencias.getColumn("id").setMaxWidth(0);
                listaSugerencias.getColumn("id").setWidth(0);
//contenedorTabla
            }
        } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "12 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
         } finally {
            objetoConexion.cerrarConexion();
        }
        contenedorTabla.setVisible(true);
        return returno;
    }

    public Mensaje SeleccionarCliente(JTable listaCliente, JTextField fieldNombre,
            JTextField fieldDireccion,
            JTextField codCliente, JScrollPane contenedorClientes,
            JTextField txtBuscarProducto,JLabel nombre, JLabel codigo, int verificador
    ) {
        int fila = listaCliente.getSelectedRow();

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();

        Integer idSelecte = 0;

        if (verificador == 1) {
            if (!codCliente.getText().equals("")) {

                try {
                    idSelecte = Integer.parseInt(codCliente.getText());
                    fila = 1;
                } catch (Exception e) {

                    codCliente.setText("");
                    limpiarCampos(fieldNombre,
                            fieldDireccion, codCliente,nombre,codigo);
                }

            } else {

                fila = 0;
                //    txtBuscarProducto.requestFocus();
                codCliente.setText("");
            }

        } else {
            idSelecte = Integer.parseInt(listaCliente.getValueAt(fila, 1).toString());
        }
        System.out.println(idSelecte);
        try {
            if (fila >= 0) {

                codCliente.setText(idSelecte.toString());

                String consulta = "select cliente.codigocliente, cliente.nombre,cliente.telefono,cliente.cuit,"
                        + "cliente.direccion, cliente.saldo_acumulado,"
                        + "cliente.limite_compra from cliente"
                        + "  where cliente.codigocliente =? and cliente.activo!=FALSE;";

                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);

                ps.setString(1, codCliente.getText());

                ResultSet rs = ps.executeQuery();

                if (!rs.isBeforeFirst()) {
                    codCliente.setText("");
                    limpiarCampos(fieldNombre,
                            fieldDireccion, codCliente,nombre,codigo);
                } else {

                    while (rs.next()) {
                        System.out.println("etralwaylie");
                        fieldNombre.setText(rs.getString("nombre"));

                        codCliente.setText(rs.getString("codigocliente"));
                        fieldNombre.setText(rs.getString("nombre"));

                        fieldDireccion.setText(rs.getString("direccion"));
nombre.setText(rs.getString("nombre"));
codigo.setText(rs.getString("codigocliente"));
                    }

                    contenedorClientes.setVisible(false);

                    txtBuscarProducto.requestFocus();
                }
            }
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, "13 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
        return null;
    }

    public Mensaje SeleccionarProductosVenta(JTable listaProducto,
            JTextField codigoCliente, JTextField codigo,
            JTextField fieldPrecio, JTextField Costo, JTextField Anterior,
            JTextField txtBuscarProducto, JScrollPane contenedorTabla, int verificador) {
        int fila = listaProducto.getSelectedRow();
        Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();
        DefaultTableModel modelo = new DefaultTableModel();
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();

        Integer idSelecte = 0;

        if (verificador == 1) {
            if (!txtBuscarProducto.getText().equals("")) {

                try {

                    idSelecte = Integer.parseInt(txtBuscarProducto.getText());
                    fila = 1;
                } catch (Exception e) {
                    fieldPrecio.setText("");
                    Costo.setText("");
                    Anterior.setText("");
                    codigo.setText("");
                    txtBuscarProducto.setText("");

                }

            }
        } else {
            if (verificador == 2) {
                idSelecte = Integer.parseInt(codigo.getText());
            } else {
                idSelecte = Integer.parseInt(listaProducto.getValueAt(fila, 1).toString());
            }

        }

        try {
            if (fila >= 0) {
                codigo.setText(idSelecte.toString());
                String consulta = "select producto.codigoproducto, producto.descripcion, producto.precio_compra,"
                        + " producto.precio_venta, producto.stock, "
                        + " producto.proveedor, producto.rubro, preciocliente.diferido, preciocliente.costoanterior from preciocliente inner join  producto "
                        + " where preciocliente.fkcliente = ? and producto.codigoproducto=? and preciocliente.fkProducto=?";

                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);

                ps.setString(1, codigoCliente.getText());
                ps.setString(2, idSelecte.toString());
                ps.setString(3, idSelecte.toString());

                ResultSet rs = ps.executeQuery();
                Double diferido = null;
                Double prCosto = null;
                String descripcion = null;
                if (rs.next()) {

                    diferido = rs.getDouble("diferido");
                    prCosto = rs.getDouble("precio_compra");
                    descripcion = rs.getString("descripcion");
                    Costo.setText(formatoMoneda.format(prCosto));
                    Anterior.setText(formatoMoneda.format(diferido));
                    txtBuscarProducto.setText(descripcion);
                    contenedorTabla.setVisible(false);
                    fieldPrecio.requestFocus();
                    diferido = null;
                    prCosto = null;
                    descripcion = null;
                    System.out.println("    entra al primer rs");
                } else {

                    consulta = "select producto.codigoproducto, producto.descripcion, producto.precio_compra,"
                            + " producto.precio_venta, producto.stock, "
                            + " producto.proveedor, producto.rubro from producto "
                            + " where codigoproducto=? and  producto.activo!=FALSE;";
                    ps = objetoConexion.estableceConexion().prepareStatement(consulta);

                    ps.setString(1, codigo.getText());
                    rs = ps.executeQuery();
                    if (!rs.isBeforeFirst()) {
                        fieldPrecio.setText("");
                        Costo.setText("");
                        Anterior.setText("");
                        codigo.setText("");
                        txtBuscarProducto.setText("");
                    } else {
                        while (rs.next()) {
                            diferido = rs.getDouble("precio_venta");
                            prCosto = rs.getDouble("precio_compra");
                            descripcion = rs.getString("descripcion");
                            Costo.setText(formatoMoneda.format(prCosto));
                            Anterior.setText(formatoMoneda.format(diferido));
                            txtBuscarProducto.setText(descripcion);
                            contenedorTabla.setVisible(false);
                            fieldPrecio.requestFocus();
                            diferido = null;
                            prCosto = null;
                            descripcion = null;
                        }
                    }
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "14 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
       } finally {
            objetoConexion.cerrarConexion();
        }
        return null;
    }

    public Mensaje editarProductoSeleccionado(JTable tbResumenVenta,
            JTextField codigoProducto, JTextField txtBuscarProducto,
            JTextField prCosto, JTextField prVenta, JTextField codCliente,
            JScrollPane contenedorTabla, JTextField prAnterior) {
        Mensaje mj = new Mensaje();
        int indiceSelleccionado = tbResumenVenta.getSelectedRow();

        if (indiceSelleccionado != -1) {
            Integer idSelecte = Integer.parseInt(tbResumenVenta.getValueAt(indiceSelleccionado, 0).toString());

            codigoProducto.setText(idSelecte.toString());
            prVenta.setText(tbResumenVenta.getValueAt(indiceSelleccionado, 3).toString());

            SeleccionarProductosVenta(tbResumenVenta, codCliente,
                    codigoProducto, prVenta, prCosto,
                    prAnterior, txtBuscarProducto, contenedorTabla, 2);
            mj.setCodigo(indiceSelleccionado);

            return mj;
        } else {
            //    JOptionPane.showMessageDialog(null, "seleccione una fila para eliminar");
            mj.setCodigo(2);
            mj.setMensaje("Seleccione una fila para editar");
            return mj;
        }

    }

   public Mensaje pasarPreciosGuardar(JTable tbResumenVenta, JTextField codigoProducto, JTextField descripcion,
                                   JTextField costo, JTextField venta, JTextField fieldAnterior, JTextField buscar) {
    Mensaje mj = null;
    DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
    String codProducto = codigoProducto.getText();

    // 1. Validar duplicados en la tabla
    for (int i = 0; i < modelo.getRowCount(); i++) {
        if (modelo.getValueAt(i, 0).toString().equals(codProducto)) {
            mj = new Mensaje(2, "El producto ya está en la lista.");
            buscar.setText("");
            buscar.requestFocus();
            return mj;
        }
    }

    try {
        // 2. Limpieza de montos (Regex: solo números y coma)
        String textoVenta = venta.getText().replaceAll("[^0-9,]", "").replace(",", ".");
        String textoCosto = costo.getText().replaceAll("[^0-9,]", "").replace(",", ".");

        if (textoVenta.isEmpty()) textoVenta = "0";
        if (textoCosto.isEmpty()) textoCosto = "0";

        BigDecimal ventabig = new BigDecimal(textoVenta);
        BigDecimal costobig = new BigDecimal(textoCosto);
        BigDecimal diferencia1 = ventabig.subtract(costobig);

        // 3. Formateo de moneda para la tabla
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        String ventaSet = formatoMoneda.format(ventabig);
        String diferencia = formatoMoneda.format(diferencia1);

        // 4. Cálculo de Margen con protección de División por Cero
        String margen;
        if (ventabig.compareTo(BigDecimal.ZERO) <= 0) {
            margen = (costobig.compareTo(BigDecimal.ZERO) > 0) ? "ERROR" : "0.00 %";
        } else {
            // (Diferencia * 100) / Venta
            BigDecimal margen1 = diferencia1.multiply(new BigDecimal("100"))
                                           .divide(ventabig, 2, RoundingMode.HALF_UP);
            
            margen = (margen1.compareTo(BigDecimal.ZERO) < 0) ? "PÉRDIDA" : margen1 + " %";
        }

        // 5. Agregar fila y limpiar interfaz
        modelo.addRow(new Object[]{codProducto, descripcion.getText(), costo.getText(), ventaSet, diferencia, margen});
        
        limpiarCampos(codigoProducto, descripcion, costo, venta, fieldAnterior, buscar);

    } catch (Exception e) {
        mj = new Mensaje(4, "Error en formato: " + e.getMessage());
    }
    return mj;
}

private void limpiarCampos(JTextField... fields) {
    for (JTextField f : fields) f.setText("");
    fields[fields.length - 1].requestFocus(); // El último es 'buscar'
}

    public void eliminarProductoSeleccionado(JTable tbResumenVenta) {

        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();

        try {
            int indiceSelleccionado = tbResumenVenta.getSelectedRow();
            if (indiceSelleccionado != -1) {
                modelo.removeRow(indiceSelleccionado);
            } else {
                JOptionPane.showMessageDialog(null, "seleccione una fila para eliminar");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error a seleccionar " + e.toString());
        }

    }

    public Mensaje guardarDatos(JTable tbResumenVenta, Integer codigoCliente) {
Mensaje mj=null;
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        LocalDateTime hoy = LocalDateTime.now();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha = hoy.format(formater);

        int codCliente = codigoCliente;

        String consulta = "insert into preciocliente"
                + "(fkcliente, fkProducto,diferido,costoanterior,fechamodificacion) values (?,?,?,?,?)";

        Integer codigoLinea = null;
// new BigDecimal(prVenta.substring(2).replace(".", "").replace(",", "."));

        try {

            int filas = tbResumenVenta.getRowCount();
            System.out.println("    es el numero de filas "+filas);
            for (int i = 0; i < filas; i++) {
                int idProducto = Integer.parseInt(tbResumenVenta.getValueAt(i, 0).toString());
                  System.out.println("    sale del for");
                double diferido = Double.parseDouble(tbResumenVenta.getValueAt(i, 3).toString().substring(2).replace(".", "").replace(",", "."));
                double costoAnterior = Double.parseDouble(tbResumenVenta.getValueAt(i, 2).toString().substring(2).replace(".", "").replace(",", "."));

                try {
                    String consulta1 = "select codigo from preciocliente where fkcliente=? and fkProducto=?;";
                    PreparedStatement ps2 = objetoConexion.estableceConexion().prepareStatement(consulta1);
                    ps2.setInt(1, codCliente);
                    ps2.setInt(2, idProducto);
                    ResultSet rs1 = ps2.executeQuery();

                    while (rs1.next()) {
                        codigoLinea = rs1.getInt("codigo");
                    }

                    if (codigoLinea != null) {

                        consulta = "Update preciocliente SET diferido=?,costoanterior=?,"
                                + "fechamodificacion=? where codigo=?";

                        PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
                        ps.setDouble(1, diferido);
                        ps.setDouble(2, costoAnterior);
                        ps.setString(3, fecha);
                        ps.setInt(4, codigoLinea);

                        ps.executeUpdate();
                        codigoLinea = null;
                    } else {
consulta = "insert into preciocliente"
                + "(fkcliente, fkProducto,diferido,costoanterior,fechamodificacion) values (?,?,?,?,?)";
                        PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
                        ps.setInt(1, codCliente);
                        ps.setInt(2, idProducto);
                        ps.setDouble(3, diferido);
                        ps.setDouble(4, costoAnterior);
                        System.out.println("    esta es la consulta 5");
                        ps.setString(5, fecha);
                          System.out.println("    esta es la consulta 6");
                        ps.executeUpdate();

                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "error en primera linea" + e.toString());
                } 

            }
          
            mj=new Mensaje();
            mj.setCodigo(1);
            mj.setMensaje("precios actualizados Correctamente");
           // JOptionPane.showMessageDialog(null, );
        } catch (Exception e) {
              mj=new Mensaje();
              mj.setCodigo(4);
            mj.setMensaje("error al Guardar " + e.toString());
           // JOptionPane.showMessageDialog(null, "error al Guardar " + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
     //   DefaultTableModel modeloTabla = (DefaultTableModel) tbResumenVenta.getModel();
    //    modeloTabla.setRowCount(0);
        //  PreparedStatement ps=objetoConexion.estableceConexion().prepareStatement(consulta);
   return mj;
    }

  public Mensaje ActualizarUnStock(JTextField texfieldStock, JTextField codigoProducto) {
    Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
    Connection con = objetoConexion.estableceConexion(); // Obtenemos la conexión una sola vez
    Mensaje mj = new Mensaje();

    // SQL compatible con SQLite (Sin el prefijo 'frigorifico.')
    String consulta1 = "UPDATE producto SET stock = ? WHERE codigoproducto = ?;";

    try {
        // Validamos que los campos no estén vacíos antes de parsear
        if (texfieldStock.getText().isEmpty() || codigoProducto.getText().isEmpty()) {
            mj.setCodigo(4);
            mj.setMensaje("Error: Los campos de stock o código están vacíos.");
            return mj;
        }

        PreparedStatement ps2 = con.prepareStatement(consulta1);
        
        // Usamos Double para el stock y Int para el código
        ps2.setDouble(1, Double.parseDouble(texfieldStock.getText().replace(",", ".")));
        ps2.setInt(2, Integer.parseInt(codigoProducto.getText()));
        
        int filasAfectadas = ps2.executeUpdate();

        if (filasAfectadas > 0) {
            mj.setCodigo(1);
            mj.setMensaje("Se actualizó el stock correctamente.");
        } else {
            mj.setCodigo(4);
            mj.setMensaje("No se encontró el producto con el código especificado.");
        }
        
    } catch (NumberFormatException e) {
        mj.setCodigo(4);
        mj.setMensaje("Error: Formato de número inválido.");
    } catch (Exception e) {
        mj.setCodigo(4);
        mj.setMensaje("Error al actualizar: " + e.getMessage());
    } finally {
        objetoConexion.cerrarConexion();
    }
    
    return mj;
}

    public Mensaje ActualizarUnPrecio(JTextField precio, JTextField costo,
            JLabel codigoCliente, JTextField codigoProducto) {
        Mensaje mj = null;
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        LocalDateTime hoy = LocalDateTime.now();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha = hoy.format(formater);
        Integer codigoLinea = null;
        int idProducto = Integer.parseInt(codigoProducto.getText());
        double diferido = Double.parseDouble(precio.getText().substring(2).replace(".", "").replace(",", "."));
        double costoAnterior = Double.parseDouble(costo.getText().substring(2).replace(".", "").replace(",", "."));

        if (codigoCliente.getText().equals("")) {
            try {
                String consulta1 = "UPDATE frigorifico.producto SET precio_venta = ? WHERE (codigoproducto= ?);";
                PreparedStatement ps2 = objetoConexion.estableceConexion().prepareStatement(consulta1);
                ps2.setDouble(1, diferido);
                ps2.setInt(2, idProducto);
                ps2.executeUpdate();
                mj = new Mensaje();
                mj.setCodigo(1);
                mj.setMensaje("precios actualizados Correctamente");
                ///  JOptionPane.showMessageDialog(null, "precios actualizados Correctamente");
            } catch (Exception e) {
                mj = new Mensaje();
                mj.setCodigo(4);
                mj.setMensaje("error al Guardar contacta al administrador" + e.toString());
                // JOptionPane.showMessageDialog(null, "error al Guardar " + e.toString());
            } finally {
                objetoConexion.cerrarConexion();
            }

        } else {
            int codCliente = Integer.parseInt(codigoCliente.getText());

            String consulta = "insert into preciocliente"
                    + "(fkcliente, fkProducto,diferido,costoanterior,fechamodificacion) values (?,?,?,?,?)";

            try {
                String consulta1 = "select codigo from preciocliente where fkcliente=? and fkProducto=?;";
                PreparedStatement ps2 = objetoConexion.estableceConexion().prepareStatement(consulta1);
                ps2.setInt(1, codCliente);
                ps2.setInt(2, idProducto);
                ResultSet rs1 = ps2.executeQuery();

                while (rs1.next()) {
                    codigoLinea = rs1.getInt("codigo");
                }

                if (codigoLinea != null) {

                    consulta = "Update preciocliente SET diferido=?,costoanterior=?,fechamodificacion=? where codigo=?";

                    PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
                    ps.setDouble(1, diferido);
                    ps.setDouble(2, costoAnterior);
                    ps.setString(3, fecha);
                    ps.setInt(4, codigoLinea);

                    ps.executeUpdate();
                    codigoLinea = null;
                } else {

                    PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
                    ps.setInt(1, codCliente);
                    ps.setInt(2, idProducto);
                    ps.setDouble(3, diferido);
                    ps.setDouble(4, costoAnterior);
                    ps.setString(5, fecha);
                    ps.executeUpdate();

                }

                mj = new Mensaje();
                mj.setCodigo(1);
                mj.setMensaje("precios actualizados Correctamente");
                ///  JOptionPane.showMessageDialog(null, "precios actualizados Correctamente");
            } catch (Exception e) {
                mj = new Mensaje();
                mj.setCodigo(4);
                mj.setMensaje("error al Guardar contacta al administrador" + e.toString());
                // JOptionPane.showMessageDialog(null, "error al Guardar " + e.toString());
            } finally {
                objetoConexion.cerrarConexion();
            }

        }
        return mj;
    }

    public void limpiarCampos(JTextField fieldNombre, JTextField fieldDireccion, JTextField codigoCliente
    ,JLabel nombre, JLabel codigo) {
        fieldDireccion.setText("");
        codigoCliente.setText("");
        fieldNombre.setText("");
        nombre.setText("----");
        codigo.setText("----");
    }
}
