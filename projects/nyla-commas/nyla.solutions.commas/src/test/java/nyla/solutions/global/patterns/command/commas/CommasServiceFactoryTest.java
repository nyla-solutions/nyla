package nyla.solutions.global.patterns.command.commas;

import org.junit.Test;

import junit.framework.TestCase;
import nyla.solutions.commas.Command;
import nyla.solutions.commas.CommandFacts;
import nyla.solutions.commas.CommasServiceFactory;
import nyla.solutions.commas.json.JsonCommandSchema;
import nyla.solutions.core.util.Debugger;


public class CommasServiceFactoryTest extends TestCase
{

	public CommasServiceFactoryTest(String name)
	{
		super(name);
	}// --------------------------------------------------------

	/*TODO
	public void testMoreThanOneArgument() 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		String name = "solutions.global.patterns.command.commas.HelloWorldCommand.helloEnv";
		
		CommasServiceFactory factory = CommasServiceFactory.getCommasServiceFactory();
		Command<String,Object[]> cmd = factory.create(name);
		
		Object[] input = {"greg green", "hello"};
		String output = null;
		 output = cmd.execute(input);
		
	  	Assert.assertNotNull(output);
		
		HelloWorldCommand helloCmd = new HelloWorldCommand();
		
		Class<?> clz = helloCmd.getClass();
		
		Method m = clz.getMethods()[1];
		
		Object[] inputs = {new Envelope<Object>(),"test"};
		
		output = (String)m.invoke(helloCmd, inputs);
		
		Debugger.println(this,"m="+m+" outut="+output);
		
		Assert.assertNotNull(output);
		
	}*/
	// --------------------------------------------------------
	@Test
	public void testCreateClass()
	{
		CommasServiceFactory factory = CommasServiceFactory.getCommasServiceFactory();
		
		Command<?,?> cmd = (Command<?,?>)factory.create(cmdName);
		
		assertNotNull(cmd);
		
		//String results = (Envelope)cmd.execute("World");
		
		//Assert.assertEquals("World", results.getPayload());
	}// --------------------------------------------------------
	@Test
	public void testJson() throws Exception
	{
		CommasServiceFactory factory = CommasServiceFactory.getCommasServiceFactory();
		
		CommandFacts facts = factory.getCommandFacts(cmdName);
		
		JsonCommandSchema schema = new JsonCommandSchema(facts);
		
		Debugger.dump(schema);
	}

	private String cmdName = "nyla.solutions.global.patterns.command.commas.HelloWorldCommand.hello";
	
}
