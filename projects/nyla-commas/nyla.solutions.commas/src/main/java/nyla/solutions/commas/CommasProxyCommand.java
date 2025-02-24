/**
 * 
 */
package nyla.solutions.commas;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import nyla.solutions.core.data.Envelope;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Debugger;

/**
 * Proxy command for Command Annotations
 * This command before and after processing similar AOP based programming.
 * @author Gregory Green
 *
 */
public class CommasProxyCommand implements Command<Object,Object >
{

	
	public CommasProxyCommand(Object target, Method method, CommandFacts facts)
	{
		if(facts == null)
			throw new IllegalArgumentException("functionFacts required");
		
		if(target == null)
			throw new IllegalArgumentException("target required");
		
		if(method == null)
			throw new IllegalArgumentException("method required");
		
		this.facts = facts;
		
		//determine if advisedSkipMethodInvoke
	
			CommandAttribute[] commandAttributes =  this.facts.getCommandAttributes();
			
			if(commandAttributes == null)
				this.advisedSkipMethodInvoke = false;
			else
			{
				//loop thru attributes
				int len = commandAttributes.length;
				if(len == 0)
				{
					this.advisedSkipMethodInvoke = false;
				}
				else
				{
					boolean isFound = false;
					for (int i = 0; i < len; i++)
					{
						if(CommasConstants.ADVISED_SKIP_METHOD_INVOKE_CMD_ATTRIB_NAME.equals(commandAttributes[i].getName()))
						{
							isFound = true;
							
							String advisedValue = commandAttributes[i].getValue();
							if(advisedValue == null || advisedValue.length() == 0)
								this.advisedSkipMethodInvoke = true;
							else
								this.advisedSkipMethodInvoke = Boolean.parseBoolean(advisedValue);
							
							break;
						}
					}//end for
					
					if(!isFound)
						advisedSkipMethodInvoke = false;
				}
				
			
			
		}
		
		
		this.method = method;
		this.target = target;
	}// -----------------------------------------------

	/**
	 * @see nyla.solutions.commas.Command#execute(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Object execute(Object object)
	{
		Object payload = null;
		
		try
		{
			Envelope<Object> env = null;
			
			if(object instanceof Envelope)
				env = (Envelope<Object>)object;
			else
				env = new Envelope<Object>(object);
			
			//TODO: may need to save original payload
			//Object[] args = {payload};
			Map<Object,Object> header = env.getHeader();
			
			//If advice Command attribute isAdvisedSkipMethodInvoke = false
			boolean skippingMethod = advisedSkipMethodInvoke;
			
			
			if(this.beforeCommand != null)
				payload = this.beforeCommand.execute(env);
			else
				payload = env.getPayload();


			//Get updated header from envelope
			header = env.getHeader();
			
			if(header!= null)
			{
				Boolean isAlwayExecuteMethod = (Boolean)header.get(CommasConstants.ALWAYS_EXECUTE_METHOD_HEADER);
				if(isAlwayExecuteMethod != null)
					skippingMethod = !Boolean.TRUE.equals(isAlwayExecuteMethod); //override skip
			}
				
			if(!skippingMethod)
			{
				try
				{
					payload = method.invoke(target, payload);
				}
				catch(IllegalArgumentException e)
				{
					throw new IllegalArgumentException(
							target.getClass().getName()+"."+method.getName()+
							"("+Debugger.toString(payload)+") ERROR:"+e.getMessage(),e);
				}
			}
			
			env.setPayload(payload);
			
			if(this.afterCommand != null)
				payload = afterCommand.execute(env);
			
			return payload;
		}
		catch (IllegalAccessException e)
		{
			Throwable cause = e.getCause();
			if(cause instanceof RuntimeException)
				throw (RuntimeException)cause;
			
			throw new SystemException(cause);
		}
		catch (InvocationTargetException e)
		{
			Throwable cause = e.getCause();
			if(cause instanceof RuntimeException)
				throw (RuntimeException)cause;
			
			throw new SystemException(cause);
		}
		
		/*catch (IllegalArgumentException e)
		{
			throw new SystemException("COMMAND:"+facts.getName()
					+" arguments:"+Debugger.toString(env),e);
			
			
		}
		catch (IllegalAccessException e)
		{
			throw new SystemException("COMMAND:"+facts.getName()+
					" CLASS:"+method.getDeclaringClass().getName()
					+" METHOD:"+method.getName(),e.getCause());
		}
		catch (InvocationTargetException e)
		{
			throw new SystemException("COMMAND:"+facts.getName()+
					" CLASS:"+method.getDeclaringClass().getName()
					+" METHOD:"+method.getName()	,e.getCause());
		}*/

	}// -----------------------------------------------
	
	/**
	 * @return the afterCommand
	 */
	public Command<Object,Envelope<Object>> getAfterCommand()
	{
		return afterCommand;
	}

	/**
	 * @param afterCommand the afterCommand to set
	 */
	public void setAfterCommand(Command<Object,Envelope<Object>> afterCommand)
	{
		this.afterCommand = afterCommand;
	}

	/**
	 * @return the beforeCommand
	 */
	public Command<Object,Envelope<Object>> getBeforeCommand()
	{
		return beforeCommand;
	}

	/**
	 * @param beforeCommand the beforeCommand to set
	 */
	public void setBeforeCommand(Command<Object,Envelope<Object>> beforeCommand)
	{
		this.beforeCommand = beforeCommand;
	}
	

	/**
	 * @return the facts
	 */
	public CommandFacts getFunctionFacts()
	{
		return facts;
	}

	/**
	 * @param facts the facts to set
	 */
	public void setFunctionFacts(CommandFacts facts)
	{
		this.facts = facts;
	}

	/**
	 * @return the target
	 */
	public Object getTarget()
	{
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Object target)
	{
		this.target = target;
	}

	/**
	 * @return the method
	 */
	public Method getMethod()
	{
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(Method method)
	{
		this.method = method;
	}


	private Command<Object,Envelope<Object>> afterCommand = null;
	private Command<Object,Envelope<Object>> beforeCommand = null;
	private CommandFacts facts = null;
	private Object target = null;
	private Method method = null;
	private boolean advisedSkipMethodInvoke;

}
