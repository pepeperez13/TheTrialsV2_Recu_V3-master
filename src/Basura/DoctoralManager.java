package Basura;

import business.DataSourceOptions;
import business.trialsTypes.DoctoralThesis;
import persistance.CSV.DoctoralCsvDAO;
import persistance.DoctoralDAO;
import persistance.JSON.DoctoralJsonDAO;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Controla aquello relacionado con las pruebas de tipo Doctoral
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class DoctoralManager {
    private DoctoralDAO doctoralDAO;
    private GenericTrialManager genericTrialManager;

    /**
     * Método constructor que crea un nuevo manager, relacionandolo con CSV o JSON
     * @param options Opción que indica si los datos se guardan en formato CSV o JSON
     * @param genericTrialManager Manager del tipo de prueba genérico, con quien se comunica el budget
     */
    public DoctoralManager(DataSourceOptions options, GenericTrialManager genericTrialManager) throws IOException {
        this.genericTrialManager = genericTrialManager;
        switch (options) {
            case JSON -> doctoralDAO = new DoctoralJsonDAO();
            case CSV -> doctoralDAO = new DoctoralCsvDAO();
        }
    }

    /**
     * Método que permite guardar una nueva prueba de tipo Doctoral
     * @param name Nombre de la prueba a añadir
     * @param field Nombre del campo de estudio
     * @param difficulty Dificultad de la prueba (doctoral)
     * @param inUse Indica si la prueba está en uso por alguna edición
     */
    public void addDoctoralThesis (String name, String field, int difficulty, boolean inUse) {
        DoctoralThesis doctoralThesis = new DoctoralThesis(name, field, difficulty, inUse);
        doctoralDAO.create(doctoralThesis);
    }

    /**
     * Obtiene un Doctoral de entre todos los existentes a partir del nombre
     * @param name Nombre del master a buscar
     * @return Doctoral que se buscaba
     */
    public DoctoralThesis getDoctoralByName (String name) {
        int i;
        boolean found = false;
        LinkedList<DoctoralThesis> doctoralTheses = doctoralDAO.readAll();
        for (i = 0; i < doctoralTheses.size() && !found; i++) {
            if (doctoralTheses.get(i).getName().equals(name)) {
                found = true;
            }
        }
        return doctoralTheses.get(i - 1);
    }

    /**
     * Elimina una prueba a partir de su posición en la lista
     * @param index indice de la prueba a eliminar
     */
    public void deleteMaster (int index) {
        doctoralDAO.delete(index);
    }

    /**
     * Nos permite saber si una prueba está en uso por alguna edición
     * @param name Nombre de la prueba que queremos saber si está en uso
     * @return booleano que indica si la prueba está en uso o no
     */
    public boolean isInUse (String name) {
        return getDoctoralByName(name).isInUse();
    }

    /**
     * Nos permite saber la posición de guardado de una prueba según su nombre
     * @param name Nombre de la prueba que queremos buscar
     * @return Posición de la prueba solicitada
     */
    public int getIndexByName (String name) {
        int i;
        boolean found = false;
        LinkedList<DoctoralThesis> doctorals = doctoralDAO.readAll();
        for (i = 0; i < doctorals.size() && !found; i++) {
            if (doctorals.get(i).getName().equals(name)) {
                found = true;
            }
        }
        return i - 1;
    }

    /**
     * Este método permite modificar el estado de una prueba a "en uso"
     * @param name Nombre de la prueba que queremos poner en uso
     */
    public void setInUseByName(String name) {
        int index = getIndexByName(name);
        DoctoralThesis auxDoctoral = getDoctoralByName(name);
        DoctoralThesis doctoralThesis = new DoctoralThesis(auxDoctoral.getName(), auxDoctoral.getFieldOfStudy(),
                auxDoctoral.getDifficulty(), true);
        doctoralDAO.changeLine(index, doctoralThesis);
    }

    /**
     * Este método permite modificar el estado de una prueba a "no en uso"
     * @param name Nombre de la prueba que queremos poner en "no uso"
     */
    public void setInNotUseByName(String name) {
        int index = getIndexByName(name);
        DoctoralThesis auxDoctoral = getDoctoralByName(name);
        DoctoralThesis doctoralThesis = new DoctoralThesis(auxDoctoral.getName(), auxDoctoral.getFieldOfStudy(),
                auxDoctoral.getDifficulty(), false);
        doctoralDAO.changeLine(index, doctoralThesis);
    }
}