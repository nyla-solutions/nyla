package nyla.solutions.core.patterns.reflection;

public interface JavaBeanVisitor
{
	/**
	 * 
	 * @param aClass the class name
	 * @param object the object
	 */
	public void visitClass(Class<?> aClass, Object object);
	
	
	/**
	 * 
	 * @param name the property
	 * @param value the property value
	 * @param object the parent object
	 */
	public void visitProperty(String name, Object value, Object object);

}
