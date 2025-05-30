/**
 * 
 */
package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.patterns.scripting.Scripting;
import nyla.solutions.core.util.Text;

import java.util.Map;

/**
 * <pre>
 * Sample
 * 	&lt;bean id="solutions.global.patterns.scripting.Scripting" class="solutions.global.patterns.scripting.SpringFramework"&gt;
	&lt;/bean&gt;
   
	&lt;bean id="solutions.global.patterns.decorator.ScriptingText" class="solutions.global.patterns.decorator.ScriptingText"&gt;
	     &lt;property name="expression" value="${new java.text.SimpleDateFormat('MM/dd/yyyy').format(T(java.util.Calendar).getInstance().getTime())}"/&gt;
	     &lt;property name="scripting"&gt;
	        &lt;ref bean="solutions.global.patterns.scripting.Scripting"/&gt;
	     &lt;/property&gt;
		&lt;property name="variables"&gt;			      	
			      	&lt;map&gt;
		   				&lt;entry&gt;
			      			&lt;key&gt;&lt;value&gt;title&lt;/value&gt;&lt;/key&gt;
			      			&lt;value&gt;Test title*lt;/value&gt;
		   				&lt;/entry&gt;

					&lt;/map&gt;
		&lt;/property&gt;
	&lt;/bean&gt;

 * </pre>
 * @author Gregory Green
 *
 */
public class ScriptingText implements Textable
{
    private Map<String,?> variables = null;
    private Object evaluationObject = null;
    private String expression = null;
    private Scripting<Object,Object> scripting = null;

   /**
    * Default constructor
    */
   public ScriptingText()
   {
   }// ----------------------------------------------
   /**
    * @return the text of output of the script
    */
   public String getText()
   {
	if (this.scripting == null)
	   throw new RequiredException("this.scripting");
	
	if(this.variables != null)
	   this.scripting.setVariables(variables);
	
	return Text.toString(this.scripting.interpret(expression, evaluationObject));
   }// ----------------------------------------------
   
   /**
    * @return the scripting
    */
   public Scripting<?,?> getScripting()
   {
      return scripting;
   }

   /**
    * @param scripting the scripting to set
    */
   @SuppressWarnings("unchecked")
   public void setScripting(Scripting<?,?> scripting)
   {
      this.scripting = (Scripting<Object,Object>)scripting;
   }// ----------------------------------------------
   
   
   /**
    * @return the variables
    */
   public Map<String,?> getVariables()
   {
      return variables;
   }// ----------------------------------------------
   /**
    * @param variables the variables to set
    */
   public void setVariables(Map<String,?> variables)
   {
      this.variables = variables;
   }
   /**
    * @return the evaluationObject
    */
   public Object getEvaluationObject()
   {
      return evaluationObject;
   }
   /**
    * @param evaluationObject the evaluationObject to set
    */
   public void setEvaluationObject(Object evaluationObject)
   {
      this.evaluationObject = evaluationObject;
   }
   /**
    * @return the expression
    */
   public String getExpression()
   {
      return expression;
   }
   /**
    * @param expression the expression to set
    */
   public void setExpression(String expression)
   {
      this.expression = expression;
   }




}
