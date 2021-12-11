package nyla.solutions.core.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PROXYTest
{

	@Test
	void findMethodByArguments() throws NoSuchMethodException
	{
		Class<?> targetClass = PROXYTest.class;
		String methodName = "verifyWityArgs";
		Object[] args = {"test"};
		Method actual = PROXY.findMethodByArguments(targetClass, methodName, args);

		assertNotNull(actual);
		assertEquals(methodName,actual.getName());
	}

	@Test
	void toParameterTypes_WhenArrayOfTypes_ThenReturnTypeMatch()
	{
		Object[] args = {Integer.valueOf(0),"test"};
		Class<?>[] actual = PROXY.toParameterTypes(args);

		assertNotNull(actual);
		assertEquals(2, actual.length);
		assertEquals(Integer.class.getName(), actual[0].getName());
		assertEquals(String.class.getName(), actual[1].getName());
	}

	@Test
	void toParameterTypes_WhenNotInstanceOfObjectArray_ThenClassArray()
	{
		String args = "";
		Class<?>[] actual = PROXY.toParameterTypes(args);

		assertNotNull(actual);
		assertEquals(1, actual.length);
		assertEquals(String.class.getName(), actual[0].getName());
	}


	@Test
	void toParameterTypes_WhenInstanceOfObjectArray_ThenClassArray()
	{
		Object[] args = {""};
		Class<?>[] actual = PROXY.toParameterTypes(args);

		assertNotNull(actual);
		assertEquals(1, actual.length);
	}

	@Test
	public void executeMethod()
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
