package Basura;

import business.DataSourceOptions;
import business.TrialTypeOptions;
import business.trialsTypes.GenericTrial;
import persistance.CSV.GenericTrialCsvDAO;
import persistance.GenericTrialDAO;
import persistance.JSON.GenericTrialJsonDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Controla aquello relacionado con las pruebas de tipo Generic (cualquier tipo de prueba)
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class GenericTrialManager {
    private GenericTrialDAO trialsDAO;

    /**
     * Método constructor que crea un nuevo manager, relacionandolo con CSV o JSON
     * @param options Opción que indica si los datos se guardan en formato CSV o JSON
     */
    public GenericTrialManager(DataSourceOptions options) throws IOException {
        switch (options) {
            case JSON -> trialsDAO = new GenericTrialJsonDAO();
            case CSV -> trialsDAO= new GenericTrialCsvDAO();
        }
    }

    /**
     * Método que permite guardar una nueva prueba del tipo genérico
     * @param name Nombre de la prueba
     * @param type Tipo de la prueba
     */
    public void addTrial (String name, TrialTypeOptions type) {
        GenericTrial genericTrial = new GenericTrial(name, type, false);
        trialsDAO.create(genericTrial);
    }

    /**
     * Método que obtiene los nombres de todas las pruebas existentes
     * @return Lista con los nombres de todas las pruebas
     */
    public LinkedList<String> getTrialsNames() {
        LinkedList<GenericTrial> trials =  trialsDAO.readAll();
        LinkedList<String> nombres = new LinkedList<>();
        for (int i = 0; i < trials.size(); i++) {
            nombres.add(trials.get(i).getName());
        }
        return nombres;
    }

    /**
     * Según una lista de índices, devuelve un array con los nombres de las pruebas que se encuentran en dichos indices
     * @param indexes indices de las pruebas que se quiere obtener su nombre
     * @return Array con los nombres de las pruebas
     */
    public String[] getTrialsNamesByIndexes (ArrayList<Integer> indexes) {
        LinkedList<String> allNames = getTrialsNames(); // Obtenemos los nombres de todas las pruebas disponibles
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
     * Metodo que permite saber el tipo de una prueba segun su posicion en una lista
     * @param index indice de la prueba que se quiere saber el tipo
     * @return tipo de la prueba buscada
     */
    public TrialTypeOptions getTrialTypeByIndex (int index) {
        LinkedList<GenericTrial> trialsNames = trialsDAO.readAll();
        return trialsNames.get(index-1).getType();
    }

    /**
     * Método que permite saber el tipo de una prueba segun su nombre
     * @param name nombre de la prueba de la que queremos saber el tipo
     * @return tipo de la prueba buscada
     */
    public TrialTypeOptions getTrialTypeByName (String name) {
        boolean found = false;
        int i;
        for (i = 0; i < getTrials().size() && !found; i++) {
            if (getTrials().get(i).getName().equals(name)) {
                found = true;
            }
        }
        return getTrials().get(i-1).getType();
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
        return getTrialsNames().contains(name);
    }

    /**
     * Nos permite saber la posición de guardado de una prueba según su nombre
     * @param name Nombre de la prueba que queremos buscar
     * @return Posición de la prueba solicitada
     */
    public int getIndexByName (String name) {
        int i;
        boolean found = false;
        LinkedList<GenericTrial> genericTrials = trialsDAO.readAll();
        for (i = 0; i < genericTrials.size() && !found; i++) {
            if (genericTrials.get(i).getName().equals(name)) {
                found = true;
            }
        }
        return i - 1;
    }

    /**
     * Elimina una prueba a través de su nombre
     * @param name nombre de la prueba a eliminar
     */
    public void deleteByname (String name) {
        trialsDAO.delete(getIndexByName(name));
    }
}
