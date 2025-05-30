package nyla.solutions.spring.validation;

import java.util.HashSet;
import java.util.Set;

import org.springframework.validation.Errors;

import nyla.solutions.core.data.Arrayable;
import nyla.solutions.core.exception.DuplicateRowException;

/**
 * @author Gregory Green
 *
 */
public class ArrayableUniqueValidator extends AbstractValidation
{


	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void validate(Object obj, Errors errors)
	{
		if(set == null)
			set = new HashSet<Object>();
		
		Arrayable<Object> arrayable = (Arrayable<Object>)obj;
		
		Object key = arrayable.toArray()[keyPosition];
		
		if(set.contains(key))
			throw new DuplicateRowException(String.valueOf(key));

		set.add(key);
		
	}// --------------------------------------------------------
	
	
	/**
	 * @return the keyPosition
	 */
	public int getKeyPosition()
	{
		return keyPosition;
	}
	/**
	 * @param keyPosition the keyPosition to set
	 */
	public void setKeyPosition(int keyPosition)
	{
		this.keyPosition = keyPosition;
	}


	private Set<Object> set = null;
	private int keyPosition = 0;

}
