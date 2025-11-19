package com.spendo.usuarios;

import com.spendo.cuentas.Cuenta;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nombreCompleto;
    private String username;
    private String password;
    private List<Cuenta> cuentas;

    /**
     * Constructor
     * @param nombreCompleto: nombre del usuario
     * @param username: nombre de usuario para iniciar sesion
     * @param password: contraseña
     */

    public Usuario (String nombreCompleto, String username, String password) {
        this.nombreCompleto = nombreCompleto;
        this.username = username;
        this.password = password;
        this.cuentas = new ArrayList<>(); //inicializa la lista vacia donde agregaremos las cuentas del usuario
    }

    /**
     * agrega una nueva cuenta al usuario
     * @param cuenta
     */
    public void addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
    }

    /**
     *
     * @param passwordIngresada: contraseña proporcionada al intentar iniciar sesion
     * @return true si la contraseña ingresada coincide, de lo contrario da false
     */
    public boolean validarPassword (String passwordIngresada) {
        return password.equals(passwordIngresada);
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getUsername() {
        return username;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }
}
