package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.data.Textable;

public class StringText implements Textable
{
	
	
	/**
	 * 
	 */
	public StringText()
	{
	}// --------------------------------------------------------
	/**
	 * @param text
	 */
	public StringText(String text)
	{
		super();
		this.text = text;
	}
	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}
	
	private String text = null;

}
