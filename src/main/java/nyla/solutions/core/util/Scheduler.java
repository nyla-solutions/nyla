package nyla.solutions.core.util;

import nyla.solutions.core.data.clock.Day;
import nyla.solutions.core.data.clock.Time;
import nyla.solutions.core.data.clock.TimeInterval;
import nyla.solutions.core.data.clock.TimeSlot;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;


/**
 * <pre>
 * Scheduler provides a set of functions to
 * perform date operations
 * </pre>
 *
 * @author Gregory Green
 * @version 1.0
 */
public class Scheduler
{
    public static final String DATE_FORMAT = "mm/dd/yyyy";
    private static final long ZERO = 0;
    private Timer timer = new Timer();

    /**
     * @param aDate the date
     * @return convert the date to a calendar
     */
    public static Calendar toCalendar(Date aDate)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(aDate);
        return calendar;
    }// --------------------------------------------

    /**
     * cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
     *
     * @param dayOfWeek
     * @return time date
     */
    public static Date toDateDayOfWeek(int dayOfWeek)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        return cal.getTime();
    }// ----------------------------------------------

    /**
     * The time between two dates
     *
     * @param start the begin time
     * @param end   the finish time
     * @return duration in milliseconds
     */
    public static long durationMS(LocalDateTime start, LocalDateTime end)
    {
        if (start == null || end == null) {
            return 0;
        }

        return Duration.between(start, end).toMillis();
    }//--------------------------------------------

    /**
     * 1 millisecond = 0.001 seconds
     *
     * @param start between time
     * @param end   finish time
     * @return duration in seconds
     */
    public static double durationSeconds(LocalDateTime start, LocalDateTime end)
    {
        return Duration.between(start, end).getSeconds();
    }//--------------------------------------------

    /**
     * 1 seconds = 1/60 minutes
     *
     * @param start between time
     * @param end   finish time
     * @return duration in minutes
     */
    public static double durationMinutes(LocalDateTime start, LocalDateTime end)
    {

        return Duration.between(start, end).toMinutes();
    }//--------------------------------------------

    /**
     * 1 Hours = 60 minutes
     *
     * @param start between time
     * @param end   finish time
     * @return duration in hours
     */
    public static long durationHours(LocalDateTime start, LocalDateTime end)
    {
        if (start == null || end == null)
            return ZERO;

        return Duration.between(start, end).toHours();
    }//--------------------------------------------

    public static Collection<TimeSlot> calculateAvailableSots(Collection<TimeInterval> takenTimeSlots, Date date,
                                                              int intervalSeconds, Time startTime, Time endTime)
    {
        Day day = new Day(date);

        Collection<TimeSlot> allTimeSlots = calculateTimeSots(day, intervalSeconds, startTime, endTime);

        if (takenTimeSlots == null || takenTimeSlots.isEmpty())
            return allTimeSlots;

        TimeSlot timeSlot = null;
        TreeSet<TimeSlot> availableSlots = new TreeSet<TimeSlot>();
        for (Iterator<TimeSlot> i = allTimeSlots.iterator(); i.hasNext(); ) {
            timeSlot = i.next();

            if (!takenTimeSlots.contains(timeSlot)) {
                availableSlots.add(timeSlot);
            }
        }
        return availableSlots;
    }// --------------------------------------------


    public static Collection<TimeSlot> calculateTimeSots(Day day, int intervalSeconds, Time startTime, Time endTime)
    {
        if (intervalSeconds < 1) {
            throw new IllegalArgumentException("interval seconds cannot be less than one");
        }

        int count = 1 * 60 * 60 * 24 / intervalSeconds;

        ArrayList<TimeSlot> slots = new ArrayList<TimeSlot>(count);

        TimeSlot startSlot = TimeSlot.firstSlot(day, startTime, intervalSeconds);
        TimeSlot slot = startSlot;

        while (slot != null) {
            slots.add(slot);
            slot = slot.nextTimeSlot(intervalSeconds, endTime);
        }

        return slots;
    }// --------------------------------------------

    public static LocalDateTime toLocalDateTime(Day day, Time time)
    {

        time.assignDate(LocalDateTime.of(day.getLocalDate(),
                time.getLocalDateTime().toLocalTime()));

        return time.getLocalDateTime();
    }// --------------------------------------------

    public static Timestamp toTimestamp(LocalDateTime localDateTime)
    {
        return Timestamp.valueOf(localDateTime);
    }

    public static long toEpocMilliseconds(LocalDateTime localDateTime)
    {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long toEpocMilliseconds(Timestamp timestamp)
    {
        return timestamp.getTime();
    }

    public static Timestamp toTimestamp(long epoc)
    {
        return new Timestamp(epoc);
    }

    public static long toEpochTimestamp()
    {
        LocalDateTime now = LocalDateTime.now();
        return Timestamp.valueOf(now).getTime();
    }

    /**
     * Convert timer task to a runnable
     *
     * @param runnable
     * @return timer task for the runnable
     */
    public static TimerTask toTimerTask(Runnable runnable)
    {
        if (runnable instanceof TimerTask)
            return (TimerTask) runnable;

        return new TimerTaskRunnerAdapter(runnable);
    }// ----------------------------------------------

    /**
     *
     * @return the current date
     */
    public static Date dateNow()
    {
        return new Date(System.currentTimeMillis());
    }

    /**
     * Wrapper to create a timer task from a runnable
     *
     * @author Gregory Green
     */
    private static class TimerTaskRunnerAdapter extends TimerTask
    {
        TimerTaskRunnerAdapter(Runnable runnable)
        {
            this.runnable = runnable;

        }// ----------------------------------------------

        /**
         * Runs the given runnable
         */
        public void run()
        {
            this.runnable.run();

        }

        private Runnable runnable = null;

    }// ----------------------------------------------

    public static boolean isDateOrTime(Class<?> clz)
    {
        if (clz == null)
            return false;
        String className = clz.getName();


        return className.matches("(java.time.Local.*|java.util.(Date|Calendar)|java.sql.(Date|Timestamp|Time|DateTime)|.*\\.Time)");
    }//------------------------------------------------

    public static LocalDateTime yesterday()
    {
        return LocalDateTime.now().minusDays(1);
    }//------------------------------------------------

    public static LocalDateTime toLocalDateTime(Date date)
    {
        if (date == null)
            return null;

        return Instant.ofEpochMilli(date.getTime())
                      .atZone(ZoneId.systemDefault())
                      .toLocalDateTime();
    }//------------------------------------------------

    public static Date toLocalDateTime(LocalDate date)
    {
        if (date == null)
            return null;

        return java.util.Date
                .from(date.atStartOfDay(ZoneId.systemDefault())
                          .toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime)
    {
        if (localDateTime == null)
            return null;

        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return new Date(zdt.toInstant().toEpochMilli());
    }//------------------------------------------------

    public static LocalDate toLocalDate(Date time)
    {
        return time.toInstant().atZone(ZoneId.systemDefault())
                   .toLocalDate();
    }//------------------------------------------------

    public static LocalDateTime tomorrow()
    {
        return LocalDateTime.now().plusDays(1);
    }

}
