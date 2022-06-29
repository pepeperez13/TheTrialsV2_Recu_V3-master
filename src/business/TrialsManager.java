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
            case JSON -> trialsDAO = new TrialsJsonDAO();
            case CSV -> trialsDAO = new TrialsCsvDAO();
        }
    }


    /**
     * Método que permite guardar una nueva prueba del tipo genérico
     */
    public void addTrial (GenericTrial genericTrial) {
        trialsDAO.create(genericTrial);

    }

    /**
     * Método que permite obtener un objeto de una prueba segun su nombre
     * @param name nombre de la prueba que se busca
     * @return Objeto de la prueba
     */
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

    /**
     * Método que permite obtener una prueba según su indice (posicion en el fichero)
     * @param index posicion de la prueba en el fichero
     * @return objeto de la prueba buscada
     */
    public GenericTrial getTrialByIndex (int index) {
        return getTrialByName(trialsDAO.findByIndex(index).getName());
    }


    /**
     * Metodo que permite saber la posición de una prueba en el fichero a partir de su nombre
     * @param name nombre de la prueba
     * @return posicion de la prueba en el fichero
     */
    private int getIndexByName (String name) {
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

    /**
     * Elimina una prueba especifica de los ficheros
     * @param genericTrial prueba que queremos eliminar
     */
    public void deleteTrial (GenericTrial genericTrial) {
        trialsDAO.delete(getIndexByName(genericTrial.getName()));
    }

    /**
     * Permite saber se una prueba está en uso por alguna edición
     * @param genericTrial prueba cuyo uso se quiere conocer
     * @return true si está en uso, false si no está en uso
     */
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


    /**
     * Método que pemite poner en uso/no uso de una prueba según su nombre
     * @param name nombre de la prueba cuyo uso se quiere modificar
     * @param usage indica si se quiere poner en uso o en desuso
     */
    public void setUsageByName (String name, boolean usage) {
        GenericTrial trial = getTrialByName(name);
        int index = getIndexByName(name);

        trial.setUsage(usage);
        trialsDAO.changeLine(index, trial);
    }

}
