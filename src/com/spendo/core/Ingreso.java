package com.spendo.core;

import com.spendo.core.exceptions.CuentaNoEncontradaException;
import com.spendo.core.exceptions.OperacionInvalidaException;
import com.spendo.enums.CategoriaIngreso;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ingreso extends Registro {
    private Cuenta cuenta;
    private CategoriaIngreso categoria;

    /**
     * Constructor
     * @param monto : cantidad a aumentar
     * @param fecha : fecha del registro
     * @param categoria : categoria asociada
     * @param cuenta : cuenta a la que se agregara dinero
     * @throws CuentaNoEncontradaException si la cuenta es nula
     * @throws OperacionInvalidaException si el monto es menor o igual a cero,
     *         la fecha es nula o el id es nulo
     */
    public Ingreso(double monto, LocalDateTime fecha, UUID id, CategoriaIngreso categoria, Cuenta cuenta) {
        super(monto,fecha,id);

        if ( cuenta == null ) {
            throw new CuentaNoEncontradaException("Cuenta no encontrada al crear el ingreso");
        }

        this.cuenta = cuenta;
        this.categoria = categoria;
    }

    /**
     * Constructor
     * Se crea con un id autogenerado
     * @param monto     : cantidad a restar
     * @param fecha     : fecha del registro
     * @param categoria : categoria asociada
     * @param cuenta    : cuenta a la que se le quitara el dinero
     * @throws CuentaNoEncontradaException si la cuenta es nula
     * @throws OperacionInvalidaException si el monto es menor o igual a cero o si la fecha es nula
     */
    public Ingreso(double monto, LocalDateTime fecha, CategoriaIngreso categoria, Cuenta cuenta) {
        this(monto,fecha,UUID.randomUUID(),categoria,cuenta);
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

    /** Getter para obtener la cuenta destino
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
