package nyla.solutions.core.patterns.creational;

/**
 * @param <ResultsType> the result type
 * Row mapper for a result set
 * @author Gregory Green
 *
 * @param <ObjectType> the object class
 */
@FunctionalInterface
public interface RowObjectCreator<ObjectType,ResultsType>
{
	/**
	 * 
	 * @param rs the result set
	 * @param index the current index
	 * @return the object instance
	 */
	ObjectType create(ResultsType rs, int index);
}
