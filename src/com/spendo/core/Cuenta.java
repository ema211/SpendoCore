package com.spendo.core;

import java.util.List;
import java.util.ArrayList;


public class Cuenta {
    //Atributos
    private String nombre;
    private double balance;
    //Lista de objetos tipo registro para contabilizar registros de cada cuenta
    private List<Registro> registros;

    /**
     * Constructor con balance en parametros
     * @param nombre : Nombre de la cuenta
     * @param balance : Balance inicial
     */
    public Cuenta(String nombre, double balance) {
        this.nombre = nombre;
        this.balance = balance;
        //se inicializa una lista para cada objeto Cuenta
        this.registros = new ArrayList<>();
    }

    /**
     * Constructor solo con nombre como parametro
     * @param nombre : Nombre de la cuenta
     */
    public Cuenta (String nombre) {
        //Se llama el constructor con nombre y balance como parametros asignando balance=0
        this(nombre, 0.0);
    }

    /**
     * Aumenta 'monto' cantidad al balance
     * @param monto : Cantidad a aumentar a balance
     */
    public void depositar(double monto) {
        if (monto > 0) {
            this.balance += monto;
        } else {
            // Debug, monto debe de ser siempre positivo.
            System.out.println("Error: El monto a depositar debe ser positivo.");
        }
    }

    /**
     * Resta 'monto' cantidad al balance
     * @param monto : Cantidad a restar a balance
     * @return true si la operación fue exitosa(si el monto es positivo y si hay balance suficiente)
     */
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

    /**
     * Agrega un nuevo registros a la lista de registros del objeto
     * @param registro : Nuevo registro
     */
    public void addRegistro (Registro registro){
        this.registros.add(registro);
    }

    //Getters
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

