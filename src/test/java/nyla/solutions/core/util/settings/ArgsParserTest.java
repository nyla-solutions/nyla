package nyla.solutions.core.util.settings;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ArgsParserTest
{

	@Test
	public void testParse()
	{
		String [] args = {"--spring.output.ansi.enabled","always","--arg1=1","--arg2","2","-arg3=3","-arg4","4","arg5=5","-arg6","\"6\"","-arg7","' 7 '"} ;
		
	
		Map<Object,Object> map = ArgsParser.parse(Arrays.asList(args));
		System.out.println("map:"+map);
		
		assertEquals("always",map.get("spring.output.ansi.enabled"));
		assertEquals("1",map.get("arg1"));
		assertEquals("2",map.get("arg2"));
		assertEquals("3",map.get("arg3"));
		assertEquals("4",map.get("arg4"));
		assertEquals("5",map.get("arg5"));
		assertEquals("6",map.get("arg6"));
		assertEquals(" 7 ",map.get("arg7"));
	}

}
