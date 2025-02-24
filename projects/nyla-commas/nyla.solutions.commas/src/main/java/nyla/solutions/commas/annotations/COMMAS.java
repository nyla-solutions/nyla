package nyla.solutions.commas.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that the execute method for this function
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface COMMAS
{
	/**
	 * 
	 * @return service name
	 */
	public String name() default "";
	
	/**
	 * 
	 * @return alias names for the function
	 */
	public String[] aliases() default {};
	
}