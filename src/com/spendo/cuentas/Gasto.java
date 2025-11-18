package com.spendo.cuentas;

import com.spendo.enums.Categoria;
import java.time.LocalDateTime;

public class Gasto extends Registro {
    private Cuenta cuenta;

    /**
     * Constructor
     * @param monto : cantidad a restar
     * @param fecha : fecha del registro
     * @param categoria : categoria asociada
     * @param cuenta : cuenta a la que se le quitara el dinero
     */
    public Gasto(double monto, LocalDateTime fecha, Categoria categoria, Cuenta cuenta) {
        super(monto,fecha,categoria);
        this.cuenta = cuenta;
    }

    /**
     * Constructor con fecha actual de registro
     * @param monto : cantidad a restar
     * @param categoria : categoria asociada
     * @param cuenta : cuenta a la que se le quitara el dinero
     */
    public Gasto (double monto, Categoria categoria, Cuenta cuenta){
        super(monto,categoria);
        this.cuenta = cuenta;
    }

    /**
     * Metodo abstracto heredaro de Registro
     * Aplica el gasto a la cuenta objetivo
     * @return true si la operacion fue exitosa
     */
    @Override
    public boolean aplicar() {
        if (!this.cuenta.retirar(this.getMonto())){
            return false;
        }
        this.cuenta.addRegistro(this);
        return true;
    }

}
