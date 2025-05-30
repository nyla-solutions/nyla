package nyla.solutions.core.patterns.conversion;

public interface Converter<SourceType, TargetType>
{
	/**
	 * 
	 * @param sourceObject the object to convert
	 * @return the target type
	 */
	public TargetType convert(SourceType sourceObject);
	
}
