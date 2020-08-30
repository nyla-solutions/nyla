package nyla.solutions.core.data.clock;

import nyla.solutions.core.util.Scheduler;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

}
