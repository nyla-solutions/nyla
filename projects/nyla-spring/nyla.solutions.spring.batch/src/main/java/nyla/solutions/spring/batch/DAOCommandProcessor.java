package nyla.solutions.spring.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import nyla.solutions.core.util.Debugger;
import nyla.solutions.dao.patterns.command.DAOCommand;


/**
 * This object used the the getBoolean method to determine .
 * 
 * Example usage
 * <bean id="STRUCTURES_MCIDB_Processor" class="solutions.dao.spring.batch.DAOCommandProcessor">
		<property name="skipCommand">
			<bean class="solutions.dao.patterns.command.HasResultsArrayableCommand">
				<property name="dataSource" ref="dataSource" />
				<property name="hasResultThanTrue" value="true"/>
				<property name="sql"><value>SELECT 'Y' from RCG_STUFF_MOLTABLE where CDBREGNO = ? </value></property>
				<property name="inputsPositions">
					<list>
					    <value>0</value>
						</list>
				</property>
			</bean>
		</property>
		<property name="beforeCommand">
			<bean class="solutions.dao.patterns.command.ExecuteUpdateArrayableCommand">
				<property name="sql">
					<value>
						DELETE FROM RCG_STUFF_MOLTABLE WHERE CDBREGNO = ?
					</value>
				</property>
				<property name="dataSource" ref="dataSource"/>
				<property name="inputsPositions">
					<list>
						<value>0</value>
					</list>
				</property>
			</bean>
		</property>
	</bean>
	   <bean id="STRUCTURES_MCIDB_WRITER" class="org.springframework.batch.item.database.JdbcBatchItemWriter">
		<property name="sql">
			<value>INSERT
						INTO RCG_STUFF_MOLTABLE
						  (
						    CDBREGNO,
						    CTAB,
						    DATESTAMP,
						    USERNAME_RECORD_CREATOR,
						    PROJECT,
						    CHEMICAL_CLASS,
						    REMARKS,
						    PBECL,
						    BIO_RATIO_CALC,
						    BIO_RATIO_EXP,
						    LIBRARY_ID,
						    COUNT_COMPON_EST,
						    SOURCE_ID,
						    IS_FORMULATION,
						    IS_RADIOLABEL,
						    COMMENTS_STEREO,
						    COMPONENT_GROUP_ID,
						    PLATE_ID,
						    WELL_ID,
						    TAG_ID,
						    L_NUM_REQUEST,
						    MP,
						    BP,
						    PARENT_FORMULA,
						    PARENT_WEIGHT,
						    STUFF_FORMULA,
						    STUFF_WEIGHT,
						    PARENT_COUNT,
						    SALT_COUNT,
						    COMMENTS,
						    COMPOUND_ID,
						    CREATION_DATE,
						    CLIBRARY_ID,
						    THERAPEUTIC_CLASS,
						    BATCH_LOAD_ID,
						    BATCH_JOB_ID,
						    TARGET,
						    OEB,
						    ENTITY_TYPE
						  )
						  VALUES
						  (
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?,
						    ?
						  )</value>
		</property>
				<property name="dataSource">
			<ref bean="dataSource"/>
		</property>
		<property name="itemPreparedStatementSetter">
			<ref bean="itemPreparedStatementSetter"/>
		</property>
	</bean>
 * @author Gregory Green
 *
 */
public class DAOCommandProcessor 
implements ItemProcessor<Object, Object>
{
	/**
	 * 
	 */
	public DAOCommandProcessor()
	{
	}// --------------------------------------------------------
	
	/**
	 * 
	 * @param stepExecution the step execution
	 */
	@BeforeStep
	public void connect(StepExecution stepExecution) 
	{
		this.skipCommand.connect();
		
		if(this.beforeCommand != null)
			this.beforeCommand.connect();
		
	}// --------------------------------------------------------
	
	/**
	 * Dispose the DAO and COSMIC connections prior to exiting
	 * @param stepExecution
	 * @return the step exit status
	 */
	@AfterStep
	public ExitStatus disposeConnections(StepExecution stepExecution) 
	{
			
			if(this.skipCommand != null)
				try{ skipCommand.dispose(); } catch(Exception e){}
			
			this.skipCommand = null;
			
			
			if(this.beforeCommand != null)
				try{ beforeCommand.dispose(); } catch(Exception e){}
			
			this.beforeCommand = null;
						
			return stepExecution.getExitStatus();
	}// --------------------------------------------------------
	
	@Override
	public Object process(Object obj) throws Exception
	{
		Debugger.println(this,"Determining if must skip");
		
		if(this.skipCommand.execute(obj))
		{
			Debugger.println(this,"SKIPPING RECORD:"+obj);
			return null;
		}
		else
		{
			if(this.beforeCommand != null)
			{
				
				beforeCommand.execute(obj);
			}
			
		}
		
		Debugger.println(this,"Return original object"+obj);
		return obj;
	}// --------------------------------------------------------


	/**
	 * @return the beforeCommand
	 */
	public DAOCommand<Object, Object> getBeforeCommand()
	{
		return beforeCommand;
	}

	/**
	 * @param beforeCommand the beforeCommand to set
	 */
	public void setBeforeCommand(DAOCommand<Object, Object> beforeCommand)
	{
		this.beforeCommand = beforeCommand;
	}

	/**
	 * @return the skipCommand
	 */
	public DAOCommand<Boolean, Object> getSkipCommand()
	{
		return skipCommand;
	}

	/**
	 * @param skipCommand the skipCommand to set
	 */
	public void setSkipCommand(DAOCommand<Boolean, Object> skipCommand)
	{
		this.skipCommand = skipCommand;
	}


	private DAOCommand<Object, Object> beforeCommand;
	private DAOCommand<Boolean,Object>  skipCommand;
	
}
