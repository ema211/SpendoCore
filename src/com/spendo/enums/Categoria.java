package com.spendo.enums;

public enum Categoria {

    // Gastos
    ALIMENTACION("Alimentación"),
    TRANSPORTE("Transporte"),
    ENTRETENIMIENTO("Entretenimiento"),
    SERVICIOS("Servicios"),
    RENTA("Renta"),

    // Ingresos
    SALARIO("Salario"),
    NEGOCIO("Negocio"),

    // General
    OTROS("Otros"),
    TRANSFERENCIA("Transferencia");

    private final String nombreBonito;

    Categoria(String nombreBonito) {
        this.nombreBonito = nombreBonito;
    }

    public String getNombreBonito() {
        return nombreBonito;
    }

    @Override
    public String toString() {
        return nombreBonito;
    }
}
