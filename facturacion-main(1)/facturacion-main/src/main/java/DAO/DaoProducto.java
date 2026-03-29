package DAO;

import Controlador.Alerta;
import Modelos.ItemSeleccionable;
import Modelos.Producto;
import configuracion.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author ALCIDES
 */
import java.sql.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.DataFormatter;

public class DaoProducto {
    // Definimos colores para reutilizar
    private final Color COLOR_EXITO = new Color(40, 167, 69);
    private final Color COLOR_ERROR = new Color(220, 53, 69);

   public boolean agregarProducto(Producto pro, int verifAux, JTable tab, JLabel menj, JInternalFrame internal) {
    boolean resultado;
    
    // 1. Ejecutamos la operación y capturamos el éxito o fallo
    if (verifAux == 1) {
        resultado = registrar(pro, internal);
    } else {
        resultado = modificar(pro, internal);
    }

    // 2. Si fue exitosa y hay una tabla, refrescamos la vista
    if (resultado && tab != null) {
        listarEnTabla(tab, internal);
    }
    
    // 3. Actualizamos el mensaje visual si existe el label
    if (menj != null) {
        menj.setText(resultado ? "Operación exitosa" : "Error en base de datos");
    }

    // 4. RETORNAMOS el resultado para que el botón sepa si limpiar o no los campos
    return resultado;
}

private boolean registrar(Producto pro, JInternalFrame internal) {
    // 1. Validar si el código de barras ya existe
    if (existeCodigoBarras(pro.getCodigoBarras())) {
        new Alerta("El código de barras ya está registrado", new Color(231, 76, 60), internal);
        return false;
    }

    String sql = "INSERT INTO productos (codigo_barras, nombre, precio_venta, precio_compra, stock, id_categoria, id_proveedor) VALUES (?,?,?,?,?,?,?)";
    Conexion conexion = new Conexion();
    
    try (Connection con = conexion.estableceConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, pro.getCodigoBarras());
        ps.setString(2, pro.getNombre());
        ps.setBigDecimal(3, pro.getPrecioVenta());
        ps.setBigDecimal(4, pro.getPrecioCompra());
        ps.setBigDecimal(5, pro.getStock());
        ps.setInt(6, pro.getIdCategoria());
        ps.setInt(7, pro.getIdProveedor());

        ps.execute();
        new Alerta("¡Producto Registrado!", new Color(46, 204, 113), internal);
        return true;
    } catch (SQLException e) {
        new Alerta("Error al registrar: " + e.getMessage(), new Color(231, 76, 60), internal);
        return false;
    }
}

