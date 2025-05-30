package nyla.solutions.core.data;

import java.io.Serializable;

/**
 * <pre>
 * Type a typed object.
 * Types object have a unique ID (primary key), name, code and version
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public interface Type 
extends Serializable
{

	String getTypeName();
	void setTypeName(String typeName);
}
