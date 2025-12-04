package com.spendo.sistemaArchivos;

import com.spendo.core.*;
import com.spendo.enums.CategoriaTransferencia;
import com.spendo.enums.CategoriaGasto;
import com.spendo.enums.CategoriaIngreso;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminArchivos {

    private final String RUTAPersonal = "database/carpetaUsuarios/";

    public void guardarRegistroFile(Usuario usuario, Registro registro) {
        String rutaRegistros = RUTAPersonal + usuario.getUsername() + "/registros.csv";

        try (FileWriter fw = new FileWriter(rutaRegistros, true)) {

            String tipo = "";
            String monto = String.valueOf(registro.getMonto());
            String categoria = "N/A";  // <<--- antes fallaba aquí
            String fecha = registro.getFecha().toString();
            String cuentaOrigen = "N/A";
            String cuentaDestino = "N/A";

            if (registro instanceof Transferencia t) {
                tipo = "Transferencia";
                categoria = t.getCategoria().name();
                cuentaOrigen = t.getCuentaOrigen().getNombre();
                cuentaDestino = t.getCuentaDestino().getNombre();

            } else if (registro instanceof Gasto g) {
                tipo = "Gasto";
                categoria = g.getCategoria().name();
                cuentaOrigen = g.getCuenta().getNombre();

            } else if (registro instanceof Ingreso i) {
                tipo = "Ingreso";
                categoria = i.getCategoria().name();
                cuentaDestino = i.getCuenta().getNombre();
            }

            fw.write(tipo + "," + monto + "," + categoria + "," + fecha + "," + cuentaOrigen + "," + cuentaDestino + "\n");

        } catch (Exception e) {
            System.out.println("Error. No se pudo guardar el registro");
        }

        actualizarCuentasFile(usuario);
    }

    // Actualiza cuentas.csv (sobrescribe)
    public void actualizarCuentasFile(Usuario usuario) {
        String rutaCuentas = RUTAPersonal + usuario.getUsername() + "/cuentas.csv";

        try (FileWriter fw = new FileWriter(rutaCuentas, false)) {
            fw.write("NOMBRE;BALANCE\n");
            for (Cuenta cuenta : usuario.getCuentas()) {
                fw.write(cuenta.getNombre() + "," + cuenta.getBalance() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error. No se pudo actualizar cuentas.csv");
        }
    }

    // Cargar cuentas desde cuentas.csv
    public List<Cuenta> cargarCuentas(String username) {
        List<Cuenta> cuentasCargadas = new ArrayList<>();
        String rutaCuentas = RUTAPersonal + username + "/cuentas.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(rutaCuentas))) {
            br.readLine(); // saltar encabezado
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

    // Cargar registros: lee registros.csv y añade objetos a las cuentas en RAM (no reaplica)
    public void cargarRegistros(String username, Usuario usuario) {
        String ruta = RUTAPersonal + username + "/registros.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            br.readLine(); // saltar encabezado
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 6) continue;

                String tipo = datos[0].trim();
                double monto = Double.parseDouble(datos[1].trim());
                String categoriaStr = datos[2].trim();
                LocalDateTime fecha = LocalDateTime.parse(datos[3].trim());

                String origen = datos[4].trim();
                String destino = datos[5].trim();

                Cuenta cuentaOrigen = findCuentaByName(usuario, origen);
                Cuenta cuentaDestino = findCuentaByName(usuario, destino);

                switch (tipo) {

                    case "Gasto":
                        try {
                            CategoriaGasto catGasto = CategoriaGasto.valueOf(categoriaStr);
                            if (cuentaOrigen != null) {
                                Registro g = new Gasto(monto, fecha, catGasto, cuentaOrigen);
                                cuentaOrigen.addRegistro(g);
                            }
                        } catch (Exception e) {
                            System.out.println("WARN: categoría de gasto inválida -> " + categoriaStr);
                        }
                        break;

                    case "Ingreso":
                        try {
                            CategoriaIngreso catIngreso = CategoriaIngreso.valueOf(categoriaStr);
                            if (cuentaDestino != null) {
                                Registro i = new Ingreso(monto, fecha, catIngreso, cuentaDestino);
                                cuentaDestino.addRegistro(i);
                            }
                        } catch (Exception e) {
                            System.out.println("WARN: categoría de ingreso inválida -> " + categoriaStr);
                        }
                        break;

                    case "Transferencia":
                        try {
                            CategoriaTransferencia catTrans = CategoriaTransferencia.valueOf(categoriaStr);
                            if (cuentaOrigen != null && cuentaDestino != null) {
                                Registro t = new Transferencia(monto, fecha, catTrans, cuentaOrigen, cuentaDestino);
                                cuentaOrigen.addRegistro(t);
                                cuentaDestino.addRegistro(t);
                            }
                        } catch (Exception e) {
                            System.out.println("WARN: categoría de transferencia inválida -> " + categoriaStr);
                        }
                        break;

                    default:
                        System.out.println("WARN: tipo desconocido -> " + tipo);
                }
            }

        } catch (IOException e) {
            System.out.println("ERROR: No se pudieron cargar los registros del archivo.");
        }
    }


    // Cargar usuario completo: crea Usuario (username como username y nombreCompleto vacío), carga cuentas y registros
    public Usuario cargarUsuarioCompleto(String username) {
        Usuario usuario = new Usuario(username, username, "");

        // Cargar cuentas y agregarlas al usuario
        List<Cuenta> cuentas = cargarCuentas(username);
        for (Cuenta c : cuentas) {
            usuario.addCuenta(c);
        }

        // Cargar registros y agregarlos a las cuentas
        cargarRegistros(username, usuario);

        return usuario;
    }

    // Buscar cuenta por nombre en las cuentas del usuario
    private Cuenta findCuentaByName(Usuario usuario, String nombre) {
        if (nombre == null) return null;
        for (Cuenta c : usuario.getCuentas()) {
            if (c.getNombre().equals(nombre)) return c;
        }
        return null;
    }
}
