package com.spendo.interfaz;

import com.spendo.core.Cuenta;
import com.spendo.core.Usuario;
import com.spendo.sistemaArchivos.AdminArchivos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;

public class VentanaCuentas extends JDialog {

    private final Usuario usuario;
    private final AdminArchivos admin;

    private JTable tablaCuentas;
    private DefaultTableModel modeloCuentas;

    public VentanaCuentas(Frame owner, Usuario usuario, AdminArchivos admin) {
        super(owner, "Cuentas de " + usuario.getUsername(), true);
        this.usuario = usuario;
        this.admin = admin;

        setSize(500, 400);
        setLocationRelativeTo(owner);
        setMinimumSize(new Dimension(420, 320));

        initUI();
        cargarCuentas();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.setBackground(new Color(241, 244, 249));

        JLabel titulo = new JLabel("Cuentas del usuario");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(new Color(45, 51, 72));

        root.add(titulo, BorderLayout.NORTH);

        modeloCuentas = new DefaultTableModel(
                new Object[]{"Cuenta", "Saldo"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaCuentas = new JTable(modeloCuentas);
        tablaCuentas.setRowHeight(22);
        tablaCuentas.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(tablaCuentas);
        root.add(scroll, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        botones.setOpaque(false);

        JButton btnAgregar = new JButton("Añadir cuenta");
        JButton btnEliminar = new JButton("Eliminar cuenta seleccionada");
        JButton btnCerrar = new JButton("Cerrar");

        btnAgregar.addActionListener(e -> agregarCuenta());
        btnEliminar.addActionListener(e -> eliminarCuenta());
        btnCerrar.addActionListener(e -> dispose());

        botones.add(btnAgregar);
        botones.add(btnEliminar);
        botones.add(btnCerrar);

        root.add(botones, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void cargarCuentas() {
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

    private void agregarCuenta() {
        JTextField txtNombre = new JTextField();
        JFormattedTextField txtSaldo = new JFormattedTextField(NumberFormat.getNumberInstance());
        txtSaldo.setColumns(10);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre de la cuenta:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Saldo inicial:"), gbc);
        gbc.gridx = 1;
        panel.add(txtSaldo, gbc);

        int res = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Nueva cuenta",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (res == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText().trim();
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "El nombre no puede estar vacío.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double saldo = 0.0;
                if (txtSaldo.getValue() != null) {
                    saldo = ((Number) txtSaldo.getValue()).doubleValue();
                }

                Cuenta nueva = new Cuenta(nombre, saldo);
                usuario.addCuenta(nueva);
                admin.actualizarCuentasFile(usuario);

                cargarCuentas();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error en los datos: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarCuenta() {
        int fila = tablaCuentas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una cuenta para eliminar.",
                    "Sin selección",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String nombreCuenta = (String) modeloCuentas.getValueAt(fila, 0);
        Cuenta cuenta = usuario.buscarCuenta(nombreCuenta);

        if (cuenta == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró la cuenta seleccionada.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!cuenta.getRegistros().isEmpty()) {
            int op = JOptionPane.showConfirmDialog(
                    this,
                    "La cuenta tiene registros asociados.\n" +
                            "¿Seguro que quieres eliminarla?\n" +
                            "(Los registros se perderán de la vista; el CSV no se modifica retroactivamente).",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (op != JOptionPane.YES_OPTION) {
                return;
            }
        }

        usuario.getCuentas().remove(cuenta);
        admin.actualizarCuentasFile(usuario);
        cargarCuentas();
    }
}
