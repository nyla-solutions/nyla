/**
 * 
 */
package nyla.solutions.core.patterns.decorator;


import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.util.Text;


/**
 * Decorate the Textable by parsing out a given string token.
 * The desire string is specified with the start and end tag
 * @author Gregory Green
 *
 */
public class ParseTextDecorator implements TextDecorator<Textable>
{

	/**
	 * @return the parse content of the given textable
	 */
	public String getText()
	{
		if(this.target == null)
			throw new RequiredException("this.target in ParseTextDecorator");

		String text = target.getText();
		if(text == null || text.length() ==0)
			return text;

		return Text.parseText(text, start, end);
	}//---------------------------------------------
	
	/**
	 * @return the start
	 */
	public String getStart()
	{
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(String start)
	{
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public String getEnd()
	{
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(String end)
	{
		this.end = end;
	}


	/**
	 * @return the target
	 */
	public Textable getTarget()
	{
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Textable target)
	{
		this.target = target;
	}


	private String start = "";
	private String end = "";
	private Textable target= null;

}
