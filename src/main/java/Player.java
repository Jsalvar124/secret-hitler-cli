import java.util.List;
import java.util.Scanner;

public class Player {

    //Attributes
    private static int currentId = 0;
    private int id;
    private String name;
    private Role role;
    private Boolean isAlive;
    private Boolean isPresident;
    private Boolean isChancellor;
    private Boolean vote;
    private final Scanner scanner;


    //Constructor
    public Player(String name, Role role) {
        this.id = ++currentId;
        this.name = name;
        this.role = role;
        this.isAlive = true;
        this.isPresident = false;
        this.isChancellor = false;
        this.vote = null;
        this.scanner = new Scanner(System.in);
    }

    //Getters And Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean alive) {
        isAlive = alive;
    }

    public Boolean getPresident() {
        return isPresident;
    }

    public void setPresident(Boolean president) {
        isPresident = president;
    }

    public Boolean getChancellor() {
        return isChancellor;
    }

    public void setChancellor(Boolean chancellor) {
        isChancellor = chancellor;
    }

    public Boolean getVote() {
        return vote;
    }

    public void setVote(Boolean vote) {
        this.vote = vote;
    }

    public int getId() {
        return id;
    }

    public void vote(Player president, Player chancellor){
//        Scanner scanner = new Scanner(System.in);
        System.out.printf("%s, please vote if you want to elect %s as president and %s as chancellor. (y/n)\n", this.getName(), president.getName(), chancellor.getName());
        String playerVote = scanner.nextLine();
        if(playerVote.equalsIgnoreCase("y")){
            setVote(true);
        } else if (playerVote.equalsIgnoreCase("n")) {
            setVote(false);
        } else {
            System.out.println("Invalid vote, please use 'y' or 'n'.");
            // call vote recursively if vote is invalid.
            vote(president, chancellor);
        }
    }

    public void discardPolicy(List<Policy> drawnPolicies, String position){
//        Scanner scanner = new Scanner(System.in);
        System.out.printf("Mr./Ms. %s, select the index of the policy you want to remove\n", position); // president/chancellor
        for (int i = 0; i < drawnPolicies.size(); i++) {
            System.out.printf("%d. %s Policy\n",(i+1), drawnPolicies.get(i).name()); // Ej:  1. Fascist Policy
        }

        if(scanner.hasNextInt()){ //Checks if the entered value is an integer.
            int policyIndexToRemove = scanner.nextInt() - 1; // Zero based index
            if(policyIndexToRemove<0 || policyIndexToRemove>= drawnPolicies.size()){
                // If policyIndex not in range call discardPolicy Recursively
                System.out.println("Invalid selection. Please try again.");
                discardPolicy(drawnPolicies, position);
            } else {
                drawnPolicies.remove(policyIndexToRemove); // Leaves an Array list of two policies for the chancellor.
            }
        } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Consume invalid input to prevent infinite loop
            discardPolicy(drawnPolicies, position);
        }
    }

    public Player presidentialExecutiveAction(List<Player> players, ExecutiveAction action){
//        Scanner scanner = new Scanner(System.in);
        int selectedPlayerIndex;
        Player selectedPlayer;

        switch (action){
            case INVESTIGATE_PLAYER:
                System.out.println("INVESTIGATE A PLAYER");
                System.out.println("Current president: "+ this.getName());
                System.out.println("Mr./Ms. president, select the index of the player you want to investigate");
                selectedPlayerIndex = selectValidPlayerIndex(players);
                selectedPlayer = players.get(selectedPlayerIndex);
                System.out.printf("The role of %s is: %s\n",selectedPlayer.getName(), selectedPlayer.getRole().name());
                break;
            case KILL_PLAYER:
                System.out.println("EXECUTE A PLAYER");
                System.out.println("Current president: "+ this.getName());
                System.out.println("Mr./Ms. president, select the index of the player you want get rid of");
                selectedPlayerIndex = selectValidPlayerIndex(players);
                selectedPlayer = players.get(selectedPlayerIndex);
                selectedPlayer.setIsAlive(false);
                System.out.printf("%s is now dead!\n", selectedPlayer.getName());
                break;
            case CHOOSE_NEXT_CANDIDATE:
                System.out.println("CHOOSE NEXT PRESIDENTIAL CANDIDATE");
                System.out.println("Current president: "+ this.getName());
                System.out.println("Mr./Ms. president, select the index of the player you want to choose as the next presidential candidate.");
                selectedPlayerIndex = selectValidPlayerIndex(players);
                selectedPlayer = players.get(selectedPlayerIndex);
                break;
            case NOMINATE_CHANCELLOR:
                System.out.println("Current candidate: "+ this.getName());
                // Next nominate cannot be current chancellor.
                System.out.println("Mr./Ms. presidential candidate, select the index of the player you want to nominate as your chancellor candidate.");
                boolean wasLastChancellor;
                do {
                    selectedPlayerIndex = selectValidPlayerIndex(players);
                    selectedPlayer = players.get(selectedPlayerIndex);
                     wasLastChancellor = selectedPlayer.getChancellor();
                     if(wasLastChancellor){
                         System.out.println("Chancellors can not be nominated in consecutive elections. Try someone else.");
                     }
                } while(wasLastChancellor);
                break;
            default:
                System.out.println("Invalid Executive Action");
                selectedPlayer = null;
                break;
        }
        return selectedPlayer;
    }

    //Helper
    public int selectValidPlayerIndex(List<Player> players){
//        Scanner scanner = new Scanner(System.in);
        System.out.println("###  LIST OF PLAYERS  ###");
        for (Player player: players) {
            // Display the list of alive players.
            if(player.getIsAlive())
                System.out.printf("%d. %s\n",player.getId(), player.getName());
        }
        if (scanner.hasNextInt()){
            int index = scanner.nextInt() - 1;
            scanner.nextLine(); // Consume the newline after the int input
            // avoid selecting its own index.
            if(index<0 || index >= players.size()){
                System.out.println("Invalid selection. Please try again.");
                return selectValidPlayerIndex(players);
            } else if(!players.get(index).getIsAlive()){
                System.out.println("Invalid selection. Selected player is dead, try again.");
                return selectValidPlayerIndex(players);
            } else if(players.indexOf(this)==index){
                System.out.println("Invalid selection, you can not select yourself.");
                return selectValidPlayerIndex(players);
            } else {
                return index;
            }
        } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Consume invalid input to prevent infinite loop
            return selectValidPlayerIndex(players);
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", isAlive=" + isAlive +
                ", isPresident=" + isPresident +
                ", isChancellor=" + isChancellor +
                ", vote=" + vote +
                '}';
    }
}
