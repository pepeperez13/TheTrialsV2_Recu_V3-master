import business.DataSourceOptions;
import business.EditionManager;
import business.ManagersTrials.*;
import business.TeamManager;
import presentation.*;

public class Main {

    public static void main (String[] args) {
        DataSourceOptions dataSourceOptions = null;
        ViewController viewController = new ViewController();
        /**
         * Preguntamos por el tipo de archivo que vamos a usar CSV O JSON
         * Debemos hacer esta parte aqui en el main, ya que si no no podríamos pasar como parametro el dataSourceOptions
         * a todas las clases que lo necesitan
         */
        String option;
        viewController.showMessage("\nThe IEEE needs to know where your allegiance lies.\n");
        viewController.showStartingMenu();

        do {
            option = viewController.askForString("Pick a faction: ");
            if (option.equals("I")) {
                dataSourceOptions = DataSourceOptions.CSV;
            } else if (option.equals("II")) {
                dataSourceOptions = DataSourceOptions.JSON;
            }else{
                viewController.showMessage("\nFaction must be one of the above (I, II).");
            }
        }while (!option.equals("I")  && !option.equals("II"));

        /**
         * Constructores
         */
        EditionManager editionManager = new EditionManager(dataSourceOptions);
        TrialsManager trialsManager = new TrialsManager(dataSourceOptions);
        TeamManager teamManager = new TeamManager(dataSourceOptions);

        GameExecutor gameExecutor = new GameExecutor(teamManager, viewController);

        TrialController trialController = new TrialController(viewController, trialsManager);

        CompositorController compositorController = new CompositorController(viewController, editionManager, trialsManager, trialController);
        ConductorController conductorController = new ConductorController(editionManager, teamManager, viewController, gameExecutor, trialsManager);

        /**
         * Le pasamos el tipo al Controller manager
         */
        ControllerManager controllerManager = new ControllerManager(viewController, compositorController, conductorController);
        controllerManager.run(option);
    }
}
