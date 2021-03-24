package nyla.solutions.core.exception.fault;

import nyla.solutions.core.exception.ErrorClassification;

import java.io.Serializable;

/**
 * Represents an application issue
 * @author Gregory Green
 *
 */
public interface Fault extends Serializable, ErrorClassification
{
	/**
	 * @return the source operation where the fault occurs
	 */
	public String getOperation();

	/**
	 * @return the module name
	 */
	public abstract String getModule();
	
	/**
	 * 
	 * @return the argument related to the fault
	 */
	public abstract Object getArgument();
	
	/**
	 * 
	 * @return the fault message
	 */
	public String getMessage();
	
	
	/**
	 * 
	 * @return the error code
	 */
	//@Override
	public String getCode();
	
	/**
	 * 
	 * @return the error category
	 */
	@Override
	public String getCategory();
	
	/**
	 * @return additional notes about the fault 
	 */
	public String getNotes();
	
	
	/**
	 * 
	 * @return printed stack trace
	 */
	public String getErrorStackTrace();
	
	/**
	 * 
	 * @return a generate classification of the source of the issues such as entity types
	 */
	public String getArgumentType();
	
	/**
	 * 
	 * @return a generic unique identified related to the source type
	 */
	public String getArgumentId();
	
}