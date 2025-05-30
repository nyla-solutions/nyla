package nyla.solutions.core.exception.fault;

import nyla.solutions.core.data.Categorizable;
import nyla.solutions.core.data.Codeable;
import nyla.solutions.core.exception.SetupException;

import java.io.Serial;
import java.io.Serializable;

/**
 * Data type of error with a code and category
 * @author Gregory Green
 *
 */
public class FaultError implements Serializable, Codeable, Categorizable
{
	private String errorCategory = null;
	private String errorCode = null;

			/**
	 * MAX_ERROR_CATEGORY_LEN =5
	 */
	public static final int MAX_ERROR_CATEGORY_LEN = 5;
	
	/**
	 * serialVersionUID = -5514197626290579272L
	 */
	@Serial
	private static final long serialVersionUID = -5514197626290579272L;
	
	/**
	 * Fault Error construction
	 * @param errorCode the error code
	 * @param errorCategory the error category
	 */
	public FaultError( String errorCode,String errorCategory)
	{
		this.setCode(errorCode);
		this.setCategory(errorCategory);
	}
	/**
	 * @return the errorCategory
	 */
	public String getCategory()
	{
		return errorCategory;
	}
	/**
	 * @param errorCategory the errorCategory to set
	 */
	public void setCategory(String errorCategory)
	{
		if(errorCategory != null && errorCategory.length() > MAX_ERROR_CATEGORY_LEN)
		{
			throw new SetupException("Max length:"+MAX_ERROR_CATEGORY_LEN+ " errorCategory:"+errorCategory);
		}
		
		this.errorCategory = errorCategory;
	}
	/**
	 * @return the errorCode
	 */
	public String getCode()
	{
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setCode(String errorCode)
	{
		if(errorCode != null && errorCode.length() > MAX_ERROR_CATEGORY_LEN)
		{
			throw new SetupException("Max length:"+MAX_ERROR_CATEGORY_LEN+ " errorCode:"+errorCode);
		}
		
		this.errorCode = errorCode;
		
		
	}

}
