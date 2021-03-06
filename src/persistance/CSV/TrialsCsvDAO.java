package persistance.CSV;

import business.trialsTypes.*;
import persistance.TrialsDAO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que gestiona la lectura y escritura del fichero CSV de los Master
 * @author José Perez
 * @author Abraham Cedeño
 */
public class TrialsCsvDAO implements TrialsDAO {
    private static final String separator = ",";
    private static final String fileName = "trials.csv";
    private static final String filePath = "files";
    private static File file = new File(filePath, fileName);


    /**
     * Método constructor que crea un fichero CSV nuevo, en caso de no existir
     */
    public TrialsCsvDAO () {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Convierte cualquier objeto de una prueba, a una linea de CSV, distinguiendo entre tipos de prueba
     * @param genericTrial objeto a convertir a string
     * @return string del objeto
     */
    private String trialToCsv (GenericTrial genericTrial) {
        // Distinguimos entre los diferentes tipos de prueba y pasamos a csv
        if (genericTrial instanceof PaperPublication article) {
            return article.getArticleName() + separator + article.getMagazineName() + separator + article.getQuartile() +
                    separator + article.getAcceptedProbability() + separator + article.getRevisedProbability() +
                    separator + article.getRejectedProbability() + separator + article.InUse() + separator + article.getType();
        } else if (genericTrial instanceof MasterStudies masterStudies) {
            return masterStudies.getName() + separator + masterStudies.getNom() + separator + masterStudies.getNumberCredits() +
                    separator + masterStudies.getProbability() + separator + masterStudies.isInUse() + separator + masterStudies.getType();
        } else if (genericTrial instanceof DoctoralThesis doctoralThesis) {
            return doctoralThesis.getName() + separator + doctoralThesis.getFieldOfStudy() + separator + doctoralThesis.getDifficulty() + separator + doctoralThesis.isInUse() + separator + doctoralThesis.getType();
        } else {
            Budget budget = (Budget) genericTrial;
            return budget.getNameTrial() + separator + budget.getNameEntity() + separator + budget.getAmount() + separator + budget.isInUse() + separator + budget.getType();
        }
    }


    /**
     * Método que crea un objeto de PaperPublication a partir de una línea de CSV
     * @param csv Línea que queremos convertir
     * @return PaperPublication pedido
     */
    private PaperPublication paperFromCsv(String csv) {
        String[] parts = csv.split(separator);
        return new PaperPublication(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Boolean.valueOf(parts[6]));
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
     * Método que crea un objeto de DoctoralThesis a partir de una línea de CSV
     * @param csv Línea que queremos convertir
     * @return Doctoral pedido
     */
    private DoctoralThesis doctoralFromCsv (String csv) {
        String[] parts = csv.split(separator);
        return new DoctoralThesis(parts[0], parts[1], Integer.parseInt(parts[2]), Boolean.valueOf(parts[3]));
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
     * Escribe una nueva prueba en el fichero
     * @param genericTrial prueba que se desea escribir
     * @return booleano que indica si se ha escrito correctamente
     */
    @Override
    public boolean create(GenericTrial genericTrial) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            list.add(trialToCsv(genericTrial));
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
            LinkedList<GenericTrial> trials = new LinkedList<>();
            List<String> list = Files.readAllLines(file.toPath());
            // Convertimos uno a uno de csv a objeto, segun el tipo y los añadimos a la lista
            for (String line: list) {
                if (line.contains("PAPER")) {
                    trials.add(paperFromCsv(line));
                } else if (line.contains("MASTER")) {
                    trials.add(masterFromCsv(line));
                } else if (line.contains("DOCTORAL")) {
                    trials.add(doctoralFromCsv(line));
                } else {
                    trials.add(budgetFromCsv(line));
                }
            }
            return trials;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Obtiene el objeto a través de la posición en la que está escrito en el fichero
     * @param index posición en el fichero
     * @return objeto de la prueba solicitada
     */
    @Override
    public GenericTrial findByIndex(int index) {
        try {
            List<String> list = Files.readAllLines(file.toPath());
            String line = list.get(index-1);
            // Convertimos a objeto según el tipo
            if (line.contains("PAPER")) {
                return paperFromCsv(line);
            } else if (line.contains("MASTER")) {
                return masterFromCsv(line);
            } else if (line.contains("DOCTORAL")) {
                return doctoralFromCsv(line);
            } else {
                return budgetFromCsv(line);
            }
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
            List<String> trials = Files.readAllLines(file.toPath());
            trials.remove(index);
            Files.write(file.toPath(), trials);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Actualiza una línea del fichero
     * @param index Posición de la línea a modificar
     * @param genericTrial Nuevo objeto que quiere escribirse en la línea
     * @return booleano que indica si se ha modificado correctamente
     */
    @Override
    public boolean changeLine (int index, GenericTrial genericTrial) {
        try {
            List<String> trials = Files.readAllLines(file.toPath());
            trials.remove(index);
            trials.add(index, trialToCsv(genericTrial));
            Files.write(file.toPath(), trials);
            return true;
        }catch (IOException e) {
            return false;
        }
    }


}

