package com.spendo.cuentas;

import com.spendo.enums.Categoria;

import java.time.LocalDateTime;

public abstract class Registro {

    private double monto;
    private LocalDateTime fecha;
    private Categoria categoria;
    private String descripcion;

    public Registro(double monto, LocalDateTime fecha, Categoria categoria) {
        if (monto <= 0) {
            System.out.println("¡Error! El monto debe ser mayor que 0.");
            this.monto = 0;
        } else {
            this.monto = monto;
        }

        this.fecha = fecha;
        this.categoria = categoria;
    }

    public Registro(double monto, Categoria categoria) {
        this(monto, LocalDateTime.now(), categoria);
    }

    public double getMonto() {
        return this.monto;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public abstract boolean aplicar();

}
