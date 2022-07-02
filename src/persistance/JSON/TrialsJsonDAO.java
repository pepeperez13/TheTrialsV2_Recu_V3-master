package persistance.JSON;

import business.trialsTypes.*;
import com.google.gson.*;
import persistance.TrialsDAO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;


public class TrialsJsonDAO implements TrialsDAO {

    private final String filename = "trials.json";
    private static final String route = "files/trials.json";
    private static final Path path = Path.of(route);
    private File file = new File("files", filename);


    /**
     * Método constructor que crea un fichero CSV nuevo, en caso de no existir
     */
    public TrialsJsonDAO () {
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
     * Recibe una nueva prueba y la escribe en el fichero
     * @param genericTrial prueba que desea guardarse
     * @return booleano que indica si se ha escrito correctamente
     */
    @Override
    public boolean create (GenericTrial genericTrial) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String lines = Files.readString(path);
            LinkedList<GenericTrial> trialsList = new LinkedList<>();
            // Solo leeremos elementos si el json no está vacío
            if (gson.fromJson(lines, LinkedList.class) != null) {
                trialsList = gson.fromJson(lines, LinkedList.class);
            }
            trialsList.add(genericTrial);
            String jsonData = gson.toJson(trialsList, LinkedList.class);
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
    public LinkedList<GenericTrial> readAll() {
        try{
            String lines = Files.readString(path);
            JsonElement element = JsonParser.parseString(lines);
            JsonArray array = element.getAsJsonArray();
            GenericTrial trial;
            List<GenericTrial> trialsList = new LinkedList<>();

            // Para todos los elementos del array de json, los transformamos a objetos de Prueba
            for (int i = 0; i < array.size(); ++i) {
                JsonObject object = (JsonObject) array.get(i);
                trial = fromJsonObjectToTrial(object);
                trialsList.add(trial);
            }
            return new LinkedList<>(trialsList);
        } catch (IOException e) {
            return new LinkedList<>();
        }
    }

    /**
     * Obtiene el objeto a través de la posición en la que está escrito en el fichero
     * @param index posición en el fichero
     * @return objeto del Master solicitado
     */
    @Override
    public GenericTrial findByIndex(int index) {
        try{
            String lines = Files.readString(path);
            JsonElement element = JsonParser.parseString(lines);
            JsonArray array = element.getAsJsonArray();
            GenericTrial trial;
            JsonObject object = (JsonObject) array.get(index - 1);
            trial = fromJsonObjectToTrial(object);
            return trial;
        } catch (IOException e) {
            return null;
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
            List<GenericTrial> trials = readAll();
            trials.remove(index);
            String jsonData = gson.toJson(trials, List.class);
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
     * @param genericTrial Nuevo objeto que quiere escribirse en la posicion
     * @return booleano que indica si se ha modificado correctamente
     */
    @Override
    public boolean changeLine(int index, GenericTrial genericTrial) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<GenericTrial> trials = readAll();
            trials.remove(index);
            trials.add(index, genericTrial);
            String jsonData = gson.toJson(trials, List.class);
            Files.write(path, jsonData.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Método privado que se encarga de convertir un Objeto JSON leído del fichero, a un objeto de un tipo de
     * prueba especifica
     * @param object objeto JSON que se desea convertir
     * @return Objeto de una prueba de cualquier tipo
     */
    private GenericTrial fromJsonObjectToTrial (JsonObject object) {
        GenericTrial trial;
        JsonPrimitive type = object.getAsJsonPrimitive("type");
        // Distinguimos entre el tipo para poder guardar en la lista que tipo concreto es
        if (type.getAsString().equals("PAPER")) {
            trial = new PaperPublication(object.getAsJsonPrimitive("name").getAsString(),
                    object.getAsJsonPrimitive("magazineName").getAsString(),
                    object.getAsJsonPrimitive("quartile").getAsString(),
                    object.getAsJsonPrimitive("acceptedProbability").getAsInt(),
                    object.getAsJsonPrimitive("revisedProbability").getAsInt(),
                    object.getAsJsonPrimitive("rejectedProbability").getAsInt(),
                    object.getAsJsonPrimitive("inUse").getAsBoolean());
        } else if (type.getAsString().equals("MASTER")) {
            trial = new MasterStudies(object.getAsJsonPrimitive("name").getAsString(),
                    object.getAsJsonPrimitive("nom").getAsString(),
                    object.getAsJsonPrimitive("numberCredits").getAsInt(),
                    object.getAsJsonPrimitive("probability").getAsInt(),
                    object.getAsJsonPrimitive("inUse").getAsBoolean());
        } else if (type.getAsString().equals("DOCTORAL")){
            trial = new DoctoralThesis(object.getAsJsonPrimitive("name").getAsString(),
                    object.getAsJsonPrimitive("fieldOfStudy").getAsString(),
                    object.getAsJsonPrimitive("difficulty").getAsInt(),
                    object.getAsJsonPrimitive("inUse").getAsBoolean());
        } else {
            trial = new Budget(object.getAsJsonPrimitive("name").getAsString(),
                    object.getAsJsonPrimitive("nameEntity").getAsString(),
                    object.getAsJsonPrimitive("amount").getAsInt(),
                    object.getAsJsonPrimitive("inUse").getAsBoolean());
        }
        return trial;
    }


}
