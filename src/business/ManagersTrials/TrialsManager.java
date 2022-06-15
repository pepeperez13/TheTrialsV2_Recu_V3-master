package business.ManagersTrials;

import business.DataSourceOptions;
import business.TrialTypeOptions;
import business.trialsTypes.*;
import persistance.*;
import persistance.CSV.*;
import persistance.JSON.*;

import java.util.ArrayList;
import java.util.LinkedList;

public class TrialsManager {
    private GenericTrialDAO trialsDAO;
    private PaperDAO paperDAO;
    private MasterDAO masterDAO;
    private DoctoralDAO doctoralDAO;
    private BudgetDAO budgetDAO;

    /**
     * Método constructor que crea un nuevo manager, relacionandolo con CSV o JSON
     * @param options Opción que indica si los datos se guardan en formato CSV o JSON
     */
    public TrialsManager(DataSourceOptions options) {
        switch (options) {
            case JSON -> {
                trialsDAO = new GenericTrialJsonDAO();
                paperDAO = new PaperJsonDAO();
                masterDAO = new MasterJsonDAO();
                doctoralDAO = new DoctoralJsonDAO();
                budgetDAO = new BudgetJsonDAO();
            }
            case CSV -> {
                trialsDAO = new GenericTrialCsvDAO();
                trialsDAO = new GenericTrialCsvDAO();
                paperDAO = new PaperCsvDAO();
                masterDAO = new MasterCsvDAO();
                doctoralDAO = new DoctoralCsvDAO();
                budgetDAO = new BudgetCsvDAO();
            }
        }
    }


    /**
     * Método que permite guardar una nueva prueba del tipo genérico
     */
    public void addTrial (GenericTrial genericTrial) {
        trialsDAO.create(genericTrial);
        if (genericTrial instanceof PaperPublication paper) {
            paperDAO.create(paper);
        } else if (genericTrial instanceof MasterStudies master) {
            masterDAO.create(master);
        } else if (genericTrial instanceof DoctoralThesis doctoral) {
            doctoralDAO.create(doctoral);
        } else if (genericTrial instanceof Budget budget) {
            budgetDAO.create(budget);
        }
    }

    // Devuelve la prueba especifica segun su nombre
    public GenericTrial getTrialByName (String name) {
        LinkedList<GenericTrial> trials = getTrials();
        GenericTrial genericTrial = null;

        // Encontramos la prueba entre todos los genéricos
        for (GenericTrial trial: trials) {
            if (trial.getName().equals(name)) {
                genericTrial = trial;
            }
        }

        // Buscamos la prueba en su DAO específico y la retornamos
        switch (genericTrial.getType()) {
            case PAPER -> {
                for (PaperPublication paper: paperDAO.readAll()) {
                    if (paper.getName().equals(name)) {
                        return paper;
                    }
                }
            }
            case MASTER -> {
                for (MasterStudies master: masterDAO.readAll()) {
                    if (master.getName().equals(name)) {
                        return master;
                    }
                }
            }
            case DOCTORAL -> {
                for (DoctoralThesis doctoral: doctoralDAO.readAll()) {
                    if (doctoral.getName().equals(name)) {
                        return doctoral;
                    }
                }
            }
            case BUDGET -> {
                for (Budget budget: budgetDAO.readAll()) {
                    if (budget.getNameTrial().equals(name)) {
                        return budget;
                    }
                }
            }
        }
        return null;
    }

    public GenericTrial getTrialByIndex (int index) {
        return getTrialByName(trialsDAO.findByIndex(index).getName());
    }

