package com.spendo.interfaz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaMenu extends JPanel {

    private JButton btnDashboard;
    private JButton btnCuentas;
    private JButton btnSalir;

    public VentanaMenu() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(36, 41, 61));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Spendo");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(titulo, BorderLayout.NORTH);

        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        btnDashboard = crearBoton("🏠 Dashboard");
        btnCuentas = crearBoton("🏦 Cuentas");
        btnSalir = crearBoton("🚪 Salir");

        center.add(btnDashboard);
        center.add(Box.createVerticalStrut(8));
        center.add(btnCuentas);
        center.add(Box.createVerticalStrut(8));
        center.add(btnSalir);

        add(center, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(46, 52, 81));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void setOnDashboard(Runnable r) {
        btnDashboard.addActionListener(e -> r.run());
    }

    public void setOnCuentas(Runnable r) {
        btnCuentas.addActionListener(e -> r.run());
    }

    public void setOnSalir(Runnable r) {
        btnSalir.addActionListener(e -> r.run());
    }
}
