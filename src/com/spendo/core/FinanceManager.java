package com.spendo.core;

import com.spendo.sistemaArchivos.AdminArchivos;

import java.util.ArrayList;

public class FinanceManager {
    private static FinanceManager instancia;
    private ArrayList<Usuario> almacenamiento;
    private AdminArchivos adminArchivos;

    // usuario actual (opcional para sesión)
    private Usuario usuarioActual;

    private FinanceManager() {
        System.out.println("¡Creando la instancia única de FinanceManager!");
        this.almacenamiento = new ArrayList<>();
        this.adminArchivos = new AdminArchivos();
    }

    public static FinanceManager getInstance() {
        if (instancia == null) {
            instancia = new FinanceManager();
        }
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

    // Setter/getter para usuario de sesión
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}
