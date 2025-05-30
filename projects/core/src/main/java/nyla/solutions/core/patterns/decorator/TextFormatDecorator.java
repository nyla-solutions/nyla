package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Text;

import java.util.Map;


/**
 * Text format decorator using the target Textable
 *
 * <code>
 *        TextFormatDecorator subject = new TextFormatDecorator();
 *         subject.setTarget(new StringText("hello ${name}"));
 *         Map&lt;Object, Object&gt; map = new HashMap&lt;&gt;();
 *         map.put("name","Joe");
 *         subject.setMap(map);
 *         assertEquals("hello Joe",subject.getText());
 * </code>
 * @author Gregory Green
 *
 */
public class TextFormatDecorator implements TextDecorator<Textable>
{
	/**
	 * 
	 * @return Text.formatMap(template,this.map)
	 */
	public String getText()
	{
		if(this.target == null)
			return null;
		
		String template = this.target.getText();
		
		if(this.map == null)
			return template;
			
		try
		{
			return Text.format(template,this.map);
			
		}
		catch (FormatException e)
		{
			throw new SystemException(e);
		}
	}// --------------------------------------------------------

	/**
	 * Set the target 
	 * @param target the target text format 
	 */
	public void setTarget(Textable target)
	{
		this.target = target;
		
	}// --------------------------------------------------------
	/**
	 * 
	 * @see nyla.solutions.core.patterns.decorator.TextDecorator#getTarget()
	 */
	public Textable getTarget()
	{
		return target;
	}

	/**
	 * @return the map
	 */
	public Map<Object, Object> getMap()
	{
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Map<Object, Object> map)
	{
		this.map = map;
	}

	private Textable target;
	private Map<Object, Object> map;

}
