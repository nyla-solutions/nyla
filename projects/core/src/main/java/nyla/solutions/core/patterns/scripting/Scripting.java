package nyla.solutions.core.patterns.scripting;

import java.util.Map;

/**
 * Interface for using bean scripting frameworks (such as GROOVY, SpEL, JRuby, etc).
 * 
 * @param <ReturnType> the return type
 * @param <EvalObjectType> the eval object type
 * @author Gregory Green
 *
 */
public interface Scripting<ReturnType,EvalObjectType>
{
	ReturnType interpret(String expression, EvalObjectType evaluationObject);
	
	/**
	 * @return the variables
	 */
	Map<String,?> getVariables();
	
	/**
	 * @param variables the variables to set
	 */
	void setVariables(Map<String,?> variables);
	
	
}
