package nyla.solutions.core.data.clock;

import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Scheduler;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeSlotTest
{

	@Test
	public void testTimeSlot()
	{
		TimeSlot ts = new TimeSlot();
		
		long duration = ts.getDurationhours();
		assertTrue(duration <= 0);
	}
	
	@Test
	public void test_constructorWithArgs() throws Exception
	{
		TimeSlot ts = new TimeSlot(Scheduler.yesterday(), LocalDateTime.now());
		
		long duration = ts.getDurationhours();
		Debugger.println("duration:"+duration);
		assertEquals(duration , 24);
	}

}
