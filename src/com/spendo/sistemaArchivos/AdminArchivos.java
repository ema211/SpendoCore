package com.spendo.sistemaArchivos;

import com.spendo.core.*;
import com.spendo.enums.Categoria;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminArchivos {

    private final String RUTAPersonal = "database/carpetaUsuarios/";

    /**
     * Guarda un registro en registros.csv
     */
    public void guardarRegistroFile(Usuario usuario, Registro registro) {
        String rutaRegistros = RUTAPersonal + usuario.getUsername() + "/registros.csv";

        try (FileWriter fw = new FileWriter(rutaRegistros, true)) {

            String tipo = "";
            String monto = String.valueOf(registro.getMonto());
            String categoria = registro.getCategoria().name(); //categorias
            String fecha = registro.getFecha().toString();
            String cuentaOrigen = "N/A";
            String cuentaDestino = "N/A";

            if (registro instanceof Transferencia t) {
                tipo = "Transferencia";
                cuentaOrigen = t.getCuentaOrigen().getNombre();
                cuentaDestino = t.getCuentaDestino().getNombre();

            } else if (registro instanceof Gasto g) {
                tipo = "Gasto";
                cuentaOrigen = g.getCuenta().getNombre();

            } else if (registro instanceof Ingreso i) {
                tipo = "Ingreso";
                cuentaDestino = i.getCuenta().getNombre();
            }

            fw.write(tipo + "," + monto + "," + categoria + "," + fecha + ","
                    + cuentaOrigen + "," + cuentaDestino + "\n");

        } catch (Exception e) {
            System.out.println("Error. No se pudo guardar el registro");
        }
    }

    /**
     * Sobrescribe el archivo cuentas.csv con las cuentas actuales
     */
    private void actualizarCuentasFile(Usuario usuario) {
        String rutaCuentas = RUTAPersonal + usuario.getUsername() + "/cuentas.csv";

        try (FileWriter fw = new FileWriter(rutaCuentas, false)) {

            for (Cuenta cuenta : usuario.getCuentas()) {
                fw.write(cuenta.getNombre() + "," + cuenta.getBalance() + "\n");
            }

        } catch (IOException e) {
            System.out.println("Error. No se pudo actualizar cuentas.csv");
        }
    }

    /**
     * Carga todas las cuentas de un usuario desde cuentas.csv
     */
    public List<Cuenta> cargarCuentas(String username) {
        List<Cuenta> cuentasCargadas = new ArrayList<>();
        String rutaCuentas = RUTAPersonal + username + "/cuentas.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(rutaCuentas))) {

            br.readLine(); // Saltar encabezado

            String linea;
            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split(",");

                if (datos.length >= 2) {
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

    /**
     * Cargar registros desde registros.csv y reconstruir objetos
     */
    public void cargarRegistros(String username, Usuario usuario) {
        String ruta = RUTAPersonal + username + "/registros.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            br.readLine(); // saltar encabezado

            String linea;
            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split(",");
                if (datos.length < 6) continue;

                String tipo = datos[0];
                double monto = Double.parseDouble(datos[1]);
                Categoria categoria = Categoria.valueOf(datos[2]);
                String fechaStr = datos[3];
                String origen = datos[4];
                String destino = datos[5];

                // buscar cuentas existentes
                Cuenta cuentaOrigen = buscarCuenta(origen, usuario);
                Cuenta cuentaDestino = buscarCuenta(destino, usuario);

                Registro reg = null;

                switch (tipo) {
                    case "Gasto" -> reg = new Gasto(monto, categoria, cuentaOrigen);

                    case "Ingreso" -> reg = new Ingreso(monto, categoria, cuentaDestino);

                    case "Transferencia" ->
                            reg = new Transferencia(monto, categoria, cuentaOrigen, cuentaDestino);
                }

                if (reg != null) {
                    reg.aplicar();
                }
            }

        } catch (IOException e) {
            System.out.println("ERROR: No se pudieron cargar registros del archivo.");
        }
    }

    /**
     * Buscar una cuenta por nombre dentro del usuario
     */
    private Cuenta buscarCuenta(String nombre, Usuario usuario) {
        for (Cuenta c : usuario.getCuentas()) {
            if (c.getNombre().equals(nombre)) return c;
        }
        return null;
    }
}
