package persistance.JSON;

import business.trialsTypes.Budget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import persistance.BudgetDAO;

import java.io.*;
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
public class BudgetJsonDAO implements BudgetDAO {
    private final String filename = "budgets.json";
    private static final String route = "files/budgets.json";
    private static final Path path = Path.of(route);
    private File file = new File("files", filename);

    /**
     * Método constructor que crea un fichero JSON nuevo, en caso de no existir
     */
    public BudgetJsonDAO () {
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
     * Crea un nuevo budget y lo escribe en el fichero
     * @param budget budget que se desea escribir
     * @return booleano que indica si se ha escrito correctamente
     */
    @Override
    public boolean create(Budget budget)  {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String lines = Files.readString(path);
            LinkedList<Budget> budgetList = new LinkedList<>();
            // Solo leeremos elementos si el json no está vacío
            if (gson.fromJson(lines, LinkedList.class) != null) {
                budgetList = gson.fromJson(lines, LinkedList.class);
            }
            budgetList.add(budget);
            String jsonData = gson.toJson(budgetList, LinkedList.class);
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
    public LinkedList<Budget> readAll() {
        try{
            Gson gson = new Gson();
            String lines = Files.readString(path);
            Type listType = new TypeToken<List<Budget>>(){}.getType();
            List<Budget> budgetList = new LinkedList<>();
            if (gson.fromJson(lines, listType) != null) {
                budgetList= gson.fromJson(lines, listType);
            }
            return new LinkedList<>(budgetList);
        } catch (IOException e) {
            return new LinkedList<>();
        }
    }

    /**
     * Obtiene el objeto a través de la posición en la que está escrito en el fichero
     * @param index posición en el fichero
     * @return objeto del budget solicitado
     */
    @Override
    public Budget findByIndex(int index) {
        try{
            Gson gson = new Gson();
            String lines = Files.readString(path);
            Type listType = new TypeToken<List<Budget>>(){}.getType();
            List<Budget> budgetList = gson.fromJson(lines, listType);
            return budgetList.get(index - 1);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Elimina un dato del fichero
     * @param index posición del dato a eliminar
     * @return booleano que indica si se ha eliminado correctamente
     */
    @Override
    public boolean delete (int index) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Budget> budgets = readAll();
            budgets.remove(index);
            String jsonData = gson.toJson(budgets, List.class);
            Files.write(path, jsonData.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Actualiza un dato en el fichero
     * @param index Posición del dato a modificar
     * @param budget Nuevo objeto que quiere escribirse en la posición
     * @return booleano que indica si se ha modificado correctamente
     */
    @Override
    public boolean changeLine (int index, Budget budget)  {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Budget> budgets = readAll();
            budgets.remove(index);
            budgets.add(index, budget);
            String jsonData = gson.toJson(budgets, List.class);
            Files.write(path, jsonData.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
