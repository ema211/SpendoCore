package com.spendo.core;

import com.spendo.core.exceptions.CuentaNoEncontradaException;
import com.spendo.core.exceptions.OperacionInvalidaException;
import com.spendo.enums.CategoriaTransferencia;

import java.time.LocalDateTime;

public class Transferencia extends  Registro {
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    private CategoriaTransferencia categoria;

    /**
     * Crea una transferencia entre dos cuentas.
     *
     * @param monto cantidad a transferir
     * @param fecha fecha del registro
     * @param categoria categoría asociada a la transferencia
     * @param cuentaOrigen cuenta desde la cual se retira el dinero
     * @param cuentaDestino cuenta a la cual se deposita el dinero
     *
     * @throws CuentaNoEncontradaException si la cuenta de origen o destino es nula
     * @throws OperacionInvalidaException si la cuenta de origen y destino son la misma
     */
    public Transferencia(double monto, LocalDateTime fecha, CategoriaTransferencia categoria,
                         Cuenta cuentaOrigen, Cuenta cuentaDestino){

        super(monto,fecha);
        if (cuentaOrigen == null || cuentaDestino == null){
            throw new CuentaNoEncontradaException("Cuenta de origen o destino no encontrada");
        }
        if (cuentaOrigen == cuentaDestino){
            throw new OperacionInvalidaException("Transferencia a la misma cuenta");
        }
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.categoria = categoria;
    }

    /**
     * Crea una transferencia con la fecha actual del sistema.
     *
     * @param monto cantidad a transferir
     * @param categoria categoría asociada a la transferencia
     * @param cuentaOrigen cuenta desde la cual se retira el dinero
     * @param cuentaDestino cuenta a la cual se deposita el dinero
     *
     * @throws CuentaNoEncontradaException si la cuenta de origen o destino es nula
     * @throws OperacionInvalidaException si la cuenta de origen y destino son la misma
     */

    public Transferencia (double monto, CategoriaTransferencia categoria, Cuenta cuentaOrigen,
                          Cuenta cuentaDestino){
        super(monto);
        if (cuentaOrigen == null || cuentaDestino == null){
            throw new CuentaNoEncontradaException("Cuenta de origen o destino no encontrada");
        }
        if (cuentaOrigen == cuentaDestino){
            throw new OperacionInvalidaException("Transferencia a la misma cuenta");
        }
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.categoria = categoria;
    }

    /**
     * Aplica la transferencia:
     * retira el monto de la cuenta de origen y lo deposita en la cuenta destino,
     * registrando la operación en ambas cuentas.
     *
     * @throws MontoInvalidoException si el monto es menor o igual a cero
     * @throws SaldoInsuficienteException si la cuenta de origen no tiene fondos suficientes
     */

    @Override
    public void aplicar() {
        this.cuentaOrigen.retirar(this.getMonto());
        this.cuentaDestino.depositar(this.getMonto());

        this.cuentaOrigen.addRegistro(this);
        this.cuentaDestino.addRegistro(this);
    }

    /**
     * Revierte la transferencia:
     * devuelve el monto a la cuenta de origen y lo retira de la cuenta destino,
     * eliminando el registro de ambas cuentas.
     *
     * @throws MontoInvalidoException si el monto es inválido
     * @throws SaldoInsuficienteException si la cuenta destino no tiene fondos suficientes
     */

    @Override
    public void revertir() {
        this.cuentaOrigen.depositar(this.getMonto());
        this.cuentaDestino.retirar(this.getMonto());

        this.cuentaOrigen.getRegistros().remove(this);
        this.cuentaDestino.getRegistros().remove(this);
    }

    /**
     * @return la cuenta de origen de la transferencia
     */

    public Cuenta getCuentaOrigen() {
        return this.cuentaOrigen;
    }

    /**
     * @return la cuenta de destino de la transferencia
     */

    public Cuenta getCuentaDestino() {
        return this.cuentaDestino;
    }

    /**
     * @return la categoria de la transferencia
     */
    public CategoriaTransferencia getCategoria() {
        return categoria;
    }

}
