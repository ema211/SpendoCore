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

    // Paleta de colores "elegante"
    private static final Color COLOR_BACKGROUND = new Color(238, 242, 248);
    private static final Color COLOR_CARD       = Color.WHITE;
    private static final Color COLOR_BORDER     = new Color(220, 224, 235);
    private static final Color COLOR_PRIMARY    = new Color(82, 109, 255);
    private static final Color COLOR_PRIMARY_D  = new Color(62, 89, 230);
    private static final Color COLOR_TEXT_MAIN  = new Color(40, 45, 68);
    private static final Color COLOR_TEXT_SOFT  = new Color(108, 117, 140);

    public VentanaLogin() {
        setTitle("Spendo · Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(540, 400);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(540, 380));
        setResizable(false);

        archivoUsuarios = new ArchivoUsuarios();
        adminArchivos = new AdminArchivos();

        initUI();
    }

    private void initUI() {
        // Root
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(20, 24, 20, 24));
        root.setBackground(COLOR_BACKGROUND);

        // Encabezado superior
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titulo = new JLabel("Spendo", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(COLOR_TEXT_MAIN);

        JLabel subtitulo = new JLabel("Controla tus finanzas de forma clara y sencilla", SwingConstants.LEFT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(COLOR_TEXT_SOFT);

        header.add(titulo, BorderLayout.NORTH);
        header.add(subtitulo, BorderLayout.SOUTH);

        // Tarjeta central
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setOpaque(false);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(20, 24, 20, 24)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Encabezado de la tarjeta
        JLabel lblBienvenida = new JLabel("Bienvenido a Spendo");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenida.setForeground(COLOR_TEXT_MAIN);
        card.add(lblBienvenida, gbc);

        gbc.gridy++;
        JLabel lblDescripcion = new JLabel("Inicia sesión o crea tu cuenta para empezar", SwingConstants.LEFT);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescripcion.setForeground(COLOR_TEXT_SOFT);
        card.add(lblDescripcion, gbc);

        // Separador visual
        gbc.gridy++;
        gbc.insets = new Insets(10, 4, 10, 4);
        JSeparator separator = new JSeparator();
        separator.setForeground(COLOR_BORDER);
        card.add(separator, gbc);

        gbc.insets = new Insets(6, 4, 6, 4);

        // Campo Usuario
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsuario.setForeground(COLOR_TEXT_MAIN);
        card.add(lblUsuario, gbc);

        gbc.gridx = 1;
        userField = new JTextField();
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                new EmptyBorder(4, 6, 4, 6)
        ));
        card.add(userField, gbc);

        // Campo Password
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPass.setForeground(COLOR_TEXT_MAIN);
        card.add(lblPass, gbc);

        gbc.gridx = 1;
        passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                new EmptyBorder(4, 6, 4, 6)
        ));
        card.add(passField, gbc);

        // Message hint
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel hint = new JLabel("Si el usuario no existe, se creará automáticamente.", SwingConstants.LEFT);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(COLOR_TEXT_SOFT);
        card.add(hint, gbc);

        // Botones
        gbc.gridy++;
        gbc.insets = new Insets(14, 4, 0, 4);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);

        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> System.exit(0));

        JButton btnIngresar = new JButton("Ingresar / Registrar");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIngresar.setBackground(COLOR_PRIMARY);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.addActionListener(e -> intentarLogin());

        // Efecto simple de hover
        btnIngresar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnIngresar.setBackground(COLOR_PRIMARY_D);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnIngresar.setBackground(COLOR_PRIMARY);
            }
        });

        botones.add(btnSalir);
        botones.add(btnIngresar);

        card.add(botones, gbc);

        cardWrapper.add(card);

        root.add(header, BorderLayout.NORTH);
        root.add(cardWrapper, BorderLayout.CENTER);

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

            // Crear usuario en memoria (username como nombre completo por simplicidad)
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
                // Validar contraseña contra lo que hay en memoria
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

            // Aquí usamos los helpers que agregaste en el core:
            // usuario.limpiarCuentas();
            // usuario.agregarCuentas(cuentasArchivo);
            // Si todavía no los llamas así, puedes adaptar el nombre.
            usuario.getCuentas().clear();
            usuario.getCuentas().addAll(cuentasArchivo);

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
