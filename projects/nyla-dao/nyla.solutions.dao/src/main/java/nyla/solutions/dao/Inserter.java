package nyla.solutions.dao;

import java.sql.SQLException;

import nyla.solutions.core.exception.DuplicateRowException;
import nyla.solutions.core.patterns.Disposable;

/**
 * Interface to insert data
 * @author greeng3
 *
 */
public interface Inserter extends Disposable
{
	/**
	 * Insert method
	 * @param aInputs the input records
	 * @param aSQL the insert prepared statement
	 */
	public boolean insert(Object [] aInputs, String aSQL)
	throws SQLException, DuplicateRowException;

}
