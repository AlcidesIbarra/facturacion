/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;
import Controlador.ControladorProducto;
import Controlador.ControladorVenta;
import Modelos.Mensaje;
import Modelos.ModeloProducto;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author ALCIDES
 */
public class FormActualizacionMasiva extends javax.swing.JInternalFrame {

    List<Object[]> datos = new ArrayList<>(); // Lista Maestra (La de memoria)
    private boolean isCargando = false;
List<Integer> clientesSeleccionados = new ArrayList<>(); 
    public FormActualizacionMasiva() {
        initComponents();
        
        
    ControladorAdministracion ca = new ControladorAdministracion();

        ca.cambiacolor(this);
        // ca.BotonesCrisitalPrim(btnGuardarCliente, Color.green, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton1, Color.blue, Color.black, Color.black);
          ca.BotonesCrisitalPrim(jButton2, Color.cyan, Color.black, Color.black);
                   ca.BotonesCrisitalPrim(jButton3, Color.red, Color.black, Color.black);
                          ca.BotonesCrisitalPrim(jButton4, Color.green, Color.black, Color.black);
            ca.BotonesCrisitalPrim(selecionarTodo, Color.yellow, Color.black, Color.black);
          
          
        Controlador.ControladorProducto cp = new Controlador.ControladorProducto();
        this.datos = cp.obtenerProductosParaTabla();

        // 2. EL MODELO: Lo definimos con tus reglas
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"SEL", "Cod", "Descripcion", "Costo Anterior", "Costo Nuevo", "Precio Nuevo", "Proveedor", "Rubro"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // ESTA ES LA CLAVE: Le dice a Java que la columna 0 es un Checkbox [Oracle Docs](https://docs.oracle.com)
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Columna 0 (SEL) y 4 (Costo Nuevo) son editables
                return column == 0 || column == 4 || column == 5;
            }
        };
        // 3. LA TABLA: Asignamos el modelo y cargamos la primera vista (Completa)
        tablaTotalProductos.setModel(modelo);
        
            tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(40);
            tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(50);
          tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(170);
            tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(124);
            tablaTotalProductos.getColumnModel().getColumn(4).setPreferredWidth(124);
            tablaTotalProductos.getColumnModel().getColumn(5).setPreferredWidth(124);
            tablaTotalProductos.getColumnModel().getColumn(6).setPreferredWidth(163);
            tablaTotalProductos.getColumnModel().getColumn(7).setPreferredWidth(162);

           DefaultTableCellRenderer cent = new DefaultTableCellRenderer();
            cent.setHorizontalAlignment(JLabel.CENTER);
            tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(1).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(3).setCellRenderer(cent);
            tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(cent);
        
        
        
            Dimension d = new Dimension(80, 26); // El tamaño que quieras
      jButton1.setPreferredSize(d);
         jButton1.setMinimumSize(d);
         jButton1.setMaximumSize(d);
         
           jButton2.setPreferredSize(d);
         jButton2.setMinimumSize(d);
         jButton2.setMaximumSize(d);
         
           jButton3.setPreferredSize(new Dimension(100, 26)); 
         jButton3.setMinimumSize(new Dimension(100, 26)); 
         jButton3.setMaximumSize(new Dimension(100, 26)); 
         
         
         jButton4.setPreferredSize(d);
         jButton4.setMinimumSize(d);
         jButton4.setMaximumSize(d);
         
          selecionarTodo  .setPreferredSize(new Dimension(140, 26));       
       selecionarTodo  .setMaximumSize(new Dimension(140, 26));       
                selecionarTodo  .setMinimumSize(new Dimension(140, 26));       
        
        
        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }
      
        Color olor = ca.retornatexField();

        Color miVerde = new Color(200, 255, 200);
        aplicarResaltadoFilas(miVerde);     // Pinta de la columna 1 a la 7
        colorearColumnaCheckbox(miVerde);
        aplicarColorEdicion(olor);
        // 4. LOS EVENTOS: Ahora que la tabla tiene modelo, activamos el listener del Checkbox
        configurarEventoCheckbox();

        // 5. LOS COMBOS: Los llenamos (esto disparará actualizarTablaFiltrada, por eso el orden importa)
        String[] proveedores = obtenerFiltrosUnicos(datos, 6);
        comboProveedor.removeAllItems();
        comboProveedor.addItem("Todos Proveedores");
        for (String p : proveedores) {
            comboProveedor.addItem(p);
        }

        String[] rubros = obtenerFiltrosUnicos(datos, 7);
        comboRubro.removeAllItems();
        comboRubro.addItem("Todos Rubros");
        for (String r : rubros) {
            comboRubro.addItem(r);
        }

        // 6. INICIO SEGURO: Forzamos la primera actualización visual
        actualizarTablaFiltrada();
    }
     private Timer alertaTimer = null;
    private AWTEventListener tecladoGlobalListener = null;
    
    public void mostrarAlerta(String palabra, Integer codigo) {
        final JPanel glass = (JPanel) this.getGlassPane();

        // 1. Limpieza de procesos anteriores
        if (alertaTimer != null && alertaTimer.isRunning()) {
            alertaTimer.stop();
        }
        if (tecladoGlobalListener != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(tecladoGlobalListener);
        }
        
        glass.removeAll();
        glass.setLayout(new GridBagLayout()); // Usamos GridBag para posicionar arriba

        // 2. Configuración de Posición (Arriba)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH; // Lo pega al techo
        gbc.insets = new Insets(20, 0, 0, 0);  // Margen de 20px desde arriba
        gbc.weighty = 1.0; // Empuja el resto del espacio hacia abajo

        // 3. Crear el Popup
        JPanel popup = new JPanel();
        popup.setLayout(new BoxLayout(popup, BoxLayout.Y_AXIS));
        // Borde redondeado original de tu código
        popup.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 8, true));

        // 4. Cargar Icono (Ruta relativa corregida)
        // Cambiá "advertencia.png" por el nombre exacto de tu archivo en src
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/main/Imagenes/advertencia.png"));
        Image imgEscalada = iconoOriginal.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
        ImageIcon icono = new ImageIcon(imgEscalada);
        
        String txtTitulo = "";
        Color colorFondo;
        switch (codigo) {
            case 1:
                txtTitulo = "EXITO:";
                colorFondo = new Color(154, 205, 50, 220);
                break;
            case 2:
                txtTitulo = "ATENCION:";
                colorFondo = new Color(255, 120, 0, 220);
                break;
            case 3:
                txtTitulo = "ADVERTENCIA:";
                colorFondo = new Color(255, 0, 0, 220);
                break;
            case 4:
                txtTitulo = "ERROR:";
                colorFondo = new Color(150, 150, 150, 220);
                break;
            default:
                txtTitulo = "AVISO:";
                colorFondo = new Color(100, 100, 100, 220);
        }
        popup.setBackground(colorFondo);

        // 5. Etiquetas con tu fuente original "consolass"
        JLabel titulo = new JLabel(txtTitulo, icono, JLabel.LEFT);
        titulo.setFont(new Font("consolass", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
   JLabel contenido = new JLabel("<html><center>"+palabra+"</center></html>");
    contenido.setFont(new Font("consolass", Font.BOLD, 16));
    popup.setMinimumSize(new Dimension(320,80));
        contenido.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        popup.add(titulo);
        popup.add(contenido);
        
        glass.add(popup, gbc); // Agregamos con la configuración de posición
        glass.setVisible(true);
        glass.revalidate();
        glass.repaint();

        // 6. Lógica de Cierre (Click, Tecla o Tiempo)
        Runnable cerrarAccion = () -> {
            glass.setVisible(false);
            glass.removeAll();
            if (alertaTimer != null) {
                alertaTimer.stop();
            }
            if (tecladoGlobalListener != null) {
                Toolkit.getDefaultToolkit().removeAWTEventListener(tecladoGlobalListener);
            }
        };
        
        glass.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                cerrarAccion.run();
            }
        });
        
        tecladoGlobalListener = event -> {
            if (event instanceof KeyEvent && event.getID() == KeyEvent.KEY_PRESSED) {
                cerrarAccion.run();
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(tecladoGlobalListener, AWTEvent.KEY_EVENT_MASK);
        
        alertaTimer = new Timer(5000, e -> cerrarAccion.run());
        alertaTimer.setRepeats(false);
        alertaTimer.start();
    }
public void configurarEventoCheckbox() {
    tablaTotalProductos.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

    tablaTotalProductos.getModel().addTableModelListener(e -> {
        // 1. Evitamos disparos accidentales durante la carga de filtros
        if (isCargando) return;

        int filaVisual = e.getFirstRow();
        int columna = e.getColumn();
        
        // Validamos que la fila sea correcta
        if (filaVisual == -1) return;

        DefaultTableModel modelo = (DefaultTableModel) tablaTotalProductos.getModel();
        Object idTabla = modelo.getValueAt(filaVisual, 1); // Código del producto (Columna 1)

        // 2. Buscamos el producto en la LISTA MAESTRA 'datos'
        for (Object[] filaOriginal : datos) {
            if (filaOriginal[1].equals(idTabla)) {

                // --- CASO A: CAMBIÓ EL CHECKBOX (Columna 0) ---
                if (columna == 0) { 
                    filaOriginal[0] = (Boolean) modelo.getValueAt(filaVisual, 0);
                    
                    // IMPORTANTE: Si el usuario marca el check y ya hay un número escrito,
                    // recalculamos los precios para esa fila inmediatamente.
                    aplicarAumentoDesdeTxt(jTextField1); 
                    
                    // Forzamos a la tabla a repintarse para que se vea el color verde/celeste
                    tablaTotalProductos.repaint();
                } 
                
                // --- CASO B: CAMBIÓ EL COSTO NUEVO MANUALMENTE (Columna 4) ---
                else if (columna == 4) { 
                    try {
                        String valorCelda = modelo.getValueAt(filaVisual, 4).toString();
                        filaOriginal[4] = Double.parseDouble(valorCelda);
                    } catch (Exception ex) {
                        // Si el dato es inválido, vuelve al Costo Anterior (Columna 3)
                        filaOriginal[4] = filaOriginal[3]; 
                    }
                }
                break; // Salimos del for una vez encontrado el ID
            }
        }
    });
}


    public String[] obtenerFiltrosUnicos(List<Object[]> listaProductos, int indiceColumna) {
        // Usamos HashSet para que no se repitan los nombres automáticamente
        Set<String> setUnicos = new HashSet<>();

        for (Object[] fila : listaProductos) {
            // Validamos que el índice exista en la fila y no sea nulo
            if (fila.length > indiceColumna && fila[indiceColumna] != null) {
                String valor = fila[indiceColumna].toString().trim();
                if (!valor.isEmpty()) {
                    setUnicos.add(valor);
                }
            }
        }

        // Convertimos el Set a Array y lo ordenamos alfabéticamente (A-Z)
        String[] resultado = setUnicos.toArray(new String[0]);
        Arrays.sort(resultado);

        return resultado;
    }

    public void aplicarColorEdicion(Color colorFondo) {
        DefaultTableCellRenderer renderizador = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                } else {
                    c.setBackground(colorFondo);
                }

                // Alineación para números
                setHorizontalAlignment(JLabel.RIGHT);
                return c;
            }
        };

        // ¡ESTA LÍNEA ES LA CLAVE! 
        // Debe ser el índice 4. Si ponés 0, borrás el Checkbox.
        tablaTotalProductos.getColumnModel().getColumn(4).setCellRenderer(renderizador);
                tablaTotalProductos.getColumnModel().getColumn(5).setCellRenderer(renderizador);
    }

    public List<Object[]> obtenerListaAuxiliar(String provSel, String rubSel) {
        List<Object[]> auxiliar = new ArrayList<>();

        for (Object[] fila : datos) {
            String p = fila[6].toString();
            String r = fila[7].toString();

            boolean coincideP = provSel.equals("Todos los Proveedores") || p.equals(provSel);
            boolean coincideR = rubSel.equals("Todos los Rubros") || r.equals(rubSel);

            if (coincideP && coincideR) {
                auxiliar.add(fila); // Agregamos la referencia del objeto original
            }
        }
        return auxiliar;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTotalProductos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtBuscarProducto = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        comboProveedor = new javax.swing.JComboBox<>();
        comboRubro = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        selecionarTodo = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);

        tablaTotalProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SEL", "Codigo", "Descripcion", "Costo Anterior", "Costo Nuevo", "Precio Nuevo", "Proveedor", "Rubro"
            }
        ));
        jScrollPane1.setViewportView(tablaTotalProductos);
        if (tablaTotalProductos.getColumnModel().getColumnCount() > 0) {
            tablaTotalProductos.getColumnModel().getColumn(0).setMinWidth(50);
            tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tablaTotalProductos.getColumnModel().getColumn(0).setMaxWidth(50);
            tablaTotalProductos.getColumnModel().getColumn(1).setMinWidth(60);
            tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(60);
            tablaTotalProductos.getColumnModel().getColumn(1).setMaxWidth(60);
        }

        jLabel1.setText("Buscar:");

        txtBuscarProducto.setAutoscrolls(false);
        txtBuscarProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarProductoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarProductoFocusLost(evt);
            }
        });
        txtBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarProductoActionPerformed(evt);
            }
        });
        txtBuscarProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarProductoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarProductoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarProductoKeyTyped(evt);
            }
        });

        jButton1.setText("Buscar");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setText("ACTUALIZACION");

        jLabel2.setText("% Aumento:");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        comboProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboProveedorActionPerformed(evt);
            }
        });

        comboRubro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboRubroActionPerformed(evt);
            }
        });

        jButton2.setText("Clientes");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        selecionarTodo.setText("Seleccionar todo");
        selecionarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selecionarTodoActionPerformed(evt);
            }
        });

        jButton3.setText("Limpiar Filtro");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Guardar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(4, 4, 4)
                        .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(selecionarTodo)
                        .addGap(40, 40, 40)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 630, Short.MAX_VALUE)
                        .addComponent(jButton4)
                        .addGap(16, 16, 16))))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboRubro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2)
                        .addComponent(jButton1)
                        .addComponent(jLabel3)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3)
                        .addComponent(jButton4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(selecionarTodo)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarProductoFocusGained
        // fucuseadoprod = true;
        txtBuscarProducto.selectAll();

    }//GEN-LAST:event_txtBuscarProductoFocusGained
    boolean abreSlide = false;
    private void txtBuscarProductoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarProductoFocusLost

    }//GEN-LAST:event_txtBuscarProductoFocusLost

    private void txtBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProductoActionPerformed

    }//GEN-LAST:event_txtBuscarProductoActionPerformed

    private void txtBuscarProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyPressed

    }//GEN-LAST:event_txtBuscarProductoKeyPressed

    private void txtBuscarProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyReleased
        actualizarTablaFiltrada();
    }//GEN-LAST:event_txtBuscarProductoKeyReleased

    private void txtBuscarProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyTyped

    }//GEN-LAST:event_txtBuscarProductoKeyTyped

    private void comboProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboProveedorActionPerformed
        String seleccionado = comboProveedor.getSelectedItem().toString();

        // 2. Ejecutás tu lógica de filtrado sobre el Array
        actualizarTablaFiltrada();
    }//GEN-LAST:event_comboProveedorActionPerformed

    private void comboRubroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboRubroActionPerformed
        String seleccionado = comboRubro.getSelectedItem().toString();

        // 2. Ejecutás tu lógica de filtrado sobre el Array
        actualizarTablaFiltrada();
    }//GEN-LAST:event_comboRubroActionPerformed

    private void selecionarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selecionarTodoActionPerformed
       ControladorAdministracion ca = new ControladorAdministracion();
           // Comprobamos si el botón dice "Seleccionar todo"
    if (selecionarTodo.getText().equalsIgnoreCase("Seleccionar todo")) {
         marcarTodoLoVisible(true);
       selecionarTodo.setText("Deseleccionar todo");
        ca.BotonesCrisitalPrim(selecionarTodo, Color.orange, Color.black, Color.black);
    } else {
     marcarTodoLoVisible(false);
       selecionarTodo.setText("Seleccionar todo");
        ca.BotonesCrisitalPrim(selecionarTodo, Color.yellow, Color.black, Color.black);
          
    }
     
    }//GEN-LAST:event_selecionarTodoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        comboProveedor.setSelectedIndex(0);
        comboRubro.setSelectedIndex(0);

        // 2. Limpiamos el buscador (esto disparará el DocumentListener automáticamente)
        txtBuscarProducto.setText("");

        // 3. Mostramos la lista completa original
        cargarDatosEnTabla(datos);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
  aplicarAumentoDesdeTxt(jTextField1); 

    String cadena = jTextField1.getText().trim();

    if (cadena.isEmpty()) {
        restablecerValoresOriginales();
        return; // IMPORTANTE: Cortamos la ejecución aquí
    }

    // --- CASO 2: SE PRESIONA ENTER ---
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        try {
            double porc = Double.parseDouble(cadena.replace(",", "."));
            aplicarAumentoPorcentual(porc);
        } catch (Exception e) { }
        return;
    }

    // --- CASO 3: VALIDACIÓN DE NÚMEROS MIENTRAS ESCRIBE ---
    char c = evt.getKeyChar();
    // Permitimos borrar y teclas de control, pero validamos el resto
    if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_DELETE) {
        
        if (Character.isDigit(c) || c == '.' || c == ',') {
            try {
                double porc = Double.parseDouble(cadena.replace(",", "."));
                aplicarAumentoPorcentual(porc);
            } catch (NumberFormatException e) {
                // Si el formato es inválido (ej: dos puntos), no hacemos nada
            }
        } else if (Character.isAlphabetic(c)) { // Si es una letra, la borramos
            mostrarAlerta("Solo se aceptan números para este campo", 2);
            cadena = cadena.substring(0, cadena.length() - 1);
            jTextField1.setText(cadena);
        }
    }
    }//GEN-LAST:event_jTextField1KeyReleased

