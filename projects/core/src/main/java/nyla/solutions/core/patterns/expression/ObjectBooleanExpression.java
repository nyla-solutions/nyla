package nyla.solutions.core.patterns.expression;

public interface ObjectBooleanExpression extends BooleanExpression<Object>
{
	public Object getEvaluationObject();
	public void setEvaluationObject(Object Object);
}
