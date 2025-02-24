package nyla.solutions.commas;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.security.SecuredToken;
import nyla.solutions.core.util.Debugger;

public class ShellMgr implements Shell, InvocationHandler
{
	/**
	 * 
	 * @param commasName the shell name
	 */
	protected ShellMgr(String commasName, Shell shell)
	{
		this(commasName,shell,null);
	}// -----------------------------------------------
	/**
	 * 
	 * @param commasName the shell name
	 */
	protected ShellMgr(String commasName, Shell shell, SecuredToken securedToken)
	{
		if (commasName == null)
			throw new
			RequiredException("commasName");
		
		if(shell == null)
			throw new RequiredException("shell");
		
		this.name = commasName;
		
		this.shell = shell;
				
		this.securedToken = securedToken;
		
	}// -----------------------------------------------
	/**
	 * Implementation for the InvocationHandler to execute functions
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
	throws Throwable
	{
		
		String methodName = method.getName();
		
		Debugger.println(this,"ShellMgr.invoke method="+methodName);
		
		if(!(proxy instanceof Shell) && !"toString".equals(methodName))
		{

			return executeMethodCommand(args, methodName);
		}
		else if(methodName.matches("executeFunction|getFunction|listFunction|toString|getName"))
		{
			//special shell function
			if("executeFunction".equals(methodName))
			{
				if(args == null || args.length != 2)
					throw new RequiredException("Function name and request for method "+methodName);
				
				String commandName = (String)args[0];
				Object request = args[1];
				
				return this.executeCommand(commandName, request);
			}
		   else if("getFunction".equals(methodName))
			{
				if(args == null || args.length == 0)
					throw new IllegalArgumentException("args required for method call "+methodName);
				
				int argsLength = args.length;
				switch(argsLength)
				{
					case 1: return this.getCommand((String)args[0]);		
					case 2: return this.getCommand((String)args[0],args[1] );
					default: throw new IllegalArgumentException("Invalid number of arguments for getFunction method");
				}
			}
			else if("listFunction".equals(methodName))
			{
				return this.getCommands();
			}
			else if("toString".equals(methodName))
			{
				return method.getDeclaringClass().getName()+"."+method.getName();
			}
			else if("getName".equals(methodName))
			{
				return this.name;
			}
		}
		
		return executeMethodCommand(args, methodName);
		
	}// -----------------------------------------------
	/**
	 * 
	 * @param args the method arguments
	 * @param methodName the method to execute
	 * @return the method's return
	 */
	private Object executeMethodCommand(Object[] args, String methodName)
	{
		Command<Object,Object> function = null;
		try
		{	
			//delegate function call
			String key = new StringBuilder().append(this.name)
					.append(CommasConstants.nameSeparator)
					.append(methodName).toString();
					
			function = this.getCommand(key);
			
			if(args != null && args.length >0 )
			{
				return function.execute(args[0]);
			}
			else
				return function.execute(null);
		
		}
		catch(RuntimeException e)
		{
			throw e;
		/*	Debugger.printError(this,e);
			
		   RuntimeException gediException = new FaultMgr().raise(e, args);
			if(function != null)
				gediException.(methodName);
			
		    throw gediException;
		    */
		}
	}// -----------------------------------------------
	/**
	 * 
	 * @see nyla.solutions.commas.medco.fabrix.grid.daf.Shell#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}// -----------------------------------------------

	/**
	 * @param commandName the function name
	 * @return Function managed
	 * @see nyla.solutions.commas.medco.fabrix.grid.daf.Shell#getFunction(java.lang.String)
	 */
	public <ReturnType,ObjectType> Command<ReturnType,ObjectType> getCommand(String commandName)
	throws NoDataFoundException
	{
		
			if (commandName == null || commandName.length() == 0)
				throw new RequiredException("commandName");
			
			commandName = CommasServiceFactory.toCommandName(this.name,commandName);
			
			Command<ReturnType,ObjectType> command = this.shell.getCommand(commandName);
			
			if(command == null)
				throw new NoDataFoundException(commandName+" in shell"+this.shell);
			

			if(this.securedToken != null)
			{
				/*

					//wrap with secured token
					function = new FunctionSecuredToken(function, this.securedToken);
			    */
			}
			
			return command;
		
	}// -----------------------------------------------


	/**
	 * Retrieve the function name
	 * @param commandName
	 * @param context place holder
	 * @return the function information
	 * @see nyla.solutions.commas.medco.fabrix.grid.daf.Shell#getFunction(java.lang.String, java.lang.Object)
	 */
	public <ReturnType,ObjectType> Command<ReturnType,ObjectType> getCommand(String commandName, Object context)
	{
		return getCommand(commandName);
	
	}// -----------------------------------------------


	/**
	 * Execute a given function
	 * @param commandName the function to execute
	 * @param request the function input
	 * @return
	 * @see nyla.solutions.commas.medco.fabrix.grid.daf.Shell#executeFunction(java.lang.String, java.lang.Object)
	 */
	public Object executeCommand(String commandName, Object request)
	{
		Command<Object,Object> command = getCommand(commandName);
		
		return command.execute(request);
		
	}// -----------------------------------------------

	/**
	 * @return
	 * @see nyla.solutions.commas.medco.fabrix.grid.daf.Shell#getFunctions()
	 */
	public Collection<Command<?,?>> getCommands()
	
	{
		return shell.getCommands();
	}// -----------------------------------------------
	/**
	 * @return the securedToken
	 */
	protected SecuredToken getSecuredToken()
	{
		return securedToken;
	}// -----------------------------------------------

	private final SecuredToken securedToken;
	private final Shell shell;
	private final String name;
}
