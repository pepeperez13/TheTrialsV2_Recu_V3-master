package presentation;

import business.TeamManager;
import business.playerTypes.Doctor;
import business.playerTypes.Engineer;
import business.playerTypes.Master;
import business.playerTypes.Player;
import business.trialsTypes.*;

import java.util.LinkedList;
import java.util.Random;

/**
 * Clase que se encarga de ejecutar la lógica de ejecución de cada una de las diferentes tipos de pruebas
 */
public class GameExecutor {
    private final TeamManager teamManager;
    private final ViewController view;

    /**
     * Constructor del GameExecutor
     * @param teamManager Gestiona aquello relacionado con los jugadores (team)
     * @param view Gestiona aquello relacionado con la interacción por pantalla
     */
    public GameExecutor(TeamManager teamManager, ViewController view) {
        this.teamManager = teamManager;
        this.view = view;
    }

    /**
     * Escoge qué lógica ejecutar según el tipo de prueba que se indique
     * @param genericTrial prueba a ejecutar
     */
    public void playTrial (GenericTrial genericTrial)  {
        if (genericTrial instanceof PaperPublication paper) {
            playPaper(paper);
        } else if (genericTrial instanceof MasterStudies master) {
            playMaster(master);
        } else if (genericTrial instanceof DoctoralThesis doctoral) {
            playDoctoral(doctoral);
        } else if (genericTrial instanceof Budget budget) {
            playBudget(budget);
        }
    }

    /*Empieza gestión de budget*/
    /**
     * Método que ejecuta la lógica general de una prueba de tipo Budget
     * @param budget prueba especifica a ejecutar
     */
    private void playBudget (Budget budget)  {
        boolean passed;
        // Calculamos si el equipo recibe el budget o no
        if (teamManager.getPITeam() > (int) (Math.log(budget.getAmount()) / Math.log(2))) {
            view.showMessage("The research group got the budget!\n");
            passed = true;
        } else {
            view.showMessage("The research group didn't get the budget!\n");
            passed = false;
        }
        // Actualizamos el PI de todos los jugadores y mostramos
        updatePiTeam(teamManager, passed);
        for (Player player : teamManager.getPlayers()) {
            if (player instanceof Engineer) {
                view.showMessage(player.getName()+", Engineer. PI count: " + player.getPI());
            } else if (player instanceof Master) {
                view.showMessage(player.getName()+", Master. PI count: " + player.getPI());
            } else if (player instanceof Doctor) {
                view.showMessage(player.getName()+", PhD. PI count: " + player.getPI());
            }
        }

        // Mostramos todos los jugaodores que hayan evolucionado
        LinkedList<String> changedType = checkUpdateStatus();
        for (String line: changedType) {
            view.showMessageLine(line);
        }
    }

    /**
     * Actualiza el PI de todos los jugadores del equipo según si han pasado la prueba o no
     * @param teamManager nos permitirá persistir los datos del equipo
     * @param passed parámetro que nos indicará si el equipo ha pasado el budget o no
     */
    private void updatePiTeam (TeamManager teamManager, boolean passed) {
        if (passed) {
            Player aux;
            for (int i = 0; i < teamManager.getPlayers().size(); i++) {
                // Cambiamos el PI para cada jugador y lo actualizamos
                aux = teamManager.getPlayers().get(i);
                aux.incrementPI((int) Math.ceil((double) aux.getPI()/2));
                teamManager.updatePlayer(i, aux);
            }
        }
        else {
            Player aux;
            for (int i = 0; i < teamManager.getPlayers().size(); i++) {
                // Cambiamos el PI para cada jugador y lo actualizamos
                aux = teamManager.getPlayers().get(i);
                aux.decrementPI(2);
                teamManager.updatePlayer(i, aux);
            }
        }
    }


    /*Acaba gestión budget*/


