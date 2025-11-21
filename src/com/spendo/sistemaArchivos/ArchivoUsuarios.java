package com.spendo.sistemaArchivos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.nio.file.*;

public class ArchivoUsuarios {

    // Se encarga de manejar el archivo usuarios
    // Verifica la información del login:
    // Si el usuario no existe se encargara de registrarlo en el archivo de usuarios y crear su carpeta personal con su cuentas.csv y registros.csv

    private final String RUTAUSUARIO = "database/usuarios.csv";

    public boolean encontrarUsuarioFile(String nombre) {

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


    public void registrarUsuarioFile(String nombre, String password) {

        try {
            // Agregamos el nombre y contraseña a usuarios.csv
            FileWriter usuarioWriter = new FileWriter(RUTAUSUARIO, true);
            usuarioWriter.write(nombre + "," + password + "\n");
            usuarioWriter.close();

            // Guarda la carpeta del usuario en carpetaUsuarios. Si esta carpeta general no existe la crea
            String rutaCarpeta = "database/carpetaUsuarios/" + nombre;
            Files.createDirectories(Paths.get(rutaCarpeta));

            // Crea cuentas.csv
            File cuentas = new File(rutaCarpeta + "/cuentas.csv");
            cuentas.createNewFile();

            //Formato
            FileWriter fwCuentas = new FileWriter(rutaCarpeta + "/cuentas.csv");
            fwCuentas.write("NOMBRE,BALANCE\n");
            fwCuentas.close();

            // Crear registros.csv
            File registros = new File(rutaCarpeta + "/registros.csv");
            registros.createNewFile();

            //Formato
            FileWriter fwRegistros = new FileWriter(rutaCarpeta + "/registros.csv");
            fwRegistros.write("TIPO,MONTO,CATEGORIA,FECHA,CUENTAORIGEN,CUENTADESTINO\n");
            fwRegistros.close();

            System.out.println("Se registro tu usuario exitosamente");

        } catch (IOException e) {
            System.out.println("ERROR: No se pudo crear archivos.csv para este usuario");
        }
    }

}


