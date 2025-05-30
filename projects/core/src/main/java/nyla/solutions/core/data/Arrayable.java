package nyla.solutions.core.data;

/**
 * Objects that support being converted to arrays
 * @author Gregory Green
 *
 * @param <T> the type
 */
public interface Arrayable<T>
{
	/**
	 * 
	 * @return array to array of objects
	 */
	T[] toArray();
}
