/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Formularios.FormClientes;
import Formularios.FormVenta;

import Modelos.Mensaje;
import Modelos.ModeloCliente;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import configuracion.Conexion;
import java.sql.Types;
import java.sql.SQLException;

/**
 *
 * @author ALCIDES
 */
public class ControladorCliente {

public boolean buscarCliente(JTextField codCliente, JTable listaSugerencias, JScrollPane contenedorTabla) {

    boolean retorno = false;

    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("descripcion");
    modelo.addColumn("id");

    boolean esNumero;
    try {
        Integer.parseInt(codCliente.getText());
        esNumero = true;
    } catch (Exception e) {
        esNumero = false;
    }

    String consulta;

    if (!esNumero) {
        consulta = "SELECT codigocliente, nombre, telefono, cuit, direccion, diaspago, saldo_acumulado, limite_compra "
                + "FROM cliente WHERE nombre LIKE ? AND activo != FALSE";
    } else {
        consulta = "SELECT codigocliente, nombre, telefono, cuit, direccion, diaspago, saldo_acumulado, limite_compra "
                + "FROM cliente WHERE codigocliente LIKE ? AND activo != FALSE";
    }

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(consulta)) {

        if (!codCliente.getText().trim().isEmpty()) {

            ps.setString(1, "%" + codCliente.getText().trim() + "%");

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
                contenedorTabla.setSize(170, contador + 6);
                contenedorTabla.setVisible(true);

                contenedorTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

                listaSugerencias.getTableHeader().setPreferredSize(new Dimension(180, 0));
                listaSugerencias.setRowMargin(0);

                // Deshabilitar edición
                for (int i = 0; i < listaSugerencias.getColumnCount(); i++) {
                    listaSugerencias.setDefaultEditor(listaSugerencias.getColumnClass(i), null);
                }

                // Ocultar columna ID
                listaSugerencias.getColumn("id").setMinWidth(0);
                listaSugerencias.getColumn("id").setMaxWidth(0);
                listaSugerencias.getColumn("id").setWidth(0);
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
    }

    return retorno;
}

   public boolean buscarClientesinCF(JTextField codCliente, JTable listaSugerencias,
        JScrollPane contenedorTabla, String activo) {

    boolean retorno = false;

    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("descripcion");
    modelo.addColumn("id");

    // 🔹 Determinar estado activo/inactivo
    boolean act;
    switch (activo) {
        case "INACTIVOS":
            act = true;
            break;
        default: // "ACTIVOS"
            act = false;
            break;
    }

    // 🔹 Detectar si es número
    boolean esNumero;
    try {
        Integer.parseInt(codCliente.getText());
        esNumero = true;
    } catch (Exception e) {
        esNumero = false;
    }

    String consulta;

    if (!esNumero) {
        consulta = "SELECT codigocliente, nombre, telefono, cuit, direccion, diaspago, saldo_acumulado, limite_compra "
                + "FROM cliente WHERE nombre LIKE ? AND activo != ? AND codigocliente != 1";
    } else {
        consulta = "SELECT codigocliente, nombre, telefono, cuit, direccion, diaspago, saldo_acumulado, limite_compra "
                + "FROM cliente WHERE codigocliente LIKE ? AND activo != ? AND codigocliente != 1";
    }

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(consulta)) {

        if (!codCliente.getText().trim().isEmpty()) {

            ps.setString(1, "%" + codCliente.getText().trim() + "%");
            ps.setBoolean(2, act);

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

                // 🔹 UI
                listaSugerencias.setModel(modelo);
                contenedorTabla.setSize(170, contador + 6);
                contenedorTabla.setVisible(true);
                contenedorTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

                listaSugerencias.getTableHeader().setPreferredSize(new Dimension(180, 0));
                listaSugerencias.setRowMargin(0);

                // Deshabilitar edición
                for (int i = 0; i < listaSugerencias.getColumnCount(); i++) {
                    listaSugerencias.setDefaultEditor(listaSugerencias.getColumnClass(i), null);
                }

                // Ocultar ID
                listaSugerencias.getColumn("id").setMinWidth(0);
                listaSugerencias.getColumn("id").setMaxWidth(0);
                listaSugerencias.getColumn("id").setWidth(0);
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
    }

    return retorno;
}

