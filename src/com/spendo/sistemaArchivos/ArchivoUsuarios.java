package com.spendo.sistemaArchivos;

import java.io.*;
import java.nio.file.*;

public class ArchivoUsuarios {

    private final String RUTAUSUARIO = "database/usuarios.csv";

    /**
     * Verifica si un usuario existe por nombre
     */
    public boolean usuarioExiste(String nombre) {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTAUSUARIO))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[0].equals(nombre)) {
                    return true;
                }
            }

        } catch (IOException e) {
            System.out.println("ERROR: No se pudo leer database de usuarios");
        }
        return false;
    }

    /**
     * Obtiene la contraseña guardada del usuario
     */
    public String obtenerPasswordDeUsuario(String nombre) {

        try (BufferedReader br = new BufferedReader(new FileReader(RUTAUSUARIO))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");

                if (datos[0].equals(nombre)) {
                    return datos[1];  // contraseña guardada
                }
            }

        } catch (IOException e) {
            System.out.println("ERROR: No se pudo leer usuarios.csv");
        }

        return null;
    }

    /**
     * Valida las credenciales
     * @return true si el username existe y la contraseña coincide
     */
    public boolean validarCredenciales(String nombre, String passwordIngresada) {
        String passCorrecta = obtenerPasswordDeUsuario(nombre);

        if (passCorrecta == null) return false; // usuario no existe

        return passCorrecta.equals(passwordIngresada);
    }

    /**
     * Registrar usuario nuevo
     */
    public void registrarUsuarioFile(String nombre, String password) {
        try {
            // Registrar en usuarios.csv
            FileWriter usuarioWriter = new FileWriter(RUTAUSUARIO, true);
            usuarioWriter.write(nombre + "," + password + "\n");
            usuarioWriter.close();

            // Crear carpeta del usuario
            String rutaCarpeta = "database/carpetaUsuarios/" + nombre;
            Files.createDirectories(Paths.get(rutaCarpeta));

            // Crear cuentas.csv
            FileWriter fwCuentas = new FileWriter(rutaCarpeta + "/cuentas.csv");
            fwCuentas.write("NOMBRE,BALANCE\n");
            fwCuentas.close();

            // Crear registros.csv
            FileWriter fwRegistros = new FileWriter(rutaCarpeta + "/registros.csv");
            fwRegistros.write("TIPO,MONTO,CATEGORIA,FECHA,CUENTAORIGEN,CUENTADESTINO\n");
            fwRegistros.close();

            System.out.println("Usuario registrado exitosamente.");

        } catch (IOException e) {
            System.out.println("ERROR: No se pudo crear archivos del usuario.");
        }
    }
}
