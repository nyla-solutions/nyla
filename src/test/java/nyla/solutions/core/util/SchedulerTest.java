package nyla.solutions.core.util;

import nyla.solutions.core.data.clock.Day;
import nyla.solutions.core.data.clock.Time;
import nyla.solutions.core.data.clock.TimeInterval;
import nyla.solutions.core.data.clock.TimeSlot;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

	@Test
	public void calculateAvailableSots_throwsException()
	{
		TimeInterval timeInterval = new TimeSlot();

		Collection<TimeInterval> takenTimeSlots = Arrays.asList(timeInterval);
		Date date = new Date(System.currentTimeMillis());
		int intervalSeconds = 0;
		Time startTime = new Time();
		Time endTime = new Time();
		assertThrows(IllegalArgumentException.class, () ->
		Scheduler.calculateAvailableSots(takenTimeSlots,date,
				intervalSeconds,
				startTime,endTime));

	}
	@Test
	public void calculateAvailableSots()
	{
		TimeInterval timeInterval = new TimeSlot();
		Collection<TimeInterval> takenTimeSlots = Arrays.asList(timeInterval);
		Date date = new Date(System.currentTimeMillis());
		int intervalSeconds = 1;
		Time startTime = new Time();
		Time endTime = new Time();

		Collection<TimeSlot> actual =
				Scheduler.calculateAvailableSots(takenTimeSlots,date,
						intervalSeconds,
						startTime,endTime);

		assertNotNull(actual);
	}
	@Test
	public void calculateTimeSots()
	{
		Day day = Day.today();
		int intervalSeconds = 1;
		Time startTime = Time.now();
		Time endTime = Time.now();

		Scheduler.calculateTimeSots(day,intervalSeconds,startTime,endTime);
	}

	@Test
	void toTimestamp()
	{
		LocalDateTime expected = LocalDateTime.now();
		Timestamp actual = Scheduler.toTimestamp(expected);
		assertNotNull(actual);
		assertEquals(Scheduler.toEpocMilliseconds(expected),Scheduler.toEpocMilliseconds(actual));

		long epoc = Scheduler.toEpocMilliseconds(actual);
		assertEquals(actual,Scheduler.toTimestamp(epoc));


	}

	@Test
	void toEpochTimestamp()
	{
		LocalDateTime now = LocalDateTime.now();
		Timestamp t1 = Timestamp.valueOf(now);

		assertThat(Scheduler
				.toEpochTimestamp())
				.isLessThan(Timestamp
						.valueOf(LocalDateTime.now()).getTime());

	}

	@Test
	void toDate()
	{
		Date actual = Scheduler.toDate(LocalDateTime.now());
		assertNotNull(actual);
	}

	//	@Test
//	public void durationHours()
//	{}
//	@Test
//	public void durationMinutes()
//	{}
//	@Test
//	public void durationMS()
//	{}
//	@Test
//	public void durationSeconds()
//	{}
//	@Test
//	public void isDateOrTime()
//	{}
//	@Test
//	public void purgeSchedules()
//	{}
//	@Test
//	public void scheduleRecurring()
//	{}
//	@Test
//	public void toCalendar()
//	{}
//	@Test
//	public void toDateAddDaysSetOfWeek()
//	{}
//	@Test
//	public void toDateDayOfWeek()
//	{}
//	@Test
//	public void toLocalDate()
//	{}
//	@Test
//	public void toLocalDateTime()
//	{}
//
//
//	@Test
//	public void tomorrow()
//	{}
//	@Test
//	public void toTimerTask()
//	{}
//	@Test
//	public void yesterday()
//	{}

}
