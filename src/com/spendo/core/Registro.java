package com.spendo.core;

import com.spendo.enums.Categoria;

import java.time.LocalDateTime;

public abstract class Registro {

    private double monto;
    private LocalDateTime fecha;
    private Categoria categoria;
    private String descripcion;

    /**
     * Constructor
     * @param monto : cantidad a aumentar/restar
     * @param fecha : fecha del registro
     * @param categoria : categoria asociada
     */
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

    /**
     * Constructor con fecha actual de registro
     * @param monto : cantidad a aumentar/restar
     * @param categoria : categoria asociada
     */
    public Registro(double monto, Categoria categoria) {
        this(monto, LocalDateTime.now(), categoria);
    }

    //getters
    public double getMonto() {
        return this.monto;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    /**
     * Metodo abstracto
     * Este metodo debe de implementarse en los metodos hijos de registro
     * @return true si la operacion fue exitosa
     */
    public abstract boolean aplicar();

}
