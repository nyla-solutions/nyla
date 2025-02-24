package nyla.solutions.commas;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.util.Debugger;

/**
 * Execute a given executable in a separate thread.
 * 
 * Schedules the specified task for repeated  fixed-rate or based on timer (one time) execution. 
 * 
 * Fix Rate executions take place at intervals, separated by the specified period.
 * In fixed-rate execution, each execution is scheduled relative to the scheduled execution time of the initial execution. 
 * 
 * Fixed-rate execution is appropriate for recurring activities that are sensitive to absolute time. 
 * 
 * 
 * Parameters
 * delay - delay in milliseconds before task is to be executed.
 * firstTime - First time at which task is to be executed.
 * period - time in milliseconds between successive task executions.
 * 
 	<bean id="exe_every 5_secs" class="solutions.global.patterns.command.TimerTaskExecutable">
 			<property name="name" value="Every 5 SEC"/>
			<property name="executable">
			     <ref bean="sample_executable" />
			</property>
			
			<property name="period" value="5000"/>
			<property name="delay" value="1"/>
	</bean>
 * 
 * Note executable must be thread safe
 * @author Gregory Green
 *
 */
public class TimerTaskExecutable extends TimerTask implements Executable, CloneableExecutable
{
   /**
    * 
    * @see nyla.solutions.commas.Executable#execute(nyla.solutions.core.data.Environment, java.lang.String[])
    */
   public Integer execute(Environment env)
   {
	if (this.executable == null)
	   throw new RequiredException("this.executable");
	
	this.env = env;
	
	if(args == null)
		this.args = null;
	else
		this.args = args.clone();

	Timer timer = null;
	
	if(this.name != null && this.name.length() > 0)
	{
	   timer = new Timer(this.name,this.daemon);
	}
	else
	{
	   timer = new Timer(this.daemon);
	}
	
	if(this.atFixedRate)
	{
	   scheduleAtfixedRate(timer);
	}
	else
	{
	   schedule(timer);
	}
	
	return 0;
   }// ----------------------------------------------
   public Object clone()
   throws CloneNotSupportedException
   {
	TimerTaskExecutable copy = (TimerTaskExecutable)super.clone();
	
	copy.name = this.name;
	
	if(this.executable instanceof CloneableExecutable)
	{
	   copy.executable = (Executable)((CloneableExecutable)this.executable).clone();
	}
	
	return copy;
	   
   }// ----------------------------------------------
   
   /**
    * 
    * @param timer the timer rate
    */
   private void scheduleAtfixedRate(Timer timer)
   {

	if(this.delay > -1 && this.period > -1)
	{
	   timer.scheduleAtFixedRate(this,this.delay,this.period);
	}
	else if(this.firstTime != null && this.period > -1)
	{
	   timer.scheduleAtFixedRate(this,this.firstTime,this.period);
	}
	else
	{
	   StringBuffer message = new StringBuffer("Invalid schedule ").append(this.toString());
	   throw new IllegalStateException(message.toString());
	}
   }// ----------------------------------------------
   /**
    * 
    * @param timer the timer 
    */
   private void schedule(Timer timer)
   {
	if(this.delay > -1 && this.period > -1)
	{
	   timer.schedule(this,this.delay,this.period);
	}
	else if(this.firstTime != null && this.period > -1)
	{
	   timer.schedule(this,this.firstTime,this.period);
	}
	else if(this.time != null)
	{
	   timer.schedule(this,this.time);
	}
	else if(this.delay > -1)
	{
	   timer.schedule(this, delay);
	}
	else
	{
	   StringBuffer message = new StringBuffer("Invalid schedule ").append(this.toString());
	   throw new IllegalStateException(message.toString());
	}
   }// ----------------------------------------------
   /**
    * Run the executable's execute method
    */
   public void run()
   {
	Debugger.println(this,"Executing the timer task");
	executable.execute(env);
   }// ----------------------------------------------
   /**
    * @return the firstTime
    */
   public Date getFirstTime()
   {
	   if(firstTime == null)
		   return null;
	   
      return (Date)firstTime.clone();
   }
   /**
    * @param firstTime the firstTime to set
    */
   public void setFirstTime(Date firstTime)
   {
	   if(firstTime == null)
		   this.firstTime = null;
	   else
		   this.firstTime = new Date(firstTime.getTime());
	   

   }
   /**
    * 
    * @return the period (milliseconds) between scheduled executes
    */
   public long getPeriod()
   {
      return period;
   }
   /**
    * @param period the period (milliseconds) to set
    */
   public void setPeriod(long period)
   {
      this.period = period;
   }
   /**
    * @return the time
    */
   public Date getTime()
   {
	   if(time == null)
		   return null;
	   
      return new Date(time.getTime());
   }
   /**
    * @param time the time to set
    */
   public void setTime(Date time)
   {
	   if(time == null)
		   this.time = null;
	   else
		   this.time = new Date(time.getTime());
   }
   /**
    * @return the delay from starting
    */
   public long getDelay()
   {
      return delay;
   }
   /**
    * @param delay the delay to set (for starting the task)
    */
   public void setDelay(long delay)
   {
      this.delay = delay;
   }
   /**
    * @return the executable
    */
   public Executable getExecutable()
   {
      return executable;
   }
   /**
    * @param executable the executable to set
    */
   public void setExecutable(Executable executable)
   {
      this.executable = executable;
   }
   /**
    * @return the atFixedRate
    */
   public boolean isAtFixedRate()
   {
      return atFixedRate;
   }
   /**
    * @param atFixedRate the atFixedRate to set
    */
   public void setAtFixedRate(boolean atFixedRate)
   {
      this.atFixedRate = atFixedRate;
   }// ---------------------------------------------- 
   /**
    * Return text version of the TimerTaskExecutable
    */
   public String toString()
   {
      StringBuffer text = new StringBuffer(this.getClass().getName())
      .append(" ")
      .append(" atFixedRate=").append(atFixedRate)
      .append(" firstTime=").append(firstTime)
      .append(" period=").append(period)
      .append(" time=").append(time)
      .append(" delay=").append(delay);
      
      return text.toString();
   }// ----------------------------------------------
   
   /**
    * @return the daemon
    */
   public boolean isDaemon()
   {
      return daemon;
   }
   /**
    * @param daemon the daemon to set
    */
   public void setDaemon(boolean daemon)
   {
      this.daemon = daemon;
   }
   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }
   /**
    * @param name the name to set
    */
   public void setName(String name)
   {
      this.name = name;
   }

   private boolean daemon= false;
   private String name = null;
   private boolean atFixedRate = false;
   private Date firstTime = null;
   private long period = -1;
   private Date time = null;
   private long delay = -1;
   private Environment env = null;
   private String[] args = null;
   private Executable executable = null;
}
