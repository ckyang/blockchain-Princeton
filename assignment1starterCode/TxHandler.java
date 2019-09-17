import java.util.*; 

public class TxHandler {

    private UTXOPool m_utxoPool;
    private HashSet<UTXO> m_toRemoved;

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
        ArrayList<Transaction.Input> inputs = tx.getInputs();
        ArrayList<Transaction.Output> outputs = tx.getOutputs();
        double inValue = 0.0, outValue = 0.0;

        for(int i = 0; i < inputs.size(); ++i) {
            UTXO utxo = new UTXO(inputs.get(i).prevTxHash, inputs.get(i).outputIndex);
   
            // (1) all outputs claimed by {@code tx} are in the current UTXO pool 
            if(!m_utxoPool.contains(utxo)) {
                return false;
            }

            // (3) no UTXO is claimed multiple times by {@code tx}
//            if(m_toRemoved.contains(utxo)) {
//                return false;
//            }

//            m_toRemoved.add(utxo);

            Transaction.Output preOutput = m_utxoPool.getTxOutput(utxo);

            // (2) the signatures on each input of {@code tx} are valid
            if(!Crypto.verifySignature(preOutput.address, tx.getRawDataToSign(i), inputs.get(i).signature)) {
                return false;
            }

            inValue += preOutput.value;
        }

        for(int i = 0; i < outputs.size(); ++i) {
            // (4) all of {@code tx}s output values are non-negative
            if(outputs.get(i).value < 0.0) {
                return false;
            }

            outValue += outputs.get(i).value;
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
        ArrayList<Transaction> valid = new ArrayList<Transaction>();
        m_toRemoved = new HashSet<UTXO>();

        for(Transaction tx : possibleTxs) {
            if(isValidTx(tx)) {
                valid.add(tx);
            }
        }

        Iterator value = m_toRemoved.iterator(); 

        while(value.hasNext()) {
            m_utxoPool.removeUTXO((UTXO)(value.next()));
        }

        Transaction[] res = valid.toArray(new Transaction[valid.size()]);
        return res;
    }

}
