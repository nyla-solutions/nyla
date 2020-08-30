package nyla.solutions.core.patterns.reflection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumTypeSchemaTest
{
    EnumTypeSchema subject = new EnumTypeSchema(ExampleEnum.class);

    enum ExampleEnum {
            HELLO
    };

    @Test
    void getClassType()
    {
        assertEquals(ClassType.ENUM,subject.getClassType());
    }

    @Test
    public void test_get_class_name()
    {
        assertEquals(ExampleEnum.class.getName(),subject.getClassName());
    }


    @Test
    public void test_get_class()
    {
        assertEquals(ExampleEnum.class,subject.getFieldClass());
    }
}