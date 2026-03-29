/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelos.Mensaje;
import configuracion.Conexion;
import java.awt.Component;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;

/**
 *
 * @author ALCIDES
 */
public class ControladorPagos {

  public Double listarDeudaCliente(int cliente, JTable tabla) {

    Double deudaFacturas = 0.0;

    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    modelo.addColumn("FACT");
    modelo.addColumn("FECHA");
    modelo.addColumn("VENCIMIENTO");
    modelo.addColumn("IMPORTE");
    modelo.addColumn("ADELANTO");
    modelo.addColumn("PAGOS");
    modelo.addColumn("SALDO");

    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    String sql = "SELECT f.montototal, f.codigo, f.acuenta, " +
            "strftime('%d-%m-%Y', f.fechaFactura) AS fechaFactura, " +
            "c.codigocliente, c.nombre, c.diaspago, f.posteriores, f.adelanto, " +
            "strftime('%d-%m-%Y', f.fechaFactura, '+' || c.diaspago || ' days') AS vencimiento, " +
            "(f.montototal - IFNULL(f.adelanto,0) - IFNULL(f.posteriores,0)) AS saldo " +
            "FROM factura f " +
            "INNER JOIN cliente c ON f.cod_cliente = c.codigocliente " +
            "WHERE f.pagado = 0 AND f.cod_cliente = ? AND f.estado = 'ACTIVA' " +
            "ORDER BY DATE(f.fechaFactura, '+' || c.diaspago || ' days') ASC";

try (Connection conn = Conexion.getConexion();
     PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, cliente);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int factura = rs.getInt("codigo");
                String fecha = rs.getString("fechaFactura");
                String vencimiento = rs.getString("vencimiento");

                double importe = rs.getDouble("montototal");
                double adelanto = rs.getDouble("adelanto");
                double pagos = rs.getDouble("posteriores");
                double saldo = rs.getDouble("saldo");

                deudaFacturas += saldo;

                modelo.addRow(new Object[]{
                        factura,
                        fecha,
                        vencimiento,
                        formatoMoneda.format(importe),
                        formatoMoneda.format(adelanto),
                        formatoMoneda.format(pagos),
                        formatoMoneda.format(saldo)
                });
            }
        }

        // 🔹 Cargar tabla solo si existe
        if (tabla != null) {
            tabla.setModel(modelo);

            // Bloquear edición
            for (int col = 0; col < tabla.getColumnCount(); col++) {
                tabla.setDefaultEditor(tabla.getColumnClass(col), null);
            }

            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            int[] widths = {55, 90, 90, 130, 130, 130, 130};
            for (int i = 0; i < widths.length; i++) {
                tabla.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }

            // Renderers
            DefaultTableCellRenderer center = new DefaultTableCellRenderer();
            center.setHorizontalAlignment(JLabel.CENTER);

            tabla.getColumnModel().getColumn(0).setCellRenderer(center);
            tabla.getColumnModel().getColumn(1).setCellRenderer(center);
            tabla.getColumnModel().getColumn(2).setCellRenderer(center);

            // Números alineados a la derecha
            for (int i = 3; i <= 6; i++) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(new paddingRig());
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
    }

    return deudaFacturas;
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

   public void SeleccionarCliente(JScrollPane contenedorCliente, JTable listaCliente, JLabel fieldNombre,
        JLabel fieldDni, JLabel deuda, JTable tabla, JTextField abona,
        JLabel cliente, JTextField buscador, JLabel deudaFacturas) {

    int fila = listaCliente.getSelectedRow();
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    if (fila < 0) return;

    try (Connection conn = Conexion.getConexion()) {

        Integer idSelecte = Integer.parseInt(listaCliente.getValueAt(fila, 1).toString());

        String consulta = "SELECT nombre, cuit, saldo_acumulado FROM cliente WHERE codigocliente = ?";
        try (PreparedStatement ps = conn.prepareStatement(consulta)) {

            ps.setInt(1, idSelecte);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    fieldNombre.setText(rs.getString("nombre"));
                    fieldDni.setText(rs.getString("cuit"));
                    deuda.setText(formatoMoneda.format(rs.getDouble("saldo_acumulado")));
                }
            }
        }

        cliente.setText(idSelecte.toString());
        buscador.setText(idSelecte.toString());

        contenedorCliente.setVisible(false);
        abona.requestFocus();

        Double deudaFac = listarDeudaCliente(idSelecte, tabla);
        deudaFacturas.setText(formatoMoneda.format(deudaFac));

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
    }
}
   
   
 public Mensaje realizarPago(Double dinero, JTable tabla, JLabel saldoCliente, JLabel cliente,
                           String metodo, JCheckBox chkAnterior, JLabel vuelto) {

    Mensaje mj = new Mensaje();
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    if (dinero == null || dinero <= 0) {
        mj.setCodigo(3);
        mj.setMensaje("El monto debe ser mayor a cero");
        return mj;
    }

    double valorDeuda;
    try {
        String limpia = saldoCliente.getText().replaceAll("[^0-9,]", "").replace(",", ".");
        valorDeuda = limpia.isEmpty() ? 0 : Double.parseDouble(limpia);
    } catch (Exception e) {
        valorDeuda = 0;
    }

    double saldoCl = valorDeuda;
    double dineroTotal = dinero;
    int idClienteInt = Integer.parseInt(cliente.getText());

    try (Connection con = Conexion.getConexion()) {

        con.setAutoCommit(false);

        if (!chkAnterior.isSelected()) {

            List<Integer> facturasAfectadas = new ArrayList<>();

            for (int i = 0; i < tabla.getRowCount() && dineroTotal > 0; i++) {

                int facturaId = Integer.parseInt(tabla.getValueAt(i, 0).toString());
                double saldoFactura = extraerDoubleDeTabla(tabla.getValueAt(i, 6).toString());
                double pagos = extraerDoubleDeTabla(tabla.getValueAt(i, 5).toString());

                double pago = Math.min(dineroTotal, saldoFactura);
                double nuevoSaldo = saldoFactura - pago;
                boolean pagado = nuevoSaldo == 0;

                String sqlFactura = "UPDATE factura SET posteriores=?, acuenta=?, pagado=? WHERE codigo=?";
                try (PreparedStatement ps = con.prepareStatement(sqlFactura)) {
                    ps.setDouble(1, pagos + pago);
                    ps.setDouble(2, nuevoSaldo);
                    ps.setBoolean(3, pagado);
                    ps.setInt(4, facturaId);
                    ps.executeUpdate();
                }

                dineroTotal -= pago;
                saldoCl -= pago;
                facturasAfectadas.add(facturaId);
            }

            // Registrar pago
            String sqlPago = "INSERT INTO pagos(fecha, importe, idcliente, facturaafectada, tipo, metodo) VALUES(?,?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sqlPago)) {

                String fecha = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                ps.setString(1, fecha);
                ps.setDouble(2, dinero);
                ps.setInt(3, idClienteInt);
                ps.setString(4, facturasAfectadas.toString());
                ps.setString(5, "PAGO");
                ps.setString(6, metodo);

                ps.executeUpdate();
            }

            mj.setCodigo(1);
            mj.setMensaje("Pago realizado");

        } else {
            // Pago externo
            Double deudaFacturas = listarDeudaCliente(idClienteInt, null);
            double diferencia = valorDeuda - deudaFacturas;

            if (diferencia > 0) {

                if (dinero <= diferencia) {
                    saldoCl = valorDeuda - dinero;
                } else {
                    saldoCl = valorDeuda - diferencia;

                    double vlto = dinero - diferencia;
                    vuelto.setText(formatoMoneda.format(vlto));

                    mj.setCodigo(2);
                    mj.setMensaje("Pago parcial (sobrante generado)");
                }

            } else {
                throw new Exception("No hay deuda externa");
            }
        }

        // Update cliente
        String sqlCliente = "UPDATE cliente SET saldo_acumulado=? WHERE codigocliente=?";
        try (PreparedStatement ps = con.prepareStatement(sqlCliente)) {
            ps.setDouble(1, saldoCl);
            ps.setInt(2, idClienteInt);
            ps.executeUpdate();
        }

        con.commit();

        saldoCliente.setText(formatoMoneda.format(saldoCl));

    } catch (Exception e) {
        mj.setCodigo(4);
        mj.setMensaje("Error: " + e.getMessage());
    }

    // refrescar tabla
    listarDeudaCliente(idClienteInt, tabla);

    return mj;
}
 
 
 
// Función auxiliar para limpiar el formato $ de las celdas antes de procesar
    private double extraerDoubleDeTabla(String valor) {
        try {
            String limpia = valor.replaceAll("[^0-9,]", "").replace(",", ".");
            return Double.parseDouble(limpia);
        } catch (Exception e) {
            return 0.0;
        }
    }

}
