package nyla.solutions.commas;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import nyla.solutions.commas.annotations.Aspect;
import nyla.solutions.commas.annotations.Attribute;
import nyla.solutions.commas.annotations.CMD;
import nyla.solutions.commas.annotations.COMMAS;
import nyla.solutions.commas.aop.Advice;
import nyla.solutions.core.exception.DataException;
import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.servicefactory.ServiceFactory;
import nyla.solutions.core.security.SecuredToken;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;

/**
 * <pre>
 * CommasServiceFactory implements service factory to create  GEDI COMMAndS.
 * Commas is short for COMMand AnnotationS. 
 * It is a Command registration framework based Annotations.
 * 
 *  
 * It is based on annotations. 
 * All Commas Functions must be annotated by the annotation solutions.gedi.commas.annotations.Service.
 * 
 * them with in a FUNCTION_CATALOG using annotations.
 * 
 * 
 * Configuration Properties
 * #Package root separate by spaces
 * nyla.solutions.global.patterns.command.commas.CommasServiceFactory.packageRoots=solutions.global.patterns.command.commas package2
 * </pre>
 * @author Gregory Green
 *
 */
public class CommasServiceFactory extends ServiceFactory
{

	/**
	 * DEFAULT_IS_CLIENT_CACHE = false 
	 */
	public static final boolean DEFAULT_IS_CLIENT_CACHE = false;
	
	/**
	 * Create an instance where for client-cache
	 */
	public CommasServiceFactory()
	{
		if(!lazyLoad)
			init();
	}// -----------------------------------------------
    /**
     * 
     * @return
     */
	public static synchronized CommasServiceFactory getCommasServiceFactory()
	{
		if(commasServiceFactory == null)
			commasServiceFactory = new CommasServiceFactory();
		
		return commasServiceFactory;
	}
	
	// -----------------------------------------------

	/**
	 * @return objMap.get(cls.getName());
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public <T> T create(Class<?> cls)
	{
		if(!initialized)
			init();
		
		return (T)objMap.get(cls.getName());
	}// -----------------------------------------------

	/**
	 * Return the object annotated with the given name
	 * @param the name of object
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T create(String name)
	{
		return (T)createCommand(name);
	}// -----------------------------------------------
	/**
	 * 
	 * @param names the command name
	 * @return the Macro command with the given commands
	 */
	public <ReturnType, InputType> MacroCommand<ReturnType, InputType> createCommandMacro(String[] names)
	{
		ArrayList<Command<ReturnType, InputType>> list = new ArrayList<Command<ReturnType, InputType>>();
		Command<ReturnType, InputType> cmd = null;
		for (int i = 0; i < names.length; i++)
		{
			cmd = createCommand(names[i]);
			list.add(cmd);
		}
		
		return new MacroCommand<ReturnType, InputType>(list);
		
	}// --------------------------------------------------------
	/**
	 * 
	 * @param name the command name
	 * @return the command with the given name
	 */
	@SuppressWarnings("unchecked")
	public <ReturnType,InputType> Command<ReturnType,InputType> createCommand(String name)
	{
		if(!initialized)
		init();
	
		Command<ReturnType,InputType> command = (Command<ReturnType,InputType>)objMap.get(name);
		
		if(command == null)
		{
			//Look by class name
			int lastPeriod = name.lastIndexOf(".");
			
			if(lastPeriod > -1)
			{
				String className =  name.substring(0, lastPeriod);
				try
				{
					command = ClassPath.newInstance(className);
					
					//save in map
					objMap.put(name,(Command<Object,Object>)command);
					
					//save shell by class name
					Shell shell = shellMap.get(className);
					
					if(shell == null)
					{
						ShellCommands shellCommands = new ShellCommands(className, name,command);
						shell = shellCommands;
						
					}
					else
					{
						((ShellCommands)shell).addCommand(name,command);
					}
					
					shellMap.put(className, shell);
					
				}
				catch(RuntimeException e)
				{
					Debugger.printWarn(this,e);
				}
			}
		}
		
		
		if(command == null)
			throw new SystemException("Command name:"+name+" not found in keySet:"+objMap.keySet());
		
		return command;
		
	}// --------------------------------------------------------

