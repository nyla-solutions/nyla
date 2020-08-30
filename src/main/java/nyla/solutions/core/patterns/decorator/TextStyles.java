package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.exception.FormatException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;


public interface TextStyles extends Styles
{
	/**
	 * The template prefix (mark the beginning of a place-holder)
	 * @return the template prefix
	 */
	public String getTemplatePrefix();
	
	/**
	 * The template suffix
	 * @return the template suffix
	 */
	public String getTemplateSuffix();
	
	/**
	 * 
	 * 
	 * 
	 * Inserts values from the object template into the Bind Str
	 * 
	 * For example if bindStr "Today is ${DAY}" and bindPara DAY="Monday"
	 * 
	 * results in a string "Today is Monday"
	 * 
	 * 
	 * 
	 * @param aBindText
	 * 
	 * @param obj values to insert into the bind text
	 * 
	 * @param aDateFormat i.e. MM/dd/yyyy
	 * @return the formatted output
	 * @throws FormatException when a format exception
	 */
	public abstract String format(String aBindText, Object obj,String aDateFormat)
	throws FormatException;

	/**
	 * 
	 * 
	 * 
	 * Inserts values from the object template into the Bind Str
	 * 
	 * For example if bindStr "Today is ${DAY}" and bindPara DAY="Monday"
	 * 
	 * results in a string "Today is Monday"
	 * 
	 * Default dateFormat MM/dd/yyyy
	 * 
	 * Functions &lt;#if x = 1&gt; x is 1 &lt;#elseif x = 2&gt; x is 2 &lt;#elseif x = 3&gt;
	 * 
	 * x is 3 &lt;#elseif x = 4&gt; x is 4 &lt;#else&gt; x is not 1 nor 2 nor 3 nor 4 &lt;/#if&gt;
	 * 
	 * &lt;#if Tithes_1?exists&gt; OK &lt;#else&gt; &lt;#assign Tithes_1= 0&gt; &lt;/#if&gt;
	 * 
	 * Date formating ${lastUpdated?string("yyyy-MM-dd HH:mm:ss zzzz")}
	 * 
	 * @param aBindText
	 * 
	 * @param aBindMap values to insert into the bind text
	 * @return the formated output
	 * @throws FormatException when an format issues
	 * 
	 * 
	 */

	public abstract String format(String aBindText, Object aBindMap)
			throws FormatException;

	/**
	 * write formatted output to the writer
	 * 
	 * @param bindObject the bind object
	 * @param text the template test
	 * @param dateFormat the date format
	 * @param writer
	 * @throws IOException when an IO exception
	 * @throws FormatException when an format exception
	 */
	public abstract void formatWriter(String text, Object bindObject,
			String dateFormat, Writer writer) throws IOException,
			FormatException;

	public abstract void formatWriterFromTemplateName(String templateName,
			Object aBindMap, Writer writer) throws IOException, FormatException;

	public abstract void formatWriter(File templateDir, String templateName,
			Object aBindMap, String dateFormat, Writer writer)
			throws IOException, FormatException;

}
