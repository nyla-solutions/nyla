package nyla.solutions.core.util.settings;

import nyla.solutions.core.util.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Gregory Green
 *
 */
public class ArgsParser
{
	public static Map<Object,Object> parse(String[] args)
	{
		if(args == null || args.length ==0)
			return null;
		
		return parse(Arrays.asList(args));
	}//------------------------------------------------

	public static Map<Object,Object> parse(List<String>   args)
	{
		final short START =1, VAL=2;
		short state = START;
		Map<Object,Object> map = new HashMap<>(args.size());
		
		String key=null,value=null;
		for (String arg : args)
		{
			arg = arg.trim();
			
			switch(state)
			{
				case START: 
					
					if(arg.startsWith("-"))
						arg = Text.editor().replaceForRegExprWith(arg, "^-*", "");
					
					if(arg.contains("="))
					{
						String[] tokens = Text.splitRE(arg, "=");
						if(tokens.length == 2)
						{
							key = tokens[0];
							value = tokens[1];
							
							map.put(key, formatValue(value));
							
							state = START;
						}
					}
					else
					{
						key = arg;
						state = VAL;
					}
					
				break;
				case VAL: 
					
					map.put(key, formatValue(arg));
					state = START;
				break;
			 	default: throw new RuntimeException("Unknown message:");
			}
		}
		
		
		return map;
	}//------------------------------------------------

	protected static String formatValue(String value)
	{
		if(value == null)
			return "";
		
		value = value.trim();
		
		if(value.startsWith("\"")|| value.startsWith("'"))
			value = value.substring(1);
		
		if(value.endsWith("\"")|| value.endsWith("'"))
			value = value.substring(0, value.length() -1);
		
		return value;
	}
}
