package nyla.solutions.dao.patterns.command;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import nyla.solutions.core.data.Arrayable;
import nyla.solutions.core.exception.SystemException;

/**
 * Example
 * 	<bean name="mustSkipBooleanExpression" class="nyla.solutions.dao.patterns.expression.HasResultsArrayableBooleanExpression">
		<property name="hasResultThanTrue"><value>true</value></property>
		<property name="dataSource" ref="dataSource" />
		<property name="sql"><value>select 'Y' from ASAP_STRUCTURES where cdregno = ? </value></property>
		<property name="inputsPositions">
			<list>
				<value>1</value>
				</list>
			</property>
	</bean>
 * @author Gregory Green
 *
 */
public class ExecuteUpdateArrayableCommand extends AbstractDAOCommand<Integer,Arrayable<Object>> 
{
	/**
	 * 
	 * @see solutions.global.patterns.command.Command#execute(java.lang.Object)
	 */
	public Integer execute(Arrayable<Object>  eval)
	{
		PreparedStatement stmt = null;
		
		try
		{
			if(!this.isConnected())
				this.connect();
			
			stmt = prepareStatement(this.sql);
			
			int size = inputsPositions.length;
			

			Object[] dataRow = eval.toArray();
			
			for(int i= 0; i < size;i++)
			{
				stmt.setObject(i+1, dataRow[inputsPositions[i]]);
			}
			
			return new Integer(stmt.executeUpdate());
			
		}
		catch(SQLException e)
		{
			throw new SystemException(this.sql,e);
		}
		finally
		{
			
			if(stmt != null)
				try{ stmt.close();} catch(Exception e){}
		}
	}// --------------------------------------------------------

	
	/**
	 * @return the inputsPositions
	 */
	public int[] getInputsPositions()
	{
		return inputsPositions;
	}


	/**
	 * @param inputsPositions the inputsPositions to set
	 */
	public void setInputsPositions(int[] inputsPositions)
	{
		this.inputsPositions = inputsPositions;
	}

	
	/**
	 * @return the sql
	 */
	public String getSql()
	{
		return sql;
	}
	/**
	 * @param sql the sql to set
	 */
	public void setSql(String sql)
	{
		this.sql = sql;
	}


	private int[] inputsPositions;
	private String sql;
}
