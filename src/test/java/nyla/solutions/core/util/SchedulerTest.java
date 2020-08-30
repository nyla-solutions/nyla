package nyla.solutions.core.util;

import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
