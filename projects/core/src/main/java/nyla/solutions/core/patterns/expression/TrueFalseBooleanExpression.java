package nyla.solutions.core.patterns.expression;


/**
 * A fixed boolean value to return
 * @param <T> the input type
 * @author Gregory Green
 *
 */
public class TrueFalseBooleanExpression<T> implements BooleanExpression<T>
{
	private boolean value;
	public TrueFalseBooleanExpression()
	{
		this.value = false;
	}

	/**
	 * Construct with a given value
	 * @param value the fixed
	 */
	public TrueFalseBooleanExpression(boolean value)
	{
		this.value = value;
	}

	/**
	 *
	 * @param obj
	 * @return value no matter what the input
	 */
	public Boolean apply(T obj)
	{		
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setBoolean(boolean value)
	{
		this.value = value;
	}
	

}
