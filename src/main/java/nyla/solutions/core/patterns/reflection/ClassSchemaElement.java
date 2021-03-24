package nyla.solutions.core.patterns.reflection;

/**
 * 
 * @author Gregory Green
 *
 */
public interface ClassSchemaElement
{
	/**
	 * 
	 * @param visitor the class schema element
	 */
	public void accept(ClassSchemaVisitor visitor);
}
