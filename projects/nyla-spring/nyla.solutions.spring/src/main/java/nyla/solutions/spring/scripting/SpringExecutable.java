package nyla.solutions.spring.scripting;
import java.util.Map;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import nyla.solutions.commas.Command;
import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;


/**
 * <pre>
 * Execute a spring expression language SPEL
 * 
 * 	&lt;bean id="cacheProcessDocSummary"
		class="solutions.global.patterns.scripting"&gt;
		&lt;property name="expression"
			value="${T(solutions.global.util.Debugger).println('Hello World')}"&gt;
		&lt;/property>
	&lt;/bean&gt;
	
      &lt;bean id="mkdirPrintingExecutable"
		class="com.merck.mrl.global.patterns.scripting.SpringExecutable"&gt;
		&lt;property name="expression"
			value="new java.io.File(${T(solutions.global.util.Config).getProperty('solutions.global.media.RotateImageFileCommand.outputPath')}).mkdir()"&gt;
		&lt;/property&gt;
	&lt;/bean&gt;
 * </pre>
 * @author Gregory Green
 *
 */
public class SpringExecutable implements Command<Integer, Environment>
{
	/**
	 * Default constructor
	 */
	public SpringExecutable()
	{
		parser = new SpelExpressionParser();
	}//---------------------------------------------
	/**
	 * Evaluation the boolean based on the evaluation object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Integer execute(Environment env)
	{
		 try
		{
			StandardEvaluationContext context = new StandardEvaluationContext(this.evaluationObject);
			
			Map runtimeVariables = Config.getProperties();
			
			//Map runtimeVariables = new Hashtable();
			
			if(env != null)
			{
				runtimeVariables.putAll(env.getMap());	
			}
			
			
			if(this.variables != null)
				runtimeVariables.putAll(this.variables);
			
			//Set variables
			context.setVariables(runtimeVariables);
			
			
			Debugger.println(this,"Parsing expression="+expression);
			
			Expression expression = parser.parseExpression(this.expression,new TemplatedParserContext());
			  
			Object object = expression.getValue(context);
			 		
			if(env != null && valueKey != null && object != null)
			{
				//put results in environment variable
				env.put(valueKey,object);				
			}
			
			return 0;
			 
		} 
		catch (Exception e)
		{
			throw new SystemException("expression="+expression+" evaluationObject="+evaluationObject+" \n ERROR:"+Debugger.stackTrace(e));
		}
		  
	}//---------------------------------------------
	/**
	 * @return the evaluationObject
	 */
	public Object getEvaluationObject()
	{
		return evaluationObject;
	}//---------------------------------------------
	/**
	 * @param evaluationObject the evaluationObject to set
	 */
	public void setEvaluationObject(Object evaluationObject)
	{
		this.evaluationObject = evaluationObject;				
		
	}//---------------------------------------------
	/**
	 * @return the expression
	 */
	public String getExpression()
	{
		return expression;
	}//---------------------------------------------
	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression)
	{
		this.expression = expression;
	}//---------------------------------------------	

	/**
	 * @return the variables
	 */
	public Map<Object,Object>  getVariables()
	{
		return variables;
	}//---------------------------------------------
	/**
	 * @param variables the variables to set
	 */
	public void setVariables(Map<Object,Object> variables)
	{
		this.variables = variables;
	}//---------------------------------------------
	/**
	 * @return the valueKey
	 */
	public String getValueKey()
	{
		return valueKey;
	}//---------------------------------------------
	/**
	 * @param valueKey the valueKey to set
	 */
	public void setValueKey(String valueKey)
	{
		this.valueKey = valueKey;
	}

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
	}
	private String valueKey = Config.getProperty(this.getClass(),"valueKey",this.getClass().getName()+".valueKey");

	private Map<Object,Object> variables = null;
	private ExpressionParser parser;
	private String expression = null;
	private Object evaluationObject = null;
}
