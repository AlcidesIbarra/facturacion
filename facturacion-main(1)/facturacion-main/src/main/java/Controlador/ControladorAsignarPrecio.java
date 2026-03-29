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
import configuracion.Conexion;

/**
 *
 * @author ALCIDES
 */
public class ControladorAsignarPrecio {

   public void buscarProducto(JTextField datoBuscar, JTable listaSugerencias, JScrollPane contenedorTabla) {

    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("descripcion");
    modelo.addColumn("id");

    boolean esNumero;
    try {
        Integer.parseInt(datoBuscar.getText());
        esNumero = true;
    } catch (Exception e) {
        esNumero = false;
    }

    String consulta;

    if (!esNumero) {
        consulta = "SELECT codigoproducto, descripcion, precio_compra, precio_venta, stock, proveedor, rubro "
                + "FROM producto WHERE descripcion LIKE ? AND activo != FALSE;";
    } else {
        consulta = "SELECT codigoproducto, descripcion, precio_compra, precio_venta, stock, proveedor, rubro "
                + "FROM producto WHERE codigoproducto LIKE ? AND activo != FALSE;";
    }

    // 🔥 TRY WITH RESOURCES (maneja conexión automáticamente)
    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(consulta)) {

        String texto = datoBuscar.getText().trim();

        if (!texto.isEmpty()) {

            ps.setString(1, "%" + texto + "%");

            try (ResultSet rs = ps.executeQuery()) {

                int contador = 0;
                int tamaño = listaSugerencias.getRowHeight();

                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getString("descripcion"),
                        rs.getInt("codigoproducto")
                    });
                    contador += tamaño;
                }

                listaSugerencias.setModel(modelo);
                contenedorTabla.setVisible(true);
                contenedorTabla.setSize(170, contador + 6);

                listaSugerencias.getTableHeader().setPreferredSize(new Dimension(180, 0));
                listaSugerencias.setRowMargin(0);

                // ❌ Desactivar edición
                for (int i = 0; i < listaSugerencias.getColumnCount(); i++) {
                    listaSugerencias.setDefaultEditor(
                        listaSugerencias.getColumnClass(i), null
                    );
                }

                // 👻 Ocultar columna ID
                listaSugerencias.getColumn("id").setMinWidth(0);
                listaSugerencias.getColumn("id").setMaxWidth(0);
                listaSugerencias.getColumn("id").setWidth(0);
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.toString());
    }
}

public Boolean buscarCliente(JTextField codCliente, JTable listaSugerencias, JScrollPane contenedorTabla, int sinCF) {

    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("descripcion");
    modelo.addColumn("id");

    boolean retorno = false;

    boolean esNumero;
    try {
        Integer.parseInt(codCliente.getText());
        esNumero = true;
    } catch (Exception e) {
        esNumero = false;
    }

    String consulta;

    if (!esNumero) {
        consulta = "SELECT codigocliente, nombre, telefono, cuit, direccion, saldo_acumulado, limite_compra " +
                   "FROM cliente WHERE nombre LIKE ? AND activo != FALSE ";
    } else {
        consulta = "SELECT codigocliente, nombre, telefono, cuit, direccion, saldo_acumulado, limite_compra " +
                   "FROM cliente WHERE codigocliente LIKE ? AND activo != FALSE ";
    }

    if (sinCF == 1) {
        consulta += "AND codigocliente != 1 ";
    }

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(consulta)) {

        String texto = codCliente.getText().trim();

        if (!texto.isEmpty()) {

            ps.setString(1, "%" + texto + "%");

            try (ResultSet rs = ps.executeQuery()) {

                int contador = 0;
                int tamaño = listaSugerencias.getRowHeight();

                while (rs.next()) {
                    retorno = true;

                    modelo.addRow(new Object[]{
                        rs.getString("nombre"),
                        rs.getInt("codigocliente")
                    });

                    contador += tamaño;
                }

                listaSugerencias.setModel(modelo);
                contenedorTabla.setVisible(true);
                contenedorTabla.setSize(170, contador + 6);

                listaSugerencias.getTableHeader().setPreferredSize(new Dimension(180, 0));
                listaSugerencias.setRowMargin(0);

                // ❌ Desactivar edición
                for (int i = 0; i < listaSugerencias.getColumnCount(); i++) {
                    listaSugerencias.setDefaultEditor(
                        listaSugerencias.getColumnClass(i), null
                    );
                }

                // 👻 Ocultar columna ID
                listaSugerencias.getColumn("id").setMinWidth(0);
                listaSugerencias.getColumn("id").setMaxWidth(0);
                listaSugerencias.getColumn("id").setWidth(0);
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.toString());
    }

    return retorno;
}

