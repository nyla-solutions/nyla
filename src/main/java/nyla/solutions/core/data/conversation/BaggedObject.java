package nyla.solutions.core.data.conversation;

import java.io.Serializable;

/**
 * Wrapped interface to assist with conversions of objects
 * @author Gregory Green
 *
 * @param <T>
 */
public interface BaggedObject<T extends Serializable> extends Serializable
{
	/**
	 * This method will wrap a given object in a format that can be de-serialized later in a given format
	 * @param unBaggedObject
	 */
	void bag(T unBaggedObject);
	
	/**
	 * This method will unwrap a given object that was wrapped with the bag method
	 * @return the unbagged object
	 */
	T unbag();
}
