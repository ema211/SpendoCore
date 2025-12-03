package com.spendo.interfaz;

import com.spendo.core.Cuenta;
import com.spendo.core.FinanceManager;
import com.spendo.core.Usuario;
import com.spendo.sistemaArchivos.AdminArchivos;
import com.spendo.sistemaArchivos.ArchivoUsuarios;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class VentanaLogin extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private final ArchivoUsuarios archivoUsuarios;
    private final AdminArchivos adminArchivos;

    public VentanaLogin() {
        setTitle("Spendo · Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 380);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(520, 360));
        setResizable(false);

        archivoUsuarios = new ArchivoUsuarios();
        adminArchivos = new AdminArchivos();

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        root.setBackground(new Color(241, 244, 249));

        // Encabezado
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titulo = new JLabel("Spendo", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(new Color(45, 51, 72));

        JLabel subtitulo = new JLabel("Controla tus finanzas de forma sencilla", SwingConstants.LEFT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(90, 98, 120));

        header.add(titulo, BorderLayout.NORTH);
        header.add(subtitulo, BorderLayout.SOUTH);

        // Tarjeta central
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 229, 238), 1, true),
                new EmptyBorder(20, 24, 20, 24)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel lblBienvenida = new JLabel("Inicia sesión o crea tu cuenta");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBienvenida.setForeground(new Color(55, 64, 90));
        card.add(lblBienvenida, gbc);

        // Usuario
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(lblUsuario, gbc);

        gbc.gridx = 1;
        userField = new JTextField();
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(userField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(lblPass, gbc);

        gbc.gridx = 1;
        passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(passField, gbc);

        // Espacio
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 4, 4, 4);
        card.add(Box.createVerticalStrut(8), gbc);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);

        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSalir.addActionListener(e -> System.exit(0));

        JButton btnIngresar = new JButton("Ingresar / Registrar");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIngresar.setBackground(new Color(76, 132, 255));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.addActionListener(e -> intentarLogin());

        botones.add(btnSalir);
        botones.add(btnIngresar);

        gbc.gridy++;
        gbc.insets = new Insets(6, 4, 0, 4);
        card.add(botones, gbc);

        root.add(header, BorderLayout.NORTH);
        root.add(card, BorderLayout.CENTER);

        setContentPane(root);

        // Enter = botón de login
        getRootPane().setDefaultButton(btnIngresar);
    }

    private void intentarLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingresa usuario y contraseña.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        FinanceManager fm = FinanceManager.getInstance();
        Usuario usuario = fm.buscarUsuario(username);

        boolean existeEnDisco = archivoUsuarios.usuarioExiste(username);

        if (!existeEnDisco) {
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "El usuario \"" + username + "\" no existe.\n\n¿Deseas registrarlo?",
                    "Registrar nuevo usuario",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (opcion != JOptionPane.YES_OPTION) {
                return;
            }

            // Crear carpeta y archivos base
            archivoUsuarios.registrarUsuarioFile(username, password);

            // Crear usuario en memoria
            // Usamos username también como nombre completo por simplicidad
            usuario = new Usuario(username, username, password);
            fm.registrarUsuario(usuario);

            // Crear una cuenta base opcional
            Cuenta cuentaBase = new Cuenta("Efectivo", 0.0);
            usuario.addCuenta(cuentaBase);
            adminArchivos.actualizarCuentasFile(usuario);

        } else {
            // Usuario existe en disco; si no está en memoria lo creamos
            if (usuario == null) {
                usuario = new Usuario(username, username, password);
                fm.registrarUsuario(usuario);
            } else {
                // Validar contraseña solo contra lo que hay en memoria
                if (!usuario.validarPassword(password)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Contraseña incorrecta.",
                            "Error de autenticación",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            // Limpiar y cargar cuentas + registros desde archivos
            List<Cuenta> cuentasArchivo = adminArchivos.cargarCuentas(username);
            usuario.getCuentas().clear();
            for (Cuenta c : cuentasArchivo) {
                usuario.addCuenta(c);
            }
            adminArchivos.cargarRegistros(username, usuario);
        }

        fm.setUsuarioActual(usuario);

        // Abrir ventana principal
        Usuario finalUsuario = usuario;
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal vp = new VentanaPrincipal(finalUsuario);
            vp.setVisible(true);
        });

        dispose();
    }

    public static void main(String[] args) {
        // Look & Feel Nimbus si está disponible
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}
