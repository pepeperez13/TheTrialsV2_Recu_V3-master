package persistance.CSV;

import business.trialsTypes.Budget;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que gestiona la lectura y escritura del fichero CSV de los budget
 * @author José Perez
 * @author Abraham Cedeño
 */
public class BudgetCsvDAO implements persistance.BudgetDAO {
    private static final String separator = ",";
    private final String fileName = "budgets.csv";
    private final String filePath = "files";
    private File file = new File(filePath, fileName);

    /**
     * Método constructor que crea un fichero CSV nuevo, en caso de no existir
     */
    public BudgetCsvDAO () {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Convierte un objeto de tipo Budget a un String de CSV
     * @param budget objeto a convertir a string
     * @return string del objeto
     */
    private String budgetToCsv(Budget budget) {
        return budget.getNameTrial() + separator + budget.getNameEntity() + separator + budget.getAmount() + separator + budget.isInUse();
    }

    /**
     * Método que crea un objeto de Budget a partir de una línea de CSV
     * @param csv Línea que queremos convertir
     * @return Budget pedido
     */
    private Budget budgetFromCsv (String csv) {
        String[] parts = csv.split(separator);
        return new Budget(parts[0], parts[1], Integer.parseInt(parts[2]), Boolean.valueOf(parts[3]));
    }

    /**
     * Crea un nuevo budget y lo escribe en el fichero
     * @param budget budget que se desea escribir
     * @return booleano que indica si se ha escrito correctamente
     */
    @Override
    public boolean create(Budget budget) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            list.add(budgetToCsv(budget));
            Files.write(file.toPath(), list);
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
    public LinkedList<Budget> readAll() {
        try{
            LinkedList<Budget> budgets = new LinkedList<>();
            List<String> list = Files.readAllLines(file.toPath());
            for (String line: list) {
                budgets.add(budgetFromCsv(line));
            }
            return budgets;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Obtiene el objeto a través de la posición en la que está escrito en el fichero
     * @param index posición en el fichero
     * @return objeto del budget solicitado
     */
    @Override
    public Budget findByIndex(int index) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            return budgetFromCsv(list.get(index-1));
        } catch (IOException e) {
            return null;
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
            List<String> budgets = Files.readAllLines(file.toPath());
            budgets.remove(index);
            Files.write(file.toPath(), budgets);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Actualiza una línea del fichero
     * @param index Posición de la línea a modificar
     * @param budget Nuevo objeto que quiere escribirse en la línea
     * @return booleano que indica si se ha modificado correctamente
     */
    @Override
    public boolean changeLine (int index, Budget budget) {
        try {
            List<String> budgets = Files.readAllLines(file.toPath());
            budgets.remove(index);
            budgets.add(index, budgetToCsv(budget));
            Files.write(file.toPath(), budgets);
            return true;
        }catch (IOException e) {
            return false;
        }
    }

}

