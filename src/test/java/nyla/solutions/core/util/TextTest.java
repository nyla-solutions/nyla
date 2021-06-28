package nyla.solutions.core.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Text
 * @author Gregory Green
 *
 */
public class TextTest 
{

	@Test
	void parseRE()
	{
		String startRE = "HereSTART";
		String endRE = "END";
		String input = "HereSTART150.19END";
		String actual = Text.parseRE(input, startRE, endRE);
		assertEquals("150.19",actual);
	}

	@Test
	void encodeBase64()
	{
		String input = "hi:mom";
		String expected = "aGk6bW9t";

		assertEquals(expected,Text.encodeBase64(input));
	}

	@Test
	void toProperCase()
	{
		assertEquals("Nyla",Text.toProperCase("nyla"));
		assertEquals("Nyla",Text.toProperCase("nylA"));
		assertEquals("Nyla",Text.toProperCase("nyLa"));
		assertEquals("Nyla",Text.toProperCase("nylA".toUpperCase()));
	}



	@Test
	public void parse()
	{
		assertEquals(
				Collections.singletonList(""),
				Text.parse("","",""));
		assertEquals(
				Collections.singletonList("hello"),
				Text.parse("starthelloend","start","end"));


		assertEquals(
				Collections.singletonList("0123"),
				Text.parse("0123","",""));

	}

	@Test
	public void build()
	{
		assertEquals("",
				Text.build());

		assertEquals("Hello World",
				Text.build("Hello"," World"));
	}
	
	@Test
	public void generateAlphabeticId()
	{
		String text = Text.generateAlphabeticId(2);
		assertEquals(2,text.length());

		text = Text.generateAlphabeticId(10);
		assertEquals(10,text.length());
	}
	
	@Test
	public void formatTextFromClassPath() throws Exception
	{
		Map<String, String> map = new HashMap<>();
		String path = "invalid";
		try
		{
			Text.formatTextFromClassPath(path, map);
			fail();
		}
		catch(IOException ignored)
		{
		}
		String results;

		path = "templates/test.txt";
		results = Text.formatTextFromClassPath(path,map);
		assertNotNull(results);
		
	}//------------------------------------------------
	
	@Test
	public void splitRE_Integer()
	{
		assertNull(Text.splitRE(null,",",Integer.class));
		
		String line = "1,2,3";
		
		Integer[] ints = Text.splitRE(line, ",",Integer.class);
		
		assertNotNull(ints);
		
		assertEquals(ints.length,3);
		
		assertEquals(Integer.valueOf(1),ints[0]);
		
	}
	@Test
	public void replaceForRegExprWith()
	{
		//text, re, replaceText)(
		assertEquals("SECURITY_USERNAME",Text.replaceForRegExprWith("security-username", "-","_").toUpperCase());
		
		assertEquals("SECURITY_USERNAME",Text.replaceForRegExprWith("security.username", "[-\\.]","_").toUpperCase());
		assertEquals("SECURITY_USER_NAME",Text.replaceForRegExprWith("security.user-name", "[-\\.]","_").toUpperCase());
		
	}//------------------------------------------------
	@Test
	public void merge()
	{
		assertEquals("1,2",Text.merge(",",1,2));
		assertEquals("1",Text.merge(",",1));
		assertNull(Text.merge(""));
		assertNull(Text.merge(null));
	}//------------------------------------------------
	@Test
	public void loadTemplate()
	throws Exception
	{
		assertNotNull(Text.loadTemplate("test"));		
		
	}//------------------------------------------------

	@Test
	void toText()
	{
		assertEquals("",Text.toText(null, null));
		String separator = "|";

		String a = "a";
		String b = "b";
		String expected=  a+separator+b;
		String actual = Text.toText(Arrays.asList(a,b), separator);
		assertEquals(expected,actual);
	}
	@Test
	void toText_noSeparate()
	{
		String a = "a";
		String b = "b";
		String expected=  a+b;
		String actual = Text.toText(Arrays.asList(a,b), null);
		assertEquals(expected,actual);
	}


