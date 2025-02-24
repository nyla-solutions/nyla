package nyla.solutions.core.patterns.creational;

/**
 * <pre>
 * Director from builder design pattern
 * @author Gregory Green
 *
 * @param <T> the builder type
 * 
 * 
 * Example Implementation
 * 
 *  public class AclConstructionDirector implements BuilderDirector<SecurityAclBuilder>
 *	{
 *	
 *		public void construct(SecurityAclBuilder builder) 
 *		{
 *		    //BUILD based on object passed into constructor
 *		} 
 *
 * </pre>
 */
public interface BuilderDirector<T>
{
	/**
	 * 
	 * @param builder
	 */
	void construct(T builder);
}
