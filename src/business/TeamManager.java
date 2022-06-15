package business;


import business.playerTypes.Player;
import com.google.gson.annotations.SerializedName;
import persistance.CSV.TeamCsvDAO;
import persistance.JSON.TeamJsonDAO;
import persistance.TeamDAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Controla aquello relacionado con el equipo jugando una edición
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class TeamManager {
    private TeamDAO teamDAO;

    /**
     * Método constructor que crea un nuevo manager, relacionándolo con JSON o CSV
     * @param options opción del tipo de persistencia que se quiere escoger
     */
    public TeamManager(DataSourceOptions options) {
        switch (options) {
            case JSON -> teamDAO = new TeamJsonDAO();
            case CSV -> teamDAO = new TeamCsvDAO();
        }
    }

    /** Método que añade un nuevo participante
     * @param player Nombre del jugador
     */
    public void addPlayer (Player player) {
        teamDAO.create(player);
    }

    /**
     * Método que actualiza la información de un jugador en una posición determinada
     * @param index Posición del jugador en el archivo
     * @param player Player actualizado
     */
    public void updatePlayer(int index, Player player) {
        teamDAO.changeLine(index, player);
    }

    /**
     * Método que elimina los jugadores muertos de un equipo
     * @return Booleano que indica si TODOS los jugadores han muerto o no
     */
    public boolean checkDeadPlayers () {
        boolean dead = true;
        LinkedList<Player> players = getPlayers();
        for (int i = 0; i < players.size() && dead; i++) {
            if (players.get(i).getPI() != 0) {
                dead = false;
            }
        }
        return dead;
    }

    /**
     * Método que eliminar a todos los jugadores
     */
    public void removeAllPlayers () {
        teamDAO.emptyFile();
    }

    /**
     * Método que obtiene todos los jugadores en una lista
     * @return LinkedList con todos lo s jugadores
     */
    public LinkedList<Player> getPlayers () {
        return teamDAO.readAll();
    }

    /**
     * Método que obtiene el PI total de todos los miembros del equipo
     * @return Entero con el PI del equipo
     */
    public int getPITeam () {
        int total = 0;
        for (int i = 0; i < getPlayers().size(); i++) {
            total += getPlayers().get(i).getPI();
        }
        return total;
    }
}