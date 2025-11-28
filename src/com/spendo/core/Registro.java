package com.spendo.core;

import com.spendo.enums.Categoria;
import java.time.LocalDateTime;

public abstract class Registro {
    private double monto;
    private LocalDateTime fecha;
    private Categoria categoria;

    public Registro(double monto, LocalDateTime fecha, Categoria categoria) {
        this.monto = monto;
        this.fecha = fecha;
        this.categoria = categoria;
    }

    public Registro(double monto, Categoria categoria) {
        this(monto, LocalDateTime.now(), categoria);
    }

    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public abstract boolean aplicar();
}
