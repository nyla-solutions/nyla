package nyla.solutions.spring.batch;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import nyla.solutions.core.data.DataRow;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.dao.ResultSetDataRow;


/**
 * Implements a RowMapper to use with JdbcCursorItemReader
 * 	<bean id="itemReader" class="org.springframework.batch.item.database.JdbcCursorItemReader">
		<property name="sql">
				<value>	select 
				            cdregno,
							to_char(cdregno), 
							stuff_formula
				  		from MCIDB_COMPOUND_SECURE
				</value>
		</property>
		<property name="dataSource">
			<ref bean="mcidbDataSource"/>
		</property>
		<property name="rowMapper">
			<ref bean="rowMapper"/>
		</property>
	</bean>
	
 Also, implements ItemPreparedStatementSetter to use 
 with a org.springframework.batch.item.database.JdbcBatchItemWriter
 * @author Gregory Green
 *
 */
public class DataRowMapperSetter implements RowMapper<ResultSetDataRow>, ItemPreparedStatementSetter<DataRow>
{

	public DataRowMapperSetter()
	{
		Debugger.println(this,"created");
		}
	@Override
	public ResultSetDataRow mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		Debugger.println(this,"creatind data for rowNum:"+rowNum);
		
		return new ResultSetDataRow(rs,rowNum);
	}// --------------------------------------------------------
	
	/**
	 * Initialize statement with data from prepared statement
	 * @see org.springframework.batch.item.database.ItemPreparedStatementSetter#setValues(java.lang.Object, java.sql.PreparedStatement)
	 */
    public  void setValues(DataRow dataRow, PreparedStatement preparedstatement)
    throws SQLException
    {
    	ResultSetDataRow.initStatement(dataRow,preparedstatement,maxParameters);
    }// --------------------------------------------------------
	
    /**
	 * @return the maxParameters
	 */
	public int getMaxParameters()
	{
		return maxParameters;
	}
	/**
	 * @param maxParameters the maxParameters to set
	 */
	public void setMaxParameters(int maxParameters)
	{
		this.maxParameters = maxParameters;
	}

	private int maxParameters = -1;
}
