# COMMAS
	

The **COMMAS** stands for **COMM**and **A**nnotation**S**.  COMMAS is a service/method execution framework that allows before and after advice to be applied during method execution. Each methods is converted into a a Command interface.  The before and after advice is simulates a light weight Aspect Oriented Programming (AOP) framework.


Each command is registered with the CommasServiceFactory. Add the following to the config.properties to configuration the CommasServiceFactory.

	
The Commas service factory looks for Command classes under a provided list of package roots. The package roots are set in the config.properties. The following is an example of the property. Note that each package root is separated by spaces.

If no packages are specified in config properties then the default is package nyla.solutions.commas will be used;

	#Configuration Properties
	#Package root separate by spaces
	nyla.solutions..commas.CommasServiceFactory.packageRoots=demo.package1 demo.package2

All Commas object classes must be given the @COMMAS annotation.

	import solutions.gedi.commas.annotations.Service;
	@COMMAS
	public class DemoRmiService implements Command


The following is the Command interface 

	package nyla.solutions.core.patterns.command;
	/**
	 * This is an abstract interface to execute an command operation.
	 *
	 */
	public interface Command<ReturnType  extends Object,InputType extends Object>
	{
		/**
		 * Implemented command interface to execute an operation on a argument and possibility return values
		 * @param input the input object 
		 * @return the altered result
		 */
		ReturnType execute(InputType input);
	
	}

The framework supports classes that implement the Command interface or POJOs.

**Performance Tip:** Object commands that implement the Command interface will generally perform faster than POJOs. All POJOs are executed using proxy reflections. This adds additional execution overhead.

	@COMMAS
	public class SayHelloService implements Command<String,Object>
	{
		@CMD(name="sayHello")
		@Override
		public String execute(String name)
		{
		
			return "Hello "+name;
		}
	}
	
The @COMMAS annotation is used at a object level to determine which objects may have its methods converted to Commands with advice injected into its execution. 



**Example Code**

    package nyla.solutions.core.demo.commas;
	
	import java.util.ArrayList;
	import java.util.Collection;
	
	import nyla.solutions.core.data.Criteria;
	import nyla.solutions.core.patterns.command.commas.annotations.Attribute;
	import nyla.solutions.core.patterns.command.commas.annotations.CMD;
	import nyla.solutions.global.patterns.command.commas.annotations.COMMAS;
	import nyla.solutions.global.patterns.command.remote.partitioning.RmiAllRoutesAdvice;
	import nyla.solutions.global.patterns.command.remote.partitioning.RmiOneRouteAdvice;
	import nyla.solutions.global.security.user.data.User;
	import nyla.solutions.global.security.user.data.UserProfile;
	import nyla.solutions.global.util.Debugger;

	@COMMAS
	public class RealSingleRouteCommand
	{
		@CMD(advice=RmiOneRouteAdvice.ADVICE_NAME,
				targetName="molecules",
				attributes={@Attribute(name=RmiOneRouteAdvice.LOOKUP_PROP_ATTRIB_NAME,value="primaryKey")})
		public Collection<User> findUsers(Criteria criteria)
		{
			Debugger.println(this,"finderUsers("+criteria+")");
			ArrayList<User> users = new ArrayList<User>();
			
			User nyla = new UserProfile("nyla@emc.com","nyla","Nyla","Green");
			
			users.add(nyla);
			
			return users;
		}
	
		
		@CMD(advice=RmiAllRoutesAdvice.ADVICE_NAME)
		public Collection<User> findUsersEveryWhere(Criteria criteria)
		{
			Debugger.println(this,"finderUsers("+criteria+")");
			ArrayList<User> users = new ArrayList<User>();
			
			String serverName = System.getProperty(RealSingleRouteCommand.class.getName()+".findUsersEveryWhere.serverName");
			User nyla = new UserProfile("nyla@"+serverName,"nyla","Nyla","Green");
			users.add(nyla);
			
			return users;
		}
	}

Each @COMMAS annotated object corresponds to a namespace. The default namespace contains the package.className. In the example above the name default is `nyla.solutions.global.demo.commas.RealSingleRouteCommand`.
Use the name attribute to change the namespace. Example @COMMAS(name=”nameOfService”). 

