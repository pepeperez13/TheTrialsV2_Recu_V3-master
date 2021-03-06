package persistance.JSON;


import business.playerTypes.Doctor;
import business.playerTypes.Engineer;
import business.playerTypes.Master;
import business.playerTypes.Player;
import com.google.gson.*;
import persistance.TeamDAO;

import java.io.File;
import java.io.IOException;
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
    private final String filename = "team.json";
    private static final String route = "files/team.json";
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

            String lines = Files.readString(path);
            LinkedList<Player> playersList = new LinkedList<>();
            // Solo leeremos elementos si el json no está vacío

            if (gson.fromJson(lines, LinkedList.class) != null) {
                playersList = gson.fromJson(lines, LinkedList.class);
            }
            playersList.add(player);
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
            String lines = Files.readString(path);
            JsonElement element = JsonParser.parseString(lines);
            JsonArray array = element.getAsJsonArray();
            Player player;
            List<Player> playersList = new LinkedList<>();

            for (int i = 0; i < array.size(); ++i) {
                JsonObject object = (JsonObject) array.get(i);
                JsonPrimitive type = object.getAsJsonPrimitive("type");
                // Distinguimos entre el tipo para poder guardar en la lista que tipo concreto es
                if (type.getAsString().equals("ENGINEER")) {
                    player = new Engineer(object.getAsJsonPrimitive("name").getAsString(), object.getAsJsonPrimitive("PI").getAsInt());
                } else if (type.getAsString().equals("MASTER")) {
                    player = new Master(object.getAsJsonPrimitive("name").getAsString(), object.getAsJsonPrimitive("PI").getAsInt());
                } else {
                    player = new Doctor(object.getAsJsonPrimitive("name").getAsString(), object.getAsJsonPrimitive("PI").getAsInt());
                }
                playersList.add(player);
            }
            return new LinkedList<>(playersList);
        } catch (IOException e) {
            return new LinkedList<>();
        }
    }

    /**
     * Elimina un jugador para una posición en el fichero
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
     * Actualiza (cambia la información) de un elemento del fichero
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
