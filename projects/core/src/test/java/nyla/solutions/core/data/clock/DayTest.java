package nyla.solutions.core.data.clock;

import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class DayTest
{

	private Day subject;
	private Calendar calendar = Calendar.getInstance();
	private int dayOfMonth = 14;

	@BeforeEach
	void setUp()
	{
		calendar = Calendar.getInstance();

		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.YEAR, 1975);

		subject = new Day(calendar);
	}

	@Test
	public void testToday()
	{
		assertNotNull(Day.today());
	}

	@Test
	void create_based_on_string() {
		assertThat(new Day("04/27/2020").toString()).isEqualTo("04/27/2020 Mon");
	}

	@Test
	void addDays()
	{
		int expected = 3;
		Day actual = subject.addDays(expected);
		assertEquals(expected+dayOfMonth,actual.getDayOfMonth());

	}

	@Test
	void addMonths()
	{
		int expected = 3;
		Day actual = subject.addMonths(expected);
		assertEquals(3,actual.getMonth());
	}



	@Test
	void addYears()
	{
		int expected = 3;
		Day actual = subject.addYears(expected);
		assertEquals(1978,actual.getYear());
	}

	@Test
	void testCompareTo()
	{
		assertEquals(0,subject.compareTo(new Day(calendar)));

		assertEquals(-1,subject.compareTo(new Day(calendar).addDays(1)));
		assertEquals(1,subject.compareTo(new Day(calendar).addDays(-1)));
	}

	@Test
	void daysBetween()
	{
		assertEquals(3,subject.daysBetween(subject.addDays(3)));
	}

	@Test
	void testEquals()
	{
		assertTrue(subject.equals(new Day(subject)));
		assertFalse(subject.equals(new Day(subject.addYears(-1))));
	}

	@Test
	void getDate()
	{
		assertNotNull(subject.getDate());
	}

	@Test
	void getDayName()
	{
		assertEquals("Sun",subject.getDayName());
	}

	@Test
	void getDayNumberOfWeek()
	{
		assertEquals(7,subject.getDayNumberOfWeek());
	}

	@Test
	void getDayOfMonth()
	{
		assertEquals(14,subject.getDayOfMonth());
	}

	@Test
	void getDayOfWeek()
	{
		assertEquals(DayOfWeek.SUNDAY,subject.getDayOfWeek());
	}

	@Test
	void getDayOfYear()
	{
		assertEquals(348,subject.getDayOfYear());
	}

	@Test
	void getDaysInMonth()
	{
		assertEquals(31,subject.getDaysInMonth());
	}

	@Test
	void getDaysInYear()
	{
		if(subject.isLeapYear())
			assertEquals(366,subject.getDaysInYear());
		else
			assertEquals(365,subject.getDaysInYear());
	}

	@Test
	void getFirstOfMonth()
	{
		int dayOfWeek = 2;
		int month = 12;
		int year = 1975;
		assertEquals(new Day(LocalDate.parse("1975-12-02")),
				subject.getFirstOfMonth(dayOfWeek,month,year));
	}

	@Test
	void getLastOfMonth()
	{
		int dayOfWeek = 2;
		int month = 12;
		int year = 1975;
		assertEquals(new Day(LocalDate.parse("1975-12-09")),
				subject.getLastOfMonth(dayOfWeek,month,year));

	}

	@Test
	void getLocalDate()
	{
		assertNotNull(subject.getLocalDate());
	}

	@Test
	void getMonth()
	{
		assertEquals(12,subject.getMonth());
	}

	@Test
	void getMonthNo()
	{
		assertEquals(12,subject.getMonthNo());
	}

	@Test
	void getNthOfMonth()
	{
		int dayOfWeek = 2;
		int month = 12;
		int year = 1975;
		Day subject = Day.getNthOfMonth(1,dayOfWeek,month,year);

		assertEquals(2,subject.getDayOfMonth());
	}

	@Test
	void getWeekOfYear()
	{
		assertEquals(50,subject.getWeekOfYear());
	}

	@Test
	void getYear()
	{
		assertEquals(1975,subject.getYear());
	}

	@Test
	void testHashCode()
	{
		assertEquals(subject.hashCode(),subject.hashCode());
		assertNotEquals(subject.addDays(1).hashCode(),subject.hashCode());
	}

	@Test
	void isAfter()
	{
		assertTrue(subject.isAfter(subject.addDays(-1)));
		assertFalse(subject.isAfter(subject.addDays(1)));
	}

	@Test
	void isBefore()
	{
		assertTrue(subject.isBefore(subject.addDays(1)));
		assertFalse(subject.isBefore(subject.addDays(-1)));
	}

	@Test
	void isLeapYear()
	{

		assertFalse(subject.isLeapYear());

	}


	@Test
	void isLeapYear_int()
	{

		assertTrue(subject.isLeapYear(2020));
	}

	@Test
	void isSameDay()
	{
		assertTrue(subject.isSameDay(subject));
		assertFalse(subject.isSameDay(subject.subtractDays(1)));
	}

	/**
	 * Test to day 
	 */
	@Test
	public void today()
	{
			Day compared = new Day("12/14/1975");

			assertEquals(compared.getDayOfMonth(),14);
			assertEquals(compared.getMonthNo(),12);
			assertEquals(compared.getYear(),1975);
			assertEquals(subject,compared);
			
			assertTrue(subject.isSameDay(compared));
			
			assertTrue(!subject.isSameDay(Day.today()));
			
			 System.out.println("day:"+subject.getDate());
			assertEquals("12/14/1975",Text.format().formatDate("MM/dd/yyyy", subject.getDate()));
	}

}
