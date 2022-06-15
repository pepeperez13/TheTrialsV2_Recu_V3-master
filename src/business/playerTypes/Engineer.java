package business.playerTypes;

/**
 * Define un tipo de jugador Engineer, que hereda del genérico Player
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class Engineer extends Player{

    /**
     * Constructor que crea un nuevo Engineer
     * @param name nombre del Enginner
     * @param PI PI del ingeniero
     */
    public Engineer (String name, int PI){
        super(name, PI);
    }

    /**
     * Incrementa el PI de un ingeniero, conociendo sus particularidades
     * @param change cantidad en la que debe cambiar el PI (en genérico)
     */
    @Override
    public void incrementPI (int change) {
        super.incrementPI(change);
    }

    /**
     * Decrementa el PI de un Ingeniero
     * @param change cantidad de puntos a retirar
     */
    @Override
    public void decrementPI (int change) {
       super.decrementPI(change);
    }

    /**
     * Obtiene el PI del jugador
     * @return PI del jugador
     */
    @Override
    public int getPI () {
        return super.getPI();
    }

    /**
     * Coloca el PI del jugador en un valor específico
     * @param PI PI al que se debe colocar
     */
    @Override
    public void setPi (int PI) {
        super.setPi(PI);
    }

    /**
     * Obtiene el nombre del jugador
     * @return nombre del jugador
     */
    @Override
    public String getName () {
        return super.getName();
    }

    /**
     * Comprueba, según su PI, si le toca evolucionar al siguiente tipo de jugador
     * @return booleano que indica si debe evolucionar o no
     */
    @Override
    public boolean checkUpdateStatus() {
        return super.checkUpdateStatus();
    }
}
