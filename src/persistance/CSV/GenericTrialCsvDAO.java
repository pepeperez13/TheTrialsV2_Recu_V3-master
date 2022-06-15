package persistance.CSV;

import business.TrialTypeOptions;
import business.trialsTypes.GenericTrial;
import persistance.GenericTrialDAO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que gestiona la lectura y escritura del fichero CSV de los GenericTrial
 * @author José Perez
 * @author Abraham Cedeño
 */
public class GenericTrialCsvDAO implements GenericTrialDAO {

    private static final String separator = ",";
    private final String fileName = "generics.csv";
    private final String filePath = "files";
    private File file = new File(filePath, fileName);

    /**
     * Método constructor que crea un fichero CSV nuevo, en caso de no existir
     */
    public GenericTrialCsvDAO () {
        if (!file.exists() ) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Convierte un objeto de tipo GenericTrial a un String de CSV
     * @param trial objeto a convertir a string
     * @return string del objeto
     */
    private String genericTrialToCsv (GenericTrial trial) {
        return trial.getName() + separator + trial.getType();
    }

    /**
     * Método que crea un objeto de GenericTrial a partir de una línea de CSV
     * @param csv Línea que queremos convertir
     * @return GenericTrial pedido
     */
    private GenericTrial genericFromCsv (String csv) {
        String[] parts = csv.split(separator);
        return new GenericTrial(parts[0], TrialTypeOptions.valueOf(parts[1]));
    }

    /**
     * Crea un nuevo GenericTrial y lo escribe en el fichero
     * @param generic GenericTrial que se desea escribir
     * @return booleano que indica si se ha escrito correctamente
     */
    @Override
    public boolean create(GenericTrial generic)  {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            list.add(genericTrialToCsv(generic));
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
    public LinkedList<GenericTrial> readAll() {
        try{
            LinkedList<GenericTrial> genericTrials = new LinkedList<>();
            List<String> list = Files.readAllLines(file.toPath());
            for (String line: list) {
                genericTrials.add(genericFromCsv(line));
            }
            return genericTrials;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Obtiene el objeto a través de la posición en la que está escrito en el fichero
     * @param index posición en el fichero
     * @return objeto del GenericTrial solicitado
     */
    @Override
    public GenericTrial findByIndex(int index) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            return genericFromCsv(list.get(index - 1));
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
            List<String> genericTrials = Files.readAllLines(file.toPath());
            genericTrials.remove(index);
            Files.write(file.toPath(), genericTrials);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
