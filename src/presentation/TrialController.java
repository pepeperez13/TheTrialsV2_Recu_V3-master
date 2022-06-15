package presentation;

import business.ManagersTrials.TrialsManager;
import business.trialsTypes.*;
import presentation.ViewController;

public class TrialController {
    private final ViewController view;
    private final TrialsManager trialsManager;

    /**
     * Constructor que crea un nuevo PaperController
     * @param view clase vista
     */
    public TrialController(ViewController view, TrialsManager trialsManager) {
        this.view = view;
        this.trialsManager = trialsManager;
    }

    /**
     * Método que pide los datos para añadir un nuevo budget, comprobando para cada dato si es correcto
     */
    public void add (int trialType) {

        switch (trialType) {
            case 1 -> askInfoAddPaper();
            case 2 -> askInfoAddMaster();
            case 3 -> askInfoAddDoctoral();
            case 4 -> askInfoAddBudget();
        }

    }

    private void askInfoAddPaper () {
        String trialName = view.askForString("\nEnter the trial's name: ");
        if (checkErrorPaper (trialName, 1)) {
            String journalName = view.askForString("Enter the journal's name: ");
            if (checkErrorPaper (journalName, 2)) {
                String quartile = view.askForString("Enter the journal's quartile: ");
                if (checkErrorPaper (quartile, 3)) {
                    int accepted = view.askForInteger("Enter the acceptance probability: ");
                    if (checkErrorPaper (String.valueOf(accepted), 4)) {
                        int revision = view.askForInteger("Enter the revision probability: ");
                        if (checkErrorPaper (String.valueOf(revision), 5)) {
                            int rejection = view.askForInteger("Enter the rejection probability: ");
                            if ((accepted + revision + rejection) == 100 && checkErrorPaper (String.valueOf(rejection), 6)) {
                                GenericTrial trial = new PaperPublication(trialName, journalName, quartile, accepted, revision, rejection, false);
                                trialsManager.addTrial(trial);
                                view.showMessage("\nThe trial was created successfully!");
                            }else{
                                view.showMessage("\nValue must be between 0 and 100 and the sum of all percenatges has to be 100.");
                            }
                        }else{
                            view.showMessage("\nValue must be between 0 and 100 and the sum of all percenatges can't be over 100.");
                        }
                    }else{
                        view.showMessage("\nValue must be between 0 and 100 and the sum of all percenatges can't be over 100.");
                    }
                }else{
                    view.showMessage("\nQuartile must be one of the following values [Q1, Q2, Q3, Q4].");
                }
            }else{
                view.showMessage("\nMagazine name must not empty.");
            }
        }else{
            view.showMessage("\nTrial name must be unique and not empty.");
        }
    }

