import java.util.*;

public class Game {

    /*
    Liberals win by enacting 5 liberal policies OR by killing Hitler
    Fascist win by enacting 6 facist policies OR by enacting 3 policies and electing Hitler as Chancellor.
    Fascists are a minority but they know each other identities, they know how Hitler is.
    Hitler does not know who are the other Fascists.
     */

    private int numberOfPlayers;
    private List<Player> players;
    private GameBoard gameBoard;
    private Player currentPresident;
    private Player nextPresidentialCandidate;
    private Player currentChancellor;
    private boolean isVetoPowerEnabled;
    private boolean isGameOver;

    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = new ArrayList<>();
        this.gameBoard = new GameBoard();
        this.isVetoPowerEnabled = false;
        this.isGameOver = false;
    }

    public List<Role> assignRoles(){
        List<Role> roles = new ArrayList<>();
        // Add one Hitler
        roles.add(Role.HITLER);
        switch (numberOfPlayers){
            case 5,6:
                // 5 or 6 players : 1 fascist , 1 hitler, 3-4 liberals
                roles.add(Role.FASCIST);
                break;
            case 7,8:
                // 7 or 8 players : 2 fascist , 1 hitler, 4-5 liberals
                Collections.addAll(roles, Role.FASCIST, Role.FASCIST);
                break;
            case 9,10:
                // 9 or 10 players : 3 fascist , 1 hitler, 5-6 liberals
                Collections.addAll(roles, Role.FASCIST, Role.FASCIST, Role.FASCIST);
                break;
            default:
                throw new IllegalArgumentException("Number of players has to be betweeen 5-10");
        }
        // Add remaining players as liberals
        int numberOfLiberals = numberOfPlayers - roles.size();
        for (int i = 0; i < numberOfLiberals; i++) {
            roles.add(Role.LIBERAL);
        }

        //Shuffle roles
        Collections.shuffle(roles);
        return roles;
    }

    public void initializeGame(){
        // Get shuffled roles list
        List<Role> rolesList = assignRoles();
        // Shuffle policies list
        gameBoard.initializePoliciesDeck();
        // Create Each player with its role.
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println("Player "+(i+1)+ ", please enter your name ");
            String name = scanner.nextLine();
            Role role = rolesList.get(i);
            Player player = new Player(name,role);
            //Add them to the players attribute list
            players.add(player);
        }
    }

    // The result of the election is true, if there is a majority and false otherwise.
    public boolean runElecion(Player president, Player chancellor){
        System.out.println("###  RUNNING ELECTION  ###");
        System.out.printf("Failed Election Count: %d\n", gameBoard.getFailedElectionCount());
//        if(president == currentPresident || chancellor == currentChancellor){
//            System.out.println("Chancellor cannot be reelected for consecutive terms, the election is nullified.");
//            return false;
//        }
        int yesVotes = 0;
        int noVotes = 0;

        for (Player player: players) {
            //Check that only alive players can vote!
            if(player.getIsAlive()){
                player.vote(president, chancellor);
                if(player.getVote()){
                    yesVotes++;
                } else {
                    noVotes++;
                }
            }
        }
        System.out.println("Votes in favor: " + yesVotes);
        System.out.println("Votes against: " + noVotes);
        if(yesVotes > noVotes){
            System.out.printf("The Candidates are majority, congratulations! %s is now president and %s is chancellor.\n", president.getName(), chancellor.getName());
            president.setPresident(true);
            chancellor.setChancellor(true);
            // Reset president and chancellor state to false
            if(currentChancellor!=null && currentPresident!=null){
                currentPresident.setPresident(false);
                currentChancellor.setChancellor(false);
            }

        } else {
            System.out.println("The election did not reach a majority! It will be repeated with a new pair of candidates.");
            gameBoard.increaseFailedElectionCount();
        }
        // Asign currentPresident and Chancelor even if election was not valid, this to avoid the same candidate.
        currentPresident = president;
        currentChancellor = chancellor;

        return yesVotes > noVotes? true : false;
    }

    /*
    During the Legislative Session, the President and Chancellor work together to enact a new Policy in secret.
    The President draws the top three tiles from the Policy deck, looks at them in secret, and discards one
    tile face down into the Discard pile. The remaining two tiles go to the Chancellor, who looks in secret,
    discards one Policy tile face down, and enacts the remaining Policy by placing the tile face up
    on the corresponding track.
     */
    public Policy runLegislativeSession(Player president, Player chancellor){
        System.out.println("###  LEGISLATIVE SESSION  ###");
        // Draw three policies from the shuffled deck.
        List<Policy> drawnPolicies = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            // Remove the first card from the policies deck and put it in the drawn Deck
            drawnPolicies.add(gameBoard.getPolicies().remove(0));
        }
        System.out.println("President drew 3 policies: " + drawnPolicies);
        // Give them to the President, who discards one and gives the remaining policies to the chancellor.
        president.discardPolicy(drawnPolicies, "President");

        // The Chancellor receives two policies and discards another one. the remaining policy is enacted.
        chancellor.discardPolicy(drawnPolicies, "Chancellor"); //reduces drawnPolicies by one.
        Policy selectedPolicy = drawnPolicies.get(0); // only one policy remaining on the policy array.
        // Enact the selected policy.
        if(selectedPolicy == Policy.LIBERAL){
            gameBoard.enactLiberalPolicy();
        } else {
            gameBoard.enactFascistPolicy();
        }
        System.out.printf("Policies Count: LIBERAL: %d - FASCIST: %d\n", gameBoard.getLiberalPolicies(), gameBoard.getFascistPolicies());
        return selectedPolicy;
    }

    public void runExecutiveAction(Policy latestPolicyEnacted){
        System.out.println("###  EXECUTIVE ACTION  ###");
        // Only Fascist Policies get Executive Actions
        if(latestPolicyEnacted == Policy.LIBERAL){
            return;
        }

        // Get the current count of enacted fascist policies.
        int fascistPolicies = gameBoard.getFascistPolicies();
        // Kept the repeated code to help understand the difference between boards.
        // Presidential Powers by number of players. 5-6
        if(numberOfPlayers == 5 || numberOfPlayers == 6){
            switch (fascistPolicies){
                case 0,1,2:
                    System.out.println("No Executive Actions for this policy");
                    break;
                case 3:
                    // The President Examines the top three cards.
                    examineFirstThreePolicies();
                    break;
                case 4:
                    // The President MUST kill a player.
                    currentPresident.presidentialExecutiveAction(players,ExecutiveAction.KILL_PLAYER);
                    break;
                case 5:
                    // The President MUST kill a player And Veto Power is unlocked.
                    currentPresident.presidentialExecutiveAction(players,ExecutiveAction.KILL_PLAYER);
                    isVetoPowerEnabled=true;
                    break;
                case 6:
                    // Fascists Win.
                    System.out.println("Oh look at that!, another fascist policy has been enacted...");
                    break;
            }
        }
        // Presidential Powers by number of players. 7-8
        if(numberOfPlayers == 7 || numberOfPlayers == 8){
            switch (fascistPolicies){
                case 0,1:
                    System.out.println("No Executive Actions for this policy");
                    break;
                case 2:
                    // The President Investigates a player's Party Member Card.
                    currentPresident.presidentialExecutiveAction(players,ExecutiveAction.INVESTIGATE_PLAYER);
                    break;
                case 3:
                    // The President picks the next presidential candidate.
                    nextPresidentialCandidate = currentPresident.presidentialExecutiveAction(players,ExecutiveAction.CHOOSE_NEXT_CANDIDATE);
                    break;
                case 4:
                    // The President MUST kill a player.
                    currentPresident.presidentialExecutiveAction(players,ExecutiveAction.KILL_PLAYER);
                    break;
                case 5:
                    // The President MUST kill a player And Veto Power is unlocked.
                    currentPresident.presidentialExecutiveAction(players,ExecutiveAction.KILL_PLAYER);
                    isVetoPowerEnabled=true;
                    break;
                case 6:
                    // Fascists Win.
                    System.out.println("Oh look at that!, another fascist policy has been enacted...");
                    break;
            }
        }
        // Presidential Powers by number of players. 9-10
        if(numberOfPlayers == 9 || numberOfPlayers == 10){
            switch (fascistPolicies){
                case 0:
                    System.out.println("No Executive Actions for this policy");
                    break;
                case 1,2:
                    // The President Investigates a player's Party Member Card.
                    currentPresident.presidentialExecutiveAction(players,ExecutiveAction.INVESTIGATE_PLAYER);
                    break;
                case 3:
                    // The President picks the next presidential candidate.
                    currentPresident.presidentialExecutiveAction(players,ExecutiveAction.CHOOSE_NEXT_CANDIDATE);
                    break;
                case 4:
                    // The President MUST kill a player.
                    currentPresident.presidentialExecutiveAction(players,ExecutiveAction.KILL_PLAYER);
                    break;
                case 5:
                    // The President MUST kill a player And Veto Power is unlocked.
                    currentPresident.presidentialExecutiveAction(players,ExecutiveAction.KILL_PLAYER);
                    isVetoPowerEnabled=true;
                    break;
                case 6:
                    // Fascists Win.
                    System.out.println("Oh look at that!, another fascist policy has been enacted...");
                    break;
            }
        }

    }

    public boolean checkGameEndConditions() {

        if (gameBoard.getLiberalPolicies() == 5) {
            System.out.println("### 5 LIBERAL POLICIES WERE ENACTED: LIBERALS WIN!  ###");
            isGameOver = true;
            return true;
        } else if (gameBoard.getFascistPolicies() == 6) {
            System.out.println("### 6 FASCIST POLICIES WERE ENACTED: HITLER AND THE FASCISTS WIN!  ###");
            isGameOver = true;
            return true;
        } else if (!isHitlerAlive()) {
            System.out.println("### HITLER WAS KILLED: LIBERALS WIN!  ###");
            isGameOver = true;
            return true;
        }
        return false;
    }
    public void startGame(){
        // Run initial methods.
        assignRoles();
        initializeGame();
        // Debbug
        System.out.println(players);

        int presidentIndex = 0;
        int nextPresidentIndex;

        Player presidentCandidate = players.get(presidentIndex); // First player starts as president
        Player chancellorCandidate = presidentCandidate.presidentialExecutiveAction(players, ExecutiveAction.NOMINATE_CHANCELLOR);

        // Start running Elections
        while(!isGameOver){
            boolean electionResult = runElecion(presidentCandidate, chancellorCandidate);

            if(isHitlerChancelorAfterThreeFascistPolicies()){
                System.out.println("### HITLER WAS ELECTED CHANCELLOR AFTER THREE FASCIST POLICIES: HITLER AND THE FASCISTS WIN!  ###");
                isGameOver = true;
                break;
            }
            if(checkGameEndConditions()){
                break;
            }
            //DEBBUG
            System.out.println(players);
            // Check if there are at least 3 cards on the policies deck.
            if(gameBoard.getPolicies().size()<3){
                // If so, reshuffle deck with all discarded, but not the enacted policies.
                gameBoard.initializePoliciesDeck();
            }
            if(electionResult){
                Policy selectedPolicy = runLegislativeSession(currentPresident,currentChancellor);
                if(checkGameEndConditions()){
                    break;
                }
                //DEBBUG
                System.out.println(gameBoard.getPolicies());
                System.out.println("REMAINING POLICIES: "+gameBoard.getPolicies().size());
                runExecutiveAction(selectedPolicy);
                if(checkGameEndConditions()){
                    break;
                }
            }
                //Get a new pair of candidates.
                // If a previous Executive order has a preselected candidate, use it.
                if(nextPresidentialCandidate!=null && !nextPresidentialCandidate.equals(currentPresident)){
                    presidentCandidate = nextPresidentialCandidate; // loose Executive order if done uncorrectly!
                } else {
                    //Pick the next alive possible candidate.
                    int i = 1;
                    do {
                        nextPresidentIndex = (players.indexOf(currentPresident) + i++) % players.size();
                        //Verify that the candidate is not the current president and it's alive,
                        presidentCandidate = players.get(nextPresidentIndex);

                    } while(!presidentCandidate.getIsAlive() ||  presidentCandidate.equals(currentPresident));
                }
                chancellorCandidate = presidentCandidate.presidentialExecutiveAction(players, ExecutiveAction.NOMINATE_CHANCELLOR);
        }

    }

    //Helper
    public void examineFirstThreePolicies(){
        System.out.println("Mr./Ms. President, Here are the first three policies on the deck");
        for (int i = 0; i < 3; i++) {
            System.out.println((i+1)+". "+gameBoard.getPolicies().get(i));
        }
    }
    private boolean isHitlerAlive() {
        for (Player player : players) {
            if (player.getRole() == Role.HITLER && player.getIsAlive()) {
                return true;
            }
        }
        return false;
    }
    private boolean isHitlerChancelorAfterThreeFascistPolicies(){
        if(currentChancellor!=null){
            return currentChancellor.getRole() == Role.HITLER && gameBoard.getFascistPolicies() >= 3;
        }
        return false;
    }

    //Getters and Setters
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "Game{" +
                "numberOfPlayers=" + numberOfPlayers +
                ", players=" + players +
                ", gameBoard=" + gameBoard +
                ", currentPresident=" + currentPresident +
                ", currentChancellor=" + currentChancellor +
                ", isVetoPowerEnabled=" + isVetoPowerEnabled +
                '}';
    }
}
