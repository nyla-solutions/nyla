package nyla.solutions.core.patterns.conversion;

import java.util.Date;

public class DateToLongConverter implements Converter<Date, Long>
{

	@Override
	public Long convert(Date sourceObject)
	{
		if(sourceObject == null)
			return Long.valueOf(0);
		
		return sourceObject.getTime();
	}

}
