package Basura;

import business.DataSourceOptions;
import business.trialsTypes.MasterStudies;
import persistance.CSV.MasterCsvDAO;
import persistance.JSON.MasterJsonDAO;
import persistance.MasterDAO;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Controla aquello relacionado con las pruebas de tipo Master
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class MasterManager {
    private MasterDAO masterDAO;
    private GenericTrialManager genericTrialManager;

    /**
     * Método constructor que crea un nuevo manager, relacionandolo con CSV o JSON
     * @param options Opción que indica si los datos se guardan en formato CSV o JSON
     * @param genericTrialManager Manager del tipo de prueba genérico, con quien se comunica el budget
     */
    public MasterManager(DataSourceOptions options, GenericTrialManager genericTrialManager) throws IOException {
        this.genericTrialManager = genericTrialManager;
        switch (options) {
            case JSON -> masterDAO = new MasterJsonDAO();
            case CSV -> masterDAO = new MasterCsvDAO();
        }
    }

    /**
     * Método que crea una nueva prueba de tipo Master
     * @param name Nombre de la prueba a añadir
     * @param nom Nom de la prueba
     * @param numberCredits Número de creditos del master
     * @param probability Probabilidad de aprobar un crédito
     * @param inUse Indica si la prueba está en uso por alguna edición
     */
    public void addMaster (String name, String nom, int numberCredits, int probability, boolean inUse) {
        MasterStudies masterStudies = new MasterStudies(name, nom, numberCredits, probability, inUse);
        masterDAO.create(masterStudies);
    }

    /**
     * Obtiene una prueba master de entre todos los existentes a partir del nombre
     * @param name Nombre del master a buscar
     * @return Master que se buscaba
     */
    public MasterStudies getMasterByName (String name) {
        int i;
        boolean found = false;
        LinkedList<MasterStudies> masterStudies = masterDAO.readAll();
        for (i = 0; i < masterStudies.size() && !found; i++) {
            if (masterStudies.get(i).getName().equals(name)) {
                found = true;
            }
        }
        return masterStudies.get(i - 1);
    }

    /**
     * Nos permite saber la posición de guardado de una prueba según su nombre
     * @param name Nombre de la prueba que queremos buscar
     * @return Posición de la prueba solicitada
     */
    public int getIndexByName (String name)  {
        int i;
        boolean found = false;
        LinkedList<MasterStudies> masters = masterDAO.readAll();
        for (i = 0; i < masters.size() && !found; i++) {
            if (masters.get(i).getName().equals(name)) {
                found = true;
            }
        }
        return i - 1;
    }

    /**
     * Elimina una prueba master a partir de su indice
     * @param index posicion de la prueba a eliminar
     * @return booleano que indica si se ha eliminado correctamente
     */
    public boolean deleteMaster (int index) {
        return masterDAO.delete(index);
    }

    /**
     * Nos permite saber si una prueba está en uso por alguna edición
     * @param name Nombre de la prueba que queremos saber si está en uso
     * @return booleano que indica si la prueba está en uso o no
     */
    public boolean isInUse (String name) {
        return getMasterByName(name).isInUse();
    }

    /**
     * Este método permite modificar el estado de una prueba a "en uso"
     * @param name Nombre de la prueba que queremos poner en uso
     */
    public void setInUseByName(String name) {
        int index = getIndexByName(name);
        MasterStudies auxMaster = getMasterByName(name);
        MasterStudies masterStudies = new MasterStudies(auxMaster.getName(), auxMaster.getNom(), auxMaster.getNumberCredits(), auxMaster.getProbability(), true);
        masterDAO.changeLine(index, masterStudies);
    }

    /**
     * Este método permite modificar el estado de una prueba a "no en uso"
     * @param name Nombre de la prueba que queremos poner en "no uso"
     */
    public void setInNotUseByName (String name) {
        int index = getIndexByName(name);
        MasterStudies auxMaster = getMasterByName(name);
        MasterStudies masterStudies = new MasterStudies(auxMaster.getName(), auxMaster.getNom(), auxMaster.getNumberCredits(), auxMaster.getProbability(), false);
        masterDAO.changeLine(index, masterStudies);
    }
}
