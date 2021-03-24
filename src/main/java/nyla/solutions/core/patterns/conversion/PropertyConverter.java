package nyla.solutions.core.patterns.conversion;

public interface PropertyConverter<SourceType, TargetType>
{
	/**
	 * 
	 * @param sourceObject the source object
	 * @param aClass the class to dictate the conversion
	 * @return the target type
	 */
	public TargetType convert(Object sourceObject, Class<?> aClass);
	
}
