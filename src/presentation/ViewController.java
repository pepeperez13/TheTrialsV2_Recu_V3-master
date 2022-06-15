package presentation;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Clase encargada de mostrar todos los mensajes que tengan una forma estándar para interactuar por pantalla con el usuario
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class ViewController {
    private final Scanner scanner;

    /**
     * Inicializamos un nuevo scanner para poder leer por teclado
     */
    public ViewController() {
        scanner = new Scanner(System.in);
    }

    /**
     * Muestra el menú principal del programa
     */
    public void showMainMenu() {
        System.out.println();
        System.out.println("\tA) The Composer");
        System.out.println("\tB) This year's Conductor");
    }

    /**
     * Muestra el menú prinipal del Compositor
     */
    public void showCompositorMenu() {
        System.out.println();
        System.out.println("\t1) Manage Trials");
        System.out.println("\t2) Manage Editions");
        System.out.println();
        System.out.println("\t3) Exit");
    }

    /**
     * Muestra un submenú de pruebas del Compositor
     */
    public void showSubMenuTrials () {
        System.out.println();
        System.out.println("\ta) Create Trial");
        System.out.println("\tb) Show Trials");
        System.out.println("\tc) Delete Trial");
        System.out.println();
        System.out.println("\td) Back");
    }

    /**
     * Muestra el submenú de ediciones del Compositor
     */
    public void showMenuEditions () {
        System.out.println();
        System.out.println("\ta) Create Edition");
        System.out.println("\tb) List Editions");
        System.out.println("\tc) Duplicate Edition");
        System.out.println("\td) Delete Edition");
        System.out.println();
        System.out.println("\te) Back");
    }

    /**
     * Muestra los diferentes tipos de pruebas entre los que se puede escoger
     */
    public void showTypesTrials () {
        System.out.println("\n\t--- Trial types ---\n");
        System.out.println("\t1) Paper Publication");
        System.out.println("\t2) Master Studies");
        System.out.println("\t3) Doctoral Thesis Defense");
        System.out.println("\t4) Budget Request\n");

    }

    /**
     * Pide un entero al usuario
     * @param message Mensaje a mostrar junto con la petición
     * @return Valor entero introducido por el usuario
     */
    public int askForInteger(String message) {
        while (true) {
            try {
                System.out.print(message);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nThat's not a valid integer, try again!\n");
            } finally {
                scanner.nextLine();
            }
        }
    }

    /**
     * Pide una cadena de caracteres al usuario
     * @param message Mensaje a mostrar junto con la petición
     * @return Cadena introducida por el usuario
     */
    public String askForString (String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     * Muestra un mensaje por pantalla
     * @param message Mensaje que se quiere mostrar
     */
    public void showMessage (String message) {
        System.out.println(message);
    }

    /**
     * Muestra un mensaje por pantalla (igual que showMessage) pero sin el '\n'
     * @param message Mensaje que se quiere mostrar
     */
    public void showMessageLine (String message) { System.out.print(message); }

    /**
     * Muestra una lista de Strings con un formato de paréntesis
     * @param items Lista a mostrar
     */
    public void showList (List<String> items) {
        for (int i = 0; i < items.size(); i++) {
            System.out.println("\t " + (i+1) + ") " + items.get(i));
        }
    }

    /**
     * Muestra una lista de Strings con un formato de guion
     * @param items Lista a mostrar
     */
    public void showListGuion (List<String> items) {
        for (int i = 0; i < items.size(); i++) {
            System.out.println("\t " + (i+1) + "- " + items.get(i));
        }
    }

    /**
     * Muestra las posibles formas de persistencia
     */
    public void showStartingMenu () {
        System.out.println("I) People’s Front of Engineering (CSV)\nII) Engineering People’s Front (JSON)\n");
    }

    /**
     * Muestra el logo inicial del programa
     */
    public void showLogo () {
        System.out.print("_____ _           _____     _       _ \n" +
                "/__   \\|__   ___ /__   \\___(_) __ _| |___ \n" +
                " / /\\/ '_ \\ / _ \\  / /\\/'__| |/ _` | / __|\n" +
                "/ /  | | | | ___/ / /  | | | | (_| | \\__ \\\n" +
                "\\/   |_| |_|\\___| \\/   |_| |_|\\__,_|_|___/\n");
    }
}