 public Integer verCodigoDisponible(JTextField tex) {
    Integer codigo = 1; // 🔥 valor por defecto

    String sql = "SELECT (t1.codigocliente + 1) AS disponible " +
                 "FROM cliente t1 " +
                 "LEFT JOIN cliente t2 ON t1.codigocliente + 1 = t2.codigocliente " +
                 "WHERE t2.codigocliente IS NULL " +
                 "ORDER BY t1.codigocliente LIMIT 1";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            codigo = rs.getInt("disponible");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al obtener código: " + e.toString());
    }

    // 🔹 siempre setea el campo (aunque sea 1)
    if (tex != null) {
        tex.setText(String.valueOf(codigo));
    }

    return codigo;
}

   public Mensaje SeleccionarCliente(JTable listaCliente, JTextField fieldNombre,
        JTextField fieldDni, JTextField fieldDireccion,
        JTextField fieldTelefono, JTextField codCliente, JScrollPane contenedorClientes,
        JTextField limiteCompra, JLabel deuda, JTextField txtBuscarProducto,
        JComboBox metodoPago, JTextPane obs, JTextField dias,
        JTextField prCosto, JTextField texfieldStock,
        JTextField prVenta, JLabel saldoDisponible,
        int verificador, JTable tbProductos, JLabel total,
        JLabel labelCodCliente, JTextField codigoProducto, JTextField fieldCantidad,
        JScrollPane contenedorTabla, JLabel descripcionProd, JCheckBox editable, JLabel kgBulto,
        JTable tablaPesos, JScrollPane scrollPesos) {

    Mensaje mj = null;
    int fila = listaCliente.getSelectedRow();
    Integer idSelecte = 0;

    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    // VALIDACIÓN
    if (verificador == 1) {
        try {
            idSelecte = Integer.parseInt(codCliente.getText());
            fila = 1;
        } catch (Exception e) {
            limpiarCampos(fieldNombre, fieldDni, fieldDireccion, fieldTelefono,
                    codCliente, limiteCompra, deuda, txtBuscarProducto,
                    metodoPago, obs, dias, prCosto, texfieldStock,
                    prVenta, saldoDisponible, 1, labelCodCliente);
            codCliente.setText("");
            return null;
        }
    } else {
        if (fila < 0) return null;
        idSelecte = Integer.parseInt(listaCliente.getValueAt(fila, 1).toString());
    }

    String consulta = "SELECT codigocliente, nombre, telefono, cuit, direccion, diaspago, " +
                      "saldo_acumulado, limite_compra, metodopago, observaciones " +
                      "FROM cliente WHERE codigocliente = ? AND activo != FALSE;";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(consulta)) {

        ps.setInt(1, idSelecte);

        try (ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) {
                limpiarCampos(fieldNombre, fieldDni, fieldDireccion, fieldTelefono,
                        codCliente, limiteCompra, deuda, txtBuscarProducto,
                        metodoPago, obs, dias, prCosto, texfieldStock,
                        prVenta, saldoDisponible, 1, labelCodCliente);
                codCliente.setText("");
                return null;
            }

            limpiarCampos(fieldNombre, fieldDni, fieldDireccion, fieldTelefono,
                    codCliente, limiteCompra, deuda, txtBuscarProducto,
                    metodoPago, obs, dias, prCosto, texfieldStock,
                    prVenta, saldoDisponible, 1, labelCodCliente);

            int cliente = rs.getInt("codigocliente");

            codCliente.setText(String.valueOf(cliente));
            labelCodCliente.setText(String.valueOf(cliente));
            fieldNombre.setText(rs.getString("nombre"));
            fieldDni.setText(rs.getString("cuit"));
            fieldTelefono.setText(rs.getString("telefono"));
            fieldDireccion.setText(rs.getString("direccion"));
            dias.setText(rs.getString("diaspago"));

            double limite = rs.getDouble("limite_compra");
            double deudaVal = rs.getDouble("saldo_acumulado");

            limiteCompra.setText(formatoMoneda.format(limite));
            deuda.setText(formatoMoneda.format(deudaVal));

            double disponible = limite - deudaVal;

            if (disponible <= 0) {
                saldoDisponible.setText("SIN SALDO");
                saldoDisponible.setForeground(Color.WHITE);
                saldoDisponible.setBackground(Color.RED);
                saldoDisponible.setOpaque(true);
            } else {
                saldoDisponible.setText(formatoMoneda.format(disponible));
                saldoDisponible.setOpaque(false);
            }

            // Método de pago
            String medioPago = rs.getString("metodopago");
            if (medioPago == null) medioPago = "Contado";
            metodoPago.setSelectedItem(medioPago);

            // Observaciones
            String obsText = rs.getString("observaciones");
            if (obsText == null) obsText = "Agregar observaciones del Cliente";

            obs.setContentType("text/html");
            obs.setText("<html><body><p style='font-size:9px'>" + obsText + "</p></body></html>");

            // ACTUALIZAR TABLA
            ControladorVenta cv = new ControladorVenta();
            mj = cv.actualizarTablaCliente(tbProductos, cliente, prVenta);

            cv.calcularTotalAPagar(tbProductos, saldoDisponible, total,
                    limiteCompra, deuda, metodoPago.getSelectedItem().toString());

            if (!editable.isSelected()) {
                hacerrEditable(1, fieldNombre, fieldDireccion, fieldDni,
                        fieldTelefono, dias, prCosto, texfieldStock,
                        prVenta, limiteCompra, obs, metodoPago);
            }

        }

        contenedorClientes.setVisible(false);

        // Cargar producto si ya hay código
        if (!codigoProducto.getText().isEmpty()) {
            ControladorVenta cv = new ControladorVenta();
            cv.SeleccionarProductosVenta(tbProductos, fieldCantidad, codigoProducto,
                    txtBuscarProducto,  prVenta, codCliente,
                    contenedorTabla, texfieldStock, descripcionProd,
                    4);
        }

    } catch (Exception e) {
        contenedorClientes.setVisible(false);
        JOptionPane.showMessageDialog(null, "ERROR: " + e.toString());
    }

    return mj;
}

    /*   public Mensaje agregarClientePrim(String codigo, JTextField nombre,
            JTextField direccion, JTextField telefono, JTextField dni,
            Double limiteSaldo, Double SaldoCliente, JComboBox metodoPago,
            String observaciones, Integer verificador, JTable tabla,
            JTextField dias, Integer codigoCliente) {

        Mensaje mj = null;
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        String consulta = "select count(*) as existe from cliente where codigocliente=?";
        try {

            PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
            ps.setInt(1, codigoCliente);
            ResultSet rs = ps.executeQuery();
            int numero = 0;
            while (rs.next()) {
                numero = (rs.getInt("existe"));
            }

            if (numero > 0) {
                String consulta1 = "select c.codigocliente+1 as sugerido from cliente c "
                        + "where not exists(select 1 from cliente c2 where"
                        + " c2.codigocliente=c.codigocliente+1)limit 1;";
                ps = objetoConexion.estableceConexion().prepareStatement(consulta1);
                ps.setInt(1, codigoCliente);
                rs = ps.executeQuery();
                while (rs.next()) {
                    numero = (rs.getInt("sugerido"));
                }
                mj=new Mensaje();
                mj.setCodigo(numero);
                mj.setMensaje("El codigo "+codigoCliente+", ya esta ocupado, prueba el: "+ numero);
                return mj;
            } else {
             //  mj=
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "no anda we13" + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }

        return mj;
    }
     */
   public ModeloCliente retornaUnCliente(String palabra) {

    ModeloCliente objetoCliente = null;

    boolean esNumero;
    try {
        Integer.parseInt(palabra);
        esNumero = true;
    } catch (Exception e) {
        esNumero = false;
    }

    if (palabra == null || palabra.trim().isEmpty()) {
        return null;
    }

    String consulta;

    if (!esNumero) {
        consulta = "SELECT codigocliente, nombre, telefono, cuit, direccion, diaspago, " +
                   "saldo_acumulado, limite_compra " +
                   "FROM cliente WHERE nombre LIKE ? AND activo != FALSE LIMIT 1";
    } else {
        consulta = "SELECT codigocliente, nombre, telefono, cuit, direccion, diaspago, " +
                   "saldo_acumulado, limite_compra " +
                   "FROM cliente WHERE codigocliente LIKE ? AND activo != FALSE LIMIT 1";
    }

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(consulta)) {

        ps.setString(1, "%" + palabra.trim() + "%");

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                objetoCliente = new ModeloCliente();
                objetoCliente.setCodigo(rs.getInt("codigocliente"));
                objetoCliente.setNombre(rs.getString("nombre"));
                objetoCliente.setTelefono(rs.getInt("telefono"));
                objetoCliente.setCuitDni(rs.getInt("cuit"));
                objetoCliente.setLimiteCompra(rs.getDouble("limite_compra"));
                objetoCliente.setSaldoAcumulado(rs.getDouble("saldo_acumulado"));
                objetoCliente.setDireccion(rs.getString("direccion"));
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.toString());
    }

    return objetoCliente;
}

   public Mensaje agregarClientePrim(String codigo, JTextField nombre,
        JTextField direccion, JTextField telefono, JTextField dni,
        Double limiteSaldo, Double SaldoCliente, JComboBox metodoPago,
        String observaciones, Integer verificador, JTable tabla,
        JTextField dias, Integer codigoCliente, JTextField CodigoClienteField) {

    Mensaje mj;

    try (Connection conn = Conexion.getConexion()) {

        // 🔹 Si no viene código, generar uno nuevo
        if (codigoCliente == 0) {

            String consulta = "SELECT c.codigocliente + 1 AS sugerido " +
                              "FROM cliente c " +
                              "WHERE NOT EXISTS (SELECT 1 FROM cliente c2 WHERE c2.codigocliente = c.codigocliente + 1) " +
                              "LIMIT 1;";

            try (PreparedStatement ps = conn.prepareStatement(consulta);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    codigoCliente = rs.getInt("sugerido");
                } else {
                    codigoCliente = 1; // fallback
                }
            }
        }

        // 🔹 Llamar a método principal de guardado
        mj = agregarCliente("NUEVO CLIENTE", nombre, direccion,
                telefono, dni, limiteSaldo,
                0d, metodoPago, observaciones,
                1, null, dias, codigoCliente, null, null);

        // 🔹 Si salió bien → setear código en UI
        if (mj != null && mj.getCodigo() == 1) {
            CodigoClienteField.setText(String.valueOf(codigoCliente));
        }

    } catch (Exception e) {
        mj = new Mensaje();
        mj.setCodigo(4);
        mj.setMensaje("Error al agregar cliente: " + e.toString());
    }

    return mj;
}
   
   
  public Mensaje agregarCliente(String codigo, JTextField nombre,
        JTextField direccion, JTextField telefono, JTextField dni,
        Double limiteSaldo, Double saldoCliente, JComboBox metodoPago,
        String observaciones, Integer verificador, JTable tabla,
        JTextField dias, Integer codigoCliente, JTextField text, JLabel lblP) {

    Mensaje mj;

    // 🔹 Validación básica
    if (nombre.getText().trim().isEmpty()) {
        mj = new Mensaje();
        mj.setCodigo(3);
        mj.setMensaje("El nombre no puede estar vacío");
        return mj;
    }

    ModeloCliente clienteExistente = retornaUnCliente(String.valueOf(codigoCliente));

    try (Connection conn = Conexion.getConexion()) {

        ModeloCliente objetoCliente = new ModeloCliente();

        // 🔹 Normalización de verificador
        int verif = (verificador == 3) ? 2 : verificador;

        switch (verif) {

            // =====================================================
            // 🔵 INSERTAR CLIENTE
            // =====================================================
            case 1:

                if (clienteExistente != null) {
                    mj = new Mensaje();
                    mj.setCodigo(3);
                   Integer num = verCodigoDisponible(text);
mj.setMensaje("El código ya existe. Probá con: " + (num != null ? num : "otro disponible"));
                    return mj;
                }

                String insertSQL = "INSERT INTO cliente " +
                        "(codigocliente, nombre, direccion, telefono, cuit, activo, " +
                        "limite_compra, saldo_acumulado, metodopago, observaciones, diaspago) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

                try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {

                    objetoCliente.setCodigo(codigoCliente);
                    objetoCliente.setNombre(nombre.getText());
                    objetoCliente.setDireccion(direccion.getText());

                    // 🔹 Parse seguros
                    Integer tel = parseEntero(telefono.getText());
                    Integer cuit = parseEntero(dni.getText());
                    Integer diasPago = parseEntero(dias.getText());

                    ps.setInt(1, objetoCliente.getCodigo());
                    ps.setString(2, objetoCliente.getNombre());
                    ps.setString(3, objetoCliente.getDireccion());
                    setNullableInt(ps, 4, tel);
                    setNullableInt(ps, 5, cuit);
                    ps.setBoolean(6, true);
                    setNullableDouble(ps, 7, limiteSaldo);
                    setNullableDouble(ps, 8, saldoCliente);
                    ps.setString(9, metodoPago.getSelectedItem().toString());
                    ps.setString(10, observaciones);
                    setNullableInt(ps, 11, diasPago);

                    ps.executeUpdate();

                    mj = new Mensaje();
                    mj.setCodigo(1);
                    mj.setMensaje("Cliente creado con código: " + codigoCliente);
                }

                break;

            // =====================================================
            // 🟡 ACTUALIZAR CLIENTE
            // =====================================================
            case 2:

                String updateSQL = "UPDATE cliente SET nombre=?, direccion=?, telefono=?, cuit=?, activo=?, " +
                        "limite_compra=?, saldo_acumulado=?, metodopago=?, observaciones=?, diaspago=? " +
                        "WHERE codigocliente=?";

                try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {

                    Integer tel = parseEntero(telefono.getText());
                    Integer cuit = parseEntero(dni.getText());
                    Integer diasPago = parseEntero(dias.getText());

                    ps.setString(1, nombre.getText());
                    ps.setString(2, direccion.getText());
                    setNullableInt(ps, 3, tel);
                    setNullableInt(ps, 4, cuit);
                    ps.setBoolean(5, true);
                    setNullableDouble(ps, 6, limiteSaldo);
                    setNullableDouble(ps, 7, saldoCliente);
                    ps.setString(8, metodoPago.getSelectedItem().toString());
                    ps.setString(9, observaciones);
                    setNullableInt(ps, 10, diasPago);
                    ps.setInt(11, codigoCliente);

                    ps.executeUpdate();

                    mj = new Mensaje();
                    mj.setCodigo(1);
                    mj.setMensaje("Cliente actualizado correctamente");
                }

                if ("RENDER".equals(codigo)) {
                    try {
                        ListaClientes(tabla, 1, null, "ACTIVO");
                    } catch (Exception e) {
                        // silencioso como tu lógica original
                    }
                }

                break;

            default:
                mj = new Mensaje();
                mj.setCodigo(4);
                mj.setMensaje("Operación no válida");
        }

    } catch (Exception e) {
        mj = new Mensaje();
        mj.setCodigo(4);
        mj.setMensaje("Error: " + e.toString());
    }

    // 🔹 Mostrar mensaje en label si existe
    if (lblP != null && mj != null) {
        lblP.setText(mj.getMensaje());
    }

    return mj;
}

  private Integer parseEntero(String valor) {
    try {
        if (valor == null || valor.trim().isEmpty()) return null;
        return Integer.parseInt(valor.trim());
    } catch (Exception e) {
        return null;
    }
}

