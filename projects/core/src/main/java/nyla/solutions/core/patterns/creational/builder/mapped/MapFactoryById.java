package nyla.solutions.core.patterns.creational.builder.mapped;

import nyla.solutions.core.Identifiable;
import nyla.solutions.core.data.Identifier;

import java.util.Map;

public interface MapFactoryById<K,V> 
extends Identifier, Identifiable
{
	public Map<K,V> createMap();
}
