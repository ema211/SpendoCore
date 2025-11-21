package com.spendo.sistemaArchivos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ArchivoUsuarios {

    private final String RUTA = "database/usuarios.csv";

    public boolean encontrarUsuario(String nombre) {

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA))) {

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
}
