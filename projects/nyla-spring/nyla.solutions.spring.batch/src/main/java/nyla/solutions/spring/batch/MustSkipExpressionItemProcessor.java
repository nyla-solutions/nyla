package nyla.solutions.spring.batch;

import org.springframework.batch.item.ItemProcessor;

import nyla.solutions.core.patterns.expression.BooleanExpression;
import nyla.solutions.core.util.Debugger;

/**
 * @author Gregory Green
 *
 */
public class MustSkipExpressionItemProcessor implements ItemProcessor<Object, Object>
{

	public MustSkipExpressionItemProcessor()
	{
		Debugger.println(this,"created");
	}
	@Override
	public Object process(Object obj) throws Exception
	{
		Debugger.println(this,"Determining if must skip");
		
		if(mustSkipBooleanExpression.apply(obj))
		{
			Debugger.println(this,"SKIPPING RECORD:"+obj);
			return null;
		}
		
		Debugger.println(this,"Return original object"+obj);
		return obj;
	}// --------------------------------------------------------


	/**
	 * @return the mustSkipBooleanExpression
	 */
	public BooleanExpression<Object> getMustSkipBooleanExpression()
	{
		return mustSkipBooleanExpression;
	}


	/**
	 * @param mustSkipBooleanExpression the mustSkipBooleanExpression to set
	 */
	public void setMustSkipBooleanExpression(
			BooleanExpression<Object> mustSkipBooleanExpression)
	{
		this.mustSkipBooleanExpression = mustSkipBooleanExpression;
	}


	private BooleanExpression<Object> mustSkipBooleanExpression;
}
