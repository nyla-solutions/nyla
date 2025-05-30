package nyla.solutions.spring.scripting;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.scripting.Scripting;
import nyla.solutions.core.util.Debugger;

/**
 * <pre>
 * Execute a spring expression
 * 
 * Sample test cases
 * 
 *
		SpringSpELScripting<String,Object> spelScripting = new SpringSpELScripting<String,Object>();
		//${T(solutions.global.util.Debugger).println('Hello World')}"
		String results = spelScripting.interpret(
		"new nyla.solutions.core.patterns.scripting.SpringSpELScriptingTest().getName()", null);
		
		Assert.assertEquals("SpringSpEL",results );
		
		
		 results = spelScripting.interpret(
					"T(java.util.Calendar).getInstance().getTime().getTime().toString()", null);
		 
		 Assert.assertTrue(results != null && !results.contains("java.util.Calendar"));
		 
		HashMap<String,String> variables = new HashMap<String,String>();
		variables.put("hello", "world");
		spelScripting.setVariables(variables);
		
		//get from SpringSpELScriptingTest object
		results = spelScripting.interpret(
				"name", new SpringSpELScriptingTest());
		
		Assert.assertEquals("SpringSpEL",results );
		
		//get from variable
		results = spelScripting.interpret(
				"get('hello')", null);
		
		Assert.assertEquals("world",results );
		
		results = spelScripting.interpret(
				"length().toString()", "world");
		
		Assert.assertEquals("5",results );
 * </pre>
 * @author Gregory Green
 * @param <ReturnType> the return type of the expression
 * @param <EvaluationObjectType>  the object to use
 *
 */
public class SpringSpELScripting<ReturnType,EvaluationObjectType> implements Scripting<ReturnType,EvaluationObjectType>
{
	/**
	 * Default constructor
	 */
	public SpringSpELScripting()
	{
		parser = new SpelExpressionParser();
	}//---------------------------------------------

	@SuppressWarnings({ "unchecked" })
	public ReturnType interpret(String expression, EvaluationObjectType evaluationObject)
	{		
		if(parser == null)
			throw new IllegalStateException("Parser required");
		
		 try
		 {			
			
			Debugger.println(this,"Parsing expression="+expression);
			
			//Unsafe if the input is control by the user..
			Expression exp = parser.parseExpression(expression);

			if(evaluationObject != null)
			{
				StandardEvaluationContext context = new StandardEvaluationContext(evaluationObject);
			
				if(this.variables != null)
					context.setVariables(this.variables);
				
				return (ReturnType)exp.getValue(context);
			}
			else
			{
				
				if(this.variables != null)
				{
					StandardEvaluationContext context = new StandardEvaluationContext(this.variables);

					
					return (ReturnType)exp.getValue(context);
				}
				
				return (ReturnType)exp.getValue();
			}
		} 
		catch (Exception e)
		{
			throw new SystemException("expression="+expression+" evaluationObject="+evaluationObject+" \n ERROR:"+e.getMessage(),e);
		}
		  
	}//---------------------------------------------
	/**
	 * @return the variables
	 */
	public Map<String,?>  getVariables()
	{
		if(this.variables == null)
			this.variables = new Hashtable<String,Object>();
		
		return variables;
	}//---------------------------------------------
	/**
	 * @param variables the variables to set
	 */
	@SuppressWarnings("unchecked")
	public void setVariables(Map<String,?> variables)
	{
		this.variables = (Map<String, Object>)variables;
	}//---------------------------------------------
	public static class TemplatedParserContext implements ParserContext 
	{
		public String getExpressionPrefix() {
		return "${";
		}
		public String getExpressionSuffix() {
		return "}";
		}
		public boolean isTemplate() {
		return true;
		}
	}//---------------------------------------------

	private Map<String,Object> variables = null;
	private ExpressionParser parser=null;
}
