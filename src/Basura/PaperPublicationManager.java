package Basura;

import business.DataSourceOptions;
import business.trialsTypes.PaperPublication;
import persistance.CSV.PaperCsvDAO;
import persistance.JSON.PaperJsonDAO;
import persistance.PaperDAO;

import java.util.LinkedList;

/**
 * Controla aquello relacionado con las pruebas de tipo PaperPublication
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class PaperPublicationManager {
    private PaperDAO paperDAO;
    private GenericTrialManager genericTrialManager;

    /**
     * Método constructor que crea un nuevo manager, relacionandolo con CSV o JSON
     * @param options Opción que indica si los datos se guardan en formato CSV o JSON
     * @param genericTrialManager Manager del tipo de prueba genérico, con quien se comunica el budget
     */
    public PaperPublicationManager(DataSourceOptions options, GenericTrialManager genericTrialManager)  {
        switch (options) {
            case JSON -> paperDAO = new PaperJsonDAO();
            case CSV -> paperDAO = new PaperCsvDAO();
        }
        this.genericTrialManager = genericTrialManager;
    }

    /**
     * Método que permite guardar una nueva prueba
     * @param name Nombre del artículo a publicar
     * @param magazine Nombre de la revista donde se publica
     * @param quartile Quartil de la revista
     * @param accepted Probabilidad de que el artículo sea aceptado
     * @param revised Probabilidad de que el artículo sea revisado
     * @param rejected Probabilidad de que el artículo sea rechazado
     */
    public void addPaper (String name, String magazine, String quartile, int accepted, int revised, int rejected, boolean inUse)  {
        PaperPublication article = new PaperPublication(name, magazine, quartile, accepted, revised, rejected, inUse);
        paperDAO.create(article);
    }

    /**
     * Método que retorna en forma de lista toda la información completa de cada prueba (artículo)
     * @return Lista con todos los artículos (pruebas)
     */
    public LinkedList<PaperPublication> getPapers ()  {
        return paperDAO.readAll();
    }

    /**
     * Método que retorna toda la información de un artículo concreto según su nombre
     * @param name Nombre del artículo solicitado
     * @return Información del artículo solicitado
     */
    public PaperPublication getPaperByName (String name) {
        int i;
        boolean found = false;
        LinkedList<PaperPublication> articles = paperDAO.readAll();
        for (i = 0; i < articles.size() && !found; i++) {
            if (articles.get(i).getArticleName().equals(name)) {
                found = true;
            }
        }
        return articles.get(i - 1);
    }

    /**
     * Nos permite saber la posición de guardado de una prueba según su nombre
     * @param name Nombre de la prueba que queremos buscar
     * @return Posición de la prueba solicitada
     */
    public int getIndexByName (String name) {
        int i;
        boolean found = false;
        LinkedList<PaperPublication> articles = paperDAO.readAll();
        for (i = 0; i < articles.size() && !found; i++) {
            if (articles.get(i).getArticleName().equals(name)) {
                found = true;
            }
        }
        return i - 1;
    }

    /**
     * Mñetodo que retorna en forma de lista los nombres de todos los artículos
     * @return Lista con los nombres de todos los artículos
     */
    public LinkedList<String> getPapersNames () {
        LinkedList<PaperPublication> articles =  paperDAO.readAll();
        LinkedList<String> nombres = new LinkedList<>();
        for (int i = 0; i < articles.size(); i++) {
            nombres.add(articles.get(i).getArticleName());
        }
        return nombres;
    }

    /**
     * Método que elimina un artículo según su posición en el fichero
     * @param index Posición del artículo a eliminar
     * @return Booleano que permite saber si el artículo se ha eliminado correctamente
     */
    public boolean deletePaper (int index) {
        return paperDAO.delete(index);
    }

    /**
     * Método que busca si existe una prueba
     * @param name nombre de la prueba de la que se busca su existencia
     * @return booleano que indica si existe la prueba o no
     */
    public boolean checkExistence (String name) {
        return getPapersNames().contains(name);
    }

    /**
     * Nos permite saber si una prueba está en uso por alguna edición
     * @param name Nombre de la prueba que queremos saber si está en uso
     * @return booleano que indica si la prueba está en uso o no
     */
    public boolean isInUse (String name) {
        return getPaperByName(name).InUse();
    }

    /**
     * Este método permite modificar el estado de una prueba a "en uso"
     * @param name Nombre de la prueba que queremos poner en uso
     */
    public void setInUseByName(String name) {
        int index = getIndexByName(name);
        PaperPublication auxPaper = getPaperByName(name);
        PaperPublication paperPublication = new PaperPublication(auxPaper.getArticleName(), auxPaper.getMagazineName(),
                auxPaper.getQuartile(), auxPaper.getAcceptedProbability(),
                auxPaper.getRevisedProbability(), auxPaper.getRejectedProbability(), true);
        paperDAO.changeLine(index, paperPublication);
    }

    /**
     * Este método permite modificar el estado de una prueba a "no en uso"
     * @param name Nombre de la prueba que queremos poner en "no uso"
     */
    public void setInNotUseByName(String name)  {
        int index = getIndexByName(name);
        PaperPublication auxPaper = getPaperByName(name);
        PaperPublication paperPublication = new PaperPublication(auxPaper.getArticleName(), auxPaper.getMagazineName(),
                auxPaper.getQuartile(), auxPaper.getAcceptedProbability(),
                auxPaper.getRevisedProbability(), auxPaper.getRejectedProbability(), false);
        paperDAO.changeLine(index, paperPublication);
    }
}