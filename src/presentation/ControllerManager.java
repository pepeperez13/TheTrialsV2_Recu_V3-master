package presentation;


import business.EditionManager;

import java.io.IOException;

/**
 * Clase que va a mostrar el mensaje inicial y posteriormente se encargará de ir ejecutando por tiempo indefinido
 * el modo compositor o el modo conductor
 *
 */
public class ControllerManager {
    private final ViewController viewController;
    private final CompositorController compositorController;
    private final ConductorController conductorController;
    private static boolean endProgram;

    /**
     * Construye un nuevo ControllerManager, con todas las clases que este necesita
     *
     * @param viewController       Inicializa la vista para poder llamarla
     * @param compositorController Inicializa el CompositorController para poder llamarlo
     * @param conductorController  Inicializa el ConductorController para poder llamarlo
     */
    public ControllerManager(ViewController viewController, CompositorController compositorController, ConductorController conductorController) {
        this.viewController = viewController;
        this.compositorController = compositorController;
        this.conductorController = conductorController;
    }

    /**
     * Método principal del programa, en forma de bucle, que da a escoger entre Compositor y Conductor,
     * cuya ejecución no acabará nunca
     */
    public void run (String option) {
        do {
            switch (option) {
                case "I" -> {
                    viewController.showMessage("\nLoading data from CSV files...\n");
                    mainLoop();
                }
                case "II" -> {
                    viewController.showMessage("\nLoading data from JSON files...\n");
                    mainLoop();
                }
                default -> viewController.showMessage("\nInvalid option");
            }
        } while (!option.equals("I") && !option.equals("II"));
    }

    /**
     * Bucle principal del programa. Ejecuta indefinidamente
     */
    private void mainLoop()  {
        viewController.showLogo();
        int finalIndex = 0;
        do {
            viewController.showMainMenu();
            String mode = viewController.askForString("\nEnter a role: ");

            switch (mode) {
                case "A" -> executeCompositor();
                case "B" -> finalIndex = executeConductor(finalIndex);
                default -> viewController.showMessage("\nIncorrect option. Option must be one of the above [A, B]");
            }
            // EL programa sólo acabará cuando se actualice el valor de la variabel "endProgram" a true
        } while (!endProgram);
    }

    /**
     * LLama a la función principal de ejecución del Compositor
     */
    private int executeCompositor () {
        return compositorController.run();
    }

    /**
     * Llama a la función principal de ejecución del Conductor
     * @param finalIndex Permite saber al Conductor cual fue la última prueba ejecutada
     * @return Permite guardar cuál ha sido la última prueba ejecutada para, si se vuelve a ejecutar, seguir la ejecución
     * desde la prueba correspondiente
     */
    private int executeConductor(int finalIndex) {
        return conductorController.run(finalIndex);
    }

    public static void setEndProgram () {
        endProgram = true;
    }
}