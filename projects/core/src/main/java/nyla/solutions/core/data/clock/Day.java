package nyla.solutions.core.data.clock;

import nyla.solutions.core.util.Scheduler;
import nyla.solutions.core.util.Text;

import java.io.Serializable;
import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



/**
 * Data structure to represent a Day
 * @author Gregory Green
 *
 */
public class Day  implements Comparable<Day>, Serializable
{
	public static final String DAY_FORMAT = "M/dd/yyyy";
	
  /**
	 * 
	 */
	private static final long serialVersionUID = 7895096174662828704L;
	
	
	/** The back end calendar instance of this day. */
	  protected final LocalDate localDate;



  /**
	 * @return the localDate
	 */
	public LocalDate getLocalDate()
	{
		return localDate;
	}

/**
   * Create a new day. The day is lenient meaning that illegal day
   * parameters can be specified and results in a recomputed day with
   * legal month/day values.
   *
   * @param year        Year of new day.
   * @param month       Month of new day (0-11)
   * @param dayOfMonth  Day of month of new day (1-31)
   */
  public Day(int year, int month, int dayOfMonth)
  {
	localDate = LocalDate.of(year, month, dayOfMonth);
  }

  public Day(String date)
  {
	  this(Text.toDate(date, DAY_FORMAT));
  }


  /**
   * Create a new day, specifying the year and the day of year.
   * The day is lenient meaning that illegal day parameters can be
   * specified and results in a recomputed day with legal month/day
   * values.
   *
   * @param year       Year of new day.
   * @param dayOfYear  1=January 1, etc.
   */
  public Day(int year, int dayOfYear)
  {
	  this.localDate = LocalDate.ofYearDay(year, dayOfYear);
  }

  /**
   * Create a new day representing the day of creation
   * (according to the setting of the current machine).
   */
  public Day()
  {
    // Now (in the current locale of the client machine)
    this.localDate = LocalDate.now();
  }
  public Day(LocalDate theCalendar)
  {
	super();
	this.localDate = theCalendar;
   }

/**
   * Create a new day based on a java.util.Calendar instance.
   * NOTE: The time component from calendar will be pruned.
   *
   * @param calendar  Calendar instance to copy.
   * @throws IllegalArgumentException  If calendar is null.
   */
  public Day(Calendar calendar)
  {
    if (calendar == null)
      throw new IllegalArgumentException("calendar cannot be null");

    this.localDate = Scheduler.toLocalDate(calendar.getTime());
  }



  /**
   * Create a new day based on a java.util.Date instance.
   * NOTE: The time component from date will be pruned.
   *
   * @param date  Date instance to copy.
   * @throws IllegalArgumentException  If date is null.
   */
  public Day(Date date)
  {
    if (date == null)
      throw new IllegalArgumentException("dat cannot be null");

    this.localDate = Scheduler.toLocalDateTime(date).toLocalDate();
  }



  /**
   * Create a new day based on a time value.
   * Time is milliseconds since "the Epoch" (1.1.1970).
   * NOTE: The time component from time will be pruned.
   *
   * @param time  Milliseconds since "the Epoch".
   */
  public Day(long time)
  {
    this(new Date(time));
  }



  /**
   * Create a new day as a copy of the specified day.
   *
   * @param day  Day to clone.
   * @throws IllegalArgumentException  If day is null.
   */
  public Day(Day day)
  {
    if (day == null)
      throw new IllegalArgumentException("day cannot be null");
    
    this.localDate = day.localDate;

  }



  /**
   * A more explicit front-end to the Day() constructor which return a day
   * object representing the day of creation.
   *
   * @return  A day instance representing today.
   */
  public static Day today()
  {
    return new Day();
  }


  /**
   * Return a Date instance representing the same date
   * as this instance. For use by secondary methods requiring
   * java.util.Date as input.
   *
   * @return  Date equivalent representing this day.
   */
  public LocalDate getDate()
  {
    return this.localDate;
  }



  /**
   * Compare this day to the specified day. If object is
   * not of type Day a ClassCastException is thrown.
   *
   * @param object  Day object to compare to.
   * @return @see Comparable#compareTo(Object)
   * @throws IllegalArgumentException  If day is null.
   */
  public int compareTo(Day object)
  {
     if (object == null)
        throw new IllegalArgumentException("day cannot be null");
     
     Day day = (Day)object;     

    return localDate.compareTo(day.localDate);
  }// --------------------------------------------




  /**
   * Return true if this day is after the specified day.
   *
   * @param day  Day to compare to.
   * @return True if this is after day, false otherwise.
   * @throws IllegalArgumentException  If day is null.
   */
  public boolean isAfter(Day day)
  {
    if (day == null)
      throw new IllegalArgumentException("day cannot be null");

    return localDate.isAfter(day.localDate);
  }



