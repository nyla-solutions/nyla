package nyla.solutions.global.patterns.command;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import nyla.solutions.commas.ProxyCommand;

public class ProxyCommandTest extends TestCase
{

	@Before
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	@After
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	@Test
	public void testExecute()
	{
		ProxyCommand cmd = new ProxyCommand();
		cmd.setTarget(this);
		
		cmd.setMethodName("tcase1");
		
		Object[] arg1 = {Boolean.valueOf(true), "HELLOW"};
		
		assertTrue(!tcase1Executed);
		Object r1 = cmd.execute(arg1);		
		assertTrue(tcase1Executed);		
		assertNull(r1);

		
		cmd.setMethodName("tcase2");
		assertTrue(!tcase2Executed);
		
		Object arg2 = new ArrayList<Object>();
		Object r2 = cmd.execute(arg2);
		assertNotNull(r2);
		assertTrue("OUT="+r2,"OUT".equals(r2));
		
	}// ------------------------------------------------

	@Test
	public void tcase1(Boolean in1, String in2)
	{
		tcase1Executed = true;
	}// ------------------------------------------------
	@Test
	public Object  tcase2(ArrayList<Object> in)
	{
		tcase2Executed = true;
		return "OUT";
	}// ------------------------------------------------
	private boolean tcase1Executed = false;
	private boolean tcase2Executed = false;
}
