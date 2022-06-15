package persistance.CSV;

import business.trialsTypes.MasterStudies;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que gestiona la lectura y escritura del fichero CSV de los Master
 * @author José Perez
 * @author Abraham Cedeño
 */
public class MasterCsvDAO implements persistance.MasterDAO {
    private static final String separator = ",";
    private final String fileName = "masters.csv";
    private final String filePath = "files";
    private File file = new File(filePath, fileName);

    /**
     * Método constructor que crea un fichero CSV nuevo, en caso de no existir
     */
    public MasterCsvDAO () {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Convierte un objeto de tipo Master a un String de CSV
     * @param masterStudies objeto a convertir a string
     * @return string del objeto
     */
    private String masterToCsv(MasterStudies masterStudies) {
        return masterStudies.getName() + separator + masterStudies.getNom() + separator + masterStudies.getNumberCredits() +
                separator + masterStudies.getProbability() + separator + masterStudies.isInUse();
    }

    /**
     * Método que crea un objeto de Master a partir de una línea de CSV
     * @param csv Línea que queremos convertir
     * @return Master pedido
     */
    private MasterStudies masterFromCsv (String csv) {
        String[] parts = csv.split(separator);
        return new MasterStudies(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Boolean.valueOf(parts[4]));
    }

    /**
     * Crea un nuevo master y lo escribe en el fichero
     * @param masterStudies master que se desea escribir
     * @return booleano que indica si se ha escrito correctamente
     */
    @Override
    public boolean create(MasterStudies masterStudies) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            list.add(masterToCsv(masterStudies));
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
    public LinkedList<MasterStudies> readAll() {
        try{
            LinkedList<MasterStudies> masters = new LinkedList<>();
            List<String> list = Files.readAllLines(file.toPath());
            for (String line: list) {
                masters.add(masterFromCsv(line));
            }
            return masters;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Obtiene el objeto a través de la posición en la que está escrito en el fichero
     * @param index posición en el fichero
     * @return objeto del Master solicitado
     */
    @Override
    public MasterStudies findByIndex(int index) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            return masterFromCsv(list.get(index-1));
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
     * @param masterStudies Nuevo objeto que quiere escribirse en la línea
     * @return booleano que indica si se ha modificado correctamente
     */
    @Override
    public boolean changeLine (int index, MasterStudies masterStudies) {
        try {
            List<String> masters = Files.readAllLines(file.toPath());
            masters.remove(index);
            masters.add(index, masterToCsv(masterStudies));
            Files.write(file.toPath(), masters);
            return true;
        }catch (IOException e) {
            return false;
        }
    }


}
