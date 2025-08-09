package nyla.solutions.core.patterns.observer;

import nyla.solutions.core.util.Debugger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


/**
 * 
 * <b>SubjectRegistry</b> is an object/mapping of multiple topics and observers
 * @author Gregory Green
 *
 */
public class  SubjectRegistry
{
    private Map<String,Subject<?>> registry = new HashMap<String,Subject<?>>();

   /**
    * Notify a given subject
    * @param subjectName the subject name
    * @param data the information to send
    */
   @SuppressWarnings("unchecked")
   public void notify(String subjectName, Object data)
   {
      //get subject
      Object object = this.registry.get(subjectName);
      
      if(object == null)
         return; //nobody to update
      
      try
      {
         Subject<Object> subject = (Subject<Object>)object;
         subject.notify(data);
      }
      catch (ClassCastException e)
      {
         throw new IllegalArgumentException(object.getClass().getName()+" "+Debugger.stackTrace(e));
      }
      
   }// --------------------------------------------

    public void notifyAll(Object data)
    {


        Consumer<? super Subject<Object>> consumer = subject -> subject.notify(data);
        this.registry.values().forEach((Consumer<? super Subject<?>>) consumer);
    }
   /**
    * Add the subject Observer (default Topic implementation may be used)
    * @param <T> the class type
    * @param subjectName the subject name
    * @param subjectObserver the subject observer
    */
   @SuppressWarnings("unchecked")
   public <T> void register(String subjectName, SubjectObserver<T> subjectObserver)
   {
      Subject<?> subject = (Subject<?>)this.registry.get(subjectName);
      
      if(subject == null)
         subject = new Topic<T>(subjectName);
      
      register(subjectName, subjectObserver, (Subject<T>)subject);
      
   }// --------------------------------------------
   /**
    * Add subject observer to a subject
    * @param <T> the class type
    * @param subjectName the subject name
    * @param subjectObserver the subject observer
    * @param subject the subject to add the observer
    */
   public <T> void register(String subjectName,
                                   SubjectObserver<T> subjectObserver,
                                   Subject<T> subject)
   {
      subject.add(subjectObserver);
      
      this.registry.put(subjectName, subject);
   }// --------------------------------------------
   /**
    * Remove an observer for a registered observer
    * @param <T> the class type
    * @param subjectName the subject name to remove
    * @param subjectObserver the subject observer
    */
   @SuppressWarnings({ "unchecked", "rawtypes" })
  public <T> void removeRegistration(String subjectName, SubjectObserver<T> subjectObserver)
   {
      Subject subject = (Subject)this.registry.get(subjectName);
      
      if(subject == null)
         return;
      
      subject.remove(subjectObserver);
      
   }// -------------------------------------------- 
   
   
   /**
    * @return the registry
    */
   public Map<String,Subject<?>> getRegistry()
   {
      return registry;
   }
   /**
    * @param registry the registry to set
    */
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public void setSubjectObservers(Map<Object,SubjectObserver> registry)
   {
      //loop thru registry 
      
      for (Map.Entry<Object, SubjectObserver> entry: registry.entrySet())
      {
         
           this.register(entry.getKey().toString(), entry.getValue());
     
      }//end for check
   }// --------------------------------------------------------
   


}
