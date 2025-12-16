package com.spendo.core;

import java.time.LocalDateTime;

public abstract class Registro {
    private double monto;
    private LocalDateTime fecha;

    /**
     * Constructor
     * @param monto : monto del registro
     * @param fecha : fecha del registro
     */
    public Registro(double monto, LocalDateTime fecha) {
        this.monto = monto;
        this.fecha = fecha;
    }

    /**
     * Constructor con fecha actual de registro
     * @param monto : monto del registro
     */
    public Registro(double monto) {
        this(monto, LocalDateTime.now());
    }

    //getters
    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }


    /**
     * Aplica el efecto del registro en la(s) cuenta(s) asociada(s).
     *
     * @return true si la operación se aplicó correctamente.
     */
    public abstract void aplicar();


    /**
     * Revierte el efecto del registro en la(s) cuenta(s) asociada(s).
     */
    public abstract void revertir();

}
