package nyla.solutions.commas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.RequiredException;

/**
 * <pre>
 * MacroExecutable implementation of the Executable 
 * based on the command design pattern.
 * Sample Spring Configuration
 * 
 * 	<bean id="scenarioA" class="solutions.global.patterns.command.MacroExecutable">
			<property name="executables">
				 <list>			         
			         <ref bean="sss1" />
			         <ref bean="sss2" />
			      </list>
			</property>
	</bean>
 * </pre>
 * @author Gregory Green
 *
 */
public class MacroExecutable implements Executable, CloneableExecutable
{

	/**
	 * Execute all given executables
	 */
	public Integer execute(Environment env)
	{
		if(this.executables == null)
			throw new RequiredException("Executable not set on "+this.getClass().getName());
		
		synchronized(executables)
		{
			//execute each executable
			for(Iterator<Executable> i = this.executables.iterator();i.hasNext();)
			{
				((Executable)i.next()).execute(env);
			}		   
		}
		
		
		return 0;
	}//---------------------------------------------
	/**
	 * @return the executables
	 */
	public Collection<Executable> getExecutables()
	{
		return executables;
	}//---------------------------------------------
	/**
	 * @param executables the executables to set
	 */
	public void setExecutables(Collection<Executable> executables)
	{
		if(executables == null)
			throw new RequiredException("executables in MacroExecutable");		
		
		this.executables = executables;
	}//---------------------------------------------
	/**
	 * @return copy of MacroExecutable
	 */
	public Object clone() throws CloneNotSupportedException
	{
      	MacroExecutable copy = (MacroExecutable)super.clone();
      	if(this.executables != null)
      	{
      	   copy.executables = new ArrayList<Executable>(this.executables);      	 
      	}
      	
      	return copy;
      	
	}// ----------------------------------------------
	private Collection<Executable> executables = null;
}
