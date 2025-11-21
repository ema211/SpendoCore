package com.spendo.menuPrincipal;

import java.util.Scanner;

public class Login {

    private Scanner sc;
    public Login(){
        sc = new Scanner(System.in);
    }

    public String pedirNombre() {
        String nombre = "";
        boolean valido = false;

        while(!valido){
            try{
                System.out.print("Ingresa tu nombre de usuario: ");
                nombre = sc.nextLine().trim();

                if(nombre.length() <= 1){
                    System.out.println("El nombre debe tener al menos 2 caracteres.");
                    continue;
                }

                valido= true;

            } catch(Exception e){
                System.out.println("Entrada inválida. Intenta de nuevo.");
            }
        }

        return nombre;
    }

    public String pedirPassword() {

        String password = "";
        boolean valido = false;

        while(!valido){
            try{
                System.out.print("Ingresa tu contraseña: ");
                password = sc.nextLine().trim();

                if(password.length() < 4){
                    System.out.println("La contraseña debe tener al menos 4 caracteres.");
                    continue;
                }

                valido= true;

            } catch(Exception e){
                System.out.println("Entrada inválida. Intenta de nuevo.");
            }
        }
        return password;
    }
}