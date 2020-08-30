package nyla.solutions.core.patterns.reflection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NonPrimitiveJavaSchemaTest
{
    Class<?> expectClass = ArrayList.class;
    String expectFieldName = "f1";
    NonPrimitiveJavaSchema subject = new NonPrimitiveJavaSchema(expectClass, expectFieldName);

    @Test
    void getFieldName()
    {
        assertEquals(expectFieldName,subject.getFieldName());

    }

    @Test
    void getFieldClass()
    {
        assertEquals(expectClass,subject.getFieldClass());
    }

    @Test
    void getClassName()
    {
        assertEquals(expectClass.getName(),subject.getClassName());
    }

    @Test
    void getClassType()
    {
        assertEquals(ClassType.NonPrimitiveJava,subject.getClassType());
    }

    @Test
    void getFieldsTypes()
    {
        assertNull(subject.getFieldsTypes());
    }
}