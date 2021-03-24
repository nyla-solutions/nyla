package nyla.solutions.core.patterns.conversion;

/**
 * Simply just return the target object provided with no
 * conversions.
 *
 * @param <SourceType> the source type
 * @param <TargetType> the target type
 * @author Gregory Green
 */
public class FixedNamedConverter<SourceType, TargetType> implements NameableConverter<SourceType, TargetType>
{
	private String name;
	private TargetType target;

	@Override
	public TargetType convert(Object sourceObject)
	{
		return target;
	}// --------------------------------------------------------

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the target
	 */
	public TargetType getTarget()
	{
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(TargetType target)
	{
		this.target = target;
	}

}
