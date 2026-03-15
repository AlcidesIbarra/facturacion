/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Formularios;

import Controlador.ControladorAdministracion;
import Formularios.FormActualizacionMasiva;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ALCIDES
 */
public class FormSeleccionarCliente extends javax.swing.JInternalFrame {

   private FormActualizacionMasiva padre;
    private List<Object[]> listaClientesBase;

    public FormSeleccionarCliente(FormActualizacionMasiva padre) {
        initComponents();
        initComponents();
        this.padre = padre;
        cargarDatosIniciales();
        
          
        
          tablaTotalProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaTotalProductos.getColumnModel().getColumn(0).setPreferredWidth(42);
            tablaTotalProductos.getColumnModel().getColumn(1).setPreferredWidth(52);
          tablaTotalProductos.getColumnModel().getColumn(2).setPreferredWidth(174);
            tablaTotalProductos.getColumnModel().getColumn(3).setPreferredWidth(122);
                ControladorAdministracion ca = new ControladorAdministracion();
                    Dimension d = new Dimension(128, 26); // El tamaño que quieras
      jButton1.setPreferredSize(d);
         jButton1.setMinimumSize(d);
         jButton1.setMaximumSize(d);
         
           jButton2.setPreferredSize(d);
         jButton2.setMinimumSize(d);
         jButton2.setMaximumSize(d);
                 
        // ca.BotonesCrisitalPrim(btnGuardarCliente, Color.green, Color.black, Color.black);
        ca.BotonesCrisitalPrim(jButton1, Color.green, Color.black, Color.black);
          ca.BotonesCrisitalPrim(jButton2, Color.red, Color.black, Color.black);
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
    private void cargarDatosIniciales() {
        Controlador.ControladorCliente cc = new Controlador.ControladorCliente();
        // Usamos el método que creamos antes para traer los clientes activos
        listaClientesBase = cc.obtenerClientesParaSeleccion(); 
        
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"SEL", "ID", "Nombre", "DNI/CUIT"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int c) {
                return (c == 0) ? Boolean.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 0; // Solo el checkbox es editable
            }
        };

        for (Object[] fila : listaClientesBase) {
            modelo.addRow(fila);
        }
        tablaTotalProductos.setModel(modelo);
        // Permitimos que el checkbox responda al primer clic
        tablaTotalProductos.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }
