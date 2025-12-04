package com.spendo.core;

import com.spendo.sistemaArchivos.AdminArchivos;

import java.util.ArrayList;

public class FinanceManager {
    private static FinanceManager instancia;
    private ArrayList<Usuario> almacenamiento;
    private AdminArchivos adminArchivos;

    // usuario actual (opcional para sesión)
    private Usuario usuarioActual;

    /**
     * Constructur privado para generar una sola instancia
     */
    private FinanceManager() {
        System.out.println("¡Creando la instancia única de FinanceManager!");
        this.almacenamiento = new ArrayList<>();
        this.adminArchivos = new AdminArchivos();
    }

    /**
     * Metodo para obtener la instancia unica
     * @return referencia de la instancia
     */
    public static FinanceManager getInstance() {
        if (instancia == null) {
            instancia = new FinanceManager();
        }
        return instancia;
    }

    /**
     *
     * @param nuevoUsuario
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
