package persistance.JSON;

import business.playerTypes.Doctor;
import business.playerTypes.Engineer;
import business.playerTypes.Master;
import business.playerTypes.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import persistance.TeamDAO;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que gestiona la lectura y escritura del fichero JSON del team
 * @author Jose Perez
 * @author Abraham Cedeño
 */
public class TeamJsonDAO implements TeamDAO {
    private final String filename = "teams.json";
    private static final String route = "files/teams.json";
    private static final Path path = Path.of(route);
    private File file = new File("files", filename);

    /**
     * Método constructor que crea un fichero CSV nuevo, en caso de no existir
     */
    public TeamJsonDAO () {
        try {
            if(!file.exists()){
                file.createNewFile();
                Files.write(Path.of(String.valueOf(path)), "[]".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea un nuevo jugador y lo escribe en el fichero
     * @param player jugador que se desea escribir
     * @return booleano que indica si se ha escrito correctamente
     */
    @Override
    public boolean create (Player player) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            //GsonBuilder builder = new GsonBuilder();
            //builder.registerTypeAdapter(Player.class, new JsonDeserializerWithInheritance<Player>());
            //Gson gson = builder.setPrettyPrinting().create();
            String lines = Files.readString(path);
            LinkedList<Player> playersList = new LinkedList<>();
            // Solo leeremos elementos si el json no está vacío
            if (gson.fromJson(lines, LinkedList.class) != null) {
                playersList = gson.fromJson(lines, LinkedList.class);
            }
            playersList.add(player);
            /*
            int i = 0;
            for (Player player1: playersList) {
                Player newPlayer;
                if (player1 instanceof Engineer engineer) {
                    newPlayer = engineer;
                }else if (player1 instanceof Master) {
                    newPlayer = new Master(player1.getName(), player1.getPI());
                } else {
                    newPlayer = new Doctor(player1.getName(), player1.getPI());
                }
                playersList.set(i, newPlayer);
                i++;
            }
             */
            String jsonData = gson.toJson(playersList, LinkedList.class);
            Files.write(path, jsonData.getBytes());
            return true;
        }catch (IOException e) {
            return false;
        }
    }

    /**
     * Lee todos los elementos de un fichero JSON
     * @return Lista con los objetos de todos los elementos leídos
     */
    @Override
    public LinkedList<Player> readAll () {
        try{
            Gson gson = new Gson();
            String lines = Files.readString(path);
            Type listType = new TypeToken<List<Player>>(){}.getType();
            List<Player> playersList = new LinkedList<>();
            if (gson.fromJson(lines, listType) != null) {
                playersList = gson.fromJson(lines, listType);
            }
            return new LinkedList<>(playersList);
        } catch (IOException e) {
            return new LinkedList<>();
        }
    }

    /**
     * Elimina un dato en una posición del fichero
     * @param index posición del dato a eliminar
     * @return booleano que indica si se ha eliminado correctamente
     */
    @Override
    public boolean delete (int index) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Player> players = readAll();
            players.remove(index);
            String jsonData = gson.toJson(players, List.class);
            Files.write(path, jsonData.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza una posición del fichero
     * @param index Posición del dato que se quiere modificar
     * @param player Nuevo objeto que quiere escribirse en la posicion
     * @return booleano que indica si se ha modificado correctamente
     */
    @Override
    public boolean changeLine (int index, Player player) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Player> players = readAll();
            players.remove(index);
            players.add(index, player);
            String jsonData = gson.toJson(players, List.class);
            Files.write(path, jsonData.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Player deserialize (String texto) {
        Gson gson = new Gson();
        return gson.fromJson(texto, Player.class);
    }

    @Override
    public String serialize(Player player) {
        Gson gson = new Gson();
        return gson.toJson(player);
    }

    /**
     * Vacía por completo del fichero de jugadores
     * @return booleano que indica si se ha vaciado el fichero correctamente
     */
    @Override
    public boolean emptyFile () {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<String> emptyList = new LinkedList<>();
            String jsonData = gson.toJson(emptyList, List.class);
            Files.write(path, jsonData.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