public Mensaje SeleccionarCliente(JTable listaCliente, JTextField fieldNombre,
        JTextField fieldDireccion,
        JTextField codCliente, JScrollPane contenedorClientes,
        JTextField txtBuscarProducto, JLabel nombre, JLabel codigo, int verificador) {

    Mensaje mj = new Mensaje();
    int fila = listaCliente.getSelectedRow();
    Integer idSelecte = null;

    try {

        // 🔍 Determinar ID
        if (verificador == 1) {
            if (!codCliente.getText().trim().isEmpty()) {
                try {
                    idSelecte = Integer.parseInt(codCliente.getText().trim());
                } catch (Exception e) {
                    codCliente.setText("");
                    limpiarCampos(fieldNombre, fieldDireccion, codCliente, nombre, codigo);
                    mj.setCodigo(2);
                    mj.setMensaje("Código inválido");
                    return mj;
                }
            } else {
                limpiarCampos(fieldNombre, fieldDireccion, codCliente, nombre, codigo);
                mj.setCodigo(2);
                mj.setMensaje("Ingrese un cliente");
                return mj;
            }
        } else {
            if (fila < 0) {
                mj.setCodigo(2);
                mj.setMensaje("Seleccione un cliente");
                return mj;
            }
            idSelecte = Integer.parseInt(listaCliente.getValueAt(fila, 1).toString());
        }

        String consulta = "SELECT codigocliente, nombre, telefono, cuit, direccion, saldo_acumulado, limite_compra "
                + "FROM cliente WHERE codigocliente = ? AND activo != FALSE;";

        // 🔥 CONEXIÓN CORRECTA
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(consulta)) {

            ps.setInt(1, idSelecte);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    limpiarCampos(fieldNombre, fieldDireccion, codCliente, nombre, codigo);
                    mj.setCodigo(2);
                    mj.setMensaje("Cliente no encontrado");
                    return mj;
                }

                // ✅ Cargar datos
                fieldNombre.setText(rs.getString("nombre"));
                fieldDireccion.setText(rs.getString("direccion"));
                codCliente.setText(rs.getString("codigocliente"));

                nombre.setText(rs.getString("nombre"));
                codigo.setText(rs.getString("codigocliente"));

                contenedorClientes.setVisible(false);
                txtBuscarProducto.requestFocus();

                mj.setCodigo(1);
                mj.setMensaje("Cliente cargado correctamente");
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.toString());
        mj.setCodigo(4);
        mj.setMensaje("Error al seleccionar cliente");
    }

    return mj;
}