	/**
	 * Return the object annotated with the given name
	 * @param cls the class the name object is an instance of
	 * @param the name of object
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object create(Class cls, String name)
	{

		return cls.cast(objMap.get(name));
		
	}// -----------------------------------------------

	/**
	 * Create the Command by name
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object create(String name, Object[] arg1)
	{
		return create(name);
	}// -----------------------------------------------

	@SuppressWarnings("unchecked")
	@Override
	public Object create(String name, Object arg1)
	{
		return create(name);
	}// -----------------------------------------------
	
	/**
	 * Given a package root, scan it to obtain the list of {@link COMMAS} 
	 * annotations and process individually as a {@link FunctionFacts} fact and
	 * using it's name as the key for later access.
	 *  
	 * @param serviceFactory is the store which we can refer to later on
	 * @param packageRoot will be used as the base package to recursively scan
	 * @return the list of function facts that have been binded
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public synchronized static void init() 	
	{
	    init(getCommandFactsDAO());
	}// -----------------------------------------------
	
	public static CommandFactsDAO getCommandFactsDAO()
	{
	  return commasDAO;
	  
	}// --------------------------------------------------------
	protected synchronized static void init(CommandFactsDAO functionFactsDAO) 	
	{
		CommasServiceFactory.functionFactsDAO = functionFactsDAO;
		
		try
		{
			Debugger.println(CommasServiceFactory.class,"Loading packages "+Arrays.asList(packageRoots));
			
			ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
			
			String[] p = packageRoots;	
			
			for (int i = 0; i < p.length; i++)
			{
				String thePackage = p[i];
				
				configurationBuilder = configurationBuilder.addUrls(ClasspathHelper.forPackage(thePackage));
			}
			
			//Reflections reflections = new Reflections(packageRoots);
			Reflections reflections = new Reflections(configurationBuilder);
			
			Map<String, Command<Object,Object>> classMap = new HashMap<String, Command<Object,Object>>(batchSize);
			
			
			//Init advice
			Set<Class<?>> aspectAdviceClasses = reflections.getTypesAnnotatedWith(Aspect.class);
			
			Advice advice = null;
			Aspect aspectAdvice = null;
			HashMap<String, Advice> adviceMap = new HashMap<String, Advice>(aspectAdviceClasses.size());
			for (Class<?> adviceClass : aspectAdviceClasses)
			{
				aspectAdvice = adviceClass.getAnnotation(Aspect.class);
				advice = (Advice)adviceClass.newInstance();
				
				//Save advice
				adviceMap.put(aspectAdvice.name(),advice);
			}
			

			Set<Class<?>> commasClasses = reflections.getTypesAnnotatedWith(COMMAS.class);
			
			Method[] methods = null;
			COMMAS service;
			for (Class<?> serviceClass : commasClasses)
			{
				service = serviceClass.getAnnotation(COMMAS.class);
				methods = serviceClass.getMethods();
				for (Method method : methods)
				{
					//find real method
					initCommand(service, method,classMap,adviceMap);	
				}
			}
			
			initialized = true;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SetupException(e);
		}
	}// -----------------------------------------------
	/**
	 * Initialize a command
	 * @param method the method/command
	 * @param classMap the in memory created services
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static void initCommand(COMMAS commas, Method method, 
			Map<String, Command<Object,Object>> classMap, Map<String, Advice> adviceMap) 
	throws InstantiationException, IllegalAccessException
	{
		
		
		//On Server
		CMD cmdAnnotation = method.getAnnotation(CMD.class);
		String commasName = commas.name();
		
		
		if(cmdAnnotation == null)
		   return; //do nothing
		
			Debugger.println(CommasServiceFactory.class,"loading method:"+method.getName());
			
			CommandFacts commandFacts = new CommandFacts();
			
			commandFacts.setCommasName(commasName);
			commandFacts.setTargetName(cmdAnnotation.targetName());
			
			commandFacts.setControllerLocation(cmdAnnotation.controller());
			
			String cmdName = cmdAnnotation.name();

			commandFacts.setCommandName(toCommandName(commasName, cmdName,method));
			
			if(cmdName == null || cmdName.length() == 0)
				cmdName = method.getName(); //use method name is name annotation not populated
			
			commandFacts.setSimpleName(cmdName);
			
			commandFacts.setNotes(cmdAnnotation.notes());
			commandFacts.setTransactionType(cmdAnnotation.transactionType());
			
			
			Class<?>[] parameterTypes = method.getParameterTypes();
			Class<?> parClass;
			if(parameterTypes != null && parameterTypes.length > 0)
			{
				parClass = method.getParameterTypes()[0];
				
				//check if Object
				if(Object.class == parClass)
				{
					//check annotation
					parClass = cmdAnnotation.inputClass();
					
				}
				
				//if(!AbstractCatalog.isHidden(parClass))
				//{
					//has one parameter (not hidden)
					//Note that a single function with a FunctionContext
					//argument will have its argument hidden
					commandFacts.setArgumentClassInfo(new CatalogClassInfo(parClass,method.getGenericParameterTypes()));
				//}
					
			}
			
			
			Class<?> returnClass = method.getReturnType();
			
			if(returnClass == Object.class)
			{
				//check annotation
				returnClass = cmdAnnotation.returnClass();
			}
			
			commandFacts.setReturnClassInfo(new CatalogClassInfo(returnClass,method.getGenericReturnType()));
		
			commandFacts.setCommandAttributes(toCommandAttributes(cmdAnnotation.attributes()));
			
			constructObject(commandFacts,cmdAnnotation.aliases(), method,classMap,adviceMap, cmdAnnotation.advice());
			
			CommasInfo commasInfoBag = commasInfoMap.get(commasName);
			
			if(commasInfoBag == null)
			{
				commasInfoBag = new CommasInfo(commasName);
				commasInfoMap.put(commasName, commasInfoBag);
			}
			
			commasInfoBag.addFact(commandFacts);
			
			
	}// -----------------------------------------------

	
	/**
	 * 
	 * @param attributes the function's annotations
	 * @return function attribute
	 */
	private static CommandAttribute[] toCommandAttributes(Attribute[] attributes)
	{
		if(attributes == null )
			return null;
		
		int len = attributes.length;
		
		if(attributes.length == 0)
			return null;
			
		
		CommandAttribute[] functionAtttributes = new CommandAttribute[len];
		
		for (int i=0;i < len;i++)
		{
			
			functionAtttributes[i] = new CommandAttribute(attributes[i].name(),
					attributes[i].type(),
					attributes[i].value());
		}
		
		return functionAtttributes;
	}// -----------------------------------------------
	/**
	 * Build the full path function name
	 * @param serviceName the service name
	 * @param functionName the function name
	 * @param method the method
	 * @return the full function name
	 */
	public static String toCommandName(String serviceName, String functionName)
	{
		if(serviceName == null || serviceName.length() == 0)
		{
			//use just the function
			return functionName;
		}
		
		if(functionName.contains(CommasConstants.nameSeparator)
			&& functionName.startsWith(serviceName))
			return functionName; //function name already contains the service name
		
		return new StringBuilder()
		.append(serviceName)
		.append(CommasConstants.nameSeparator)
		.append(functionName).toString();
	}// -----------------------------------------------
	/**
	 * Build the full path function name
	 * @param commasName the service name
	 * @param cmdName the function name
	 * @param method the method
	 * @return the full function name
	 */
	public static String toCommandName(String commasName, String cmdName, Method method)
	{
		if(commasName == null || commasName.length() == 0)
		{
			//check if functionName provided
			if(cmdName == null || cmdName.length() == 0)
			{
				//service name will be the class name
				commasName = method.getDeclaringClass().getName();
				
				//function will be the method name
				cmdName = method.getName();
				
			}
		}
		else if(cmdName == null || cmdName.length() == 0)
		{
			//service name provide by the function name was not provided
			cmdName = method.getName();
		}
		
		return toCommandName(commasName, cmdName);
	}// ---------------------------------------------
	/**
	 * Create new instance of the given class
	 * @param clazz
	 * @return the object based on the class name
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static synchronized void constructObject(CommandFacts commandFacts, String[] aliases, 
			Method method, Map<String, Command<Object,Object>> classMap, Map<String, Advice> adviceMap, String adviceName) 
	throws InstantiationException,
			IllegalAccessException
	{
		Debugger.println(CommasServiceFactory.class,"constructObject(functionFacts:"+commandFacts+")");
		
		
		Class<?> clazz = method.getDeclaringClass();
		
		String className = clazz.getName();
		Command<Object,Object> command = classMap.get(commandFacts.getName());
		
		Object obj = null;
		CommasProxyCommand commasProxyCommand  = null;
		
		if(command == null)
		{
			//Create new
			obj = clazz.newInstance();
			
			if(obj instanceof Command)
				command = (Command<Object,Object>)obj;
		}
		else
		{
			obj = command;
		}
		
		//Test if object instance of Command
		if(!(obj instanceof Command))
		{
			Debugger.println(CommasServiceFactory.class,new StringBuilder("wrapping into commas proxy class:").append(className));
			
			commasProxyCommand = new CommasProxyCommand(obj,method,commandFacts);
			
			command = (Command)commasProxyCommand;
			
		}
		else if(obj instanceof CommasProxyCommand)
		{
			commasProxyCommand = (CommasProxyCommand)obj;
			command = (Command)commasProxyCommand;
		}

		
		//Class<?>[] parameterTypes = method.getParameterTypes();
		
		
		if(adviceName != null && adviceName.length() > 0)
		{
			//Get Advice
			Advice advice = adviceMap.get(adviceName);
			
			if(advice == null)
				throw new SystemException("Advice:"+adviceName+" not found in in keyset:"+adviceMap.keySet());
			
			//Set function facts
			advice.setFacts(commandFacts);
			
			//wrap Advice 
			if(commasProxyCommand == null)
			{
				commasProxyCommand = new CommasProxyCommand(command,method,commandFacts);
				command = (Command)commasProxyCommand;
			}
			
			//Set before command based on advice
			commasProxyCommand.setBeforeCommand((Command)advice.getBeforeCommand());
			
			//Set after command based on advice
			commasProxyCommand.setAfterCommand((Command)advice.getAfterCommand());
			
		}		
		
		classMap.put(commandFacts.getName(), command);
		objMap.put(commandFacts.getName(),command);
		
		//save shell
		//for shell
		
		
		functionFactsDAO.saveByKey(commandFacts.getName(), commandFacts);
		
		//save each alias
		for (String alias : aliases)
		{
			objMap.put(alias,command);
			functionFactsDAO.saveByKey(alias, commandFacts);
		}
	}// -----------------------------------------------
	/**
	 * The function facts
	 * @param commandName the function name
	 * @return the Function fact that matches a function name
	 */
	public CommandFacts getCommandFacts(String commasName,String cmdName)
	{
		return getCommandFacts(toCommandName(commasName, cmdName));
	}// --------------------------------------------------------
	/**
	 * The function facts
	 * @param commandName the function name
	 * @return the Function fact that matches a function name
	 */
	public CommandFacts getCommandFacts(String commandName)
	{
		if(commandName == null || commandName.length() == 0)
			throw new RequiredException("commandName");
		
		CommandFacts facts = functionFactsDAO.findFactsByKey(commandName);
		
		if(facts == null)
			throw new DataException("No facts found with name:\""+commandName+"\" in set:"+functionFactsDAO.selectFactKeys());
		
		return facts;
	}// -----------------------------------------------
	/**
	 * 
	 * @param commasName commas name
	 * @return the commands information
	 */
	public CommasInfo getCommasInfo()
	{
		return getCommasInfo(CommasConstants.ROOT_SERVICE_NAME);
	}// --------------------------------------------------------
	
