package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.data.Textable;

/**
 * TextDecorator interface to wrap a target object with additional text.
 * 
 * @param <DecoratorType> the decorator type
 * @author Gregory Green
 */
public interface TextDecorator<DecoratorType> extends Textable
{   
	/**
	 * Set the target object to wrap
	 * @param target the target
	 */
   public default void setTarget(DecoratorType target){}
   
   /**
    * 
    * @return the targeted object
    */
   public DecoratorType getTarget();
   

}
