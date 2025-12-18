package com.spendo.core;

import com.spendo.core.exceptions.CuentaNoEncontradaException;
import com.spendo.core.exceptions.MontoInvalidoException;
import com.spendo.core.exceptions.OperacionInvalidaException;
import com.spendo.enums.CategoriaGasto;

import java.time.LocalDateTime;
import java.util.UUID;

public class Gasto extends Registro {
    private Cuenta cuenta;
    private CategoriaGasto categoria;

    /**
     * Constructor
     *
     * @param monto     : cantidad a restar
     * @param fecha     : fecha del registro
     * @param categoria : categoria asociada
     * @param cuenta    : cuenta a la que se le quitara el dinero
     * @throws CuentaNoEncontradaException si la cuenta es nula
     * @throws OperacionInvalidaException si el monto es menor o igual a cero,
     *         la fecha es nula o el id es nulo
     */
    public Gasto(double monto, LocalDateTime fecha, UUID id, CategoriaGasto categoria, Cuenta cuenta) {
        super(monto, fecha,id);

        if ( cuenta == null ) {
            throw new CuentaNoEncontradaException("Cuenta no encontrada al crear el gasto");
        }


        this.categoria = categoria;
        this.cuenta = cuenta;
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
    public Gasto(double monto, LocalDateTime fecha, CategoriaGasto categoria, Cuenta cuenta) {
        this(monto,fecha,UUID.randomUUID(),categoria,cuenta);
    }

    /**
     * Aplica el gasto a la cuenta asociada:
     * retira el monto del balance y registra la operación en la cuenta.
     * @throws MontoInvalidoException si el monto es menor o igual a cero
     */
    @Override
    public void aplicar() {
        this.cuenta.retirar(this.getMonto());
        this.cuenta.addRegistro(this);
    }

    /**
     * Revierte el gasto:
     * devuelve el monto al balance y elimina el registro de la lista de la cuenta.
     * @throws MontoInvalidoException si el monto es menor o igual a cero
     */
    @Override
    public void revertir() {
        this.cuenta.depositar(this.getMonto());
        this.cuenta.getRegistros().remove(this);
    }

    /**
     * @return la cuenta asociada al gasto
     */
    public Cuenta getCuenta() {
        return cuenta;
    }

    /**
     * Getter para obtener la categoria
     * @return la categoria
     */
    public CategoriaGasto getCategoria() {
        return categoria;
    }
}
