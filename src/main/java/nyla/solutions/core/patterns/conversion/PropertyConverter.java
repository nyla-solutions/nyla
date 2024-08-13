package nyla.solutions.core.patterns.conversion;

/**
 * Converter for properties with Java Beans
 * @author gregory green
 * @param <SourceType> the source property type
 * @param <TargetType> the target property type
 *
 */
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