// MÉTODO AUXILIAR PARA VALIDAR DUPLICADOS
private boolean existeCodigoBarras(String codigo) {
    String sql = "SELECT COUNT(*) FROM productos WHERE codigo_barras = ?";
  Conexion conexion = new Conexion();
    try (Connection con = conexion.estableceConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, codigo);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    private boolean modificar(Producto pro,JInternalFrame internal) {
        String sql = "UPDATE productos SET codigo_barras=?, nombre=?, precio_venta=?, precio_compra=?, stock=?, id_categoria=?, id_proveedor=? WHERE id_producto=?";
      Conexion conexion = new Conexion();
        try (Connection con = conexion.estableceConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pro.getCodigoBarras());
            ps.setString(2, pro.getNombre());
            ps.setBigDecimal(3, pro.getPrecioVenta());
            ps.setBigDecimal(4, pro.getPrecioCompra());
            ps.setBigDecimal(5, pro.getStock());
            ps.setInt(6, pro.getIdCategoria());
            ps.setInt(7, pro.getIdProveedor());
            ps.setInt(8, pro.getId());

            int res = ps.executeUpdate();
            if (res > 0) {
                new Alerta("¡Producto Actualizado!", COLOR_EXITO,internal);
                return true;
            }
            return false;
        } catch (SQLException e) {
            new Alerta("Error al modificar: " + e.getMessage(), COLOR_ERROR,internal);
            return false;
        }
    }

    public void listarEnTabla(JTable tabla,JInternalFrame internal) {
        String sql = "SELECT * FROM productos ORDER BY id_producto DESC";
      Conexion conexion = new Conexion();
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); // Limpiar tabla antes de cargar

        try (Connection con = conexion.estableceConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            Object[] fila = new Object[8];
            while (rs.next()) {
                fila[0] = rs.getInt("id_producto");
                fila[1] = rs.getString("codigo_barras");
                fila[2] = rs.getString("nombre");
                fila[3] = rs.getBigDecimal("precio_venta");
                fila[4] = rs.getBigDecimal("precio_compra");
                fila[5] = rs.getBigDecimal("stock");
                fila[6] = rs.getInt("id_categoria");
                fila[7] = rs.getInt("id_proveedor");
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            new Alerta("Error al listar: " + e.getMessage(), COLOR_ERROR,internal);
        }
    }
    
    public List<String> obtenerTodosLosRubros() {
    List<String> lista = new ArrayList<>();
    String sql = "SELECT nombre FROM rubro";
     Conexion conexion = new Conexion();
         try (Connection con = conexion.estableceConexion();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            lista.add(rs.getString("nombre"));
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return lista;
}

    
 /**/
    public void cargarComboGenerico(javax.swing.JComboBox<Modelos.ItemSeleccionable> combo, int tipo, javax.swing.JComponent siguiente) {
    String sql = (tipo == 1) ? "SELECT id_rubro, nombre FROM rubro ORDER BY nombre ASC" 
                             : "SELECT id_proveedor, nombre FROM proveedor ORDER BY nombre ASC";
    String colId = (tipo == 1) ? "id_rubro" : "id_proveedor";

    final java.util.List<Modelos.ItemSeleccionable> listaLocal = new java.util.ArrayList<>();
  Conexion conexion = new Conexion();

    try (java.sql.Connection con = conexion.estableceConexion(); 
         java.sql.Statement stmt = con.createStatement(); 
         java.sql.ResultSet rs = stmt.executeQuery(sql)) {

        combo.removeAllItems();
        combo.addItem(new Modelos.ItemSeleccionable(0, "-- Seleccione --"));

        while (rs.next()) {
            Modelos.ItemSeleccionable item = new Modelos.ItemSeleccionable(rs.getInt(colId), rs.getString("nombre"));
            combo.addItem(item);
            listaLocal.add(item); 
        }
        
        combo.setEditable(true);
        javax.swing.JTextField editor = (javax.swing.JTextField) combo.getEditor().getEditorComponent();

        // Limpiar listeners previos
        for (java.awt.event.KeyListener kl : editor.getKeyListeners()) editor.removeKeyListener(kl);
        for (java.awt.event.FocusListener fl : editor.getFocusListeners()) editor.removeFocusListener(fl);

        // EVENTO FOCO: SELECT ALL VISIBLE
        editor.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    editor.selectAll();
                    editor.getCaret().setSelectionVisible(true);
                    editor.getCaret().setVisible(true);
                });
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                editor.getCaret().setVisible(false);
                editor.getCaret().setSelectionVisible(false);
            }
        });

        // EVENTO TECLADO: MANEJO DE ENTER Y FILTRADO
        editor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    evt.consume(); // BLOQUEA EL SALTO DOBLE
                    
                    if (combo.getSelectedIndex() != -1) {
                        Modelos.ItemSeleccionable seleccionado = (Modelos.ItemSeleccionable) combo.getSelectedItem();
                        combo.getEditor().setItem(seleccionado);
                        combo.setSelectedItem(seleccionado);
                    }
                    combo.hidePopup();
                    
                    // Realizar el salto de foco
                    if (siguiente != null) {
                        if (siguiente instanceof javax.swing.JComboBox) {
                            javax.swing.JTextField nextEditor = (javax.swing.JTextField) ((javax.swing.JComboBox) siguiente).getEditor().getEditorComponent();
                            nextEditor.requestFocusInWindow();
                            ((javax.swing.JComboBox) siguiente).setPopupVisible(true);
                        } else {
                            siguiente.requestFocusInWindow();
                        }
                    }
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                // El filtrado solo ocurre si NO es la tecla Enter
                if (evt.getKeyCode() != java.awt.event.KeyEvent.VK_ENTER) {
                    ejecutarFiltro(combo, editor, evt, listaLocal);
                }
            }
        });

    } catch (java.sql.SQLException e) {
        e.printStackTrace();
    }
}

