package nyla.solutions.spring.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import nyla.solutions.commas.Command;


/**
 * Execute a command during the before and after processing of a step
 * 
 * <step id="step1">
    <tasklet>
        <chunk reader="reader" writer="writer" commit-interval="10"/>
        <listeners>
            <listener ref="chunkListener"/>
        </listeners>
    </tasklet>
    </step>
    
    
    <bean id="GCIM_PREPROCESSOR" class="solutions.dao.spring.batch.CommandStepExecutionListener">
		<property name="beforeCommand" ref="GCIM_DELETE_PREVIOUS_COMMAND"/>
	</bean>
	<bean id="GCIM_DELETE_PREVIOUS_COMMAND" class="solutions.dao.executable.SqlExecutable">
		<property name="sql">
			<value>
				delete from INVENTORY where INV_SOURCE_CD = 'GCIM'
			</value>
		</property>
		<property name="dataSource" ref="dataSource"/>
	</bean>
 * @author Gregory Green
 *
 */
public class CommandStepExecutionListener implements StepExecutionListener
{
	/**
	 * Execute the before command
	 * @param stepExecution the step execution context
	 */
	@Override
	public void beforeStep(StepExecution stepExecution)
	{
		if(this.beforeCommand != null)
			this.beforeCommand.execute(null);
	}// --------------------------------------------------------
	
	/**
	 * Execute the after command
	 * @return  stepExecution.getExitStatus()
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution)
	{
		if(this.afterCommand != null)
			this.afterCommand.execute(null);
		
		return stepExecution.getExitStatus();
	}// --------------------------------------------------------
	
	/**
	 * @return the beforeCommand
	 */
	public Command<?, ?> getBeforeCommand()
	{
		return beforeCommand;
	}

	/**
	 * @param beforeCommand the beforeCommand to set
	 */
	public void setBeforeCommand(Command<?, ?> beforeCommand)
	{
		this.beforeCommand = beforeCommand;
	}

	/**
	 * @return the afterCommand
	 */
	public Command<?, ?> getAfterCommand()
	{
		return afterCommand;
	}

	/**
	 * @param afterCommand the afterCommand to set
	 */
	public void setAfterCommand(Command<?, ?> afterCommand)
	{
		this.afterCommand = afterCommand;
	}

	private Command<?,?> beforeCommand;
	private Command<?,?> afterCommand;
	

}
