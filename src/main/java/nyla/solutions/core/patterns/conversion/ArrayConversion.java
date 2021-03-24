package nyla.solutions.core.patterns.conversion;

/**
 * 
 * @author Gregory Green
 *
 * @param <ConvertType> the convert type
 * @param <InputType> the input type
 */
public interface ArrayConversion<ConvertType,InputType>
{
	/**
	 * 
	 * @param input the input array
	 * @return the covnert output
	 */
	 ConvertType convert(InputType[] input); 	
}