	/**
	 * 
	 * @param commasName commas name
	 * @return the commands information
	 */
	public CommasInfo getCommasInfo(String commasName)
	{
		return commasInfoMap.get(commasName);
	}// --------------------------------------------------------
	/**
	 * 
	 * @return the catalog
	 */
	public static synchronized Catalog getCatalog()
	{

			if(catalog == null)
				catalog = (Catalog)ClassPath.newInstance(catalogCommasClassName);
			
			return catalog;
		
	}// --------------------------------------------------------
	/**
	 * 
	 * @return commas meta -data
	 */
	public Collection<CommasInfo> getCommasInfos()
	{
		 Collection<CommasInfo> results = new HashSet<CommasInfo>(commasInfoMap.values());
		 
		 return results;
	}// --------------------------------------------------------
	/**
	 * Get the crate based on an interface
	 * 
	 * @param crateName the crate name
	 * @return the Crate of the interface
	 */
	public <T> T createShell(Class<T> instance)
	{
		return createShell(instance.getName(),instance);
	}// --------------------------------------------------------
	/**
	 * Get the crate based on an interface
	 * 
	 * @param crateName the crate name
	 * @return the Crate of the interface
	 */
	@SuppressWarnings("unchecked")
	public <T> T createShell(String commasName, Class<T> instance)
	{
		

		ShellMgr shellMgr = getShellMgr(new ShellIdentifier(commasName,
					this.getSecuredToken()));
			Class<?>[] interfaces =
			{ instance };

			return (T) Proxy.newProxyInstance(instance.getClassLoader(),
					interfaces, shellMgr);

       
	}// -----------------------------------------------
	/**
	 * 
	 * @param identifier the identifier containing the shell name
	 * @return the matching shell
	 */
	private ShellMgr getShellMgr(ShellIdentifier identifier)
	{
		// check if crate in cache
		ShellMgr shellMgr = shellMgrMap.get(identifier);
		if (shellMgr != null)
			return shellMgr;

		String shellName = identifier.getShellName();


		SecuredToken securedToken = identifier.getSecuredToken();
		Shell shell = shellMap.get(shellName);
		
		if(shell == null)
			throw new NoDataFoundException("shellName:"+shellName+" in keySey"+shellMap.keySet());
		
		// wrapped crate with manager for token
		shellMgr = new ShellMgr(shellName, shell, securedToken);

		// put shell in cache
		shellMgrMap.put(identifier, shellMgr);

		return shellMgr;

	}// --------------------------------------------------------
	
