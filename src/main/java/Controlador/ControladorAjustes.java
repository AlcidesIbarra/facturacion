/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelos.Mensaje;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ALCIDES
 */
public class ControladorAjustes {

    // Método auxiliar para parsear números de forma segura
    private double parseSeguro(String texto) {
        try {
            return Double.parseDouble(texto.replace(",", "."));
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Mensaje pasarPreciosEditar(JTable tbResumenVenta, JTextField buscar,
            JTextField codigoProducto, JTextField descripcion,
            JTextField costo, JTextField venta, JTextField stock, Double stockReal,
            JTextField rubro, JTextField proveedor, boolean sumar, String check, JLabel total, int indice) {
        
        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();

        double ingresoTab = parseSeguro(stock.getText());
        double prCosto = parseSeguro(costo.getText());
        double prVenta = parseSeguro(venta.getText());
        double diferencia = prVenta - prCosto;
        double nuevoStock = sumar ? (stockReal + ingresoTab) : ingresoTab;

        String campo9 = "----";
        if ("factura".equals(check)) {
            BigDecimal subtotal = new BigDecimal(prCosto).multiply(new BigDecimal(ingresoTab));
            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
            campo9 = formatoMoneda.format(subtotal);
        }

        // Actualizar valores en el modelo
        modelo.setValueAt(codigoProducto.getText(), indice, 0);
        modelo.setValueAt(descripcion.getText(), indice, 1);
        modelo.setValueAt(costo.getText(), indice, 2);
        modelo.setValueAt(venta.getText(), indice, 3);
        modelo.setValueAt(diferencia, indice, 4);
        modelo.setValueAt(stock.getText(), indice, 5);

        modelo.setValueAt(rubro.getText(), indice, 7);
        modelo.setValueAt(proveedor.getText(), indice, 6);
        modelo.setValueAt( campo9, indice, 8);

        if ("factura".equals(check)) {
            calcularMontoFactura(tbResumenVenta, total);
        }
        
        limpiarCampos(codigoProducto, descripcion, costo, venta, rubro, proveedor, buscar, stock);
        return null;
    }

   public Mensaje pasarPreciosGuardar(JTable tbResumenVenta, JTextField buscar,
            JTextField codigoProducto, JTextField descripcion,
            JTextField costo, JTextField venta, JTextField stock, Double stockReal,
            JTextField rubro, JTextField proveedor, boolean sumar, String check, JLabel total) {

    DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
    String codProducto = codigoProducto.getText();

    // 1. Validar que el código no esté vacío
    if (codProducto.isEmpty()) return null;

    // 2. Parseo de números con 2 decimales
    double ingresoTab = parseSeguro(stock.getText());
    double prCosto = parseSeguro(costo.getText());
    double prVenta = parseSeguro(venta.getText());
    double diferencia = prVenta - prCosto;

    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    String campo9 = "----";
    if ("factura".equals(check)) {
        BigDecimal subtotal = new BigDecimal(prCosto).multiply(new BigDecimal(ingresoTab));
        campo9 = formatoMoneda.format(subtotal);
    }

    // 3. Agregamos la fila formateando los números a String con 2 decimales
    modelo.addRow(new Object[]{
        codProducto,                               // Col 0: Código (CENTRO)
        descripcion.getText(),       // Col 1: Descripción
        String.format(Locale.US, "%.2f", prCosto), // Col 2: Costo (DERECHA)
        String.format(Locale.US, "%.2f", prVenta), // Col 3: Venta (DERECHA)
        String.format(Locale.US, "%.2f", diferencia),// Col 4: Dif (DERECHA)
        String.format(Locale.US, "%.2f", ingresoTab),// Col 5: KG Stock (CENTRO)
        proveedor.getText(),                       // Col 6: Proveedor
        rubro.getText(),                           // Col 7: Rubro
        campo9                                     // Col 8: Total Factura (DERECHA)
    });

    if ("factura".equals(check)) {
        calcularMontoFactura(tbResumenVenta, total);
    }

    limpiarCampos(codigoProducto, descripcion, costo, venta, rubro, proveedor, buscar, stock);
    configurarAlineacion(tbResumenVenta); // Aplicamos el estilo visual
    
    return null;
}
   public void configurarAlineacion(JTable tabla) {
    DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
    centro.setHorizontalAlignment(SwingConstants.CENTER);

    DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
    derecha.setHorizontalAlignment(SwingConstants.RIGHT);

    // Columna 0 (Código) -> CENTRO
    tabla.getColumnModel().getColumn(0).setCellRenderer(centro);
    
    // Columnas 2, 3, 4 (Precios) -> DERECHA
    tabla.getColumnModel().getColumn(2).setCellRenderer(derecha);
    tabla.getColumnModel().getColumn(3).setCellRenderer(derecha);
    tabla.getColumnModel().getColumn(4).setCellRenderer(derecha);
    
    // Columna 5 (KG Stock) -> CENTRO
    tabla.getColumnModel().getColumn(5).setCellRenderer(centro);
    
    // Columna 8 (Subtotal si existe) -> DERECHA
    tabla.getColumnModel().getColumn(8).setCellRenderer(derecha);
    
    // Forzar a la tabla a repintar los cambios
    tabla.repaint();
}
    public void calcularMontoFactura(JTable tbResumenVenta, JLabel total) {
        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        BigDecimal totalGeneral = BigDecimal.ZERO;
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object valorCelda = modelo.getValueAt(i, 8);
            if (valorCelda != null && !valorCelda.equals("----")) {
                try {
                    // Limpieza segura del formato moneda para volver a número
                    String limpio = valorCelda.toString()
                            .replaceAll("[^0-9,]", "") // Quita todo menos números y coma
                            .replace(",", ".");        // Cambia coma por punto
                    totalGeneral = totalGeneral.add(new BigDecimal(limpio));
                } catch (Exception e) {
                    // Si falla el parseo de una fila, se ignora esa fila
                }
            }
        }
        total.setText(formatoMoneda.format(totalGeneral));
    }

    private void limpiarCampos(JTextField... campos) {
        for (JTextField f : campos) {
            f.setText("");
        }
        // El último campo pasado en la llamada de arriba (en este caso 'stock') 
        // no es el que queremos con foco, sino el de búsqueda
    }
}