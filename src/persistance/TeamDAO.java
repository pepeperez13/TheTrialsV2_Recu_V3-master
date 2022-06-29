package persistance;

import business.playerTypes.Player;

import java.util.LinkedList;

/**
 *  Interifice que contiene los metodos de lectura, escritura, etc. de un fichero de jugadores (CSV o JSON)
 */
public interface TeamDAO {

    /**
     * Escribe un nuevo jugador en un fichero
     * @param player jugador a escribir
     * @return indicia si se ha escrito correctamente (true) o no (false)
     */
    boolean create(Player player);

    /**
     * Lee todos los jugadores de un fichero y los guarda en una lista
     * @return lista de los jugadores leídas
     */
    LinkedList<Player> readAll();

    /**
     * Elimina un jugador del fichero mediante su posicion
     * @param index posicion del jugador a eliminar
     * @return indica si se ha eliminado correctamente (true) o no (false)
     */
    boolean delete(int index);

    /**
     * Actualiza la información de un objeto del fichero a traves de su posicion
     * @param index posicion del elemento a modificar
     * @param player nuevo elemento a escribir
     * @return indica si se ha eliminado correctamente (true) o no (false)
     */
    boolean changeLine(int index, Player player);

    /**
     * Borra todos los elementos existentes del fichero
     * @return indica si los elementos se han eliminado correctamente (true) o no (false)
     */
    boolean emptyFile();
}