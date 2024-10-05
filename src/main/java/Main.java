import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("###  SECRET HITLER!  ###");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of players: ");
        int numberOfPlayers = scanner.nextInt();
        Game game = new Game(numberOfPlayers);
        game.startGame();

        System.out.println("###  GAME OVER!  ###");
    }
}
