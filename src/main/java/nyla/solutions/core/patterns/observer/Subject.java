package nyla.solutions.core.patterns.observer;

import nyla.solutions.core.data.Nameable;

/**
 * 
 * <b>Subject</b> Send notify signal to observer object whenever data changes
 * @author Gregory Green
 * @param <T> the update type
 *
 */
public interface Subject<T> extends Nameable
{
	/**
	 * The subject name
	 * @return the class name
	 */
	public default String getName()
	{
		return getClass().getName();
	}//------------------------------------------------
   /**
    * 
    * @param observer the observer to add
    */
   public void add(SubjectObserver<T> observer);
     
   /**
    * 
    * @param observer the observer to 
    */
   public void remove(SubjectObserver<T> observer);
      
   /**
    * 
    * @param object notify all observers
    */
   void notify(T object);
}
