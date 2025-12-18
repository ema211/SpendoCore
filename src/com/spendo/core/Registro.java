package com.spendo.core;

import com.spendo.core.exceptions.OperacionInvalidaException;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Registro {
    private double monto;
    private LocalDateTime fecha;
    private  final UUID id;

    /**
     * Constructor
     * @param monto : monto del registro
     * @param fecha : fecha del registro
     * @param id : id del registro
     * @throws OperacionInvalidaException si el monto es menor o igual a cero,
     *         la fecha es nula o el id es nulo
     */
    public Registro(double monto, LocalDateTime fecha, UUID id) {
        if ( monto < 0 || fecha == null ||  id == null){
            throw new OperacionInvalidaException("Datos inválidos para crear el registro");
        }
        this.monto = monto;
        this.fecha = fecha;
        this.id = id;
    }

    /**
     * Constructor con ID randomizado
     * @param monto : monto del registro
     * @param fecha : fecha del registro
     * @throws OperacionInvalidaException si el monto es menor o igual a cero,
     *         la fecha es nula o el id es nulo
     */
    public Registro(double monto, LocalDateTime fecha) {
        this(monto, fecha, UUID.randomUUID());
    }

    /**
     * Aplica el efecto del registro en la(s) cuenta(s) asociada(s).
     */
    public abstract void aplicar();

    /**
     * Revierte el efecto del registro en la(s) cuenta(s) asociada(s).
     */
    public abstract void revertir();


    // -------------- Getters --------------
    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public UUID getId() {
        return id;
    }
}
