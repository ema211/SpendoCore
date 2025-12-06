package com.spendo.core;

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
        this.categoria = categoria;
        this.cuenta = cuenta;
    }

    /**
     * Constructor con fecha actual de registro
     *
     * @param monto     : cantidad a restar
     * @param categoria : categoria asociada
     * @param cuenta    : cuenta a la que se le quitara el dinero
     */
    public Gasto(double monto, CategoriaGasto categoria, Cuenta cuenta) {
        super(monto);
        this.categoria = categoria;
        this.cuenta = cuenta;
    }

    /**
     * Metodo abstracto heredaro de Registro
     * Aplica el gasto a la cuenta objetivo
     *
     * @return true si la operacion fue exitosa
     */
    @Override
    public boolean aplicar() {
        if (this.cuenta == null) {
            System.out.println("ERROR: cuenta nula en Gasto.aplicar()");
            return false;
        }

        if (!this.cuenta.retirar(this.getMonto())) {
            return false;
        }

        this.cuenta.addRegistro(this);
        return true;
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
