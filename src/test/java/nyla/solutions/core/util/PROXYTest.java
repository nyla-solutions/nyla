package nyla.solutions.core.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PROXYTest
{
	@Test
	public void testExecuteMethod()
	throws Exception
	{
		Object response = PROXY.executeMethod(this, "verifyNoArgs", null);
		assertNull(response);
		assertTrue(verifyNoArgsCalled);
		
		response = PROXY.executeMethod(this, "verifyWityArgs", Arrays.asList("test").toArray());
		assertNull(response);
		assertTrue(verifyWityArgs);
		
		response = PROXY.executeMethod(this, "verifyWityArgsWithReturn", Arrays.asList("test").toArray());
		assertEquals("test", response);
		assertTrue(verifyWityArgsWithReturn);
	}
	void verifyNoArgs()
	{
		System.out.println("verifyNoArgs");
		verifyNoArgsCalled = true;
	}
	void verifyWityArgs(String text)
	{
		System.out.println("verifyWityArgs:"+text);
		verifyWityArgs = true;
	}
	String verifyWityArgsWithReturn(String text)
	{
		System.out.println("verifyWityArgsWithReturn:"+text);
		verifyWityArgsWithReturn = true;
		return text;
	}
	
	private static boolean verifyNoArgsCalled = false;
	private static boolean verifyWityArgs = false;
	private static boolean verifyWityArgsWithReturn = false;
}