	@Test
	public void formatText_matches_parse()
	{
		
		//Format text replacing place-holders prefixed with ${ and suffixed by } 
	    //with the corresponding values in a map.
		String text = "${company} A2D2 Solution Global Application Testings";
		Map<String,String> map = new HashMap<>();
	    map.put("company", "EMC");
	    text = Text.formatText(text,map);
	    assertEquals("EMC A2D2 Solution Global Application Testings", text);
	    
	    
	    //Use complex boolean logic regular expressions by adding ${AND}, ${NOT} and $OR} tags
		assertTrue(Text.matches("Nubian", ".*"));
		assertTrue(Text.matches("Kenya Africa", ".*Kenya.*"));
		assertFalse(Text.matches("Kenya", "${NOT}.*Kenya.*"));
		assertTrue(Text.matches("Kenya", "${NOT}${NOT}.*Kenya.*"));
		assertFalse(Text.matches("America, Kenya, Paris", ".*Paris.*${AND}.${NOT}.*Kenya.*"));
		assertFalse(Text.matches("America, Kenya, Paris", "(.*Paris.*${AND}.${NOT}.*Kenya.*)${OR}(.*Paris.*${AND}.${NOT}.*Kenya.*)"));
		assertTrue(Text.matches("United States, Kenya, France", "${NOT}.*America.*${AND}.*Kenya.${NOT}.*Paris.*"));
		assertTrue(Text.matches("United States, Kenya, France", "${NOT}.*America.*${AND}.*Kenya.${NOT}.*Paris.*"));

		//Use the parse method to retrieve one or more token between a start and end strings
		//Note the parse method can be used to none regular expressions
		String start = "Color:";
		String end = ";";
		Collection<String> collection = Text.parse("Color:green; Weight:155oz; Color:Blue; Weight:23oz", start, end);
		assertEquals(2,collection.size()); //two color
        Iterator<String> i  = collection.iterator();
        assertEquals("green", i.next()); //first is green
        assertEquals("Blue", i.next()); //second is Blue
		
        
		//There methods to the count of a given character
		int count = Text.characterCount('A', "All Apples");
		assertEquals(2, count);
		
		//There are methods get digit counts
		count = Text.digitCount(text);
		assertEquals(2, count);
		
		//Format text numbers/decimals
		String format = "#,###,###.###";
		String formattedText = Text.formatNumber(123443243240.033423,format);
		assertEquals("123,443,243,240.033",formattedText);
		
		//Format text currency
	    formattedText = Text.formatCurrency("1000.33");
	    assertEquals("$1,000.33",formattedText);
	    
	    //format text percentages
	    formattedText = Text.formatPercent("2.3");
	    assertEquals("2.3%",formattedText);
	    
	    //Use grep to search for expression across multiple lines in a string
	    text = "Greg on line 1\nGreen on line two";
	    String results = Text.grepText("Green", text);
		assertEquals("Green on line two",results);
	   

	}// --------------------------------------------------------
	@Test
	public void matches()
	{
		assertFalse(Text.matches(null, "${NOT}.*America.*${AND}.*Kenya.${NOT}.*Paris.*"));
		assertTrue(Text.matches(null, null));

	}//------------------------------------------------
	@Test
	public void toLocalDateTime()
	{
	
		LocalDateTime results = Text.toLocalDateTime("03/10/2013 00:00:00:00 AM");
		Debugger.println("results:"+results);
		
		assertNotNull(results);
	}//------------------------------------------------
	
	@Test
	public void toLocalDate()
	{
		assertNotNull(Text.toLocalDate("03/10/2013",null));
		assertNotNull(Text.toLocalDate("03/10/2013",""));
		assertNotNull(Text.toLocalDate("12/10/2013",""));
		assertNull(Text.toLocalDate("","M/dd/yyyy"));
		assertNull(Text.toLocalDate(null,"M/dd/yyyy"));
		
		LocalDate results = Text.toLocalDate("03/10/2013","M/dd/yyyy");
		Debugger.println("results:"+results);
		
		assertNotNull(results);
	}//------------------------------------------------
	@Test
	public void formatDate_LocalDateTime()
	{
		String results = Text.formatDate(LocalDateTime.now());
		Debugger.println("results:"+results);
		
		assertNotNull(results);
	}//------------------------------------------------
	@Test
	public void formatDate_LocalDate()
	{
		String results = Text.formatDate(LocalDate.now());
		Debugger.println("results:"+results);
		
		assertNotNull(results);
	}//------------------------------------------------

	@Test
	public void fixedLength_num_nm()
	{
		assertEquals("001",Text.fixedLength(1, 3));
		
		assertEquals("04000",Text.fixedLength(4000, 5));
	}
	@Test
	public void fixedLength()
	{
		assertEquals("YO ",Text.fixedLength("YO", 3,' '));
		
		assertEquals("YO-",Text.fixedLength("YO", 3,'-'));
		
		assertEquals("YO---",Text.fixedLength("YO", 5,'-'));
		
		assertEquals("   ",Text.fixedLength("", 3,' '));
	}

}
