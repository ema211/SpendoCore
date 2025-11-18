package com.spendo.interfaz;
import javax.swing.*;

public class VentanaLogin extends JFrame {

    public VentanaLogin() {
        setTitle("Spendo - Login"); //titulo de la ventana de login

        setSize(400, 250); //medidas de la ventana de login

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Bienvenido a Spendo"));
        add(panel);

        setVisible(true);
    }
}
