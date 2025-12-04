package com.spendo.core;

import com.spendo.enums.CategoriaTransferencia;

import java.time.LocalDateTime;

public class Transferencia extends  Registro {
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    private CategoriaTransferencia categoria;

    /**
     * Constructor
     * @param monto : cantidad a transferir
     * @param fecha : fecha del registro
     * @param categoria : categoria asociada
     * @param cuentaOrigen : cuenta origen
     * @param cuentaDestino :  cuenta destino
     */
    public Transferencia(double monto, LocalDateTime fecha, CategoriaTransferencia categoria,
                         Cuenta cuentaOrigen, Cuenta cuentaDestino){
        super(monto,fecha);
        this.categoria = categoria;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
    }

    /**
     * Constructor con fecha actual de registro
     * @param monto : cantidad a transferir
     * @param categoria : categoria asociada
     * @param cuentaOrigen : cuenta origen
     * @param cuentaDestino :  cuenta destino
     */
    public Transferencia (double monto, CategoriaTransferencia categoria, Cuenta cuentaOrigen,
                          Cuenta cuentaDestino){
        super(monto);
        this.categoria = categoria;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
    }

    /**
     * Metodo abstracto heredaro de Registro
     * Aplica el ingreso a la cuenta objetivo y el gasto a la de origen
     * @return true si la operacion fue exitosa
     */
    @Override
    public boolean aplicar() {
        if (this.cuentaOrigen == null || this.cuentaDestino == null) {
            System.out.println("ERROR: cuenta null en Transferencia.aplicar()");
            return false;
        }

        if (!this.cuentaOrigen.retirar(this.getMonto())) {
            return false;
        }

        this.cuentaDestino.depositar(this.getMonto());
        this.cuentaOrigen.addRegistro(this);
        this.cuentaDestino.addRegistro(this);

        return true;
    }

    /**
     * Elimina la Transferencia (this objeto)
     */
    @Override
    public void revertir() {
        this.cuentaOrigen.depositar(this.getMonto());
        this.cuentaDestino.retirar(this.getMonto());

        this.cuentaOrigen.getRegistros().remove(this);
        this.cuentaDestino.getRegistros().remove(this);
    }

    public Cuenta getCuentaOrigen() {
        return this.cuentaOrigen;
    }

    public Cuenta getCuentaDestino() {
        return this.cuentaDestino;
    }

    public CategoriaTransferencia getCategoria() {
        return categoria;
    }

}
