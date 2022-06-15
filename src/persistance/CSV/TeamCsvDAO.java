package persistance.CSV;

import business.PlayerTypeOptions;
import business.playerTypes.Doctor;
import business.playerTypes.Engineer;
import business.playerTypes.Master;
import business.playerTypes.Player;
import persistance.TeamDAO;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que gestiona la lectura y escritura del fichero CSV del team
 * @author Jose Perez
 * @author Abraham Cedeño
 */
public class TeamCsvDAO implements TeamDAO {
    private static final String separator = ",";
    private final String fileName = "team.csv";
    private final String filePath = "files";
    private File file = new File(filePath, fileName);

    /**
     * Método constructor que crea un fichero CSV nuevo, en caso de no existir
     */
    public TeamCsvDAO () {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Transforma un objeto de jugador a una línea de CSV
     * @param player jugador que se quiere transformar
     * @return String Cadena de caracteres a guardar
     */
    private String playerToCsv (Player player) {
        // Según que instancia de Player sea, se escribe de una forma determinada
        if (player instanceof Engineer) {
            return player.getName() + separator + player.getPI() + separator + PlayerTypeOptions.ENGINEER;
        } else if (player instanceof Doctor) {
            return player.getName() + separator + player.getPI() + separator + PlayerTypeOptions.DOCTOR;
        } else {
            return player.getName() + separator + player.getPI() + separator + PlayerTypeOptions.MASTER;
        }
    }


    /**
     * Método que crea un objeto de Player (paper, budget, master o doctoral) según su tipo
     * @param csv Línea que queremos convertir
     * @return Player que se quería obtener
     */
    private Player playerFromCsv(String csv) {
        String[] parts = csv.split(separator);
        // Segun que tipo de player sea, se crea una instancia determinada
        if (parts[2].equals("ENGINEER")) {
            return new Engineer(parts[0], Integer.parseInt(parts[1]));
        } else if (parts[2].equals("DOCTOR")) {
            return new Doctor(parts[0], Integer.parseInt(parts[1]));
        } else {
            return new Master(parts[0], Integer.parseInt(parts[1]));
        }
    }

    /**
     * Crea un nuevo jugador y lo escribe en el fichero
     * @param player jugador que se desea escribir
     * @return booleano que indica si se ha escrito correctamente
     */
    @Override
    public boolean create(Player player) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            list.add(playerToCsv(player));
            Files.write(file.toPath(), list);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Elimina una línea del fichero
     * @param index posición de la línea a eliminar
     * @return booleano que indica si se ha eliminado correctamente
     */
    @Override
    public boolean delete(int index) {
        try {
            List<String> players = Files.readAllLines(file.toPath());
            players.remove(index);
            Files.write(file.toPath(), players);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Lee todos los elementos de un fichero CSV
     * @return Lista con los objetos de todos los elementos leídos
     */
    @Override
    public LinkedList<Player> readAll() {
        try{
            LinkedList<Player> players = new LinkedList<>();
            List<String> list = Files.readAllLines(file.toPath());
            for (String line: list) {
                players.add(playerFromCsv(line));
            }
            return players;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Actualiza una línea del fichero
     * @param index Posición de la línea a modificar
     * @param player Nuevo objeto que quiere escribirse en la línea
     * @return booleano que indica si se ha modificado correctamente
     */
    @Override
    public boolean changeLine(int index, Player player) {
        try {
            List<String> players = Files.readAllLines(file.toPath());
            players.remove(index);
            players.add(index, playerToCsv(player));
            Files.write(file.toPath(), players);
            return true;
        }catch (IOException e) {
            return false;
        }
    }

    @Override
    public Player deserialize(String texto) {
        return null;
    }

    @Override
    public String serialize(Player player) {
        return null;
    }

    /**
     * Vacía por completo del fichero de jugadores
     * @return booleano que indica si se ha vaciado el fichero correctamente
     */
    @Override
    public boolean emptyFile() {
        try {
            LinkedList<String> players = new LinkedList<>();
            Files.write(file.toPath(), players);
            return true;
        } catch (IOException e) {
            return false;
        }
    }


}

