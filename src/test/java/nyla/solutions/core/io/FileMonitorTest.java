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

import static org.junit.jupiter.api.Assertions.*;

public class FileMonitorTest
{
	//Create the file monitoring

	private long expectedPollingInterval =10;
	private long expectedDelay =0;
	static boolean called = false;

	@Test
	public void testProcessPreviousFile()
	throws Exception
	{
		FileMonitor subject = new FileMonitor(expectedPollingInterval,expectedDelay);

		boolean processCurrentFiles = false;
		ArrayList<Boolean> hasEvent  = new ArrayList<Boolean>();

		SubjectObserver<FileEvent> observer = (observable, arg) -> {hasEvent.add(Boolean.TRUE);};
		//monitor.addObserver(observer);
		subject.add(observer);

		subject.monitor("runtime", "*.txt", processCurrentFiles);
		Thread.sleep(1000*2);
		assertTrue(hasEvent.isEmpty());
		IO.touch(Paths.get("runtime/FileMonitor.txt").toFile());
		Thread.sleep(1000*2);

		assertFalse(hasEvent.isEmpty());

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
		FileMonitor subject = new FileMonitor(expectedPollingInterval,expectedDelay);
		assertNotNull(subject);

		assertEquals(expectedDelay,subject.getDelayMs());
		assertEquals(expectedPollingInterval,subject.getPollingInterval());
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
							Thread.sleep(sleepTimeMs);
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

		Thread.sleep(1000);
	}

}
