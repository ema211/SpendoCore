package com.spendo.cuentas;

import java.util.List;
import java.util.ArrayList;


public class Cuenta {
    /*
    * define lo que es una cuenta
    * define lo que es una transaccion
    * ¿que info tiene una transaccion? etc
     */

    private String nombre;
    private double balance;

    private List<Registro> registros;

    public Cuenta(String nombre, double balance) {
        this.nombre = nombre;
        this.balance = balance;
        this.registros = new ArrayList<>();
    }

    public Cuenta (String nombre) {
        this(nombre, 0.0);
    }

    public void depositar(double monto) {
        if (monto > 0) {
            this.balance += monto;
        } else {
            System.out.println("Error: El monto a depositar debe ser positivo.");
        }
    }

    public boolean retirar(double monto) {
        if (monto <= 0) {
            System.out.println("Error: El monto a retirar debe ser positivo.");
            return false;
        }

        if (monto > this.balance) {
            System.out.println("Error: Fondos insuficientes.");
            return false;
        }

        // Si todo está bien, restamos
        this.balance -= monto;
        return true;
    }

    public void addRegistro (Registro registro){
        this.registros.add(registro);
    }

    public String getNombre() {
        return this.nombre;
    }

    public double getBalance() {
        return this.balance;
    }

    public List<Registro> getRegistros() {
        return this.registros;
    }

}