private void restablecerValoresOriginales() {
    if (datos == null || datos.isEmpty()) return;

    for (Object[] fila : datos) {
        // Solo para los que están marcados (o todos, según prefieras)
        if (fila != null && (Boolean) fila[0]) {
            // 1. Costo Nuevo (4) vuelve a ser Costo Anterior (3)
            fila[4] = fila[3]; 

            // 2. Precio Nuevo (5) vuelve a ser Precio Original Oculto (8)
            // IMPORTANTE: Aquí usamos el índice 8 que creamos en el controlador
            fila[5] = fila[8]; 
        }
    }
    // Refrescamos la JTable para que pida los datos nuevos a la lista 'datos'
    actualizarTablaFiltrada();
    tablaTotalProductos.repaint(); 
}
    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed


    }//GEN-LAST:event_jTextField1KeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       FormSeleccionarCliente fsc = new  FormSeleccionarCliente (this);
    
    // 1. Obtenemos el contenedor (DesktopPane)
   javax.swing.JDesktopPane desktop = this.getDesktopPane();
    desktop.add(fsc); 

    // 1. Calculamos el centro horizontal (X)
    int x = (desktop.getWidth() / 2) - (fsc.getWidth() / 2);
    
    // 2. Forzamos la posición arriba (Y = 0)
    int y = 0; 

    // 3. Ubicamos, mostramos y traemos al frente
    fsc.setLocation(x, y); 
    fsc.setVisible(true);
    try {
        fsc.setSelected(true); // Le da el foco a la ventana nueva
    } catch (java.beans.PropertyVetoException e) {
        System.out.println("Error al enfocar: " + e.getMessage());
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
      Mensaje mj = null;
        try {
        // 1. Validamos el porcentaje primero
        String pTexto = jTextField1.getText().trim().replace(",", ".");
        if (pTexto.isEmpty()) {
                 mostrarAlerta("Debe ingresar un porcentaje de aumento.", 2);
         //   JOptionPane.showMessageDialog(this, "Debe ingresar un porcentaje de aumento.");
            return;
        }
        double porc = Double.parseDouble(pTexto);

        // 2. Instanciamos los controladores
        Controlador.ControladorCliente cc = new Controlador.ControladorCliente();
        Controlador.ControladorProducto cp = new Controlador.ControladorProducto();

        // 3. LÓGICA DE VALIDACIÓN DE CLIENTES
        if (clientesSeleccionados == null || clientesSeleccionados.isEmpty()) {
            // Si no hay clientes, preguntamos qué hacer
            int respuesta = JOptionPane.showConfirmDialog(this, 
                "No ha seleccionado clientes específicos.\n¿Desea actualizar solo los precios generales de productos?", 
                "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (respuesta != JOptionPane.YES_OPTION) return; // Cancela la operación
            
            // Si dijo que SÍ, solo actualizamos productos
         mj=   cp.guardarCambiosMasivos(datos);
            
        } else {
            // Si hay clientes seleccionados, actualizamos AMBAS tablas
            int rtaCompleta = JOptionPane.showConfirmDialog(this, 
                "Se actualizarán los precios generales y los precios de " + clientesSeleccionados.size() + " clientes.\n¿Continuar?", 
                "Confirmar actualización dual", JOptionPane.YES_NO_OPTION);
            
            if (rtaCompleta != JOptionPane.YES_OPTION) return;

          mj=  cp.guardarCambiosMasivos(datos);
            cc.actualizarPreciosPorClienteMasivo(clientesSeleccionados, datos, porc);
        }

        // 4. Limpieza final de la interfaz
        logicParaRecargarTodo();
        if (clientesSeleccionados != null) clientesSeleccionados.clear();
       // mostrarAlerta("Proceso finalizado con éxito.", 1);
     //   JOptionPane.showMessageDialog(this, "Proceso finalizado con éxito.");

    } catch (NumberFormatException e) {
          mostrarAlerta("Ingrese un porcentaje numérico válido.", 2);
      //  JOptionPane.showMessageDialog(this, "Ingrese un porcentaje numérico válido.");
    }
       if (mj!=null){
        mostrarAlerta(mj.getMensaje(), mj.getCodigo());
       }
    }//GEN-LAST:event_jButton4ActionPerformed
  
  
    
    public void aplicarAumentoDesdeTxt(javax.swing.JTextField txtPorc) {
    if (datos == null || datos.isEmpty()) return;

    try {
        String texto = txtPorc.getText().trim().replace(",", ".");
        
        // SI ESTÁ VACÍO: Restablecemos todo al original
        if (texto.isEmpty()) {
            restablecerValoresOriginales();
            return;
        }

        double porcentaje = Double.parseDouble(texto);
        double factor = 1 + (porcentaje / 100.0);

        for (Object[] fila : datos) {
            // SOLO SI EL CHECKBOX (pos 0) ESTÁ MARCADO
        if (fila[0] != null && (Boolean) fila[0]) { 
                
                // USAMOS EL RESPALDO (Columna 3: Costo Anterior)
                double costoOriginal = Double.parseDouble(fila[3].toString());
                double costoCalculado = (costoOriginal * factor);
                fila[4] = Math.round(costoCalculado * 100.0) / 100.0; // Guardamos en Costo Nuevo

                // USAMOS EL RESPALDO (Columna 8: Precio Original Oculto)
                double precioOriginal = Double.parseDouble(fila[8].toString());
                double precioCalculado = (precioOriginal * factor);
                fila[5] = Math.round(precioCalculado * 100.0) / 100.0; // Guardamos en Precio Nuevo
            }
        }

        // REFRESCAMOS LA VISTA
        actualizarTablaFiltrada();
        tablaTotalProductos.repaint();

    } catch (NumberFormatException e) {
        // Ignoramos si el usuario está escribiendo un punto o está vacío
    }
}
    
public void recibirClientesSeleccionados(List<Integer> listaIDs) {
    if (datos == null || listaIDs.isEmpty()) return;

    List<Object[]> auxiliar = new ArrayList<>();

    for (Object[] filaProducto : datos) {
        try {
            // USAMOS Double.valueOf(...).intValue() para evitar el ClassCastException
            // Y revisamos el índice. Si el ID cliente no está en la 8, 
            // probá con el índice donde lo hayas guardado.
            
            String valorCelda = filaProducto[1].toString(); // Probamos con el índice 1 si es el código
            int idFila = Integer.parseInt(valorCelda); 

            if (listaIDs.contains(idFila)) {
                auxiliar.add(filaProducto);
            }
               this.clientesSeleccionados = listaIDs; 
               
        } catch (Exception e) {
            // Si la columna no es un número, saltamos la fila sin que explote
            System.out.println("Error de casteo en fila: " + e.getMessage());
        }
    }

 
}
    
    
  private void actualizarTablaFiltrada() {
    if (datos == null || comboProveedor.getSelectedItem() == null || comboRubro.getSelectedItem() == null) {
        return;
    }

    String prov = comboProveedor.getSelectedItem().toString();
    String rub = comboRubro.getSelectedItem().toString();
    String busqueda = txtBuscarProducto.getText().toLowerCase().trim();

    List<Object[]> auxiliar = new ArrayList<>();

    for (Object[] fila : datos) {
        String pFila = fila[6].toString();
        String rFila = fila[7].toString();
        String descFila = fila[2].toString().toLowerCase();
        String idFila = fila[1].toString().toLowerCase();

        // 1. Solo filtramos por Combos
        boolean coincideP = prov.equals("Todos Proveedores") || pFila.equals(prov);
        boolean coincideR = rub.equals("Todos Rubros") || rFila.equals(rub);

        // 2. Solo filtramos por Buscador de texto
        boolean coincideBusqueda = busqueda.isEmpty() || idFila.contains(busqueda) || descFila.contains(busqueda);

        // --- ELIMINAMOS LA VALIDACIÓN DEL PORCENTAJE AQUÍ ---
        
        if (coincideP && coincideR && coincideBusqueda) {
            auxiliar.add(fila);
        }
    }
    cargarDatosEnTabla(auxiliar);
}

    public void cargarDatosEnTabla(List<Object[]> listaParaMostrar) {
        isCargando = true; // <--- Bloqueo
        DefaultTableModel modelo = (DefaultTableModel) tablaTotalProductos.getModel();
        modelo.setRowCount(0);

        for (Object[] fila : listaParaMostrar) {
            modelo.addRow(fila);
        }
        isCargando = false; // <--- Desbloqueo
    }

    private void logicParaRecargarTodo() {
    if (datos == null || datos.isEmpty()) return;

    for (Object[] fila : datos) {
        // 1. Desmarcamos el Checkbox (Columna 0)
        fila[0] = false; 

        // 2. El Costo Anterior (Columna 3) ahora es el Costo Nuevo (Columna 4)
        // Esto es porque ya impactamos el aumento en la base de datos
        fila[3] = fila[4]; 

        // 3. El Precio Original Oculto (Columna 8) ahora es el Precio Nuevo (Columna 5)
        // Sincronizamos los respaldos con la nueva realidad de la DB
        fila[8] = fila[5]; 
    }

    // 4. Limpiamos el buscador de porcentaje para que no siga aplicando aumentos
    jTextField1.setText(""); 

    // 5. Refrescamos la vista para que el usuario vea la tabla "limpia"
    actualizarTablaFiltrada();
    tablaTotalProductos.repaint(); 
     mostrarAlerta("Vista actualizada. Los cambios ya son permanentes.", 1);
   // JOptionPane.showMessageDialog(this, "Vista actualizada. Los cambios ya son permanentes.");
}
    
    public void aplicarResaltadoFilas(Color colorResaltado) {
        // Definimos el renderizador para TODA la tabla [Oracle Docs](https://docs.oracle.com)
        DefaultTableCellRenderer renderizadorFila = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // 1. Verificamos el valor del checkbox en la columna 0 de ESTA fila
                Boolean estaMarcado = (Boolean) table.getValueAt(row, 0);

                if (isSelected) {
                    // Si el usuario hace clic en la celda, usamos el color de selección estándar
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else if (estaMarcado != null && estaMarcado) {
                    // 2. Si el checkbox está TRUE, pintamos toda la fila del color elegido
                    c.setBackground(colorResaltado);
                    c.setForeground(Color.BLACK);
                } else {
                    // 3. Si no, color blanco normal o el que tenga la tabla
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }

                // Mantenemos la alineación a la derecha solo para las columnas de precios (4 y 5)
                if (column == 4 || column == 5) {
                    setHorizontalAlignment(JLabel.RIGHT);
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                }

                return c;
            }
        };

        // Aplicamos este renderizador a TODAS las columnas (excepto la 0 si querés que el check sea estándar)
        for (int i = 1; i < tablaTotalProductos.getColumnCount(); i++) {
            tablaTotalProductos.getColumnModel().getColumn(i).setCellRenderer(renderizadorFila);
        }
    }

    public void marcarTodoLoVisible(boolean estado) {
        // 1. Obtenemos el modelo de la tabla (que contiene solo lo filtrado)
        DefaultTableModel modelo = (DefaultTableModel) tablaTotalProductos.getModel();
        int filasVisibles = modelo.getRowCount();

        // 2. Recorremos únicamente las filas que el usuario está viendo
        for (int i = 0; i < filasVisibles; i++) {
            // Seteamos el valor en la columna 0 (SEL)
            // Esto disparará el TableModelListener y actualizará tu lista 'datos' automáticamente
            modelo.setValueAt(estado, i, 0);
        }
    }

    public void aplicarAumentoPorcentual(double porcentaje) {
        // 1. Validamos que haya datos
        if (datos == null || datos.isEmpty()) {
            return;
        }

        // Convertimos el porcentaje (ej: 10% -> 1.10)
        double factor = 1 + (porcentaje / 100.0);

        for (Object[] fila : datos) {
            // 2. Solo procesamos si el Checkbox (Columna 0) está en TRUE
            if ((Boolean) fila[0]) {

                // 3. Obtenemos el Costo Anterior (Columna 3)
                double costoAnterior = Double.parseDouble(fila[3].toString());

                // 4. Calculamos el Costo Nuevo (Columna 4) con redondeo a 2 decimales
                double costoNuevo = Math.round((costoAnterior * factor) * 100.0) / 100.0;
                fila[4] = costoNuevo; // Guardamos en la lista maestra

                // 5. Opcional: Actualizar Precio Nuevo (Columna 5) 
                // Si querés que el precio de venta también suba en la misma proporción:
                double precioVentaAnterior = Double.parseDouble(fila[5].toString());
                double precioNuevo = Math.round((precioVentaAnterior * factor) * 100.0) / 100.0;
                fila[5] = precioNuevo;
            }
        }

        // 6. Refrescamos la tabla para ver los cambios y los colores
        actualizarTablaFiltrada();
        tablaTotalProductos.repaint();
    }

    public void colorearColumnaCheckbox(Color colorResaltado) {
        // Renderizador especial que hereda de JCheckBox [Oracle Docs](https://docs.oracle.com)
        TableCellRenderer renderCheck = new DefaultTableCellRenderer() {
            private final JCheckBox check = new JCheckBox();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                // Seteamos si está marcado o no según el valor (Boolean)
                check.setSelected(value != null && (Boolean) value);
                check.setHorizontalAlignment(JLabel.CENTER);
                check.setOpaque(true); // Fundamental para que se vea el color de fondo

                if (isSelected) {
                    check.setBackground(table.getSelectionBackground());
                } else if (value != null && (Boolean) value) {
                    // Pintamos el fondo del checkbox igual que el resto de la fila
                    check.setBackground(colorResaltado);
                } else {
                    check.setBackground(table.getBackground());
                }

                return check;
            }
        };

        // Aplicamos este renderizador SOLO a la columna 0
        tablaTotalProductos.getColumnModel().getColumn(0).setCellRenderer(renderCheck);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboProveedor;
    private javax.swing.JComboBox<String> comboRubro;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton selecionarTodo;
    private javax.swing.JTable tablaTotalProductos;
    private javax.swing.JTextField txtBuscarProducto;
    // End of variables declaration//GEN-END:variables
}
