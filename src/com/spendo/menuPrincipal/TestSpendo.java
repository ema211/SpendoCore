package com.spendo.menuPrincipal;

import com.spendo.core.*;
import com.spendo.enums.Categoria;
import com.spendo.interfaz.VentanaLogin;

import static com.spendo.enums.Categoria.ALIMENTACION;

public class TestSpendo {
    public static void main(String[] args) {
       // new VentanaLogin();
        //Singleton: Obtén la instancia de FinanceManager.
        FinanceManager manager = FinanceManager.getInstance();

        //Registro: Crea un Usuario y regístralo
        Usuario usuario1 = new Usuario("Jesus Aldana", "ema211", "nose2");
        manager.registrarUsuario(usuario1);

        //Búsqueda: Busca ese usuario con buscarUsuario y guárdalo en una variable.
        Usuario usuarioEncontrado = manager.buscarUsuario("ema211");

        // Validacion rapida para no romper todo si es null
        if(usuarioEncontrado == null) {
            System.out.println("Error: Usuario no encontrado");
            return;
        }

        System.out.println("Usuario encontrado: " + usuarioEncontrado.getNombreCompleto());

        //Setup Cuenta: A ese usuario encontrado, agrégale una Cuenta
        Cuenta cuenta1 = new Cuenta("Cash",1000);
        usuarioEncontrado.addCuenta(cuenta1);

        //Acción: Crea un Gasto de 200 pesos asociado a esa cuenta y ejecútalo (.aplicar()).
        Gasto gasto = new Gasto(200, ALIMENTACION, cuenta1);

        boolean exito = gasto.aplicar();
        if(exito) {
            System.out.println("Gasto aplicado correctamente.");
        } else {
            System.out.println("Error al aplicar gasto.");
        }

        //Verdad: Imprime el saldo final de la cuenta. (Debería ser 800.0).
        System.out.println(cuenta1.getBalance());

    }
}
