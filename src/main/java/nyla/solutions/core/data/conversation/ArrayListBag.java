package nyla.solutions.core.data.conversation;

import nyla.solutions.core.exception.NonSerializableException;
import nyla.solutions.core.util.Organizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Wrapper for ArrayList object to support serialization
 * @author Gregory Green
 *
 */
public class ArrayListBag<T extends Serializable> implements Serializable, BaggedObject<ArrayList<T>>
{
	public  ArrayListBag()
	{
	}

	public  ArrayListBag(ArrayList<T> list)
	{
		bag(list);
	}


	@Override
	public void bag(ArrayList<T> unBaggedObject)
	{
		if(unBaggedObject == null || unBaggedObject.isEmpty())
			return;
		
		
		arrayObj = new Object[unBaggedObject.size()];

		Object obj = null;
		
		for (int i=0; i < arrayObj.length; i ++)
		{
			obj = unBaggedObject.get(i);

			if(!(obj instanceof Serializable))
				throw new NonSerializableException("Non serializable object at index "+i);
			else if(obj instanceof Date)
				obj = new DateBag((Date)obj);
			else if(obj instanceof List)
				obj = new ArrayListBag(Organizer.toArrayList((List)obj));
			
			arrayObj[i] = obj;
		}
	}// --------------------------------------------------------
	
	/**
	 * @return the arrayObj
	 */
	public Object[] getArrayObj()
	{
		if(arrayObj == null)
			return null;
		
		Object[] copy = new Object[this.arrayObj.length];
		
		System.arraycopy(this.arrayObj, 0, copy, 0, copy.length);
		return copy;
				
	}

	/**
	 * @param arrayObj the arrayObj to set
	 */
	public void setArrayObj(Object[] arrayObj)
	{
		if(arrayObj == null)
			return;
		
		this.arrayObj = new Object[arrayObj.length]; 
		
		System.arraycopy(arrayObj, 0, this.arrayObj, 0, this.arrayObj.length);
	}// --------------------------------------------------------
	/*
	 * Creates arraylist from the wrapped object array 
	 */
	@Override
	public ArrayList<T> unbag()
	{
		if(arrayObj == null || arrayObj.length == 0)
			return null;
		
		ArrayList<Object> list = new ArrayList<Object>(this.arrayObj.length);
		
		Object value = null;
		for (int i = 0; i < arrayObj.length; i++)
		{
			value = arrayObj[i];
			
			if(value instanceof BaggedObject)
				value = ((BaggedObject)value).unbag();
			
			list.add(value);
		}
		return (ArrayList)list;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4995496063381565327L;
	private Object[] arrayObj;
}
