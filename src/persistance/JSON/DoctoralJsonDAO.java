package persistance.JSON;

import business.trialsTypes.DoctoralThesis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import persistance.DoctoralDAO;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;


/**
 * Clase que gestiona la lectura y escritura del fichero JSON de los doctorals
 * @author José Perez
 * @author Abraham Cedeño
 */
public class DoctoralJsonDAO implements DoctoralDAO {
    private final String filename = "doctorals.json";
    private static final String route = "files/doctorals.json";
    private static final Path path = Path.of(route);
    private File file = new File("files", filename);

    /**
     * Método constructor que crea un fichero JSON nuevo, en caso de no existir
     */
    public DoctoralJsonDAO ()  {
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
     * Crea un nuevo DoctoralThesis y lo escribe en el fichero
     * @param doctoralThesis doctoral que se desea escribir
     * @return booleano que indica si se ha escrito correctamente
     */
    @Override
    public boolean create (DoctoralThesis doctoralThesis) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String lines = Files.readString(path);
            LinkedList<DoctoralThesis> doctoralsList = new LinkedList<>();
            // Solo leeremos elementos si el json no está vacío
            if (gson.fromJson(lines, LinkedList.class) != null) {
                doctoralsList = gson.fromJson(lines, LinkedList.class);
            }
            doctoralsList.add(doctoralThesis);
            String jsonData = gson.toJson(doctoralsList, LinkedList.class);
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
    public LinkedList<DoctoralThesis> readAll() {
        try{
            Gson gson = new Gson();
            String lines = Files.readString(path);
            Type listType = new TypeToken<List<DoctoralThesis>>(){}.getType();
            List<DoctoralThesis> doctoralsList = new LinkedList<>();
            if (gson.fromJson(lines, listType) != null) {
                doctoralsList = gson.fromJson(lines, listType);
            }
            return new LinkedList<>(doctoralsList);
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
    public DoctoralThesis findByIndex(int index) {
        try{
            Gson gson = new Gson();
            String lines = Files.readString(path);
            Type listType = new TypeToken<List<DoctoralThesis>>(){}.getType();
            List<DoctoralThesis> doctoralsList = gson.fromJson(lines, listType);
            return doctoralsList.get(index - 1);
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
    public boolean delete(int index) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<DoctoralThesis> doctorals = readAll();
            String jsonData = gson.toJson(doctorals, List.class);
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
     * @param doctoral Nuevo objeto que quiere escribirse en la posicion
     * @return booleano que indica si se ha modificado correctamente
     */
    @Override
    public boolean changeLine (int index, DoctoralThesis doctoral) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<DoctoralThesis> doctorals = readAll();
            doctorals.remove(index);
            doctorals.add(index, doctoral);
            String jsonData = gson.toJson(doctorals, List.class);
            Files.write(path, jsonData.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
