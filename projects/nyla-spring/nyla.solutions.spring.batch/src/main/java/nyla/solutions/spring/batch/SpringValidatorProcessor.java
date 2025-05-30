package nyla.solutions.spring.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.validation.Validator;

import nyla.solutions.core.util.Debugger;

/**
 * @author Gregory Green
 *
 */
public class SpringValidatorProcessor implements ItemProcessor<Object, Object>
{

	public SpringValidatorProcessor()
	{
		Debugger.println(this,"created");
	}// --------------------------------------------------------
	
	
	@Override
	public Object process(Object obj) throws Exception
	{	

		this.validator.validate(obj, null);
		
		return obj;
	}// --------------------------------------------------------
	
	/**
	 * @return the validator
	 */
	public Validator getValidator()
	{
		return validator;
	}
	/**
	 * @param validator the validator to set
	 */
	public void setValidator(Validator validator)
	{
		this.validator = validator;
	}


	private Validator validator;
}
