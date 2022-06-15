package persistance.JSON;


import business.trialsTypes.MasterStudies;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import persistance.MasterDAO;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que gestiona la lectura y escritura del fichero JSON de los Master
 * @author José Perez
 * @author Abraham Cedeño
 */
public class MasterJsonDAO implements MasterDAO {
    private final String filename = "masters.json";
    private static final String route = "files/masters.json";
    private static final Path path = Path.of(route);
    private File file = new File("files", filename);

    /**
     * Método constructor que crea un fichero CSV nuevo, en caso de no existir
     */
    public MasterJsonDAO () {
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
     * Crea un nuevo master y lo escribe en el fichero
     * @param masterStudies master que se desea escribir
     * @return booleano que indica si se ha escrito correctamente
     */
    @Override
    public boolean create(MasterStudies masterStudies) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String lines = Files.readString(path);
            LinkedList<MasterStudies> mastersList = new LinkedList<>();
            // Solo leeremos elementos si el json no está vacío
            if (gson.fromJson(lines, LinkedList.class) != null) {
                mastersList = gson.fromJson(lines, LinkedList.class);
            }
            mastersList.add(masterStudies);
            String jsonData = gson.toJson(mastersList, LinkedList.class);
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
    public LinkedList<MasterStudies> readAll() {
        try{
            Gson gson = new Gson();
            String lines = Files.readString(path);
            Type listType = new TypeToken<List<MasterStudies>>(){}.getType();
            List<MasterStudies> mastersList = new LinkedList<>();
            if (gson.fromJson(lines, listType) != null) {
                mastersList = gson.fromJson(lines, listType);
            }
            return new LinkedList<>(mastersList);
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
    public MasterStudies findByIndex(int index) {
        try{
            Gson gson = new Gson();
            String lines = Files.readString(path);
            Type listType = new TypeToken<List<MasterStudies>>(){}.getType();
            List<MasterStudies> mastersList = gson.fromJson(lines, listType);
            return mastersList.get(index - 1);
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
    public boolean delete(int index) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<MasterStudies> masters = readAll();
            masters.remove(index);
            String jsonData = gson.toJson(masters, List.class);
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
     * @param master Nuevo objeto que quiere escribirse en la posicion
     * @return booleano que indica si se ha modificado correctamente
     */
    @Override
    public boolean changeLine(int index, MasterStudies master) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<MasterStudies> masters = readAll();
            masters.remove(index);
            masters.add(index, master);
            String jsonData = gson.toJson(masters, List.class);
            Files.write(path, jsonData.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
