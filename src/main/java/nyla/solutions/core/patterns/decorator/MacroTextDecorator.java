package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.data.Textable;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Macro implement for text objects
 * @author Gregory Green
 *
 */
public class MacroTextDecorator implements TextDecorator<Textable>
{
	private Textable target = null;

	private Collection<TextDecorator<Textable>>  textables = new ArrayList<TextDecorator<Textable>> ();
	private String separator = "";

	public MacroTextDecorator()
	{
	}

	public MacroTextDecorator(String separator)
	{
		this.separator = separator;
	}

	public String getSeparator()
	{
		return separator;
	}

	public void setSeparator(String separator)
	{
		this.separator = separator;
	}

	public MacroTextDecorator(Textable target, Collection<TextDecorator<Textable>> textables)
	{
		this.target = target;
		this.textables = textables;
	}

	/**
	 * Get the target text and execute each text decorator on its results
	 * @return the text results
	 */
	public String getText()
	{
		//loop thru text
		StringBuilder builder = new StringBuilder();
		if(this.target != null && this.textables.isEmpty())
		{
			builder.append(this.target.getText());
		}

		//loop thru decorator and get results from each
		String decoration =null;
		for(TextDecorator<Textable> textDecorator :textables)
		{
			if(this.target != null && textDecorator.getTarget() == null)
			{
				textDecorator.setTarget(target);
			}

			decoration = textDecorator.getText();

			if(decoration == null || decoration.length() ==0)
				continue;;

			if(builder.length() != 0)
				builder.append(separator);

			builder.append(decoration);
		}

		return builder.toString();
	}//---------------------------------------------

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

	/**
	 * @return the textables
	 */
	public Collection<TextDecorator<Textable>> getTextables()
	{
		return textables;
	}

	/**
	 * @param textables the textables to set
	 */
	public void setTextables(Collection<TextDecorator<Textable>>  textables)
	{
		this.textables = textables;
	}

}
