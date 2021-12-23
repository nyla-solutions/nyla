package nyla.solutions.core.util;

import nyla.solutions.core.data.clock.Day;
import nyla.solutions.core.data.clock.Time;
import nyla.solutions.core.data.clock.TimeInterval;
import nyla.solutions.core.data.clock.TimeSlot;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
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
                Scheduler.calculateAvailableSots(takenTimeSlots, date,
                        intervalSeconds,
                        startTime, endTime));

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
                Scheduler.calculateAvailableSots(takenTimeSlots, date,
                        intervalSeconds,
                        startTime, endTime);

        assertNotNull(actual);
    }

    @Test
    public void calculateTimeSots()
    {
        Day day = Day.today();
        int intervalSeconds = 1;
        Time startTime = Time.now();
        Time endTime = Time.now();

        Scheduler.calculateTimeSots(day, intervalSeconds, startTime, endTime);
    }

    @Test
    void dateNow()
    {
        assertNotNull(Scheduler.dateNow());
    }

    @Test
    void toTimestamp()
    {
        LocalDateTime expected = LocalDateTime.now();
        Timestamp actual = Scheduler.toTimestamp(expected);
        assertNotNull(actual);
        assertEquals(Scheduler.toEpocMilliseconds(expected), Scheduler.toEpocMilliseconds(actual));

        long epoc = Scheduler.toEpocMilliseconds(actual);
        assertEquals(actual.getTime(), Scheduler.toTimestamp(epoc).getTime());


    }

    @Test
    void toEpochTimestamp() throws InterruptedException
    {

        long actual = Scheduler
                .toEpochTimestamp();

        Thread.sleep(5);

        LocalDateTime now = LocalDateTime.now();
        Timestamp t1 = Timestamp.valueOf(now);

        assertThat(actual)
                .isLessThan(Timestamp
                        .valueOf(LocalDateTime.now()).getTime());

    }

    @Test
    void toDate()
    {
        Date actual = Scheduler.toDate(LocalDateTime.now());
        assertNotNull(actual);
    }

    @Test
    public void durationHours()
    {
        int year = 1940;
        int month = 12;
        int day = 4;
        int hour = 10;
        int minute = 0;
        long actual = Scheduler.durationHours(LocalDateTime.of(year,month,day,hour,minute),LocalDateTime.of(year,month,day,hour+1,minute));

        assertEquals(1,actual);

    }
	@Test
	public void durationMinutes()
	{
        int year = 1940;
        int month = 12;
        int day = 4;
        int hour = 10;
        int minute = 0;
        int expected = 20;
        double actual = Scheduler.durationMinutes(LocalDateTime.of(year,month,day,hour,minute),LocalDateTime.of(year,month,day,hour,minute+expected));

        assertEquals(expected,actual);

    }
	@Test
	public void durationMS()
	{
        int year = 1940;
        int month = 12;
        int day = 4;
        int hour = 10;
        int minute = 0;
        int expected = 13;
        int seconds = 12;
        int nanoseconds = 20;
        double actual = Scheduler.durationMS(
                LocalDateTime.of(year,month,day,hour,minute,seconds,nanoseconds),
                LocalDateTime.of(year,month,day,hour,minute,seconds+expected,nanoseconds));


        assertEquals(expected*1000,actual);

    }
	@Test
	public void durationSeconds()
	{
        int year = 1940;
        int month = 12;
        int day = 4;
        int hour = 10;
        int minute = 0;
        int expected = 13;
        int seconds = 12;
        int nanoseconds = 20;
        double actual = Scheduler.durationSeconds(
                LocalDateTime.of(year,month,day,hour,minute,seconds,nanoseconds),
                LocalDateTime.of(year,month,day,hour,minute,seconds+expected,nanoseconds));


        assertEquals(expected,actual);
    }
	@Test
	public void isDateOrTime()
	{
	    assertTrue(Scheduler.isDateOrTime(Date.class));
        assertTrue(Scheduler.isDateOrTime(java.sql.Date.class));
        assertTrue(Scheduler.isDateOrTime(java.sql.Timestamp.class));
        assertTrue(Scheduler.isDateOrTime(java.sql.Time.class));
        assertTrue(Scheduler.isDateOrTime(LocalDateTime.class));
        assertTrue(Scheduler.isDateOrTime(LocalDate.class));
        assertTrue(Scheduler.isDateOrTime(LocalTime.class));
        assertTrue(Scheduler.isDateOrTime(Calendar.class));
    }

	@Test
	public void toCalendar()
	{
	    long time =System.currentTimeMillis();

	    Calendar actual = Scheduler.toCalendar(new Date(time));
	    assertEquals(time,actual.getTime().getTime());
    }

	@Test
	public void toDateDayOfWeek()
	{

	    LocalDateTime actual = Scheduler.toLocalDateTime(Scheduler.toDateDayOfWeek(1));

	    assertEquals(actual.getDayOfWeek(),DayOfWeek.SUNDAY);

    }
	@Test
	public void toLocalDate()
	{
	    Date time = new Date();
        LocalDate actual = Scheduler.toLocalDate(time);

        assertNotNull(actual);
    }
	@Test
	public void toLocalDateTime()
	{
	    assertNotNull(Scheduler.toLocalDateTime(new Date()));
    }


	@Test
	public void tomorrow()
	{
	    assertNotNull(Scheduler.tomorrow());
    }

	@Test
	public void toTimerTask()
	{
	    assertNotNull(Scheduler.toTimerTask(()-> System.out.println()));
    }
	@Test
	public void yesterday()
	{
        assertNotNull(Scheduler.yesterday());
    }

}
