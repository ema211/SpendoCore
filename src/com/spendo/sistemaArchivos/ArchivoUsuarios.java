package com.spendo.sistemaArchivos;

import java.io.*;
import java.nio.file.*;

public class ArchivoUsuarios {

    private final String RUTAUSUARIO = "database/usuarios.csv";

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

    public boolean validarCredenciales(String nombre, String passwordIngresada) {
        String passCorrecta = obtenerPasswordDeUsuario(nombre);
        if (passCorrecta == null) return false; // usuario no existe
        return passCorrecta.equals(passwordIngresada);
    }

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
