package com.spendo.cuentas;

import com.spendo.enums.Categoria;
import java.time.LocalDateTime;

public class Transferencia extends  Registro {
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;

    /**
     * Constructor
     * @param monto : cantidad a transferir
     * @param fecha : fecha del registro
     * @param categoria : categoria asociada
     * @param cuentaOrigen : cuenta origen
     * @param cuentaDestino :  cuenta destino
     */
    public Transferencia(double monto, LocalDateTime fecha, Categoria categoria,
                         Cuenta cuentaOrigen, Cuenta cuentaDestino){
        super(monto,fecha,categoria);
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
    public Transferencia (double monto, Categoria categoria, Cuenta cuentaOrigen,
                          Cuenta cuentaDestino){
        super(monto,categoria);
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
        if (!this.cuentaOrigen.retirar(this.getMonto())){
            return false;
        }

        this.cuentaDestino.depositar(this.getMonto());

        this.cuentaOrigen.addRegistro(this);
        this.cuentaDestino.addRegistro(this);

        return true;
    }
}