private void ejecutarFiltro(javax.swing.JComboBox<Modelos.ItemSeleccionable> combo, javax.swing.JTextField editor, java.awt.event.KeyEvent evt, java.util.List<Modelos.ItemSeleccionable> listaOriginal) {
    String busqueda = editor.getText();
    
    // Ignorar flechas
    if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_UP || evt.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN || 
        evt.getKeyCode() == java.awt.event.KeyEvent.VK_LEFT || evt.getKeyCode() == java.awt.event.KeyEvent.VK_RIGHT) return;

    javax.swing.DefaultComboBoxModel<Modelos.ItemSeleccionable> nuevoModelo = new javax.swing.DefaultComboBoxModel<>();
    
    if (busqueda.isEmpty()) {
        nuevoModelo.addElement(new Modelos.ItemSeleccionable(0, "-- Seleccione --"));
        for (Modelos.ItemSeleccionable item : listaOriginal) nuevoModelo.addElement(item);
    } else {
        for (Modelos.ItemSeleccionable item : listaOriginal) {
            if (item.getNombre().toLowerCase().contains(busqueda.toLowerCase())) {
                nuevoModelo.addElement(item);
            }
        }
    }

    javax.swing.SwingUtilities.invokeLater(() -> {
        combo.setModel(nuevoModelo);
        editor.setText(busqueda);
        if (editor.isFocusOwner()) {
            editor.getCaret().setVisible(true);
            editor.setCaretPosition(busqueda.length());
        }
        if (nuevoModelo.getSize() > 0 && !busqueda.isEmpty()) {
            combo.setPopupVisible(false);
            combo.setPopupVisible(true);
        } else {
            if (busqueda.isEmpty()) combo.hidePopup();
        }
    });
}

    
    
    public boolean insertar(String tabla, int id, String nombre) throws SQLException {
        // Usamos el nombre de la tabla de forma dinámica
        String sql = "INSERT INTO " + tabla + " (id_" + tabla + ", nombre) VALUES (?, ?)";
     Conexion conexion = new Conexion();
         try (Connection con = conexion.estableceConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.setString(2, nombre.trim());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
public boolean existeID(String tabla, int id) {
    String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE id_" + tabla + " = ?";
     Conexion conexion = new Conexion();
         try (Connection con = conexion.estableceConexion();
         PreparedStatement pstmt = con.prepareStatement(sql)) {
        
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1) > 0; // Si es mayor a 0, el ID ya existe
        }
    } catch (SQLException e) {
        System.out.println("Error al verificar ID: " + e.getMessage());
    }
    return false;
}
public int obtenerSiguienteIDDisponible(String tabla) {
    String sql = "SELECT MAX(id_" + tabla + ") FROM " + tabla;
       Conexion conexion = new Conexion();
         try (Connection con = conexion.estableceConexion();
         PreparedStatement pstmt = con.prepareStatement(sql)) {
        
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            int maxId = rs.getInt(1);
            return maxId + 1; // Retorna el siguiente
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener ID sugerido: " + e.getMessage());
    }
    return 1; // Si la tabla está vacía, empieza en 1
}

