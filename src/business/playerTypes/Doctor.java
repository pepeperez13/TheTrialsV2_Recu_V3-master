package business.playerTypes;

import business.PlayerTypeOptions;
import presentation.ViewController;

/**
 * Define un tipo de jugador Doctor, que hereda del genérico Player
 * @author Abraham Cedeno
 * @author José Pérez
 */
public class Doctor extends Player{

    /**
     * Constructor que crea un nuevo Doctor
     * @param name nombre del Doctor
     * @param PI PI del doctor
     */
    public Doctor (String name, int PI) {
        super(name, PI, PlayerTypeOptions.DOCTOR);
    }

    /**
     * Incrementa el PI de un Doctor, conociendo sus particularidades
     * @param change cantidad en la que debe cambiar el PI (en genérico)
     */
    @Override
    public void incrementPI (int change) {
        int PI = super.getPI() + change*2;
        super.setPi(PI);
    }


    /**
     * Decrementa el PI de un Doctor
     * @param change cantidad de puntos a retirar
     */
    @Override
    public void decrementPI (int change) {
        super.decrementPI(change);
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
     * Obtiene el nombre del jugador
     * @return nombre del jugador
     */
    @Override
    public String getName () {
        return super.getName();
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
     * Mustra el PI de un Doctor
     * @param view controlador de la vista
     */
    @Override
    public void showInfo (ViewController view) {
        view.showMessage(getName()+", PhD. PI count: " + getPI());
    }

}
