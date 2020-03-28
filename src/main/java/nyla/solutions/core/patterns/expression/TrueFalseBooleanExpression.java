package nyla.solutions.core.patterns.expression;


/**
 * A hard coded boolean to return
 * @param <T> the type
 * @author Gregory Green
 *
 */
public class TrueFalseBooleanExpression<T> implements BooleanExpression<T>
{
	public Boolean apply(T obj)
	{		
		return value;
	}//---------------------------------------------
	/**
	 * @param value the value to set
	 */
	public void setBoolean(boolean value)
	{
		this.value = value;
	}//---------------------------------------------
	
	private boolean value = false;
}
