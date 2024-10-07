import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameBoard {
    private int liberalPolicies;
    private int fascistPolicies;
    private int failedElectionCount;
    private static final int MAX_FAILED_ELECTION = 3;
    private static final int MAX_LIBEARL_POLICIES = 6;
    private static final int MAX_FASCIST_POLICIES = 11;
    private List<Policy> policies;

    // There are 17 policies tiles, 6 liberal and 11 fascist.
    // Keeps track of the enacted policies and has methods to enact a given policy.
    public GameBoard() {
        this.liberalPolicies = 0;
        this.fascistPolicies = 0;
        this.failedElectionCount = 0;
        this.policies = new ArrayList<>();
    }

    public void enactLiberalPolicy(){
        if(liberalPolicies<MAX_LIBEARL_POLICIES){
            liberalPolicies++;
            //reset election failed election tracker.
            failedElectionCount = 0;
            System.out.println("A Liberal policy has been enacted!");
        } else {
            System.out.println("Damn! Maximum number of Liberal policies has already been enacted.");
        }
    }

    public void enactFascistPolicy(){
        if(fascistPolicies<MAX_FASCIST_POLICIES){
            fascistPolicies++;
            //reset election failed election tracker.
            failedElectionCount = 0;
            System.out.println("A Fascist policy has been enacted!");
        } else {
            System.out.println("Damn! Maximum number of Fascist policies has already been enacted.");
        }
    }

    public int getLiberalPolicies() {
        return liberalPolicies;
    }

    public int getFascistPolicies() {
        return fascistPolicies;
    }

    public int getFailedElectionCount() {
        return failedElectionCount;
    }


    public void initializePoliciesDeck(){
        // Add policies, and shuffle.
        for (int i = 0; i < 17-(liberalPolicies+fascistPolicies); i++) {
            if(i<(MAX_LIBEARL_POLICIES-liberalPolicies)){
                policies.add(Policy.LIBERAL);
            } else {
                policies.add(Policy.FASCIST);
            }
        }
        System.out.println("Policies Deck Reshuffled!");
        Collections.shuffle(policies);
        System.out.println(policies);
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public void increaseFailedElectionCount() {
        failedElectionCount++;
        if(failedElectionCount == MAX_FAILED_ELECTION){
            System.out.println("There has been 3 failed elections in a row!, to avoid chaos a policy must be enacted!");
            Policy topPolicy = policies.get(0);
            if(topPolicy == Policy.FASCIST){
                policies.remove(0); //remove first policy
                System.out.println("Fascist policy enacted.");
                enactFascistPolicy();
            } else {
                policies.remove(0); //remove first policy
                System.out.println("Liberal policy enacted.");
                enactLiberalPolicy();
            }
            // reset failed election count.
            System.out.println("Failed election counter restarting.");
            failedElectionCount = 0;
        }
    }

    @Override
    public String toString() {
        return "GameBoard{" +
                "liberalPolicies=" + liberalPolicies +
                ", fascistPolicies=" + fascistPolicies +
                ", failedElectionCount=" + failedElectionCount +
                ", policies=" + policies +
                '}';
    }
}
