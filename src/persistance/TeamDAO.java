package persistance;

import business.playerTypes.Player;

import java.util.LinkedList;

/**
 * Interficie contiene los métodos de escritura y lectura del equipo
 */
public interface TeamDAO {

    boolean create(Player player);

    LinkedList<Player> readAll();

    boolean delete(int index);

    boolean changeLine(int index, Player player);

    Player deserialize (String texto);

    String serialize (Player player);

    boolean emptyFile();
}