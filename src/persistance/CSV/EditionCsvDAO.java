package persistance.CSV;

import business.Edition;
import persistance.EditionDAO;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que gestiona la lectura y escritura del fichero CSV de las ediciones
 * @author José Perez
 * @author Abraham Cedeño
 */
public class EditionCsvDAO implements EditionDAO {
    private static final String separator = ",";
    private final String fileName = "editions.csv";
    private final String filePath = "files";
    private File file = new File(filePath, fileName);

    /**
     * Método constructor que crea un fichero CSV nuevo, en caso de no existir
     */
    public EditionCsvDAO () {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Este método transforma un elemento de la clase Edition
     * para darle el formato requerido
     * @param edition edicion a transformar
     * @return String
     */
    private String editionToCsv (Edition edition) {
        return (edition.getYear() + separator + edition.getNumPlayers() + separator + edition.getNumTrials() + separator + String.join("-", edition.getTrialNames()));
    }

    /**
     * Este método divide una cadena de caracteres
     * y llama al constructor Edition() para asi obtener un
     * nuevo objeto
     * @param csv Cadena a separar
     * @return Edition Objeto obtenido despues de separa la cadena
     */
    private Edition editionFromCsv (String csv) {
        String[] parts = csv.split(separator);
        return new Edition(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3].split("-"));
    }

    /**
     * Método, público, que se encarga de llamar al método  para guardar un objeto de la clase Edition
     * en un fichero .CSV
     * @param edition Objeto a guardar
     * @return boolean Retorna si se ha podido o no guardar en el fichero
     */
    @Override
    public boolean create(Edition edition) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            list.add(editionToCsv(edition));
            Files.write(file.toPath(), list);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Método, público, que se encarga de llamar al método que se encarga de leer todos las lineas del .csv
     * @return Retorna una lista que contiene todas las ediciones
     */
    @Override
    public LinkedList<Edition> readAll() {
        try{
            LinkedList<Edition> editions = new LinkedList<>();
            List<String> list = Files.readAllLines(file.toPath());
            for (String line: list) {
                editions.add(editionFromCsv(line));
            }
            return editions;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Este método nos permite obtener una edicion en concreto
     * @param index Indice que nos indica la posicion del objeto deseado
     * @return Edition Retorna la edición deseada
     */
    @Override
    public Edition findByIndex(int index) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            return editionFromCsv(list.get(index-1));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Método que elimina una edición según el año
     * @param index Entero que nos indica la edición a eliminar
     * @return boolean Indica si se ha podido o no eliminar una edición
     */
    @Override
    public boolean delete(int index) {
        try {
            List<String> editions = Files.readAllLines(file.toPath());
            editions.remove(index);
            Files.write(file.toPath(), editions);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
