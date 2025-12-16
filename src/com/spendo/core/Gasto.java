package com.spendo.core;

import com.spendo.core.exceptions.CuentaNoEncontradaException;
import com.spendo.core.exceptions.DomainException;
import com.spendo.core.exceptions.MontoInvalidoException;
import com.spendo.core.exceptions.SaldoInsuficienteException;
import com.spendo.enums.CategoriaGasto;

import java.time.LocalDateTime;

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
     */
    public Gasto(double monto, LocalDateTime fecha, CategoriaGasto categoria, Cuenta cuenta) {
        super(monto, fecha);

        if ( cuenta == null ) {
            throw new CuentaNoEncontradaException("Cuenta no encontrada al crear el gasto");
        }
        this.categoria = categoria;
        this.cuenta = cuenta;
    }

    /**
     * Constructor con fecha actual de registro
     * @param monto     : cantidad a restar
     * @param categoria : categoria asociada
     * @param cuenta    : cuenta a la que se le quitara el dinero
     */
    public Gasto(double monto, CategoriaGasto categoria, Cuenta cuenta) {
        super(monto);
        if ( cuenta == null ) {
            throw new CuentaNoEncontradaException("Cuenta no encontrada al crear el gasto");
        }
        this.cuenta = cuenta;
        this.categoria = categoria;
    }


    /**
     * Aplica el gasto a la cuenta asociada.
     *
     * @throws CuentaNoEncontradaException si la cuenta no existe
     * @throws MontoInvalidoException si el monto es inválido
     * @throws SaldoInsuficienteException si no hay fondos suficientes
     */
    @Override
    public void aplicar() {
        this.cuenta.retirar(this.getMonto());
        this.cuenta.addRegistro(this);
    }

    /**
     * Elimina el Gasto (this objeto)
     */
    @Override
    public void revertir() {
        this.cuenta.depositar(this.getMonto());
        this.cuenta.getRegistros().remove(this);
    }

    /**
     * Getter para obtener la cuenta destino
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
    public CategoriaGasto getCategoria() {
        return categoria;
    }
}
