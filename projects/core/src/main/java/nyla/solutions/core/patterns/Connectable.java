package nyla.solutions.core.patterns;

import nyla.solutions.core.exception.ConnectionException;

/**
 * Represents a connect DAO
 * @author Gregory Green
 *
 */
public interface Connectable extends Disposable
{
	/**
	 * Establish a connection
	 * @throws ConnectionException
	 */
	void connect()
	throws ConnectionException;
}