    /*Empieza gestión doctoral*/
    /**
     * Método que ejecuta la gestión general de un Doctoral
     * @param doctoral prueba a ejecutar
     */
    private void playDoctoral (DoctoralThesis doctoral)  {
        double result = 0;
        int j = 0;

        // Para todos los jugadores, comprobamos si pasan la prueba y gestionamos sus PI y estado
        for (Player player: teamManager.getPlayers()) {
            if (player.getPI() != 0) { // Solo jugarán una prueba los jugadores que aún no hayan muerto
                // Calculamos resultado
                for (int i = 1; i <= doctoral.getDifficulty(); i++) {
                    result = result + ((2 * i) - 1);
                }
                // Comprobamos si pasa y actualizamos PI, mostrandolo
                if (player.getPI() > result) {
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

        // Mostramos todos los jugadores que hayan evolucionado
        LinkedList<String> changedType = checkUpdateStatus();
        for (String line: changedType) {
            view.showMessageLine(line);
        }
    }


    /*Acaba gestión doctoral*/

    /**Empieza gestion paper**/
    /**
     * Método que ejecuta la gestión general de un PaperPublication
     * @param paper prueba a ejecutar
     */
    private void playPaper (PaperPublication paper) {
        int i = 0;

        // Hacemos que todos los jugadores publiquen su articulo y vamos actualizando su PI
        for (Player player: teamManager.getPlayers()){
            if (player.getPI() != 0) { // Solo jugarán una prueba los jugadores que aún no hayan muerto
                view.showMessageLine(player.getName() + " is submitting...");
                player = publishArticle(paper, player);
                teamManager.updatePlayer(i, player);
                view.showMessageLine(" PI count: " + player.getPI() + "\n");
            }
            i++;
        }

        // Mostramos todos los jugadores que hayan evolucionado
        LinkedList<String> changedType = checkUpdateStatus();
        for (String line: changedType) {
            view.showMessageLine(line);
        }
    }

    /**
     * Método que llama a los métodos que obtienen qué se hace con el artículo enviado y que calcula la puntuación
     * final del jugador
     * @param article Artículo que se está ejecutando (prueba)
     * @param player Jugador que está pasando la prueba
     * @return Nuevo jugador, con la PI actualizada según su desempeño
     */
    private Player publishArticle (PaperPublication article, Player player) {

        // Calculamos de forma aleatoria si se acepta, revisa o rechaza
        int response;
        do {
            response = calculateResponse(article);
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
    private int calculateResponse (PaperPublication article) {
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
    /*Acaba gestión paper*/

    /**Empieza gestión master**/
    /**
     * Método que ejecuta la gestión general de una prueba Master
     * @param master prueba a ejecutar
     */
    private void playMaster (MasterStudies master) {
        int i = 0;
        // Comprobamos para cada jugador si pasan el master y actualizamos su PI
        for (Player player: teamManager.getPlayers()) {
            if (player.getPI() != 0) { // Solo jugarán una prueba los jugadores que aún no hayan muerto
                checkPassed(master, player);
                teamManager.updatePlayer(i, player);
                i++;
            }
        }

        // Mostramos todos los jugadores que hayan evolucionado
        LinkedList<String> changedType = checkUpdateStatus();
        for (String line: changedType) {
            view.showMessageLine(line);
        }
    }

    /**
     * Calcula si un jugador pasa el master o no
     * @param masterStudies prueba a ejecutar
     * @param player jugador que está en la prueba
     */
    private void checkPassed (MasterStudies masterStudies, Player player) {
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

    /*Acaba gestión master*/

    /**
     * Método que revisa los PI de todos los jugadores de un equipo y cambia su estado (evoluciona) si es necesario.
     * Lo usan todos los tipos de pruebas
     * @return lista con los nombres de los jugadores que hayan evolucionado
     */
    private LinkedList<String> checkUpdateStatus () {
        int i = 0;
        LinkedList<String> changedType = new LinkedList<>();
        // Para cada jugador, comprobamos si debe evolucionar de tipo
        for (Player player : teamManager.getPlayers()) {
            if (player instanceof Engineer) {
                // Si debe evolucionar (tiene 10 PI, lo evolucionamos)
                if (player.checkUpdateStatus()) {
                    Player master = new Master(player.getName(), 5);
                    teamManager.updatePlayer(i, master);
                    // Añadimos a la lista de jugadores evolucionados para mostrarlo
                    changedType.add(player.getName() + " is now a master (with 5 PI). ");
                    if (i == teamManager.getPlayers().size() - 1) { // Añade un espacio al final
                        view.showMessage("");
                    }
                }
            } else if (player instanceof Master) {
                // Si debe evolucionar (tiene 10 PI, lo evolucionamos)
                if (player.checkUpdateStatus()) {
                    Player doctor = new Doctor(player.getName(), 5);
                    teamManager.updatePlayer(i, doctor);
                    // Añadimos a la lista de jugadores evolucionados para mostrarlo
                    changedType.add(player.getName() + " is now a doctor (with 5 PI). ");
                    if (i == teamManager.getPlayers().size() - 1) { // Añade un espacio al final
                        view.showMessage("");
                    }
                }
            }
            i++;
        }
        return changedType;
    }
}
