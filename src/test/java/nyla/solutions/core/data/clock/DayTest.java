package nyla.solutions.core.data.clock;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import org.junit.jupiter.api.Test;

import nyla.solutions.core.util.Text;

public class DayTest
{

	@Test
	public void testToday()
	{
		assertNotNull(Day.today());
	}
	
	
	/**
	 * Test to day 
	 */
	@Test
	public void testToDay()
	{
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 14);
			cal.set(Calendar.MONTH, Calendar.DECEMBER);
			cal.set(Calendar.YEAR, 1975);
			
			System.out.println(cal.getTime());
			
			Day day = new Day(cal);
			
			Day compared = new Day("12/14/1975");
			
			
			
			assertEquals(compared.getDayOfMonth(),14);
			assertEquals(compared.getMonthNo(),12);
			assertEquals(compared.getYear(),1975);
			assertEquals(day,compared);
			
			assertTrue(day.isSameDay(compared));
			
			assertTrue(!day.isSameDay(Day.today()));
			
			 System.out.println("day:"+day.getDate());
			assertEquals("12/14/1975",Text.formatDate("MM/dd/yyyy", day.getDate()));
			
			
		
	}

}
