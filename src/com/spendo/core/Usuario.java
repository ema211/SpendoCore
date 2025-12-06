package com.spendo.core;

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
     * Valida si la contraseña es la correcta
     * @param passwordIngresada: contraseña proporcionada al intentar iniciar sesion
     * @return true si la contraseña ingresada coincide, de lo contrario da false
     */
    public boolean validarPassword (String passwordIngresada) {
        return password.equals(passwordIngresada);
    }

    /**
     * Getter para el nombre completo
     * @return nombre completo en String
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     *  Getter para el username
     * @return username en tipo String
     */
    public String getUsername() {
        return username;
    }

    /**
     *  Getter para la lista de cuentas
     * @return lista de cuentas en tipo List<Cuenta>
     */
    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    /**
     * Busca una cuenta del usuario por nombre
     * @param nombreCuenta nombre de la cuenta
     * @return la cuenta encontrada o null si no existe
     */
    public Cuenta buscarCuenta(String nombreCuenta) {
        for (Cuenta c : cuentas) {
            if (c.getNombre().equalsIgnoreCase(nombreCuenta.trim())) {
                return c;
            }
        }
        return null;
    }

}
