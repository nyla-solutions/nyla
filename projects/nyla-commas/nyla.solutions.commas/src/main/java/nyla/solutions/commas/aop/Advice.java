package nyla.solutions.commas.aop;
import nyla.solutions.commas.Command;
import nyla.solutions.commas.CommandFacts;

/**
 * Aspect similar to AOP based
 * @author Gregory Green
 *
 */
public interface Advice
{

	/**
	 * Before advice in the form  a command
	 * @return command pre advice
	 */
	Command<?,?> getBeforeCommand();
	
	/**
	 * After advice in the form  a command
	 * @return command post advice
	 */
	Command<?,?> getAfterCommand();
	
	/**
	 * 
	 * @return the function facts
	 */
	CommandFacts getFacts();
	
	/**
	 * 
	 * @param facts the function facts
	 */
	void setFacts(CommandFacts facts);
}
