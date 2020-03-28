package nyla.solutions.core.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import nyla.solutions.core.patterns.observer.SubjectObserver;
import nyla.solutions.core.util.Text;

public class FileMonitorTest extends FileMonitor
{

	static boolean called = false;
	@Test
	public void testMonitor()
	throws Exception
	{
		//Create the file monitoring
		FileMonitor fileMonitor = new FileMonitor();
		
		//Create an observer to be called for a file appears
		//Observer printFileObserver = new Observer()
		SubjectObserver<FileEvent> printFileObserver = new SubjectObserver<FileEvent>()
		{
			@Override
			public void update(String topic, FileEvent arg)
			{
				//Each argment is an instance of FileEvent
				FileEvent fileEvent = (FileEvent)arg;
				
				//Note that file with assert the file is nto changing (ex: FTP files)
				File file = fileEvent.getFile();
				
				//PRint file
				System.out.println("file:"+file);
				
				//Set flag for test case
				called = true;
			}
		};
		
		//Register the observer with the file monitor
		//fileMonitor.addObserver(printFileObserver);
		fileMonitor.add(printFileObserver);
		
		//Set the file, pattern and whether current files should trigger event
		fileMonitor.monitor("runtime/", "*.txt", false);
		
		//Write file to trigger observer
		String filePath = "runtime/"+Text.generateId()+".txt"; 
		IO.writeFile(filePath, "Hello");
		Thread.sleep(1000*2);
		
		//Check if observer was trigger
		assertTrue(called);
		
		//Cleanup file
		IO.delete(Paths.get(filePath).toFile());
	}
	
	
	@Test
	public void testProcessPreviousFile()
	throws Exception
	{
		//Create the file monitoring
		FileMonitor monitor = new FileMonitor();
	
		boolean processCurrentFiles = false;
		ArrayList<Boolean> hasEvent  = new ArrayList<Boolean>();
		
		SubjectObserver<FileEvent> observer = (observable, arg) -> {hasEvent.add(Boolean.TRUE);};
		//monitor.addObserver(observer);
		monitor.add(observer);
		
		monitor.monitor("runtime", "*.txt", processCurrentFiles);
		Thread.sleep(1000*2);
		assertTrue(hasEvent.isEmpty());
		IO.touch(Paths.get("runtime/FileMonitor.txt").toFile());
		Thread.sleep(1000*2);
		
		assertFalse(hasEvent.isEmpty());
		
	}

}
