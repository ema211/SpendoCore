package com.spendo.sistemaArchivos;

import java.util.Scanner;

public class TestArchivoUsuarios {

    //SOLO DE PRUEBA - para que se formen bien los archivos en la database y poder checarlos!
    //OJO: Si lo corres, se agregaran los archivos para el nombre que registraste pero no se agregan automaticamente al git

    public static void main(String[] args) {

        ArchivoUsuarios archivoUsuarios = new ArchivoUsuarios();
        Scanner sc = new Scanner(System.in);

        System.out.print("Ingresa nombre de usuario para probar: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Ingresa contraseña: ");
        String password = sc.nextLine().trim();

        // Verificar si existe
        if (archivoUsuarios.encontrarUsuarioArchivo(nombre)) {
            System.out.println("El usuario '" + nombre + "' ya existe.");
        } else {
            System.out.println("Usuario no existe, registrando...");
            archivoUsuarios.registrarUsuarioArchivo(nombre, password);
        }

        sc.close();
    }

}
