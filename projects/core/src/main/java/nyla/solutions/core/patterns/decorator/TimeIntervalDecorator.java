package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.data.clock.TimeInterval;

/**
 * 
 * @author Gregory Green
 *
 */
public interface TimeIntervalDecorator
{

   /**
    * 
    * @param timeInterval
    */
   void decorator(TimeInterval timeInterval);
}
