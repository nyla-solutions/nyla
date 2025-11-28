package nyla.solutions.core.util;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Text
 * @author Gregory Green
 *
 */
public class TextTest 
{

	private UserProfile userProfile;


	@BeforeEach
	void setUp() {
		userProfile = JavaBeanGeneratorCreator.of(UserProfile.class).create();
	}

	@Test
	void valueOfNull() {
		assertEquals(Text.valueOf(null,0), "0");
	}

	@Test
	void valueOfEmptyString() {
		assertEquals(Text.valueOf("",0), "0");
	}

	@Test
	void valueOfNullString() {
		assertEquals(Text.valueOf("null",0), "0");
	}

	@Test
	void valueOf() {
		assertEquals(Text.valueOf("hello",0), "hello");
	}

	@Test
	void transform() {
		assertThat(Text.format().transformTexts(String::toLowerCase,"IMANI")).isEqualTo(List.of("imani"));
	}

	@Test
	void toUpperCase() {
		assertThat(Text.format().toUpperCase(List.of("imani"))).isEqualTo(List.of("IMANI"));
	}

	@Test
	void generateId() {
		assertThat(Text.generator().generateId()).isNotBlank();
	}

	@Test
	void toStrings() {
		List<Object> list = List.of(1, 2);
		var actual = Text.format().toStrings(list);

		Iterable<String> expected = asList("1","2");
		assertThat(actual).containsAll(expected);

	}

	@Test
	void trim()
	{
		String expected = "select * from /SensorMeasurement where id = '2|DOOR' and value = 1";
		String input = "'''''''select * from /SensorMeasurement where id = '2|DOOR' and value = 1'''''''";
		String actual = Text.editor().trim(input,'\'');
		assertEquals(expected,actual);
	}
	@Test
	void trim_When_noMatchingChar_thenExpected()
	{
		String expected = "select * from /SensorMeasurement where id = '2|DOOR' and value = 1";
		String actual = Text.editor().trim(expected,'\'');
		assertEquals(expected,actual);
	}
	@Test
	void trim_when_Null_Then_Return_Empty()
	{
		assertEquals("",Text.editor().trim(null,'\''));
	}

	@Test
	void parserRE()
	{
		String startRE = "HereSTART";
		String endRE = "END";
		String input = "HereSTART150.19END";
		String actual = Text.parser().parseRE(input, startRE, endRE);
		assertEquals("150.19",actual);
	}

	@Test
	void encodeBase64()
	{
		String input = "hi:mom";
		String expected = "aGk6bW9t";

		assertEquals(expected,Text.format().encodeBase64(input));
	}

	@Test
	void toProperCase()
	{
		assertEquals("Nyla",Text.format().toProperCase("nyla"));
		assertEquals("Nyla",Text.format().toProperCase("nylA"));
		assertEquals("Nyla",Text.format().toProperCase("nyLa"));
		assertEquals("Nyla",Text.format().toProperCase("nylA".toUpperCase()));
	}



	@Test
	public void parser()
	{
		assertEquals(
				Collections.singletonList(""),
				Text.parser().parse("","",""));
		assertEquals(
				Collections.singletonList("hello"),
				Text.parser().parse("starthelloend","start","end"));


		assertEquals(
				Collections.singletonList("0123"),
				Text.parser().parse("0123","",""));

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
		String text = Text.generator().generateAlphabeticId(2);
		assertEquals(2,text.length());

		text = Text.generator().generateAlphabeticId(10);
		assertEquals(10,text.length());
	}
	
