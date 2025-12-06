package com.spendo.sistemaArchivos;

import java.io.*;
import java.nio.file.*;

public class ArchivoUsuarios {

    private final String RUTAUSUARIO = "database/usuarios.csv";

    /**
     * Verifica si un usuario ya existe en el archivo usuarios.csv.
     *
     * @param nombre Nombre del usuario a buscar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean usuarioExiste(String nombre) {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTAUSUARIO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 1 && datos[0].equals(nombre)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: No se pudo leer database de usuarios");
        }
        return false;
    }

    /**
     * Obtiene la contraseña almacenada para un usuario.
     *
     * @param nombre Nombre del usuario.
     * @return Contraseña asociada o null si no se encuentra el usuario.
     */
    public String obtenerPasswordDeUsuario(String nombre) {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTAUSUARIO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 2 && datos[0].equals(nombre)) {
                    return datos[1];
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: No se pudo leer usuarios.csv");
        }
        return null;
    }

    /**
     * Valida si la contraseña ingresada coincide con la registrada para el usuario.
     *
     * @param nombre Nombre del usuario.
     * @param passwordIngresada Contraseña proporcionada por el usuario.
     * @return true si las credenciales son correctas, false si no.
     */
    public boolean validarCredenciales(String nombre, String passwordIngresada) {
        String passCorrecta = obtenerPasswordDeUsuario(nombre);
        if (passCorrecta == null) return false; // usuario no existe
        return passCorrecta.equals(passwordIngresada);
    }

    /**
     * Registra un nuevo usuario creando su entrada en usuarios.csv
     * y generando su carpeta individual con archivos de cuentas y registros.
     *
     * @param nombre Nombre del nuevo usuario.
     * @param password Contraseña asociada al nuevo usuario.
     */
    public void registrarUsuarioFile(String nombre, String password) {
        try {
            FileWriter usuarioWriter = new FileWriter(RUTAUSUARIO, true);
            usuarioWriter.write(nombre + "," + password + "\n");
            usuarioWriter.close();

            String rutaCarpeta = "database/carpetaUsuarios/" + nombre;
            Files.createDirectories(Paths.get(rutaCarpeta));

            FileWriter fwCuentas = new FileWriter(rutaCarpeta + "/cuentas.csv");
            fwCuentas.write("NOMBRE,BALANCE\n");
            fwCuentas.close();

            FileWriter fwRegistros = new FileWriter(rutaCarpeta + "/registros.csv");
            fwRegistros.write("TIPO,MONTO,CATEGORIA,FECHA,CUENTAORIGEN,CUENTADESTINO\n");
            fwRegistros.close();

            System.out.println("Usuario registrado exitosamente.");

        } catch (IOException e) {
            System.out.println("ERROR: No se pudo crear archivos del usuario.");
        }
    }
}
