package nyla.solutions.core.util.settings;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Test for ArgsParser
 */
public class ArgsParserTest
{

	@Test
	void parse_When_ArgsEmpty_Then_Return_Null()
	{
		assertNull(ArgsParser.parse((String[])null));

		String [] args = {};
		assertNull(ArgsParser.parse(args));
	}

	@Test
	public void parse()
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

	@Test
	void formatValue_WhenValueEmpty_ThenReturnEmpty()
	{
		assertEquals("",ArgsParser.formatValue(""));
	}

	@Test
	void formatValue_WhenValueStartsWithDoubleQuote_ThenRemoveQuote()
	{
		assertEquals("test",ArgsParser.formatValue("\"test"));
	}

	@Test
	void formatValue_WhenValueStartsWithSingleQuote_ThenRemoveQuote()
	{
		assertEquals("test",ArgsParser.formatValue("\'test"));
	}


	@Test
	void formatValue_WhenValueEndsWithDoubleQuote_ThenRemoveQuote()
	{
		assertEquals("test",ArgsParser.formatValue("test\""));
	}

	@Test
	void formatValue_WhenValueEndsWithSingleQuote_ThenRemoveQuote()
	{
		assertEquals("test",ArgsParser.formatValue("test\'"));
	}

	@Test
	void formatValue_WhenValueHasSingleQuote_ThenRemoveQuote()
	{
		assertEquals("test",ArgsParser.formatValue("\'test\'"));
	}

	@Test
	void formatValue_WhenValueHasDoubleQuote_ThenRemoveQuote()
	{
		assertEquals("test",ArgsParser.formatValue("\"test\""));
	}

}