	@Test
	public void formatTextFromClassPath() throws Exception
	{
		Map<String, String> map = new HashMap<>();
		String path = "invalid";
		try
		{
			Text.format().formatTextFromClassPath(path, map);
			fail();
		}
		catch(IOException ignored)
		{
		}
		String results;

		path = "templates/test.txt";
		results = Text.format().formatTextFromClassPath(path,map);
		assertNotNull(results);
		
	}

	
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
		assertEquals("SECURITY_USERNAME",Text.editor().replaceForRegExprWith("security-username", "-","_").toUpperCase());
		assertEquals("SECURITY_USERNAME",Text.editor().replaceForRegExprWith("security.username", "[-\\.]","_").toUpperCase());
		assertEquals("SECURITY_USER_NAME",Text.editor().replaceForRegExprWith("security.user-name", "[-\\.]","_").toUpperCase());
		
	}
	@Test
	public void merge()
	{
		assertEquals("1,2",Text.merge(",",1,2));
		assertEquals("1",Text.merge(",",1));
		assertNull(Text.merge(""));
		assertNull(Text.merge(null));
	}
	@Test
	public void loadTemplate()
	throws Exception
	{
		assertNotNull(Text.format().loadTemplate("test"));
		
	}

	@Test
	void toText()
	{
		assertEquals("",Text.format().toText(null, null));
		String separator = "|";

		String a = "a";
		String b = "b";
		String expected=  a+separator+b;
		String actual = Text.format().toText(asList(a,b), separator);
		assertEquals(expected,actual);
	}
	@Test
	void toText_noSeparate()
	{
		String a = "a";
		String b = "b";
		String expected=  a+b;
		String actual = Text.format().toText(asList(a,b), null);
		assertEquals(expected,actual);
	}

	@Test
	public void formatTextWithArrayMap()
	{
		String name = "Imani";
		String day = "Monday";
		String month = "December";
		var text = """
								Hello ${name}
								How are you on ${day}
								How are you on ${month}
				""";
		var actual = Text.format().formatMap(text,"name",name,"day",day,"month",month);

		System.out.printf(actual);
		assertThat(actual).contains(name);
		assertThat(actual).contains(day);
		assertThat(actual).contains(month);

	}

	@Test
	void format_withObject() {


		var text = "Hi ${firstName}";

		var actual = Text.format().formatTemplate(text,userProfile);

		assertThat(actual).isEqualTo("Hi "+userProfile.getFirstName());

	}

	@Test
	public void formatText_matches_parser()
	{
		
		//Format text replacing place-holders prefixed with ${ and suffixed by } 
	    //with the corresponding values in a map.
		String text = "${company} A2D2 Solution Global Application Testings";
		Map<String,String> map = new HashMap<>();
	    map.put("company", "EMC");
	    text = Text.format().formatMap(text,map);
	    assertEquals("EMC A2D2 Solution Global Application Testings", text);
	    
	    
	    //Use complex boolean logic regular expressions by adding ${AND}, ${NOT} and $OR} tags
		assertTrue(Text.match().matches("Nubian", ".*"));
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
		Collection<String> collection = Text.parser().parse("Color:green; Weight:155oz; Color:Blue; Weight:23oz", start, end);
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
		String formattedText = Text.format().formatNumber(123443243240.033423,format);
		assertEquals("123,443,243,240.033",formattedText);
		
		//Format text currency
	    formattedText = Text.format().formatCurrency("1000.33");
	    assertEquals("$1,000.33",formattedText);
	    
	    //format text percentages
	    formattedText = Text.format().formatPercent("2.3");
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

	}
	@Test
	public void toLocalDateTime()
	{
	
		LocalDateTime results = Text.dates().toLocalDateTime("03/10/2013 00:00:00:00 AM");
		Debugger.println("results:"+results);
		
		assertNotNull(results);
	}
	
	@Test
	public void toLocalDate()
	{
		assertNotNull(Text.dates().toLocalDate("03/10/2013",null));
		assertNotNull(Text.dates().toLocalDate("03/10/2013",""));
		assertNotNull(Text.dates().toLocalDate("12/10/2013",""));
		assertNull(Text.dates().toLocalDate("","M/dd/yyyy"));
		assertNull(Text.dates().toLocalDate(null,"M/dd/yyyy"));
		
		LocalDate results = Text.dates().toLocalDate("03/10/2013","M/dd/yyyy");
		Debugger.println("results:"+results);
		
		assertNotNull(results);
	}
	@Test
	public void formatDate_LocalDateTime()
	{
		String results = Text.format().formatDate(LocalDateTime.now());
		Debugger.println("results:"+results);
		
		assertNotNull(results);
	}
	@Test
	public void formatDate_LocalDate()
	{
		String results = Text.format().formatDate(LocalDate.now());
		Debugger.println("results:"+results);
		
		assertNotNull(results);
	}

	@Test
	public void fixedLength_num_nm()
	{
		assertEquals("001",Text.generator().fixedLength(1, 3));
		
		assertEquals("04000",Text.generator().fixedLength(4000, 5));
	}
	@Test
	public void fixedLength()
	{
		assertEquals("YO ",Text.generator().fixedLength("YO", 3,' '));
		
		assertEquals("YO-",Text.generator().fixedLength("YO", 3,'-'));
		
		assertEquals("YO---",Text.generator().fixedLength("YO", 5,'-'));
		
		assertEquals("   ",Text.generator().fixedLength("", 3,' '));
	}

}
