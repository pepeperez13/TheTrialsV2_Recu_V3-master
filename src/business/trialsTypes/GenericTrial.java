package business.trialsTypes;

import business.TeamManager;
import business.TrialTypeOptions;
import presentation.ViewController;

/**
 * Clase que contiene el nombre y el tipo de prueba
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class GenericTrial {
    private String name;
    private TrialTypeOptions type;
    private boolean inUse;

    /**
     * Método que crea una prueba Generica
     * @param name Nombre de la prueba
     * @param type Tipo de la prueba
     */
    public GenericTrial(String name, TrialTypeOptions type, boolean inUse) {
        this.name = name;
        this.type = type;
        this.inUse = inUse;
    }

    /**
     * Método que retorna el nombre de la prueba
     * @return String con el nombre de la prueba
     */
    public String getName () {
        return name;
    }

    /**
     * Método que retorna el tipo de prueba
     * @return Retorna un typo TrialTypeOptions
     */
    public TrialTypeOptions getType (){
        return type;
    }

    /**
     * Método que retorna si una prueba está siendo usada por alguna edición
     * @return true si está siendo usada, false si no está siendo usada
     */
    public boolean getInUse () {
        return inUse;
    }

    /**
     * Método que actualiza el uso de una prueba
     * @param use true si se usa, false si no se usa
     */
    public void setUsage(boolean use) {
        inUse = use;
    }

    /**
     * Método que será implementado por cada tipo de prueba, que gestionará la ejecución de la misma
     * @param teamManager manager que nos permite acceder a los datos de los jugadores
     * @param viewController controlador de la vista
     */
    public void playTrial (TeamManager teamManager, ViewController viewController) {

    }
}
