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

/**
 *
 * @author ALCIDES
 */
public class ControladorCliente {

    public boolean buscarCliente(JTextField codCliente, JTable listaSugerencias, JScrollPane contenedorTabla) {
        boolean returno = false;
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloCliente objetoCliente = new Modelos.ModeloCliente();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("descripcion");
        modelo.addColumn("id");

        Boolean numeroLetra = false;
        Integer numero;
        try {
            numero = Integer.parseInt(codCliente.getText());
            numeroLetra = true;
        } catch (Exception e) {
            numeroLetra = false;
        }

        try {

            String consulta = null;
            if (numeroLetra == false) {
                consulta = "select cliente.codigocliente, cliente.nombre,cliente.telefono,cliente.cuit,"
                        + "cliente.direccion, cliente.diaspago,cliente.saldo_acumulado,cliente.limite_compra "
                        + "from cliente where cliente.nombre like concat('%',?,'%')and  cliente.activo!=FALSE";

            } else {
                consulta = "select cliente.codigocliente, cliente.nombre,cliente.telefono,cliente.cuit,"
                        + "cliente.direccion, cliente.diaspago,cliente.saldo_acumulado,"
                        + "cliente.limite_compra from cliente  where cliente.codigocliente like concat('%',?,'%')and  cliente.activo!=FALSE";

            }

            if (!codCliente.getText().equalsIgnoreCase("")) {

                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
                ps.setString(1, codCliente.getText());
                ResultSet rs = ps.executeQuery();

                int contador = 0;
              int tamaño= listaSugerencias.getRowHeight();
                System.out.println("tamaño row"+tamaño);  
              
              
                while (rs.next()) {
                    returno = true;
                    objetoCliente.setCodigo(rs.getInt("codigocliente"));
                    objetoCliente.setNombre(rs.getString("nombre"));
                    objetoCliente.setTelefono(rs.getInt("telefono"));
                    objetoCliente.setCuitDni(rs.getInt("cuit"));
                    objetoCliente.setLimiteCompra(rs.getDouble("limite_compra"));
                    objetoCliente.setSaldoAcumulado(rs.getDouble("saldo_acumulado"));
                    objetoCliente.setDireccion(rs.getString("direccion"));
                    //   objetoCliente.setDiasPago(Integer.parseInt(rs.getString("diaspago")));

                    modelo.addRow(new Object[]{objetoCliente.getNombre(), objetoCliente.getCodigo()});
                    contador = contador + tamaño;
                    System.out.println(rs.getString("nombre"));
                }
                listaSugerencias.setModel(modelo);          
                contenedorTabla.setSize(170, contador+6);
                
                
                
                
                contenedorTabla.setVerticalScrollBarPolicy(contenedorTabla.VERTICAL_SCROLLBAR_NEVER);

                listaSugerencias.getTableHeader().setPreferredSize(new Dimension(180, 0));
                listaSugerencias.setRowMargin(0);
                //  listaSugerencias.setBounds(10, 100, 50, 150);

//no modificar
                for (int i = 0; i < listaSugerencias.getColumnCount(); i++) {
                    Class<?> colClas = listaSugerencias.getColumnClass(i);
                    listaSugerencias.setDefaultEditor(colClas, null);
                }

                //oculto la segunda columna
                // TableColumnModel tcm= listaSugerencias.getTableHeader().getColumnModel();
                // tcm.removeColumn(tcm.getColumn(1));
                listaSugerencias.getColumn("id").setMinWidth(0);
                listaSugerencias.getColumn("id").setMaxWidth(0);
                listaSugerencias.getColumn("id").setWidth(0);

//contenedorTabla
            }
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "14 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }

        contenedorTabla.setVisible(true);
        return returno;
    }

    public boolean buscarClientesinCF(JTextField codCliente, JTable listaSugerencias, 
            JScrollPane contenedorTabla,String activo) {
        boolean returno = false;
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        Modelos.ModeloCliente objetoCliente = new Modelos.ModeloCliente();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("descripcion");
        modelo.addColumn("id");
        System.out.println("entra por este camino");
        
              boolean act = false;
        switch (activo) {
            case "ACTIVOS":
                act = false;
                break;
            case "INACTIVOS":
                act = true;
                break;

            default:
                act = false;
        }
        
        
        
        
        Boolean numeroLetra = false;
        Integer numero;
        try {
            numero = Integer.parseInt(codCliente.getText());
            numeroLetra = true;
        } catch (Exception e) {
            numeroLetra = false;
        }

        try {

            String consulta = null;
            if (numeroLetra == false) {
                consulta = "select cliente.codigocliente, cliente.nombre,cliente.telefono,cliente.cuit,"
                        + "cliente.direccion, cliente.diaspago,cliente.saldo_acumulado,cliente.limite_compra "
                        + "from cliente where cliente.nombre like concat('%',?,'%')and "
                        + " cliente.activo!="+act+" and cliente.codigocliente!=1";

            } else {
                consulta = "select cliente.codigocliente, cliente.nombre,cliente.telefono,cliente.cuit,"
                        + "cliente.direccion, cliente.diaspago,cliente.saldo_acumulado,"
                        + "cliente.limite_compra from cliente  where cliente.codigocliente like concat('%',?,'%')and "
                        + " cliente.activo!="+act+" and cliente.codigocliente!=1";

            }
            System.out.println(consulta);
            
            if (!codCliente.getText().equalsIgnoreCase("")) {

                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
                ps.setString(1, codCliente.getText());
                ResultSet rs = ps.executeQuery();

                int contador = 0;            
              int tamaño= listaSugerencias.getRowHeight();    
               //     contador = contador + tamaño;
               //         contenedorTabla.setSize(170, contador+5);

                
                while (rs.next()) {
                    returno = true;
                    objetoCliente.setCodigo(rs.getInt("codigocliente"));
                    objetoCliente.setNombre(rs.getString("nombre"));
                    objetoCliente.setTelefono(rs.getInt("telefono"));
                    objetoCliente.setCuitDni(rs.getInt("cuit"));
                    objetoCliente.setLimiteCompra(rs.getDouble("limite_compra"));
                    objetoCliente.setSaldoAcumulado(rs.getDouble("saldo_acumulado"));
                    objetoCliente.setDireccion(rs.getString("direccion"));
                    //   objetoCliente.setDiasPago(Integer.parseInt(rs.getString("diaspago")));

                    modelo.addRow(new Object[]{objetoCliente.getNombre(), objetoCliente.getCodigo()});
                     contador = contador + tamaño;
                }

//AQUI DIMENSIONO LAS SELECCIONES
                listaSugerencias.setModel(modelo);
             //   listaSugerencias.setRowHeight(18);
               contenedorTabla.setSize(170, contador+6);
                contenedorTabla.setVerticalScrollBarPolicy(contenedorTabla.VERTICAL_SCROLLBAR_NEVER);

                listaSugerencias.getTableHeader().setPreferredSize(new Dimension(180, 0));
                listaSugerencias.setRowMargin(0);
                //  listaSugerencias.setBounds(10, 100, 50, 150);

//no modificar
                for (int i = 0; i < listaSugerencias.getColumnCount(); i++) {
                    Class<?> colClas = listaSugerencias.getColumnClass(i);
                    listaSugerencias.setDefaultEditor(colClas, null);
                }

                //oculto la segunda columna
                // TableColumnModel tcm= listaSugerencias.getTableHeader().getColumnModel();
                // tcm.removeColumn(tcm.getColumn(1));
                listaSugerencias.getColumn("id").setMinWidth(0);
                listaSugerencias.getColumn("id").setMaxWidth(0);
                listaSugerencias.getColumn("id").setWidth(0);

//contenedorTabla
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "15 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
       } finally {
            objetoConexion.cerrarConexion();
        }

        contenedorTabla.setVisible(true);
        return returno;
    }

    public Integer verCodigoDisponlible(JTextField tex) {
        Integer codigo = null;
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        String sql = " select (t1.codigocliente+1) as disponible from cliente "
                + "as t1 left join cliente as t2 on t1.codigocliente+1=t2.codigocliente"
                + " where t2.codigocliente is null order by t1.codigocliente limit 1;";

        try {
            PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            int contador = 0;
            while (rs.next()) {
                tex.setText(rs.getString("disponible"));
                codigo = (rs.getInt("disponible"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "no anda w12344e" + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
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
            JScrollPane contenedorTabla, JLabel descripcionProd, JCheckBox editable,JLabel kgBulto,
                 JTable tablaPesos, JScrollPane scrollPesos    ) {
        /*
limpiarCampos(fieldNombre, fieldDni, fieldDireccion,
                        fieldTelefono, codCliente, limiteCompra, deuda,
                        txtBuscarProducto, metodoPago, obs,
                        dias, prCosto, texfieldStock, prVenta, saldoDisponible, 1, labelCodCliente);
         */

        Boolean existe = false;
        for (int i = 0; i < metodoPago.getItemCount(); i++) {
            if (metodoPago.getItemAt(i).equals("Cuenta Corriente")) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            metodoPago.addItem("Cuenta Corriente");
        }

        int fila = listaCliente.getSelectedRow();
        Mensaje mj = null;

        // FormVenta fv = new FormVenta();
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        Modelos.ModeloProducto objetoProducto = new Modelos.ModeloProducto();
        DefaultTableModel modelo = new DefaultTableModel();
        //     modelo.addColumn("descripcion");
        //     modelo.addColumn("id");
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();

        Integer idSelecte = 0;

        if (verificador == 1) {
            if (!codCliente.getText().equals("")) {

                try {
                    idSelecte = Integer.parseInt(codCliente.getText());
                    fila = 1;
                } catch (Exception e) {

                    codCliente.setText("");
                    limpiarCampos(fieldNombre, fieldDni,
                            fieldDireccion, fieldTelefono, codCliente,
                            limiteCompra, deuda, txtBuscarProducto, metodoPago,
                            obs, dias, prCosto, texfieldStock, prVenta, saldoDisponible, 1, labelCodCliente);
                    codCliente.setText("");
                }

            } else {

                fila = 0;
                //    txtBuscarProducto.requestFocus();
                codCliente.setText("");
            }

        } else {
            idSelecte = Integer.parseInt(listaCliente.getValueAt(fila, 1).toString());
        }

        try {
            if (fila >= 0) {

                codCliente.setText(idSelecte.toString());

                String consulta = "select cliente.codigocliente, cliente.nombre,cliente.telefono,cliente.cuit,"
                        + "cliente.direccion, cliente.diaspago, cliente.saldo_acumulado,cliente.limite_compra, "
                        + "cliente.metodopago ,cliente.observaciones from cliente  where cliente.codigocliente =?  and  cliente.activo!=false;";

                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);

                ps.setString(1, codCliente.getText());

                ResultSet rs = ps.executeQuery();
                ControladorVenta cv = new ControladorVenta();

                if (!rs.isBeforeFirst()) {
                    System.out.println("yo creo que entra por aqui");
                    //  codCliente.requestFocus();
                    if (!editable.isSelected()) {
                        codCliente.selectAll();
                        limpiarCampos(fieldNombre, fieldDni,
                                fieldDireccion, fieldTelefono, codCliente,
                                limiteCompra, deuda, txtBuscarProducto, metodoPago,
                                obs, dias, prCosto, texfieldStock, prVenta, saldoDisponible, 1, labelCodCliente);
                        codCliente.setText("");
                    } else {
                        String codigo = codCliente.getText();
                        limpiarCampos(fieldNombre, fieldDni,
                                fieldDireccion, fieldTelefono, codCliente,
                                limiteCompra, deuda, txtBuscarProducto, metodoPago,
                                obs, dias, prCosto, texfieldStock, prVenta, saldoDisponible, 1, labelCodCliente);
                        codCliente.setText(codigo);
                    }  //   
                } else {
                    int cliente = 0;

                    limpiarCampos(fieldNombre, fieldDni, fieldDireccion,
                            fieldTelefono, codCliente, limiteCompra, deuda,
                            txtBuscarProducto, metodoPago, obs,
                            dias, prCosto, texfieldStock, prVenta, saldoDisponible, 1, labelCodCliente);
                    codCliente.setText("");
                    while (rs.next()) {
                        // fieldNombre.setText(rs.getString("nombre"));
                        cliente = rs.getInt("codigocliente");

                        codCliente.setText(rs.getString("codigocliente"));
                        labelCodCliente.setText(rs.getString("codigocliente"));
                        fieldNombre.setText(rs.getString("nombre"));

                        if (cliente != 1) {

                            fieldDni.setText(rs.getString("cuit"));
                            limiteCompra.setText(rs.getString("limite_compra"));
                            fieldTelefono.setText(rs.getString("telefono"));
                            fieldDireccion.setText(rs.getString("direccion"));
                            limiteCompra.setText(formatoMoneda.format(rs.getDouble("limite_compra")));
                            deuda.setText(formatoMoneda.format(rs.getDouble("saldo_acumulado")));
                            dias.setText(rs.getString("diaspago"));

                            String medioPago = rs.getString("metodopago");

                            String textoObservaciones = "";

                            if (rs.getString("observaciones") == null) {
                                textoObservaciones = "Agregar observaciones del Cliente";
                            } else {
                                textoObservaciones = (rs.getString("observaciones"));
                            }

                            try {
                                if (medioPago.equals(null)) {
                                    medioPago = "Contado";
                                }
                            } catch (Exception e) {
                                medioPago = "Contado";
                            }

                            metodoPago.setSelectedItem(medioPago);

                            obs.setContentType("text/html");
                            obs.setText("<html><body><p style='margin:2px; text-align: center;"
                                    + "line-height:1; font-size:9px; line-height:1; font-family:consolass; '>" + textoObservaciones
                                    + "</p></body></html>");
                            System.out.println("probando!!!");
                            Double limiteCP = 0d;
                            Double acumuladSALD = 0d;
                            try {
                                limiteCP = Double.parseDouble(rs.getString("limite_compra"));
                            } catch (Exception e) {
                            }
                            try {
                                acumuladSALD = Double.parseDouble(rs.getString("saldo_acumulado"));
                            } catch (Exception e) {
                            }

                            Double disponibe = limiteCP - acumuladSALD;

                            String textoSetear = formatoMoneda.format(disponibe);
                            if (disponibe <= 0) {
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
                    }

                    // deuda.setText(rs.getString("saldo_acumulado"));
                    mj = cv.actualizarTablaCliente(tbProductos, cliente, prVenta);

                    cv.calcularTotalAPagar(tbProductos, saldoDisponible, total, limiteCompra, deuda,metodoPago.getSelectedItem().toString());
                    // txtBuscarProducto.requestFocus();
                    if (!editable.isSelected()) {
                        hacerrEditable(1, fieldNombre, fieldDireccion, fieldDni,
                                fieldTelefono, dias, prCosto, texfieldStock,
                                prVenta, limiteCompra, obs, metodoPago);
                    }

                    /*  public void hacerrEditable(int num, JTextField fieldNombre,
            JTextField fieldDireccion, JTextField fieldDni, JTextField fieldTelefono,
            JTextField dias, JTextField prCosto, JTextField textFieldStock,
            JTextField prVenta, JTextField limiteCompra, JTextPane paneObservaciones) {*/
                }
                contenedorClientes.setVisible(false);
            }

            if (!codigoProducto.getText().equals("")) {

                ControladorVenta cv = new ControladorVenta();
                cv.SeleccionarProductosVenta(tbProductos, fieldCantidad, codigoProducto,
                        txtBuscarProducto, prCosto, prVenta, codCliente, contenedorTabla,
                        texfieldStock, descripcionProd, 4,kgBulto,tablaPesos,scrollPesos);
            }

        } catch (Exception e) {
            //  txtBuscarProducto.requestFocus();
            contenedorClientes.setVisible(false);

           JOptionPane.showMessageDialog(null, "16 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
       } finally {
            objetoConexion.cerrarConexion();
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

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();

        DefaultTableModel modelo = new DefaultTableModel();
        ModeloCliente objetoCliente = null;

        Boolean numeroLetra = false;
        Integer numero;
        try {
            numero = Integer.parseInt(palabra);
            numeroLetra = true;
        } catch (Exception e) {
            numeroLetra = false;
        }

        try {

            String consulta = null;
            if (numeroLetra == false) {
                consulta = "select cliente.codigocliente, cliente.nombre,cliente.telefono,cliente.cuit,"
                        + "cliente.direccion, cliente.diaspago,cliente.saldo_acumulado,cliente.limite_compra "
                        + "from cliente where cliente.nombre like concat('%',?,'%')and  cliente.activo!=FALSE limit 1";

            } else {
                consulta = "select cliente.codigocliente, cliente.nombre,cliente.telefono,cliente.cuit,"
                        + "cliente.direccion, cliente.diaspago,cliente.saldo_acumulado,"
                        + "cliente.limite_compra from cliente  where cliente.codigocliente like concat('%',?,'%')and  cliente.activo!=FALSE limit 1";

            }

            if (!palabra.equalsIgnoreCase("")) {

                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
                ps.setString(1, palabra);
                ResultSet rs = ps.executeQuery();

                int contador = 0;
                while (rs.next()) {
                    objetoCliente = new ModeloCliente();
                    objetoCliente.setCodigo(rs.getInt("codigocliente"));
                    objetoCliente.setNombre(rs.getString("nombre"));
                    objetoCliente.setTelefono(rs.getInt("telefono"));
                    objetoCliente.setCuitDni(rs.getInt("cuit"));
                    objetoCliente.setLimiteCompra(rs.getDouble("limite_compra"));
                    objetoCliente.setSaldoAcumulado(rs.getDouble("saldo_acumulado"));
                    objetoCliente.setDireccion(rs.getString("direccion"));
                    //   objetoCliente.setDiasPago(Integer.parseInt(rs.getString("diaspago")));

                    modelo.addRow(new Object[]{objetoCliente.getNombre(), objetoCliente.getCodigo()});
                    contador = contador + 18;
                }
            }
        } catch (Exception e) {

        } finally {
            objetoConexion.cerrarConexion();
        }
        return objetoCliente;
    }

    public Mensaje agregarClientePrim(String codigo, JTextField nombre,
            JTextField direccion, JTextField telefono, JTextField dni,
            Double limiteSaldo, Double SaldoCliente, JComboBox metodoPago,
            String observaciones, Integer verificador, JTable tabla,
            JTextField dias, Integer codigoCliente, JTextField CodigoClienteField) {

        Mensaje mj = null;

        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        try {

            System.out.println("        codigo cliente que viene" + codigoCliente);
            if (codigoCliente == 0) {
                int cod = 1;
                String consulta1 = "select c.codigocliente+1 as sugerido from cliente c "
                        + "where not exists(select 1 from cliente c2 where"
                        + " c2.codigocliente=c.codigocliente+1)limit 1;";
                PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta1);
                //    ps.setInt(1, cod);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {

                    codigoCliente = (rs.getInt("sugerido"));
                    System.out.println(rs.getInt("sugerido"));
                    System.out.println(codigoCliente + "anteriro codigo cliente");
                }
            }

        } catch (Exception e) {

            mj = new Mensaje();
            mj.setCodigo(4);
            mj.setMensaje("Error al actualizar cliente P, contacta al admministrador " + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
        mj = agregarCliente("NUEVO CLIENTE", nombre, direccion,
                telefono, dni, limiteSaldo,
                0d, metodoPago, observaciones, 1, null, dias, codigoCliente, null,null);
        if (mj.getCodigo() == 1) {
            CodigoClienteField.setText(codigoCliente.toString());
        }

        return mj;

    }

    public Mensaje agregarCliente(String codigo, JTextField nombre,
            JTextField direccion, JTextField telefono, JTextField dni,
            Double limiteSaldo, Double SaldoCliente, JComboBox metodoPago,
            String observaciones, Integer verificador, JTable tabla,
            JTextField dias, Integer codigoCliente, JTextField text,JLabel lblP) {
        Mensaje mj = null;
   
        ModeloCliente cliente = retornaUnCliente(codigoCliente.toString());

        if (nombre.getText().equals("")) {
            mj = new Mensaje();
            mj.setCodigo(3);
            mj.setMensaje("El nombre no puede estar vacio");
            return mj;
        } else {

            System.out.println(SaldoCliente + "    " + verificador + "verif" + codigoCliente);
            Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
            Modelos.ModeloCliente objetoCliente = new Modelos.ModeloCliente();
            Integer verificadorPrimo = verificador;
            if (verificador == 3) {
                verificador = 2;
            }
          
            switch (verificador) {
                case 1:
                    if (cliente != null) {
                        mj = new Mensaje();
                        mj.setCodigo(3);
                int num = verCodigoDisponlible(text);
                 mj.setMensaje("El codigo, ya esta asignado, prueba el: " + num);

                        return mj;
                    }
                  
                    String consulta = "insert into cliente(codigocliente,nombre ,direccion,"
                            + "telefono,cuit,activo,limite_compra,saldo_acumulado,metodopago,observaciones,diaspago) "
                            + "values(?,?,?,?,?,?,?,?,?,?,?)";
                    try {

                        objetoCliente.setCodigo(codigoCliente);
                        objetoCliente.setNombre(nombre.getText());
                        objetoCliente.setDireccion(direccion.getText());
                        try {
                            objetoCliente.setTelefono(Integer.parseInt(telefono.getText()));
                        } catch (Exception e) {
                        }
                        try {
                            objetoCliente.setCuitDni(Integer.parseInt(dni.getText()));

                        } catch (Exception e) {
                        }

                        objetoCliente.setActivo(Boolean.TRUE);
                        objetoCliente.setLimiteCompra(limiteSaldo);
                        objetoCliente.setSaldoAcumulado(SaldoCliente);
                        objetoCliente.setMetodoPago(metodoPago.getSelectedItem().toString());
                        objetoCliente.setObservaciones(observaciones);
                        try {
                            objetoCliente.setDiasPago(Integer.parseInt(dias.getText()));
                        } catch (Exception e) {
                        }
   System.out.println("anda por aqui111");
                     //   CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
                           System.out.println("anda por aqui1234");
                          PreparedStatement cs=objetoConexion.estableceConexion().prepareStatement(consulta);
                           
                        cs.setInt(1, objetoCliente.getCodigo());
                        cs.setString(2, objetoCliente.getNombre());
                        cs.setString(3, objetoCliente.getDireccion());
                        try {
                            cs.setInt(4, objetoCliente.getTelefono());
                        } catch (Exception e) {
                            cs.setString(4, null);
                        }
                        try {
                            cs.setInt(5, objetoCliente.getCuitDni());
                        } catch (Exception e) {
                            cs.setString(5, null);
                        }

                        System.out.println("anda1");
                        cs.setBoolean(6, objetoCliente.getActivo());
                        System.out.println("anda2");
                        try {
                            cs.setDouble(7, limiteSaldo);
                        } catch (Exception e) {
                            cs.setString(7, null);
                        }

                        System.out.println("anda3");
                        try {
                            cs.setDouble(8, objetoCliente.getSaldoAcumulado());
                        } catch (Exception e) {
                            cs.setString(8, null);
                        }

                        System.out.println("anda4");
                        cs.setString(9, objetoCliente.getMetodoPago());
                        System.out.println("anda5");
                        cs.setString(10, objetoCliente.getObservaciones());
                        System.out.println("anda");
                        try {
                            cs.setInt(11, objetoCliente.getDiasPago());
                        } catch (Exception e) {
                            cs.setString(11, null);
                        }
                        System.out.println("muextro la consulta");
                        System.out.println(cs.toString());
                        cs.execute();
                        mj = new Mensaje();
                        mj.setCodigo(1);

                        mj.setMensaje("Se registro el nuevo cliente con el codigo: " + codigoCliente);
                    } catch (Exception e) {
                        mj = new Mensaje();
                        mj.setCodigo(4);
                        System.out.println(e.toString());
                        mj.setMensaje("Error al guardar, contacta al administrador: " + e.toString());
                        //  JOptionPane.showMessageDialog(null, "no anda we13" + e.toString());
                    } finally {
                        objetoConexion.cerrarConexion();
                    }

                    break;
                case 2:
                    String consulta1 = "update cliente set nombre =?,direccion=?,"
                            + "telefono=?,cuit=?,activo=?,limite_compra=?,saldo_acumulado=?,metodopago=?,observaciones=? ,diaspago=?"
                            + " where cliente.codigocliente=?;";
                    try {
                        verificadorPrimo = 3;

                        objetoCliente.setCodigo(codigoCliente);
                        objetoCliente.setNombre(nombre.getText());
                        objetoCliente.setDireccion(direccion.getText());

                        try {
                            objetoCliente.setTelefono(Integer.parseInt(telefono.getText()));
                        } catch (Exception e) {

                        }
                        try {
                            objetoCliente.setCuitDni(Integer.parseInt(dni.getText()));
                        } catch (Exception e) {
                        }
                        System.out.println("    -----------anda4");
                        objetoCliente.setActivo(Boolean.TRUE);

                        objetoCliente.setLimiteCompra(limiteSaldo);
                        objetoCliente.setSaldoAcumulado(SaldoCliente);
                        objetoCliente.setMetodoPago(metodoPago.getSelectedItem().toString());
                        objetoCliente.setObservaciones(observaciones);

                        try {
                            objetoCliente.setDiasPago(Integer.parseInt(dias.getText()));
                        } catch (Exception e) {
                        }
                        System.out.println("    -----------anda3");
                     //   CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta1);
  PreparedStatement cs=objetoConexion.estableceConexion().prepareStatement(consulta1);
                        cs.setString(1, objetoCliente.getNombre());
                        cs.setString(2, objetoCliente.getDireccion());

                        try {
                            cs.setInt(3, objetoCliente.getTelefono());
                        } catch (Exception e) {
                            cs.setString(3, null);
                        }
                        try {
                            cs.setInt(4, objetoCliente.getCuitDni());
                        } catch (Exception e) {
                            cs.setString(4, null);
                        }
                        System.out.println("    -----------anda2");
                        cs.setBoolean(5, objetoCliente.getActivo());

                        //    cs.setDouble(6, objetoCliente.getLimiteCompra());
                        try {
                            cs.setDouble(6, objetoCliente.getLimiteCompra());
                        } catch (Exception e) {
                            cs.setString(6, null);
                        }

                        // cs.setDouble(7, objetoCliente.getSaldoAcumulado());
                        try {
                            cs.setDouble(7, objetoCliente.getSaldoAcumulado());
                        } catch (Exception e) {
                            cs.setString(7, null);
                        }
                        System.out.println("    -----------anda1");
                        cs.setString(8, objetoCliente.getMetodoPago());

                        cs.setString(9, objetoCliente.getObservaciones());
                        try {
                            cs.setInt(10, objetoCliente.getDiasPago());
                        } catch (Exception e) {
                            cs.setString(10, null);
                        }

                        cs.setInt(11, objetoCliente.getCodigo());
                        cs.execute();

                        if (verificadorPrimo == 3) {
                            mj = new Mensaje();
                            mj.setCodigo(1);

                            mj.setMensaje("se actualizo el Cliente: " + objetoCliente.getNombre() + ", correctamente");
                        } else {
                            mj = new Mensaje();
                            mj.setCodigo(1);

                            mj.setMensaje("Se registro el nuevo cliente con el codigo: " + codigoCliente);
                        }

                    } catch (Exception e) {
                        mj = new Mensaje();
                        mj.setCodigo(4);
                        mj.setMensaje("Error al actualizar cliente, contacta al admministrador " + e.toString());
                    } finally {
                        objetoConexion.cerrarConexion();
                    }
                    if (codigo.equals("RENDER")) {
                        try {
                            ListaClientes(tabla, 1, null, "ACTIVO");
                        } catch (Exception e) {
                        }

                    }
                    break;

                default:
                    throw new AssertionError();
            }
            if(lblP!=null){
            lblP.setText(mj.getMensaje());}
            
            return mj;
        }
    }

    public void ListaClientes(JTable tablaTotalProductos, int codigoMuestra, String texto, String activos) {
        Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
        System.out.println(activos);
      //  FormClientes fc = new FormClientes();
        
     //   fc.mostrarAlerta("HOLA MUNDIO", 1);
        
        boolean act = false;
        switch (activos) {
            case "ACTIVOS":
                act = false;
                break;
            case "INACTIVOS":
                act = true;
                break;

            default:
                act = false;
        }

        DefaultTableModel modelo = new DefaultTableModel();
        String sql = "";
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

        switch (codigoMuestra) {
            case 1:
                sql = "select * from cliente where cliente.activo!=" + act + ";";
                break;
            case 2:
                System.out.println("entralcaso2222222222");
                Boolean numeroLetra = false;
                Integer numero;
                try {
                    numero = Integer.parseInt(texto);
                    numeroLetra = true;
                } catch (Exception e) {
                    numeroLetra = false;
                }

                if (numeroLetra == false) {
                    sql = "select * from cliente where cliente.nombre "
                            + "like concat('%','" + texto + "','%') and  cliente.activo!=" + act + ";";

                } else {
                    sql = "select * from cliente  where cliente.codigocliente"
                            + " like concat('%','" + texto + "','%') and  cliente.activo!=" + act + ";";

                }

                break;

            default:
                throw new AssertionError();
        }
        System.out.println(sql);
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        try {
            Statement st = objetoConexion.estableceConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Integer codigo = rs.getInt("codigocliente");
                String nombre = rs.getString("nombre");
                String limite = formatoMoneda.format(rs.getDouble("limite_compra"));
                String saldo = formatoMoneda.format(rs.getDouble("saldo_acumulado"));
                Integer telefono = rs.getInt("telefono");
                Integer cuit = rs.getInt("cuit");
                String direccion = rs.getString("direccion");
                String pago = rs.getString("metodopago");
                String obs = rs.getString("observaciones");
                Integer diaspago = rs.getInt("diaspago");

                modelo.addRow(new Object[]{codigo, nombre, limite, saldo, diaspago, telefono, cuit,
                    direccion, pago, obs});
            }
            
                   for (int i = 0; i <  tablaTotalProductos.getColumnCount(); i++) {
                        Class<?> colClas =  tablaTotalProductos.getColumnClass(i);
                        tablaTotalProductos.setDefaultEditor(colClas, null);
                    }
            tablaTotalProductos.setModel(modelo);

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

            DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
            cent.setHorizontalAlignment(JLabel.CENTER);
            tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(5).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(6).setCellRenderer(cent);

            tablaTotalProductos.getColumnModel().getColumn(2).setCellRenderer(new paddingRig());
            tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(new paddingRig());

        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "17 ERROR: CONTACTE AL ADMINISTARDOR:" + e.toString());
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
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
            return this;
        }
    }

    public Mensaje eliminarClienteSeleccionado(JTable tbResumenVenta) {
        Mensaje mj = null;
        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        try {
            int indiceSelleccionado = tbResumenVenta.getSelectedRow();
            if (indiceSelleccionado != -1) {
                Integer codigoCliente = Integer.parseInt(modelo.getValueAt(indiceSelleccionado, 0).toString());
                Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
                String consulta = "update cliente set activo=FALSE where cliente.codigocliente=?; ";
                
             //   CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
                  PreparedStatement cs=objetoConexion.estableceConexion().prepareStatement(consulta);
                cs.setInt(1, codigoCliente);
                cs.execute();
                ListaClientes(tbResumenVenta, 1, null, "ACTIVOS");
                mj = new Mensaje();
                mj.setCodigo(1);
                mj.setMensaje("Se eliminino con exito el cliente: " + codigoCliente);
            } else {
                mj = new Mensaje();
                mj.setCodigo(2);
                mj.setMensaje("Seleccione una fila para eliminar");
                //  JOptionPane.showMessageDialog(null, "seleccione una fila para eliminar");
            }
        } catch (Exception e) {
            mj = new Mensaje();
            mj.setCodigo(4);
            mj.setMensaje("error a seleccionar " + e.toString());
            //  JOptionPane.showMessageDialog(null, "error a seleccionar " + e.toString());
        }
        return mj;
    }
    
        public Mensaje activarClienteSeleccionado(JTable tbResumenVenta) {
        Mensaje mj = null;
        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        try {
            int indiceSelleccionado = tbResumenVenta.getSelectedRow();
            if (indiceSelleccionado != -1) {
                Integer codigoCliente = Integer.parseInt(modelo.getValueAt(indiceSelleccionado, 0).toString());
                Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
                String consulta = "update cliente set activo=TRUE where cliente.codigocliente=?; ";
              //  CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
                  PreparedStatement cs=objetoConexion.estableceConexion().prepareStatement(consulta);
                cs.setInt(1, codigoCliente);
                cs.execute();
                ListaClientes(tbResumenVenta, 1, null, "INACTIVOS");
                mj = new Mensaje();
                mj.setCodigo(1);
                mj.setMensaje("Se restauró con exito el cliente: " + codigoCliente);
            } else {
                mj = new Mensaje();
                mj.setCodigo(2);
                mj.setMensaje("Seleccione una fila para activar");
                //  JOptionPane.showMessageDialog(null, "seleccione una fila para eliminar");
            }
        } catch (Exception e) {
            mj = new Mensaje();
            mj.setCodigo(4);
            mj.setMensaje("error a seleccionar " + e.toString());
            //  JOptionPane.showMessageDialog(null, "error a seleccionar " + e.toString());
        }
        return mj;
    }

    public Integer editarCliente(JTable tbResumenVenta) {
        DefaultTableModel modelo = (DefaultTableModel) tbResumenVenta.getModel();
        Integer codigoCliente = 0;
        try {
            int indiceSelleccionado = tbResumenVenta.getSelectedRow();
            if (indiceSelleccionado != -1) {
                codigoCliente = Integer.parseInt(modelo.getValueAt(indiceSelleccionado, 0).toString());

            } else {
                codigoCliente = indiceSelleccionado;
                //  JOptionPane.showMessageDialog(null, "seleccione una fila para editar");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error a seleccionar " + e.toString());
        }

        return codigoCliente;

    }

    public ModeloCliente obtenerCliente(Integer numero) {
        ModeloCliente cliente = null;
        if (numero == null) {
            return cliente;
        }
        try {
            Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
            String consulta = "select * from cliente where cliente.codigocliente=? and cliente.activo!=false; ";

            PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(consulta);
            ps.setInt(1, numero);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                cliente = new ModeloCliente();
                cliente.setCodigo(rs.getInt("codigocliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setLimiteCompra(rs.getDouble("limite_compra"));
                cliente.setSaldoAcumulado(rs.getDouble("saldo_acumulado"));
                cliente.setTelefono(rs.getInt("telefono"));
                cliente.setCuitDni(rs.getInt("cuit"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setMetodoPago(rs.getString("metodopago"));
                cliente.setObservaciones(rs.getString("observaciones"));
                cliente.setDiasPago(rs.getInt("diaspago"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error a seleccionar " + e.toString());
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
    
public List<Object[]> obtenerClientesParaSeleccion() {
    List<Object[]> lista = new ArrayList<>();
    // Es mejor nombrar las columnas para no depender del orden de la DB
    String sql = "SELECT * FROM cliente WHERE activo = 1";
    Configuracion.CConexion objetoConexion = new Configuracion.CConexion();

    try (Connection conn = objetoConexion.estableceConexion();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            // Creamos un Object con 4 posiciones:
            // [0] SEL (Boolean)
            // [1] ID (Integer)
            // [2] Nombre (String)
            // [3] DNI (String)
            lista.add(new Object[]{
                false, 
                rs.getInt("codigocliente"), 
                rs.getString("nombre"), 
                rs.getString("cuit")
            });
        }
    } catch (Exception e) { 
        System.out.println("Error en obtenerClientes: " + e.getMessage()); 
    }
    return lista;
}
public void actualizarPreciosPorClienteMasivo(List<Integer> listaClientes, List<Object[]> listaProductos, double porcentaje) {
    // 1. Validaciones previas
    if (listaClientes == null || listaClientes.isEmpty() || listaProductos == null || porcentaje == 0) {
        return; 
    }

    // Calculamos el factor de aumento (ej: 10% -> 1.10)
    double factor = 1 + (porcentaje / 100.0);
    
    Configuracion.CConexion objetoConexion = new Configuracion.CConexion();
    
    // SQL: Multiplicamos el valor actual en la DB por el factor y redondeamos a 2 decimales
    String sql = "UPDATE preciocliente SET " +
                 "costoanterior = ROUND(costoanterior * ?, 2), " +
                 "diferido = ROUND(diferido * ?, 2), " +
                 "fechamodificacion = datetime('now', 'localtime') " +
                 "WHERE fkcliente = ? AND fkproducto = ?";

    try (Connection conn = objetoConexion.estableceConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        conn.setAutoCommit(false); // Iniciar transacción
        int contador = 0;

        for (Integer idCliente : listaClientes) {
            for (Object[] fila : listaProductos) {
                
                // Solo si el producto está marcado con el Checkbox (Índice 0)
                 if (fila != null && (Boolean) fila[0]) {
                    
                    // Extraemos el ID del producto (Índice 1 según tu esquema)
                    int idProducto = Integer.parseInt(fila[1].toString());

                    ps.setDouble(1, factor);    // Multiplicador Costo
                    ps.setDouble(2, factor);    // Multiplicador Precio
                    ps.setInt(3, idCliente);    // ID Cliente
                    ps.setInt(4, idProducto);   // ID Producto

                    ps.addBatch();
                    contador++;
                }
            }
        }

        if (contador > 0) {
            ps.executeBatch();
            conn.commit(); // Guardar cambios en el archivo .db
            System.out.println("Éxito: Se actualizaron " + contador + " precios personalizados.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error en controlador al actualizar precios: " + e.getMessage());
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
