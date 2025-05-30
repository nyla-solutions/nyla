package nyla.solutions.spring.batch;


import org.springframework.batch.item.database.JdbcCursorItemReader;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.util.Debugger;

/**
 * Enhanced JDBC cursor reader to use a Textable object
 * @author Gregory Green
 *
 * @param <T>
 */
public class JdbcCursorItemTextableReader<T> extends JdbcCursorItemReader<T>
{
	/**
	 * @return this.textable.getText()
	 * @see org.springframework.batch.item.database.JdbcCursorItemReader#getSql()
	 */
	@Override
	public String getSql()
	{
		return this.textable.getText();
	}// --------------------------------------------------------
	
	/**
	 * @return the textable
	 */
	public Textable getTextable()
	{
		return textable;
	}
	/**
	 * @param textable the textable to set
	 */
	public void setTextable(Textable textable)
	{
		
		this.textable = textable;
		
	}// --------------------------------------------------------
	
	/**
	 * Set the SQL
	 * @see org.springframework.batch.item.database.JdbcCursorItemReader#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception
	{
		 if(this.textable !=null)
		 {
			 String sql = this.textable.getText();
			 Debugger.println(this,"sql="+sql);
			 
			 this.setSql(sql);
		 }
		 
				super.afterPropertiesSet();
	}// --------------------------------------------------------

  /* @Override
   public void open(ExecutionContext executionContext)
	{
	   if(this.textable !=null)
		   this.setSql(this.textable.getText());
	   
		super.open(executionContext);
	}*/

	private Textable textable;
}
