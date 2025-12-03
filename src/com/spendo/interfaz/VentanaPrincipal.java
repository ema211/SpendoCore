package com.spendo.interfaz;

import com.spendo.core.*;
import com.spendo.sistemaArchivos.AdminArchivos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Comparator; // <--- Importante para ordenar


public class VentanaPrincipal extends JFrame {

    private Usuario usuario;
    private AdminArchivos admin;

    // Modelos para las tablas
    private DefaultTableModel modeloCuentas;
    private DefaultTableModel modeloMovimientos;

    private JLabel labelSaldoTotal;

    public VentanaPrincipal(Usuario usuario) {
        this.usuario = usuario;
        this.admin = new AdminArchivos();

        setTitle("Spendo - Dashboard de " + usuario.getUsername());
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar

        initUI();
        cargarDatos();
    }

    private void initUI() {
        // Layout Principal
        setLayout(new BorderLayout());

        // --- 1. PANEL SUPERIOR (Botones) ---
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(60, 63, 65)); // Gris oscuro
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));

        JButton btnGasto = crearBoton("💸 Gasto", new Color(220, 53, 69)); // Rojo
        JButton btnIngreso = crearBoton("💰 Ingreso", new Color(40, 167, 69)); // Verde
        JButton btnTransfer = crearBoton("🔄 Transferir", new Color(255, 193, 7)); // Amarillo
        JButton btnNuevaCuenta = crearBoton("🏦 Nueva Cuenta", new Color(23, 162, 184)); // Cyan

        // Acciones de botones
        btnGasto.addActionListener(e -> registrarOperacion("GASTO"));
        btnIngreso.addActionListener(e -> registrarOperacion("INGRESO"));
        btnTransfer.addActionListener(e -> registrarOperacion("TRANSFERENCIA"));
        btnNuevaCuenta.addActionListener(e -> crearNuevaCuenta());

        topPanel.add(btnGasto);
        topPanel.add(btnIngreso);
        topPanel.add(btnTransfer);
        topPanel.add(btnNuevaCuenta);

        // Label de Saldo Total
        labelSaldoTotal = new JLabel("Total: $0.00");
        labelSaldoTotal.setForeground(Color.WHITE);
        labelSaldoTotal.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(Box.createHorizontalStrut(50));
        topPanel.add(labelSaldoTotal);

        add(topPanel, BorderLayout.NORTH);

        // --- 2. PANEL CENTRAL (Tablas) ---
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(300); // Espacio para la tabla de cuentas

        // TABLA CUENTAS (Izquierda)
        modeloCuentas = new DefaultTableModel();
        modeloCuentas.addColumn("Cuenta");
        modeloCuentas.addColumn("Saldo");
        JTable tablaCuentas = new JTable(modeloCuentas);
        // Estilo básico
        tablaCuentas.setRowHeight(25);
        tablaCuentas.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel panelCuentas = new JPanel(new BorderLayout());
        panelCuentas.add(new JLabel("  Mis Cuentas"), BorderLayout.NORTH);
        panelCuentas.add(new JScrollPane(tablaCuentas), BorderLayout.CENTER);

        // TABLA MOVIMIENTOS (Derecha)
        modeloMovimientos = new DefaultTableModel();
        modeloMovimientos.addColumn("Fecha");
        modeloMovimientos.addColumn("Tipo");
        modeloMovimientos.addColumn("Categoría");
        modeloMovimientos.addColumn("Monto");
        modeloMovimientos.addColumn("Detalle");

        JTable tablaMovimientos = new JTable(modeloMovimientos);
        tablaMovimientos.setRowHeight(25);

        JPanel panelMovs = new JPanel(new BorderLayout());
        panelMovs.add(new JLabel("  Historial de Movimientos (Por Cuenta)"), BorderLayout.NORTH);
        panelMovs.add(new JScrollPane(tablaMovimientos), BorderLayout.CENTER);

        splitPane.setLeftComponent(panelCuentas);
        splitPane.setRightComponent(panelMovs);

        add(splitPane, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(colorFondo);
        btn.setForeground(Color.BLACK);
        if (colorFondo.equals(new Color(220, 53, 69)) || colorFondo.equals(new Color(40, 167, 69))) {
            btn.setForeground(Color.WHITE);
        }
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        return btn;
    }

    // --- MÉTODOS DE LÓGICA UI (Aquí está la magia nueva) ---

    private void cargarDatos() {
        // 1. Limpiar tablas visuales
        modeloCuentas.setRowCount(0);
        modeloMovimientos.setRowCount(0);
        double totalGlobal = 0;

        // 2. Iterar sobre cada cuenta
        for (Cuenta c : usuario.getCuentas()) {

            // Llenar tabla de Saldos (Izquierda)
            modeloCuentas.addRow(new Object[]{c.getNombre(), "$" + c.getBalance()});
            totalGlobal += c.getBalance();

            // --- ORDENAMIENTO (Comparator) ---
            // Ordenamos los registros de más antiguo a más nuevo
            c.getRegistros().sort(Comparator.comparing(Registro::getFecha));

            // --- SEPARADOR VISUAL ---
            // Fila falsa para que se vea bonito
            modeloMovimientos.addRow(new Object[]{
                    "──────",
                    "📂 " + c.getNombre().toUpperCase(), // Título de la cuenta
                    "──────",
                    "──────",
                    "──────"
            });

            // 3. Llenar Movimientos (Derecha)
            for (Registro r : c.getRegistros()) {
                String detalle = "";
                String tipo = "";

                if (r instanceof Gasto) {
                    tipo = "Gasto";
                    detalle = "Salida";
                } else if (r instanceof Ingreso) {
                    tipo = "Ingreso";
                    detalle = "Entrada";
                } else if (r instanceof Transferencia) {
                    tipo = "Transf.";
                    detalle = "Movimiento interno";
                }

                modeloMovimientos.addRow(new Object[]{
                        r.getFecha().toLocalDate().toString(), // Fecha limpia
                        tipo,
                        r.getCategoria(),
                        "$" + r.getMonto(),
                        detalle
                });
            }
        }

        labelSaldoTotal.setText("Total Neto: $" + totalGlobal);
    }

    private void crearNuevaCuenta() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la cuenta:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        String saldoStr = JOptionPane.showInputDialog(this, "Saldo inicial:");
        try {
            double saldo = Double.parseDouble(saldoStr);
            Cuenta nueva = new Cuenta(nombre, saldo);

            // Guardar en RAM y en DISCO
            usuario.addCuenta(nueva);
            admin.actualizarCuentasFile(usuario); // <--- Persistencia

            cargarDatos(); // Refrescar UI
            JOptionPane.showMessageDialog(this, "Cuenta creada!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en el monto.");
        }
    }

    private void registrarOperacion(String tipo) {
        if (usuario.getCuentas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "¡Primero crea una cuenta!");
            return;
        }

        // Panel personalizado para el Pop-up
        JPanel panel = new JPanel(new GridLayout(0, 1));

        JComboBox<String> comboCuentas = new JComboBox<>();
        JComboBox<String> comboDestino = new JComboBox<>(); // Solo para transferencia
        for (Cuenta c : usuario.getCuentas()) {
            comboCuentas.addItem(c.getNombre());
            comboDestino.addItem(c.getNombre());
        }

        JComboBox<Categoria> comboCat = new JComboBox<>(Categoria.values());
        JTextField txtMonto = new JTextField();

        panel.add(new JLabel("Cuenta " + (tipo.equals("TRANSFERENCIA") ? "Origen" : "") + ":"));
        panel.add(comboCuentas);

        if (tipo.equals("TRANSFERENCIA")) {
            panel.add(new JLabel("Cuenta Destino:"));
            panel.add(comboDestino);
        }

        panel.add(new JLabel("Categoría:"));
        panel.add(comboCat);
        panel.add(new JLabel("Monto:"));
        panel.add(txtMonto);

        int result = JOptionPane.showConfirmDialog(this, panel, "Registrar " + tipo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double monto = Double.parseDouble(txtMonto.getText());
                Categoria cat = (Categoria) comboCat.getSelectedItem();

                String nombreCuenta = (String) comboCuentas.getSelectedItem();
                Cuenta cuentaSeleccionada = buscarCuenta(nombreCuenta);

                Registro nuevoRegistro = null;

                if (tipo.equals("GASTO")) {
                    nuevoRegistro = new Gasto(monto, LocalDateTime.now(), cat, cuentaSeleccionada);
                } else if (tipo.equals("INGRESO")) {
                    nuevoRegistro = new Ingreso(monto, LocalDateTime.now(), cat, cuentaSeleccionada);
                } else if (tipo.equals("TRANSFERENCIA")) {
                    String nombreDest = (String) comboDestino.getSelectedItem();
                    if (nombreCuenta.equals(nombreDest)) {
                        JOptionPane.showMessageDialog(this, "No puedes transferir a la misma cuenta.");
                        return;
                    }
                    Cuenta cuentaDest = buscarCuenta(nombreDest);
                    nuevoRegistro = new Transferencia(monto, LocalDateTime.now(), Categoria.TRANSFERENCIA, cuentaSeleccionada, cuentaDest);
                }

                // --- ATOMICIDAD + PERSISTENCIA ---
                if (nuevoRegistro != null && nuevoRegistro.aplicar()) {
                    // Guardar en disco duro
                    admin.guardarRegistroFile(usuario, nuevoRegistro);

                    JOptionPane.showMessageDialog(this, "¡Éxito!");
                    cargarDatos(); // Refrescar la tabla con los datos nuevos
                } else {
                    JOptionPane.showMessageDialog(this, "Saldo insuficiente o error.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos: " + ex.getMessage());
            }
        }
    }

    private Cuenta buscarCuenta(String nombre) {
        for (Cuenta c : usuario.getCuentas()) {
            if (c.getNombre().equals(nombre)) return c;
        }
        return null;
    }
}