public Mensaje SeleccionarProductosVenta(JTable listaProducto,
        JTextField codigoCliente, JTextField codigo,
        JTextField fieldPrecio, JTextField Costo, JTextField Anterior,
        JTextField txtBuscarProducto, JScrollPane contenedorTabla, int verificador) {

    Mensaje mj = new Mensaje();
    int fila = listaProducto.getSelectedRow();
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    Integer idSelecte = null;

    try {
        // 🔍 Determinar ID
        if (verificador == 1) {
            if (!txtBuscarProducto.getText().trim().isEmpty()) {
                try {
                    idSelecte = Integer.parseInt(txtBuscarProducto.getText().trim());
                } catch (Exception e) {
                    limpiarCampos(codigo, txtBuscarProducto, Costo, Anterior, fieldPrecio);
                    mj.setCodigo(2);
                    mj.setMensaje("Código inválido");
                    return mj;
                }
            }
        } else if (verificador == 2) {
            idSelecte = Integer.parseInt(codigo.getText());
        } else {
            if (fila < 0) {
                mj.setCodigo(2);
                mj.setMensaje("Seleccione un producto");
                return mj;
            }
            idSelecte = Integer.parseInt(listaProducto.getValueAt(fila, 1).toString());
        }

        if (idSelecte == null) {
            mj.setCodigo(2);
            mj.setMensaje("Producto inválido");
            return mj;
        }

        codigo.setText(idSelecte.toString());

        // 🔥 CONEXIÓN CORRECTA
        try (Connection conn = Conexion.getConexion()) {

            // 🔹 1. Buscar precio por cliente
            String consulta = "SELECT p.codigoproducto, p.descripcion, p.precio_compra, p.precio_venta, " +
                    "pc.diferido, pc.costoanterior " +
                    "FROM producto p " +
                    "INNER JOIN preciocliente pc ON p.codigoproducto = pc.fkProducto " +
                    "WHERE pc.fkcliente = ? AND p.codigoproducto = ?;";

            try (PreparedStatement ps = conn.prepareStatement(consulta)) {

                ps.setInt(1, Integer.parseInt(codigoCliente.getText()));
                ps.setInt(2, idSelecte);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        double diferido = rs.getDouble("diferido");
                        double costo = rs.getDouble("precio_compra");
                        String descripcion = rs.getString("descripcion");

                        Costo.setText(formatoMoneda.format(costo));
                        Anterior.setText(formatoMoneda.format(diferido));
                        txtBuscarProducto.setText(descripcion);

                        contenedorTabla.setVisible(false);
                        fieldPrecio.requestFocus();

                        mj.setCodigo(1);
                        return mj;
                    }
                }
            }

            // 🔹 2. Si no existe precio por cliente → buscar producto normal
            consulta = "SELECT codigoproducto, descripcion, precio_compra, precio_venta " +
                       "FROM producto WHERE codigoproducto = ? AND activo != FALSE;";

            try (PreparedStatement ps = conn.prepareStatement(consulta)) {

                ps.setInt(1, idSelecte);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        limpiarCampos(codigo, txtBuscarProducto, Costo, Anterior, fieldPrecio);
                        mj.setCodigo(2);
                        mj.setMensaje("Producto no encontrado");
                        return mj;
                    }

                    double venta = rs.getDouble("precio_venta");
                    double costo = rs.getDouble("precio_compra");
                    String descripcion = rs.getString("descripcion");

                    Costo.setText(formatoMoneda.format(costo));
                    Anterior.setText(formatoMoneda.format(venta));
                    txtBuscarProducto.setText(descripcion);

                    contenedorTabla.setVisible(false);
                    fieldPrecio.requestFocus();

                    mj.setCodigo(1);
                }
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.toString());
        mj.setCodigo(4);
        mj.setMensaje("Error al seleccionar producto");
    }

    return mj;
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

    Mensaje mj = new Mensaje();

    String sqlSelect = "SELECT codigo FROM preciocliente WHERE fkcliente=? AND fkProducto=?";
    String sqlInsert = "INSERT INTO preciocliente (fkcliente, fkProducto, diferido, costoanterior, fechamodificacion) VALUES (?,?,?,?,?)";
    String sqlUpdate = "UPDATE preciocliente SET diferido=?, costoanterior=?, fechamodificacion=? WHERE codigo=?";

    LocalDateTime hoy = LocalDateTime.now();
    DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String fecha = hoy.format(formater);

    try (Connection conn = Conexion.getConexion()) {

        int filas = tbResumenVenta.getRowCount();

        for (int i = 0; i < filas; i++) {

            int idProducto = Integer.parseInt(tbResumenVenta.getValueAt(i, 0).toString());

            double diferido = Double.parseDouble(
                    tbResumenVenta.getValueAt(i, 3).toString()
                            .substring(2).replace(".", "").replace(",", ".")
            );

            double costoAnterior = Double.parseDouble(
                    tbResumenVenta.getValueAt(i, 2).toString()
                            .substring(2).replace(".", "").replace(",", ".")
            );

            Integer codigoLinea = null;

            // 🔎 BUSCAR SI YA EXISTE
            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
                psSelect.setInt(1, codigoCliente);
                psSelect.setInt(2, idProducto);

                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        codigoLinea = rs.getInt("codigo");
                    }
                }
            }

            // 🔄 UPDATE o INSERT
            if (codigoLinea != null) {

                try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                    psUpdate.setDouble(1, diferido);
                    psUpdate.setDouble(2, costoAnterior);
                    psUpdate.setString(3, fecha);
                    psUpdate.setInt(4, codigoLinea);
                    psUpdate.executeUpdate();
                }

            } else {

                try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                    psInsert.setInt(1, codigoCliente);
                    psInsert.setInt(2, idProducto);
                    psInsert.setDouble(3, diferido);
                    psInsert.setDouble(4, costoAnterior);
                    psInsert.setString(5, fecha);
                    psInsert.executeUpdate();
                }
            }
        }

        mj.setCodigo(1);
        mj.setMensaje("Precios actualizados correctamente");

    } catch (Exception e) {
        mj.setCodigo(4);
        mj.setMensaje("Error al guardar: " + e.getMessage());
        JOptionPane.showMessageDialog(null, mj.getMensaje());
    }

    return mj;
}

  public Mensaje ActualizarUnStock(JTextField texfieldStock, JTextField codigoProducto) {

    Mensaje mj = new Mensaje();
    String sql = "UPDATE producto SET stock = ? WHERE codigoproducto = ?;";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        // Validación
        if (texfieldStock.getText().trim().isEmpty() || codigoProducto.getText().trim().isEmpty()) {
            mj.setCodigo(4);
            mj.setMensaje("Error: Campos vacíos.");
            return mj;
        }

        double stock = Double.parseDouble(texfieldStock.getText().replace(",", "."));
        int codigo = Integer.parseInt(codigoProducto.getText());

        ps.setDouble(1, stock);
        ps.setInt(2, codigo);

        int filas = ps.executeUpdate();

        if (filas > 0) {
            mj.setCodigo(1);
            mj.setMensaje("Stock actualizado correctamente.");
        } else {
            mj.setCodigo(4);
            mj.setMensaje("Producto no encontrado.");
        }

    } catch (NumberFormatException e) {
        mj.setCodigo(4);
        mj.setMensaje("Formato numérico inválido.");
    } catch (Exception e) {
        mj.setCodigo(4);
        mj.setMensaje("Error: " + e.getMessage());
    }

    return mj;
}

  public Mensaje ActualizarUnPrecio(JTextField precio, JTextField costo,
        JLabel codigoCliente, JTextField codigoProducto) {

    Mensaje mj = new Mensaje();

    LocalDateTime hoy = LocalDateTime.now();
    DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String fecha = hoy.format(formater);

    try (Connection conn = Conexion.getConexion()) {

        int idProducto = Integer.parseInt(codigoProducto.getText());

        double diferido = Double.parseDouble(
                precio.getText().substring(2).replace(".", "").replace(",", ".")
        );

        double costoAnterior = Double.parseDouble(
                costo.getText().substring(2).replace(".", "").replace(",", ".")
        );

        // 👉 SIN CLIENTE (precio general)
        if (codigoCliente.getText().trim().isEmpty()) {

            String sql = "UPDATE producto SET precio_venta = ? WHERE codigoproducto = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDouble(1, diferido);
                ps.setInt(2, idProducto);
                ps.executeUpdate();
            }

        } else {

            int codCliente = Integer.parseInt(codigoCliente.getText());
            Integer codigoLinea = null;

            String sqlSelect = "SELECT codigo FROM preciocliente WHERE fkcliente=? AND fkProducto=?";
            String sqlUpdate = "UPDATE preciocliente SET diferido=?, costoanterior=?, fechamodificacion=? WHERE codigo=?";
            String sqlInsert = "INSERT INTO preciocliente (fkcliente, fkProducto, diferido, costoanterior, fechamodificacion) VALUES (?,?,?,?,?)";

            // 🔎 BUSCAR SI EXISTE
            try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
                ps.setInt(1, codCliente);
                ps.setInt(2, idProducto);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        codigoLinea = rs.getInt("codigo");
                    }
                }
            }

            // 🔄 UPDATE o INSERT
            if (codigoLinea != null) {

                try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                    ps.setDouble(1, diferido);
                    ps.setDouble(2, costoAnterior);
                    ps.setString(3, fecha);
                    ps.setInt(4, codigoLinea);
                    ps.executeUpdate();
                }

            } else {

                try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                    ps.setInt(1, codCliente);
                    ps.setInt(2, idProducto);
                    ps.setDouble(3, diferido);
                    ps.setDouble(4, costoAnterior);
                    ps.setString(5, fecha);
                    ps.executeUpdate();
                }
            }
        }

        mj.setCodigo(1);
        mj.setMensaje("Precio actualizado correctamente.");

    } catch (Exception e) {
        mj.setCodigo(4);
        mj.setMensaje("Error: " + e.getMessage());
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
