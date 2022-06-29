package business;

/**
 * Clase que representa una edición
 * @author Abraham Cedeno
 * @author José Pérez
 */
public class Edition {
    private final int year;
    private final int numPlayers;
    private final int numTrials;
    private final String[] trialsNames;

    /**
     * Método constructor que crea una edición nueva
     * @param year Ano de la edición
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
     * Método que nos permite saber el ano de la edición
     * @return Ano de la edición
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
     * @return Ano de la edición
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

    /**
     * Obtiene el nombre de una prueba según su indice (posición) en la edición
     * @param index posición de la prueba
     * @return nombre de la prueba
     */
    public String getTrialNameByIndex (int index) {
        return trialsNames[index];
    }
}
