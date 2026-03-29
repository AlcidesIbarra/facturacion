/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import configuracion.Conexion;

/**
 *
 * @author ALCIDES
 */
public class ControladorEstadisticas {

  public void consultaPagos(JTable tablaTotalProductos, String condicion,
        java.util.Date desde, java.util.Date hasta,
        Integer cliente, Integer facturaBuscar) {

    // 🔹 Modelo limpio
    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 4; // SOLO combo editable
        }
    };

    modelo.addColumn("CLIENTE");
    modelo.addColumn("FECHA");
    modelo.addColumn("IMPORTE");
    modelo.addColumn("TIPO");
    modelo.addColumn("FACT");
    modelo.addColumn("METODO");

    tablaTotalProductos.setModel(modelo);
    tablaTotalProductos.setAutoCreateColumnsFromModel(true);

    NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    SimpleDateFormat sdfSql = new SimpleDateFormat("yyyy-MM-dd");

    // 🔹 SQL dinámico
    StringBuilder sql = new StringBuilder(
            "SELECT strftime('%d-%m-%Y', p.fecha) as fechaFormateada, " +
            "p.importe, p.facturaafectada, p.tipo, c.nombre, p.metodo " +
            "FROM pagos p JOIN cliente c ON p.idcliente = c.codigocliente WHERE 1=1 "
    );

    if (desde != null) sql.append(" AND DATE(p.fecha) >= DATE(?) ");
    if (hasta != null) sql.append(" AND DATE(p.fecha) <= DATE(?) ");
    if (cliente != null && cliente > 0) sql.append(" AND p.idcliente = ? ");
    if (facturaBuscar != null && facturaBuscar > 0)
        sql.append(" AND EXISTS (SELECT 1 FROM json_each(p.facturaafectada) WHERE value = ?) ");
    if (condicion != null && !condicion.equals("TODOS"))
        sql.append(" AND p.tipo = ? ");

    sql.append(" ORDER BY p.fecha DESC");

    Map<Integer, String[]> opcionesporfila = new HashMap<>();

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int index = 1;

        if (desde != null) ps.setString(index++, sdfSql.format(desde));
        if (hasta != null) ps.setString(index++, sdfSql.format(hasta));
        if (cliente != null && cliente > 0) ps.setInt(index++, cliente);
        if (facturaBuscar != null && facturaBuscar > 0) ps.setInt(index++, facturaBuscar);
        if (condicion != null && !condicion.equals("TODOS")) ps.setString(index++, condicion);

        ResultSet rs = ps.executeQuery();

        int fila = 0;

        while (rs.next()) {

            String facturaRaw = rs.getString("facturaafectada");
            String[] arrayFacturas = {"-"};

            if (facturaRaw != null && facturaRaw.length() > 2) {
                String limpio = facturaRaw.replace("[", "").replace("]", "").trim();
                if (!limpio.isEmpty()) {
                    arrayFacturas = limpio.split(",\\s*");
                }
            }

            modelo.addRow(new Object[]{
                rs.getString("nombre"),
                rs.getString("fechaFormateada"),
                moneda.format(rs.getDouble("importe")),
                rs.getString("tipo"),
                arrayFacturas[0],
                rs.getString("metodo")
            });

            opcionesporfila.put(fila, arrayFacturas);
            fila++;
        }

        // 🔹 Editor combo por fila
        if (!opcionesporfila.isEmpty()) {
            tablaTotalProductos.getColumnModel()
                    .getColumn(4)
                    .setCellEditor(new ComboPorFilaEditor(opcionesporfila));
        }

        // 🔹 Deshabilitar edición EXCEPTO combo
        for (int i = 0; i < tablaTotalProductos.getColumnCount(); i++) {
            if (i != 4) {
                tablaTotalProductos.setDefaultEditor(
                        tablaTotalProductos.getColumnClass(i), null);
            }
        }

        // 🔹 UI
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(158);
        tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(74);
        tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(140);
        tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(160);
        tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(60);
        tablaTotalProductos.getColumnModel().getColumn(5).setPreferredWidth(100);

        DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
        cent.setHorizontalAlignment(JLabel.CENTER);

        tablaTotalProductos.getColumnModel().getColumn(1).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(cent);

        tablaTotalProductos.getColumnModel().getColumn(2)
                .setCellRenderer(new paddingRig());

        tablaTotalProductos.setRowHeight(25);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
                "Error al cargar pagos: " + e.getMessage());
    }
}

    class ComboPorFilaEditor extends DefaultCellEditor {

        private Map<Integer, String[]> mapaOpciones;

        public ComboPorFilaEditor(Map<Integer, String[]> mapaOpciones) {
            super(new JComboBox<String>());
            this.mapaOpciones = mapaOpciones;
        }
        JComboBox<String> combo = new JComboBox<>();

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            combo.removeAllItems();
            combo = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);

            String[] opciones = mapaOpciones.get(row);

            if (opciones != null) {
                for (String item : opciones) {
                    combo.addItem(item);
                }
            }

            if (value != null) {
                combo.setSelectedItem(value);
            }

            return combo;
        }
    }

    public class paddingleft extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            return this;
        }
    }

    public Integer verDetalle(JTable tbResumenVenta, int col) {

        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();

        int numero = 0;
        try {
            int indiceSelleccionado = tbResumenVenta.getSelectedRow();
            if (indiceSelleccionado != -1) {

                numero = Integer.parseInt(tbResumenVenta.getValueAt(indiceSelleccionado, col).toString());

            } else {
                JOptionPane.showMessageDialog(null, "seleccione una fila para eliminar");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error a seleccionar " + e.toString());
        }
        return numero;
    }

 public void consultaindividual(JTable tablaTotalProductos,
        String condicion, java.sql.Date desde, java.sql.Date hasta,
        Integer codigoProducto, Integer codigofactura) {

    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    modelo.addColumn("FECHA");
    modelo.addColumn("DESCRIPCION");
    modelo.addColumn("CANTIDAD");
    modelo.addColumn("PRECIO VENTA");
    modelo.addColumn("SUBTOTAL");
    modelo.addColumn("FACTURA");

    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    StringBuilder sql = new StringBuilder(
            "SELECT strftime('%d-%m-%Y', f.fechaFactura) as fechaF, " +
            "p.descripcion, d.cantidad, d.precioventa, " +
            "(d.cantidad * d.precioventa) as subtotal, f.codigo " +
            "FROM producto p " +
            "JOIN detalle d ON p.codigoproducto = d.fkproducto " +
            "JOIN factura f ON d.fkfactura = f.codigo WHERE 1=1 "
    );

    if (desde != null) sql.append(" AND DATE(f.fechaFactura) >= DATE(?) ");
    if (hasta != null) sql.append(" AND DATE(f.fechaFactura) <= DATE(?) ");
    if (codigoProducto != null && codigoProducto > 0) sql.append(" AND p.codigoproducto = ? ");
    if (codigofactura != null && codigofactura > 0) sql.append(" AND f.codigo = ? ");

    sql.append(" AND f.estado='ACTIVA' ORDER BY f.fechaFactura ASC");

    try (Connection conn = Conexion.getConexion();
         PreparedStatement pst = conn.prepareStatement(sql.toString())) {

        int it = 1;
        if (desde != null) pst.setString(it++, desde.toString());
        if (hasta != null) pst.setString(it++, hasta.toString());
        if (codigoProducto != null && codigoProducto > 0) pst.setInt(it++, codigoProducto);
        if (codigofactura != null && codigofactura > 0) pst.setInt(it++, codigofactura);

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getString("fechaF"),
                rs.getString("descripcion"),
                rs.getInt("cantidad"),
                formatoMoneda.format(rs.getDouble("precioventa")),
                formatoMoneda.format(rs.getDouble("subtotal")),
                rs.getInt("codigo")
            });
        }

        tablaTotalProductos.setModel(modelo);
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
        cent.setHorizontalAlignment(JLabel.CENTER);

        int[] anchos = {88, 152, 76, 142, 142, 64};
        for (int i = 0; i < anchos.length; i++) {
            tablaTotalProductos.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(5).setCellRenderer(cent);

        tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(new paddingRig());
        tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(new paddingRig());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error en consulta: " + e.getMessage());
    }
}


  public void consultaTotales(JTable tablaTotalProductos,
        String condicion, java.util.Date desde, java.util.Date hasta, Integer codigoProducto) {

    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    modelo.addColumn("COD.");
    modelo.addColumn("DESCRIPCION");
    modelo.addColumn("CANT. TOTAL");
    modelo.addColumn("COSTO TOTAL");
    modelo.addColumn("INGRESO FINAL");
    modelo.addColumn("GANANCIA");

    NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    SimpleDateFormat sdfSql = new SimpleDateFormat("yyyy-MM-dd");

    double totalUnidades = 0, totalCosto = 0, totalIngreso = 0, totalGanancia = 0;

    StringBuilder sql = new StringBuilder(
            "SELECT p.codigoproducto, p.descripcion, " +
            "SUM(d.cantidad) as total_unidades, " +
            "SUM(d.cantidad * d.preciocompra) as costo_totales, " +
            "SUM(d.cantidad * d.precioventa) as ingreso_totales, " +
            "SUM(d.cantidad * (d.precioventa - d.preciocompra)) as ganancia_total " +
            "FROM producto p " +
            "JOIN detalle d ON p.codigoproducto = d.fkproducto " +
            "JOIN factura f ON d.fkfactura = f.codigo WHERE 1=1 "
    );

    if (desde != null) sql.append(" AND DATE(f.fechaFactura) >= DATE(?) ");
    if (hasta != null) sql.append(" AND DATE(f.fechaFactura) <= DATE(?) ");
    if (codigoProducto != null && codigoProducto > 0) sql.append(" AND p.codigoproducto = ? ");

    sql.append(" AND f.estado='ACTIVA' GROUP BY p.codigoproducto, p.descripcion ORDER BY total_unidades DESC");

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int i = 1;
        if (desde != null) ps.setString(i++, sdfSql.format(desde));
        if (hasta != null) ps.setString(i++, sdfSql.format(hasta));
        if (codigoProducto != null && codigoProducto > 0) ps.setInt(i++, codigoProducto);

        ResultSet rs = ps.executeQuery();
        DecimalFormat df = new DecimalFormat("#.00");

        while (rs.next()) {
            double unidades = rs.getDouble("total_unidades");
            double costo = rs.getDouble("costo_totales");
            double ingreso = rs.getDouble("ingreso_totales");
            double ganancia = rs.getDouble("ganancia_total");

            totalUnidades += unidades;
            totalCosto += costo;
            totalIngreso += ingreso;
            totalGanancia += ganancia;

            modelo.addRow(new Object[]{
                rs.getInt("codigoproducto"),
                rs.getString("descripcion"),
                df.format(unidades),
                moneda.format(costo),
                moneda.format(ingreso),
                moneda.format(ganancia)
            });
        }

        // 🔹 Fila total
        modelo.addRow(new Object[]{
            "", "TOTALES GENERALES",
            df.format(totalUnidades),
            moneda.format(totalCosto),
            moneda.format(totalIngreso),
            moneda.format(totalGanancia)
        });

        tablaTotalProductos.setModel(modelo);

        // 🔹 Render
        DefaultTableCellRenderer render = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String nombre = table.getValueAt(row, 1).toString();

                if ("TOTALES GENERALES".equals(nombre)) {
                    c.setBackground(new Color(220, 220, 220));
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setBackground(table.getBackground());
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }

                if (column >= 3) setHorizontalAlignment(JLabel.RIGHT);
                else setHorizontalAlignment(JLabel.CENTER);

                if (isSelected) c.setBackground(table.getSelectionBackground());

                return c;
            }
        };

        for (int j = 0; j < tablaTotalProductos.getColumnCount(); j++) {
            tablaTotalProductos.getColumnModel().getColumn(j).setCellRenderer(render);
        }

        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(46);
        tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(176);
        tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(117);
        tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(117);
        tablaTotalProductos.getColumnModel().getColumn(5).setPreferredWidth(117);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

   public void consultaTotalesCliente(JTable tablaTotalProductos,
        String condicion, java.util.Date desde, java.util.Date hasta, Integer idCliente) {

    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    modelo.addColumn("CODIGO");
    modelo.addColumn("NOMBRE");
    modelo.addColumn("DESCRIPCION");
    modelo.addColumn("CANT. TOTAL");
    modelo.addColumn("IMPORTE COMPRADO");

    NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    SimpleDateFormat sdfSql = new SimpleDateFormat("yyyy-MM-dd");

    StringBuilder sql = new StringBuilder(
            "SELECT c.codigocliente, c.nombre, p.descripcion, " +
            "SUM(d.cantidad) as total_comprado, " +
            "SUM(d.cantidad * d.precioventa) as importe_comprado " +
            "FROM cliente c " +
            "JOIN factura f ON f.cod_cliente = c.codigocliente " +
            "JOIN detalle d ON d.fkfactura = f.codigo " +
            "JOIN producto p ON d.fkproducto = p.codigoproducto " +
            "WHERE f.estado='ACTIVA' ");

    if (desde != null) sql.append(" AND DATE(f.fechaFactura) >= DATE(?) ");
    if (hasta != null) sql.append(" AND DATE(f.fechaFactura) <= DATE(?) ");
    if (idCliente != null && idCliente > 0) sql.append(" AND c.codigocliente = ? ");

    sql.append(" GROUP BY c.codigocliente, p.codigoproducto, p.descripcion ")
       .append(" ORDER BY c.nombre ASC, total_comprado DESC;");

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int i = 1;
        if (desde != null) ps.setString(i++, sdfSql.format(desde));
        if (hasta != null) ps.setString(i++, sdfSql.format(hasta));
        if (idCliente != null && idCliente > 0) ps.setInt(i++, idCliente);

        ResultSet rs = ps.executeQuery();
        DecimalFormat df = new DecimalFormat("#,##0.00");

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("codigocliente"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                df.format(rs.getDouble("total_comprado")),
                moneda.format(rs.getDouble("importe_comprado"))
            });
        }

        tablaTotalProductos.setModel(modelo);

        // 🔹 Configuración visual
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] anchos = {60, 177, 177, 80, 171};
        for (int c = 0; c < anchos.length; c++) {
            tablaTotalProductos.getColumnModel().getColumn(c).setPreferredWidth(anchos[c]);
        }

        DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
        cent.setHorizontalAlignment(JLabel.CENTER);

        tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(new paddingRig());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error en consulta: " + e.getMessage());
    }
}

    
    
    
   public void consultaDetallePesosPorCliente(JTable tablaTotalProductos,
        java.util.Date desde, java.util.Date hasta, Integer idCliente) {

    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    modelo.addColumn("FACT");
    modelo.addColumn("CLIENTE");
    modelo.addColumn("PRODUCTO");
    modelo.addColumn("KG COMPRADO");
    modelo.addColumn("FECHA");
    modelo.addColumn("IMPORTE");

    NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    SimpleDateFormat sdfSql = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfSalida = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    DecimalFormat df = new DecimalFormat("#,##0.00");

    StringBuilder sql = new StringBuilder(
            "SELECT f.codigo as nro_factura, c.nombre, p.descripcion, d.cantidad, f.fechaFactura, f.montototal " +
            "FROM factura f " +
            "JOIN cliente c ON f.cod_cliente = c.codigocliente " +
            "JOIN detalle d ON d.fkfactura = f.codigo " +
            "JOIN producto p ON d.fkproducto = p.codigoproducto " +
            "WHERE f.estado = 'ACTIVA' ");

    if (desde != null) sql.append(" AND DATE(f.fechaFactura) >= DATE(?) ");
    if (hasta != null) sql.append(" AND DATE(f.fechaFactura) <= DATE(?) ");
    if (idCliente != null && idCliente > 0) sql.append(" AND c.codigocliente = ? ");

    sql.append(" ORDER BY f.codigo DESC");

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int i = 1;
        if (desde != null) ps.setString(i++, sdfSql.format(desde));
        if (hasta != null) ps.setString(i++, sdfSql.format(hasta));
        if (idCliente != null && idCliente > 0) ps.setInt(i++, idCliente);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String fecha;
            try {
                fecha = sdfSalida.format(rs.getTimestamp("fechaFactura"));
            } catch (Exception e) {
                fecha = rs.getString("fechaFactura");
            }

            modelo.addRow(new Object[]{
                rs.getInt("nro_factura"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                df.format(rs.getDouble("cantidad")),
                fecha,
                moneda.format(rs.getDouble("montototal"))
            });
        }

        tablaTotalProductos.setModel(modelo);

        // 🔹 Diseño
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] anchos = {60, 140, 140, 80, 120, 120};
        for (int c = 0; c < anchos.length; c++) {
            tablaTotalProductos.getColumnModel().getColumn(c).setPreferredWidth(anchos[c]);
        }

        DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
        cent.setHorizontalAlignment(JLabel.CENTER);

        tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(5).setCellRenderer(new paddingRig());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error en reporte de pesos: " + e.getMessage());
    }
}
    
    
    
 public void consultaTotalesCliente(JTable tablaTotalProductos,
        java.util.Date desde, java.util.Date hasta, Integer idCliente) {

    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    modelo.addColumn("CODIGO");
    modelo.addColumn("NOMBRE");
    modelo.addColumn("DESCRIPCION");
    modelo.addColumn("CANT. TOTAL");
    modelo.addColumn("IMPORTE TOTAL");

    NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    SimpleDateFormat sdfSql = new SimpleDateFormat("yyyy-MM-dd");

    StringBuilder sql = new StringBuilder(
            "SELECT c.codigocliente, c.nombre, p.descripcion, " +
            "SUM(d.cantidad) as total_comprado, " +
            "SUM(d.cantidad * d.precioventa) as importe_comprado " +
            "FROM cliente c " +
            "JOIN factura f ON f.cod_cliente = c.codigocliente " +
            "JOIN detalle d ON d.fkfactura = f.codigo " +
            "JOIN producto p ON d.fkproducto = p.codigoproducto " +
            "WHERE f.pagado = 0 AND f.estado='ACTIVA' ");

    if (desde != null) sql.append(" AND date(f.fechaFactura) >= date(?) ");
    if (hasta != null) sql.append(" AND date(f.fechaFactura) <= date(?) ");
    if (idCliente != null && idCliente > 0) sql.append(" AND c.codigocliente = ? ");

    sql.append(" GROUP BY c.codigocliente, p.codigoproducto, p.descripcion ")
       .append(" ORDER BY total_comprado DESC");

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int i = 1;
        if (desde != null) ps.setString(i++, sdfSql.format(desde));
        if (hasta != null) ps.setString(i++, sdfSql.format(hasta));
        if (idCliente != null && idCliente > 0) ps.setInt(i++, idCliente);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("codigocliente"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("total_comprado"),
                moneda.format(rs.getDouble("importe_comprado"))
            });
        }

        tablaTotalProductos.setModel(modelo);

        // 🔹 Configuración visual (FUERA del while)
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] anchos = {48, 120, 180, 120, 160};
        for (int c = 0; c < anchos.length; c++) {
            tablaTotalProductos.getColumnModel().getColumn(c).setPreferredWidth(anchos[c]);
        }

        DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
        cent.setHorizontalAlignment(JLabel.CENTER);

        tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(new paddingRig());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error en consulta: " + e.getMessage());
    }
}

  public void consultaDeudaTotal(JTable tablaTotalProductos,
        String condicion, java.util.Date desde, java.util.Date hasta, Integer idCliente) {

    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    modelo.addColumn("CODIGO");
    modelo.addColumn("NOMBRE");
    modelo.addColumn("SALDO");
    modelo.addColumn("PROXIMO VENCIMIENTO");

    NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    SimpleDateFormat sdfSql = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfSalida = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "AR"));

    StringBuilder sql = new StringBuilder(
            "SELECT c.codigocliente, c.nombre, " +
            "SUM(f.montototal - f.adelanto - f.posteriores) as saldo, " +
            "MIN(date(f.fechaFactura, '+' || c.diaspago || ' day')) as vencimiento " +
            "FROM factura f " +
            "JOIN cliente c ON f.cod_cliente = c.codigocliente " +
            "WHERE f.pagado = 0 AND f.estado='ACTIVA' ");

    if (desde != null) sql.append(" AND date(f.fechaFactura) >= date(?) ");
    if (hasta != null) sql.append(" AND date(f.fechaFactura) <= date(?) ");
    if (idCliente != null && idCliente > 0) sql.append(" AND c.codigocliente = ? ");

    sql.append(" GROUP BY c.codigocliente, c.nombre ")
       .append(" HAVING saldo > 0 ")
       .append(" ORDER BY vencimiento ASC");

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int i = 1;
        if (desde != null) ps.setString(i++, sdfSql.format(desde));
        if (hasta != null) ps.setString(i++, sdfSql.format(hasta));
        if (idCliente != null && idCliente > 0) ps.setInt(i++, idCliente);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String fechaTexto = "Sin vencimiento";
            if (rs.getString("vencimiento") != null) {
java.util.Date d = sdfSql.parse(rs.getString("vencimiento"));
                fechaTexto = sdfSalida.format(d);
            }

            modelo.addRow(new Object[]{
                rs.getInt("codigocliente"),
                rs.getString("nombre"),
                moneda.format(rs.getDouble("saldo")),
                fechaTexto
            });
        }

        tablaTotalProductos.setModel(modelo);

        // 🔹 Configuración visual (FUERA del while)
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] anchos = {66, 210, 151, 226};
        for (int c = 0; c < anchos.length; c++) {
            tablaTotalProductos.getColumnModel().getColumn(c).setPreferredWidth(anchos[c]);
        }

        DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
        cent.setHorizontalAlignment(JLabel.CENTER);

        tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(new paddingRig());
        tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(cent);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.toString());
    }
}

   public void consultaDeudaporCliente(JTable tablaTotalProductos,
        String condicion, java.util.Date desde, java.util.Date hasta,
        Integer cliente, Integer factura) {

    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    modelo.addColumn("COD");
    modelo.addColumn("FECHA");
    modelo.addColumn("IMPORTE");
    modelo.addColumn("CLIENTE");
    modelo.addColumn("VENCIMIENTO");
    modelo.addColumn("SALDO");

    NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat salida = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", new Locale("es", "AR"));

    StringBuilder sql = new StringBuilder(
            "SELECT c.codigocliente, f.montototal, f.codigo, " +
            "strftime('%d-%m-%Y', f.fechaFactura) AS fechaVisible, " +
            "DATE(f.fechaFactura, '+' || c.diaspago || ' days') AS vencimiento, " +
            "c.nombre, " +
            "(f.montototal - IFNULL(f.adelanto,0) - IFNULL(f.posteriores,0)) AS saldo " +
            "FROM factura f " +
            "INNER JOIN cliente c ON f.cod_cliente = c.codigocliente " +
            "WHERE f.pagado = 0 ");

    // 🔹 Filtros dinámicos
    if (desde != null) sql.append(" AND date(f.fechaFactura) >= date(?) ");
    if (hasta != null) sql.append(" AND date(f.fechaFactura) <= date(?) ");
    if (cliente != null && cliente > 0) sql.append(" AND c.codigocliente = ? ");
    if (factura != null && factura > 0) sql.append(" AND f.codigo = ? ");

    if ("VENCIDO".equals(condicion)) {
        sql.append(" AND DATE(f.fechaFactura, '+' || c.diaspago || ' days') < DATE('now','localtime') ");
    }

    sql.append(" AND f.estado='ACTIVA' ORDER BY vencimiento ASC");

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int i = 1;

        if (desde != null) ps.setString(i++, iso.format(desde));
        if (hasta != null) ps.setString(i++, iso.format(hasta));
        if (cliente != null && cliente > 0) ps.setInt(i++, cliente);
        if (factura != null && factura > 0) ps.setInt(i++, factura);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String vencTexto = "Sin fecha";

            String vencRaw = rs.getString("vencimiento");
            if (vencRaw != null) {
                try {
java.util.Date d = iso.parse(vencRaw);
vencTexto = salida.format(d);
                } catch (Exception e) {
                    vencTexto = vencRaw;
                }
            }

            modelo.addRow(new Object[]{
                rs.getInt("codigo"),
                rs.getString("fechaVisible"),
                moneda.format(rs.getDouble("montototal")),
                rs.getString("nombre"),
                vencTexto,
                moneda.format(rs.getDouble("saldo"))
            });
        }

        tablaTotalProductos.setModel(modelo);

        // 🔹 Bloquear edición
        for (int col = 0; col < tablaTotalProductos.getColumnCount(); col++) {
            tablaTotalProductos.setDefaultEditor(tablaTotalProductos.getColumnClass(col), null);
        }

        // 🔹 Tamaños
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] widths = {50, 80, 120, 140, 180, 120};

        for (int col = 0; col < widths.length; col++) {
            tablaTotalProductos.getColumnModel().getColumn(col).setPreferredWidth(widths[col]);
        }

        // 🔹 Renderers
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(center);
        tablaTotalProductos.getColumnModel().getColumn(1).setCellRenderer(center);
        tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(new paddingRig());
        tablaTotalProductos.getColumnModel().getColumn(5).setCellRenderer(new paddingRig());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error en consulta: " + e.getMessage());
    }
}
   
   
public void reporteUnico(JLabel titulo, JLabel tarjeta, JLabel efectivo, JLabel corriente,
                        JLabel total, int num, JLabel transferencia, JLabel gananciaLabel) {

    NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    String filtro = "";

    switch (num) {
        case 1:
            filtro = "date(f.fechaFactura) = date('now','localtime')";
            titulo.setText("RESUMEN DE VENTA DIARIO");
            break;
        case 2:
            filtro = "strftime('%Y-%W', f.fechaFactura) = strftime('%Y-%W','now')";
            titulo.setText("RESUMEN DE VENTA SEMANAL");
            break;
        case 3:
            filtro = "strftime('%m-%Y', f.fechaFactura) = strftime('%m-%Y','now')";
            titulo.setText("RESUMEN DE VENTA MENSUAL");
            break;
        case 4:
            filtro = "strftime('%Y', f.fechaFactura) = strftime('%Y','now')";
            titulo.setText("RESUMEN DE VENTA ANUAL");
            break;
        default:
            filtro = "1=1";
    }

    String sql =
            "SELECT mediopago, SUM(monto) totalvendido, SUM(ganancia) totalganancia, COUNT(*) cantidad FROM ( " +
            " SELECT f.codigo, f.metodopago AS mediopago, f.montototal AS monto, " +
            " SUM((d.precioventa - d.preciocompra) * d.cantidad) AS ganancia " +
            " FROM factura f " +
            " LEFT JOIN detalle d ON f.codigo = d.fkfactura " +
            " WHERE " + filtro + " AND f.estado='ACTIVA' " +
            " GROUP BY f.codigo " +
            ") GROUP BY mediopago " +

            " UNION ALL " +

            "SELECT 'total', SUM(monto), SUM(ganancia), COUNT(*) FROM ( " +
            " SELECT f.codigo, f.montototal AS monto, " +
            " SUM((d.precioventa - d.preciocompra) * d.cantidad) AS ganancia " +
            " FROM factura f " +
            " LEFT JOIN detalle d ON f.codigo = d.fkfactura " +
            " WHERE " + filtro + " AND f.estado='ACTIVA' " +
            " GROUP BY f.codigo " +
            ")";

    // 🔹 Reset valores
    String cero = "( 0 )   " + formato.format(0);
    corriente.setText(cero);
    efectivo.setText(cero);
    tarjeta.setText(cero);
    transferencia.setText(cero);
    total.setText(cero);
    gananciaLabel.setText(formato.format(0));

    try (Connection conn = Conexion.getConexion();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {

            String medio = rs.getString(1);
            double vendido = rs.getDouble(2);
            double ganancia = rs.getDouble(3);
            int cantidad = rs.getInt(4);

            String texto = "( " + cantidad + " )   " + formato.format(vendido);

            if (medio == null) continue;

            switch (medio) {
                case "Cuenta Corriente":
                    corriente.setText(texto);
                    break;
                case "Contado":
                    efectivo.setText(texto);
                    break;
                case "Tarjeta":
                    tarjeta.setText(texto);
                    break;
                case "Transferencia":
                    transferencia.setText(texto);
                    break;
                case "total":
                    total.setText(texto);
                    gananciaLabel.setText(formato.format(ganancia));
                    break;
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error en reporte: " + e.getMessage());
    }
}


    public class paddingRig extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.RIGHT);
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            return this;
        }
    }

}
