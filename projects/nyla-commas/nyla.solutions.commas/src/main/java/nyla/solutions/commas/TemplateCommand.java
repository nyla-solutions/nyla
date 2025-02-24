package nyla.solutions.commas;

import nyla.solutions.core.exception.RequiredException;

/**
 * @author Gregory Green
 *
 */
public class TemplateCommand implements Command<Object,Object> 
{

	/**
	 * Execute a target command to before and after command execution processing
	 * @see nyla.solutions.commas.Command#execute(java.lang.Object)
	 */
	public Object execute(Object source)
	{
		if (this.targetCommand == null)
			throw new RequiredException("this.targetCommand");
		
		
		if (beforeCommand != null)
		{
			source = beforeCommand.execute(source);
		}
		
		
		//Perform operation
		source = targetCommand.execute(source);
	    
	    
	    if(afterCommand == null)
	    	return source;
	    
	    return afterCommand.execute(source);
	}
	
	/**
	 * @return the beforeCommand
	 */
	public Command<Object,Object> getBeforeCommand()
	{
		return beforeCommand;
	}
	/**
	 * @param beforeCommand the beforeCommand to set
	 */
	public void setBeforeCommand(Command<Object,Object> beforeCommand)
	{
		this.beforeCommand = beforeCommand;
	}
	/**
	 * @return the targetCommand
	 */
	public Command<Object,Object> getTargetCommand()
	{
		return targetCommand;
	}
	/**
	 * @param targetCommand the targetCommand to set
	 */
	public void setTargetCommand(Command<Object,Object> targetCommand)
	{
		this.targetCommand = targetCommand;
	}
	
	/**
	 * @return the afterCommand
	 */
	public Command<Object,Object> getAfterCommand()
	{
		return afterCommand;
	}
	/**
	 * @param afterCommand the afterCommand to set
	 */
	public void setAfterCommand(Command<Object,Object> afterCommand)
	{
		this.afterCommand = afterCommand;
	}

	private Command<Object,Object>  beforeCommand = null;
	private Command<Object,Object> targetCommand = null;
	private Command<Object,Object> afterCommand = null;

}
