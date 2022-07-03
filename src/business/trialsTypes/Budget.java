package business.trialsTypes;

import business.TeamManager;
import business.TrialTypeOptions;
import business.playerTypes.Player;
import presentation.ViewController;

/**
 * Representa el tipo de prueba Budget
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class Budget extends GenericTrial{
    private String nameEntity;
    private int amount;

    /**
     * Método constructor que crea un nuevo artículo, teniendo en cuenta si está en uso
     * @param nameTrial Nombre de la prueba
     * @param nameEntity Nombre del campo de estudio
     * @param amount Dificultad de la defensa de la tesis
     * @param inUse Nos permitirá saber si la prueba se usa en alguna edición
     */
    public Budget(String nameTrial, String nameEntity, int amount, boolean inUse) {
        super(nameTrial, TrialTypeOptions.BUDGET, inUse);
        this.nameEntity = nameEntity;
        this.amount = amount;
    }

    /**
     * Método que obtiene el nombre de la prueba
     * @return String con el nombre de la prueba
     */
    public String getNameTrial() {
        return super.getName();
    }

    /**
     * Método que nos permite saber si la prueba esta en uso
     * @return true si está en uso, false si no lo está
     */
    public boolean isInUse () {
        return super.getInUse();
    }

    /**
     * Método que obtiene el nombre de la entidad
     * @return
     */
    public String getNameEntity() {
        return nameEntity;
    }

    /**
     * Método que obtiene el monto de la prueba
     * @return Entero con el monto de la prueba
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Método que actualiza el uso de una prueba
     * @param use true si está en uso, false en desuso
     */
    public void setUsage(boolean use) {
        super.setUsage(use);
    }


    /**
     * Método que ejecuta, para un equipo entero, un Budget request y actualiza los datos de los jugadores
     * @param teamManager manager que nos permite acceder a los datos de los jugadores
     * @param view controlador de la vista
     */
    public void playTrial (TeamManager teamManager, ViewController view) {
        boolean passed;
        // Calculamos si el equipo recibe el budget o no
        if (teamManager.getPITeam() > (int) (Math.log(getAmount()) / Math.log(2))) {
            view.showMessage("The research group got the budget!\n");
            passed = true;
        } else {
            view.showMessage("The research group didn't get the budget!\n");
            passed = false;
        }

        // Segun si se ha recibido el budget o no, se cambia el PI de todos los jugadores
        if (passed) {
            Player aux;
            for (int i = 0; i < teamManager.getPlayers().size(); i++) {
                // Incrementamos el PI para cada jugador y lo actualizamos
                aux = teamManager.getPlayers().get(i);
                if (aux.getPI() != 0) {
                    aux.incrementPI((int) Math.ceil((double) aux.getPI() / 2));
                    teamManager.updatePlayer(i, aux);
                }
            }
        } else {
            Player aux;
            for (int i = 0; i < teamManager.getPlayers().size(); i++) {
                // Incrementamos el PI para cada jugador y lo actualizamos
                aux = teamManager.getPlayers().get(i);
                if (aux.getPI() != 0) {
                    aux.decrementPI(2);
                    teamManager.updatePlayer(i, aux);
                }
            }
        }

        for (Player player : teamManager.getPlayers()) {
            if (player.getPI() != 0) {
                player.showInfo(view);
            }
        }
    }
}