	/**
	 * @return the secured token for this connection
	 */
	public SecuredToken getSecuredToken()
	{
		return this.securedToken;
	}// -----------------------------------------------
	private SecuredToken securedToken = null;
	
	private static Catalog catalog = null;
	private static String catalogCommasClassName = Config.getProperty(
				CommasServiceFactory.class,
					"catalogCommasClassName",
						CatalogCommas.class.getName());
	
	private static MapCommandFactsDAO commasDAO = new MapCommandFactsDAO();
	private static CommasServiceFactory commasServiceFactory = null;	
	private static boolean initialized = false; 
	//private static boolean isClientCache = Config.getPropertyBoolean(CommasServiceFactory.class,"isClientCache", DEFAULT_IS_CLIENT_CACHE).booleanValue();
	private static int batchSize = Config.getPropertyInteger(CommasServiceFactory.class,"batchSize", 50);
	private static CommandFactsDAO functionFactsDAO = null;
	private static String[] packageRoots = Config.getPropertyStrings(CommasServiceFactory.class,"packageRoots",CommasConstants.DEFAULT_PACKAGE_ROOT);
	private static Map<String, Command<Object,Object>> objMap = new HashMap<String,Command<Object,Object>>();
	private Map<ShellIdentifier, ShellMgr> shellMgrMap = new HashMap<ShellIdentifier, ShellMgr>();
	
	private static Map<String, Shell> shellMap = new HashMap<String, Shell>();
	
	private static HashMap<String,CommasInfo> commasInfoMap = new HashMap<String,CommasInfo>();
	private static boolean lazyLoad = Config.getPropertyBoolean(CommasServiceFactory.class, "lazyLoad",false).booleanValue();
}
