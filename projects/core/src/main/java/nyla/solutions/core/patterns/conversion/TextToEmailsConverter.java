package nyla.solutions.core.patterns.conversion;

import java.util.HashSet;
import java.util.Set;

/**
 * Get the emails from the given text
 * @author Gregory Green
 *
 */
public class TextToEmailsConverter implements Converter<String, Set<String>>
{
	/**
	 * EMAIL_REG_EXP ="([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,25})$"
	 */
	public static final String EMAIL_REG_EXP ="([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,25})$";
	
	@Override
	public Set<String> convert(String text)
	{
		if(text == null)
			return null;
		
		//find @
		int startIndex = 0;
		int indexOfAtt = text.indexOf("@",startIndex);
		
		if(indexOfAtt < 0)
			return null;
		
		int len = text.length();
		
	    HashSet<String> emails = new HashSet<String>();
		do
		{
			//loop back to last
			StringBuilder prefix = new StringBuilder();
			
			for(int i = indexOfAtt-1;i > -1;i--)
			{
				char c = text.charAt(i);
				if(!Character.isDigit(c)&& !Character.isAlphabetic(c) && (c != '-' && c != '_' && c != '.'))
					break;
				
				prefix.append(c);				
			}
			
			StringBuilder end = new StringBuilder();
			for(int i = indexOfAtt+1;i< len;i++)
			{
				char c = text.charAt(i);
				if(!Character.isDigit(c)&& !Character.isAlphabetic(c) && (c != '-' && c != '_' && c != '.'))
					break;
				
				end.append(c);				
			}
			
			if((prefix.length() > 0 || end.length() > 0) && end.indexOf(".") > -1)
			{
				emails.add(prefix.reverse().append("@").append(end).toString());
			}
			
			startIndex = startIndex+ indexOfAtt;
			indexOfAtt = text.indexOf("@",startIndex);
			
		}while(indexOfAtt > -1);
		
		if(emails.isEmpty())
			return null;
		
		return emails;
	}//------------------------------------------------
}
