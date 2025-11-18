package com.spendo.cuentas;
import java.time.LocalDateTime;

public class Cuenta {
    private String idCuenta; //es el identificador de la cuenta
    private String nombreCuenta;
    private double saldo;

    public Cuenta (String idCuenta, String nombreCuenta, double saldoInicial){ //este es el Constructor
        this.idCuenta = idCuenta;
        this.nombreCuenta = nombreCuenta;
        this.saldo = saldoInicial; //no hay if porque vamos a manejar numeros negativos para las deudas
    }

    //metodo
    public boolean depositar(double monto){
        if (monto > 0){ //mayor que 0 porque solo se aceptan montos positivos para depositar
            double nuevoSaldo = this.saldo + monto; //se suma el monto al saldo actual
            this.saldo = nuevoSaldo; //asigna el nuevo saldo al campo de saldo del objeto
            return true; //operacion exitosa
        }
        else{
            return false; //operacion no exitosa
        }
    }

    //getters

    public String getIdCuenta() {
        return idCuenta;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    @Override
    public String toString(){
        return "Cuenta: " + this.idCuenta + " | Nombre: " + this.nombreCuenta + " | Saldo: " + this.saldo;
    }
}