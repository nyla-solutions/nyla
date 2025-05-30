package nyla.solutions.core.patterns.creational;

public interface KeyValueSaver<Key,Value>
{
	
	
	Value save(Key key, Value value);
	
		
}
