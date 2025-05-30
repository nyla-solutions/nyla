package nyla.solutions.core.patterns.transaction;

/**
 * This interface indicates the nature of a function call transaction support.
 * 
 *  NONE - non-transaction
 *  READONLY- read (not write) 
 *  WRITE - read/write or read transaction data
 * @author Gregory Green
 *
 */
public interface Transactional
{
	/**
	 * 
	 * NONE - no transaction support
	 * READONLY- read (not write) transaction
	 * WRITE - read/write or read transaction data
	 */
	public enum TransactionType
	{
		NONE,
		READONLY,
		WRITE
	}// ------------------------------------------------
	
	/**
	 * Set the transaction type
	 * @param transactionType the transaction type to set
	 */
	void setTransactionType(TransactionType transactionType);
	
	/**
	 * 
	 * @return the current transaction status
	 */
	TransactionType getTransactionType();
}
