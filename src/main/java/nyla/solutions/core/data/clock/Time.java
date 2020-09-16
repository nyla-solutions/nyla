package nyla.solutions.core.data.clock;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;


/**
 * A class representing a moment in time. Extends Day which represents the day
 * of the moment, and defines the time within the day to millisecond accuracy.
 * 
 */
public class Time implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7760994938523990073L;

	private LocalDateTime localDateTime;

	/**
	 * Instantiate a Time object. The time is lenient meaning that illegal day
	 * parameters can be specified and results in a recomputed day with legal
	 * month/day values.
	 * 
	 * @param year Year of this time
	 * @param month Month of this time
	 * @param dayOfMonth Day of month of this time.
	 * @param hours  Hours of this time [0-23]
	 * @param minutes  Minutes of this time [0-23]
	 * @param seconds Seconds of this time [0-23]
	 */
	public Time(int year, int month, int dayOfMonth,
	int hours, int minutes, int seconds)
	{
		LocalDateTime.of(year, month, dayOfMonth, hours, minutes, seconds);
	}

	public Time(LocalDateTime date)
	{
		this.localDateTime = date;
	}// --------------------------------------------

	public static Time now()
	{
		return new Time();
	}

	public void assignDate(LocalDateTime date)
	{
		if (date == null)
			return;

		this.localDateTime = date;
	}// --------------------------------------------

	public Time(Day day, int hours, int minutes, int seconds)
	{
		this(day.getYear(), day.getMonth(), day.getDayOfMonth(),
		hours, minutes, seconds);
	}

	public Time(int hours, int minutes, int seconds)
	{
		this(new Day(), hours, minutes, seconds);
	}

	public Time()
	{
		this.localDateTime = LocalDateTime.now();
	}

	public int getHour24()
	{
		return localDateTime.get(ChronoField.CLOCK_HOUR_OF_AMPM);
	}// --------------------------------------------

	public int getHour()
	{
		return localDateTime.getHour();
	}// --------------------------------------------

	/**
	 * 
	 * @return AM or PM
	 */
	public String getAmOrPm()
	{
		return localDateTime.get(ChronoField.AMPM_OF_DAY) == 0 ? "AM" : "PM";
	}// --------------------------------------------

	public int getMinutes()
	{
		return localDateTime.getMinute();
	}

	public int getSeconds()
	{
		return localDateTime.getSecond();
	}


	public long getMilliSeconds()
	{
		return localDateTime.get(ChronoField.MILLI_OF_SECOND);
	}

	public boolean isAfter(Time time)
	{
		return this.localDateTime.isAfter(time.localDateTime);
	}

	public boolean isBefore(Time time)
	{
		return this.localDateTime.isBefore(time.localDateTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((localDateTime == null) ? 0 : localDateTime.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Time other = (Time) obj;
		if (localDateTime == null)
		{
			if (other.localDateTime != null)
				return false;
		}
		else if (!localDateTime.equals(other.localDateTime))
			return false;
		return true;
	}

	public void addHours(int nHours)
	{
		localDateTime = localDateTime.plusHours(nHours);
	}

	public void addMinutes(long nMinutes)
	{
		localDateTime = localDateTime.plusMinutes(nMinutes);
	}

	public void addSeconds(long nSeconds)
	{
		localDateTime = localDateTime.plusSeconds(nSeconds);
	}

	public void addNanoSeconds(long nNanoSeconds)
	{
		localDateTime.plusNanos(nNanoSeconds);
	}

	public long milliSecondsBetween(Time time)
	{
		return Duration.between(this.localDateTime, time.localDateTime).toMillis();
	}

	public long secondsBetween(Time time)
	{
		return Duration.between(this.localDateTime, time.localDateTime).getSeconds();
	}

	public long minutesBetween(Time time)
	{
		return Duration.between(this.localDateTime, time.localDateTime).toMinutes();
	}

	public double hoursBetween(Time time)
	{
		return Duration.between(this.localDateTime, time.localDateTime).toHours();
	}

	public String toString()
	{
		StringBuffer string = new StringBuffer();
		string.append(super.toString());
		string.append(' ');
		if (getHour24() < 10)
			string.append('0');
		string.append(getHour24());
		string.append(':');
		if (getMinutes() < 10)
			string.append('0');
		string.append(getMinutes());
		string.append(':');
		if (getSeconds() < 10)
			string.append('0');
		string.append(getSeconds());
		string.append(',');
		string.append(getMilliSeconds());

		return string.toString();
	}

	/**
	 * @return the localDateTime
	 */
	public LocalDateTime getLocalDateTime()
	{
		return localDateTime;
	}

	public void setHour24(int startHour24)
	{
		this.localDateTime = this.localDateTime
		.with(ChronoField.HOUR_OF_DAY, startHour24);
		
	}
	
	
}