  /**
   * Return true if this day is before the specified day.
   *
   * @param day  Day to compare to.
   * @return  True if this is before day, false otherwise.
   * @throws IllegalArgumentException  If day is null.
   */
  public boolean isBefore(Day day)
  {
    if (day == null)
      throw new IllegalArgumentException("day cannot be null");

    return localDate.isBefore(day.localDate);
  }



  /**
   * Return true if this day equals (represent the same date)
   * as the specified day.
   *
   * @param object  Object to compare to.
   * @return      True if this equals day, false otherwise.
   * @throws IllegalArgumentException  If day is null.
   */
  public boolean equals(Object object)
  {
	  if(!(object instanceof Day))
		  return false;
	  
	  
    Day day = (Day) object;


    return localDate.equals(day.localDate);
  }



  /**
   * Overload required as default definition of equals() has changed.
   *
   * @return  A hash code value for this object.
   */
  public int hashCode()
  {
    return localDate.hashCode();
  }



  /**
   * Return year of this day.
   *
   * @return  Year of this day.
   */
  public int getYear()
  {
    return localDate.getYear();
  }



  /**
   * Return month of this day. The result must be compared to Calendar.JANUARY,
   * Calendar.FEBRUARY, etc.
   *
   * @return  Month of this day.
   */
  public int getMonth()
  {
    return localDate.getMonthValue();
  }



  /**
   * Return the 1-based month number of the month of this day.
   * 1 = January, 2 = February and so on.
   *
   * @return Month number of this month
   */
  public int getMonthNo()
  {
   return this.localDate.getMonthValue();
  }// --------------------------------------------


  /**
   * Return day of month of this day.
   * NOTE: First day of month is 1 (not 0).
   *
   * @return  Day of month of this day.
   */
  public int getDayOfMonth()
  {
    return localDate.getDayOfMonth();
  }



  /**
   * Return the day number of year this day represents.
   * January 1 = 1 and so on.
   *
   * @return day number of year.
   */
  public int getDayOfYear()
  {
    return localDate.getDayOfYear();
  }



  /**
   * Return the day of week of this day.
   * NOTE: Must be compared to Calendar.MONDAY, TUSEDAY etc.
   *
   * @return  Day of week of this day.
   */
  public DayOfWeek getDayOfWeek()
  {
    return localDate.getDayOfWeek();
  }



  /**
   * Return the day number of week of this day, where
   * Monday=1, Tuesday=2, ... Sunday=7.
   *
   * @return  Day number of week of this day.
   */
  public int getDayNumberOfWeek()
  {
	  return this.localDate.get(ChronoField.DAY_OF_WEEK);
  }



  /**
   * Return the week number of year, this day
   * belongs to. 1st=1 and so on.
   *
   * @return  Week number of year of this day.
   */
  public int getWeekOfYear()
  {
    return localDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
  }



  /**
   * Return a day which is the given number of days after this day.
   *
   * @param nDays  Number of days to add. May be negative.
   * @return  Day as requested.
   */
  public Day addDays(int nDays)
  {
	 
	return new Day(this.localDate.plusDays(nDays));
  }



  /**
   * Subtract a number of days from this day.
   *
   * @param nDays  Number of days to subtract.
   * @return  Day as requested.
   */
  public Day subtractDays(int nDays)
  {
    return addDays(-nDays);
  }



  /**
   * Return a day wich is a given number of month after this day.
   *
   * The actual number of days added depends on the staring day.
   * Subtracting a number of months can be done
   * by a negative argument to addMonths() or calling subtactMonths()
   * explicitly.
   * NOTE: addMonth(n) m times will in general give a different result
   * than addMonth(m*n). Add 1 month to January 31, 2005 will give
   * February 28, 2005.
   *
   * @param nMonths  Number of months to add.
   * @return  Day as requested.
   */
  public Day addMonths(int nMonths)
  {
    return new Day(this.localDate.plusMonths(nMonths));
  }



  /**
   * Subtract a number of months from this day.
   *
   * @param nMonths  Number of months to subtract.
   * @return  Day as requested.
   */
  public Day subtractMonths(int nMonths)
  {
    return addMonths(-nMonths);
  }



  /**
   * Return a day wich is a given number of years after this day.
   *
   * Add a number of years to this day. The actual
   * number of days added depends on the starting day.
   * Subtracting a number of years can be done by a negative argument to
   * addYears() or calling subtractYears explicitly.
   *
   * @param nYears  Number of years to add.
   * @return  Day as requested.
   */
  public Day addYears(int nYears)
  {
	  return new Day(this.localDate.plusYears(nYears));
  }



  /**
   * Subtract a number of years from this day.
   *
   * @param nYears  Number of years to subtract.
   * @return  Day as requested.
   */
  public Day subtractYears(int nYears)
  {
    return addYears(-nYears);
  }



