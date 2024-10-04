import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("###  SECRET HITLER!  ###");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of players: ");
        int numberOfPlayers = scanner.nextInt();
        Game game = new Game(numberOfPlayers);

        List<Role> rolesList = game.assignRoles();

        game.initializeGame();

        List<Player> players = game.getPlayers();
        // First Round
        boolean electionResult = game.runElecion(players.get(0), players.get(1));

        if(electionResult){
            game.runLegislativeSession(players.get(0), players.get(1));
        }
        // Second Round
        electionResult = game.runElecion(players.get(2), players.get(3));

        if(electionResult){
            game.runLegislativeSession(players.get(2), players.get(3));
        }
        // Third Round
        electionResult = game.runElecion(players.get(4), players.get(0));

        if(electionResult){
            game.runLegislativeSession(players.get(4), players.get(0));
        }

        System.out.println(game.toString());
    }
}
