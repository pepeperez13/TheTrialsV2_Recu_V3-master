package presentation;

import business.Edition;
import business.EditionManager;
import business.ManagersTrials.TrialsManager;
import business.TeamManager;
import business.playerTypes.Engineer;
import business.playerTypes.Player;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * Clase que se encarga de gestionar la ejecución del menú de ejecución (Conductor)
 * @author José Pérez
 * @author Abraham Cedeño
 */
public class ConductorController {
    private EditionManager editionManager;
    private TeamManager teamManager;
    private ViewController view;
    private GameExecutor gameExecutor;
    private TrialsManager trialsManager;

    /**
     * Construye un nuevo ConductorController, con todas las clases que este necesita
     * @param editionManager Gestiona aquello relacionado con las ediciones
     * @param teamManager Gestiona aquello relacionado con los jugadores (team)
     * @param view Gestiona aquello relacionado con la interacción por pantalla
     * @param gameExecutor Se encarga de gestionar la ejecución de todos los tipos de pruebas
     */
    public ConductorController(EditionManager editionManager, TeamManager teamManager, ViewController view, GameExecutor gameExecutor, TrialsManager trialsManager) {
        this.editionManager = editionManager;
        this.teamManager = teamManager;
        this.view = view;
        this.gameExecutor = gameExecutor;
        this.trialsManager = trialsManager;
    }

    /**
     * Método principal que va llamado a los diferentes método que permiten la ejecución de una edición
     * @param finalIndex Variable que nos permitirá saber qué prueba se ha ejecutado la última (por ejemplo si el jugador
     *                   ha decidido pausar la ejecución anteriormente) y por tanto si la próxima vez que entremos
     *                   se tiene que continuar la ejecución a partir de ese punto o empezar de nuevo
     */
    public int run (int finalIndex) {
        // Variable que nos permitirá saber qué prueba se ha ejecutado la última, y por tanto si la próxima
        // vez que entremos tiene que continuar la ejecución o empezar de nuevo
        view.showMessage("\nEntering execution mode ...");
        if (!checkYear(editionManager.getEditions())) {
            view.showMessage("\nNo edition is defined for the current year (2022)");
            view.showMessage("\nShutting down...");
            ControllerManager.setEndProgram();
        } else {
            view.showMessage("\n--- The Trials 2022 ---\n"); //Ojo con la fecha
            // Siempre que finalIndex valga 0, se tendran que volver a pedir los jugadores
            if (finalIndex == 0) {
                savePlayers();
            }
            // Ejecutamos la edición desde la edición correspondientes
            finalIndex = playTrials(editionManager.getEditionCurrentYear().getNumTrials(), finalIndex);
            // Borramos del fichero los jugadores eliminados y obtenemos si todos los jugadores tienen PI = 0 o no (equipo pierde)
            boolean dead = teamManager.checkDeadPlayers();
            // Si se ha llegado a la última prueba y los jugadores no han perdido
            if (!dead && finalIndex == editionManager.getEditionCurrentYear().getNumTrials()) {
                view.showMessage("\nTHE TRIALS 2022 HAVE ENDED - PLAYERS WON\n\nShutting down...");
                teamManager.removeAllPlayers();
                finalIndex = 0;
                ControllerManager.setEndProgram();
            } else {
                if (dead) {
                    view.showMessage("\nExecution ended... Players lost!");
                    teamManager.removeAllPlayers();
                    finalIndex = editionManager.getEditionCurrentYear().getNumTrials(); // SI han muerto todos los jugadores, la proxima edicion se tendrá que empezar de nuevo
                } else {
                    view.showMessage("\nSaving & shutting down...");
                    //ControllerManager.setEndProgram();
                }
            }
        }
        return finalIndex;
    }

    /**
     * Se encarga de pedir los jugadores de la edición y guardarlos
     */
    private void savePlayers () {
        int numPlayers = editionManager.getEditionCurrentYear().getNumPlayers();

        for (int j = 0; j < numPlayers; j++) {
            String name = view.askForString("Enter the player's name" + " (" + (j+1) +"/" + numPlayers + "): ");
            Player newPlayer = new Engineer(name, 5);
            teamManager.addPlayer(newPlayer);
        }

    }

    /**
     * Encargado de ejecutar y organizar la ejecución de la edición, llamando al resto de métodos que permiten su ejecución
     * @param numTrials Número de pruebas con las que cuenta esa edición
     * @param index Indice que permite al método saber a partir de qué prueba se debe ejecutar la edición
     * @return Entero que nos permitirá saber cuál ha sido la última prueba ejecutada (si se ha parado la ejecución o no)
     */
    private int playTrials (int numTrials, int index)  {
        int i;
        boolean continueExecution = true;
        String aux = "";
        for (i = index; i < numTrials && continueExecution && !teamManager.checkDeadPlayers(); i++) {
            view.showMessage("\nTrial #" + (i+1) + " - " + editionManager.getEditionCurrentYear().getTrialNameByIndex(i));
            //Pasamos un GenericTrial al gameExecutor
            gameExecutor.playTrial(trialsManager.getTrialByName(editionManager.getEditionCurrentYear().getTrialNameByIndex(i)));
            //gameExecutor.playTrial(genericTrialManager.getGenericalTrial(genericTrialManager.getIndexByName(editionManager.getEditionCurrentYear().getTrialNameByIndex(i))+1));
            boolean dead = teamManager.checkDeadPlayers();
            if (i != numTrials - 1 && !dead) { // Si no se han ejecutado ya todos los trials, preguntamos si seguir con ejecución o no
                do {
                    aux = view.askForString("\nContinue the execution? [yes/no]: ");
                    switch (aux) {
                        case "yes" -> continueExecution = true;
                        case "no" -> continueExecution = false;
                        default -> view.showMessage("Expected yes/no. Try again. ");
                    }
                } while (!aux.equals("yes") && !aux.equals("no"));
            }
        }
        return i;
    }

    /**
     * Se encarga de comprobar que exista una edición para el año actual
     * @param editions Lista con las ediciones donde buscaremos la de este año
     * @return Nos permitirá saber si se ha encontrado una edición para este año o no
     */
    private boolean checkYear (LinkedList<Edition> editions) {
        Calendar calendar = new GregorianCalendar();
        boolean ok = false;
        for (int i = 0; i < editions.size(); i++) {
            if (editions.get(i).getYear() == calendar.get(Calendar.YEAR)) {
                ok = true;
            }
        }
        return ok;
    }
}