private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
    if (value != null) {
        ps.setInt(index, value);
    } else {
        ps.setNull(index, Types.INTEGER); // ✔ correcto
    }
}

private void setNullableDouble(PreparedStatement ps, int index, Double value) throws SQLException {
    if (value != null) {
        ps.setDouble(index, value);
    } else {
        ps.setNull(index, Types.DOUBLE); // ✔ correcto
    }
}
  
public void ListaClientes(JTable tablaTotalProductos, int codigoMuestra, String texto, String activos) {

    boolean act = !"ACTIVOS".equals(activos); // ✔ más simple

    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("COD.");
    modelo.addColumn("NOMBRE");
    modelo.addColumn("LIMITE");
    modelo.addColumn("SALDO");
    modelo.addColumn("DIAS");
    modelo.addColumn("TELEFONO");
    modelo.addColumn("CUIT");
    modelo.addColumn("DIRECCION");
    modelo.addColumn("PAGO");
    modelo.addColumn("OBSERVACIONES");

    tablaTotalProductos.setModel(modelo);

    String sql = "";
    boolean usarParametro = false;

    switch (codigoMuestra) {
        case 1:
            sql = "SELECT * FROM cliente WHERE activo != ?";
            break;

        case 2:
            boolean esNumero;
            try {
                Integer.parseInt(texto);
                esNumero = true;
            } catch (Exception e) {
                esNumero = false;
            }

            if (esNumero) {
                sql = "SELECT * FROM cliente WHERE codigocliente LIKE ? AND activo != ?";
            } else {
                sql = "SELECT * FROM cliente WHERE nombre LIKE ? AND activo != ?";
            }
            usarParametro = true;
            break;

        default:
            return;
    }

    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        if (codigoMuestra == 1) {
            ps.setBoolean(1, act);
        } else {
            ps.setString(1, "%" + (texto != null ? texto : "") + "%");
            ps.setBoolean(2, act);
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            modelo.addRow(new Object[]{
                rs.getInt("codigocliente"),
                rs.getString("nombre"),
                formatoMoneda.format(rs.getDouble("limite_compra")),
                formatoMoneda.format(rs.getDouble("saldo_acumulado")),
                rs.getInt("diaspago"),
                rs.getObject("telefono"), // ✔ evita error null
                rs.getObject("cuit"),
                rs.getString("direccion"),
                rs.getString("metodopago"),
                rs.getString("observaciones")
            });
        }

        // 🔹 bloquear edición
        for (int i = 0; i < tablaTotalProductos.getColumnCount(); i++) {
            tablaTotalProductos.setDefaultEditor(tablaTotalProductos.getColumnClass(i), null);
        }

        // 🔹 tamaños
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(140);
        tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(40);
        tablaTotalProductos.getColumnModel().getColumn(5).setPreferredWidth(78);
        tablaTotalProductos.getColumnModel().getColumn(6).setPreferredWidth(78);
        tablaTotalProductos.getColumnModel().getColumn(7).setPreferredWidth(133);
        tablaTotalProductos.getColumnModel().getColumn(8).setPreferredWidth(103);
        tablaTotalProductos.getColumnModel().getColumn(9).setPreferredWidth(140);

        // 🔹 centrado
        DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
        cent.setHorizontalAlignment(JLabel.CENTER);

        tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(5).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(6).setCellRenderer(cent);

        // 🔹 padding personalizado
        tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(new paddingRig());
        tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(new paddingRig());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.toString());
    }
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

   public Mensaje eliminarClienteSeleccionado(JTable tabla) {

    Mensaje mj = new Mensaje();
    int fila = tabla.getSelectedRow();

    if (fila == -1) {
        mj.setCodigo(2);
        mj.setMensaje("Seleccione una fila para eliminar");
        return mj;
    }

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(
                 "UPDATE cliente SET activo = FALSE WHERE codigocliente = ?")) {

        int codigoCliente = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

        ps.setInt(1, codigoCliente);
        ps.executeUpdate();

        ListaClientes(tabla, 1, null, "ACTIVOS");

        mj.setCodigo(1);
        mj.setMensaje("Cliente eliminado: " + codigoCliente);

    } catch (Exception e) {
        mj.setCodigo(4);
        mj.setMensaje("Error: " + e.toString());
    }

    return mj;
}
   
   
      public Mensaje activarClienteSeleccionado(JTable tabla) {

    Mensaje mj = new Mensaje();
    int fila = tabla.getSelectedRow();

    if (fila == -1) {
        mj.setCodigo(2);
        mj.setMensaje("Seleccione una fila para activar");
        return mj;
    }

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(
                 "UPDATE cliente SET activo = TRUE WHERE codigocliente = ?")) {

        int codigoCliente = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

        ps.setInt(1, codigoCliente);
        ps.executeUpdate();

        ListaClientes(tabla, 1, null, "INACTIVOS");

        mj.setCodigo(1);
        mj.setMensaje("Cliente activado: " + codigoCliente);

    } catch (Exception e) {
        mj.setCodigo(4);
        mj.setMensaje("Error: " + e.toString());
    }

    return mj;
}
      
      
   public Integer editarCliente(JTable tabla) {

    int fila = tabla.getSelectedRow();

    if (fila == -1) {
        return null; // ✔ mejor que devolver -1
    }

    try {
        return Integer.parseInt(tabla.getValueAt(fila, 0).toString());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        return null;
    }
}
   
   public List<Object[]> obtenerClientesParaSeleccion() {

    List<Object[]> lista = new ArrayList<>();

    String sql = "SELECT codigocliente, nombre, cuit_dni FROM cliente WHERE activo = 1";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

            Object[] fila = new Object[4];

            fila[0] = false; // checkbox
            fila[1] = rs.getInt("codigocliente");
            fila[2] = rs.getString("nombre");
            fila[3] = rs.getString("cuit_dni");

            lista.add(fila);
        }

    } catch (Exception e) {
        System.err.println("Error cargando clientes: " + e.getMessage());
    }

    return lista;
}

  public ModeloCliente obtenerCliente(Integer numero) {

    if (numero == null) return null;

    ModeloCliente cliente = null;

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM cliente WHERE codigocliente = ? AND activo != FALSE")) {

        ps.setInt(1, numero);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            cliente = new ModeloCliente();

            cliente.setCodigo(rs.getInt("codigocliente"));
            cliente.setNombre(rs.getString("nombre"));
            cliente.setLimiteCompra(rs.getDouble("limite_compra"));
            cliente.setSaldoAcumulado(rs.getDouble("saldo_acumulado"));
            cliente.setTelefono(rs.getObject("telefono") != null ? rs.getInt("telefono") : null);
            cliente.setCuitDni(rs.getObject("cuit") != null ? rs.getInt("cuit") : null);
            cliente.setDireccion(rs.getString("direccion"));
            cliente.setMetodoPago(rs.getString("metodopago"));
            cliente.setObservaciones(rs.getString("observaciones"));
            cliente.setDiasPago(rs.getObject("diaspago") != null ? rs.getInt("diaspago") : null);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.toString());
    }

    return cliente;
}
  
    public void hacerrEditable(int num, JTextField fieldNombre,
            JTextField fieldDireccion, JTextField fieldDni, JTextField fieldTelefono,
            JTextField dias, JTextField prCosto, JTextField textFieldStock,
            JTextField prVenta, JTextField limiteCompra, JTextPane paneObservaciones, JComboBox metodoPago) {
        Boolean editar = true;
        ControladorAdministracion ca = new ControladorAdministracion();

        if (num == 1 || num == 3) {
            editar = false;

            //    metodoPago.setBackground( ca.retornatexField());     
            fieldDireccion.setBackground(ca.retornatexFieldF());
            fieldNombre.setBackground(ca.retornatexFieldF());
            fieldDni.setBackground(ca.retornatexFieldF());
            fieldTelefono.setBackground(ca.retornatexFieldF());
            dias.setBackground(ca.retornatexFieldF());
            paneObservaciones.setBackground(ca.retornatexFieldF());
            limiteCompra.setBackground(ca.retornatexFieldF());
        }
        if (num == 2 || num == 4) {
            editar = true;
            //   metodoPago.setBackground( ca.retornatexFieldF());
            fieldDireccion.setBackground(ca.retornatexField());
            fieldNombre.setBackground(ca.retornatexField());
            fieldDni.setBackground(ca.retornatexField());
            fieldTelefono.setBackground(ca.retornatexField());
            dias.setBackground(ca.retornatexField());
            paneObservaciones.setBackground(ca.retornatexField());
        }

        metodoPago.setEnabled(editar);
        metodoPago.setFocusable(editar);
        fieldNombre.setEditable(editar);
        fieldDireccion.setEditable(editar);
        fieldDni.setEditable(editar);
        fieldTelefono.setEditable(editar);
        dias.setEditable(editar);
        paneObservaciones.setEditable(editar);
        limiteCompra.setEditable(editar);

        if (num == 3 || num == 4) {
            if (editar == false) {
                textFieldStock.setBackground(ca.retornatexFieldF());
                prVenta.setBackground(ca.retornatexFieldF());
                prCosto.setBackground(ca.retornatexFieldF());
                limiteCompra.setBackground(ca.retornatexFieldF());
            } else {
                textFieldStock.setBackground(ca.retornatexField());
                prVenta.setBackground(ca.retornatexField());
                prCosto.setBackground(ca.retornatexField());
                limiteCompra.setBackground(ca.retornatexField());
            }
            textFieldStock.setEditable(editar);
            prVenta.setEditable(editar);
            prCosto.setEditable(editar);
            limiteCompra.setEditable(editar);
        }

    }

    public void limpiarCampos(JTextField fieldNombre,
            JTextField fieldDni, JTextField fieldDireccion,
            JTextField fieldTelefono, JTextField codCliente,
            JTextField limiteCompra, JLabel deuda, JTextField txtBuscarProducto,
            JComboBox metodoPago, JTextPane obs, JTextField dias,
            JTextField prCosto, JTextField textFieldStock,
            JTextField prVenta, JLabel saldoDisponible, int verificador, JLabel cliente) {
        saldoDisponible.setForeground(null);
        saldoDisponible.setBackground(null);
        saldoDisponible.setOpaque(false);
        saldoDisponible.setHorizontalAlignment(JLabel.LEFT);
        saldoDisponible.setFont(null);
        codCliente.setText("");
        saldoDisponible.setText("----");
        fieldNombre.setText("");
        fieldDni.setText("");
        fieldTelefono.setText("");
        fieldDireccion.setText("");
        limiteCompra.setText("");
        deuda.setText("----");
        dias.setText("");
        obs.setText("");
        cliente.setText("");
        metodoPago.setSelectedIndex(0);
        hacerrEditable(0, fieldNombre, fieldDireccion, fieldDni,
                fieldTelefono, dias, prCosto, textFieldStock,
                prVenta, limiteCompra, obs, metodoPago);
        try {
            //  metodoPago.removeItem("Cuenta Corriente");
        } catch (Exception e) {
        }

    }
    
