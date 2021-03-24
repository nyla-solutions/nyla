package nyla.solutions.core.patterns.observer;

import nyla.solutions.core.data.clock.TimeInterval;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.decorator.TimeIntervalDecorator;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;

import java.time.LocalDateTime;

/**
 * 
 * <b>SubjectTimerObserver</b> is a observer to track the time between two subject events 
 * @author Gregory Green
 *
 */
public class SubjectTimerObserver implements SubjectObserver<Object>, TimeInterval
{
   /**
    * Constructor for SubjectTimerObserver initializes internal
    */
   public SubjectTimerObserver()
   {
   }//--------------------------------------------
   /**
    * 
    * Constructor for SubjectTimerObserver initializes internal 
    * @param timeIntervalDecorator
    */
   public SubjectTimerObserver(TimeIntervalDecorator timeIntervalDecorator)
   {
      this.decorator = timeIntervalDecorator;
   }//--------------------------------------------
   public void update(String subjectName, Object data)
   {
         Debugger.println(this,"Recieve subject="+subjectName);
         
         if(isStart(subjectName))
         {          
            this.startData = data;
            this.startDate = LocalDateTime.now();
            Debugger.printInfo(this,"TIMER START DATE ["+Text.formatDate(startDate)+"]\n "+Text.toString(startData));
            
         }
         else if(isEnd(subjectName))
         {            
            this.endData = data;
            this.endDate = LocalDateTime.now();
            
            Debugger.printInfo(this,"TIMER END DATE ["+endDate+"]\n "+endData);
            
            if(this.decorator != null)
               decorator.decorator(this);
         }   
         else
         {
            throw new SystemException("Unknown subject "+subjectName);
         }
   }//--------------------------------------------
    public boolean isStart(String name)
   {
      if(name == null || this.startSubjectNamePattern == null)
         return false;
      
      return Text.matches(name, this.startSubjectNamePattern);
   }//--------------------------------------------
   /**
    * 
    * @param name the subject name
    * @return true is subject name matches the regular expression
    */
   public boolean isEnd(String name)
   {
      if(name == null || this.endSubjectNamePattern == null)
         return false;
      
      return Text.matches(name, this.endSubjectNamePattern);
   }//--------------------------------------------
   /**
    * @return this.getClass().getName()+"START:"+startSubjectNamePattern+" END:"+endSubjectNamePattern
    * @see nyla.solutions.core.data.Identifier#getId()
    */
   public String getId()
   {
      return this.getClass().getName()+" START:"+startSubjectNamePattern+" END:"+endSubjectNamePattern;
   }//--------------------------------------------
   /**
    * @return the startSubjectNamePattern
    */
   public String getStartSubjectNamePattern()
   {
      return startSubjectNamePattern;
   }//--------------------------------------------
   /**
    * @param startSubjectNamePattern the startSubjectNamePattern to set
    */
   public void setStartSubjectNamePattern(String startSubjectNamePattern)
   {
      this.startSubjectNamePattern = startSubjectNamePattern;
   }//--------------------------------------------
   /**
    * @return the endSubjectNamePattern
    */
   public String getEndSubjectNamePattern()
   {
      return endSubjectNamePattern;
   }//--------------------------------------------
   /**
    * @param endSubjectNamePattern the endSubjectNamePattern to set
    */
   public void setEndSubjectNamePattern(String endSubjectNamePattern)
   {
      this.endSubjectNamePattern = endSubjectNamePattern;
   }//--------------------------------------------
   /**
    * @return the decorator
    */
   public TimeIntervalDecorator getDecorator()
   {
      return decorator;
   }
   /**
    * @param decorator the decorator to set
    */
   public void setDecorator(TimeIntervalDecorator decorator)
   {
      this.decorator = decorator;
   }//--------------------------------------------
   /**
    * @return the startDate
    */
   public LocalDateTime getStartDate()
   {
	   if(startDate == null)
		   return null;
	   
      return startDate;
   }//--------------------------------------------
   /**
    * @param startDate the startDate to set
    */
   public void setStartDate(LocalDateTime startDate)
   {
	   if(startDate == null)
		 {
		   this.startDate = null;
		   return;
		 }
	   
      this.startDate = startDate ;
   }//--------------------------------------------
   /**
    * @return the endDate
    */
   public LocalDateTime getEndDate()
   {	   
      return endDate;
   }//--------------------------------------------
   /**
    * @param endDate the endDate to set
    */
   public void setEndDate(LocalDateTime endDate)
   {
	   
      this.endDate = endDate;
   }//--------------------------------------------   
   /**
    * @return the startData
    */
   public Object getStartData()
   {
      return startData;
   }//--------------------------------------------
   /**
    * @param startData the startData to set
    */
   public void setStartData(Object startData)
   {
      this.startData = startData;
   }//--------------------------------------------
   /**
    * @return the endData
    */
   public Object getEndData()
   {
      return endData;
   }//--------------------------------------------
   /**
    * @param endData the endData to set
    */
   public void setEndData(Object endData)
   {
      this.endData = endData;
   }//--------------------------------------------

   private Object startData = null;
   private Object endData = null;
   private TimeIntervalDecorator decorator = null;
   private LocalDateTime startDate = null;
   private LocalDateTime endDate = null;
   private String startSubjectNamePattern = null;
   private String endSubjectNamePattern = null;
}
