package nyla.solutions.commas.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents generic meta-data
 * 
 * Exampe:
 * 
 * 	@CMD(advice=RmiOneRouteAdvice.ADVICE_NAME,
			attributes={@Attribute(name="sdsd")})
 * @author Gregory Green
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Attribute
{
	public String name()  default "";
	public String type() default "";
	public String value()  default "";
}