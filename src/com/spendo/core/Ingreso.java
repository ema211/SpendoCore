package com.spendo.core;

import com.spendo.enums.CategoriaIngreso;

import java.time.LocalDateTime;

public class Ingreso extends Registro {
    private Cuenta cuenta;
    private CategoriaIngreso categoria;

    /**
     * Constructor
     * @param monto : cantidad a aumentar
     * @param fecha : fecha del registro
     * @param categoria : categoria asociada
     * @param cuenta : cuenta a la que se agregara dinero
     */
    public Ingreso(double monto, LocalDateTime fecha, CategoriaIngreso categoria, Cuenta cuenta) {
        super(monto,fecha);
        this.categoria = categoria;
        this.cuenta = cuenta;
    }

    /**
     * Constructor con fecha actual de registro
     * @param monto : cantidad a aumentar
     * @param categoria : categoria asociada
     * @param cuenta : cuenta a la que se agregara dinero
     */
    public Ingreso(double monto, CategoriaIngreso categoria, Cuenta cuenta) {
        super(monto);
        this.categoria = categoria;
        this.cuenta = cuenta;
    }

    /**
     * Metodo abstracto heredaro de Registro
     * Aplica el ingreso a la cuenta objetivo
     * @return true siempre (porque recibir dinero siempre es un exito:D)
     */
    @Override
    public boolean aplicar() {
         this.cuenta.depositar(this.getMonto());
         this.cuenta.addRegistro(this);
        return true;
    }

    /**
     * Elimina el Ingreso (this objeto)
     */
    @Override
    public void revertir() {
        this.cuenta.retirar(this.getMonto());
        this.cuenta.getRegistros().remove(this);
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public CategoriaIngreso getCategoria() {
        return categoria;
    }

}
