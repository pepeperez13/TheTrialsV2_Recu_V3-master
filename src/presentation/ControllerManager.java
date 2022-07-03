package presentation;


/**
 * Clase que va a mostrar el mensaje inicial y posteriormente se encargará de ir ejecutando hasta que el usuario termine la ejecución
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
     * @param option opcion de persistencia escogida
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
     * Bucle principal del programa. Ejecuta hasta que lo indique el usuario o termine la ejecución
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
    private void executeCompositor () {
        compositorController.run();
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

    /**
     * Actualiza una variable que permite saber al programa si debe finalizarse la ejecución
     */
    public static void setEndProgram () {
        endProgram = true;
    }
}