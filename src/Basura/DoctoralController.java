package Basura;

import business.TrialTypeOptions;
import business.trialsTypes.DoctoralThesis;
import presentation.ViewController;

/**
 * Controla ciertas partes de la entrada y salida de datos de los Doctoral
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class DoctoralController {
    private ViewController view;
    private DoctoralManager doctoralManager;
    private GenericTrialManager genericTrialManager;

    /**
     * Constructor que crea un nuevo DoctoralController
     * @param view clase vista
     * @param doctoralManager manager de los doctorals que se comunica con la persistencia
     * @param genericTrialManager manager de los triols de tipo genérico
     */
    public DoctoralController(ViewController view, DoctoralManager doctoralManager, GenericTrialManager genericTrialManager) {
        this.view = view;
        this.doctoralManager = doctoralManager;
        this.genericTrialManager = genericTrialManager;
    }

    /**
     * Método que pide los datos para añadir un nuevo doctoral, comprobando para cada dato si es correcto
     */
    public void add() {
        String trialName = view.askForString("\nEnter the trial's name: ");
        if (checkError(trialName, 1)) {
            String thesis = view.askForString("Enter the thesis field of study: ");
            if (checkError(thesis, 2)) {
                int difficulty = view.askForInteger("Enter the defense difficulty: ");
                if (checkError(String.valueOf(difficulty), 3)) {
                    doctoralManager.addDoctoralThesis(trialName, thesis, difficulty, false);
                    genericTrialManager.addTrial(trialName, TrialTypeOptions.valueOf("DOCTORAL"));
                    view.showMessage("\nThe trial was created successfully!");
                }else{
                    view.showMessage("\nDifficulty must be an integer in the [1, 10] range.");
                }
            }else{
                view.showMessage("\nThesis field can not be empty.");
            }
        }else{
            view.showMessage("\nTrial name must be unique and not empty.");
        }
    }

    /**
     * Muestra toda la información de un doctoral concreto
     * @param index posición del doctoral que se quiere mostrar
     */
    public void showDoctoral (int index) {
        String name = genericTrialManager.getGenericalTrial(index).getName();
        DoctoralThesis doctor = doctoralManager.getDoctoralByName(name);
        view.showMessage("\nTrial: " + doctor.getName() + " (Doctoral thesis defense)");
        view.showMessage("Field: " + doctor.getFieldOfStudy());
        view.showMessage("Difficulty: " + doctor.getDifficulty());
    }

    /**
     * Comprueba, para cada tipo de dato, si hay algún error
     * @param aux cadena que contiene la información para comprobar
     * @param mode entero que nos permite saber qué tipo de comprobación realizar
     * @return booleano que permite saber is ha habido error o no
     */
    private boolean checkError (String aux, int mode) {
        switch (mode) {
            case 1: // Comprobamos que el nombre no este vacío y que no exista
                if (!aux.isEmpty()) {
                    return !genericTrialManager.checkExistance(aux);
                }else{
                    return false;
                }
            case 2: // Comprobamos que el nombre no este vacío
                return !aux.isEmpty();
            case 3: // Comprobamos que sea uno de los valores posibles
                return Integer.parseInt(aux) >= 0 && Integer.parseInt(aux) <= 10;
        }
        // Nunca se dará un caso diferente a los del switch
        return true;
    }
}