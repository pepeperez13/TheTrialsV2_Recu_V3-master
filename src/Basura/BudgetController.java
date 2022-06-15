package Basura;

import business.TrialTypeOptions;
import business.trialsTypes.Budget;
import presentation.ViewController;


/**
 * Controla ciertas partes de la entrada y salida de datos de los Budget
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class BudgetController {
    private final BudgetManager budgetManager;
    private final ViewController view;
    private final GenericTrialManager genericTrialManager;

    /**
     * Constructor que crea un nuevo BudgetController
     * @param budgetManager manager de los budgets que se comunica con la persistencia
     * @param view clase vista
     * @param genericTrialManager manager de los trials de tipo genérico
     */
    public BudgetController(BudgetManager budgetManager, ViewController view, GenericTrialManager genericTrialManager) {
        this.budgetManager = budgetManager;
        this.view = view;
        this.genericTrialManager = genericTrialManager;
    }

    /**
     * Método que pide los datos para añadir un nuevo budget, comprobando para cada dato si es correcto
     */
    public void add() {
        String trialName = view.askForString("\nEnter the trial's name: ");
        if (checkError(trialName, 1)) {
            String entityName = view.askForString("Enter the entity's name: ");
            if (checkError(entityName, 2)) {
                int budget = view.askForInteger("Enter the budget amount: ");
                if (checkError(String.valueOf(budget), 3)) {
                    budgetManager.addBudget(trialName, entityName, budget, false);
                    genericTrialManager.addTrial(trialName, TrialTypeOptions.valueOf("BUDGET"));
                    view.showMessage("\nThe trial was created successfully!");
                }else{
                    view.showMessage("\nBudget amount must be in the [1000, 2000000000] range.");
                }
            }else{
                view.showMessage("\nEntity name must not be empty.");
            }
        }else{
            view.showMessage("\nTrial name must be unique and not empty.");
        }

    }

    /**
     * Muestra toda la información de un budget concreto
     * @param index posición del budget que se quiere mostrar
     */
    public void showBudget (int index) {
        String name = genericTrialManager.getGenericalTrial(index).getName();
        Budget budget = budgetManager.getBudgetByNameTrial(name);
        view.showMessage("\nTrial: " + budget.getNameTrial() + " (Budget request)");
        view.showMessage("Entity: " + budget.getNameEntity());
        view.showMessage("Budget: " + budget.getAmount() + " €");
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
                return Integer.parseInt(aux) >= 1000 && Integer.parseInt(aux) <= 2000000000;
        }
        // Nunca se dará un caso diferente a los del switch
        return true;
    }

}