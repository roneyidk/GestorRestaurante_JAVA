/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.orders;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Locale;
/**
 *
 * @author patito321
 */
public class MainFrame extends JFrame{
    private GestorPedidos gestor = new GestorPedidos();
    
    private JComboBox<String> comboPlatillos;
    private JCheckBox checkQueso;
    private JCheckBox checkBacon;
    private JSpinner spinnerCantidad;
    private JTextField tfCliente;
    private DefaultTableModel tablaModel;
    private JTable tablaPedidos;
    private NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.getDefault());
    
    public MainFrame() {
        setTitle("--Gestor de Pedidos--\n--Restaurante Yulan--");
        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }
    
    private void initComponents(){
        //SE CREA EL PEDIDO
        JPanel panelCrear = new JPanel();
        panelCrear .setBorder(BorderFactory.createTitledBorder("Crear nuevo pedido"));
        panelCrear .setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c .insets = new Insets(4,4,4,4);
        c .anchor = GridBagConstraints.WEST;
        
        c.gridx = 0; c.gridy = 0;
        panelCrear.add(new JLabel("Cliente:"), c);
        c.gridx = 1;
        tfCliente = new JTextField(12);
        panelCrear.add(tfCliente, c);

        c.gridx = 0; c.gridy = 1;
        panelCrear.add(new JLabel("Platillo:"), c);
        c.gridx = 1;
        comboPlatillos = new JComboBox<>(new String[]{"Hamburguesa", "Pizza", "Ensalada"});
        panelCrear.add(comboPlatillos, c);

        c.gridx = 0; c.gridy = 2;
        panelCrear.add(new JLabel("Extras:"), c);
        c.gridx = 1;
        JPanel extrasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkQueso = new JCheckBox("Queso (+$1.50)");
        checkBacon = new JCheckBox("Bacon (+$2.00)");
        extrasPanel.add(checkQueso);
        extrasPanel.add(checkBacon);
        panelCrear.add(extrasPanel, c);

        c.gridx = 0; c.gridy = 3;
        panelCrear.add(new JLabel("Cantidad:"), c);
        c.gridx = 1;
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1,1,20,1));
        panelCrear.add(spinnerCantidad, c);

        c.gridx = 0; c.gridy = 4; c.gridwidth = 2;
        JButton btnAgregar = new JButton("Agregar pedido");
        panelCrear.add(btnAgregar, c);

        // Panel central: tabla de pedidos
        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBorder(BorderFactory.createTitledBorder("Pedidos"));
        tablaModel = new DefaultTableModel(new Object[]{"ID","Cliente","Total","Estado"}, 0) {
            @Override public boolean isCellEditable(int row,int col){return false;}
        };
        tablaPedidos = new JTable(tablaModel);
        JScrollPane scroll = new JScrollPane(tablaPedidos);
        panelLista.add(scroll, BorderLayout.CENTER);

        // Panel derecho: detalles y acciones
        JPanel panelAcciones = new JPanel();
        panelAcciones.setPreferredSize(new Dimension(260, 0));
        panelAcciones.setLayout(new BoxLayout(panelAcciones, BoxLayout.Y_AXIS));
        panelAcciones.setBorder(BorderFactory.createTitledBorder("Acciones"));

        JButton btnVerDetalles = new JButton("Ver detalles");
        JButton btnCambiarEstado = new JButton("Cambiar estado");
        JComboBox<EstadoPedido> comboEstado = new JComboBox<>(EstadoPedido.values());

        panelAcciones.add(btnVerDetalles);
        panelAcciones.add(Box.createVerticalStrut(8));
        panelAcciones.add(new JLabel("Nuevo estado:"));
        panelAcciones.add(comboEstado);
        panelAcciones.add(Box.createVerticalStrut(8));
        panelAcciones.add(btnCambiarEstado);
        panelAcciones.add(Box.createVerticalStrut(16));

        // Agregar paneles al frame
        getContentPane().setLayout(new BorderLayout());
        add(panelCrear, BorderLayout.NORTH);
        add(panelLista, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.EAST);

        // Acciones
        btnAgregar.addActionListener(e -> accionAgregarPedido());
        btnVerDetalles.addActionListener(e -> accionVerDetalles());
        btnCambiarEstado.addActionListener(e -> {
            int fila = tablaPedidos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un pedido en la tabla.");
                return;
            }
            int id = (int) tablaModel.getValueAt(fila, 0);
            Pedido p = gestor.buscarPorId(id);
            if (p != null) {
                EstadoPedido nuevo = (EstadoPedido) comboEstado.getSelectedItem();
                p.setEstado(nuevo);
                actualizarTabla();
            }
        });
    }

    private void accionAgregarPedido() {
        String cliente = tfCliente.getText().trim();
        if (cliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del cliente.");
            return;
        }
        Platillo base;
        String elegido = (String) comboPlatillos.getSelectedItem();
        switch (elegido) {
            case "Hamburguesa" -> base = new Hamburguesa();
            case "Pizza" -> base = new Pizza();
            default -> base = new Ensalada();
        }
        // aplicar extras (Decorator)
        if (checkQueso.isSelected()) base = new Queso(base);
        if (checkBacon.isSelected()) base = new Bacon(base);

        int cantidad = (Integer) spinnerCantidad.getValue();

        Pedido pedido = new Pedido(cliente);
        pedido.agregarItem(new PedidoItem(base, cantidad));
        gestor.agregarPedido(pedido);
        actualizarTabla();
        limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Pedido agregado. ID: " + pedido.getId());
    }

    private void accionVerDetalles() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido en la tabla.");
            return;
        }
        int id = (int) tablaModel.getValueAt(fila, 0);
        Pedido p = gestor.buscarPorId(id);
        if (p == null) return;
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido ID: ").append(p.getId()).append("\n");
        sb.append("Cliente: ").append(p.getCliente()).append("\n");
        sb.append("Estado: ").append(p.getEstado()).append("\n");
        sb.append("Items:\n");
        for (PedidoItem it : p.getItems()) {
            sb.append("  - ").append(it.getDescripcionCompleta()).append("\n");
        }
        sb.append("Total: ").append(moneda.format(p.getTotal())).append("\n");
        JOptionPane.showMessageDialog(this, sb.toString(), "Detalles del pedido", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarTabla() {
        tablaModel.setRowCount(0);
        for (Pedido p : gestor.obtenerPedidos()) {
            tablaModel.addRow(new Object[]{p.getId(), p.getCliente(), moneda.format(p.getTotal()), p.getEstado()});
        }
    }

    private void limpiarFormulario() {
        tfCliente.setText("");
        checkQueso.setSelected(false);
        checkBacon.setSelected(false);
        spinnerCantidad.setValue(1);
    }
}