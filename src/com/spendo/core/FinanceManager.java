package com.spendo.core;

import com.spendo.sistemaArchivos.AdminArchivos;

import java.util.ArrayList;

public class FinanceManager {
    private static FinanceManager instancia;
    private ArrayList<Usuario> almacenamiento;
    private AdminArchivos adminArchivos;

    /**
     * Constructor privado (Singleton)
     */
    private FinanceManager() {
        System.out.println("¡Creando la instancia única de FinanceManager!");
        this.almacenamiento = new ArrayList<>();
    }

    /**
     * Devuelve solo una misma instancia de la clase en cualquier momento
     * @return : Devuelve la instancia unica.
     */
    public static FinanceManager getInstance() {
        if (instancia == null) {
            // Si no existe, la creamos
            instancia = new FinanceManager();
        }
        // Si ya existe, devolvemos la que ya estaba
        return instancia;
    }

    /**
     * Registra un nuevo usuario
     * @param nuevoUsuario : El usuario a registrar
     */
    public void registrarUsuario(Usuario nuevoUsuario){
        for(Usuario usuario : almacenamiento){
            if (usuario.getUsername().equals(nuevoUsuario.getUsername())){
                System.out.println("El usuario existe en el sistema");
                return;
            }
        }
        almacenamiento.add(nuevoUsuario);
    }

    /**
     * Busca un usuario
     * @param username : username del usuario a buscar
     * @return : La referencia del usuario encontrado; null si no se encontró
     */
    public Usuario buscarUsuario(String username){
        for(Usuario usuario : almacenamiento){
            if (usuario.getUsername().equals(username)){
                return usuario;
            }
        }
        return null;
    }


}
