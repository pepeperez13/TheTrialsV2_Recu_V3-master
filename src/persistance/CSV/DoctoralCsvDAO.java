package persistance.CSV;

import business.trialsTypes.DoctoralThesis;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que gestiona la lectura y escritura del fichero CSV de los doctorals
 * @author José Perez
 * @author Abraham Cedeño
 */
public class DoctoralCsvDAO implements persistance.DoctoralDAO {
    private static final String separator = ",";
    private final String fileName = "doctorals.csv";
    private final String filePath = "files";
    private File file = new File(filePath, fileName);

    /**
     * Método constructor que crea un fichero CSV nuevo, en caso de no existir
     */
    public DoctoralCsvDAO () {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Convierte un objeto de tipo DoctoralThesis a un String de CSV
     * @param doctoralThesis objeto a convertir a string
     * @return string del objeto
     */
    private String doctoralToCsv(DoctoralThesis doctoralThesis) {
        return doctoralThesis.getName() + separator + doctoralThesis.getFieldOfStudy() + separator + doctoralThesis.getDifficulty() + separator + doctoralThesis.isInUse();
    }

    /**
     * Método que crea un objeto de DoctoralThesis a partir de una línea de CSV
     * @param csv Línea que queremos convertir
     * @return Doctoral pedido
     */
    private DoctoralThesis doctoralFromCsv (String csv) {
        String[] parts = csv.split(separator);
        return new DoctoralThesis(parts[0], parts[1], Integer.parseInt(parts[2]), Boolean.valueOf(parts[3]));
    }

    /**
     * Crea un nuevo DoctoralThesis y lo escribe en el fichero
     * @param doctoralThesis doctoral que se desea escribir
     * @return booleano que indica si se ha escrito correctamente
     */
    @Override
    public boolean create(DoctoralThesis doctoralThesis) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            list.add(doctoralToCsv(doctoralThesis));
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
    public LinkedList<DoctoralThesis> readAll() {
        try{
            LinkedList<DoctoralThesis> doctorals = new LinkedList<>();
            List<String> list = Files.readAllLines(file.toPath());
            for (String line: list) {
                doctorals.add(doctoralFromCsv(line));
            }
            return doctorals;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Obtiene el objeto a través de la posición en la que está escrito en el fichero
     * @param index posición en el fichero
     * @return objeto del Doctoral solicitado
     */
    @Override
    public DoctoralThesis findByIndex(int index) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            return doctoralFromCsv(list.get(index-1));
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
            List<String> doctorals = Files.readAllLines(file.toPath());
            doctorals.remove(index);
            Files.write(file.toPath(), doctorals);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Actualiza una línea del fichero
     * @param index Posición de la línea a modificar
     * @param doctoral Nuevo objeto que quiere escribirse en la línea
     * @return booleano que indica si se ha modificado correctamente
     */
    @Override
    public boolean changeLine (int index, DoctoralThesis doctoral) {
        try {
            List<String> doctorals = Files.readAllLines(file.toPath());
            doctorals.remove(index);
            doctorals.add(index, doctoralToCsv(doctoral));
            Files.write(file.toPath(), doctorals);
            return true;
        }catch (IOException e) {
            return false;
        }
    }

}
