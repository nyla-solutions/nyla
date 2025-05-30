package nyla.solutions.commas.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import nyla.solutions.commas.Command;
import nyla.solutions.core.util.Organizer;

public class TransformationAroundCommandAdvice
{
	/**
	 * transform the arguments and return for a join point
	 * @param proceedingJoinPoint
	 * @return transformed output
	 * @throws Throwable
	 */
	public Object transform(ProceedingJoinPoint proceedingJoinPoint) throws Throwable 
	{
		Object[] arguments = proceedingJoinPoint.getArgs();
		if(beforeCommand != null)
		{
			Object inputArgument = arguments;
			
			if(arguments != null && arguments.length == 1)
			{
				//only use a single argument
				inputArgument = arguments[0];
			}
			
			Object resultArguments = beforeCommand.execute(inputArgument);
			
			arguments = Organizer.toArray(resultArguments);
			
		}
		
	    // proceeding processing and get results
	    Object retVal = proceedingJoinPoint.proceed(arguments);
	    
	    
	    if(afterCommand != null)
	    {
	    	retVal = afterCommand.execute(retVal);
	    }
	    
	    return retVal;
	}// ------------------------------------------------
	
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

	private Command<Object,Object> beforeCommand = null;
	private Command<Object,Object> afterCommand = null;
	

}
