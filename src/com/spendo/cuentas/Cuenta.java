package com.spendo.cuentas;
import java.time.LocalDateTime; //esto es en el caso que queramos registrar la fecha en
                                // la que se hicieron los cambios en la cuenta bancaria

<<<<<<< HEAD
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
=======
import java.util.List;
import java.util.ArrayList;

public class Cuenta {
    /*
    * define lo que es una cuenta
    * define lo que es una transaccion
    * ¿que info tiene una transaccion? etc
     */

    private String nombre;
    private double balance;

    private List<Registro> registros;

    public Cuenta(String nombre, double balance) {
        this.nombre = nombre;
        this.balance = balance;
        this.registros = new ArrayList<>();
    }

    public Cuenta (String nombre) {
        this(nombre, 0.0);
    }

    public void depositar(double monto) {
        if (monto > 0) {
            this.balance += monto;
        } else {
            System.out.println("Error: El monto a depositar debe ser positivo.");
        }
    }

    public boolean retirar(double monto) {
        if (monto <= 0) {
            System.out.println("Error: El monto a retirar debe ser positivo.");
            return false;
        }

        if (monto > this.balance) {
            System.out.println("Error: Fondos insuficientes.");
            return false;
        }

        // Si todo está bien, restamos
        this.balance -= monto;
        return true;
    }

    public void addRegistro (Registro registro){
        this.registros.add(registro);
    }

    public String getNombre() {
        return this.nombre;
    }

    public double getBalance() {
        return this.balance;
    }

    public List<Registro> getRegistros() {
        return this.registros;
    }

}
>>>>>>> 4ebe278 (avance de ema en el paquete cuentas :D)
