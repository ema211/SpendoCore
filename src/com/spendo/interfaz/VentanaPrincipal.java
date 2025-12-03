package com.spendo.interfaz;

import com.spendo.core.*;
import com.spendo.enums.CategoriaGasto;
import com.spendo.enums.CategoriaIngreso;
import com.spendo.enums.CategoriaTransferencia;
import com.spendo.sistemaArchivos.AdminArchivos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentanaPrincipal extends JFrame {

    private final Usuario usuario;
    private final AdminArchivos admin;

    private JTable tablaCuentas;
    private JTable tablaMovimientos;
    private DefaultTableModel modeloCuentas;
    private DefaultTableModel modeloMovimientos;

    private JLabel labelSaldoTotal;
    private JComboBox<String> comboFiltroCuenta;
    private JComboBox<String> comboFiltroTipo;

    private final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public VentanaPrincipal(Usuario usuario) {
        this.usuario = usuario;
        this.admin = new AdminArchivos();

        setTitle("Spendo · Panel principal de " + usuario.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        initUI();
        cargarDatosIniciales();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(241, 244, 249));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // HEADER
        JPanel header = crearHeader();
        root.add(header, BorderLayout.NORTH);

        // Panel central: botones + tablas
        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);

        JPanel panelBotones = crearPanelBotones();
        centro.add(panelBotones, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                crearPanelCuentas(),
                crearPanelMovimientos()
        );
        split.setResizeWeight(0.30);
        split.setBorder(null);

        centro.add(split, BorderLayout.CENTER);

        root.add(centro, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel izquierda = new JPanel();
        izquierda.setOpaque(false);
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Panel principal");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(45, 51, 72));

        JLabel lblUsuario = new JLabel("Usuario: " + usuario.getUsername());
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsuario.setForeground(new Color(90, 98, 120));

        izquierda.add(lblTitulo);
        izquierda.add(Box.createVerticalStrut(4));
        izquierda.add(lblUsuario);

        JPanel derecha = new JPanel();
        derecha.setOpaque(false);
        derecha.setLayout(new BoxLayout(derecha, BoxLayout.Y_AXIS));

        labelSaldoTotal = new JLabel("Saldo total: $0.00");
        labelSaldoTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelSaldoTotal.setForeground(new Color(46, 204, 113));
        labelSaldoTotal.setHorizontalAlignment(SwingConstants.RIGHT);

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        derecha.add(labelSaldoTotal);
        derecha.add(Box.createVerticalStrut(4));
        derecha.add(btnCerrarSesion);

        header.add(izquierda, BorderLayout.WEST);
        header.add(derecha, BorderLayout.EAST);

        return header;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setOpaque(false);

        JButton btnGasto = crearBotonAccion("💸 Registrar gasto", new Color(220, 53, 69));
        JButton btnIngreso = crearBotonAccion("💰 Registrar ingreso", new Color(40, 167, 69));
        JButton btnTransfer = crearBotonAccion("🔄 Transferencia", new Color(255, 193, 7));
        JButton btnCuentas = crearBotonAccion("🏦 Administrar cuentas", new Color(23, 162, 184));

        btnGasto.addActionListener(e -> mostrarDialogoGasto());
        btnIngreso.addActionListener(e -> mostrarDialogoIngreso());
        btnTransfer.addActionListener(e -> mostrarDialogoTransferencia());
        btnCuentas.addActionListener(e -> abrirVentanaCuentas());

        panel.add(btnGasto);
        panel.add(btnIngreso);
        panel.add(btnTransfer);
        panel.add(btnCuentas);

        return panel;
    }

    private JButton crearBotonAccion(String texto, Color base) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(base);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        return btn;
    }

    private JPanel crearPanelCuentas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 229, 238), 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));

        JLabel titulo = new JLabel("Cuentas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titulo.setForeground(new Color(55, 64, 90));

        panel.add(titulo, BorderLayout.NORTH);

        modeloCuentas = new DefaultTableModel(
                new Object[]{"Cuenta", "Saldo"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCuentas = new JTable(modeloCuentas);
        tablaCuentas.setRowHeight(22);
        tablaCuentas.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(tablaCuentas);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelMovimientos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 229, 238), 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));

        // Filtros
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filtros.setOpaque(false);

        filtros.add(new JLabel("Cuenta:"));
        comboFiltroCuenta = new JComboBox<>();
        filtros.add(comboFiltroCuenta);

        filtros.add(new JLabel("Tipo:"));
        comboFiltroTipo = new JComboBox<>(new String[]{"Todos", "Gasto", "Ingreso", "Transferencia"});
        filtros.add(comboFiltroTipo);

        comboFiltroCuenta.addActionListener(e -> actualizarTablaMovimientos());
        comboFiltroTipo.addActionListener(e -> actualizarTablaMovimientos());

        panel.add(filtros, BorderLayout.NORTH);

        // Tabla
        modeloMovimientos = new DefaultTableModel(
                new Object[]{"Fecha", "Tipo", "Categoría", "Monto", "Cuenta origen", "Cuenta destino"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaMovimientos = new JTable(modeloMovimientos);
        tablaMovimientos.setRowHeight(22);
        tablaMovimientos.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(tablaMovimientos);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void cargarDatosIniciales() {
        actualizarSaldoTotal();
        recargarTablaCuentas();
        recargarFiltroCuentas();
        actualizarTablaMovimientos();
    }

    private void actualizarSaldoTotal() {
        double total = 0.0;
        for (Cuenta c : usuario.getCuentas()) {
            total += c.getBalance();
        }

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        String texto = "Saldo total: $" + nf.format(total);
        labelSaldoTotal.setText(texto);
    }

    private void recargarTablaCuentas() {
        modeloCuentas.setRowCount(0);
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        for (Cuenta c : usuario.getCuentas()) {
            modeloCuentas.addRow(new Object[]{
                    c.getNombre(),
                    nf.format(c.getBalance())
            });
        }
    }

    private void recargarFiltroCuentas() {
        comboFiltroCuenta.removeAllItems();
        comboFiltroCuenta.addItem("Todas las cuentas");
        for (Cuenta c : usuario.getCuentas()) {
            comboFiltroCuenta.addItem(c.getNombre());
        }
    }

    private void actualizarTablaMovimientos() {
        modeloMovimientos.setRowCount(0);

        String filtroCuenta = (String) comboFiltroCuenta.getSelectedItem();
        String filtroTipo = (String) comboFiltroTipo.getSelectedItem();

        boolean filtrarCuenta = filtroCuenta != null && !"Todas las cuentas".equals(filtroCuenta);
        boolean filtrarTipo = filtroTipo != null && !"Todos".equals(filtroTipo);

        for (Cuenta cuenta : usuario.getCuentas()) {
            for (Registro r : cuenta.getRegistros()) {
                String tipo = "";
                String categoria = "";
                String cuentaOrigen = "";
                String cuentaDestino = "";

                if (r instanceof Gasto g) {
                    tipo = "Gasto";
                    categoria = g.getCategoria().name();
                    cuentaOrigen = g.getCuenta().getNombre();
                } else if (r instanceof Ingreso i) {
                    tipo = "Ingreso";
                    categoria = i.getCategoria().name();
                    cuentaDestino = i.getCuenta().getNombre();
                } else if (r instanceof Transferencia t) {
                    tipo = "Transferencia";
                    categoria = t.getCategoria().name();
                    cuentaOrigen = t.getCuentaOrigen().getNombre();
                    cuentaDestino = t.getCuentaDestino().getNombre();
                }

                // Filtrado por tipo
                if (filtrarTipo && !tipo.equals(filtroTipo)) {
                    continue;
                }

                // Filtrado por cuenta (si coincide con origen o destino)
                if (filtrarCuenta) {
                    boolean coincide =
                            filtroCuenta.equals(cuentaOrigen) ||
                                    filtroCuenta.equals(cuentaDestino);
                    if (!coincide) {
                        continue;
                    }
                }

                LocalDateTime fecha = r.getFecha();
                String fechaStr = fecha.format(FORMATO_FECHA);

                modeloMovimientos.addRow(new Object[]{
                        fechaStr,
                        tipo,
                        categoria,
                        String.format("$%,.2f", r.getMonto()),
                        cuentaOrigen,
                        cuentaDestino
                });
            }
        }
    }

    // === Diálogos de registro ===

    private void mostrarDialogoGasto() {
        if (usuario.getCuentas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay cuentas registradas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<Cuenta> comboCuenta = new JComboBox<>(usuario.getCuentas().toArray(new Cuenta[0]));
        JComboBox<CategoriaGasto> comboCategoria = new JComboBox<>(CategoriaGasto.values());
        JFormattedTextField txtMonto = new JFormattedTextField(NumberFormat.getNumberInstance());
        txtMonto.setColumns(10);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Cuenta:"), gbc);
        gbc.gridx = 1;
        panel.add(comboCuenta, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        panel.add(comboCategoria, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Monto:"), gbc);
        gbc.gridx = 1;
        panel.add(txtMonto, gbc);

        int res = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Registrar gasto",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (res == JOptionPane.OK_OPTION) {
            try {
                double monto = ((Number) txtMonto.getValue()).doubleValue();
                Cuenta cuenta = (Cuenta) comboCuenta.getSelectedItem();
                CategoriaGasto cat = (CategoriaGasto) comboCategoria.getSelectedItem();

                Registro r = new Gasto(monto, LocalDateTime.now(), cat, cuenta);
                if (!r.aplicar()) {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo aplicar el gasto (fondos insuficientes).",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                admin.guardarRegistroFile(usuario, r);

                actualizarSaldoTotal();
                recargarTablaCuentas();
                actualizarTablaMovimientos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error en los datos: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoIngreso() {
        if (usuario.getCuentas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay cuentas registradas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<Cuenta> comboCuenta = new JComboBox<>(usuario.getCuentas().toArray(new Cuenta[0]));
        JComboBox<CategoriaIngreso> comboCategoria = new JComboBox<>(CategoriaIngreso.values());
        JFormattedTextField txtMonto = new JFormattedTextField(NumberFormat.getNumberInstance());
        txtMonto.setColumns(10);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Cuenta:"), gbc);
        gbc.gridx = 1;
        panel.add(comboCuenta, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        panel.add(comboCategoria, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Monto:"), gbc);
        gbc.gridx = 1;
        panel.add(txtMonto, gbc);

        int res = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Registrar ingreso",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (res == JOptionPane.OK_OPTION) {
            try {
                double monto = ((Number) txtMonto.getValue()).doubleValue();
                Cuenta cuenta = (Cuenta) comboCuenta.getSelectedItem();
                CategoriaIngreso cat = (CategoriaIngreso) comboCategoria.getSelectedItem();

                Registro r = new Ingreso(monto, LocalDateTime.now(), cat, cuenta);
                if (!r.aplicar()) {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo aplicar el ingreso.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                admin.guardarRegistroFile(usuario, r);

                actualizarSaldoTotal();
                recargarTablaCuentas();
                actualizarTablaMovimientos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error en los datos: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoTransferencia() {
        if (usuario.getCuentas().size() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Necesitas al menos dos cuentas para realizar una transferencia.",
                    "No es posible transferir",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<Cuenta> comboOrigen = new JComboBox<>(usuario.getCuentas().toArray(new Cuenta[0]));
        JComboBox<Cuenta> comboDestino = new JComboBox<>(usuario.getCuentas().toArray(new Cuenta[0]));
        JComboBox<CategoriaTransferencia> comboCategoria = new JComboBox<>(CategoriaTransferencia.values());
        JFormattedTextField txtMonto = new JFormattedTextField(NumberFormat.getNumberInstance());
        txtMonto.setColumns(10);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Cuenta origen:"), gbc);
        gbc.gridx = 1;
        panel.add(comboOrigen, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Cuenta destino:"), gbc);
        gbc.gridx = 1;
        panel.add(comboDestino, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        panel.add(comboCategoria, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Monto:"), gbc);
        gbc.gridx = 1;
        panel.add(txtMonto, gbc);

        int res = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Registrar transferencia",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (res == JOptionPane.OK_OPTION) {
            try {
                double monto = ((Number) txtMonto.getValue()).doubleValue();
                Cuenta origen = (Cuenta) comboOrigen.getSelectedItem();
                Cuenta destino = (Cuenta) comboDestino.getSelectedItem();
                CategoriaTransferencia cat = (CategoriaTransferencia) comboCategoria.getSelectedItem();

                if (origen == null || destino == null || origen == destino) {
                    JOptionPane.showMessageDialog(this,
                            "Selecciona cuentas de origen y destino distintas.",
                            "Datos inválidos",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Registro r = new Transferencia(monto, LocalDateTime.now(), cat, origen, destino);
                if (!r.aplicar()) {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo aplicar la transferencia (fondos insuficientes).",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                admin.guardarRegistroFile(usuario, r);

                actualizarSaldoTotal();
                recargarTablaCuentas();
                actualizarTablaMovimientos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error en los datos: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirVentanaCuentas() {
        VentanaCuentas dlg = new VentanaCuentas(this, usuario, admin);
        dlg.setVisible(true);
        actualizarSaldoTotal();
        recargarTablaCuentas();
        recargarFiltroCuentas();
        actualizarTablaMovimientos();
    }

    private void cerrarSesion() {
        dispose();
        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}
