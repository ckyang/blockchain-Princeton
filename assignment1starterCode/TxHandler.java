import java.util.*; 

public class TxHandler {

    UTXOPool m_utxoPool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        m_utxoPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        ArrayList<Transaction.Input> input = tx.getInputs();
        ArrayList<Transaction.Output> output = tx.getOutputs();
        double inValue = 0.0, outValue = 0.0;

        for(int i = 0; i < input.size(); ++i) {
            // (2) the signatures on each input of {@code tx} are valid
            if(!Crypto.verifySignature(output.get(input.get(i).outputIndex).address, tx.getRawDataToSign(i), input.get(i).signature)) {
                return false;
            }

            inValue += output.get(input.get(i).outputIndex).value;
        }

        for(int i = 0; i < output.size(); ++i) {
            // (4) all of {@code tx}s output values are non-negative
            if(output.get(i).value < 0.0) {
                return false;
            }

            outValue += output.get(i).value;
        }

        // (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
        //     values; and false otherwise
        return inValue >= outValue;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        ArrayList<Transaction> res = new ArrayList<Transaction>();

        for(Transaction tx : possibleTxs) {
            if(isValidTx(tx)) {
                res.add(tx);
            }
        }

        return (Transaction[])res.toArray();
    }

}
