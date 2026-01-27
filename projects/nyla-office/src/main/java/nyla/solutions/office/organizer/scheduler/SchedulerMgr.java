package nyla.solutions.office.organizer.scheduler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import nyla.solutions.core.data.clock.Appointment;
import nyla.solutions.core.data.clock.Time;
import nyla.solutions.core.data.clock.TimeInterval;
import nyla.solutions.core.data.clock.TimeSlot;
import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.creational.servicefactory.ServiceFactory;
import nyla.solutions.core.security.user.data.User;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Scheduler;



public class SchedulerMgr implements SchedulerService
{

   public TimeInterval[] listAvailableSlots(User user, Date date)
   {
      try
      {       
         
         TimeInterval[] takenSlots = listEventTimeSlots(user, date);
         
                  
         Collection<TimeSlot> availableSlots = Scheduler.calculateAvailableSots(
        		 Arrays.asList(takenSlots),date, this.intervalSeconds,this.getStartTime(), getEndTime());
         
         TimeInterval[] timeSlots = new TimeInterval[availableSlots.size()];
         
         System.arraycopy(availableSlots.toArray(), 0, timeSlots, 0, timeSlots.length);
         
         return timeSlots;
      }
      catch (Exception e)
      {
         throw new SystemException(e.getMessage(),e);
      }
   }
   public TimeInterval[] listEventTimeSlots(User user, Date date)
   throws NoDataFoundException
   {    
      
      SchedulerDAO dao = null;
      try
      {
         dao = getSchedulerDAO(user);

         Appointment[] events = listEvents(user, date);
         
         TimeInterval[] eventTimeSlot = new TimeInterval[events.length];
         
         for (int i =0; i < events.length;i++)
         {
            eventTimeSlot[i] = events[i].getTimeSlot();
         }
         
         return eventTimeSlot;
      }
      catch (Exception e)
      {
         throw new SystemException(Debugger.stackTrace(e));
      }
      finally
      {
         if (dao != null)
            dao.dispose();
      }
   }
   /**
    * 
    * @param user the user
    * @return instance of the scheduler DAO
    */
   public static SchedulerDAO getSchedulerDAO(User user)
   {
      return (SchedulerDAO) ServiceFactory.getInstance().create(SchedulerDAO.class);
   }

   public Time getStartTime()
   {
     Time time = new Time();
     
     time.setHour24(this.startHourOfDay);
//     time.setMinutes(this.startMinute);
     
     return time;
   }
   public Time getEndTime()
   {
     Time time = new Time();
     
     time.setHour24(this.endHourOfDay);
//     time.setMinutes(this.endMinute);
     
     return time;
   }
   public Appointment[] listEvents(User user, Date date)
   {
      Debugger.dump(user);
      
      SchedulerDAO dao = null;
      try
      {
         dao = getSchedulerDAO(user);

         Collection<Appointment> events = dao.selectEvents(user, date);
         
         Appointment [] eventArray = new Appointment[events.size()];
         events.toArray(eventArray);
         
         return eventArray;
      }
      catch (NoDataFoundException e)
      {
         Debugger.println(this, "No events found for owner="+user+" date="+date);
         return null;
      }
      catch (Exception e)
      {
         throw new SystemException(e.getMessage(),e);
      }
      finally
      {
         if (dao != null)
        	 dao.dispose();
      }
   }
   public Appointment saveEvent(User user, Appointment event)
   {
      SchedulerDAO dao = null;
      try
      {
         dao = getSchedulerDAO(user);
         dao.insertEvent(user, event);
         
         return event;

      }
      catch (Exception e)
      {
         throw new SystemException(Debugger.stackTrace(e));
      }
      finally
      {
         if (dao != null)
            dao.dispose();
      }
   }

   private int startHourOfDay = 9;  //9 AM
   private int startMinute = 0;
   private int endHourOfDay = 17; //5 PM
   private int endMinute = 0;
   private int intervalSeconds = 60 * 60; //1 per hour


}
