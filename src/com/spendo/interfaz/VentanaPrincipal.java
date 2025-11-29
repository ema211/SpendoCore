package com.spendo.ui;

import com.spendo.core.*;
import com.spendo.enums.Categoria;
import com.spendo.sistemaArchivos.AdminArchivos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private Usuario usuario;
    private AdminArchivos admin;

    // Modelos para las tablas (nos permiten borrar/agregar filas facil)
    private DefaultTableModel modeloCuentas;
    private DefaultTableModel modeloMovimientos;

    private JLabel labelSaldoTotal;

    public VentanaPrincipal(Usuario usuario) {
        this.usuario = usuario;
        this.admin = new AdminArchivos();

        setTitle("Spendo - Dashboard de " + usuario.getUsername());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        cargarDatos();
    }

    private void initUI() {
        // Layout Principal: BorderLayout
        setLayout(new BorderLayout());

        // --- 1. PANEL SUPERIOR (Botones) ---
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(60, 63, 65));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));

        JButton btnGasto = crearBoton("💸 Gasto", new Color(220, 53, 69)); // Rojo
        JButton btnIngreso = crearBoton("💰 Ingreso", new Color(40, 167, 69)); // Verde
        JButton btnTransfer = crearBoton("🔄 Transferir", new Color(255, 193, 7)); // Amarillo
        JButton btnNuevaCuenta = crearBoton("🏦 Nueva Cuenta", new Color(23, 162, 184)); // Cyan

        // Acciones
        btnGasto.addActionListener(e -> registrarOperacion("GASTO"));
        btnIngreso.addActionListener(e -> registrarOperacion("INGRESO"));
        btnTransfer.addActionListener(e -> registrarOperacion("TRANSFERENCIA"));
        btnNuevaCuenta.addActionListener(e -> crearNuevaCuenta());

        topPanel.add(btnGasto);
        topPanel.add(btnIngreso);
        topPanel.add(btnTransfer);
        topPanel.add(btnNuevaCuenta);

        // Saldo Total Label
        labelSaldoTotal = new JLabel("Total: $0.00");
        labelSaldoTotal.setForeground(Color.WHITE);
        labelSaldoTotal.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(Box.createHorizontalStrut(50)); // Espacio
        topPanel.add(labelSaldoTotal);

        add(topPanel, BorderLayout.NORTH);

        // --- 2. PANEL CENTRAL (Tablas) ---
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(300); // 300px para cuentas

        // TABLA CUENTAS (Izquierda)
        modeloCuentas = new DefaultTableModel();
        modeloCuentas.addColumn("Cuenta");
        modeloCuentas.addColumn("Saldo");
        JTable tablaCuentas = new JTable(modeloCuentas);
        JPanel panelCuentas = new JPanel(new BorderLayout());
        panelCuentas.add(new JLabel("  Mis Cuentas"), BorderLayout.NORTH);
        panelCuentas.add(new JScrollPane(tablaCuentas), BorderLayout.CENTER);

        // TABLA MOVIMIENTOS (Derecha)
        modeloMovimientos = new DefaultTableModel();
        modeloMovimientos.addColumn("Fecha");
        modeloMovimientos.addColumn("Tipo");
        modeloMovimientos.addColumn("Categoría");
        modeloMovimientos.addColumn("Monto");
        modeloMovimientos.addColumn("Detalle"); // Cuentas involucradas
        JTable tablaMovimientos = new JTable(modeloMovimientos);
        JPanel panelMovs = new JPanel(new BorderLayout());
        panelMovs.add(new JLabel("  Historial de Movimientos"), BorderLayout.NORTH);
        panelMovs.add(new JScrollPane(tablaMovimientos), BorderLayout.CENTER);

        splitPane.setLeftComponent(panelCuentas);
        splitPane.setRightComponent(panelMovs);

        add(splitPane, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(colorFondo);
        btn.setForeground(Color.BLACK); // Texto negro para contraste en amarillo/cyan
        if (colorFondo.equals(new Color(220, 53, 69)) || colorFondo.equals(new Color(40, 167, 69))) {
            btn.setForeground(Color.WHITE); // Texto blanco para Rojo/Verde
        }
        btn.setFocusPainted(false);
        return btn;
    }

    // --- MÉTODOS DE LÓGICA UI ---

    private void cargarDatos() {
        // 1. Limpiar tablas
        modeloCuentas.setRowCount(0);
        modeloMovimientos.setRowCount(0);
        double totalGlobal = 0;

        // 2. Llenar Cuentas
        for (Cuenta c : usuario.getCuentas()) {
            modeloCuentas.addRow(new Object[]{c.getNombre(), "$" + c.getBalance()});
            totalGlobal += c.getBalance();

            // 3. Llenar Movimientos de esta cuenta (OJO: Pueden salir duplicados si no filtramos,
            // pero para esta version simple mostremos todo lo que hay en cada cuenta)
            for (Registro r : c.getRegistros()) {
                String detalle = "";
                String tipo = "";

                if (r instanceof Gasto) {
                    tipo = "Gasto";
                    detalle = c.getNombre();
                } else if (r instanceof Ingreso) {
                    tipo = "Ingreso";
                    detalle = c.getNombre();
                } else if (r instanceof Transferencia) {
                    tipo = "Transferencia";
                    // Hack rápido para mostrar info en tabla
                    detalle = "Movimiento en " + c.getNombre();
                }

                modeloMovimientos.addRow(new Object[]{
                        r.getFecha().toLocalDate().toString(), // Solo fecha sin hora
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

            // Lógica Core + Persistencia
            usuario.addCuenta(nueva);
            admin.actualizarCuentasFile(usuario);

            cargarDatos(); // Refrescar UI
            JOptionPane.showMessageDialog(this, "Cuenta creada!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en el monto.");
        }
    }

    private void registrarOperacion(String tipo) {
        // Necesitamos listas para los ComboBox (Selectores)
        if (usuario.getCuentas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "¡Primero crea una cuenta!");
            return;
        }

        // Crear un Panel personalizado para el Pop-up
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

                // Buscar objetos Cuenta reales
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

                // --- MOMENTO DE LA VERDAD (CORE + PERSISTENCIA) ---
                if (nuevoRegistro != null && nuevoRegistro.aplicar()) {
                    // Guardar en archivo
                    admin.guardarRegistroFile(usuario, nuevoRegistro);

                    JOptionPane.showMessageDialog(this, "¡Éxito!");
                    cargarDatos(); // Refrescar la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "Saldo insuficiente o error.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos: " + ex.getMessage());
            }
        }
    }

    // Helper para buscar cuenta en la lista del usuario
    private Cuenta buscarCuenta(String nombre) {
        for (Cuenta c : usuario.getCuentas()) {
            if (c.getNombre().equals(nombre)) return c;
        }
        return null;
    }
}