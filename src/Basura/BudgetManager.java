package Basura;

import business.DataSourceOptions;
import business.trialsTypes.Budget;
import persistance.BudgetDAO;
import persistance.CSV.BudgetCsvDAO;
import persistance.JSON.BudgetJsonDAO;

import java.util.LinkedList;

/**
 * Controla aquello relacionado con las pruebas de tipo Budget
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class BudgetManager {
    private BudgetDAO budgetDAO;
    private GenericTrialManager genericTrialManager;

    /**
     * Método constructor que crea un nuevo manager, relacionandolo con CSV o JSON
     * @param options Opción que indica si los datos se guardan en formato CSV o JSON
     * @param genericTrialManager Manager del tipo de prueba genérico, con quien se comunica el budget
     */
    public BudgetManager(DataSourceOptions options, GenericTrialManager genericTrialManager)  {
        this.genericTrialManager = genericTrialManager;
        switch (options) {
            case JSON -> budgetDAO = new BudgetJsonDAO();
            case CSV -> budgetDAO = new BudgetCsvDAO();
        }
    }

    /**
     * Método que permite guardar una nueva prueba de tipo budget
     * @param nameTrial Nombre de la prueba a añadir
     * @param nameEntity Nombre de la entidad
     * @param amount Cantidad de presupuesto (budget)
     * @param inUse Indica si la prueba está en uso por alguna edición
     */
    public void addBudget (String nameTrial, String nameEntity, int amount, boolean inUse) {
        Budget budget = new Budget(nameTrial, nameEntity, amount, inUse);
        budgetDAO.create(budget);
    }

    /**
     * Método que retorna en forma de lista toda la información completa de cada prueba (budget)
     * @return Lista con todos las pruebas (budget)
     */
    public LinkedList<Budget> getBudget () {
        return budgetDAO.readAll();
    }

    /**
     * Obtiene un Budget de entre todos los existentes a partir del nombre
     * @param name Nombre del budget a buscar
     * @return Budget que se buscaba
     */
    public Budget getBudgetByNameTrial (String name) {
        int i;
        boolean found = false;
        LinkedList<Budget> budgets = budgetDAO.readAll();
        for (i = 0; i < budgets.size() && !found; i++) {
            if (budgets.get(i).getNameTrial().equals(name)) {
                found = true;
            }
        }
        return budgets.get(i - 1);
    }

    /**
     * Elimina una prueba a partir de su posición en la lista
     * @param index indice de la prueba a eliminar
     */
    public void deleteBudget (int index) {
        budgetDAO.delete(index);
    }

    /**
     * Nos permite saber la posición de guardado de una prueba según su nombre
     * @param name Nombre de la prueba que queremos buscar
     * @return Posición de la prueba solicitada
     */
    public int getIndexByName (String name) {
        int i;
        boolean found = false;
        LinkedList<Budget> budgets = budgetDAO.readAll();
        for (i = 0; i < budgets.size() && !found; i++) {
            if (budgets.get(i).getNameTrial().equals(name)) {
                found = true;
            }
        }
        return i - 1;
    }

    /**
     * Nos permite saber si una prueba está en uso por alguna edición
     * @param name Nombre de la prueba que queremos saber si está en uso
     * @return booleano que indica si la prueba está en uso o no
     */
    public boolean isInUse (String name) {
        return getBudgetByNameTrial(name).isInUse();
    }

    /**
     * Este método permite modificar el estado de una prueba a "en uso"
     * @param name Nombre de la prueba que queremos poner en uso
     */
    public void setInUseByName(String name) {
        int index = getIndexByName(name);
        Budget auxBudget = getBudgetByNameTrial(name);
        Budget budget = new Budget(auxBudget.getNameTrial(),
                auxBudget.getNameEntity(),
                auxBudget.getAmount(), true);
        budgetDAO.changeLine(index, budget);
    }

    /**
     * Este método permite modificar el estado de una prueba a "no en uso"
     * @param name Nombre de la prueba que queremos poner en "no uso"
     */
    public void setInNotUseByName(String name) {
        int index = getIndexByName(name);
        Budget auxBudget = getBudgetByNameTrial(name);
        Budget budget = new Budget(auxBudget.getNameTrial(),
                auxBudget.getNameEntity(),
                auxBudget.getAmount(), false);
        budgetDAO.changeLine(index, budget);
    }
}