public Integer verCodigoDisponible(JTextField tex) {
 Conexion objetoConexion = new Conexion();
    
    // Esta consulta verifica si el 1 existe; si no, lo devuelve. 
    // Si el 1 existe, busca el primer número faltante en la secuencia.
    String sql = "SELECT CASE " +
                 "  WHEN (SELECT COUNT(*) FROM productos WHERE CAST(codigo_barras AS INTEGER) = 1) = 0 THEN 1 " +
                 "  ELSE (SELECT CAST(t1.codigo_barras AS INTEGER) + 1 " +
                 "        FROM productos AS t1 " +
                 "        LEFT JOIN productos AS t2 ON CAST(t1.codigo_barras AS INTEGER) + 1 = CAST(t2.codigo_barras AS INTEGER) " +
                 "        WHERE t2.codigo_barras IS NULL " +
                 "        ORDER BY CAST(t1.codigo_barras AS INTEGER) ASC LIMIT 1) " +
                 "END AS disponible";

    Integer disponible = 1;

    try (Connection con = objetoConexion.estableceConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            disponible = rs.getInt("disponible");
            if (disponible <= 0) disponible = 1;
            
            if (tex != null) {
                tex.setText(String.valueOf(disponible));
            }
        }
    } catch (Exception e) {
        new Alerta("Error al buscar Código disponible", new java.awt.Color(231, 76, 60), null);
        e.printStackTrace();
    }
    
    return disponible;
}

public void seleccionarItemPorId(JComboBox<ItemSeleccionable> combo, int idBuscado) {
    for (int i = 0; i < combo.getItemCount(); i++) {
        ItemSeleccionable item = combo.getItemAt(i);
        if (item.getId() == idBuscado) {
            combo.setSelectedIndex(i);
            break;
        }
    }
}
public Producto obtenerproducto(Integer codigo) {
    // 1. SQL para buscar por ID de producto (o codigo_barras si preferís)
    String sql = "SELECT id_producto, codigo_barras, nombre, precio_venta, precio_compra, stock, id_categoria, id_proveedor " +
                 "FROM productos WHERE id_producto = ?";
    
    Producto pro = new Producto();
  Conexion conexion = new Conexion();

    try (Connection con = conexion.estableceConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, codigo);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            // 2. Mapeo de la base de datos al objeto Producto
            pro.setId(rs.getInt("id_producto"));
            pro.setCodigoBarras(rs.getString("codigo_barras"));
            pro.setNombre(rs.getString("nombre"));
            pro.setPrecioVenta(rs.getBigDecimal("precio_venta"));
            pro.setPrecioCompra(rs.getBigDecimal("precio_compra"));
            pro.setStock(rs.getBigDecimal("stock"));
            
            // Estos IDs son la clave para que tus ComboBox se seleccionen solos
            pro.setIdCategoria(rs.getInt("id_categoria"));
            pro.setIdProveedor(rs.getInt("id_proveedor"));
        }

    } catch (SQLException e) {
        System.out.println("Error al obtener producto: " + e.getMessage());
    }
    
    return pro;
}




