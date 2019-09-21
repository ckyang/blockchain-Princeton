import java.util.*;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    private double m_graph, m_malicious, m_txDistribution;
    private int m_numRounds;
    private boolean[] m_followees;

    private Set<Transaction> m_agree;

    // Setup the user input
    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        m_graph = p_graph;
        m_malicious = p_malicious;
        m_txDistribution = p_txDistribution;
        m_numRounds = numRounds;
    }

    // Setup the followee -> follower graph
    public void setFollowees(boolean[] followees) {
        m_followees = followees;
    }

    // (1) Initial transaction status
    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        m_agree = pendingTransactions;
    }

    // (2) For each round, it will be called to get the agreed transactions
    public Set<Transaction> sendToFollowers() {
        return m_agree;
    }

    // (3) For each round, it will be called to update the latest transaction status
    public void receiveFromFollowees(Set<Candidate> candidates) {
        m_agree.clear();

        for(Candidate c : candidates) {
            m_agree.add(c.tx);
        }
    }
}
