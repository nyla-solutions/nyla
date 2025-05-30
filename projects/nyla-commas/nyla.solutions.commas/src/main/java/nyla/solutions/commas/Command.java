package nyla.solutions.commas;

/**
 * This is an abstract interface to execute an command operation.
 * @author Gregory Green
 *
 */
public interface Command<ReturnType  extends Object,InputType extends Object>
{
	/**
	 * Implemented command interface to execute an operation on a argument and possibility return values
	 * @param input the input object 
	 * @return the altered result
	 */
	ReturnType execute(InputType input);

}
