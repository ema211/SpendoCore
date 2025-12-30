package com.spendo.core;

import java.util.*;

import com.spendo.core.exceptions.MontoInvalidoException;
import com.spendo.core.exceptions.OperacionInvalidaException;
import com.spendo.core.exceptions.RegistroDuplicadoException;

public class Cuenta {
    //Atributos
    private String nombre;
    private double balance;

    private Map<UUID, Registro> registros;

    /**
     * Constructor con balance en parametros
     * @param nombre : Nombre de la cuenta
     * @param balance : Balance inicial
     */
    public Cuenta(String nombre, double balance) {
        this.nombre = nombre;
        this.balance = balance;
        this.registros = new LinkedHashMap<>();
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
     * @throws MontoInvalidoException si el monto es menor o igual a cero
     */
    public void depositar(double monto) {
        if (monto > 0) {
            this.balance += monto;
        } else {
            throw new  MontoInvalidoException("Monto invalido");
        }
    }

    /**
     * Resta 'monto' cantidad al balance
     * @param monto : Cantidad a restar a balance
     * @throws MontoInvalidoException si el monto es menor o igual a cero
     */
    public void retirar(double monto) {
        if (monto <= 0) {
            throw new  MontoInvalidoException("Monto invalido");
        }

        // Si todo está bien, restamos
        this.balance -= monto;
    }

    /**
     * Agrega un nuevo registros a la lista de registros del objeto
     * @param registro : Nuevo registro
     * @throws RegistroDuplicadoException si el registro es el mismo
     */
    public void addRegistro (Registro registro){
        if (registro == null) {
            throw new OperacionInvalidaException("Registro invalido");
        }

        if (this.registros.containsKey(registro.getId())) {
            throw new RegistroDuplicadoException("Registro duplicado");
        }


        this.registros.put(registro.getId(), registro);
    }

    /**
     * Remueve un registro por su id
     * @param id identificador del registro a remover
     */
    public void removeRegistro(UUID id) {
        this.registros.remove(id);
    }

    /**
     * toString de la clase
     * @return el nombre de la cuenta en String
     */
    @Override
    public String toString() {
        return nombre;
    }

    /**
     * Getter para nombre
     * @return String del nombre
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     *  Getter del balance
     * @return  balance en tipo double
     */
    public double getBalance() {
        return this.balance;
    }

    /**
     *  Getter para la lista de registros
     * @return  una lista de registros en tipo List<Registro>
     */
    public List<Registro> getRegistros() {
        return new ArrayList<>(this.registros.values());
    }

}

