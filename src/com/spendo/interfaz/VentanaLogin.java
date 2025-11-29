package com.spendo.interfaz;

import com.spendo.core.FinanceManager;
import com.spendo.core.Usuario;
import com.spendo.sistemaArchivos.AdminArchivos;
import com.spendo.sistemaArchivos.ArchivoUsuarios;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private ArchivoUsuarios archivoUsuarios;
    private AdminArchivos adminArchivos;

    public VentanaLogin() {
        // Configuración básica de la ventana
        setTitle("Spendo - Iniciar Sesión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);

        // Instancias de lógica
        archivoUsuarios = new ArchivoUsuarios();
        adminArchivos = new AdminArchivos();

        initUI();
    }

    private void initUI() {
        // Usamos un Panel principal con Padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(new Color(245, 245, 250)); // Color de fondo suave

        // Título Bonito
        JLabel titleLabel = new JLabel("Spendo 💰");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(50, 50, 150)); // Azul oscuro

        // Campos
        userField = new JTextField();
        passField = new JPasswordField();

        // Botón
        JButton btnLogin = new JButton("Ingresar / Registrar");
        btnLogin.setBackground(new Color(70, 130, 180)); // Azul acero
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Acción del botón
        btnLogin.addActionListener(e -> procesarLogin());

        // Añadir todo al panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Espacio
        mainPanel.add(new JLabel("Usuario:"));
        mainPanel.add(userField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(new JLabel("Contraseña:"));
        mainPanel.add(passField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(btnLogin);

        add(mainPanel);
    }

    private void procesarLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lógica híbrida: Si no existe, registra. Si existe, valida.
        if (!archivoUsuarios.usuarioExiste(user)) {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "El usuario no existe. ¿Quieres registrarte?",
                    "Registro", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                archivoUsuarios.registrarUsuarioFile(user, pass);
                ingresarAlSistema(user);
            }
        } else {
            if (archivoUsuarios.validarCredenciales(user, pass)) {
                ingresarAlSistema(user);
            } else {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ingresarAlSistema(String username) {
        // Cargar datos COMPLETOS antes de entrar
        Usuario usuario = adminArchivos.cargarUsuarioCompleto(username);

        // Configurar Singleton
        FinanceManager.getInstance().setUsuarioActual(usuario); // ¡Asegúrate de tener este setter en FinanceManager!
        // Si no tienes setUsuarioActual, usa registrarUsuario(usuario) o similar.

        // Abrir Dashboard
        new VentanaPrincipal(usuario).setVisible(true);
        this.dispose(); // Cerrar login
    }

    public static void main(String[] args) {
        // Look and Feel moderno (Nimbus)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}
