package nyla.solutions.core.patterns.repository;

public interface KeyValueSaver<Key,Value>
{
	
	
	Value save(Key key, Value value);
	
		
}
