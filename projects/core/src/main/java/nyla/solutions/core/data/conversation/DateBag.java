package nyla.solutions.core.data.conversation;

import java.io.Serializable;
import java.util.Date;

/**
 * This object support conversations of the java.util.Date objects
 * @author Gregory Green
 *
 */
public class DateBag implements Serializable, BaggedObject<Date>
{
	/**
	 * Default constructor
	 */
	public DateBag()
	{
	}// --------------------------------------------------------
	/**
	 * 
	 * @param date the date to wrap
	 */
	public DateBag(Date date)
	{
		if(date ==null)
			this.time = 0;
		else
			setTime(date.getTime());
	}// --------------------------------------------------------
	/**
	 * @return the time
	 */
	public long getTime()
	{
		return time;
	}// --------------------------------------------------------

	/**
	 * @param time the time to set
	 */
	public void setTime(long time)
	{
		this.time = time;
	}


	/**
	 * Wraps a given date in a format that can be easily unbagged
	 */
	@Override
	public void bag(Date unBaggedObject)
	{
		if(unBaggedObject == null)
			this.time = 0;
		else
			this.time = unBaggedObject.getTime();
		
	}// --------------------------------------------------------
	@Override
	public Date unbag()
	{
		if(time == 0)
			return null;
		
		return new Date(time);
	}
	
	private long time = 0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7751615472903561078L;
}
