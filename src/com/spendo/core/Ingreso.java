package com.spendo.core;

import com.spendo.core.exceptions.CuentaNoEncontradaException;
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

        if ( cuenta == null ) {
            throw new CuentaNoEncontradaException("Cuenta no encontrada al crear el ingreso");
        }
        this.cuenta = cuenta;
        this.categoria = categoria;
    }

    /**
     * Constructor con fecha actual de registro
     * @param monto : cantidad a aumentar
     * @param categoria : categoria asociada
     * @param cuenta : cuenta a la que se agregara dinero
     */
    public Ingreso(double monto, CategoriaIngreso categoria, Cuenta cuenta) {
        super(monto);
        if ( cuenta == null ) {
            throw new CuentaNoEncontradaException("Cuenta no encontrada al crear el ingreso");
        }
        this.cuenta = cuenta;
        this.categoria = categoria;
    }

    /**
     * Metodo abstracto heredaro de Registro
     * Aplica el ingreso a la cuenta objetivo
     */
    @Override
    public void aplicar() {
         this.cuenta.depositar(this.getMonto());
         this.cuenta.addRegistro(this);
    }

    /**
     * Elimina el Ingreso (this objeto)
     */
    @Override
    public void revertir() {
        this.cuenta.retirar(this.getMonto());
        this.cuenta.getRegistros().remove(this);
    }

    /*ñ{{  * Getter para obtener la cuenta destino
     *
     * @return cuenta destino
     */
    public Cuenta getCuenta() {
        return cuenta;
    }

    /**
     * Getter para obtener la categoria
     * @return la categoria
     */
    public CategoriaIngreso getCategoria() {
        return categoria;
    }

}
