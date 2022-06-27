package business.trialsTypes;

import business.TeamManager;
import business.TrialTypeOptions;
import business.playerTypes.Player;
import presentation.ViewController;

import java.util.Random;

/**
 * Representa una de las pruebas que pueden incluir las ediciones
 * @author Abraham Cedeño
 * @author José Pérez
 */
public class PaperPublication extends GenericTrial{
    private String magazineName;
    private String quartile;
    private int acceptedProbability;
    private int revisedProbability;
    private int rejectedProbability;


    /**
     * Método constructor que crea un nuevo artículo, teniendo en cuenta si está en uso
     * @param name Nombre del artículo a publicar
     * @param magazine Nombre de la revista donde se publica
     * @param quartile Quartil de la revista
     * @param accepted Probabilidad de que el artículo sea aceptado
     * @param revised Probabilidad de que el artículo sea revisado
     * @param rejected Probabilidad de que el artículo sea rechazado
     * @param inUse Nos permitirá saber si el artículo se usa en alguna edición
     */
    public PaperPublication (String name, String magazine, String quartile, int accepted, int revised, int rejected, boolean inUse) {
        super(name, TrialTypeOptions.PAPER, inUse);
        this.magazineName = magazine;
        this.quartile = quartile;
        this.acceptedProbability = accepted;
        this.revisedProbability = revised;
        this.rejectedProbability = rejected;
    }

    /**
     * Método que nos permite saber el nombre de un artículo
     * @return El nombre del artículo
     */
    public String getArticleName() {
        return super.getName();
    }

    /**
     * Método que nos permite saber el nombre de una revista
     * @return El nombre de la revista
     */
    public String getMagazineName() {
        return magazineName;
    }

    /**
     * Método que nos permite saber quartil de una revista
     * @return El quartil de la revista
     */
    public String getQuartile() {
        return quartile;
    }

    /**
     * Método que nos permite saber la probabilidad de un artículo de ser aceptado
     * @return Probabilidad de ser aceptado
     */
    public int getAcceptedProbability() {
        return acceptedProbability;
    }

    /**
     * Método que nos permite saber la probabilidad de un artículo de ser revisado
     * @return Probabilidad de ser revisado
     */
    public int getRevisedProbability() {
        return revisedProbability;
    }

    /**
     * Método que nos permite saber la probabilidad de un artículo de ser rechazado
     * @return Probabilidad de ser rechazado
     */
    public int getRejectedProbability() {
        return rejectedProbability;
    }

    /**
     * Método que nos permite saber si un artículo está en uso por alguna edición
     * @return true si está en uso, false si no lo está
     */
    public boolean InUse () {
        return super.getInUse();
    }

    public void setUsage(boolean use) {
        super.setUsage(use);
    }

    @Override
    public void playTrial(TeamManager teamManager, ViewController view) {
        int i = 0;

        // Hacemos que todos los jugadores publiquen su articulo y vamos actualizando su PI
        for (Player player: teamManager.getPlayers()){
            if (player.getPI() != 0) { // Solo jugarán una prueba los jugadores que aún no hayan muerto
                view.showMessageLine(player.getName() + " is submitting...");
                player = publishArticle(this, player, view);
                teamManager.updatePlayer(i, player);
                view.showMessageLine(" PI count: " + player.getPI() + "\n");
            }
            i++;
        }
    }

    /**
     * Método que llama a los métodos que obtienen qué se hace con el artículo enviado y que calcula la puntuación
     * final del jugador
     * @param article Artículo que se está ejecutando (prueba)
     * @param player Jugador que está pasando la prueba
     * @return Nuevo jugador, con la PI actualizada según su desempeño
     */
    private Player publishArticle (PaperPublication article, Player player, ViewController view) {

        // Calculamos de forma aleatoria si se acepta, revisa o rechaza
        int response;
        do {
            response = calculateResponse(article, view);
        }while (response != 1 && response != 3);
        // Aumentamos, mantenemos o reducimos puntuación segon el cuartil
        manageScore(response, article, player);

        return player;
    }

    /**
     * Método que nos dirá de forma aleatoria si el artículo se acepta, revisa o rechaza
     * @param article Artículo que se está presentando
     * @return Entero que puede valer: 1 (si se acepta el artículo), 2 (si se revisa el artículo), 3 (si se rechaza el artículo)
     */
    private int calculateResponse (PaperPublication article, ViewController view) {
        // Generamos un número aleatorio que estará entre 0 y 100
        Random rand = new Random();
        int response = 0;
        int randomNumber = rand.nextInt(101);

        //Creamos los rangos de aceptado, revisado y rechazado
        int acceptedRange = article.getAcceptedProbability();
        int revisedRange = acceptedRange + article.getRevisedProbability();
        int rejectedRange = revisedRange + article.getRejectedProbability();

        if (randomNumber  <= acceptedRange) {
            view.showMessageLine("Accepted!");
            response = 1;
        }else if(randomNumber < revisedRange) {
            view.showMessageLine("Revisions...");
            response = 2;
        }else if (randomNumber < rejectedRange){
            view.showMessageLine("Rejected.");
            response = 3;
        }
        return response;
    }

    /**
     * Método que, según si se ha aceptado o rechazado el artíuclo, modifica los puntos del jugador, dependiendo del
     * Quartil de la prueba ejecutada. En caso de haber sido revisado (2), la puntuación del jugador se mantiene
     * @param response Indica si el artículo ha sido aceptado (1), revisado (2) o rechazado (3)
     * @param article Artículo que se está presentando (prueba)
     * @param player Jugador que está pasando la prueba
     */
    private void manageScore (int response, PaperPublication article, Player player) {
        String quartile = article.getQuartile();

        if (response == 1) {
            switch (quartile) {
                case "Q1" -> player.incrementPI(4);
                case "Q2" -> player.incrementPI(3);
                case "Q3" -> player.incrementPI(2);
                case "Q4" -> player.incrementPI(1);
            }
        }
        if (response == 3) {
            switch (quartile) {
                case "Q1" -> player.decrementPI(5);
                case "Q2" -> player.decrementPI(4);
                case "Q3" -> player.decrementPI(3);
                case "Q4" -> player.decrementPI(2);
            }
        }

    }

}
