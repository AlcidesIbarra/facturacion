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

/**
 *
 * @author ALCIDES
 */
public class ControladorEstadisticas {

    public void consultaPagos(JTable tablaTotalProductos, String condicion,
            java.util.Date desde, java.util.Date hasta,
            Integer cliente, Integer facturaBuscar) {

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();

        // 1. RECONSTRUCCIÓN TOTAL DEL MODELO (Para limpiar columnas de productos)
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Solo la columna de facturas es editable para el combo
            }
        };

        // Definimos las columnas específicas de PAGOS
        modelo.addColumn("CLIENTE");
        modelo.addColumn("FECHA");
        modelo.addColumn("IMPORTE");
        modelo.addColumn("TIPO");
        modelo.addColumn("FACT");
          modelo.addColumn("METODO");

        // Aplicamos el modelo y forzamos a la tabla a regenerar sus columnas visuales
        tablaTotalProductos.setModel(modelo);
        tablaTotalProductos.setAutoCreateColumnsFromModel(true);

        NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        SimpleDateFormat sdfSql = new SimpleDateFormat("yyyy-MM-dd");

        // 2. SQL OPTIMIZADO PARA SQLITE (Opción 2: Evita el error del día menos)
        StringBuilder sql = new StringBuilder("SELECT strftime('%d-%m-%Y', p.fecha) as fechaFormateada, "
                + "p.importe, p.facturaafectada, p.tipo, c.nombre, p.metodo "
                + "FROM pagos p JOIN cliente c ON p.idcliente = c.codigocliente WHERE 1=1 ");

        if (desde != null) {
            sql.append(" AND DATE(p.fecha) >= DATE(?) ");
        }
        if (hasta != null) {
            sql.append(" AND DATE(p.fecha) <= DATE(?) ");
        }
        if (cliente != null && cliente > 0) {
            sql.append(" AND p.idcliente = ? ");
        }

        if (facturaBuscar != null && facturaBuscar > 0) {
            sql.append(" AND EXISTS (SELECT 1 FROM json_each(p.facturaafectada) WHERE json_each.value = ?) ");
        }

        if (condicion != null && !condicion.equals("TODOS")) {
            sql.append(" AND p.tipo = ? ");
        }
        sql.append(" ORDER BY p.fecha DESC;");

        Map<Integer, String[]> opcionesporfila = new HashMap<>();

        try (Connection conn = objetoConexion.estableceConexion(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int pIndex = 1;
            if (desde != null) {
                ps.setString(pIndex++, sdfSql.format(desde));
            }
            if (hasta != null) {
                ps.setString(pIndex++, sdfSql.format(hasta));
            }
            if (cliente != null && cliente > 0) {
                ps.setInt(pIndex++, cliente);
            }
            if (facturaBuscar != null && facturaBuscar > 0) {
                ps.setInt(pIndex++, facturaBuscar);
            }
            if (condicion != null && !condicion.equals("TODOS")) {
                ps.setString(pIndex++, condicion);
            }

            ResultSet rs = ps.executeQuery();
            int contadorFilas = 0;

            while (rs.next()) {
                String facturaRaw = rs.getString("facturaafectada");
                String[] arrayFacturas = {"-"};

                if (facturaRaw != null && facturaRaw.length() > 2) {
                    // Limpieza de corchetes del String proveniente de SQLite
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
                    arrayFacturas[0], // Muestra el primero, el combo hará el resto
                     rs.getString("metodo")
                });

                opcionesporfila.put(contadorFilas, arrayFacturas);
                contadorFilas++;
            }

            // 3. CONFIGURACIÓN VISUAL FINAL (Dentro del mismo método)
            if (!opcionesporfila.isEmpty()) {
                tablaTotalProductos.getColumnModel().getColumn(4).setCellEditor(new ComboPorFilaEditor(opcionesporfila));
            }

            for (int i = 0; i < tablaTotalProductos.getColumnCount(); i++) {
                Class<?> colClas = tablaTotalProductos.getColumnClass(i);
                tablaTotalProductos.setDefaultEditor(colClas, null);
            }

            tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(158);
            tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(74);
            tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(140);
            tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(160);
            tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(60);

            //     tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(new ControladorFacturas.paddingRig());
            DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
            cent.setHorizontalAlignment(JLabel.CENTER);
            tablaTotalProductos.getColumnModel().getColumn(1).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(new paddingRig());

            tablaTotalProductos.setRowHeight(25);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar pagos: " + e.getMessage());
        } finally {
            objetoConexion.cerrarConexion();
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

    Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
    
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

    // SQL Corregido para SQLite: Comparamos fechas en formato ISO YYYY-MM-DD
    StringBuilder sql = new StringBuilder(
            "SELECT strftime('%d-%m-%Y', f.fechaFactura) as fechaF, "
            + "p.descripcion, d.cantidad, d.precioventa, "
            + "(d.cantidad * d.precioventa) as subtotal, f.codigo "
            + "FROM producto p "
            + "JOIN detalle d ON p.codigoproducto = d.fkproducto "
            + "JOIN factura f ON d.fkfactura = f.codigo WHERE 1=1 ");

    // FILTROS: Aseguramos que SQLite compare DATE contra DATE
    if (desde != null) {
        sql.append(" AND date(f.fechaFactura) >= date(?) ");
    }
    if (hasta != null) {
        sql.append(" AND date(f.fechaFactura) <= date(?) ");
    }
    if (codigoProducto != null && codigoProducto > 0) {
        sql.append(" AND p.codigoproducto = ? ");
    }
    if (codigofactura != null && codigofactura > 0) {
        sql.append(" AND f.codigo = ? ");
    }

    sql.append("and f.estado='ACTIVA' ORDER BY f.fechaFactura ASC;");

    // Usamos Try-with-resources para asegurar el cierre de conexión
    try (java.sql.Connection con = objetoConexion.estableceConexion(); 
         java.sql.PreparedStatement pst = con.prepareStatement(sql.toString())) {

        int it = 1;
        if (desde != null) {
            // Pasamos la fecha como String ISO (YYYY-MM-DD) para evitar fallos de driver
            pst.setString(it++, desde.toString());
        }
        if (hasta != null) {
            pst.setString(it++, hasta.toString());
        }
        if (codigoProducto != null && codigoProducto > 0) {
            pst.setInt(it++, codigoProducto);
        }
        if (codigofactura != null && codigofactura > 0) {
            pst.setInt(it++, codigofactura);
        }

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

        // Configuración visual de la tabla
        tablaTotalProductos.setModel(modelo);
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Renderizadores y anchos
        DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
        cent.setHorizontalAlignment(JLabel.CENTER);
        
        int[] anchos = {88, 152, 76, 142, 142, 64};
        for (int i = 0; i < anchos.length; i++) {
            tablaTotalProductos.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(5).setCellRenderer(cent);
        
        // Asumiendo que paddingRig es una clase externa que ya tenés creada
        tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(new paddingRig());
        tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(new paddingRig());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error en consulta: " + e.getMessage());
        e.printStackTrace();
    } finally {
        // El try-with-resources ya cierra Connection y PreparedStatement,
        // pero llamamos a tu método por si tiene lógica extra de limpieza.
        objetoConexion.cerrarConexion();
    }
}


  public void consultaTotales(JTable tablaTotalProductos,
        String condicion, java.util.Date desde, java.util.Date hasta, Integer codigoProducto) {

    Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };

    // Columnas (0 a 5)
    modelo.addColumn("COD.");
    modelo.addColumn("DESCRIPCION");
    modelo.addColumn("CANT. TOTAL");
    modelo.addColumn("COSTO TOTAL");
    modelo.addColumn("INGRESO FINAL");
    modelo.addColumn("GANANCIA");

    NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    SimpleDateFormat sdfSql = new SimpleDateFormat("yyyy-MM-dd");

    double granTotalUnidades = 0, granTotalCosto = 0, granTotalIngreso = 0, granTotalGanancia = 0;

    StringBuilder sql = new StringBuilder("SELECT p.codigoproducto, p.descripcion, "
            + "SUM(d.cantidad) as total_unidades, "
            + "SUM(d.cantidad * d.preciocompra) as costo_totales, "
            + "SUM(d.cantidad * d.precioventa) as ingreso_totales, "
            + "SUM(d.cantidad * (d.precioventa - d.preciocompra)) as ganancia_total "
            + "FROM producto p "
            + "JOIN detalle d ON p.codigoproducto = d.fkproducto "
            + "JOIN factura f ON d.fkfactura = f.codigo "
            + "WHERE 1=1 ");

    if (desde != null) sql.append(" AND DATE(f.fechaFactura) >= DATE(?) ");
    if (hasta != null) sql.append(" AND DATE(f.fechaFactura) <= DATE(?) ");
    if (codigoProducto != null && codigoProducto > 0) sql.append(" AND p.codigoproducto = ? ");

    sql.append("and f.estado='ACTIVA' GROUP BY p.codigoproducto, p.descripcion ORDER BY total_unidades DESC;");

    try (Connection conn = objetoConexion.estableceConexion(); 
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int pIndex = 1;
        if (desde != null) ps.setString(pIndex++, sdfSql.format(desde));
        if (hasta != null) ps.setString(pIndex++, sdfSql.format(hasta));
        if (codigoProducto != null && codigoProducto > 0) ps.setInt(pIndex++, codigoProducto);

        ResultSet rs = ps.executeQuery();
        DecimalFormat df = new DecimalFormat("#.00");

        while (rs.next()) {
            double unidades = rs.getDouble("total_unidades");
            double costo = rs.getDouble("costo_totales");
            double ingreso = rs.getDouble("ingreso_totales");
            double ganancia = rs.getDouble("ganancia_total");

            granTotalUnidades += unidades;
            granTotalCosto += costo;
            granTotalIngreso += ingreso;
            granTotalGanancia += ganancia;

            modelo.addRow(new Object[]{
                rs.getInt("codigoproducto"), rs.getString("descripcion"), df.format(unidades),
                moneda.format(costo), moneda.format(ingreso), moneda.format(ganancia)
            });
        }

        // AGREGAMOS LA FILA DE TOTALES
        modelo.addRow(new Object[]{ "", "TOTALES:", df.format(granTotalUnidades),
            moneda.format(granTotalCosto), moneda.format(granTotalIngreso), moneda.format(granTotalGanancia)
        });

        tablaTotalProductos.setModel(modelo);

        // --- RENDERIZADOR PARA COLOR GRIS ---
        DefaultTableCellRenderer renderizadorColor = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Si la celda de la columna 1 dice "TOTALES GENERALES"
                String nombreFila = table.getValueAt(row, 1).toString();
                
                if (nombreFila.equals("TOTALES GENERALES")) {
                    c.setBackground(new Color(220, 220, 220)); // Gris claro [Oracle Color API](https://docs.oracle.com)
                    c.setFont(c.getFont().deriveFont(Font.BOLD)); // Negrita para que resalte
                } else {
                    c.setBackground(table.getBackground());
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }

                // Mantener alineación según la columna
                if (column == 3 || column == 4 || column == 5) {
                    setHorizontalAlignment(JLabel.RIGHT);
                } else {
                    setHorizontalAlignment(JLabel.CENTER);
                }

                if (isSelected) c.setBackground(table.getSelectionBackground());

                return c;
            }
        };

        // Aplicamos el renderizador a todas las columnas
        for (int i = 0; i < tablaTotalProductos.getColumnCount(); i++) {
            tablaTotalProductos.getColumnModel().getColumn(i).setCellRenderer(renderizadorColor);
        }

        // Configuración de anchos [Baeldung JTable Column Width](https://www.baeldung.com)
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(46);
        tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(176);
        tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(117);
        tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(117);
        tablaTotalProductos.getColumnModel().getColumn(5).setPreferredWidth(117);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.toString());
    }
}

    public void consultaTotalesCliente(JTable tablaTotalProductos,
            String condicion, java.util.Date desde, java.util.Date hasta, Integer idCliente) {

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        // 1. Modelo no editable para evitar errores al hacer clic
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

        // 2. SQL Optimizado para SQLite con JOINs corregidos
        StringBuilder sql = new StringBuilder("SELECT c.codigocliente, c.nombre, p.descripcion, "
                + "SUM(d.cantidad) as total_comprado, "
                + "SUM(d.cantidad * d.precioventa) as importe_comprado "
                + "FROM cliente c "
                + "JOIN factura f ON f.cod_cliente = c.codigocliente "
                + "JOIN detalle d ON d.fkfactura = f.codigo "
                + "JOIN producto p ON d.fkproducto = p.codigoproducto "
                + "WHERE 1=1 ");

        // Filtros de fecha (Opción 2: Ignorar hora para incluir el día completo)
        if (desde != null) {
            sql.append(" AND DATE(f.fechaFactura) >= DATE(?) ");
        }
        if (hasta != null) {
            sql.append(" AND DATE(f.fechaFactura) <= DATE(?) ");
        }

        // Filtro por Cliente específico
        if (idCliente != null && idCliente > 0) {
            sql.append(" AND c.codigocliente = ? ");
        }

        sql.append(" and f.estado='ACTIVA' GROUP BY c.codigocliente, p.codigoproducto, p.descripcion "
                + " ORDER BY c.nombre ASC, total_comprado DESC;");

        try (Connection conn = objetoConexion.estableceConexion(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int pIndex = 1;
            if (desde != null) {
                ps.setString(pIndex++, sdfSql.format(desde));
            }
            if (hasta != null) {
                ps.setString(pIndex++, sdfSql.format(hasta));
            }
            if (idCliente != null && idCliente > 0) {
                ps.setInt(pIndex++, idCliente);
            }

            ResultSet rs = ps.executeQuery();
    DecimalFormat df = new DecimalFormat("#,##0.00");


while (rs.next()) {
    modelo.addRow(new Object[]{
        rs.getInt("codigocliente"),
        rs.getString("nombre"),
        rs.getString("descripcion"),
        df.format(rs.getDouble("total_comprado")), // Aplicado el df aquí
        moneda.format(rs.getDouble("importe_comprado"))
    });
}

   // Asignamos el modelo a la tabla
            tablaTotalProductos.setModel(modelo);

            for (int i = 0; i < tablaTotalProductos.getColumnCount(); i++) {
                Class<?> colClas = tablaTotalProductos.getColumnClass(i);
                tablaTotalProductos.setDefaultEditor(colClas, null);
            }

            tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(60);
            tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(177);
            tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(177);
            tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(80);
            tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(171);
      
            //     tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(new ControladorFacturas.paddingRig());
            DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
            cent.setHorizontalAlignment(JLabel.CENTER);
            tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
  tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(new paddingRig());
            //    tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(80);  
            tablaTotalProductos.setAutoCreateColumnsFromModel(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en consulta: " + e.getMessage());
        } finally {
            objetoConexion.cerrarConexion();
        }
    }

    
    
    
    public void consultaDetallePesosPorCliente(JTable tablaTotalProductos, 
        java.util.Date desde, java.util.Date hasta, Integer idCliente) {

    Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };

    // Columnas solicitadas
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

    // SQL que vincula stock_detalle para rastrear la pieza exacta
    StringBuilder sql = new StringBuilder(
            "SELECT f.codigo as nro_factura, c.nombre, p.descripcion, d.cantidad, f.fechaFactura, f.montototal " +
            "FROM factura f " +
            "JOIN cliente c ON f.cod_cliente = c.codigocliente " +
            "JOIN detalle d ON d.fkfactura = f.codigo " +
            "JOIN producto p ON d.fkproducto = p.codigoproducto " +
            "LEFT JOIN stock_detalle sd ON d.fk_stock_detalle = sd.id " + // Vinculo con la pieza
            "WHERE f.estado = 'ACTIVA' ");

    if (desde != null) sql.append(" AND DATE(f.fechaFactura) >= DATE(?) ");
    if (hasta != null) sql.append(" AND DATE(f.fechaFactura) <= DATE(?) ");
    if (idCliente != null && idCliente > 0) sql.append(" AND c.codigocliente = ? ");

    sql.append(" ORDER BY f.codigo DESC");

    try (Connection conn = objetoConexion.estableceConexion(); 
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int pIndex = 1;
        if (desde != null) ps.setString(pIndex++, sdfSql.format(desde));
        if (hasta != null) ps.setString(pIndex++, sdfSql.format(hasta));
        if (idCliente != null && idCliente > 0) ps.setInt(pIndex++, idCliente);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String fechaFormateada = "";
            try {
                // Ajuste por si el driver devuelve el objeto fecha directamente
                fechaFormateada = sdfSalida.format(rs.getTimestamp("fechaFactura"));
            } catch (Exception e) { fechaFormateada = rs.getString("fechaFactura"); }

            modelo.addRow(new Object[]{
                rs.getInt("nro_factura"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                df.format(rs.getDouble("cantidad")),
                fechaFormateada,
                moneda.format(rs.getDouble("montototal"))
            });
        }

        tablaTotalProductos.setModel(modelo);
        
        // Diseño con las medidas de tu modelo anterior
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(60);  // COD. FACT
        tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(140); // CLIENTE
        tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(140); // PRODUCTO
        tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(80);  // KG
        tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(120); // FECHA (ajustada para el formato)
        tablaTotalProductos.getColumnModel().getColumn(5).setPreferredWidth(120); // IMPORTE

        // Renderers de alineación
        DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
        cent.setHorizontalAlignment(JLabel.CENTER);
        tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(cent);
        tablaTotalProductos.getColumnModel().getColumn(5).setCellRenderer(new paddingRig());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error en reporte de pesos: " + e.getMessage());
    } finally {
        objetoConexion.cerrarConexion();
    }
}
    
    
    
    public void consultaTotalesCliente(JTable tablaTotalProductos,
            java.util.Date desde, java.util.Date hasta, Integer idCliente) {

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        // 1. Modelo no editable para mayor estabilidad visual
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

        // 2. SQL con JOINs y Filtros para SQLite
        StringBuilder sql = new StringBuilder("SELECT c.codigocliente, c.nombre, p.descripcion, "
                + "SUM(d.cantidad) as total_comprado, "
                + "SUM(d.cantidad * d.precioventa) as importe_comprado "
                + "FROM cliente c "
                + "JOIN factura f ON f.cod_cliente = c.codigocliente "
                + "JOIN detalle d ON d.fkfactura = f.codigo "
                + "JOIN producto p ON d.fkproducto = p.codigoproducto "
                + "WHERE f.pagado = 0 "); // Solo facturas en cuenta/pendientes o todas según prefieras

        // Filtros de fecha (Ignora la hora para incluir el día completo)
        if (desde != null) {
            sql.append(" AND date(f.fechaFactura) >= date(?) ");
        }
        if (hasta != null) {
            sql.append(" AND date(f.fechaFactura) <= date(?) ");
        }

        // --- FILTRO POR CLIENTE ---
        if (idCliente != null && idCliente > 0) {
            sql.append(" AND c.codigocliente = ? ");
        }

        sql.append(" and f.estado='ACTIVA' GROUP BY c.codigocliente, p.codigoproducto, p.descripcion "
                + " ORDER BY total_comprado DESC;");

        try (Connection conn = objetoConexion.estableceConexion(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int pIndex = 1;
            if (desde != null) {
                ps.setString(pIndex++, sdfSql.format(desde));
            }
            if (hasta != null) {
                ps.setString(pIndex++, sdfSql.format(hasta));
            }

            // Seteo del parámetro cliente
            if (idCliente != null && idCliente > 0) {
                ps.setInt(pIndex++, idCliente);
            }

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

            for (int i = 0; i < tablaTotalProductos.getColumnCount(); i++) {
                Class<?> colClas = tablaTotalProductos.getColumnClass(i);
                tablaTotalProductos.setDefaultEditor(colClas, null);
            }

           // Asignamos el modelo a la tabla
            tablaTotalProductos.setModel(modelo);

            for (int i = 0; i < tablaTotalProductos.getColumnCount(); i++) {
                Class<?> colClas = tablaTotalProductos.getColumnClass(i);
                tablaTotalProductos.setDefaultEditor(colClas, null);
            }

            tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(48);
            tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(74);
            tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(126);
            tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(132);
            tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(172);
            tablaTotalProductos.getColumnModel().getColumn(5).setPreferredWidth(126);
            //     tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(new ControladorFacturas.paddingRig());
            DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
            cent.setHorizontalAlignment(JLabel.CENTER);
            tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(5).setCellRenderer(cent);

            //    tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(80);  
            tablaTotalProductos.setAutoCreateColumnsFromModel(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en consulta: " + e.getMessage());
        } finally {
            objetoConexion.cerrarConexion();
        }
    }

    public void consultaDeudaTotal(JTable tablaTotalProductos,
            String condicion, java.util.Date desde, java.util.Date hasta, Integer idCliente) {

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
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
        SimpleDateFormat sdfSalida = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", new Locale("es", "AR"));

        // 1. SQL dinámico con filtro por cliente
        StringBuilder sql = new StringBuilder("SELECT c.codigocliente, c.nombre, "
                + "SUM(f.montototal - f.adelanto - f.posteriores) as saldo, "
                + "MIN(date(f.fechaFactura, '+' || c.diaspago || ' day')) as vencimiento "
                + "FROM factura as f INNER JOIN cliente as c ON f.cod_cliente = c.codigocliente "
                + "WHERE f.pagado = 0 ");

        if (desde != null) {
            sql.append(" AND date(f.fechaFactura) >= date(?) ");
        }
        if (hasta != null) {
            sql.append(" AND date(f.fechaFactura) <= date(?) ");
        }

        // --- FILTRO POR ID DE CLIENTE ---
        if (idCliente != null && idCliente > 0) {
            sql.append(" AND c.codigocliente = ? ");
        }

        sql.append(" and f.estado='ACTIVA' GROUP BY c.codigocliente, c.nombre ");
        sql.append(" HAVING saldo > 0 ORDER BY vencimiento ASC;");

        try (Connection conn = objetoConexion.estableceConexion(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int pIndex = 1;
            if (desde != null) {
                ps.setString(pIndex++, sdfSql.format(desde));
            }
            if (hasta != null) {
                ps.setString(pIndex++, sdfSql.format(hasta));
            }

            // Seteo del parámetro cliente
            if (idCliente != null && idCliente > 0) {
                ps.setInt(pIndex++, idCliente);
            }

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

                tablaTotalProductos.setModel(modelo);

                for (int i = 0; i < tablaTotalProductos.getColumnCount(); i++) {
                    Class<?> colClas = tablaTotalProductos.getColumnClass(i);
                    tablaTotalProductos.setDefaultEditor(colClas, null);
                }
                tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(66);
                tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(210);
                tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(151);
                tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(226);

                //     tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(new ControladorFacturas.paddingRig());
                DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
                cent.setHorizontalAlignment(JLabel.CENTER);
                tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(cent);
                tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
                tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(cent);
                tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(new paddingRig());

                tablaTotalProductos.setAutoCreateColumnsFromModel(false);
            }
        } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "18 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
    }

    public void consultaDeudaporCliente(JTable tablaTotalProductos,
            String condicion, java.util.Date desde, java.util.Date hasta, Integer cliente, Integer factura) {

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();

        // Definimos el modelo para que las celdas NO sean editables
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Columnas del modelo
        modelo.addColumn("COD");
        modelo.addColumn("FECHA");
        modelo.addColumn("IMPORTE");
        modelo.addColumn("CLIENTE");
        modelo.addColumn("VENCIMIENTO");
        modelo.addColumn("SALDO");

        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        SimpleDateFormat formatoFechaLargo = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", new Locale("es", "AR"));
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd"); // Formato estándar SQLite

        // SQL compatible con SQLite y corrección de "día menos" usando date()
        StringBuilder sql = new StringBuilder("SELECT c.codigocliente, f.montototal, f.codigo, ")
                .append("strftime('%d-%m-%Y', f.fechaFactura) AS fechaFacturaVisible, ")
                .append("DATE(f.fechaFactura, '+' || c.diaspago || ' days') AS vencimiento, ")
                .append("c.nombre, (f.montototal - IFNULL(f.adelanto, 0) - IFNULL(f.posteriores, 0)) AS saldo ")
                .append("FROM factura AS f ")
                .append("INNER JOIN cliente AS c ON f.cod_cliente = c.codigocliente ")
                .append("WHERE f.pagado = 0 ");

        // Filtros con PreparedStatement para evitar errores de formato y el "día menos"
        if (desde != null) {
            sql.append(" AND date(f.fechaFactura) >= date(?) ");
        }
        if (hasta != null) {
            sql.append(" AND date(f.fechaFactura) <= date(?) ");
        }

        if (cliente != null && cliente > 0) {
            sql.append(" AND c.codigocliente = ? ");
        }
        if (factura != null && factura > 0) {
            sql.append(" AND f.codigo = ? ");
        }

 if ("VENCIDO".equals(condicion)) {
    // Compara la fecha de vencimiento calculada contra la fecha actual del sistema
    sql.append(" AND DATE(f.fechaFactura, '+' || c.diaspago || ' days') < DATE('now', 'localtime') ");
}
        sql.append(" and f.estado='ACTIVA' ORDER BY vencimiento ASC");

        try (Connection conn = objetoConexion.estableceConexion(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Seteo dinámico de parámetros
            int pIndex = 1;
            if (desde != null) {
                ps.setString(pIndex++, isoFormat.format(desde));
            }
            if (hasta != null) {
                ps.setString(pIndex++, isoFormat.format(hasta));
            }
            if (cliente != null && cliente > 0) {
                ps.setInt(pIndex++, cliente);
            }
            if (factura != null && factura > 0) {
                ps.setInt(pIndex++, factura);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int codigoC = rs.getInt("codigocliente");
                if (codigoC > 0) {
                    int codigo = rs.getInt("codigo");
                    String nombre = rs.getString("nombre");
                    double total = rs.getDouble("montototal");
                    double saldo = rs.getDouble("saldo");
                    String fechaFactura = rs.getString("fechaFacturaVisible");

                    // Procesar vencimiento: SQLite lo devuelve como String YYYY-MM-DD
                    String vencimientoRaw = rs.getString("vencimiento");
                    String fechaVencimientoFormateada = "Sin fecha";

                    if (vencimientoRaw != null) {
                        try {
                            java.util.Date dateVenc = isoFormat.parse(vencimientoRaw);
                            fechaVencimientoFormateada = formatoFechaLargo.format(dateVenc);
                        } catch (Exception e) {
                            fechaVencimientoFormateada = vencimientoRaw;
                        }
                    }

                    modelo.addRow(new Object[]{
                        codigo,
                        fechaFactura,
                        formatoMoneda.format(total),
                        nombre,
                        fechaVencimientoFormateada,
                        formatoMoneda.format(saldo)
                    });
                }
            }

            // Asignamos el modelo a la tabla
            tablaTotalProductos.setModel(modelo);

            for (int i = 0; i < tablaTotalProductos.getColumnCount(); i++) {
                Class<?> colClas = tablaTotalProductos.getColumnClass(i);
                tablaTotalProductos.setDefaultEditor(colClas, null);
            }

            tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(48);
            tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(74);
            tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(126);
            tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(123);
            tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(163);
            tablaTotalProductos.getColumnModel().getColumn(5).setPreferredWidth(125);
            //     tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(new ControladorFacturas.paddingRig());
            DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
            cent.setHorizontalAlignment(JLabel.CENTER);
            tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(5).setCellRenderer(cent);

            //    tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(80);  
            tablaTotalProductos.setAutoCreateColumnsFromModel(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en consulta: " + e.getMessage());
        } finally {
            objetoConexion.cerrarConexion();
        }
    }

public void reporteUnico(JLabel titulo, JLabel tarjeta, JLabel efectivo, JLabel corriente, 
                         JLabel total, int num, JLabel transferencia, JLabel gananciaLabel) {
    
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
    
    String filtroTiempo = "";
    switch (num) {
        case 1:
            filtroTiempo = "date(f.fechaFactura) = date('now', 'localtime')";
            titulo.setText("RESUMEN DE VENTA DIARIO");
            break;
        case 2:
            filtroTiempo = "strftime('%Y-%W', f.fechaFactura) = strftime('%Y-%W', 'now')";
            titulo.setText("RESUMEN DE VENTA SEMANAL");
            break;
        case 3:
            filtroTiempo = "strftime('%m-%Y', f.fechaFactura) = strftime('%m-%Y', 'now')";
            titulo.setText("RESUMEN DE VENTA MENSUAL");
            break;
        case 4:
            filtroTiempo = "strftime('%Y', f.fechaFactura) = strftime('%Y', 'now')";
            titulo.setText("RESUMEN DE VENTA ANUAL");
            break;
    }

    // Se agrega f.estado = 'ACTIVA' en ambos bloques del UNION
    String sql = "SELECT metodopago AS mediopago, SUM(monto_f) AS totalvendido, SUM(ganancia_f) AS totalganancia, COUNT(DISTINCT id_f) AS cantidad "
               + "FROM ("
               + "  SELECT f.codigo AS id_f, f.metodopago, f.montototal AS monto_f, "
               + "  SUM((d.precioventa - d.preciocompra) * d.cantidad) AS ganancia_f "
               + "  FROM factura f "
               + "  LEFT JOIN detalle d ON f.codigo = d.fkfactura "
               + "  WHERE " + filtroTiempo + " AND f.estado = 'ACTIVA' " // <--- FILTRO AQUÍ
               + "  GROUP BY f.codigo"
               + ") GROUP BY metodopago "
               + "UNION ALL "
               + "SELECT 'total' AS mediopago, SUM(monto_f), SUM(ganancia_f), COUNT(DISTINCT id_f) "
               + "FROM ("
               + "  SELECT f.codigo AS id_f, f.montototal AS monto_f, "
               + "  SUM((d.precioventa - d.preciocompra) * d.cantidad) AS ganancia_f "
               + "  FROM factura f "
               + "  LEFT JOIN detalle d ON f.codigo = d.fkfactura "
               + "  WHERE " + filtroTiempo + " AND f.estado = 'ACTIVA' " // <--- FILTRO AQUÍ
               + "  GROUP BY f.codigo"
               + ");";

    try {
        Statement st = objetoConexion.estableceConexion().createStatement();
        ResultSet rs = st.executeQuery(sql);

        // Reset de labels (Valores por defecto)
        String cero = " ( 0 )   " + formatoMoneda.format(0);
        corriente.setText(cero); 
        efectivo.setText(cero); 
        tarjeta.setText(cero);
        transferencia.setText(cero); 
        total.setText(cero); 
        gananciaLabel.setText(formatoMoneda.format(0));

        while (rs.next()) {
            String medio = rs.getString("mediopago");
            double totalVendido = rs.getDouble("totalvendido");
            double totalGanancia = rs.getDouble("totalganancia");
            int cant = rs.getInt("cantidad");

            String textoFormateado = " ( " + cant + " )   " + formatoMoneda.format(totalVendido);

            if (medio == null) continue;

            switch (medio) {
                case "Cuenta Corriente": corriente.setText(textoFormateado); break;
                case "Contado": efectivo.setText(textoFormateado); break;
                case "Tarjeta": tarjeta.setText(textoFormateado); break;
                case "Transferencia": transferencia.setText(textoFormateado); break;
                case "total": 
                    total.setText(textoFormateado); 
                    gananciaLabel.setText(formatoMoneda.format(totalGanancia));
                    break;
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error en reporte: " + e.getMessage());
    } finally {
        objetoConexion.cerrarConexion();
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
