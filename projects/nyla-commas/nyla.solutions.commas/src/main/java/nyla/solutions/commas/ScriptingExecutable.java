package nyla.solutions.commas;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.patterns.scripting.Scripting;
import nyla.solutions.core.util.settings.Settings;

/**
 * Executable wrapper to execute a scripting expression
 * @author Gregory Green
 *
 */
public class ScriptingExecutable implements Executable
{

	/**
	 * @return scripting.interpret(expression, input)
	 * @see nyla.solutions.commas.Command#execute(java.lang.Object)
	 */
	@Override
	public Integer execute(Settings input)
	{
		if(scripting == null)
			throw new RequiredException("this.scripting");
		
		return scripting.interpret(expression, input);
	}
	
	/**
	 * @return the scripting
	 */
	public Scripting<Integer, Settings> getScripting()
	{
		return scripting;
	}
	/**
	 * @param scripting the scripting to set
	 */
	public void setScripting(Scripting<Integer, Settings> scripting)
	{
		this.scripting = scripting;
	}
	/**
	 * @return the expression
	 */
	public String getExpression()
	{
		return expression;
	}
	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression)
	{
		this.expression = expression;
	}


	private Scripting<Integer, Settings> scripting;
	private String expression = null;
}
