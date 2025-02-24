package nyla.solutions.core.io;

import nyla.solutions.core.patterns.observer.SubjectObserver;
import nyla.solutions.core.patterns.workthread.ThreadScheduler;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Text;
import nyla.solutions.core.util.settings.Settings;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for FileMonitor
 * @author gregory green
 */
public class FileMonitorTest
{
	//Create the file monitoring

	private long pollingIntervalMs =10;
	private long delayMs =0;
	static boolean called = false;

	@Test
	public void processPreviousFile()
	throws Exception
	{
		var expectedResults  = new ArrayList<Boolean>();


		var subject = new FileMonitor(pollingIntervalMs, delayMs);

		//The observers add a TRUE value to expectedResults when file monitored
		SubjectObserver<FileEvent> observer = (observable, arg) -> {

			System.out.println(" observable:"+observable+" arg:"+arg);
			expectedResults.add(Boolean.TRUE);
		};

		subject.add(observer);

		var processCurrentFiles = false; //Process current files
		subject.monitor("runtime", "*.txt", processCurrentFiles);

		sleep(1000*2);

		//Initially the results are empty
		assertTrue(expectedResults.isEmpty());

		//Update file
		IO.touch(Paths.get("runtime/FileMonitor.txt").toFile());
		sleep(1000*2);

		//Observer should have executed to add TRUE to
		assertFalse(expectedResults.isEmpty());

		//Clean up file
		IO.delete(Paths.get("runtime/FileMonitor.txt").toFile());

	}


	@Test
	public void test_delay_is_set()
	{

		long pollInterval =  100;
		long delay = 500;
		FileMonitor fileMonitor = new FileMonitor(pollInterval,delay);
		assertTrue(fileMonitor.getDelayMs() > 0);
	}

	@Test
	public void test_constructor()
	{
		FileMonitor subject = new FileMonitor(pollingIntervalMs, delayMs);
		assertNotNull(subject);

		assertEquals(delayMs,subject.getDelayMs());
		assertEquals(pollingIntervalMs,subject.getPollingInterval());
	}

	@Test
	public void test_deadLock()
	throws InterruptedException, IOException
	{
		File file = Paths.get("target/runtime/test.properties").toFile();
		Properties prop = new Properties();
		prop.setProperty("test",Text.generateId());
		prop.store(new FileWriter(file),null);

		System.setProperty(Config.SYS_PROPERTY,file.getAbsolutePath());

		SubjectObserver<Settings> o = new SubjectObserver<Settings>()
		{
			@Override
			public void update(String subjectName, Settings settings)
			{
				settings.registerObserver(this);
			}
		};

		Config.registerObserver(o);

		int retry = 10;
		long sleepTimeMs = 100;
		Runnable[] runnables =  {
				() -> {

					for(int i =0;  i< retry; i++)
					{
						try
						{
							System.out.println("touching");
							prop.setProperty("test",Text.generateId());
							prop.store(new FileWriter(file),null);
							System.out.println("touched");
							sleep(sleepTimeMs);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}

				}
		};

		ThreadScheduler threadScheduler = new ThreadScheduler();

		Collection<Thread> threads = threadScheduler.startThreads(runnables);

		threadScheduler.waitForThreads(threads);

		sleep(1000);
	}

}
