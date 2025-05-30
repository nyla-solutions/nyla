package nyla.solutions.dao;

import java.sql.ResultSet;

import nyla.solutions.core.patterns.creational.RowObjectCreator;

/**
 * Row mapper for a result set
 * @author Gregory Green
 *
 * @param <ObjectType> the object class
 */
public interface ResultSetObjectCreator<ObjectType> extends RowObjectCreator<ObjectType, ResultSet>
{
	/**
	 * 
	 * @param rs the result set
	 * @param index the current index
	 * @return the object instance
	 */
	ObjectType create(ResultSet rs, int index);
}
