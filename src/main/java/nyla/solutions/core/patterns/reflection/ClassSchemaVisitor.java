package nyla.solutions.core.patterns.reflection;

public interface ClassSchemaVisitor
{
	/**
	 * 
	 * @param classSchema class schema
	 */
	void visitClass(ClassSchema classSchema);
	
	/**
	 * 
	 * @param typeSchema class
	 */
	void visitField(TypeSchema typeSchema);	
	
	
	/**
	 * 
	 * @param methodSchema
	 */
	void visitMethod(MethodSchema methodSchema);

}
