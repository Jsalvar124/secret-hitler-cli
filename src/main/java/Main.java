import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("###  SECRET HITLER!  ###");

        Scanner scanner = new Scanner(System.in);
        int numberOfPlayers = 0;
        do {
            System.out.println("Enter the number of players: ");
            if(scanner.hasNextInt()){
                numberOfPlayers = scanner.nextInt();
                if(numberOfPlayers<5 || numberOfPlayers>10)
                    System.out.println("Invalid input, please enter a number between 5 and 10");
                scanner.nextLine();
            } else {
                System.out.println("Invalid input, please enter a number.");
                scanner.nextLine();
            }
        } while(numberOfPlayers<5 || numberOfPlayers>10);

        Game game = new Game(numberOfPlayers);
        game.startGame();

        System.out.println("###  GAME OVER!  ###");
    }
}
