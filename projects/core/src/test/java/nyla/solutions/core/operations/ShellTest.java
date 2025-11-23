package nyla.solutions.core.operations;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.operations.Shell.ProcessInfo;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUNIT test for Shell operations
 * @author Gregory Green
 *
 */
public class ShellTest
{

	
	@Test
	public void testExecuteListOfString()
	throws Exception
	{
		Shell shell = new Shell();
		ProcessInfo pi = shell.execute(Arrays.asList("unknown"));
		assertNotNull(pi);
		System.out.println("pi:"+pi);
		assertEquals(-1,pi.exitValue);
		assertNotNull(pi.error);
		assertNull(pi.output);

		
		pi = shell.execute(Arrays.asList("java","-classpath",ClassPath.getClassPathText(),"nyla.solutions.core.operations.ShellTest"));
		assertNotNull(pi);
		assertTrue(pi.exitValue > -1);
		
		System.out.println("pi:"+pi);
		assertThat(pi.error).isEmpty();
		assertNotNull(pi.output);
	}

	@Test
	public void testExecuteBackground()
	{
		Shell shell = new Shell();
		boolean background = true;
		ProcessInfo pi = shell.execute(background, "javap","java.lang.String");
		assertNotNull(pi);
		
		assertNotNull(pi.output);
		assertThat(pi.error).isEmpty();
		
		
		pi = shell.execute(background, "unknown");
		assertNotNull(pi);
		
		assertNull(pi.output);
		assertNotNull(pi.error);
		
	}

	@Test
	public void testExecuteStringArray()
	throws IOException
	{
		var file = new File("target/file.log");
		if(file.delete())
		{
			System.out.println("Delete file");
		}
		
		var shell = new Shell("target",file);
		
		ProcessInfo pi = shell.execute(Arrays.asList("java","-classpath",ClassPath.getClassPathText(),"nyla.solutions.core.operations.ShellTest"));

        System.out.println("ERROR:"+pi.error);
        System.out.println("OUTPUT:"+pi.output);

		assertTrue(file.exists());
		
		assertTrue(pi.exitValue ==0);
		assertThat(pi.output).isEmpty();
		
		assertTrue(IO.reader().readTextFile(file.toPath()).contains("TEST"));
	}
	
	@Test
	public void testEnvironment() throws Exception
	{
		Shell shell = new Shell();
		shell.setEnvProperty("TEST","hello");
		assertEquals("hello",shell.getEnvProperty("TEST"));
		
			
		ProcessInfo pi = shell.execute(Arrays.asList("java","-classpath",ClassPath.getClassPathText(),"nyla.solutions.core.operations.ShellTest"));
		assertTrue(pi.exitValue ==0);
		assertTrue(pi.output.contains("hello"));
		
	}
	
	public static void main(String[] args)
	{
		System.out.println("TEST:"+System.getenv("TEST"));
	}
	

}
