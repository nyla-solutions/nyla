package nyla.solutions.commas.file;

import java.io.File;
import java.io.IOException;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.io.SynchronizedIO;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;



/**
 * Overwrite the file with textable.getText(). It replaces the this.regExp with the output of 
 * textable.getText().
 * 
 * @author Gregory Green
 *
 */
public class ReplaceWithTextCommand implements FileCommand<Void>
{
	/**
	 * Overwrite file with text output 
	 */
	public Void execute(File file)
	{		
		if(this.textable == null)
			throw new RequiredException("this.textable in ReplaceWithTextCommand");

		try
		{
			if(regExp != null)
				regExp = regExp.trim();
			
			//read text
			String text = IO.readFile(file.getAbsolutePath());
			
			//replace text
			
			if(textable == null)
				throw new RequiredException("textable in ReplaceWithTextCommand");
			
			SynchronizedIO.getInstance().writeFile(file.getAbsolutePath(),Text.replaceForRegExprWith(text,this.regExp,this.textable.getText()));
			
			return null;
		} 
		catch (IOException e)
		{
			throw new SystemException(Debugger.stackTrace(e));
		}
	}//---------------------------------------------
	/**
	 * @return the textable
	 */
	public Textable getTextable()
	{
		return textable;
	}//---------------------------------------------
	/**
	 * @param textable the textable to set
	 */
	public void setTextable(Textable textable)
	{
		this.textable = textable;
	}//---------------------------------------------

	/**
	 * @return the regExp
	 */
	public String getRegExp()
	{
		return regExp;
	}//---------------------------------------------
	/**
	 * @param regExp the regExp to set
	 */
	public void setRegExp(String regExp)
	{
		this.regExp = regExp;
	}//---------------------------------------------

	private String regExp = "";
	private Textable textable = null;
}
