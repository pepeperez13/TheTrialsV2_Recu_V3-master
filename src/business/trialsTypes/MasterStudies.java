package business.trialsTypes;

import business.TeamManager;
import business.TrialTypeOptions;
import business.playerTypes.Engineer;
import business.playerTypes.Player;
import presentation.ViewController;

import java.util.Random;

/**
 * Representa una de las pruebas (en este caso siempre artículos) que pueden incluir las ediciones
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class MasterStudies extends GenericTrial{
    private String nom;
    private int numberCredits;
    private int probability;


    /**
     * Método constructor que crea un nuevo artículo, teniendo en cuenta si está en uso
     * @param name Nombre de la prueba
     * @param nom Nombre del master a estudiar
     * @param numberCredits Número de créditos del master
     * @param probability Probabilidad de aprobar el master
     * @param inUse Nos permitirá saber si la prueba se usa en alguna edición
     */
    public MasterStudies(String name, String nom, int numberCredits, int probability, boolean inUse) {
        super(name, TrialTypeOptions.MASTER, inUse);
        this.nom = nom;
        this.numberCredits = numberCredits;
        this.probability = probability;
    }

    /**
     * Método que nos permite saber el nombre de la prueba
     * @return El nombre de la prueba
     */
    public String getName() {
        return super.getName();
    }

    /**
     * Método que nos permite saber el nombre del master
     * @return El nombre del master
     */
    public String getNom() {
        return nom;
    }

    /**
     * Método que nos permite saber si la prueba esta en uso
     * @return true si está en uso, false si no lo está
     */
    public boolean isInUse() {
        return super.getInUse();
    }

    /**
     * Método que nos permite saber el número de créditos del master
     * @return El numero de créditos del master
     */
    public int getNumberCredits() {
        return numberCredits;
    }

    /**
     * Método que nos retorna la probabilidad de aprobar el master
     * @return La probabilidad de aprobar
     */
    public int getProbability() {
        return probability;
    }

    public void setUsage(boolean use) {
        super.setUsage(use);
    }

    @Override
    public void playTrial(TeamManager teamManager, ViewController viewController) {
        int i = 0;
        // Comprobamos para cada jugador si pasan el master y actualizamos su PI
        for (Player player: teamManager.getPlayers()) {
            if (player.getPI() != 0) { // Solo jugarán una prueba los jugadores que aún no hayan muerto
                checkPassed(this, player, viewController);
                teamManager.updatePlayer(i, player);
                i++;
            }
        }
    }

    /**
     * Calcula si un jugador pasa el master o no
     * @param masterStudies prueba a ejecutar
     * @param player jugador que está en la prueba
     */
    private void checkPassed (MasterStudies masterStudies, Player player, ViewController view) {
        Random random = new Random();
        int randomNumber;
        int pass = 0, deny = 0;

        //Comprobar uno a uno cúantos créditos pasa
        for (int i = 0; i < masterStudies.getNumberCredits() ; i++) {
            randomNumber = random.nextInt(101);
            if (randomNumber <= masterStudies.getProbability()) {
                pass++;
            }
            else {
                deny++;
            }
        }

        // Modificamos PI según el resultado
        if (pass > deny) {
            if (player instanceof Engineer) {
                player.setPi(10);
            }else{
                player.incrementPI(3);
            }
            view.showMessage(player.getName() + " passed " + pass + "/" + masterStudies.getNumberCredits() + " Congrats! PI count: " + player.getPI());
        }else{
            player.decrementPI(3);
            view.showMessage(player.getName() + " passed " + pass + "/" + masterStudies.getNumberCredits() + " Sorry... PI count: " + player.getPI());
        }

    }
}
