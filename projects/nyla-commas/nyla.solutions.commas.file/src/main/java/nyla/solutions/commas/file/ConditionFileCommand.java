package nyla.solutions.commas.file;

import java.io.File;

//import javax.annotation.concurrent.NotThreadSafe;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.patterns.expression.ObjectBooleanExpression;
import nyla.solutions.core.util.Debugger;


/**
 * Execute a given file command if the boolean expression is true
 * @author Gregory Green
 *
 */
//@NotThreadSafe
public class ConditionFileCommand implements FileCommand<Object>
{
	/**
	 * Execute fileCommand if expression is true
	 */
	public synchronized Object execute(File file)
	{	
		if(this.objectBooleanExpression == null)
			throw new RequiredException("this.booleanExpression in ConditionFileCommand");
		
		if(this.fileCommand == null)
			throw new RequiredException("this.fileCommand in ConditionFileCommand");
		
		objectBooleanExpression.setEvaluationObject(file);

		if(objectBooleanExpression.apply(file))
		{
			Debugger.println(this,"Executing "+this.fileCommand.getClass().getName());
			return this.fileCommand.execute(file);
		}
		
		
		return null;

	}//---------------------------------------------

	
	
	/**
	 * @return the objectBooleanExpression
	 */
	public synchronized ObjectBooleanExpression getObjectBooleanExpression()
	{
		return objectBooleanExpression;
	}



	/**
	 * @param objectBooleanExpression the objectBooleanExpression to set
	 */
	public synchronized void setObjectBooleanExpression(
			ObjectBooleanExpression objectBooleanExpression)
	{
		this.objectBooleanExpression = objectBooleanExpression;
	}



	/**
	 * @return the fileCommand
	 */
	public synchronized FileCommand<Object> getFileCommand()
	{
		return fileCommand;
	}//---------------------------------------------

	/**
	 * @param fileCommand the fileCommand to set
	 */
	public synchronized void setFileCommand(FileCommand<Object> fileCommand)
	{
		this.fileCommand = fileCommand;
	}

	private FileCommand<Object> fileCommand = null;
	private ObjectBooleanExpression objectBooleanExpression = null;
	
}
