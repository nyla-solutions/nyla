package nyla.solutions.core.util;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Time;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class SchedulerTest
{

	@Test
	public void testIsDateOrTime()
	{
		assertFalse(Scheduler.isDateOrTime(String.class));
		assertTrue(Scheduler.isDateOrTime(Time.class));
		assertTrue(Scheduler.isDateOrTime(Date.class));
		assertTrue(Scheduler.isDateOrTime(java.sql.Date.class));
	}

}