    private void askInfoAddMaster () {
        String trialName = view.askForString("\nEnter the trial's name: ");
        if (checkErrorMaster(trialName, 1)) {
            String masterName = view.askForString("Enter the master's name: ");
            if (checkErrorMaster(masterName, 2)) {
                int ECTS = view.askForInteger("Enter the master's ECTS number: ");
                if (checkErrorMaster(String.valueOf(ECTS), 3)) {
                    int creditPass = view.askForInteger("Enter the credit pass probability: ");
                    if (checkErrorMaster(String.valueOf(creditPass), 4)){
                        GenericTrial trial = new MasterStudies(trialName, masterName, ECTS, creditPass, false);
                        trialsManager.addTrial(trial);
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

    private void askInfoAddDoctoral () {
        String trialName = view.askForString("\nEnter the trial's name: ");
        if (checkErrorDoctoral(trialName, 1)) {
            String thesis = view.askForString("Enter the thesis field of study: ");
            if (checkErrorDoctoral(thesis, 2)) {
                int difficulty = view.askForInteger("Enter the defense difficulty: ");
                if (checkErrorDoctoral(String.valueOf(difficulty), 3)) {
                    GenericTrial trial = new DoctoralThesis(trialName, thesis, difficulty, false);
                    trialsManager.addTrial(trial);
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

    private void askInfoAddBudget () {
        String trialName = view.askForString("\nEnter the trial's name: ");
        if (checkErrorBudget(trialName, 1)) {
            String entityName = view.askForString("Enter the entity's name: ");
            if (checkErrorBudget(entityName, 2)) {
                int budget = view.askForInteger("Enter the budget amount: ");
                if (checkErrorBudget(String.valueOf(budget), 3)) {
                    GenericTrial trial = new Budget(trialName, entityName, budget, false);
                    trialsManager.addTrial(trial);
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
     * Muestra la información detallada de un Paper en concreto
     * @param numTrial Indice de la prueba concreta sobre la que se quiera obtener información
     */
    public void showTrial (int numTrial)  {
        GenericTrial trial = trialsManager.getTrialByIndex(numTrial);
        if (trial instanceof PaperPublication paper) {
            view.showMessage("\nTrial: " + paper.getArticleName() + " (Paper publication)");
            view.showMessage("Journal: " + paper.getMagazineName() + " (" + paper.getQuartile() + ")");
            view.showMessage("Chances: " + paper.getAcceptedProbability() + "% acceptance, " +
                    paper.getRevisedProbability() + "% revision, " +
                    paper.getRejectedProbability() + "% rejection");
        } else if (trial instanceof MasterStudies master){
            view.showMessage("\nTrial: " + master.getName() + " (Master studies)");
            view.showMessage("Master: " + master.getNom());
            view.showMessage("ECTS: " + master.getNumberCredits()
                    + ", with a " + master.getProbability()
                    + "% chance to pass each one");
        } else if (trial instanceof DoctoralThesis doctoral) {
            view.showMessage("\nTrial: " + doctoral.getName() + " (Doctoral thesis defense)");
            view.showMessage("Field: " + doctoral.getFieldOfStudy());
            view.showMessage("Difficulty: " + doctoral.getDifficulty());
        } else if (trial instanceof Budget budget) {
            view.showMessage("\nTrial: " + budget.getNameTrial() + " (Budget request)");
            view.showMessage("Entity: " + budget.getNameEntity());
            view.showMessage("Budget: " + budget.getAmount() + " €");
        }
    }

    /**
     * Comprueba, para cada tipo de dato, si hay algún error
     * @param aux cadena que contiene la información para comprobar
     * @param mode entero que nos permite saber qué tipo de comprobación realizar
     * @return booleano que permite saber is ha habido error o no
     */
    private boolean checkErrorPaper (String aux, int mode) {
        switch (mode) {
            case 1: // Comprobamos que el nombre no este vacío y que no exista
                if (!aux.isEmpty()) {
                    return !trialsManager.checkExistance(aux);
                }else{
                    return false;
                }
            case 2: // Comprobamos que el nombre no este vacío
                return !aux.isEmpty();
            case 3: // Comprobamos que sea uno de los valores posibles
                return aux.equals("Q1") || aux.equals("Q2") || aux.equals("Q3") || aux.equals("Q4");
            case 4, 5, 6: // Comprobamos que este entre 0 y 100
                return Integer.parseInt(aux) >= 0 && Integer.parseInt(aux) <= 100;
        }
        // Nunca se dará un caso diferente a los del switch
        return true;
    }

    private boolean checkErrorMaster (String aux, int mode) {
        switch (mode) {
            case 1: // Comprobamos que el nombre no este vacío y que no exista
                if (!aux.isEmpty()) {
                    return !trialsManager.checkExistance(aux);
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

    private boolean checkErrorDoctoral (String aux, int mode) {
        switch (mode) {
            case 1: // Comprobamos que el nombre no este vacío y que no exista
                if (!aux.isEmpty()) {
                    return !trialsManager.checkExistance(aux);
                }else{
                    return false;
                }
            case 2: // Comprobamos que el nombre no este vacío
                return !aux.isEmpty();
            case 3: // Comprobamos que sea uno de los valores posibles
                return Integer.parseInt(aux) > 0 && Integer.parseInt(aux) <= 10;
        }
        // Nunca se dará un caso diferente a los del switch
        return true;
    }

    private boolean checkErrorBudget (String aux, int mode) {
        switch (mode) {
            case 1: // Comprobamos que el nombre no este vacío y que no exista
                if (!aux.isEmpty()) {
                    return !trialsManager.checkExistance(aux);
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

