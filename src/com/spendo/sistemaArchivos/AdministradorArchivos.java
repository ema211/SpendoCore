package com.spendo.sistemaArchivos;

import com.spendo.core.*;

import java.io.FileWriter;

public class AdministradorArchivos {

    private final String RUTAPersonal = "database/carpetaUsuarios/";

    public void guardarRegistroFile(Usuario usuario, Registro registro){
        String rutaRegistros = RUTAPersonal + usuario.getUsername() + "/registros.csv";

        try (FileWriter fw = new FileWriter(rutaRegistros, true)) {

            // Variables
            String tipo = "";
            String monto = String.valueOf(registro.getMonto());
            String categoria = registro.getCategoria().toString();
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

    /*
    * guarda y lee las cuentas en un archivo
    * guarda y lee transacciones
    */
}
