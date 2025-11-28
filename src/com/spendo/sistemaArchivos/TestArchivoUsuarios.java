package com.spendo.sistemaArchivos;

import java.util.Scanner;

public class TestArchivoUsuarios {

    public static void main(String[] args) {

        ArchivoUsuarios archivoUsuarios = new ArchivoUsuarios();
        Scanner sc = new Scanner(System.in);

        System.out.print("Ingresa nombre de usuario: ");
        String nombre = sc.nextLine().trim();

        // Si el usuario NO existe → registrarlo
        if (!archivoUsuarios.usuarioExiste(nombre)) {

            System.out.println("Usuario no existe. Registrándolo...");

            System.out.print("Crea una contraseña: ");
            String password = sc.nextLine().trim();

            archivoUsuarios.registrarUsuarioFile(nombre, password);
            System.out.println("¡Registro completado!");

        } else {
            // Usuario existe → validar contraseña

            System.out.print("Ingresa tu contraseña: ");
            String passwordIngresada = sc.nextLine().trim();

            if (archivoUsuarios.validarCredenciales(nombre, passwordIngresada)) {
                System.out.println("¡Login exitoso!");
            } else {
                System.out.println("Contraseña incorrecta. Acceso denegado.");
            }
        }

        sc.close();
    }
}
