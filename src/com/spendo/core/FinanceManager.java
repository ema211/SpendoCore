package com.spendo.core;

import java.util.ArrayList;

public class FinanceManager {
    private static FinanceManager instancia;
    private ArrayList<Usuario> almacenamiento;

    private FinanceManager() {
        System.out.println("¡Creando la instancia única de FinanceManager!");
        this.almacenamiento = new ArrayList<>();
    }

    public static FinanceManager getInstance() {
        if (instancia == null) {
            // Si no existe, la creamos
            instancia = new FinanceManager();
        }
        // Si ya existe, devolvemos la que ya estaba
        return instancia;
    }


    public void registrarUsuario(Usuario nuevoUsuario){
        for(Usuario usuario : almacenamiento){
            if (usuario.getUsername().equals(nuevoUsuario.getUsername())){
                System.out.println("El usuario existe en el sistema");
                return;
            }
        }
        almacenamiento.add(nuevoUsuario);
    }

    public Usuario buscarUsuario(String username){
        for(Usuario usuario : almacenamiento){
            if (usuario.getUsername().equals(username)){
                return usuario;
            }
        }
        return null;
    }

}
