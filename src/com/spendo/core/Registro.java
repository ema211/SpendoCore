package com.spendo.core;

import java.time.LocalDateTime;

public abstract class Registro {
    private double monto;
    private LocalDateTime fecha;


    public Registro(double monto, LocalDateTime fecha) {
        this.monto = monto;
        this.fecha = fecha;
    }

    public Registro(double monto) {
        this(monto, LocalDateTime.now());
    }

    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public abstract boolean aplicar();
    public abstract void revertir();


}