public void importarConReporte(Component parent) {
    JFileChooser fc = new JFileChooser();
    fc.setDialogTitle("Seleccionar Excel de Productos");
    if (fc.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) return;

    StringBuilder logErrores = new StringBuilder();
    int exitos = 0;
    int errores = 0;

   Conexion conexion = new Conexion();
    
    // Abrimos todo en un solo bloque try-with-resources
    try (Connection con = conexion.estableceConexion();
         FileInputStream fis = new FileInputStream(fc.getSelectedFile());
         Workbook workbook = WorkbookFactory.create(fis)) {
        
        // --- MODO VELOCIDAD ACTIVADO ---
        con.setAutoCommit(false); 

        Sheet hoja = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i <= hoja.getLastRowNum(); i++) {
            Row fila = hoja.getRow(i);
            if (fila == null) continue;

            try {
                Modelos.Producto pro = new Modelos.Producto();
                
                String codigo = getCellValue(fila.getCell(0)).trim();
                String nombre = getCellValue(fila.getCell(1)).trim();
                
                if (codigo.isEmpty() || nombre.isEmpty()) throw new Exception("Código o Nombre vacíos");

                pro.setCodigoBarras(codigo);
                pro.setNombre(nombre);

                // Manejo de comas y celdas vacías en números
                String pVenta = getCellValue(fila.getCell(2)).replace(",", ".");
                String pCompra = getCellValue(fila.getCell(3)).replace(",", ".");
                String stockVal = getCellValue(fila.getCell(4)).replace(",", ".");

                pro.setPrecioVenta(new BigDecimal(pVenta.isEmpty() ? "0" : pVenta));
                pro.setPrecioCompra(new BigDecimal(pCompra.isEmpty() ? "0" : pCompra));
                pro.setStock(new BigDecimal(stockVal.isEmpty() ? "0" : stockVal));

                // Obtener IDs de Rubro y Proveedor (los crea si no existen)
                pro.setIdCategoria(obtenerIdOCrearConConexion(con, "rubro", getCellValue(fila.getCell(5))));
                pro.setIdProveedor(obtenerIdOCrearConConexion(con, "proveedor", getCellValue(fila.getCell(6))));

                // Guardar o Actualizar
                if (registrarOActualizarConConexion(con, pro)) {
                    exitos++;
                }

            } catch (Exception ex) {
                errores++;
                logErrores.append("Fila ").append(i + 1).append(": ").append(ex.getMessage()).append("\n");
            }
        }

        // --- COMMIT: Guardamos todo de un solo golpe ---
        con.commit(); 
        
        String resumen = "Importación finalizada.\nÉxitos: " + exitos + "\nErrores: " + errores;
        Color colorFinal = (errores == 0) ? new Color(46, 204, 113) : new Color(255, 153, 51);
        
        new Alerta(resumen, colorFinal, (Container) parent);
        if (errores > 0) mostrarReporteErrores(logErrores.toString(), parent);

    } catch (Exception e) {
        new Alerta("Error crítico al procesar Excel", new Color(231, 76, 60), (Container) parent);
        e.printStackTrace();
    }
}
private boolean registrarOActualizarConConexion(Connection con, Modelos.Producto pro) throws SQLException {
    // SQLITE: Si el codigo_barras ya existe, actualiza los campos. Si no, inserta nuevo.
    String sql = "INSERT INTO productos (codigo_barras, nombre, precio_venta, precio_compra, stock, id_categoria, id_proveedor) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                 "ON CONFLICT(codigo_barras) DO UPDATE SET " +
                 "nombre=excluded.nombre, precio_venta=excluded.precio_venta, precio_compra=excluded.precio_compra, " +
                 "stock=excluded.stock, id_categoria=excluded.id_categoria, id_proveedor=excluded.id_proveedor";
               
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, pro.getCodigoBarras());
        ps.setString(2, pro.getNombre());
        ps.setBigDecimal(3, pro.getPrecioVenta());
        ps.setBigDecimal(4, pro.getPrecioCompra());
        ps.setBigDecimal(5, pro.getStock());
        ps.setInt(6, pro.getIdCategoria());
        ps.setInt(7, pro.getIdProveedor());
        return ps.executeUpdate() > 0;
    }
}

private int obtenerIdOCrearConConexion(Connection con, String tabla, String nombre) throws SQLException {
    if (nombre == null || nombre.trim().isEmpty()) return 1; 
    String nom = nombre.trim();
    String colId = "id_" + tabla;

    // Buscar si existe
    String sqlBusq = "SELECT " + colId + " FROM " + tabla + " WHERE nombre = ?";
    try (PreparedStatement ps = con.prepareStatement(sqlBusq)) {
        ps.setString(1, nom);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
    }

    // Crear si no existe
    String sqlIns = "INSERT INTO " + tabla + " (nombre) VALUES (?)";
    try (PreparedStatement ps = con.prepareStatement(sqlIns, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, nom);
        ps.executeUpdate();
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) return rs.getInt(1);
        }
    }
    return 1;
}

private String getCellValue(Cell cell) {
    if (cell == null) return "";
    DataFormatter formatter = new DataFormatter();
    return formatter.formatCellValue(cell).trim();
}

private void mostrarReporteErrores(String reporte, Component parent) {
    JTextArea textArea = new JTextArea(reporte);
    textArea.setEditable(false);
    JScrollPane scroll = new JScrollPane(textArea);
    scroll.setPreferredSize(new Dimension(450, 250));
    JOptionPane.showMessageDialog(parent, scroll, "Detalle de Errores", JOptionPane.WARNING_MESSAGE);
}
}
