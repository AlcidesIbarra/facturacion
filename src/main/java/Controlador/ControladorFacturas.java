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

/**
 *
 * @author ALCIDES
 */
public class ControladorFacturas {

    public void muestraFacturas(JTable tablaTotalProductos,
            String condicion, java.sql.Date desde, java.sql.Date hasta, Integer cliente,Integer factura ) {
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();
        DefaultTableModel modelo = new DefaultTableModel();

        System.out.println("    recibe cliente"+cliente);
        
     
        StringBuilder sql;
        modelo.addColumn("NUM");

        modelo.addColumn("CLIENTE");

        modelo.addColumn("FECHA");
        modelo.addColumn("IMPORTE");
        modelo.addColumn("PAGO");
        modelo.addColumn("OBSERVACIONES");
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        tablaTotalProductos.setModel(modelo);
//        sql = new StringBuilder("select date_format (cast(f.fechaFactura as date),"
        sql = new StringBuilder("select strftime('%d-%m-%Y',f.fechaFactura) as fechaFactura, f.codigo, f.metodopago, f.observaciones,"
                + "f.montototal,c.nombre from factura f inner join cliente as "
                + "c where 1=1 and f.estado='ACTIVA' ");

        if (desde != null) {
            sql.append(" AND DATE(f.fechaFactura) >= '" + desde + "' ");
        }
        if (hasta != null) {
            sql.append(" AND DATE(f.fechaFactura) <= '" + hasta + "' ");
        }

        if (cliente != null) {
            sql.append(" and f.cod_cliente=" + cliente);
        }
        
             if (factura != null) {
            sql.append(" and f.codigo like '%" + factura+"%'");
        }

        if (condicion.equals("TODOS")) {

            sql.append(" AND f.cod_cliente=c.codigocliente  order by f.fechaFactura desc;");

        } else {

            sql.append(" AND f.cod_cliente=c.codigocliente and f.metodopago like '"
                    + condicion + "' order by f.fechaFactura desc;");

        }

        try {
            Statement st = objetoConexion.estableceConexion().createStatement();
            ResultSet rs = st.executeQuery(sql.toString());
            System.out.println(sql.toString());
            while (rs.next()) {
                Integer numero = rs.getInt("codigo");
                String fecha = rs.getString("fechaFactura");
                String metodo = rs.getString("metodopago");
                String nombre = rs.getString("nombre");
                String obs = rs.getString("observaciones");
                Double importe = rs.getDouble("montototal");
                modelo.addRow(new Object[]{numero, nombre, fecha, formatoMoneda.format(importe), metodo, obs});
            }
            tablaTotalProductos.setModel(modelo);

            for (int i = 0; i < tablaTotalProductos.getColumnCount(); i++) {
                Class<?> colClas = tablaTotalProductos.getColumnClass(i);
                tablaTotalProductos.setDefaultEditor(colClas, null);
            }

            tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(40);
            tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(170);
            tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(80);
            tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(132);
            tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(110);
                tablaTotalProductos.getColumnModel().getColumn(5).setPreferredWidth(130);
              tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(new paddingRig());
              DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
                 cent.setHorizontalAlignment(JLabel.CENTER);
                    tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(cent);
                       tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);


            //    tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(80);  
            tablaTotalProductos.setAutoCreateColumnsFromModel(false);
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "19 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
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
