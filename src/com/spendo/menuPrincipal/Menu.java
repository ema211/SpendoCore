package com.spendo.menuPrincipal;

import com.spendo.core.*;
import com.spendo.enums.Categoria;
import com.spendo.sistemaArchivos.AdminArchivos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner sc = new Scanner(System.in);
    private final FinanceManager fm = FinanceManager.getInstance();
    private final AdminArchivos admin = new AdminArchivos();

    public void iniciar(String username) {

        // Cargar datos del usuario
        Usuario usuario = admin.cargarUsuarioCompleto(username);
        fm.setUsuarioActual(usuario);

        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("Usuario: " + usuario.getUsername());
            System.out.println("1. Ver cuentas");
            System.out.println("2. Crear nueva cuenta");
            System.out.println("3. Registrar Gasto");
            System.out.println("4. Registrar Ingreso");
            System.out.println("5. Registrar Transferencia");
            System.out.println("6. Ver movimientos por cuenta");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> mostrarCuentas(usuario);
                case 2 -> crearCuenta(usuario);
                case 3 -> registrarGasto(usuario);
                case 4 -> registrarIngreso(usuario);
                case 5 -> registrarTransferencia(usuario);
                case 6 -> mostrarMovimientos(usuario);
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción incorrecta.");
            }
        }
    }

    private void mostrarCuentas(Usuario usuario) {
        System.out.println("\n=== CUENTAS ===");
        for (Cuenta c : usuario.getCuentas()) {
            System.out.println(c.getNombre() + " → $" + c.getBalance());
        }
    }

    private void crearCuenta(Usuario usuario) {
        System.out.print("Nombre de la nueva cuenta: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Balance inicial: ");
        double balance = Double.parseDouble(sc.nextLine());

        Cuenta nueva = new Cuenta(nombre, balance);
        usuario.addCuenta(nueva);
        admin.actualizarCuentasFile(usuario);

        System.out.println("Cuenta creada correctamente.");
    }

    private void registrarGasto(Usuario usuario) {
        System.out.println("\n=== Registrar Gasto ===");

        Cuenta cuenta = solicitarCuenta(usuario, "Selecciona la cuenta de donde sale el dinero: ");
        if (cuenta == null) return;

        System.out.print("Monto: ");
        double monto = Double.parseDouble(sc.nextLine());

        Categoria categoria = elegirCategoria();

        Registro g = new Gasto(monto, LocalDateTime.now(), categoria, cuenta);

        if (g.aplicar()) {
            //DUPLICADO EN CLASE GASTO
            //cuenta.addRegistro(g);
            admin.guardarRegistroFile(usuario, g);
            System.out.println("Gasto registrado.");
        } else {
            System.out.println("Saldo insuficiente.");
        }
    }

    private void registrarIngreso(Usuario usuario) {
        System.out.println("\n=== Registrar Ingreso ===");

        Cuenta cuenta = solicitarCuenta(usuario, "Selecciona la cuenta que recibe el dinero: ");
        if (cuenta == null) return;

        System.out.print("Monto: ");
        double monto = Double.parseDouble(sc.nextLine());

        Categoria categoria = elegirCategoria();

        Registro i = new Ingreso(monto, LocalDateTime.now(), categoria, cuenta);

        i.aplicar();
        //DUPLICADO EN CLASE INGRESO
        //cuenta.addRegistro(i);
        admin.guardarRegistroFile(usuario, i);
        System.out.println("Ingreso registrado.");
    }

    private void registrarTransferencia(Usuario usuario) {
        System.out.println("\n=== Registrar Transferencia ===");

        Cuenta origen = solicitarCuenta(usuario, "Cuenta origen: ");
        Cuenta destino = solicitarCuenta(usuario, "Cuenta destino: ");

        if (origen == null || destino == null || origen == destino) {
            System.out.println("Cuentas inválidas.");
            return;
        }

        System.out.print("Monto: ");
        double monto = Double.parseDouble(sc.nextLine());

        Registro t = new Transferencia(monto, LocalDateTime.now(),
                                        Categoria.TRANSFERENCIA, origen, destino);

        if (t.aplicar()) {
            //DUPLICADO EN CLASE TRANSFERENCIA
            //origen.addRegistro(t);
            //destino.addRegistro(t);
            admin.guardarRegistroFile(usuario, t);
            System.out.println("Transferencia realizada.");
        } else {
            System.out.println("Fondos insuficientes.");
        }
    }

    private void mostrarMovimientos(Usuario usuario) {
        System.out.println("\n=== Movimientos ===");

        Cuenta cuenta = solicitarCuenta(usuario, "Selecciona la cuenta: ");
        if (cuenta == null) return;

        System.out.println("Movimientos de la cuenta " + cuenta.getNombre());
        for (Registro r : cuenta.getRegistros()) {
            System.out.println(r.getFecha() + " | " + r.getClass().getSimpleName() + " | "
                    + r.getCategoria() + " | $" + r.getMonto());
        }
    }

    private Cuenta solicitarCuenta(Usuario usuario, String mensaje) {
        System.out.println(mensaje);
        List<Cuenta> cuentas = usuario.getCuentas();

        for (int i = 0; i < cuentas.size(); i++) {
            System.out.println((i + 1) + ". " + cuentas.get(i).getNombre());
        }

        System.out.print("Opción: ");
        int opcion = Integer.parseInt(sc.nextLine());

        if (opcion < 1 || opcion > cuentas.size()) {
            System.out.println("Opción inválida.");
            return null;
        }

        return cuentas.get(opcion - 1);
    }

    private Categoria elegirCategoria() {
        System.out.println("Elige categoría:");

        Categoria[] valores = Categoria.values();
        for (int i = 0; i < valores.length; i++) {
            System.out.println((i + 1) + ". " + valores[i]);
        }

        System.out.print("Opción: ");
        int opcion = Integer.parseInt(sc.nextLine());

        if (opcion < 1 || opcion > valores.length) {
            System.out.println("Categoría inválida. Usando OTROS.");
            return Categoria.OTROS;
        }

        return valores[opcion - 1];
    }
}
