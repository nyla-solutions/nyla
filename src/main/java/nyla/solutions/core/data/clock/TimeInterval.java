package nyla.solutions.core.data.clock;

import java.time.LocalDateTime;

public interface TimeInterval
{
   /**
    * @return the start
    */
   public abstract LocalDateTime getStartDate();
   // --------------------------------------------

   /**
    * @param start the start to set
    */
   public abstract void setStartDate(LocalDateTime start);
   //----------------------------------------------
   
   /**
    * @return the end
    */
   public abstract LocalDateTime getEndDate();
   // --------------------------------------------

   /**
    * @param end the end to set
    */
   public abstract void setEndDate(LocalDateTime end);
   // --------------------------------------------
}