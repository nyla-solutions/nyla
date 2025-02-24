package nyla.solutions.spring.patterns.scripting;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nyla.solutions.spring.scripting.SpringSpELScripting;

/**
 * Test cases for SpringSpELScripting
 * @author Gregory Green
 *
 */
public class SpringSpELScriptingTest
{
	public SpringSpELScriptingTest()
	{
	}//------------------------------------------------
	
	@Before
	public void setUp() throws Exception
	{
	}//------------------------------------------------
	@Test
	public void testIntepret()
	{
		
		SpringSpELScripting<String,Object> spelScripting = new SpringSpELScripting<String,Object>();
		//${T(solutions.global.util.Debugger).println('Hello World')}"
		String results = spelScripting.interpret(
		"new "+SpringSpELScriptingTest.class.getName()+"().getName()", null);
		
		Assert.assertEquals("SpringSpEL",results );
		
		
		 results = spelScripting.interpret(
					"T(java.util.Calendar).getInstance().getTime().getTime().toString()", null);
		 
		 Assert.assertTrue(results != null && !results.contains("java.util.Calendar"));
		 
		HashMap<String,String> variables = new HashMap<String,String>();
		variables.put("hello", "world");
		spelScripting.setVariables(variables);
		
		//get from SpringSpELScriptingTest object
		results = spelScripting.interpret(
				"name", new SpringSpELScriptingTest());
		
		Assert.assertEquals("SpringSpEL",results );
		
		//get from variable
		results = spelScripting.interpret(
				"get('hello')", null);
		
		Assert.assertEquals("world",results );
		
		results = spelScripting.interpret(
				"length().toString()", "world");
		
		Assert.assertEquals("5",results );
	}// --------------------------------------------------------
	
	
	public String getName()
	{
		return name;
	}


	private final static  String name ="SpringSpEL";

}
