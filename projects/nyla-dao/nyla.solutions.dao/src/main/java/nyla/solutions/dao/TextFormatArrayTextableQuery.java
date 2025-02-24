/**
 * 
 */
package nyla.solutions.dao;
import nyla.solutions.core.data.DataRow;
import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;

/**
 * @author Gregory Green 
 * 
 * Query object text object to returns a single text SQL select result
 * This uses the Text.formatArray method based on a given pattern
 * 
 *
 */
public class TextFormatArrayTextableQuery extends Query implements Textable
{

	/**
	 * @return first text results
	 */
	public String getText()
	{		
	   try
	   {
		   DataResultSet rs = super.getResults();
		   
		   DataRow dataRow = (DataRow)rs.getRows().iterator().next();
		
		   Object[] inputs = dataRow.getStrings();
		   Debugger.println(this,"inputs:"+Debugger.toString(inputs));
		   
		   String text =  Text.formatArray(this.pattern.getText(), inputs);
		   
		   Debugger.println(this,"Formatted text:"+text);
		   
		   return text;
	   } 
	   catch(FormatException e)
	   {
		   throw new ConfigException(e);
	   }
	   catch (NoDataFoundException e)
	   {
		   Debugger.printWarn(e.getMessage());
		   
		   return null;
	   }
	}//---------------------------------------------
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}//---------------------------------------------	
   
	/**
	 * @return the pattern
	 */
	public Textable getPattern()
	{
		return pattern;
	}
	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(Textable pattern)
	{
		this.pattern = pattern;
	}

	private Textable pattern;
}
