package Basura;

import business.TrialTypeOptions;
import business.trialsTypes.MasterStudies;
import presentation.ViewController;

/**
 * Controla ciertas partes de la entrada y salida de datos de los Master
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class MasterController {
    private ViewController view;
    private MasterManager masterManager;
    private GenericTrialManager genericTrialManager;

    /**
     * Constructor que crea un nuevo MasterController
     * @param viewController clase vista
     * @param masterManager manager de los masters que se comunica con la persistencia
     * @param genericTrialManager manager de los triols de tipo genérico
     */
    public MasterController(ViewController viewController, MasterManager masterManager, GenericTrialManager genericTrialManager) {
        this.view = viewController;
        this.masterManager = masterManager;
        this.genericTrialManager = genericTrialManager;
    }

    /**
     * Método que pide los datos para añadir un nuevo master, comprobando para cada dato si es correcto
     */
    public void add() {
        String trialName = view.askForString("\nEnter the trial's name: ");
        if (checkError(trialName, 1)) {
            String masterName = view.askForString("Enter the master's name: ");
            if (checkError(masterName, 2)) {
                int ECTS = view.askForInteger("Enter the master's ECTS number: ");
                if (checkError(String.valueOf(ECTS), 3)) {
                    int creditPass = view.askForInteger("Enter the credit pass probability: ");
                    if (checkError(String.valueOf(creditPass), 4)){
                        masterManager.addMaster(trialName, masterName, ECTS, creditPass, false);
                        genericTrialManager.addTrial(trialName, TrialTypeOptions.valueOf("MASTER"));
                        view.showMessage("\nThe trial was created successfully!");
                    }else{
                        view.showMessage("\nProbability must be in the [0, 100] range.");
                    }
                }else{
                    view.showMessage("\nCredits number must be in the [60, 120] range.");
                }
            }else{
                view.showMessage("\nNom of the master can't be empty");
            }
        }else{
            view.showMessage("\nTrial name must be unique and not empty.");
        }
    }

    /**
     * Muestra toda la información de un master concreto
     * @param numTrial posición del budget que se quiere mostrar
     */
    public void showMaster (int numTrial) {
        String name = genericTrialManager.getGenericalTrial(numTrial).getName();
        MasterStudies master = masterManager.getMasterByName(name);
        view.showMessage("\nTrial: " + master.getName() + " (Master studies)");
        view.showMessage("Master: " + master.getNom());
        view.showMessage("ECTS: " + master.getNumberCredits()
                + ", with a " + master.getProbability()
                + "% chance to pass each one");
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
                return Integer.parseInt(aux) >= 60 && Integer.parseInt(aux) <= 120;
            case 4: // Comprobamos que este entre 0 y 100
                return Integer.parseInt(aux) >= 0 && Integer.parseInt(aux) <= 100;
        }
        // Nunca se dará un caso diferente a los del switch
        return true;
    }
}
