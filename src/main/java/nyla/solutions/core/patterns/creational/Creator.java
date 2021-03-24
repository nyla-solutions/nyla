package nyla.solutions.core.patterns.creational;

/**
 * 
 * Interface to create objects
 * 
 * @author Gregory Green
 *
 * @param <T> the type to make
 */
public interface Creator<T>
{
	/**
	 * 
	 * @return the create object
	 */
	T create();
}
