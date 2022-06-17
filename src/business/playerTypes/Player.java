package business.playerTypes;


import business.PlayerTypeOptions;
import business.TrialTypeOptions;
import presentation.ViewController;

/**
 * Clase abstracta que representará un jugador en general
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class Player {
    private String name;
    private int PI;
    // Atributo que se usa exclusivamente al escribir los jugadores en un JSON. Para los csv no hace falta ya que se añade un campo de más en el teamCsvDao, pero
    // no se como hacer para añadir ese mismo campo en json
    private PlayerTypeOptions type;

    public Player(String name, int PI, PlayerTypeOptions type) {
        this.name = name;
        this.PI = PI;
        this.type = type;
    }

    /**
     * Incrementa el PI de un jugador
     * @param change cantidad en la que debe cambiar el PI (en genérico)
     */
    public void incrementPI (int change) {
        this.PI = this.PI + change;
    }

    /**
     * Decrementa el PI de un jugador
     * @param change cantidad de puntos a retirar
     */
    public void decrementPI (int change) {
        if (PI - change >= 0) {
            PI = PI - change;
        }else{
            this.PI = 0;
        }
    }

    /**
     * Obtiene el PI del jugador
     * @return PI del jugador
     */
    public int getPI () {
        return PI;
    }

    /**
     * Coloca el PI del jugador en un valor específico
     * @param PI PI al que se debe colocar
     */
    public void setPi (int PI) {
        this.PI = PI;
    }

    /**
     * Obtiene el nombre del jugador
     * @return nombre del jugador
     */
    public String getName () {
        return name;
    }

    public boolean checkUpdateStatus () {
        if ( PI >= 10) {
            return true;
        }else {
            return false;
        }
    }

    public void showInfo (ViewController view) {

    }

    public void setType(PlayerTypeOptions type) {
        this.type = type;
    }
}
