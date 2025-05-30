package nyla.solutions.core.data.clock;

import nyla.solutions.core.util.Scheduler;
import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTest extends Appointment
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4251380207362258208L;

	@Test
	public void test_twoAppointmentCompare()
	{
		Appointment a1 = new Appointment();
		a1.setTimeSlot(new TimeSlot(LocalDateTime.now(), LocalDateTime.now()));
		
		Appointment a2 = new Appointment();
		a2.setTimeSlot(new TimeSlot(Scheduler.tomorrow(), Scheduler.tomorrow()));
		
		assertTrue(a1.compareTo(a2) < 0);
		
		
	}


	@Test
	void given_nameHourDurations_WhenGetEvent_ThenValueMatch()
	{
		String  name = "test";
		int startHour24 = 16;
		int durationSeconds = 24;

		Appointment subject = Appointment.getEvent(name,startHour24,durationSeconds);
		assertEquals(name,subject.getName());
		LocalDateTime reminderDate = subject.getTimeSlot().getStartDate();
		assertNotNull(reminderDate);
		assertEquals(String.valueOf(startHour24), Text.formatDate(reminderDate,"HH"));
	}
}