public void actualizarPreciosPorClienteMasivo(List<Integer> listaClientes,
                                              List<Object[]> listaProductos,
                                              double porcentaje) {

    // 🔹 Validaciones
    if (listaClientes == null || listaClientes.isEmpty()
            || listaProductos == null || listaProductos.isEmpty()
            || porcentaje == 0) {
        return;
    }

    double factor = 1 + (porcentaje / 100.0);

    String sql = "UPDATE preciocliente SET " +
                 "costoanterior = ROUND(costoanterior * ?, 2), " +
                 "diferido = ROUND(diferido * ?, 2), " +
                 "fechamodificacion = datetime('now','localtime') " +
                 "WHERE fkcliente = ? AND fkproducto = ?";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        conn.setAutoCommit(false); // 🔹 iniciar transacción

        int contador = 0;

        for (Integer idCliente : listaClientes) {

            if (idCliente == null) continue;

            for (Object[] fila : listaProductos) {

                if (fila == null) continue;

                // 🔹 Validación segura del checkbox
                boolean seleccionado = false;
                if (fila[0] instanceof Boolean) {
                    seleccionado = (Boolean) fila[0];
                }

                if (!seleccionado) continue;

                int idProducto = Integer.parseInt(fila[1].toString());

                ps.setDouble(1, factor);
                ps.setDouble(2, factor);
                ps.setInt(3, idCliente);
                ps.setInt(4, idProducto);

                ps.addBatch();
                contador++;
            }
        }

        if (contador > 0) {
            ps.executeBatch();
            conn.commit();
            System.out.println("✔ Se actualizaron " + contador + " precios.");
        } else {
            conn.rollback();
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
                "Error al actualizar precios: " + e.getMessage());
    }
}

