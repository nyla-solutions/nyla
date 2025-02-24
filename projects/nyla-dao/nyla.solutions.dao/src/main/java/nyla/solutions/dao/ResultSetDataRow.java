package nyla.solutions.dao;


import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import nyla.solutions.core.data.Arrayable;
import nyla.solutions.core.data.DataRow;
import nyla.solutions.core.util.Debugger;

/**
 * Represents an entity row
 * @author Gregory Green
 *
 */
public class ResultSetDataRow extends DataRow implements Arrayable<Object>
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 6421124227514373292L;
	
/**
    * 
    * Constructor for DataRow initializes internal
    */
   protected ResultSetDataRow()
   {
      super();
   }// --------------------------------------------
   /**
    * 
    * Constructor for DataRow initializes internal from the result set 
    * @param resultSet the database result set
    * @throws SQLException
    */
   public ResultSetDataRow(ResultSet resultSet)
   throws SQLException 
   {
	   this(resultSet,0);
	   
   }// --------------------------------------------------------
   /**
    * 
    * Constructor for DataRow initializes internal from the result set 
    * @param resultSet the database result set
    * @param rowNum starts from 0
    * @throws SQLException
    */
   public ResultSetDataRow(ResultSet resultSet, int rowNum)
   throws SQLException 
   {      
	   super(rowNum);
	   
      int columnCount = DAOFactory.retrieveColumnCount(resultSet);
      
     for (int i = 0; i< columnCount; i++)
     {         
        
        add(resultSet.getObject(i+1));
     }
     
  
   }// --------------------------------------------   
   /**
    * Initialize the statement
    * @param ps the parameter statement
    * @throws SQLException
    */
	public static void initStatement(DataRow dataRow, PreparedStatement ps, int maxParameters)
	throws SQLException
	{
		if(maxParameters < 1)
			maxParameters = dataRow.size();
		
		Object obj = null;
		Blob blob;
		Clob clob;
		for (int i = 0; i < maxParameters; i++)
		{
			obj = dataRow.retrieveObject(i+1);
			try
			{
			   if(obj instanceof Blob)
			   {
				   blob = ps.getConnection().createBlob();
				   SQL.copy((Blob)obj, blob);
				   ps.setBlob(i+1, blob);
			   }
			   else if(obj instanceof Clob)
			   {
				   clob = ps.getConnection().createClob();
				   SQL.copy((Clob)obj, clob);
				   ps.setClob(i+1, clob);
			   }
			   else if(obj != null)
					ps.setObject(i+1, obj);
				else
					ps.setNull(i+1,  java.sql.Types.NULL);
			}
			catch(SQLException e)
			{
				throw new SQLException("position:"+i,e);
			}
		}
		
		Debugger.println(dataRow,"initStatement maxParameters="+maxParameters+" "+dataRow);
	}// --------------------------------------------------------
   /**
    * Initialize the statement
    * @param ps the parameter statement
    * @throws SQLException
    */
	public void initStatement(PreparedStatement ps, int maxParameters)
	throws SQLException
	{
		initStatement(this, ps, maxParameters);
	}// --------------------------------------------------------
   

}
