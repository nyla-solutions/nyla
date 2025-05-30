package nyla.solutions.core.patterns.creational;

import nyla.solutions.core.exception.DuplicateRowException;

/**
 * Implement create, read, update and delete operations for records
 * @author Gregory Green
 *
 * @param <Key>
 * @param <Value>
 */
public interface KeyValueCRUD<Key,Value> extends KeyValueSaver<Key,Value>
{
	
	Value create(Key key, Value value)
	throws DuplicateRowException;
	
	Value read(Key key);
	
	Value update(Key key,Value value);
	
	Value delete(Key key);
	
	void deleteAll();
	
}
