package com.spendo.core.exceptions;


public class SaldoInsuficienteException extends DomainException {
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}