public void ordenarTablaDesdeInterfaz(JTable tabla, int indiceColumna, boolean deMayorAMenor) {
    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    if (modelo.getRowCount() == 0) return;

    List<Object[]> listaTemporal = new ArrayList<>();
    for (int i = 0; i < modelo.getRowCount(); i++) {
        Object[] fila = new Object[modelo.getColumnCount()];
        for (int j = 0; j < modelo.getColumnCount(); j++) {
            fila[j] = modelo.getValueAt(i, j);
        }
        listaTemporal.add(fila);
    }

    Collections.sort(listaTemporal, new Comparator<Object[]>() {
        @Override
        public int compare(Object[] o1, Object[] o2) {
            String v1 = (o1[indiceColumna] != null) ? o1[indiceColumna].toString().trim() : "0";
            String v2 = (o2[indiceColumna] != null) ? o2[indiceColumna].toString().trim() : "0";

            try {
                // LIMPIEZA EXTREMA: 
                // 1. Quitamos todo lo que NO sea número o coma
                // 2. Reemplazamos la coma por punto
                double n1 = Double.parseDouble(v1.replaceAll("[^0-9,]", "").replace(",", "."));
                double n2 = Double.parseDouble(v2.replaceAll("[^0-9,]", "").replace(",", "."));
                
                return deMayorAMenor ? Double.compare(n2, n1) : Double.compare(n1, n2);
            } catch (Exception e) {
                // Si no es número (Nombre/Método Pago), orden alfabético
                return deMayorAMenor ? v2.compareToIgnoreCase(v1) : v1.compareToIgnoreCase(v2);
            }
        }
    });

    modelo.setRowCount(0); 
    for (Object[] fila : listaTemporal) {
        modelo.addRow(fila);
    }
}
}
