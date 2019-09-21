import java.util.*;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    // Setup the user input
    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        // IMPLEMENT THIS
    }

    // Setup the followee -> follower graph
    public void setFollowees(boolean[] followees) {
        // IMPLEMENT THIS
    }

    // (1) Initial transaction status
    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        // IMPLEMENT THIS
    }

    // (2) For each round, it will be called to get the agreed transactions
    public Set<Transaction> sendToFollowers() {
        // IMPLEMENT THIS
        Set<Transaction> res = new HashSet<Transaction>();
        return res;
    }

    // (3) For each round, it will be called to update the latest transaction status
    public void receiveFromFollowees(Set<Candidate> candidates) {
        // IMPLEMENT THIS
    }
}