   // Devuelve la posicion de la prueba en su dao especifico segun su nombre
    public int getIndexByName (String name) {
        LinkedList<GenericTrial> trials = getTrials();
        GenericTrial genericTrial = null;

        // Encontramos la prueba entre todos los genéricos
        for (GenericTrial trial: trials) {
            if (trial.getName().equals(name)) {
                genericTrial = trial;
            }
        }

        // Buscamos la prueba en su DAO específico y la retornamos
        switch (genericTrial.getType()) {
            case PAPER -> {
                LinkedList<PaperPublication> papers = paperDAO.readAll();
                for (int i = 0; i < papers.size(); i++) {
                    if (papers.get(i).getName().equals(name)) {
                        return i;
                    }
                }
            }
            case MASTER -> {
                LinkedList<MasterStudies> masters = masterDAO.readAll();
                for (int i = 0; i < masters.size(); i++) {
                    if (masters.get(i).getName().equals(name)) {
                        return i;
                    }
                }
            }
            case DOCTORAL -> {
                LinkedList<DoctoralThesis> dotorals = doctoralDAO.readAll();
                for (int i = 0; i < dotorals.size(); i++) {
                    if (dotorals.get(i).getName().equals(name)) {
                        return i;
                    }
                }
            }
            case BUDGET -> {
                LinkedList<Budget> budgets = budgetDAO.readAll();
                for (int i = 0; i < budgets.size(); i++) {
                    if (budgets.get(i).getName().equals(name)) {
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    // Devuelve todos los nombres de un tipo de prueba especifica
    public LinkedList<String> getTrialsNamesByType (TrialTypeOptions type){
        LinkedList<GenericTrial> trials =  trialsDAO.readAll();
        LinkedList<String> nombres = new LinkedList<>();
        for (GenericTrial trial : trials) {
            if (trial.getType().equals(type)) {
                nombres.add(trial.getName());
            }
        }
        return nombres;
    }

    /**
     * Método que obtiene los nombres de todas las pruebas existentes
     * @return Lista con los nombres de todas las pruebas
     */
    public LinkedList<String> getAllTrialsNames() {
        LinkedList<GenericTrial> trials =  trialsDAO.readAll();
        LinkedList<String> nombres = new LinkedList<>();
        for (GenericTrial trial : trials) {
            nombres.add(trial.getName());
        }
        return nombres;
    }

    // Elimina el trial que nos manden (hasta ahora no se usaba, pero se uede mirar de sustituir por otros deletes)
    public boolean deleteTrial (GenericTrial genericTrial) {
        TrialTypeOptions type = getTrialType(genericTrial);
        switch (type) {
            case PAPER -> paperDAO.delete(getIndexByName(genericTrial.getName()));
            case MASTER -> masterDAO.delete(getIndexByName(genericTrial.getName()));
            case DOCTORAL -> doctoralDAO.delete(getIndexByName(genericTrial.getName()));
            case BUDGET -> budgetDAO.delete(getIndexByName(genericTrial.getName()));
        }
        return trialsDAO.delete(getGenericIndexByName(genericTrial.getName()));
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
     * Metodo que permite saber el tipo de una prueba segun su posicion en una lista
     * @param index indice de la prueba que se quiere saber el tipo
     * @return tipo de la prueba buscada
     */
    public TrialTypeOptions getTrialTypeByIndex (int index) {
        GenericTrial trial = trialsDAO.readAll().get(index-1);
        return getTrialType(trial);
    }

    /**
     * Método que permite saber el tipo de una prueba segun su nombre
     * @param name nombre de la prueba de la que queremos saber el tipo
     * @return tipo de la prueba buscada
     */
    public TrialTypeOptions getTrialTypeByName (String name) {
        GenericTrial trial = null;
        for (GenericTrial trial1: getTrials()) {
            if (trial1.getName().equals(name)) {
                trial = trial1;
                break;
            }
        }
        return getTrialType(trial);
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
     * Nos permite saber la posición de guardado de una prueba según su nombre
     * @param name Nombre de la prueba que queremos buscar
     * @return Posición de la prueba solicitada
     */
    public int getGenericIndexByName(String name) {
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
        trialsDAO.delete(getGenericIndexByName(name));
    }

    private TrialTypeOptions getTrialType (GenericTrial trial) {
        if (trial instanceof PaperPublication) {
            return TrialTypeOptions.PAPER;
        }else if (trial instanceof MasterStudies) {
            return TrialTypeOptions.MASTER;
        } else if (trial instanceof DoctoralThesis) {
            return TrialTypeOptions.DOCTORAL;
        } else {
            return TrialTypeOptions.BUDGET;
        }
    }

    public void setUsageByName (String name, boolean usage) {
        GenericTrial trial = getTrialByName(name);
        int index = getIndexByName(name);
        if (trial instanceof PaperPublication paper) {
            paper.setUsage(usage);
            paperDAO.changeLine(index, paper);
        } else if (trial instanceof MasterStudies master) {
            master.setUsage(usage);
            masterDAO.changeLine(index, master);
        } else if (trial instanceof DoctoralThesis doctoral) {
            doctoral.setUsage(usage);
            doctoralDAO.changeLine(index, doctoral);
        } else if (trial instanceof Budget budget) {
            budget.setUsage(usage);
            budgetDAO.changeLine(index, budget);
        }
    }

}
