package nyla.solutions.commas.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nyla.solutions.core.patterns.transaction.Transactional.TransactionType;


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
	 * @return the input name
	 */
	public String inputName() default "";
	
	/**
	 * 
	 * @return the name of the target
	 */
	public String targetName() default "";
	
	/**
	 * 
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
	 * @return the list of attributes
	 */
	public Attribute[] attributes() default {};
	
	/**
	 * 
	 * @return the input class
	 */
	public Class<?> inputClass() default Object.class;
	
	/**
	 * 
	 * @return the return class
	 */
	public Class<?> returnClass() default Object.class;
}