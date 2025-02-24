package nyla.solutions.office.organizer.scheduler;

import java.util.Collection;
import java.util.Date;

import nyla.solutions.core.data.clock.Appointment;
import nyla.solutions.core.data.Identifier;
import nyla.solutions.core.exception.DuplicateRowException;
import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.patterns.Disposable;



/**
 * Data access interface schedule
 * @author Gregory Green
 *
 */
public interface SchedulerDAO extends Disposable
{
   Collection<Appointment> selectEvents(Identifier owner, Date date)
   throws NoDataFoundException;
   
   void insertEvent(Identifier owner, Appointment event)
   throws DuplicateRowException;
   
   void deleteEvent(Identifier owner, Appointment event)
   throws NoDataFoundException;
   
}
