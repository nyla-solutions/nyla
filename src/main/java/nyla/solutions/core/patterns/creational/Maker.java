package nyla.solutions.core.patterns.creational;

import java.util.function.Function;

public interface Maker<I,O> extends Function<I, O>
{

	O make(I value);
	
	@Override
	default O apply(I t)
	{
		return make(t);
	}
}
