package business;

/**
 * Clase que representa una edición
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class Edition {
    private final int year;
    private final int numPlayers;
    private final int numTrials;
    private final String[] trialsNames;

    /**
     * Método constructor que crea una edición nueva
     * @param year Año de la edición
     * @param numPlayers Número de jugadores que participarán en la edición
     * @param numTrials Número de pruebas que habrán en la edición
     * @param articles Nombres de todos las pruebas (artículos) que componen la edición
     */
    public Edition (int year, int numPlayers, int numTrials, String[] articles) {
        this.year = year;
        this.numPlayers = numPlayers;
        this.numTrials = numTrials;
        this.trialsNames = articles;
    }

    /**
     * Método que nos permite saber el año de la edición
     * @return Año de la edición
     */
    public int getYear() {
        return year;
    }

    /**
     * Método que nos permite saber el numero de jugadores en la edición
     * @return Número jugadores
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Método que nos permite saber el número de pruebas en la edición
     * @return Año de la edición
     */
    public int getNumTrials() {
        return numTrials;
    }

    /**
     * Método que nos permite saber los nombres de las pruebas que componen la edición
     * @return Array que contiene los nombres de las pruebas
     */
    public String[] getTrialNames() {
        return trialsNames;
    }

    public String getTrialNameByIndex (int index) {
        return trialsNames[index];
    }
}
