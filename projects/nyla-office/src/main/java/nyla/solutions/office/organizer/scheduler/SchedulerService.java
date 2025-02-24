package nyla.solutions.office.organizer.scheduler;

import java.util.Date;

import nyla.solutions.core.data.clock.Appointment;
import nyla.solutions.core.data.clock.TimeInterval;
import nyla.solutions.core.security.user.data.User;


public interface SchedulerService
{
   /**
    * 
    * @param user the users' schedule
    * @param time the day request
    * @return the list of time slots
    */
   public TimeInterval[] listAvailableSlots(User user, Date time);
   
   /**
    * 
    * @param user
    * @param time
    * @return the list of event appointments
    */
   public Appointment[] listEvents(User user, Date time);
   
   /**
    * 
    * @param user
    * @param event
    * @return the saved appointment event
    */
   public Appointment saveEvent(User user, Appointment event);
}
