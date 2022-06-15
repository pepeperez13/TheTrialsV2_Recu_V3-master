package persistance.CSV;

import business.trialsTypes.PaperPublication;
import persistance.PaperDAO;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que gestiona la lectura y escritura del fichero CSV de los PaperPublication
 * @author José Perez
 * @author Abraham Cedeño
 */
public class PaperCsvDAO implements PaperDAO {
    private static final String separator = ",";
    private final String fileName = "papers.csv";
    private final String filePath = "files";
    private File file = new File(filePath, fileName);

    /**
     * Método constructor que crea un fichero CSV nuevo, en caso de no existir
     */
    public PaperCsvDAO () {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Convierte un objeto de tipo PaperPublication a un String de CSV
     * @param article objeto a convertir a string
     * @return string del objeto
     */
    private String trialToCsv (PaperPublication article) {
        return article.getArticleName() + separator + article.getMagazineName() + separator + article.getQuartile() +
                separator + article.getAcceptedProbability() + separator + article.getRevisedProbability() +
                separator + article.getRejectedProbability() + separator + article.InUse();
    }

    /**
     * Método que crea un objeto de PaperPublication a partir de una línea de CSV
     * @param csv Línea que queremos convertir
     * @return PaperPublication pedido
     */
    private PaperPublication trialFromCsv(String csv) {
        String[] parts = csv.split(separator);

        return new PaperPublication(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Boolean.valueOf(parts[6]));
    }

    /**
     * Crea un nuevo PaperPublication y lo escribe en el fichero
     * @param article article que se desea escribir
     * @return booleano que indica si se ha escrito correctamente
     */
    @Override
    public boolean create (PaperPublication article) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            list.add(trialToCsv(article));
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
    public LinkedList<PaperPublication> readAll ()  {
        try{
            LinkedList<PaperPublication> articles = new LinkedList<>();
            List<String> list = Files.readAllLines(file.toPath());
            for (String line: list) {
                articles.add(trialFromCsv(line));
            }
            return articles;
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
    public boolean delete (int index) {
        try {
            List<String> articles = Files.readAllLines(file.toPath());
            articles.remove(index);
            Files.write(file.toPath(), articles);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Actualiza una línea del fichero
     * @param index Posición de la línea a modificar
     * @param article Nuevo objeto que quiere escribirse en la línea
     * @return booleano que indica si se ha modificado correctamente
     */
    @Override
    public boolean changeLine (int index, PaperPublication article) {
        try {
            List<String> articles = Files.readAllLines(file.toPath());
            articles.remove(index);
            articles.add(index, trialToCsv(article));
            Files.write(file.toPath(), articles);
            return true;
        }catch (IOException e) {
            return false;
        }
    }

}
