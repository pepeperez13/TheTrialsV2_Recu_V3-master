package presentation;

import business.TeamManager;
import business.playerTypes.Doctor;
import business.playerTypes.Engineer;
import business.playerTypes.Master;
import business.playerTypes.Player;
import business.trialsTypes.*;

import java.util.LinkedList;

/**
 * Clase que se encarga de ejecutar la lógica de ejecución de cada una de las diferentes tipos de pruebas
 */
public class GameExecutor {
    private final TeamManager teamManager;
    private final ViewController view;

    /**
     * Constructor del GameExecutor
     * @param teamManager Gestiona aquello relacionado con los jugadores (team)
     * @param view Gestiona aquello relacionado con la interacción por pantalla
     */
    public GameExecutor(TeamManager teamManager, ViewController view) {
        this.teamManager = teamManager;
        this.view = view;
    }

    /**
     * Llama al método de jugar prueba, que se encuentra dentro de cada prueba especifica. Posteriormente
     * muestra si han habido cambios en los tipos de jugadores
     * @param genericTrial prueba a ejecutar
     */
    public void playTrial (GenericTrial genericTrial)  {
        genericTrial.playTrial(teamManager,view);

        // Se muestran las evoluciones
        LinkedList<String> changedType = checkUpdateStatus();
        for (String line: changedType) {
            view.showMessageLine(line);
        }

    }

    /**
     * Método que revisa los PI de todos los jugadores de un equipo y cambia su estado (evoluciona) si es necesario.
     * @return lista con los mensajes de todos los jugadores que hayan evolucionado
     */
    private LinkedList<String> checkUpdateStatus () {
        int i = 0;
        LinkedList<String> changedType = new LinkedList<>();
        // Para cada jugador, comprobamos si debe evolucionar de tipo
        for (Player player : teamManager.getPlayers()) {
            // Debemos distinguir que tipo de jugador es para poder escribir el mensaje correcto
            if (player instanceof Engineer) {
                // Si debe evolucionar (tiene 10 PI, lo evolucionamos)
                if (player.checkUpdateStatus()) {
                    Player master = new Master(player.getName(), 5);
                    teamManager.updatePlayer(i, master);
                    // Añadimos a la lista de jugadores evolucionados para mostrarlo
                    changedType.add(player.getName() + " is now a master (with 5 PI). ");
                    if (i == teamManager.getPlayers().size() - 1) { // Añade un espacio al final
                        view.showMessage("");
                    }
                }
            } else if (player instanceof Master) {
                // Si debe evolucionar (tiene 10 PI, lo evolucionamos)
                if (player.checkUpdateStatus()) {
                    Player doctor = new Doctor(player.getName(), 5);
                    teamManager.updatePlayer(i, doctor);
                    // Añadimos a la lista de jugadores evolucionados para mostrarlo
                    changedType.add(player.getName() + " is now a doctor (with 5 PI). ");
                    if (i == teamManager.getPlayers().size() - 1) { // Añade un espacio al final
                        view.showMessage("");
                    }
                }
            }
            i++;
        }
        return changedType;
    }
}
