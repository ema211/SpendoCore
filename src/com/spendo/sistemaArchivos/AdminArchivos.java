package com.spendo.sistemaArchivos;

import com.spendo.core.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminArchivos {

    private final String RUTAPersonal = "database/carpetaUsuarios/";

    public void guardarRegistroFile(Usuario usuario, Registro registro){
        String rutaRegistros = RUTAPersonal + usuario.getUsername() + "/registros.csv";

        try (FileWriter fw = new FileWriter(rutaRegistros, true)) {

            // Variables
            String tipo = "";
            String monto = String.valueOf(registro.getMonto());
            String categoria = registro.getCategoria().getNombreBonito();
            String fecha = registro.getFecha().toString();
            String cuentaOrigen = "N/A";
            String cuentaDestino = "N/A";

            if (registro instanceof Transferencia) {
                tipo = "Transferencia";
                Transferencia t = (Transferencia) registro;
                cuentaOrigen = t.getCuentaOrigen().getNombre();
                cuentaDestino = t.getCuentaDestino().getNombre();

            } else if (registro instanceof Gasto) {
                tipo = "Gasto";
                Gasto g = (Gasto) registro;
                cuentaOrigen = g.getCuenta().getNombre();

            } else if (registro instanceof Ingreso) {
                tipo = "Ingreso";
                Ingreso i = (Ingreso) registro;
                // Origen se queda en "N/A"
                cuentaDestino = i.getCuenta().getNombre();

            } else {
                System.out.println("Error: Tipo de registro no identificado");
            }

            //Ya obtubimos las variables asi que las escribimos
            fw.write(tipo + "," + monto + "," + categoria + "," + fecha + "," + cuentaOrigen + "," + cuentaDestino + "\n");

        }catch (Exception e){
            System.out.println("Error. No se pudo guardar el registro");
        }
    }


    private void actualizarCuentasFile(Usuario usuario){
        String rutaCuentas = RUTAPersonal + usuario.getUsername() + "/cuentas.csv";

        //va a sobrescribir todo
        try (FileWriter fw = new FileWriter(rutaCuentas, false)) {

            for (Cuenta cuenta : usuario.getCuentas()) {
                fw.write(cuenta.getNombre() + "," + cuenta.getBalance() + "\n");
            }

        } catch (IOException e) {
            System.out.println("Error. No se pudo actualizar cuentas.csv");
        }
    }


    public List<Cuenta> cargarCuentas(String username) {
        List<Cuenta> cuentasCargadas = new ArrayList<>();
        String rutaCuentas = RUTAPersonal + username + "/cuentas.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(rutaCuentas))) {

            // saltamos la primera linea
            br.readLine();
            String linea;
            // Empezamos a leer desde la segunda línea que ya contiene datos.
            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split(",");

                if (datos.length >= 2) { //verificacion que si tenga dos datos el archivo
                    String nombreCuenta = datos[0];

                    double balance = Double.parseDouble(datos[1]);

                    Cuenta cuenta = new Cuenta(nombreCuenta, balance);
                    cuentasCargadas.add(cuenta);
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: No se pudo cargar las cuentas.");
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Archivo con formato incorrecto");
        }
        return cuentasCargadas;
    }


}
/*
 * guarda y lee las cuentas en un archivo
 * guarda y lee transacciones
 */
