package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.util.Text;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;


public class BasicTextStyles extends TextStylist
{

	@SuppressWarnings("unchecked")
	public String format(String aBindText, Object obj, String aDateFormat)
			throws FormatException
	{

		Map<Object, Object> map = (Map<Object, Object>) obj;
		if(map == null || map.isEmpty())
			return aBindText;

		//loop thru key
		Object key;
		String keyText;
		String prefix = this.getTemplatePrefix();
		String suffix = this.getTemplateSuffix();
		
		Object value = null;
		for(Map.Entry<Object,Object> entry : map.entrySet())
		{
			key = entry.getKey();
			
			value = entry.getValue();
			
			keyText = new StringBuilder().append(prefix).append(key).append(suffix).toString();
			
			if(value !=null)
				aBindText = Text.replace(keyText, Text.toString(value), aBindText);
			else
				aBindText = Text.replace(keyText, "", aBindText);
		}
		
		return aBindText;
	}// --------------------------------------------------------



	public void formatWriterFromTemplateName(String templateName,
			Object aBindMap, Writer writer) throws IOException, FormatException
	{
		// TODO Auto-generated method stub

	}// --------------------------------------------------------

	public void formatWriter(File templateDir, String templateName,
			Object aBindMap, String dateFormat, Writer writer)
			throws IOException, FormatException
	{
		// TODO Auto-generated method stub

	}// --------------------------------------------------------

}
