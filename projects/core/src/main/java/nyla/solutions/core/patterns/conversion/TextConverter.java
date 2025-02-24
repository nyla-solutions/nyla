package nyla.solutions.core.patterns.conversion;

/**
 *  Interface for converter objects to string
 * @author Gregory Green
 *
 */
public interface TextConverter
{
	/**
	 * Converts the input object to a text
	 * @param input the source input object
	 * @return the text version of the input object
	 */
	public String toText(Object input);
}
