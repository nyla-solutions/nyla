package nyla.solutions.core.patterns.conversion;

import nyla.solutions.core.data.Nameable;

/**
 * Used a generic version of Textable with a name.
 * @author Gregory Green
 *
 * @param <SourceType>
 * @param <TargetType>
 */
public interface NameableConverter<SourceType, TargetType> extends Nameable, Converter<SourceType, TargetType>
{

}
