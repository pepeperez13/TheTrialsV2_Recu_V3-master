package business.playerTypes;

/**
 * Define un tipo de jugador Master, que hereda de Engineer
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class Master extends Player {

    /**
     * Constructor que crea un nuevo Master
     * @param name nombre del Master
     * @param PI PI del master
     */
    public Master (String name, int PI) {
        super(name, PI);
    }

    /**
     * Incrementa el PI de un Master, conociendo sus particularidades
     * @param change cantidad en la que debe cambiar el PI (en genérico)
     */
    @Override
    public void incrementPI (int change) {
        super.incrementPI(change);
    }

    /**
     * Decrementa el PI de un Master
     * @param change cantidad de puntos a retirar
     */
    @Override
    public void decrementPI (int change) {
        int PI = super.getPI();
        if (PI - change/2 >= 0) {
            PI = PI - change/2;
        }else{
            PI = 0;
        }
        super.setPi(PI);
    }

    /**
     * Comprueba, según su PI, si le toca evolucionar al siguiente tipo de jugador
     * @return booleano que indica si debe evolucionar o no
     */
    @Override
    public boolean checkUpdateStatus() {
        return super.checkUpdateStatus();
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
     * Obtiene el nombre del jugador
     * @return nombre del jugador
     */
    @Override
    public String getName () {
        return super.getName();
    }
}