  /**
   * Return the number of days in the year of this day.
   *
   * @return  Number of days in this year.
   */
  public int getDaysInYear()
  {
    return this.localDate.lengthOfYear();
  }



  /**
   * Return true if the year of this day is a leap year.
   *
   * @return  True if this year is a leap year, false otherwise.
   */
  public boolean isLeapYear()
  {
    return this.localDate.isLeapYear();
  }



  /**
   * Return true if the specified year is a leap year.
   *
   * @param year  Year to check.
   * @return      True if specified year is leap year, false otherwise.
   */
  public static boolean isLeapYear(int year)
  {
    return new Day(year, 1, 1).isLeapYear();
  }



  /**
   * Return the number of days in the month of this day.
   *
   * @return  Number of days in this month.
   */
  public int getDaysInMonth()
  {
    return localDate.getMonth().length(Year.now().isLeap());
  }



  /**
   * Get default locale name of this day ("Monday", "Tuesday", etc.
   *
   * @return  Name of day.
   */
  public String getDayName()
  {
    return this.localDate.getDayOfWeek()
    .getDisplayName(TextStyle.SHORT, Locale.US);
  }// --------------------------------------------
  

  /**
   * Return number of days between two days.
   * The method always returns a positive number of days.
   *
   * @param day  The day to compare to.
   * @return  Number of days between this and day.
   * @throws IllegalArgumentException  If day is null.
   */
  public long daysBetween(Day day)
  {
    return Duration.between(this.localDate.atStartOfDay(), day.localDate.atStartOfDay()).toDays();
  }



  /**
   * Find the n'th xxxxday of s specified month (for instance find 1st sunday
   * of May 2006; findNthOfMonth (1, Calendar.SUNDAY, Calendar.MAY, 2006);
   * Return null if the specified day doesn't exists.
   *
   * @param n          Nth day to look for.
   * @param dayOfWeek  Day to look for (Calendar.XXXDAY).
   * @param month      Month to check (Calendar.XXX).
   * @param year       Year to check.
   * @return           Required Day (or null if non-existent)
   * @throws IllegalArgumentException if dyaOfWeek parameter
   *                   doesn't represent a valid day.
   */
  public static Day getNthOfMonth(int n, int dayOfWeek, int month, int year)
  {
    // Validate the dayOfWeek argument
    if (dayOfWeek < 0 || dayOfWeek > 6)
      throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);

    LocalDateTime localDateTime = LocalDateTime.of(year, month, n, 0, 0);
    return new Day(localDateTime
    		.with(TemporalAdjusters
    		.next(DayOfWeek.of(dayOfWeek)))
    				.toLocalDate());
  }



  /**
   * Find the first of a specific day in a given month. For instance
   * first Tuesday of May:
   * getFirstOfMonth(Calendar.TUESDAY, Calendar.MAY, 2005);
   *
   * @param dayOfWeek  Weekday to get.
   * @param month      Month of day to get.
   * @param year       Year of day to get.
   * @return           The requested day.
   */
  public static Day getFirstOfMonth(int dayOfWeek, int month, int year)
  {
    return Day.getNthOfMonth(1, dayOfWeek, month, year);
  }



  /**
   * Find the last of a specific day in a given month. For instance
   * last Tuesday of May:
   * getLastOfMonth (Calendar.TUESDAY, Calendar.MAY, 2005);
   *
   * @param dayOfWeek  Weekday to get.
   * @param month      Month of day to get.
   * @param year       Year of day to get.
   * @return           The requested day.
   */
  public static Day getLastOfMonth(int dayOfWeek, int month, int year)
  {
    return Day.getNthOfMonth(Calendar.SATURDAY, dayOfWeek, month, year);
    //return day != null ? day : Day.getNthOfMonth(4, dayOfWeek, month, year);
  }



  /**
   * Return a scratch string representation of this day.
   * Used for debugging only. The format of the
   * day is dd/mm-yyyy
   *
   * @return  A string representation of this day.
   */
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    if (getMonth() < 9)
        s.append('0');
    s.append(getMonth());
    s.append('/');
    if (getDayOfMonth() < 10)
      s.append('0');
    s.append(getDayOfMonth());
    s.append('/');


    s.append(getYear());
    s.append(" ");
    s.append(getDayName());

    return s.toString();
  }

  /**
   * Compare based on month day year
   * @param compared the day to compare
   * @return if compared is the same day
   */
  public boolean isSameDay(Day compared)
  {
	  if(compared == null)
		  return false;
	 
  	return this.getMonth() == compared.getMonth()
  	&& this.getDayOfMonth() == compared.getDayOfMonth()
  	&& this.getYear() == compared.getYear();
  }
}
