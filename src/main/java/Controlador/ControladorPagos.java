/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelos.Mensaje;
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

/**
 *
 * @author ALCIDES
 */
public class ControladorPagos {

    public Double listarDeudaCliente(int cliente, JTable tabla) {
        Double DeudaFacturas = null;
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("FACT");
        modelo.addColumn("FECHA");
        modelo.addColumn("VENCIMIENTO");
        modelo.addColumn("IMPORTE");
        modelo.addColumn("ADELANTO");
        modelo.addColumn("PAGOS");
        modelo.addColumn("SALDO");
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        try {
            String sql = "select f.montototal, f.codigo, f.acuenta, "
                    + "strftime('%d-%m-%Y', f.fechaFactura) as fechaFactura, " // Formateo de fecha
                    + "c.codigocliente, c.nombre, c.diaspago, f.posteriores, f.adelanto, "
                    + "strftime('%d-%m-%Y', f.fechaFactura, '+' || c.diaspago || ' days') as vencimiento, " // Suma de días
                    + "f.montototal - f.adelanto - f.posteriores as saldo "
                    + "from factura as f "
                    + "inner join cliente as c on f.cod_cliente = c.codigocliente "
                    + "where f.pagado = 0 " // SQLite usa 0 para false
                    + "and f.cod_cliente = ? and f.estado='ACTIVA'"
                    + "order by vencimiento";

            PreparedStatement ps2 = objetoConexion.estableceConexion().prepareStatement(sql);

            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
            ps2.setInt(1, cliente);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                Integer factura = rs2.getInt("codigo");
                String fecha = rs2.getString("fechaFactura");
                String vencimiento = rs2.getString("vencimiento");
                Double importe = rs2.getDouble("montototal");
                Double adelanto = rs2.getDouble("adelanto");
                Double pagos = rs2.getDouble("posteriores");
                Double saldo = rs2.getDouble("saldo");
                // System.out.println("    saldo= " + saldo);
                if (DeudaFacturas == null) {
                    DeudaFacturas = 0d;
                }
                DeudaFacturas = DeudaFacturas + saldo;
                modelo.addRow(new Object[]{factura, fecha, vencimiento, formatoMoneda.format(importe),
                    formatoMoneda.format(adelanto),
                    formatoMoneda.format(pagos), formatoMoneda.format(saldo)});
            }
            if (tabla != null) {
                tabla.setModel(modelo);

                tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

                tabla.getColumnModel().getColumn(0).setPreferredWidth(55);

                tabla.getColumnModel().getColumn(1).setPreferredWidth(90);
                tabla.getColumnModel().getColumn(2).setPreferredWidth(90);
                tabla.getColumnModel().getColumn(3).setPreferredWidth(133);
                tabla.getColumnModel().getColumn(4).setPreferredWidth(133);
                tabla.getColumnModel().getColumn(5).setPreferredWidth(133);
                tabla.getColumnModel().getColumn(6).setPreferredWidth(133);
                DefaultTableCellRenderer cent = new DefaultTableCellRenderer();

                cent.setHorizontalAlignment(JLabel.CENTER);

                tabla.getColumnModel().getColumn(0).setCellRenderer(cent);

                //  DefaultTableCellRenderer cent1=new  DefaultTableCellRenderer();
                //    cent1.setHorizontalAlignment(SwingConstants.RIGHT);
                //    cent1.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
                tabla.getColumnModel().getColumn(3).setCellRenderer(new paddingRig());
                tabla.getColumnModel().getColumn(4).setCellRenderer(new paddingRig());
                tabla.getColumnModel().getColumn(5).setCellRenderer(new paddingRig());
                tabla.getColumnModel().getColumn(6).setCellRenderer(new paddingRig());
            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "22 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
        return DeudaFacturas;
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

        Modelos.ModeloCliente objetoProducto = new Modelos.ModeloCliente();

        DefaultTableModel modelo = new DefaultTableModel();

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        Integer idSelecte;
        try {
            if (fila >= 0) {
                idSelecte = Integer.parseInt(listaCliente.getValueAt(fila, 1).toString());
                String consulta = "select cliente.nombre,cliente.cuit,cliente.saldo_acumulado"
                        + " from cliente  where cliente.codigocliente =?";
                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
                ps.setInt(1, idSelecte);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    fieldNombre.setText(rs.getString("nombre"));
                    fieldDni.setText(rs.getString("cuit"));
                    deuda.setText(formatoMoneda.format(rs.getDouble("saldo_acumulado")));
                }

                cliente.setText(idSelecte.toString());
                contenedorCliente.setVisible(false);
                abona.requestFocus();

                Double deudaFac = listarDeudaCliente(Integer.parseInt(cliente.getText()), tabla);
                buscador.setText(idSelecte.toString());
                if (deudaFac != null) {
                    deudaFacturas.setText(formatoMoneda.format(deudaFac));
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "23 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
    }

   public Mensaje realizarPago(Double dinero, JTable tabla, JLabel saldoCliente, JLabel cliente, String metodo, JCheckBox chkAnterior, JLabel vuelto) {
    Mensaje mj = new Mensaje();
    double valorDeuda = 0.0;
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    if (dinero == null || dinero <= 0) {
        mj.setCodigo(3);
        mj.setMensaje("El monto a abonar debe ser mayor a cero.");
        return mj;
    }

    // Conversión segura del saldo del cliente
    try {
        String limpia = saldoCliente.getText().replaceAll("[^0-9,]", "").replace(",", ".");
        valorDeuda = (!limpia.isEmpty() && !limpia.equals("----")) ? Double.parseDouble(limpia) : 0.0;
    } catch (NumberFormatException e) {
        valorDeuda = 0.0;
    }

    Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
    Connection con = objetoConexion.estableceConexion();
    
    Double saldoCl = valorDeuda;
    Double dineroTotal = dinero;
    Double dineroSetear = dinero;
    int idClienteInt = Integer.parseInt(cliente.getText());

    try {
        con.setAutoCommit(false);

        if (!chkAnterior.isSelected()) {
            // --- CASO 1: PAGO NORMAL DE FACTURAS EN TABLA ---
            List<Integer> facturasAfectadas = new ArrayList<>();
            int filaActual = 0;

            while (dineroTotal > 0 && filaActual < tabla.getRowCount()) {
                int facturaId = Integer.parseInt(tabla.getValueAt(filaActual, 0).toString());
                double saldoFactura = extraerDoubleDeTabla(tabla.getValueAt(filaActual, 6).toString());
                double pagosYaRealizados = extraerDoubleDeTabla(tabla.getValueAt(filaActual, 5).toString());
                
                double pagoAFactura;
                double nuevoSaldoFactura;
                boolean facturaPagada;

                if (dineroTotal >= saldoFactura) {
                    pagoAFactura = saldoFactura;
                    nuevoSaldoFactura = 0;
                    facturaPagada = true;
                } else {
                    pagoAFactura = dineroTotal;
                    nuevoSaldoFactura = saldoFactura - dineroTotal;
                    facturaPagada = false;
                }

                // Update Factura
                String sqlUpdateFactura = "UPDATE factura SET posteriores=?, acuenta=?, pagado=? WHERE codigo=?";
                try (PreparedStatement psFactura = con.prepareStatement(sqlUpdateFactura)) {
                    psFactura.setDouble(1, pagosYaRealizados + pagoAFactura);
                    psFactura.setDouble(2, nuevoSaldoFactura);
                    psFactura.setBoolean(3, facturaPagada);
                    psFactura.setInt(4, facturaId);
                    psFactura.executeUpdate();
                }

                facturasAfectadas.add(facturaId);
                dineroTotal -= pagoAFactura;
                saldoCl -= pagoAFactura;
                filaActual++; // IMPORTANTE: Siempre incrementar para evitar bucle infinito
            }

            // Registrar Pago
            String sqlRegistrarPago = "INSERT INTO pagos(fecha, importe, idcliente, facturaafectada, tipo, metodo) VALUES(?,?,?,?,?,?)";
            try (PreparedStatement psPago = con.prepareStatement(sqlRegistrarPago)) {
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                psPago.setString(1, fecha);
                psPago.setDouble(2, dineroSetear);
                psPago.setInt(3, idClienteInt);
                psPago.setString(4, facturasAfectadas.toString());
                psPago.setString(5, "PAGA SALDO");
                psPago.setString(6, metodo);
                psPago.executeUpdate();
            }

            mj.setCodigo(1);
            mj.setMensaje("Pago realizado con éxito");

        } else {
            // --- CASO 2: PAGO DE DEUDA EXTERNA (CHECK SELECCIONADO) ---
            // Nota: listarDeudaCliente NO debe abrir otra conexión o causará Deadlock
            Double montoFacturas = listarDeudaCliente(idClienteInt, null); 
            Double diferenciaSaldo = valorDeuda - montoFacturas;

            if (diferenciaSaldo > 0) {
                if (dineroSetear <= diferenciaSaldo) {
                    saldoCl = valorDeuda - dineroSetear;
                } else {
                    saldoCl = valorDeuda - diferenciaSaldo; // Solo consume la deuda externa
                    mj.setCodigo(2);
                    mj.setMensaje("Pago realizado, solo se contabilizaron deudas externas. Revise el vuelto.");
                    double vlto = dineroSetear - diferenciaSaldo;
                    vuelto.setText(String.format("%.2f", vlto));
                }
            } else {
                throw new Exception("No hay deudas externas para saldar.");
            }
        }

        // --- ACTUALIZACIÓN FINAL DEL CLIENTE ---
        String sqlUpdateCliente = "UPDATE cliente SET saldo_acumulado=? WHERE codigocliente=?";
        try (PreparedStatement psCliente = con.prepareStatement(sqlUpdateCliente)) {
            psCliente.setDouble(1, saldoCl);
            psCliente.setInt(2, idClienteInt);
            psCliente.executeUpdate();
        }

        con.commit();
        saldoCliente.setText(formatoMoneda.format(saldoCl));

    } catch (Exception e) {
        try { if (con != null) con.rollback(); } catch (SQLException ex) {}
        mj.setCodigo(4);
        mj.setMensaje("Error: " + e.getMessage());
    } finally {
        objetoConexion.cerrarConexion();
    }

    // Refrescar tabla al final
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
