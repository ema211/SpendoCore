package com.spendo.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinanceManager {
    private static FinanceManager instancia;
    private Map<String, Usuario> almacenamiento;

    private FinanceManager() {
        System.out.println("¡Creando la instancia única de FinanceManager!");
        this.almacenamiento = new HashMap<>();
    }

    public static FinanceManager getInstance() {
        if (instancia == null) {
            // Si no existe, la creamos
            instancia = new FinanceManager();
        }
        // Si ya existe, devolvemos la que ya estaba
        return instancia;
    }

}
