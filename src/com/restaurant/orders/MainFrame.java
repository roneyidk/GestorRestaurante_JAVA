/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.orders;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {
    private GestorPedidos gestor = new GestorPedidos();

    private JComboBox<String> comboPlatillos;
    private JPanel extrasPanel;
    private JSpinner spinnerCantidad;
    private JTextField tfCliente;
    private DefaultTableModel tablaModel;
    private JTable tablaPedidos;
    private NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.getDefault());

    // Mapa platillo -> lista de extras (clave interna)
    private Map<String, List<ExtraOption>> extrasPorPlatillo = new HashMap<>();

    public MainFrame() {
        setTitle("Gestor de Pedidos - Restaurante");
        setSize(1000, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        configurarExtrasPorPlatillo();
        initComponents();
    }

    private void configurarExtrasPorPlatillo() {
        extrasPorPlatillo.put("Hamburguesa", Arrays.asList(
                new ExtraOption("Queso", "Queso"), new ExtraOption("Bacon", "Bacon")
        ));
        extrasPorPlatillo.put("Pizza", Arrays.asList(
                new ExtraOption("Queso extra", "Queso"), new ExtraOption("Pepperoni", "Pepperoni")
        ));
        extrasPorPlatillo.put("Ensalada", Arrays.asList(
                new ExtraOption("Crutones", "Crutones"), new ExtraOption("Queso Feta", "QuesoFeta")
        ));
    }

    private void initComponents() {
        // Panel Crear (rojo suave)
        JPanel panelCrear = new JPanel();
        panelCrear.setBorder(BorderFactory.createTitledBorder("Crear nuevo pedido"));
        panelCrear.setBackground(new Color(255, 200, 200));
        panelCrear.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;

        // Campos
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
        extrasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        extrasPanel.setBackground(new Color(255, 200, 200));
        panelCrear.add(extrasPanel, c);

        c.gridx = 0; c.gridy = 3;
        panelCrear.add(new JLabel("Cantidad:"), c);
        c.gridx = 1;
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1,1,20,1));
        panelCrear.add(spinnerCantidad, c);

        c.gridx = 0; c.gridy = 4; c.gridwidth = 2;
        JButton btnAgregar = new JButton("Agregar pedido");
        panelCrear.add(btnAgregar, c);

        // Logo a la derecha (columna 2 ocupando filas)
        c.gridx = 2; c.gridy = 0; c.gridheight = 5; c.anchor = GridBagConstraints.CENTER;
        JLabel etiquetaLogo = cargarLogoLabel();
        panelCrear.add(etiquetaLogo, c);
        c.gridheight = 1; // reset

        // Panel central: tabla de pedidos
        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBorder(BorderFactory.createTitledBorder("Pedidos"));
        tablaModel = new DefaultTableModel(new Object[]{"ID","Cliente","Total","Estado"}, 0) {
            @Override public boolean isCellEditable(int row,int col){return false;}
        };
        tablaPedidos = new JTable(tablaModel);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaPedidos);
        panelLista.add(scroll, BorderLayout.CENTER);

        // Aplicar renderer para colorear filas según estado
        tablaPedidos.setDefaultRenderer(Object.class, new EstadoRenderer());

        // Panel derecho: acciones (verdecito)
        JPanel panelAcciones = new JPanel();
        panelAcciones.setBackground(new Color(200, 255, 200));
        panelAcciones.setPreferredSize(new Dimension(300, 0));
        panelAcciones.setLayout(new BoxLayout(panelAcciones, BoxLayout.Y_AXIS));
        panelAcciones.setBorder(BorderFactory.createTitledBorder("Acciones"));

        JButton btnVerDetalles = new JButton("Ver detalles");
        JButton btnCambiarEstado = new JButton("Cambiar estado");
        JButton btnEliminar = new JButton("Eliminar pedido");
        JComboBox<EstadoPedido> comboEstado = new JComboBox<>(EstadoPedido.values());

        panelAcciones.add(Box.createVerticalStrut(10));
        panelAcciones.add(btnVerDetalles);
        panelAcciones.add(Box.createVerticalStrut(8));
        panelAcciones.add(new JLabel("Nuevo estado:"));
        panelAcciones.add(comboEstado);
        panelAcciones.add(Box.createVerticalStrut(8));
        panelAcciones.add(btnCambiarEstado);
        panelAcciones.add(Box.createVerticalStrut(10));
        panelAcciones.add(btnEliminar);

        // Layout principal
        getContentPane().setLayout(new BorderLayout(8,8));
        add(panelCrear, BorderLayout.NORTH);
        add(panelLista, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.EAST);

        // Inicializar extras según platillo seleccionado al inicio
        actualizarExtras();

        // Listeners
        comboPlatillos.addActionListener(e -> actualizarExtras());
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
                // Mensajes y guardado de historial cuando corresponde
                if (nuevo == EstadoPedido.ENTREGADO) {
                    JOptionPane.showMessageDialog(this, "✔ Producto entregado.");
                    guardarHistorial(p);
                } else if (nuevo == EstadoPedido.CANCELADO) {
                    JOptionPane.showMessageDialog(this, "❌ Pedido cancelado.");
                    guardarHistorial(p);
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tablaPedidos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un pedido.");
                return;
            }
            int id = (int) tablaModel.getValueAt(fila, 0);
            Pedido p = gestor.buscarPorId(id);
            if (p == null) return;
            if (p.getEstado() != EstadoPedido.CANCELADO) {
                JOptionPane.showMessageDialog(this, "Solo se pueden eliminar pedidos CANCELADOS.");
                return;
            }
            gestor.eliminarPedido(id);
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Pedido eliminado correctamente.");
        });
    }

    private JLabel cargarLogoLabel() {
        try {
            // Intentamos cargar recurso desde el classpath primero
            URL resource = getClass().getResource("/com/restaurant/orders/logo.jpeg");
            Image img;
            if (resource != null) {
                img = ImageIO.read(resource);
            } else {
                // fallback cargar desde ruta relativa (útil al desarrollar)
                img = ImageIO.read(new java.io.File("src/com/restaurant/orders/logo.jpeg"));
            }
            Image scaled = img.getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(scaled));
        } catch (IOException ex) {
            // Si no existe la imagen, devolvemos un JLabel vacío
            JLabel lbl = new JLabel();
            lbl.setPreferredSize(new Dimension(130,130));
            return lbl;
        }
    }

    private void actualizarExtras() {
        extrasPanel.removeAll();
        String elegido = (String) comboPlatillos.getSelectedItem();
        List<ExtraOption> extras = extrasPorPlatillo.getOrDefault(elegido, Collections.emptyList());
        for (ExtraOption eo : extras) {
            JCheckBox chk = new JCheckBox(eo.getLabel() + " (+" + String.format("$%.2f", eo.getPrecio()) + ")");
            chk.setActionCommand(eo.getKey()); // clave para identificar extra
            chk.setBackground(new Color(255, 200, 200));
            extrasPanel.add(chk);
        }
        extrasPanel.revalidate();
        extrasPanel.repaint();
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

        // Recolectar extras seleccionados y aplicar Decorator
        Component[] comps = extrasPanel.getComponents();
        for (Component comp : comps) {
            if (comp instanceof JCheckBox chk && chk.isSelected()) {
                String key = chk.getActionCommand();
                base = aplicarExtraPorClave(base, key);
            }
        }

        int cantidad = (Integer) spinnerCantidad.getValue();

        Pedido pedido = new Pedido(cliente);
        pedido.agregarItem(new PedidoItem(base, cantidad));
        gestor.agregarPedido(pedido);
        actualizarTabla();
        limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Pedido agregado. ID: " + pedido.getId());
    }

    private Platillo aplicarExtraPorClave(Platillo base, String clave) {
        return switch (clave) {
            case "Queso" -> new Queso(base);
            case "Bacon" -> new Bacon(base);
            case "Pepperoni" -> new Pepperoni(base);
            case "Crutones" -> new Crutones(base);
            case "QuesoFeta" -> new QuesoFeta(base);
            default -> base;
        };
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
            tablaModel.addRow(new Object[]{p.getId(), p.getCliente(), moneda.format(p.getTotal()), p.getEstado().name()});
        }
    }

    private void limpiarFormulario() {
        tfCliente.setText("");
        spinnerCantidad.setValue(1);
        actualizarExtras();
    }

    private void guardarHistorial(Pedido p) {
        try (FileWriter fw = new FileWriter("historial.txt", true)) {
            fw.write(p.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clase auxiliar para describir opciones de extras
    private static class ExtraOption {
        private final String label;
        private final String key;

        public ExtraOption(String label, String key) {
            this.label = label;
            this.key = key;
        }

        public String getLabel() { return label; }
        public String getKey() { return key; }

        // precio derivado por clave (sincrónico con decoradores)
        public double getPrecio() {
            return switch (key) {
                case "Queso" -> 1.50;
                case "Bacon" -> 2.00;
                case "Pepperoni" -> 1.75;
                case "Crutones" -> 1.20;
                case "QuesoFeta" -> 1.80;
                default -> 0.0;
            };
        }

    }
}
