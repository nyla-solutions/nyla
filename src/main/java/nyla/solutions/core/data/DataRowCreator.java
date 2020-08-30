package nyla.solutions.core.data;

import nyla.solutions.core.patterns.creational.RowObjectCreator;
import nyla.solutions.core.patterns.reflection.JavaBeanVisitor;
import nyla.solutions.core.util.JavaBean;

import java.io.Serializable;
import java.util.HashMap;


/**
 * Create a Data ROW that data from a object
 * @author Gregory Green
 *
 */
public class DataRowCreator implements JavaBeanVisitor, RowObjectCreator<DataRow, Object>
{
	public DataRowCreator()
	{
	}

	/**
	 * @see nyla.solutions.core.patterns.reflection.JavaBeanVisitor#visitClass(java.lang.Class, java.lang.Object)
	 */
	@Override
	public void visitClass(Class<?> aClass, Object object)
	{
	}

	/**
	 * @see nyla.solutions.core.patterns.reflection.JavaBeanVisitor#visitProperty(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void visitProperty(String name, Object value, Object object)
	{
		dataRow.put(name, (Serializable)value);
	}// --------------------------------------------------------
	/**
	 * 
	 * @return the data row
	 */
	@SuppressWarnings("unchecked")
	public DataRow getDataRow()
	{
		return new DataRow((HashMap<String,Serializable>)dataRow.clone());
	}// --------------------------------------------------------
	
	@Override
	public DataRow create(Object rs, int index)
	{
		JavaBean.acceptVisitor(rs, this);
		
		DataRow dataRow = this.getDataRow();
		
		dataRow.setRowNum(index);
		
		this.clear();
		
		return dataRow;
	}// --------------------------------------------------------
	
	/**
	 * Clear the current built properties
	 */
	public void clear()
	{
		dataRow.clear();
	}
	
	private transient HashMap<String, Serializable> dataRow = new HashMap<String, Serializable>();



}
