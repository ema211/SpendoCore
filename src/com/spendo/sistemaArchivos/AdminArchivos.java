package com.spendo.sistemaArchivos;

import com.spendo.core.*;
import com.spendo.enums.Categoria;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminArchivos {

    private final String RUTAPersonal = "database/carpetaUsuarios/";

    // Guarda un registro (sigue usando Categoria.name())
    public void guardarRegistroFile(Usuario usuario, Registro registro) {
        String rutaRegistros = RUTAPersonal + usuario.getUsername() + "/registros.csv";

        try (FileWriter fw = new FileWriter(rutaRegistros, true)) {

            String tipo = "";
            String monto = String.valueOf(registro.getMonto());
            String categoria = registro.getCategoria().name();
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

            fw.write(tipo + "," + monto + "," + categoria + "," + fecha + "," + cuentaOrigen + "," + cuentaDestino + "\n");

        } catch (Exception e) {
            System.out.println("Error. No se pudo guardar el registro");
        }
    }

    // Actualiza cuentas.csv (sobrescribe)
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
                double monto;
                try {
                    monto = Double.parseDouble(datos[1].trim());
                } catch (NumberFormatException e) {
                    System.out.println("WARN: monto inválido -> " + datos[1]);
                    continue;
                }

                Categoria categoria;
                try {
                    categoria = Categoria.valueOf(datos[2].trim());
                } catch (IllegalArgumentException e) {
                    System.out.println("WARN: categoría desconocida -> " + datos[2]);
                    continue;
                }

                LocalDateTime fecha;
                try {
                    fecha = LocalDateTime.parse(datos[3].trim());
                } catch (Exception e) {
                    System.out.println("WARN: fecha inválida -> " + datos[3]);
                    continue;
                }

                String origen = datos[4].trim();
                String destino = datos[5].trim();

                Cuenta cuentaOrigen = findCuentaByName(usuario, origen);
                Cuenta cuentaDestino = findCuentaByName(usuario, destino);

                switch (tipo) {
                    case "Gasto":
                        if (cuentaOrigen != null) {
                            Registro g = new Gasto(monto, fecha, categoria, cuentaOrigen);
                            cuentaOrigen.addRegistro(g);
                        } else {
                            System.out.println("WARN: cuenta origen no encontrada para gasto -> " + origen);
                        }
                        break;

                    case "Ingreso":
                        if (cuentaDestino != null) {
                            Registro i = new Ingreso(monto, fecha, categoria, cuentaDestino);
                            cuentaDestino.addRegistro(i);
                        } else {
                            System.out.println("WARN: cuenta destino no encontrada para ingreso -> " + destino);
                        }
                        break;

                    case "Transferencia":
                        if (cuentaOrigen != null && cuentaDestino != null) {
                            Registro t = new Transferencia(monto, fecha, categoria, cuentaOrigen, cuentaDestino);
                            cuentaOrigen.addRegistro(t);
                            cuentaDestino.addRegistro(t);
                        } else {
                            System.out.println("WARN: cuentas no encontradas para transferencia -> " + origen + " / " + destino);
                        }
                        break;

                    default:
                        System.out.println("WARN: tipo desconocido en registros.csv -> " + tipo);
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: No se pudieron cargar los registros del archivo.");
        }
    }

    // Cargar usuario completo: crea Usuario (username como username y nombreCompleto vacío), carga cuentas y registros
    public Usuario cargarUsuarioCompleto(String username) {
        // Creamos Usuario con nombreCompleto = username (puedes ajustar si tienes otro constructor)
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
