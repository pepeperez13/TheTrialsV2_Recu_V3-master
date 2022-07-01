package business.trialsTypes;

import business.TeamManager;
import business.TrialTypeOptions;
import business.playerTypes.Doctor;
import business.playerTypes.Player;
import presentation.ViewController;

/**
 * Representa el tipo de prueba Doctoral
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class DoctoralThesis extends GenericTrial{
    private String fieldOfStudy;
    private int difficulty;

    /**
     * Método constructor que crea un nuevo DoctoralThesis, teniendo en cuenta si está en uso
     * @param name Nombre de la prueba
     * @param fieldOfStudy Nombre del campo de estudio
     * @param difficulty Dificultad de la defensa de la tesis
     * @param inUse Nos permitirá saber si la prueba se usa en alguna edición
     */
    public DoctoralThesis(String name, String fieldOfStudy, int difficulty, boolean inUse) {
        super(name, TrialTypeOptions.DOCTORAL, inUse);
        this.fieldOfStudy = fieldOfStudy;
        this.difficulty = difficulty;
    }

    /**
     * Método que nos permite saber el nombre de la prueba
     * @return El nombre de la prueba
     */
    public String getName() {
        return super.getName();
    }

    /**
     * Método que nos permite saber el nombre del campo de estudio
     * @return String con el nombre de campo de estudio
     */
    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    /**
     * Método que nos permite saber si la prueba esta en uso
     * @return true si está en uso, false si no lo está
     */
    public boolean isInUse() {
        return super.getInUse();
    }

    /**
     * Método que nos retorna la dificultad de presentar la tesis
     * @return Entero que indica la dificultad
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Método que actualiza el uso de una prueba
     * @param use true si está en uso, false en desuso
     */
    public void setUsage(boolean use) {
        super.setUsage(use);
    }

    /**
     * Método que ejecuta, para un equipo entero, un Doctoral Thesis y actualiza los datos de los jugadores
     * @param teamManager manager que nos permite acceder a los datos de los jugadores
     * @param view controlador de la vista
     */
    @Override
    public void playTrial (TeamManager teamManager, ViewController view) {
        double result = 0;
        int j = 0;

        // Para todos los jugadores, comprobamos si pasan la prueba y gestionamos sus PI y estado
        for (Player player: teamManager.getPlayers()) {
            if (player.getPI() != 0) { // Solo jugarán una prueba los jugadores que aún no hayan muerto
                // Calculamos resultado
                for (int i = 1; i <= getDifficulty(); i++) {
                    result = result + ((2 * i) - 1);
                }
                result = Math.sqrt(result);
                // Comprobamos si pasa y actualizamos PI, mostrandolo por pantalla
                if (player.getPI() > result) {
                    // Si el jugador es un Doctor, automáticamente su PI pasa a 10
                    if (player instanceof Doctor) {
                        player.setPi(10);
                    } else {
                        player.incrementPI(5);
                    }
                    view.showMessage(player.getName() + " was successful. Congrats! PI count: " + player.getPI());
                } else {
                    player.decrementPI(5);
                    view.showMessage(player.getName() + " was not successful. Sorry... PI count: " + player.getPI());
                }
                teamManager.updatePlayer(j, player);
                j++;
            }
        }
    }
}