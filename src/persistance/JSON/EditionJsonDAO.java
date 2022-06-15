package persistance.JSON;

import business.Edition;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import persistance.EditionDAO;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que gestiona la lectura y escritura del fichero JSON de las ediciones
 * @author José Perez
 * @author Abraham Cedeño
 */
public class EditionJsonDAO implements EditionDAO {
    private final String filename = "editions.json";
    private static final String route = "files/editions.json";
    private static final Path path = Path.of(route);
    private File file = new File("files", filename);

    /**
     * Método constructor que crea un fichero CSV nuevo, en caso de no existir
     */
    public EditionJsonDAO () {
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
     * Crea una nueva edición y la escribe en el fichero
     * @param edition Objeto a guardar
     * @return boolean Retorna si se ha podido o no guardar en el fichero
     */
    @Override
    public boolean create (Edition edition) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String lines = Files.readString(path);
            LinkedList<Edition> editionsList = new LinkedList<>();
            // Solo leeremos elementos si el json no está vacío
            if (gson.fromJson(lines, LinkedList.class) != null) {
                editionsList = gson.fromJson(lines, LinkedList.class);
            }
            editionsList.add(edition);
            String jsonData = gson.toJson(editionsList, LinkedList.class);
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
    public LinkedList<Edition> readAll() {
        try{
            Gson gson = new Gson();
            String lines = Files.readString(path);
            Type listType = new TypeToken<List<Edition>>(){}.getType();
            List<Edition> editionsList = new LinkedList<>();
            if (gson.fromJson(lines, listType) != null) {
                editionsList = gson.fromJson(lines, listType);
            }
            return new LinkedList<>(editionsList);
        } catch (IOException e) {
            return new LinkedList<>();
        }
    }

    /**
     * Obtiene el objeto a través de la posición en la que está escrito en el fichero
     * @param index posición en el fichero
     * @return objeto del Doctoral solicitado
     */
    @Override
    public Edition findByIndex (int index) {
        try{
            Gson gson = new Gson();
            String lines = Files.readString(path);
            Type listType = new TypeToken<List<Edition>>(){}.getType();
            List<Edition> editionsList = gson.fromJson(lines, listType);
            return editionsList.get(index - 1);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Elimina un dato del fichero
     * @param index posición del dato que se quiere eliminar
     * @return booleano que indica si se ha eliminado correctamente
     */
    @Override
    public boolean delete (int index) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Edition> editions = readAll();
            editions.remove(index);
            String jsonData = gson.toJson(editions, List.class);
            Files.write(path, jsonData.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
