/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Formularios.FormVenta;
import Formularios.ModeloFactura;
import Modelos.Mensaje;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.awt.TextField;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author ALCIDES
 */
public class ControladorVenta {

    public void buscarNumeroFactura(JTextField factura, JTextField fechaField) {

        LocalDateTime hoy = LocalDateTime.now();
        ZoneId defalutZone = ZoneId.systemDefault();
        Instant instancia = hoy.atZone(defalutZone).toInstant();

        java.util.Date timeHoy = Date.from(instancia);

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(timeHoy);

        int diaSemana = calendario.get(Calendar.DAY_OF_WEEK);
        String[] diaSe = {"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "sabado"};
        String nombreDia = diaSe[diaSemana - 1];
        SimpleDateFormat formatoFecha = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy");
        String FechaFormateada = formatoFecha.format(timeHoy);
        fechaField.setText(FechaFormateada);

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        String consulta = "select MAX(codigo)as codigo from factura;";

        try {
            PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);

            ResultSet rs = ps.executeQuery();
            int facturaNumero = 0;

            while (rs.next()) {
                facturaNumero = (rs.getInt("codigo"));
                facturaNumero = facturaNumero + 1;
            }
            int longitud = 8;
            String resultado = String.format("%0" + longitud + "d", facturaNumero);
            factura.setText(resultado);

        } catch (Exception e) {
            ////tratar exepcionnnnnnnnnn
            //   JOptionPane.showMessageDialog(null, "no anda we" + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }

    }

 public boolean buscarProducto(JTextField datoBuscar, JTable listaSugerencias, JScrollPane contenedorTabla, String activos) {
    boolean returno = false;
    Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
    Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("descripcion");
    modelo.addColumn("id");
    
    // Obtenemos el texto y limpiamos espacios
    String textoBusqueda = datoBuscar.getText().trim();
    
    // Ajuste de lógica para el campo 'activo' (1 para ACTIVOS, 0 para INACTIVOS)
    int estadoActivo = activos.equalsIgnoreCase("ACTIVOS") ? 1 : 0;

    // SOLUCIÓN: Usamos regex para saber si son solo números (sin importar el largo)
    // Esto evita el error de Integer.parseInt con códigos de más de 9 dígitos
    boolean esNumerico = textoBusqueda.matches("\\d+"); 

    try {
        String consulta = null;
        
        // Consultas actualizadas para tabla 'productos' y sintaxis SQLite
        if (!esNumerico) {
            // Búsqueda por Nombre
            consulta = "SELECT id_producto, codigo_barras, nombre, precio_compra, precio_venta, stock "
                    + "FROM productos WHERE nombre LIKE '%' || ? || '%' AND activo = " + estadoActivo + " LIMIT 10;";
        } else {
            // Búsqueda por Código de Barras (ahora soporta cualquier longitud)
            consulta = "SELECT id_producto, codigo_barras, nombre, precio_compra, precio_venta, stock "
                    + "FROM productos WHERE codigo_barras LIKE '%' || ? || '%' AND activo = " + estadoActivo + " LIMIT 10;";
        }

        if (!textoBusqueda.isEmpty()) {
            PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
            ps.setString(1, textoBusqueda);
            ResultSet rs = ps.executeQuery();

            int contador = 0;
            int tamaño = listaSugerencias.getRowHeight();

            while (rs.next()) {
                returno = true;
                // Mapeo a tu objeto con los nombres de columna de tu nueva tabla
                objetoProducto.setCodigo(rs.getInt("id_producto"));
                objetoProducto.setDescripcion(rs.getString("nombre"));
                objetoProducto.setPrecioCompra(rs.getDouble("precio_compra"));
                objetoProducto.setPrecioVenta(rs.getDouble("precio_venta"));
                objetoProducto.setStock(rs.getDouble("stock"));
                
                // Agregamos a la fila (Descripción visible, ID oculto)
                modelo.addRow(new Object[]{objetoProducto.getDescripcion(), objetoProducto.getCodigo()});
                contador = contador + tamaño;
            }

            // --- CONFIGURACIÓN VISUAL (Sin modificar dimensiones) ---
            contenedorTabla.setVerticalScrollBarPolicy(contenedorTabla.VERTICAL_SCROLLBAR_NEVER);
            contenedorTabla.setVisible(true);
            listaSugerencias.setModel(modelo);
            contenedorTabla.setSize(170, contador + 6);
            
            listaSugerencias.getTableHeader().setPreferredSize(new Dimension(180, 0));
            listaSugerencias.setRowMargin(0);

            // Hacer la tabla no editable
            for (int i = 0; i < listaSugerencias.getColumnCount(); i++) {
                Class<?> colClas = listaSugerencias.getColumnClass(i);
                listaSugerencias.setDefaultEditor(colClas, null);
            }

            // Ocultar columna de ID (para que el usuario solo vea el nombre)
            listaSugerencias.getColumn("id").setMinWidth(0);
            listaSugerencias.getColumn("id").setMaxWidth(0);
            listaSugerencias.getColumn("id").setWidth(0);
        } else {
            contenedorTabla.setVisible(false);
        }
    } catch (Exception e) {
        // Usamos tu Alerta para errores graves
        new Alerta("Error en búsqueda: " + e.getMessage(), new Color(231, 76, 60), null);
    } finally {
        objetoConexion.cerrarConexion();
    }
    return returno;
}

    public Mensaje SeleccionarProductosVentaFocusLost(JTable listaProducto, JTextField fieldCantidad,
            JTextField codigoProducto, JTextField txtBuscarProducto,
            JTextField prCosto, JTextField prVenta, JTextField codCliente,
            JScrollPane contenedorTabla, JTextField texfieldStock,
            JLabel descripcionProd, int verificador, JLabel kgBulto) {

        DefaultTableModel modelo = new DefaultTableModel();
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        int fila = 0;
        Integer idSelecte = 0;

        try {

            idSelecte = Integer.parseInt(txtBuscarProducto.getText());
            fila = 1;

        } catch (Exception e) {
            codigoProducto.setText("");
            prCosto.setText("");
            prVenta.setText("");
            texfieldStock.setText("");
            txtBuscarProducto.setText("");
            fieldCantidad.setText("");
            descripcionProd.setText("");

        }

        try {
            if (fila >= 0) {

                //  idSelecte = Integer.parseInt(listaProducto.getValueAt(fila, 1).toString());
                String consulta = "select * from preciocliente inner join  producto "
                        + " where preciocliente.fkcliente = ? and producto.codigoproducto=? and preciocliente.fkProducto=? and  producto.activo!=FALSE";

                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);

                if (codCliente == null) {
                    ps.setString(1, "");
                } else {
                    ps.setString(1, codCliente.getText());
                }

                ps.setString(2, idSelecte.toString());
                ps.setString(3, idSelecte.toString());

                ResultSet rs = ps.executeQuery();
                Double diferido = null;
                Double prCostoVar = null;
                String descripcion = null;
                Double Stock = null;
                Double kgporBulto = null;
                if (rs.next()) {

                    codigoProducto.setText(idSelecte.toString());
                    diferido = rs.getDouble("diferido");
                    prCostoVar = rs.getDouble("precio_compra");
                    descripcion = rs.getString("descripcion");
                    Stock = rs.getDouble("stock");
                    kgporBulto = rs.getDouble("kgbulto");
                    Double precioverificar = rs.getDouble("costoanterior");
                    //verifico que no hayan subas
//   limiteCompra.setText(formatoMoneda.format(rs.getDouble("limite_compra")));
                    kgBulto.setText(kgporBulto.toString());
                    prCosto.setText(formatoMoneda.format(prCostoVar));
                    prVenta.setText(formatoMoneda.format(diferido));
                    texfieldStock.setText(Stock.toString());
                    descripcionProd.setText(descripcion);
                    txtBuscarProducto.setText(descripcion);
                    contenedorTabla.setVisible(false);

                    diferido = null;
                    prCosto = null;
                    //   descripcion = null;
                    if (Integer.parseInt(codCliente.getText()) != 1) {
                        if (!precioverificar.equals(prCostoVar) && verificador != 2) {
                            System.out.println("    entro al verificador de precios");
                            Mensaje mj = new Mensaje();
                            mj.setCodigo(3);
                            mj.setMensaje("Hubo una modificacion en: " + descripcion + " verifica tus costos, P. anterior: "
                                    + formatoMoneda.format(precioverificar) + " P. Nuevo: " + formatoMoneda.format(prCostoVar));
                            prVenta.setEditable(true);
                            ControladorAdministracion ca = new ControladorAdministracion();
                            prVenta.setBackground(ca.retornatexField());

                            return mj;
                            ///  JOptionPane.showMessageDialog(null, );
                        }
                    }
                } else {

                    consulta = "select * from producto "
                            + " where codigoproducto=? and  producto.activo!=FALSE";
                    ps = objetoConexion.estableceConexion().prepareStatement(consulta);

                    ps.setInt(1, idSelecte);
                    rs = ps.executeQuery();

                    if (!rs.isBeforeFirst()) {

                        codigoProducto.setText("");
                        prCosto.setText("");
                        prVenta.setText("");
                        texfieldStock.setText("");
                        txtBuscarProducto.setText("");
                        fieldCantidad.setText("");
                        descripcionProd.setText("");
                        kgBulto.setText("");

                    } else {
                        while (rs.next()) {
                            codigoProducto.setText(idSelecte.toString());
                            diferido = rs.getDouble("precio_venta");
                            prCostoVar = rs.getDouble("precio_compra");
                            descripcion = rs.getString("descripcion");
                            Stock = rs.getDouble("stock");
                            kgporBulto = rs.getDouble("kgbulto");
                        }
                        kgBulto.setText(kgporBulto.toString());
                        prCosto.setText(formatoMoneda.format(prCostoVar));
                        prVenta.setText(formatoMoneda.format(diferido));
                        texfieldStock.setText(Stock.toString());
                        descripcionProd.setText(descripcion);
                        txtBuscarProducto.setText(descripcion);
                        contenedorTabla.setVisible(false);

                        diferido = null;
                        prCosto = null;
                        descripcion = null;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "26 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
        return null;
    }

    public Mensaje SeleccionarProductosVenta(JTable listaProducto, JTextField fieldCantidad,
            JTextField codigoProducto, JTextField txtBuscarProducto,
            JTextField prCosto, JTextField prVenta, JTextField codCliente,
            JScrollPane contenedorTabla, JTextField texfieldStock,
            JLabel descripcionProd, int verificador, JLabel kgbulto,
            JTable tablaPesos, JScrollPane scrollPesos) {

        int fila = listaProducto.getSelectedRow();
        Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();
        DefaultTableModel modelo = new DefaultTableModel();
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        Integer idSelecte = 0;
        if (verificador != 4) {

            if (verificador == 1) {
                if (!txtBuscarProducto.getText().equals("")) {

                    try {

                        idSelecte = Integer.parseInt(txtBuscarProducto.getText());
                        fila = 1;
                    } catch (Exception e) {
                        codigoProducto.setText("");
                        prCosto.setText("");
                        prVenta.setText("");
                        texfieldStock.setText("");
                        txtBuscarProducto.setText("");
                        fieldCantidad.setText("");
                        descripcionProd.setText("");
                        kgbulto.setText("");
                    }

                } else {
                    fila = 0;
                }
            } else {
                if (verificador == 2) {
                    idSelecte = Integer.parseInt(codigoProducto.getText());
                } else {
                    idSelecte = Integer.parseInt(listaProducto.getValueAt(fila, 1).toString());
                }
            }
        } else {

            idSelecte = Integer.parseInt(codigoProducto.getText());
            fila = 1;
        }
        try {
            if (fila >= 0) {

                //  idSelecte = Integer.parseInt(listaProducto.getValueAt(fila, 1).toString());
                String consulta = "select * from preciocliente inner join  producto "
                        + " where preciocliente.fkcliente = ? and producto.codigoproducto=? and preciocliente.fkProducto=? and  producto.activo!=FALSE";

                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);

                if (codCliente == null) {
                    ps.setString(1, "");
                } else {
                    ps.setString(1, codCliente.getText());
                }

                ps.setString(2, idSelecte.toString());
                ps.setString(3, idSelecte.toString());

                ResultSet rs = ps.executeQuery();
                Double diferido = null;
                Double prCostoVar = null;
                String descripcion = null;
                Double Stock = null;
                Double kgporBulto = null;
                if (rs.next()) {

                    codigoProducto.setText(idSelecte.toString());
                    diferido = rs.getDouble("diferido");
                    prCostoVar = rs.getDouble("precio_compra");
                    descripcion = rs.getString("descripcion");
                    Stock = rs.getDouble("stock");
                    Double precioverificar = rs.getDouble("costoanterior");
                    kgporBulto = rs.getDouble("kgbulto");
                    //verifico que no hayan subas
//   limiteCompra.setText(formatoMoneda.format(rs.getDouble("limite_compra")));
                    prCosto.setText(formatoMoneda.format(prCostoVar));
                    prVenta.setText(formatoMoneda.format(diferido));
                    texfieldStock.setText(Stock.toString());
                    descripcionProd.setText(descripcion);
                    txtBuscarProducto.setText(descripcion);
                    contenedorTabla.setVisible(false);
                    /*
                    if (verificador != 1) {

                        fieldCantidad.requestFocus();
                    }
                     */
                    diferido = null;
                    prCosto = null;
                    //   descripcion = null;
                    if (Integer.parseInt(codCliente.getText()) != 1) {
                        if (!precioverificar.equals(prCostoVar) && verificador != 2) {
                            System.out.println("    entro al verificador de precios");
                            Mensaje mj = new Mensaje();
                            mj.setCodigo(3);
                            mj.setMensaje("Hubo una modificacion en: " + descripcion + " verifica tus costos, P. anterior: "
                                    + formatoMoneda.format(precioverificar) + " P. Nuevo: " + formatoMoneda.format(prCostoVar));
                            prVenta.setEditable(true);
                            ControladorAdministracion ca = new ControladorAdministracion();
                            prVenta.setBackground(ca.retornatexField());
                            //    prVenta.requestFocus();
                            return mj;
                            ///  JOptionPane.showMessageDialog(null, );
                        }
                    }

                } else {

                    consulta = "select * from producto "
                            + " where codigoproducto=? and  producto.activo!=FALSE";
                    ps = objetoConexion.estableceConexion().prepareStatement(consulta);

                    ps.setInt(1, idSelecte);
                    rs = ps.executeQuery();

                    if (!rs.isBeforeFirst()) {

                        codigoProducto.setText("");
                        prCosto.setText("");
                        prVenta.setText("");
                        texfieldStock.setText("");
                        txtBuscarProducto.setText("");
                        fieldCantidad.setText("");
                        descripcionProd.setText("");
                        kgbulto.setText("");
                    } else {
                        while (rs.next()) {
                            codigoProducto.setText(idSelecte.toString());
                            diferido = rs.getDouble("precio_venta");
                            prCostoVar = rs.getDouble("precio_compra");
                            descripcion = rs.getString("descripcion");
                            Stock = rs.getDouble("stock");
                            kgporBulto = rs.getDouble("kgbulto");
                        }
                        kgbulto.setText(kgporBulto.toString());
                        prCosto.setText(formatoMoneda.format(prCostoVar));
                        prVenta.setText(formatoMoneda.format(diferido));
                        texfieldStock.setText(Stock.toString());
                        descripcionProd.setText(descripcion);
                        txtBuscarProducto.setText(descripcion);
                        contenedorTabla.setVisible(false);

                        if (verificador != 1) {
                            //   fieldCantidad.requestFocus();
                        }

                        diferido = null;
                        prCosto = null;
                        descripcion = null;
                    }
                }
                contenedorTabla.setVisible(false);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "27 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
        return null;
    }

    public boolean buscarDetallesPesos(JTextField codigoProducto, JTable listaSugerencias, JScrollPane contenedorTabla) {
        boolean returno = false;
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();

        // Modelo no editable
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        modelo.addColumn("descripcion");
        modelo.addColumn("id");

        try {
            String consulta = "SELECT id, kg_individual FROM stock_detalle WHERE producto_id = ? AND estado = 'DISPONIBLE'";

            PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
            ps.setString(1, codigoProducto.getText());
            ResultSet rs = ps.executeQuery();

            // --- CONFIGURACIÓN DE MEDIDAS ---
            int contador = 0;
            int tamaño = listaSugerencias.getRowHeight();
            if (tamaño <= 0) {
                tamaño = 18; // Valor por defecto si la tabla no ha renderizado aún
            }
            DecimalFormat df = new DecimalFormat("#,##0.00");

            while (rs.next()) {
                returno = true;

                // Convertimos el String de la DB a Double para poder formatearlo
                double pesoCargado = rs.getDouble("kg_individual");
                String pesoFormateado = df.format(pesoCargado) + " Kg";

                modelo.addRow(new Object[]{
                    pesoFormateado,
                    rs.getString("id")
                });
                contador = contador + tamaño;
            }
            if (returno) {
                listaSugerencias.setModel(modelo);

                // Configuración de Scroll y Tabla según tus requerimientos
                contenedorTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                listaSugerencias.getTableHeader().setPreferredSize(new Dimension(180, 0));
                listaSugerencias.setRowMargin(0);

                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                listaSugerencias.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

                // No modificar editores
                for (int i = 0; i < listaSugerencias.getColumnCount(); i++) {
                    Class<?> colClas = listaSugerencias.getColumnClass(i);
                    listaSugerencias.setDefaultEditor(colClas, null);
                }

                // Ocultar columna ID
                listaSugerencias.getColumn("id").setMinWidth(0);
                listaSugerencias.getColumn("id").setMaxWidth(0);
                listaSugerencias.getColumn("id").setWidth(0);

                // --- APLICACIÓN DE DIMENSIONES DINÁMICAS (Solución Absolute Layout) ---
                final int altoFinal = contador + 6;

                SwingUtilities.invokeLater(() -> {
                    // Usamos setBounds para fijar posición actual y el nuevo tamaño calculado
                    contenedorTabla.setBounds(contenedorTabla.getX(), contenedorTabla.getY(), 170, altoFinal);
                    contenedorTabla.setVisible(true);

                    // Traer al frente para evitar que el Absolute Layout lo tape
                    if (contenedorTabla.getParent() != null) {
                        contenedorTabla.getParent().setComponentZOrder(contenedorTabla, 0);
                        contenedorTabla.getParent().repaint();
                    }
                });

                listaSugerencias.setFocusable(true);
                listaSugerencias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                listaSugerencias.setRowSelectionAllowed(true);
            } else {
                contenedorTabla.setVisible(false);
            }

        } catch (Exception e) {
            System.err.println("Error en detalles pesos: " + e.getMessage());
        } finally {
            objetoConexion.cerrarConexion();
        }
        return returno;
    }

    public void liberarPesosReservados() {
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        // Volvemos a DISPONIBLE todo lo que quedó a mitad de camino
        String consulta = "UPDATE stock_detalle SET estado = 'DISPONIBLE' WHERE estado = 'RESERVADO'";

        try (Connection con = objetoConexion.estableceConexion(); PreparedStatement ps = con.prepareStatement(consulta)) {

            ps.executeUpdate();
            System.out.println("Pesos reservados liberados correctamente.");

        } catch (Exception e) {
            System.err.println("Error al liberar pesos: " + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
    }

    public void actualizarEstadoPeso(String idDetalle, String nuevoEstado, Connection conRecibida) {
        String consulta = "UPDATE stock_detalle SET estado = ? WHERE id = ?";
        Connection conUso = null;
        PreparedStatement ps = null;
        boolean esConexionExterna = (conRecibida != null);

        try {
            // Si recibo una conexión la uso, sino creo una nueva
            conUso = esConexionExterna ? conRecibida : new Configuracion.CConexion().estableceConexion();

            ps = conUso.prepareStatement(consulta);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, Integer.parseInt(idDetalle));
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado peso: " + e.getMessage());
        } finally {
            // IMPORTANTE: Solo cerramos el PreparedStatement.
            // La conexión SOLO se cierra si la creamos nosotros aquí dentro.
            try {
                if (ps != null) {
                    ps.close();
                }
                if (!esConexionExterna && conUso != null) {
                    conUso.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error al cerrar recursos: " + ex.getMessage());
            }
        }
    }

    public double versaldosCliente(JTextField codCliente) {
        Double saldo = 0d;
        if (!codCliente.getText().equals("1")) {
            if (codCliente.getText().equals("")) {
                return saldo;
            } else {
                int cliente = Integer.parseInt(codCliente.getText());

                LocalDateTime hoy = LocalDateTime.now();
                ZoneId defalutZone = ZoneId.systemDefault();
                Instant instancia = hoy.atZone(defalutZone).toInstant();

                java.util.Date timeHoy = Date.from(instancia);

                Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
                Integer dias = 0;

                String consultaHabilitado = "select f.montototal ,f.codigo, f.acuenta, f.fechaFactura,"
                        + " c.codigocliente,c.nombre,c.diaspago, f.posteriores,"
                        + " montototal-adelanto-posteriores as saldo from factura"
                        + " as f inner join cliente as c on f.cod_cliente=c.codigocliente "
                        + "where f.pagado=false and f.estado='ACTIVA' and f.cod_cliente=? and"
                        + "  DATE(f.fechaFactura,'+' || c.diaspago || 'days') < date('NOW');";

                try {

                    PreparedStatement ps2 = objetoConexion.estableceConexion().prepareStatement(consultaHabilitado);

                    ps2.setInt(1, cliente);
                    ResultSet rs1 = ps2.executeQuery();

                    while (rs1.next()) {

                        saldo = saldo + rs1.getDouble("saldo");
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "28 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
                } finally {
                    objetoConexion.cerrarConexion();
                }

            }
        }
        return saldo;
    }

    public Mensaje pasarPreciosEditar(JTable tbResumenVenta, JTextField codigoProducto, JTextField descripcion,
            JTextField costo, JTextField venta, JTextField cantidad, JTextField fieldBuscador,
            JTextField Stock, JCheckBox negativo, JCheckBox saldo,
            JLabel deuda, JTextField limite, JLabel total,
            JTextField codCliente, JLabel labelDescripcion, JCheckBox cuentaVencida, JLabel kgBulto) {
        Double vencido = versaldosCliente(codCliente);
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        //  versaldosCliente(Integer.parseInt(codCliente.getText()));
        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        String codProducto = codigoProducto.getText();

        Double cantidadnm = Double.parseDouble(cantidad.getText());
        Double stocknm = Double.parseDouble(Stock.getText());
        System.out.println(stocknm + "stock comparar");
        BigDecimal versifactura = new BigDecimal("0.0");
        BigDecimal totalFacturado;
        BigDecimal cantidadFacturar;
        BigDecimal prVentaver;
        BigDecimal saldoDisp;
        BigDecimal limiteDisp;
        boolean verificadorSsaldo = false;

        if (deuda.getText().equalsIgnoreCase("----")) {
            verificadorSsaldo = true;
        } else {

            //  try {
            try {

                totalFacturado = new BigDecimal(total.getText().substring(2).replace(".", "").replace(",", "."));
            } catch (Exception e) {
                totalFacturado = new BigDecimal("0.0");

            }

            cantidadFacturar = new BigDecimal(cantidad.getText());
            prVentaver = new BigDecimal(venta.getText().substring(2).replace(".", "").replace(",", "."));
            saldoDisp = new BigDecimal(deuda.getText().substring(2).replace(".", "").replace(",", "."));
            limiteDisp = new BigDecimal(limite.getText().substring(2).replace(".", "").replace(",", "."));

            versifactura = totalFacturado.add((cantidadFacturar.multiply(prVentaver)).add(saldoDisp));
            BigDecimal producto = cantidadFacturar.multiply(prVentaver);

            // versifactura = totalFacturado + (cantidadFacturar * prVentaver) + saldoDisp;    
            int numero = 0;
            numero = versifactura.compareTo(limiteDisp);

            if (numero < 0) {
                verificadorSsaldo = true;
            } else {
                verificadorSsaldo = false;
            }

            //  } catch (Exception e) {
            //    verificadorSsaldo = false;
            //    }
        }

        //||
        if (cuentaVencida.isSelected() || vencido <= 0) {

            if (verificadorSsaldo == true || saldo.isSelected()) {

                if (cantidadnm <= stocknm || negativo.isSelected()) {

                    String descripcionProd = labelDescripcion.getText();
                    //  Double prCosto = Double.parseDouble(costo.getText());
                    BigDecimal prCosto = new BigDecimal(costo.getText().substring(2).replace(".", "").replace(",", "."));
                    BigDecimal prVenta = new BigDecimal(venta.getText().substring(2).replace(".", "").replace(",", "."));

                    /// Double prVenta = Double.parseDouble(venta.getText());
                    //  Double diferencia = prVenta - prCosto;
                    BigDecimal diferencia = prVenta.subtract(prVenta);

                    BigDecimal cantidadComprada = new BigDecimal(cantidad.getText());

                    BigDecimal subtotal = cantidadComprada.multiply(prVenta);

                    limpiarCamposFacturacion(fieldBuscador, codigoProducto,
                            costo, venta, cantidad, Stock, labelDescripcion, kgBulto);
                    /*
                    codigoProducto.setText("");
                    descripcion.setText("");
                    costo.setText("");
                    venta.setText("");
                    cantidad.setText("");
                    Stock.setText("");
                    labelDescripcion.setText("");
                     */
                    modelo.addRow(new Object[]{codProducto, descripcionProd,
                        formatoMoneda.format(prCosto), cantidadComprada,
                        formatoMoneda.format(prVenta), formatoMoneda.format(subtotal)});
                    fieldBuscador.requestFocus();
                    //la hace no editable
                    /*
                    for (int i = 0; i < tbResumenVenta.getColumnCount(); i++) {
                        Class<?> colClas = tbResumenVenta.getColumnClass(i);
                        tbResumenVenta.setDefaultEditor(colClas, null);
                    }
                     */
                    int fila = tbResumenVenta.getRowCount();
                    int columnas = tbResumenVenta.getColumnCount();
                    Object[][] matriz = new Object[fila][columnas];
                    for (int i = 0; i < fila; i++) {
                        for (int j = 0; j < columnas; j++) {
                            matriz[i][j] = tbResumenVenta.getValueAt(i, j);
                        }
                    }
                    String[] nColumnas = new String[columnas];
                    for (int j = 0; j < columnas; j++) {
                        nColumnas[j] = tbResumenVenta.getColumnName(j);
                    }

                    DefaultTableModel mimodelo = new DefaultTableModel(matriz, nColumnas) {
                        @Override
                        public boolean isCellEditable(int row, int col) {

                            return false;
                        }
                    };

                    tbResumenVenta.setModel(mimodelo);

                } else {
                    Mensaje mj = new Mensaje();
                    mj.setCodigo(3);
                    mj.setMensaje("No tienes stock sificiente de " + descripcion.getText() + " para facturar");
                    //   JOptionPane.showMessageDialog(null, "No tienes stock sificiente de " + descripcion.getText() + " para facturar");

                    ControladorAdministracion ca = new ControladorAdministracion();
                    Stock.setEditable(true);
                    Color color = ca.retornatexField();
                    System.out.println(color.getRed() + "color");
                    Stock.setBackground(ca.retornatexField());

                    return mj;
                }

            } else {
                //  JOptionPane.showMessageDialog(null, "El cliente superó el monto disponible para facturar");
                Mensaje mj = new Mensaje();
                mj.setCodigo(3);
                mj.setMensaje("El cliente superó el monto disponible para facturar");
                limite.setEditable(true);
                return mj;
            }

        } else {
            if (vencido > 0) {
                //  JOptionPane.showMessageDialog(null, "el cliente registra un saldo vencido de: " + vencido);
                Mensaje mj = new Mensaje();
                mj.setCodigo(3);
                mj.setMensaje("el cliente registra un saldo vencido de: " + vencido);

                return mj;
            }
        }
        return null;
    }

    public Mensaje pasarPreciosGuardar(JTable tbResumenVenta, JTextField codigoProducto, JTextField descripcion,
            JTextField costo, JTextField venta, JTextField cantidad, JTextField fieldBuscador,
            JTextField Stock, JCheckBox negativo, JCheckBox saldo,
            JLabel deuda, JTextField limite, JLabel total,
            JTextField codCliente, JLabel labelDescripcion,
            JCheckBox cuentaVencida, int indice, String metodoPago, JLabel kgBulto, String idDetallePeso) {
        Double vencido = versaldosCliente(codCliente);
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        // DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        String codProducto = codigoProducto.getText();
        System.out.println(cantidad.getText() + "cantidaddddddddddddddddddd");
        String cantLimpiar = cantidad.getText().replace(".", "").replace(",", ".");
        Double cantidadnm = Double.parseDouble(cantidad.getText());

        String stockLimpiar = Stock.getText().replace(".", "").replace(",", ".");
        Double stocknm = Double.parseDouble(Stock.getText());
        
        System.out.println("stock a comparar: "+stocknm);
        //   Double stocknm = Double.parseDouble(Stock.getText());

        BigDecimal versifactura = new BigDecimal("0.0");
        BigDecimal totalFacturado;
        BigDecimal cantidadFacturar;
        BigDecimal prVentaver;
        BigDecimal saldoDisp;
        BigDecimal limiteDisp;
        boolean verificadorSsaldo = false;
        if (deuda.getText().equalsIgnoreCase("----")) {
            verificadorSsaldo = true;
        } else {
            try {
                totalFacturado = new BigDecimal(total.getText().substring(2).replace(".", "").replace(",", "."));
            } catch (Exception e) {
                totalFacturado = new BigDecimal("0.0");
            }
            cantidadFacturar = new BigDecimal(cantidad.getText());
            prVentaver = new BigDecimal(venta.getText().substring(2).replace(".", "").replace(",", "."));
            saldoDisp = new BigDecimal(deuda.getText().substring(2).replace(".", "").replace(",", "."));
            limiteDisp = new BigDecimal(limite.getText().substring(2).replace(".", "").replace(",", "."));

            versifactura = totalFacturado.add((cantidadFacturar.multiply(prVentaver)).add(saldoDisp));
            BigDecimal producto = cantidadFacturar.multiply(prVentaver);
            int numero = 0;
            numero = versifactura.compareTo(limiteDisp);

            if (numero < 0) {
                verificadorSsaldo = true;
            } else {
                verificadorSsaldo = false;
            }
        }
        if (cuentaVencida.isSelected() || vencido <= 0) {
            if ((verificadorSsaldo == true || !metodoPago.equalsIgnoreCase("CUENTA CORRIENTE")) || saldo.isSelected()) {
           
                if (cantidadnm <= stocknm || negativo.isSelected()) {

                    String descripcionProd = labelDescripcion.getText();
                    BigDecimal prCosto = new BigDecimal(costo.getText().substring(2).replace(".", "").replace(",", "."));
                    BigDecimal prVenta = new BigDecimal(venta.getText().substring(2).replace(".", "").replace(",", "."));
                    BigDecimal diferencia = prVenta.subtract(prVenta);
                    BigDecimal cantidadComprada = new BigDecimal(cantidad.getText());
                    BigDecimal subtotal = cantidadComprada.multiply(prVenta);
                    BigDecimal cantidadBulto = new BigDecimal("0.0");
                    try {
                        System.out.println(kgBulto.toString());
                        cantidadBulto = cantidadComprada.divide(new BigDecimal(kgBulto.getText()), 0, RoundingMode.HALF_UP);
                        cantidadBulto = cantidadBulto.max(BigDecimal.ONE);
                    } catch (Exception e) {
                        cantidadBulto = new BigDecimal("1");
                    }
                    limpiarCamposFacturacion(fieldBuscador,
                            codigoProducto, costo, venta, cantidad, Stock, labelDescripcion, kgBulto);
                    DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();

// 2. FORZAMOS QUE EL MODELO TENGA 8 COLUMNAS (0 a 7) SI NO LAS TIENE
                    if (modelo.getColumnCount() < 8) {
                        modelo.setColumnCount(8);
                    }

                    if (indice != -1) {
                        try {
                            modelo.setValueAt(codProducto, indice, 0);
                            modelo.setValueAt(descripcionProd, indice, 1);
                            modelo.setValueAt(formatoMoneda.format(prCosto), indice, 2);
                            modelo.setValueAt(cantidadBulto, indice, 3);
                            modelo.setValueAt(cantidadComprada, indice, 4);
                            modelo.setValueAt(formatoMoneda.format(prVenta), indice, 5);
                            modelo.setValueAt(formatoMoneda.format(subtotal), indice, 6);
                            // GUARDAMOS EL ID EN LA COLUMNA 7
                            modelo.setValueAt(idDetallePeso, indice, 7);
                        } catch (Exception e) {
                            modelo.addRow(new Object[]{codProducto, descripcionProd,
                                formatoMoneda.format(prCosto), cantidadBulto, cantidadComprada,
                                formatoMoneda.format(prVenta), formatoMoneda.format(subtotal),
                                idDetallePeso});
                        }
                    } else {
                        modelo.addRow(new Object[]{codProducto, descripcionProd,
                            formatoMoneda.format(prCosto), cantidadBulto, cantidadComprada,
                            formatoMoneda.format(prVenta), formatoMoneda.format(subtotal),
                            idDetallePeso});
                    }

// --- RECONSTRUCCIÓN DEL MODELO ---
                    int fila = tbResumenVenta.getRowCount();
                    int columnas = 8; // FORZAMOS 8 PARA QUE EL ID NO SE PIERDA
                    Object[][] matriz = new Object[fila][columnas];

                    for (int i = 0; i < fila; i++) {
                        for (int j = 0; j < columnas; j++) {
                            matriz[i][j] = tbResumenVenta.getValueAt(i, j);
                        }
                    }

                    String[] nColumnas = {"Cód", "Descripción", "Costo", "Bultos", "Cant", "P. Venta", "Subtotal", "ID_DETALLE"};

                    DefaultTableModel mimodelo = new DefaultTableModel(matriz, nColumnas) {
                        @Override
                        public boolean isCellEditable(int row, int col) {
                            return false;
                        }
                    };

                    tbResumenVenta.setModel(mimodelo);

// --- CONFIGURACIÓN VISUAL ---
                    tbResumenVenta.getColumnModel().getColumn(0).setPreferredWidth(50);
                    tbResumenVenta.getColumnModel().getColumn(1).setPreferredWidth(230);
                    tbResumenVenta.getColumnModel().getColumn(2).setPreferredWidth(132);
                    tbResumenVenta.getColumnModel().getColumn(3).setPreferredWidth(80);
                    tbResumenVenta.getColumnModel().getColumn(4).setPreferredWidth(80);
                    tbResumenVenta.getColumnModel().getColumn(5).setPreferredWidth(132);
                    tbResumenVenta.getColumnModel().getColumn(6).setPreferredWidth(133);

// OCULTAR LA COLUMNA 7 (Donde está el ID)
                    tbResumenVenta.getColumnModel().getColumn(7).setMinWidth(0);
                    tbResumenVenta.getColumnModel().getColumn(7).setMaxWidth(0);
                    tbResumenVenta.getColumnModel().getColumn(7).setPreferredWidth(0);

                    tbResumenVenta.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

// 6. Aplicamos los Renderers (Tus alineaciones)
                    DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
                    cent.setHorizontalAlignment(JLabel.CENTER);
                    tbResumenVenta.getColumnModel().getColumn(4).setCellRenderer(cent);
                    tbResumenVenta.getColumnModel().getColumn(3).setCellRenderer(cent);
                    tbResumenVenta.getColumnModel().getColumn(0).setCellRenderer(cent);

                    tbResumenVenta.getColumnModel().getColumn(6).setCellRenderer(new paddingRig());
                    tbResumenVenta.getColumnModel().getColumn(2).setCellRenderer(new paddingRig());
                    tbResumenVenta.getColumnModel().getColumn(5).setCellRenderer(new paddingRig());
                    tbResumenVenta.getColumnModel().getColumn(1).setCellRenderer(new paddingleft());
                } else {
                    Mensaje mj = new Mensaje();
                    mj.setCodigo(3);
                    mj.setMensaje("No tienes stock sificiente de " + descripcion.getText() + " para facturar");
                    //   JOptionPane.showMessageDialog(null, "No tienes stock sificiente de " + descripcion.getText() + " para facturar");
                    Stock.setEditable(true);
                    ControladorAdministracion ca = new ControladorAdministracion();
                    Stock.setEditable(true);
                    Color color = ca.retornatexField();
                    System.out.println(color.getRed() + "color");
                    Stock.setBackground(ca.retornatexField());
                    return mj;
                }
            } else {
                Mensaje mj = new Mensaje();
                mj.setCodigo(3);
                mj.setMensaje("El cliente superó el monto disponible para facturar");
                limite.setEditable(true);
                return mj;
            }
        } else {
            if (vencido > 0) {
                Mensaje mj = new Mensaje();
                mj.setCodigo(3);
                mj.setMensaje("el cliente registra un saldo vencido de: " + vencido);

                return mj;
            }
        }
        return null;
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

    public void devolverPesosCancelados(JTable tbResumenVenta) {
        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            // Leemos la columna oculta 7
            Object valorId = modelo.getValueAt(i, 7);
            String idDetalle = (valorId != null) ? valorId.toString() : "0";

            // Si tiene un ID de peso real, lo devolvemos a DISPONIBLE
            if (!idDetalle.equals("0") && !idDetalle.isEmpty()) {
                actualizarEstadoPeso(idDetalle, "DISPONIBLE", null);
            }
        }
    }

    public void finalizarPesosFacturados(JTable tbResumenVenta) {
        if (tbResumenVenta == null) {
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            try {
                // Leemos la columna oculta 7 (ID del peso)
                Object valorId = modelo.getValueAt(i, 7);
                String idDetalle = (valorId != null) ? valorId.toString() : "0";

                if (!idDetalle.equals("0") && !idDetalle.isEmpty()) {
                    // Cambiamos el estado para que la "limpieza" al cerrar no los libere
                    actualizarEstadoPeso(idDetalle, "VENDIDO", null);
                }
            } catch (Exception e) {
                // Si la tabla no tiene columna 7 (venta sin pesos), el bucle sigue
            }
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

    public Mensaje eliminarProductoSeleccionado(JTable tbResumenVenta) {
        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        int filaSelleccionada = tbResumenVenta.getSelectedRow();

        if (filaSelleccionada != -1) {
            try {
                // USAR EL MODELO para leer la columna 7 (la oculta)
                Object valorId = modelo.getValueAt(filaSelleccionada, 7);
                String idDetallePeso = (valorId != null) ? valorId.toString() : "0";

                // Si tiene un ID válido, lo devolvemos a DISPONIBLE
                if (!idDetallePeso.equals("0") && !idDetallePeso.isEmpty()) {
                    actualizarEstadoPeso(idDetallePeso, "DISPONIBLE", null);
                    System.out.println("Éxito: Peso " + idDetallePeso + " liberado.");
                }

                // RECIÉN DESPUÉS de leer el ID, borramos la fila
                modelo.removeRow(filaSelleccionada);

            } catch (Exception e) {
                System.err.println("Error al recuperar ID de la factura: " + e.getMessage());
            }
            return null;
        } else {
            Mensaje mj = new Mensaje();
            mj.setCodigo(2);
            mj.setMensaje("Seleccione una fila para eliminar");
            return mj;
        }
    }

    public Mensaje editarProductoSeleccionado(JTable tbResumenVenta, JTextField fieldCantidad,
            JTextField codigoProducto, JTextField txtBuscarProducto,
            JTextField prCosto, JTextField prVenta, JTextField codCliente,
            JScrollPane contenedorTabla,
            JTextField texfieldStock, JLabel descripcionProd, JLabel kgBulto,
            JTable tablaPesos, JScrollPane scrollPesos) {
        Mensaje mj = new Mensaje();
        int indiceSelleccionado = tbResumenVenta.getSelectedRow();

        if (indiceSelleccionado != -1) {
            Integer idSelecte = Integer.parseInt(tbResumenVenta.getValueAt(indiceSelleccionado, 0).toString());

            codigoProducto.setText(idSelecte.toString());
            fieldCantidad.setText(tbResumenVenta.getValueAt(indiceSelleccionado, 3).toString());

            SeleccionarProductosVenta(tbResumenVenta, fieldCantidad, codigoProducto,
                    txtBuscarProducto, prCosto, prVenta, codCliente, contenedorTabla,
                    texfieldStock, descripcionProd, 2, kgBulto, tablaPesos, scrollPesos);
            mj.setCodigo(indiceSelleccionado);
            return mj;
        } else {
            //    JOptionPane.showMessageDialog(null, "seleccione una fila para eliminar");
            mj.setCodigo(2);
            mj.setMensaje("Seleccione una fila para editar");
            return mj;
        }

    }

    public void calcularTotalAPagar(JTable tbResumenVenta, JLabel saldoDisponible, JLabel total,
            JTextField limiteCompra, JLabel deuda, String metodoPago) {
        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        BigDecimal totalSubtotal = new BigDecimal("0.0");
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        if (!limiteCompra.getText().equals("")) {
            BigDecimal disponible;
            try {
                disponible = new BigDecimal(saldoDisponible.getText().substring(2).replace(".", "").replace(",", "."));

            } catch (Exception e) {
                disponible = new BigDecimal("0.0");
            }

            BigDecimal limite = new BigDecimal(limiteCompra.getText().substring(2).replace(".", "").replace(",", "."));
            BigDecimal saldoDeuda;
            boolean verificaddor = false;

            if (limiteCompra.getText().equalsIgnoreCase("----") || limiteCompra.getText().equalsIgnoreCase("")) {
                limite = new BigDecimal("0.0");
            } else {

                //   String  fromat1=limiteCompra.getText().substring(2).replace(".", "").replace(",", ".");
                limite = new BigDecimal(limiteCompra.getText().substring(2).replace(".", "").replace(",", "."));

                verificaddor = true;
            }
            if (deuda.getText().equalsIgnoreCase("----")) {
                saldoDeuda = new BigDecimal("0.0");
            } else {
                saldoDeuda = new BigDecimal(deuda.getText().substring(2).replace(".", "").replace(",", "."));
                verificaddor = true;
            }

            if (modelo.getRowCount() == 0) {
                totalSubtotal = new BigDecimal("0.0");

                if (metodoPago.equalsIgnoreCase("CUENTA CORRIENTE")) {
                    disponible = limite.subtract(saldoDeuda.add(totalSubtotal));

                    String textoSetear = formatoMoneda.format(disponible);
                    if (disponible.compareTo(BigDecimal.ZERO) <= 0) {
                        Font fuente = new Font("serif", Font.BOLD, 14);
                        textoSetear = "SIN SALDO";
                        saldoDisponible.setForeground(Color.white);
                        saldoDisponible.setBackground(Color.red);
                        saldoDisponible.setOpaque(true);
                        saldoDisponible.setHorizontalAlignment(JLabel.CENTER);
                        saldoDisponible.setFont(fuente);
                    } else {
                        saldoDisponible.setForeground(null);
                        saldoDisponible.setBackground(null);
                        saldoDisponible.setOpaque(false);
                        saldoDisponible.setHorizontalAlignment(JLabel.LEFT);
                        saldoDisponible.setFont(null);
                    }

                    saldoDisponible.setText(textoSetear);
                }
                total.setText(formatoMoneda.format(totalSubtotal));
            } else {
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    //  totalSubtotal =new   BigDecimal(totalSubtotal);
                    BigDecimal otro = new BigDecimal(modelo.getValueAt(i, 6).
                            toString().substring(2).replace(".", "").replace(",", "."));

                    totalSubtotal = totalSubtotal.add(otro);
                    total.setText(formatoMoneda.format(totalSubtotal));

                    if (metodoPago.equalsIgnoreCase("CUENTA CORRIENTE")) {
                        disponible = limite.subtract(saldoDeuda.add(totalSubtotal));
                        if (verificaddor == true) {
                            String textoSetear = formatoMoneda.format(disponible);
                            if (disponible.compareTo(BigDecimal.ZERO) <= 0) {
                                Font fuente = new Font("serif", Font.BOLD, 14);
                                if (disponible.compareTo(BigDecimal.ZERO) == 0) {
                                    textoSetear = "SIN SALDO";
                                }
                                saldoDisponible.setForeground(Color.white);
                                saldoDisponible.setBackground(Color.red);
                                saldoDisponible.setOpaque(true);
                                saldoDisponible.setHorizontalAlignment(JLabel.CENTER);
                                saldoDisponible.setFont(fuente);
                            } else {
                                saldoDisponible.setForeground(null);
                                saldoDisponible.setBackground(null);
                                saldoDisponible.setOpaque(false);
                                saldoDisponible.setHorizontalAlignment(JLabel.LEFT);
                                saldoDisponible.setFont(null);
                            }

                            saldoDisponible.setText(textoSetear);
                        }
                    }
                }
            }
        } else {
            if (modelo.getRowCount() == 0) {
                totalSubtotal = new BigDecimal("0.0");
                total.setText(formatoMoneda.format(totalSubtotal));
            } else {
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    //  totalSubtotal =new   BigDecimal(totalSubtotal);
                    BigDecimal otro = new BigDecimal(modelo.getValueAt(i, 6).toString().
                            substring(2).replace(".", "").replace(",", "."));

                    totalSubtotal = totalSubtotal.add(otro);

                    total.setText(formatoMoneda.format(totalSubtotal));

                }
            }
        }

    }

    public void guardarDatosConfirmar(String metodoPago, Double DineroPagado, Double importeFactura,
            String observaciones, Double SaldoCliente, Integer codigoCliente,
            JTable tbResumenVenta, JDesktopPane contenedor, JLabel mensajeador) {

        if (SaldoCliente == null) {
            SaldoCliente = 0d;
        }

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Connection con = objetoConexion.estableceConexion(); // Una sola conexión para todo

        LocalDateTime hoy = LocalDateTime.now();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha = hoy.format(formater);

        int facturaNumero = 0;

        // Consultas SQL actualizadas
        String consultaFactura = "INSERT INTO factura (fechaFactura, cod_cliente, metodopago, observaciones, montototal, adelanto, pagado, posteriores, acuenta,estado) VALUES (?,?,?,?,?,?,?,?,?,'ACTIVA')";
        String consultaDetalle = "INSERT INTO detalle (fkfactura, fkproducto, cantidad, precioventa, preciocompra, fk_stock_detalle) VALUES (?,?,?,?,?,?)";
        String consultaStockGen = "UPDATE producto SET stock = stock - ? WHERE codigoproducto = ?";
        String consultaActualizaCliente = "UPDATE cliente SET saldo_acumulado = ? WHERE codigocliente = ?";
        String consultaRegistrarPago = "INSERT INTO pagos (fecha, importe, idcliente, facturaafectada, tipo, metodo) VALUES (?,?,?,?,?,?)";

        try {
            con.setAutoCommit(false); // Iniciamos transacción para seguridad de datos

            // 1. GUARDAR FACTURA
            PreparedStatement psFactura = con.prepareStatement(consultaFactura, Statement.RETURN_GENERATED_KEYS);
            psFactura.setString(1, fecha);
            psFactura.setInt(2, codigoCliente);
            psFactura.setString(3, metodoPago);
            psFactura.setString(4, observaciones);
            psFactura.setDouble(5, importeFactura);
            psFactura.setDouble(6, DineroPagado);
            psFactura.setDouble(8, 0);

            double aCuenta = 0;
            if (metodoPago.equalsIgnoreCase("Cuenta Corriente")) {
                aCuenta = importeFactura - DineroPagado;
                psFactura.setBoolean(7, (aCuenta == 0));
                psFactura.setDouble(9, aCuenta);

                PreparedStatement psClie = con.prepareStatement(consultaActualizaCliente);
                psClie.setDouble(1, SaldoCliente + aCuenta);
                psClie.setInt(2, codigoCliente);
                psClie.executeUpdate();
            } else {
                psFactura.setBoolean(7, true);
                psFactura.setDouble(9, 0);
            }

            psFactura.executeUpdate();

            // Obtener el ID de la factura recién creada
            ResultSet rsId = psFactura.getGeneratedKeys();
            if (rsId.next()) {
                facturaNumero = rsId.getInt(1);
            }

            // 2. GUARDAR DETALLES Y ACTUALIZAR STOCK
            PreparedStatement psDetalle = con.prepareStatement(consultaDetalle);
            PreparedStatement psStock = con.prepareStatement(consultaStockGen);

            int filas = tbResumenVenta.getRowCount();
            for (int i = 0; i < filas; i++) {
                int idProducto = Integer.parseInt(tbResumenVenta.getValueAt(i, 0).toString());
                double cantidad = Double.parseDouble(tbResumenVenta.getValueAt(i, 4).toString());

                // CAPTURAMOS EL ID DEL PESO (Columna 7)
                Object valorIdPeso = tbResumenVenta.getValueAt(i, 7);
                String idDetallePeso = (valorIdPeso != null) ? valorIdPeso.toString() : "0";

                double precioVenta = Double.parseDouble(tbResumenVenta.getValueAt(i, 5).toString().substring(2).replace(".", "").replace(",", "."));
                double precioCompra = Double.parseDouble(tbResumenVenta.getValueAt(i, 2).toString().substring(2).replace(".", "").replace(",", "."));

                psDetalle.setInt(1, facturaNumero);
                psDetalle.setInt(2, idProducto);
                psDetalle.setDouble(3, cantidad);
                psDetalle.setDouble(4, precioVenta);
                psDetalle.setDouble(5, precioCompra);

                if (!idDetallePeso.equals("0") && !idDetallePeso.isEmpty()) {
                    psDetalle.setInt(6, Integer.parseInt(idDetallePeso));

                    psDetalle.setInt(6, Integer.parseInt(idDetallePeso));
                    // IMPORTANTE: Pasamos 'con', la conexión que ya está en curso
                    actualizarEstadoPeso(idDetallePeso, "VENDIDO", con);
                } else {
                    psDetalle.setNull(6, java.sql.Types.INTEGER);
                }
                psDetalle.executeUpdate();

                psStock.setDouble(1, cantidad);
                psStock.setInt(2, idProducto);
                psStock.executeUpdate();
            }

            // 3. REGISTRAR PAGO SI CORRESPONDE
            if (DineroPagado > 0) {
                PreparedStatement psPago = con.prepareStatement(consultaRegistrarPago);
                psPago.setString(1, fecha);
                psPago.setDouble(2, DineroPagado);
                psPago.setInt(3, codigoCliente);
                psPago.setString(4, "[" + facturaNumero + "]");
                psPago.setString(5, "PAGA FACTURA DEL DIA");
                psPago.setString(6, metodoPago);
                psPago.executeUpdate();
            }

            con.commit(); // Confirmamos todos los cambios en la DB

            DefaultTableModel modeloTabla = (DefaultTableModel) tbResumenVenta.getModel();
            modeloTabla.setRowCount(0);
            mensajeador.setText("Venta realizada!!! - Factura Nº: " + facturaNumero);

            // Abrir vista de factura
            FormVenta fv = new FormVenta();
            fv.abrirvistaFactura(facturaNumero, contenedor, 0);

        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
            } // Si falla, deshacemos todo
            JOptionPane.showMessageDialog(null, "Error al Guardar Venta: " + e.toString());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
            objetoConexion.cerrarConexion();
        }
    }

    public void bloquearEdicionPrecios(int num, JTextField prCosto,
            JTextField textFieldStock, JTextField prVenta, JTextField limiteCompra) {
        Boolean editar = true;
        if (num == 1) {
            editar = false;
        }
        //  limiteCompra.setEditable(editar);
        prCosto.setEditable(editar);
        textFieldStock.setEditable(editar);
        prVenta.setEditable(editar);

    }

    public void limpiarCamposFacturacion(JTextField fieldBuscador, JTextField codigoProducto,
            JTextField costo, JTextField venta, JTextField cantidad,
            JTextField Stock, JLabel labelDescripcion, JLabel kgBulto) {
        fieldBuscador.setText("");;
        codigoProducto.setText("");
        labelDescripcion.setText("");
        costo.setText("");
        venta.setText("");
        cantidad.setText("");
        Stock.setText("");
        labelDescripcion.setText("");
        kgBulto.setText("");
    }

    public Mensaje actualizarTabla(JTable tbResumenVenta, JLabel saldoDisponible, JLabel total,
            JTextField limiteCompra, JLabel deuda) {
        Mensaje mj = new Mensaje();
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        int fila = tbResumenVenta.getRowCount();
        int columnas = tbResumenVenta.getColumnCount();
        Object[][] matriz = new Object[fila][columnas];
        for (int i = 0; i < fila; i++) {
            for (int j = 0; j < columnas; j++) {
                if (j == 4) {

                    String texto = tbResumenVenta.getValueAt(i, 5).toString();
                    //(Object) formatoMoneda.format(tbResumenVenta.getValueAt(i, 4).
                    //     toString().substring(2).replace(".", "").replace(",", "."))

                    matriz[i][j] = (Object) formatoMoneda.format(texto);
                } else {
                    matriz[i][j] = tbResumenVenta.getValueAt(i, j);
                }
            }
        }

        String[] nColumnas = new String[columnas];
        for (int j = 0; j < columnas; j++) {
            nColumnas[j] = tbResumenVenta.getColumnName(j);
        }

        DefaultTableModel mimodelo = new DefaultTableModel(matriz, nColumnas);

        tbResumenVenta.setModel(mimodelo);

        /*tbResumenVenta.setCellSelectionEnabled(true);
            tbResumenVenta.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            tbResumenVenta.changeSelection(indiceSelleccionado, 3, false, false);
            tbResumenVenta.changeSelection(indiceSelleccionado, 4, false, true);
             mj.setCodigo(1);
            mj.setMensaje("Haz doble click en el precio o cantidad a editar, al finalizar presiona ENTER");
         */
        return mj;
    }

    public Mensaje actualizarTablaCliente(JTable tbProductos, Integer codCliente, JTextField prVenta) {

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        DefaultTableModel modelo = (DefaultTableModel) tbProductos.getModel();
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        String preciosActualizados = "";
        Mensaje mj = null;
        int contador = 0;

        try {

            for (int i = 0; i < modelo.getRowCount(); i++) {
                int idSelecte = Integer.parseInt(modelo.getValueAt(i, 0).toString());

                String consulta = "select producto.codigoproducto, producto.descripcion, producto.precio_compra,"
                        + " producto.precio_venta, producto.stock, "
                        + " producto.proveedor, producto.rubro, preciocliente.diferido, preciocliente.costoanterior from preciocliente inner join  producto "
                        + " where preciocliente.fkcliente = ? and producto.codigoproducto=? and preciocliente.fkProducto=? and  producto.activo!=FALSE";

                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);

                ps.setInt(1, codCliente);

                ps.setInt(2, idSelecte);

                ps.setInt(3, idSelecte);

                ResultSet rs = ps.executeQuery();
                Double diferido = null;
                Double prCostoVar = null;
                String descripcion = null;
                Double cantidad = Double.parseDouble(modelo.getValueAt(i, 4).toString());

                if (rs.next()) {

                    prCostoVar = rs.getDouble("precio_compra");
                    descripcion = rs.getString("descripcion");
                    Double precioverificar = rs.getDouble("costoanterior");
                    diferido = rs.getDouble("diferido");

                    //verifico que no hayan subas
//   limiteCompra.setText(formatoMoneda.format(rs.getDouble("limite_compra")));
                    modelo.setValueAt((Object) formatoMoneda.format(diferido), i, 5);

                    if (!precioverificar.equals(prCostoVar)) {
                        mj = new Mensaje();
                        mj.setCodigo(3);
                        contador++;
                        preciosActualizados = preciosActualizados + descripcion + ", ";
                        //  mj.setMensaje("Hubo una suba, por favor verifica tus costos, Precio anterior: ";
                    }
                } else {
                    String consulta1 = "select producto.codigoproducto, producto.descripcion, producto.precio_compra,"
                            + " producto.precio_venta, producto.stock, "
                            + " producto.proveedor, producto.rubro from producto "
                            + " where codigoproducto=? and  producto.activo!=FALSE";
                    ps = objetoConexion.estableceConexion().prepareStatement(consulta1);

                    ps.setInt(1, idSelecte);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        diferido = rs.getDouble("precio_venta");
                    }

                }
                mj = new Mensaje();
                mj.setCodigo(1);
                mj.setMensaje("Se actualizaron los precios");
                modelo.setValueAt((Object) formatoMoneda.format(diferido), i, 5);
                modelo.setValueAt((Object) formatoMoneda.format(diferido * cantidad), i, 6);
            }

            if (contador > 0) {
                if (contador == 1) {
                    if (codCliente != 1) {
                        mj.setCodigo(3);
                        preciosActualizados = "Hubo una modificacion en: " + preciosActualizados + " Por favor revisa tus costos";
                        mj.setMensaje(preciosActualizados);
                        prVenta.setEditable(true);
                        //   prVenta.requestFocus();
                        ControladorAdministracion ca = new ControladorAdministracion();
                        prVenta.setBackground(ca.retornatexField());
                    }
                }
            }

        } catch (Exception e) {
            mj = new Mensaje();
            mj.setCodigo(4);
            mj.setMensaje("Error al actualizar tabla, contacta al administrador: " + e.toString());
            // JOptionPane.showMessageDialog(null, "Error al actualizar tabla, contacta al administrador: " + e.toString());
        } finally {
            objetoConexion.cerrarConexion();

        }
        return mj;
    }

    public Mensaje actualizarTablaClientePrim(JTable tbProductos, Integer codCliente) {

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        DefaultTableModel modelo = (DefaultTableModel) tbProductos.getModel();
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        String preciosActualizados = "";
        Mensaje mj = null;
        int contador = 0;
        try {
            for (int i = 0; i < modelo.getRowCount(); i++) {
                int idSelecte = Integer.parseInt(modelo.getValueAt(i, 0).toString());

                String consulta = "select producto.codigoproducto, producto.descripcion, producto.precio_compra,"
                        + " producto.precio_venta, producto.stock, "
                        + " producto.proveedor, producto.rubro, preciocliente.diferido, preciocliente.costoanterior from preciocliente inner join  producto "
                        + " where preciocliente.fkcliente = ? and producto.codigoproducto=? and preciocliente.fkProducto=? and  producto.activo!=FALSE";

                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);

                ps.setInt(1, codCliente);

                ps.setInt(2, idSelecte);

                ps.setInt(3, idSelecte);

                ResultSet rs = ps.executeQuery();
                Double diferido = null;
                Double prCostoVar = null;
                String descripcion = null;
                Double cantidad = Double.parseDouble(modelo.getValueAt(i, 4).toString());

                if (rs.next()) {

                    prCostoVar = rs.getDouble("precio_compra");
                    descripcion = rs.getString("descripcion");
                    Double precioverificar = rs.getDouble("costoanterior");
                    diferido = rs.getDouble("diferido");
                    modelo.setValueAt((Object) formatoMoneda.format(diferido), i, 5);
                    if (!precioverificar.equals(prCostoVar)) {
                        mj = new Mensaje();
                        mj.setCodigo(3);
                        contador++;
                        preciosActualizados = preciosActualizados + descripcion + ", ";
                        //  mj.setMensaje("Hubo una suba, por favor verifica tus costos, Precio anterior: ";
                    }
                } else {
                    String consulta1 = "select producto.codigoproducto, producto.descripcion, producto.precio_compra,"
                            + " producto.precio_venta, producto.stock, "
                            + " producto.proveedor, producto.rubro from producto "
                            + " where codigoproducto=? and  producto.activo!=FALSE";
                    ps = objetoConexion.estableceConexion().prepareStatement(consulta1);

                    ps.setInt(1, idSelecte);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        diferido = rs.getDouble("precio_venta");
                    }

                }
                mj = new Mensaje();
                mj.setCodigo(1);
                mj.setMensaje("Se actualizaron los precios");
                modelo.setValueAt((Object) formatoMoneda.format(diferido), i, 5);
                modelo.setValueAt((Object) formatoMoneda.format(diferido * cantidad), i, 6);
            }

            if (contador > 0) {
                if (codCliente != 1) {
                    if (contador == 1) {
                        mj.setCodigo(3);
                        preciosActualizados = "Hubo una modificacion en: " + preciosActualizados + " Por favor revisa tus costos";
                        mj.setMensaje(preciosActualizados);
                    }
                }
            }

        } catch (Exception e) {
            mj = new Mensaje();
            mj.setCodigo(4);
            mj.setMensaje("Error al actualizar tabla, contacta al administrador: " + e.toString());
        } finally {
            objetoConexion.cerrarConexion();

        }
        return mj;
    }

    public Mensaje anularFactura(int facturaId) {
        Mensaje mj = new Mensaje();
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Connection con = objetoConexion.estableceConexion();

        try {
            con.setAutoCommit(false); // Iniciar transacción

            // 1. Obtener datos de la factura antes de anular
            String sqlDatos = "SELECT cod_cliente, metodopago, montototal, adelanto, estado FROM factura WHERE codigo = ?";
            PreparedStatement psDatos = con.prepareStatement(sqlDatos);
            psDatos.setInt(1, facturaId);
            ResultSet rsF = psDatos.executeQuery();

            if (!rsF.next() || rsF.getString("estado").equals("INACTIVA")) {
                //  JOptionPane.showMessageDialog(null, "La factura no existe o ya está anulada.");
                mj.setCodigo(3);
                mj.setMensaje("La factura no existe o ya está anulada.");
                return mj;
            }

            int idCliente = rsF.getInt("cod_cliente");
            double montoTotal = rsF.getDouble("montototal");
            double adelanto = rsF.getDouble("adelanto");
            String metodo = rsF.getString("metodopago");

            // 2. Restablecer Stock y Estado de Pesos Individuales (Stock Detalle)
            String sqlDetalle = "SELECT fkproducto, cantidad, fk_stock_detalle FROM detalle WHERE fkfactura = ?";
            PreparedStatement psDet = con.prepareStatement(sqlDetalle);
            psDet.setInt(1, facturaId);
            ResultSet rsD = psDet.executeQuery();

            while (rsD.next()) {
                int idProd = rsD.getInt("fkproducto");
                double cant = rsD.getDouble("cantidad");
                int idPeso = rsD.getInt("fk_stock_detalle");

                // Devolver stock general
                String upStock = "UPDATE producto SET stock = stock + ? WHERE codigoproducto = ?";
                PreparedStatement psUpS = con.prepareStatement(upStock);
                psUpS.setDouble(1, cant);
                psUpS.setInt(2, idProd);
                psUpS.executeUpdate();

                // Si tenía un peso individual, volver a ponerlo DISPONIBLE
                if (idPeso > 0) {
                    String upPeso = "UPDATE stock_detalle SET estado = 'DISPONIBLE' WHERE id = ?";
                    PreparedStatement psUpP = con.prepareStatement(upPeso);
                    psUpP.setInt(1, idPeso);
                    psUpP.executeUpdate();
                }
            }

            // 3. Revertir Saldo del Cliente si fue Cuenta Corriente
            if (metodo.equalsIgnoreCase("Cuenta Corriente")) {
                double deudaGenerada = montoTotal - adelanto;
                String upSaldo = "UPDATE cliente SET saldo_acumulado = saldo_acumulado - ? WHERE codigocliente = ?";
                PreparedStatement psSal = con.prepareStatement(upSaldo);
                psSal.setDouble(1, deudaGenerada);
                psSal.setInt(2, idCliente);
                psSal.executeUpdate();
            }

            // 4. Marcar Factura como INACTIVA
            String sqlAnular = "UPDATE factura SET estado = 'INACTIVA' WHERE codigo = ?";
            PreparedStatement psAnu = con.prepareStatement(sqlAnular);
            psAnu.setInt(1, facturaId);
            psAnu.executeUpdate();

            // 5. (Opcional) Eliminar o marcar como anulado el pago registrado
            String sqlAnuPago = "DELETE FROM pagos WHERE facturaafectada = ?";
            PreparedStatement psAnuP = con.prepareStatement(sqlAnuPago);
            psAnuP.setString(1, "[" + facturaId + "]");
            psAnuP.executeUpdate();

            con.commit();
            // JOptionPane.showMessageDialog(null, "Factura Nº " + facturaId + " anulada con éxito. Stock y saldos restablecidos.");

            mj.setCodigo(1);
            mj.setMensaje("Factura Nº " + facturaId + " anulada con éxito. Stock y saldos restablecidos.");

        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
            }
            mj.setCodigo(4);
            mj.setMensaje("Error al anular: " + e.toString());
            //   JOptionPane.showMessageDialog(null, "Error al anular: " + e.toString());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
        return mj;
    }

}