public void seleccionarTodos(boolean seleccionar) {
    // Obtenemos el modelo de la tabla
    DefaultTableModel modelo = (DefaultTableModel) tablaTotalProductos.getModel();
    
    // Recorremos todas las filas
    for (int i = 0; i < modelo.getRowCount(); i++) {
        // La columna 0 es donde definiste el Boolean (checkbox)
        modelo.setValueAt(seleccionar, i, 0);
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTotalProductos = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        txtBuscarCliente = new javax.swing.JTextField();
        comboOrden = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Seleccionar Clientes");

        tablaTotalProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaTotalProductos);

        jButton1.setText("Confirmar Seleccion");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtBuscarCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarClienteKeyReleased(evt);
            }
        });

        comboOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ordenar por Nombre", "Ordenar por DNI", "Ordenar por Codigo" }));
        comboOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboOrdenActionPerformed(evt);
            }
        });

        jButton2.setText("Seleccionar todo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addComponent(comboOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       List<Integer> seleccionados = new ArrayList<>();
        DefaultTableModel modelo = (DefaultTableModel) tablaTotalProductos.getModel();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Boolean check = (Boolean) modelo.getValueAt(i, 0);
            if (check != null && check) {
                // El ID está en la columna 1
                seleccionados.add((Integer) modelo.getValueAt(i, 1));
            }
        }

        if (seleccionados.isEmpty()) {
            mostrarAlerta("Debe seleccionar al menos un cliente.", 2);
           // JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un cliente.");
            return;
        }

        // Llamamos al método del padre para filtrar su lista 'datos'
        padre.recibirClientesSeleccionados(seleccionados);
        
        // Cerramos este frame
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtBuscarClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarClienteKeyReleased
     if (listaClientesBase == null) {
        return; 
    }

    String busqueda = txtBuscarCliente.getText().toLowerCase().trim();
    DefaultTableModel modelo = (DefaultTableModel) tablaTotalProductos.getModel();
    modelo.setRowCount(0);

    for (Object[] fila : listaClientesBase) {
        // 2. SEGURIDAD: Validamos que cada celda no sea NULL antes de usar toString()
        String codigo = (fila[1] != null) ? fila[1].toString().toLowerCase() : "";
        String nombre = (fila[2] != null) ? fila[2].toString().toLowerCase() : "";
        String dni    = (fila[3] != null) ? fila[3].toString().toLowerCase() : "";
        
        if (codigo.contains(busqueda) || nombre.contains(busqueda) || dni.contains(busqueda)) {
            modelo.addRow(fila);
        }
    }
    }//GEN-LAST:event_txtBuscarClienteKeyReleased

    private void comboOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboOrdenActionPerformed
                                           
    String seleccion = comboOrden.getSelectedItem().toString();

    if (seleccion.equals("Ordenar por Codigo")) {
        ordenarListaMemoria(1); // Índice 1: ID
    } else if (seleccion.equals("Ordenar por Nombre")) {
        ordenarListaMemoria(2); // Índice 2: Nombre
    } else if (seleccion.equals("Ordenar por DNI")) {
        ordenarListaMemoria(3); // Índice 3: DNI
    }


    }//GEN-LAST:event_comboOrdenActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
   // Comprobamos si el botón dice "Seleccionar todo"
    if (jButton2.getText().equalsIgnoreCase("Seleccionar todo")) {
        seleccionarTodos(true);
        jButton2.setText("Deseleccionar todo");
            ControladorAdministracion ca = new ControladorAdministracion();
     
          ca.BotonesCrisitalPrim(jButton2, Color.yellow, Color.black, Color.black);
    
    } else {
        seleccionarTodos(false);
        jButton2.setText("Seleccionar todo");
               ControladorAdministracion ca = new ControladorAdministracion();
     
          ca.BotonesCrisitalPrim(jButton2, Color.red, Color.black, Color.black);
    }
    }//GEN-LAST:event_jButton2ActionPerformed
public void ordenarListaMemoria(int indiceColumna) {
    // 1. Validamos que la lista no sea nula
    if (listaClientesBase == null || listaClientesBase.isEmpty()) return;

    Collections.sort(listaClientesBase, new Comparator<Object[]>() {
        @Override
        public int compare(Object[] o1, Object[] o2) {
            // 2. Extraemos los valores con seguridad (si es null, tratamos como vacío "")
            String v1 = (o1[indiceColumna] != null) ? o1[indiceColumna].toString() : "";
            String v2 = (o2[indiceColumna] != null) ? o2[indiceColumna].toString() : "";
            
            // 3. Si comparamos CÓDIGO (índice 1), forzamos orden numérico
            if (indiceColumna == 1) {
                try {
                    int n1 = v1.isEmpty() ? 0 : Integer.parseInt(v1);
                    int n2 = v2.isEmpty() ? 0 : Integer.parseInt(v2);
                    return Integer.compare(n1, n2);
                } catch (NumberFormatException e) {
                    return 0; // Si no es número, no movemos la posición
                }
            }
            
            // 4. Para Nombre y DNI, usamos comparación de texto ignorando mayúsculas
            return v1.compareToIgnoreCase(v2);
        }
    });

    // 5. Refrescamos la tabla llamando a tu buscador (que redibuja el modelo)
    txtBuscarClienteKeyReleased(null); 
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboOrden;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaTotalProductos;
    private javax.swing.JTextField txtBuscarCliente;
    // End of variables declaration//GEN-END:variables
}
