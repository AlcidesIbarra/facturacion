/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import configuracion.Conexion;
import java.text.SimpleDateFormat;
import java.sql.Connection;


/**
 *
 * @author ALCIDES
 */
public class ControladorFacturas {

public void muestraFacturas(JTable tablaTotalProductos,
        String condicion, java.sql.Date desde, java.sql.Date hasta,
        Integer cliente, Integer factura) {

    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    modelo.addColumn("NUM");
    modelo.addColumn("CLIENTE");
    modelo.addColumn("FECHA");
    modelo.addColumn("IMPORTE");
    modelo.addColumn("PAGO");
    modelo.addColumn("OBSERVACIONES");

    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // 1. CAMBIO CRÍTICO: Usamos LEFT JOIN para que la factura se muestre SIEMPRE
    // Usamos IFNULL para mostrar "Consumidor Final" si el cliente no existe
    StringBuilder sql = new StringBuilder(
        "SELECT f.codigo, f.fechaFactura, f.metodopago, f.observaciones, f.montototal, " +
        "IFNULL(c.nombre, 'Consumidor Final') as nombreCliente " +
        "FROM factura f " +
        "LEFT JOIN cliente c ON f.cod_cliente = c.codigocliente " +
        "WHERE f.estado = 'ACTIVA' "
    );

    // 2. Filtros dinámicos
    if (desde != null) sql.append(" AND f.fechaFactura >= ? ");
    if (hasta != null) sql.append(" AND f.fechaFactura <= ? ");
    if (cliente != null && cliente > 0) sql.append(" AND f.cod_cliente = ? ");
    if (factura != null && factura > 0) sql.append(" AND f.codigo = ? ");
    if (condicion != null && !condicion.equalsIgnoreCase("TODOS")) sql.append(" AND f.metodopago = ? ");

    sql.append(" ORDER BY f.codigo DESC");

    Conexion objetoConexion = new Conexion();

    try (Connection conn = objetoConexion.estableceConexion();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int pIndex = 1;
        if (desde != null) ps.setString(pIndex++, sdf.format(desde) + " 00:00:00");
        if (hasta != null) ps.setString(pIndex++, sdf.format(hasta) + " 23:59:59");
        if (cliente != null && cliente > 0) ps.setInt(pIndex++, cliente);
        if (factura != null && factura > 0) ps.setInt(pIndex++, factura);
        if (condicion != null && !condicion.equalsIgnoreCase("TODOS")) ps.setString(pIndex++, condicion);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String fechaOriginal = rs.getString("fechaFactura");
            String fechaFormateada = "";
            
            try {
                // Ajuste para procesar fechas con o sin hora
                String limpia = fechaOriginal.split(" ")[0];
                java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(limia);
                fechaFormateada = new SimpleDateFormat("dd-MM-yyyy").format(date);
            } catch (Exception ex) {
                fechaFormateada = fechaOriginal; 
            }

            modelo.addRow(new Object[]{
                rs.getInt("codigo"),
                rs.getString("nombreCliente"), // Usamos el alias con IFNULL
                fechaFormateada,
                formatoMoneda.format(rs.getDouble("montototal")),
                rs.getString("metodopago"),
                rs.getString("observaciones")
            });
        }

        tablaTotalProductos.setModel(modelo);

        // 3. Configuración visual
        tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] widths = {70, 200, 100, 120, 110, 180};
        for (int col = 0; col < widths.length; col++) {
            if(col < tablaTotalProductos.getColumnCount()){
                tablaTotalProductos.getColumnModel().getColumn(col).setPreferredWidth(widths[col]);
            }
        }

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(center);
        tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(center);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al cargar facturas: " + e.getMessage());
        e.printStackTrace();
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
    
    
    public Integer verDetalle(JTable tbResumenVenta) {

        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();

        int numero = 0;
        try {
            int indiceSelleccionado = tbResumenVenta.getSelectedRow();
            if (indiceSelleccionado != -1) {

                numero = Integer.parseInt(tbResumenVenta.getValueAt(indiceSelleccionado, 0).toString());

            } else {
             //   JOptionPane.showMessageDialog(null, "seleccione una fila para ver");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error a seleccionar " + e.toString());
        }
        return numero;
    }

    public void ponerMaxFecha(JDateChooser calendar) {
        Calendar maxcalendar = Calendar.getInstance();
        maxcalendar.set(Calendar.HOUR_OF_DAY, 0);
        maxcalendar.set(Calendar.MINUTE, 0);
        maxcalendar.set(Calendar.SECOND, 0);
        maxcalendar.set(Calendar.MILLISECOND, 0);
        // Date hoy=maxcalendar.getTime();
        calendar.setMaxSelectableDate(maxcalendar.getTime());
    }

    public void ponerMinFecha(JDateChooser desde, JDateChooser hasta) {

        Calendar maxcalendar = desde.getCalendar();
        maxcalendar.set(Calendar.HOUR_OF_DAY, 0);
        maxcalendar.set(Calendar.MINUTE, 0);
        maxcalendar.set(Calendar.SECOND, 0);
        maxcalendar.set(Calendar.MILLISECOND, 0);
        // Date hoy=maxcalendar.getTime();
        hasta.setMinSelectableDate(maxcalendar.getTime());

    }

    public void ponerMaximaFecha(JDateChooser desde, JDateChooser hasta) {
        System.out.println(hasta.getCalendar().toString());
        if (hasta != null) {
            Calendar maxcalendar = hasta.getCalendar();
            maxcalendar.set(Calendar.HOUR_OF_DAY, 0);
            maxcalendar.set(Calendar.MINUTE, 0);
            maxcalendar.set(Calendar.SECOND, 0);
            maxcalendar.set(Calendar.MILLISECOND, 0);
            // Date hoy=maxcalendar.getTime();
            desde.setMaxSelectableDate(maxcalendar.getTime());
        }
    }
}

/*
    JTable tabla = new JTable(modelo){
        @Override
        public TableCellEditor getCellEditor(int row,int column){
            if(column==4){
            String []items =opcionesporfila.get(row);
            if(items!=null){
                return new DefaultCellEditor(new JComboBox<>(items));
            }
            }
            return super.getCellEditor(row,column);
        }
        };
        
        
        
        
        
        
        
            int contadorFilas=0;
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String fecha = rs.getString("fecha");
                Double importe = rs.getDouble("importe");
                String metodo = rs.getString("tipo");
                String factura = rs.getString("facturaafectada");
factura=factura.substring(1, factura.length()-1);
                String[] array=factura.split(",\\s*");
                for (int i = 0; i < array.length; i++) {
                    System.out.println("a"+array[i]+"a");
                }
                
                
                
                JComboBox<String> combo= new  JComboBox<>(array);
                
                modelo.addRow(new Object[]{nombre, fecha, formatoMoneda.format(importe), metodo,array[0]});
             opcionesporfila.put(contadorFilas, array);
            }
            tablaTotalProductos.setModel(modelo);
tablaTotalProductos.getColumnModel().getColumn(4).setCellEditor(new ComboPorFilaEditor(opcionesporfila));
*/