Each method can be given an @CMD annotation.
The method level annotation (@CMD) also have a name attribute. This name matches the registered Command method name. The exact method name is used if the name attribute is not provided. In the following example, the full command name is *nyla.solutions.global.demo.commas.findUsers".

	@CMD(advice=RmiOneRouteAdvice.ADVICE_NAME,
					targetName="molecules",
					attributes={@Attribute(name=RmiOneRouteAdvice.LOOKUP_PROP_ATTRIB_NAME,value="primaryKey")})
	public Collection<User> findUsers(Criteria criteria)



The following is definition for the CMD annotation.

	package nyla.solutions.global.patterns.command.commas.annotations;
	...
	/**
	 * Indicates that the execute command method
	 * 
	 * @author Gregory Green
	 */
	@Target({ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	@Documented
	public @interface CMD
	{
		/** The unique name .  Please note that  names can be overwritten by 
		 * other modules with the same name.
		 * @return the module name
		 */
		public String name() default "";
		
		/**
		 * Placeholder for a controller method execution pattern 
		 * 
		 * Default "controller"
		 * @return @see controller
		 */
		public String controller() default "controller";
		
		/**
		 * The placeholder for an input container
		 * @return
		 */
		public String inputName() default "";
		
		/**
		 * @return the name of the target
		 */
		public String targetName() default "";
		
		/**
		 * @return alias names for the methods
		 */
		public String[] aliases() default {};
		
		/**
		 * Description of command method
		 */
		public String notes() default "";
	
		/**
		 * Transaction Type
		 * NONE - no transaction support
		 * READONLY- read (not write) transaction
		 * WRITE - read/write or read transaction data
		 * @return default WRITE
		 */
		public TransactionType transactionType() default TransactionType.WRITE;
		
		/**
		 * Aspect advice name
		 */
		public String advice()  default "";
		
		/**
		 * Command method attributes
		 * @return
		 */
		public Attribute[] attributes() default {};
		
		/**
		 * @return the input class
		 */
		public Class<?> inputClass() default Object.class;
		
		/**
		 * @return the return class
		 */
		public Class<?> returnClass() default Object.class;
	}
	
COMMAS advice is a convention that allows developers to introduce AOP like concerns into Command(s). Developers can introduce concerns such as transformation, security and auditing by implementing the Advice interface. The advice for a method command is specified by "advice" attribute in the CMD annotations.

	@CMD(name="FUNCTION_NAME", advice ="ADVICE_NAME_HERE")
	public Object execute(Object envAndFuncContextArray)
	{
	}

This interface allows before and after processing to be injected for a command. The before and after are represented by objects that implement the Command interface. Each advice will be provided  meta-data by CommasServiceFactory for the Command that it will give advice by the setFacts(CommandFacts) method.

See the Advice interface below.

	package nyla.solutions.global.patterns.aop;
	
	import nyla.solutions.global.patterns.command.Command;
	import nyla.solutions.global.patterns.command.commas.CommandFacts;
	
	/**
	 * Aspect similar to AOP based
	 *
	 */
	public interface Advice
	{
	
		/**
		 * Before advice in the form  a command
		 * @return
		 */
		Command<?,?> getBeforeCommand();
		
		/**
		 * After advice in the form  a command
		 * @return
		 */
		Command<?,?> getAfterCommand();
		
		/**
		 * 
		 * @return the function facts
		 */
		CommandFacts getFacts();
		
		/**
		 * 
		 * @param facts the function facts
		 */
		void setFacts(CommandFacts facts);
	}

The name of the Advice is specified through the Aspect annotation

	package nyla.solutions.global.patterns.command.commas.annotations;
	
	import java.lang.annotation.Documented;
	import java.lang.annotation.ElementType;
	import java.lang.annotation.Inherited;
	import java.lang.annotation.Retention;
	import java.lang.annotation.RetentionPolicy;
	import java.lang.annotation.Target;
	
	@Target({ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	@Documented
	public @interface Aspect
	{
	   String name() default "";
	}