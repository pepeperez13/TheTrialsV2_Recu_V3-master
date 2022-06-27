package business;

import business.trialsTypes.GenericTrial;
import persistance.*;
import persistance.CSV.TrialsCsvDAO;
import persistance.JSON.TrialsJsonDAO;

import java.util.ArrayList;
import java.util.LinkedList;

public class TrialsManager {
    private TrialsDAO trialsDAO;

    /**
     * Método constructor que crea un nuevo manager, relacionandolo con CSV o JSON
     * @param options Opción que indica si los datos se guardan en formato CSV o JSON
     */
    public TrialsManager(DataSourceOptions options) {
        switch (options) {
            case JSON -> {
                trialsDAO = new TrialsJsonDAO();
            }
            case CSV -> {
                trialsDAO = new TrialsCsvDAO();
            }
        }
    }


    /**
     * Método que permite guardar una nueva prueba del tipo genérico
     */
    public void addTrial (GenericTrial genericTrial) {
        trialsDAO.create(genericTrial);

    }

    // Devuelve la prueba especifica segun su nombre
    public GenericTrial getTrialByName (String name) {
        LinkedList<GenericTrial> trials = getTrials();
        GenericTrial genericTrial = null;

        // Encontramos la prueba entre todos los genéricos
        for (GenericTrial trial: trials) {
            if (trial.getName().equals(name)) {
                return trial;
            }
        }

        return  null;
    }

    public GenericTrial getTrialByIndex (int index) {
        return getTrialByName(trialsDAO.findByIndex(index).getName());
    }

   // Devuelve la posicion de la prueba en su dao especifico segun su nombre
    public int getIndexByName (String name) {
        LinkedList<GenericTrial> trials = getTrials();
        GenericTrial genericTrial = null;

        // Encontramos la prueba entre todos los genéricos
        for (int i = 0; i < trials.size(); i++) {
            if (trials.get(i).getName().equals(name)) {
                return i;
            }
        }

        return 0;
    }


    /**
     * Método que obtiene los nombres de todas las pruebas existentes
     * @return Lista con los nombres de todas las pruebas
     */
    public LinkedList<String> getAllTrialsNames() {
        LinkedList<GenericTrial> trials = trialsDAO.readAll();
        LinkedList<String> nombres = new LinkedList<>();
        for (GenericTrial trial : trials) {
            nombres.add(trial.getName());
        }
        return nombres;
    }

    // Elimina el trial que nos manden (hasta ahora no se usaba, pero se uede mirar de sustituir por otros deletes)
    public boolean deleteTrial (GenericTrial genericTrial) {
        return trialsDAO.delete(getIndexByName(genericTrial.getName()));
    }

    public boolean isInUse (GenericTrial genericTrial) {
        return genericTrial.getInUse();
    }

    /**
     * Según una lista de índices, devuelve un array con los nombres de las pruebas que se encuentran en dichos indices
     * @param indexes indices de las pruebas que se quiere obtener su nombre
     * @return Array con los nombres de las pruebas
     */
    public String[] getTrialsNamesByIndexes (ArrayList<Integer> indexes) {
        LinkedList<String> allNames = getAllTrialsNames(); // Obtenemos los nombres de todas las pruebas disponibles
        LinkedList<String> names = new LinkedList<>();  // Array de strings donde se guardaran los nombres que necesitemos

        for (int i = 0; i < indexes.size(); i++) {
            names.add(allNames.get(indexes.get(i))) ;
        }
        String[] stringNames = new String[names.size()];
        for (int i = 0; i < names.size(); i++) {
            stringNames[i] = names.get(i);
        }
        return stringNames;
    }


    /**
     * Obtiene una prueba genérica de entre todas las existentes
     * @param index posicion en la lista de la prueba que se quiere obtener
     * @return prueba que se estaba buscando
     */
    public GenericTrial getGenericalTrial (int index) {
        return trialsDAO.findByIndex(index);
    }

    /**
     * Método que obtiene una lista con todas las pruebas existentes
     * @return Lista con las pruebas existentes
     */
    public LinkedList<GenericTrial> getTrials () {

        return trialsDAO.readAll();
    }

    /**
     * Método que busca si existe una prueba
     * @param name nombre de la prueba de la que se busca su existencia
     * @return booleano que indica si existe la prueba o no
     */
    public boolean checkExistance (String name) {
        return getAllTrialsNames().contains(name);
    }


    public void setUsageByName (String name, boolean usage) {
        GenericTrial trial = getTrialByName(name);
        int index = getIndexByName(name);

        trial.setUsage(usage);
        trialsDAO.changeLine(index, trial);
    }

}
