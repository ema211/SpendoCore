package com.spendo.sistemaArchivos;

import com.spendo.core.FinanceManager;
import com.spendo.core.Usuario;
import com.spendo.menuPrincipal.Menu;
import java.util.Scanner;

public class TestArchivoUsuarios {

    public static void main(String[] args) {

        ArchivoUsuarios archivoUsuarios = new ArchivoUsuarios();
        AdminArchivos admin = new AdminArchivos();
        Scanner sc = new Scanner(System.in);

        System.out.print("Ingresa nombre de usuario: ");
        String nombre = sc.nextLine().trim();

        // Si no existe, registrarlo
        if (!archivoUsuarios.usuarioExiste(nombre)) {
            System.out.println("Usuario no existe. Registrándolo...");

            System.out.print("Crea una contraseña: ");
            String password = sc.nextLine().trim();

            archivoUsuarios.registrarUsuarioFile(nombre, password);
            System.out.println("¡Registro completado!");

        } else { // Si existe, validar contraseña
            System.out.print("Ingresa tu contraseña: ");
            String passwordIngresada = sc.nextLine().trim();

            if (!archivoUsuarios.validarCredenciales(nombre, passwordIngresada)) {
                System.out.println("Contraseña incorrecta. Acceso denegado.");
                sc.close();
                return;
            }
            System.out.println("¡Login exitoso!");
        }

        // Cargar usuario completo desde archivos
        Usuario usuario = admin.cargarUsuarioCompleto(nombre);

        // Registrarlo en FinanceManager
        FinanceManager fm = FinanceManager.getInstance();
        fm.setUsuarioActual(usuario);
        fm.registrarUsuario(usuario);

        // Mostrar el menú principal del sistema
        Menu menu = new Menu();
        menu.iniciar(nombre);

        sc.close();
    }
}
