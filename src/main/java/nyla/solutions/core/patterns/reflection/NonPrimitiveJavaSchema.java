package nyla.solutions.core.patterns.reflection;

import java.io.Serializable;

/**
 * @author Gregory Green
 */
public class NonPrimitiveJavaSchema implements Serializable, TypeSchema
{
    private final String fieldName;
    private final Class<?> fieldClass;
    private final String fieldClassName;

    public NonPrimitiveJavaSchema(Class<?> fieldClass, String fieldName)
    {
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
        this.fieldClassName = fieldClass.getName();

    }

    @Override
    public String getFieldName()
    {
        return fieldName;
    }

    @Override
    public Class<?> getFieldClass()
    {
        return fieldClass;
    }

    @Override
    public String getClassName()
    {
        return fieldClassName;
    }

    @Override
    public ClassType getClassType()
    {
        return ClassType.NonPrimitiveJava;
    }

    @Override
    public TypeSchema[] getFieldsTypes()
    {
        return null;
    }
}
