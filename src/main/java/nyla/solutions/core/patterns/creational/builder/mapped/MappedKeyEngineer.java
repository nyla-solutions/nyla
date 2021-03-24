package nyla.solutions.core.patterns.creational.builder.mapped;

import java.util.Map;

public interface MappedKeyEngineer<K,V>
{
	public void construct(String id,Map<K,V> map);
}
