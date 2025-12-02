package nyla.solutions.core.operations;


import nyla.solutions.core.patterns.creational.generator.MyEnum;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ClassPathTest
{

    /**
     * Test the creation of classes
     *
     * @throws Exception
     */
    @Test
    public void testClasspath()
    throws Exception
    {
        ClassPath classPath = new ClassPath();
        String expectedClassName = "nyla.solutions.core.security.user.data.UserProfile";

        Class<?> keyClass = classPath.loadClass(expectedClassName);


        assertNotNull(keyClass);

    }

    public void test_create_abstractClass()
    {
        AbstractClassPathQA abstractClassPathQA = ClassPath.newInstance(AbstractClassPathQA.class);

        assertNull(abstractClassPathQA);
        ClassPathQA classPathQA = ClassPath.newInstance(ClassPathQA.class);
        assertNotNull(classPathQA);


    }

    @Test
    public void testNewInstance()
    throws Exception
    {
        UserProfile up = ClassPath.newInstance(UserProfile.class);

        assertNotNull(up);
    }

    @Test
    public void testNewInstanceFloat()
    throws Exception
    {
        assertNotNull(ClassPath.newInstance(float.class));
        assertNotNull(ClassPath.newInstance(Float.class));

    }

    @Test
    public void testNewInstanceDouble()
    throws Exception
    {
        assertNotNull(ClassPath.newInstance(Double.class));
        assertNotNull(ClassPath.newInstance(double.class));

    }

    @Test
    public void testNewInstanceLong()
    throws Exception
    {
        assertNotNull(ClassPath.newInstance(Long.class));
        assertNotNull(ClassPath.newInstance(long.class));

    }

    @Test
    public void testNewInstanceCollections()
    throws Exception
    {
        assertNotNull(ClassPath.newInstance(Collection.class));
        assertNotNull(ClassPath.newInstance(List.class));
        assertNotNull(ClassPath.newInstance(Set.class));

    }

    @Test
    public void testMap()
    throws Exception
    {
        assertNotNull(ClassPath.newInstance(Map.class));

    }


    @Test
    void createSortSet() {

        var actual = ClassPath.newInstance(SortedSet.class);
        assertThat(actual).isNotNull();
    }

    @Test
    void createEnum() {

        assertDoesNotThrow(() -> ClassPath.newInstance(MyEnum.class));
    }

    @Test
    public void testNewInstanceDate()
    throws Exception
    {
        assertNotNull(ClassPath.newInstance(Date.class));

    }

    @Test
    public void testNewInstanceChar()
    throws Exception
    {
        assertNotNull(ClassPath.newInstance(char.class));
        assertNotNull(ClassPath.newInstance(Character.class));

    }

    public static abstract class AbstractClassPathQA
    {
        public AbstractClassPathQA()
        {
        }

        public abstract void qa();
    }

    public static class ClassPathQA extends AbstractClassPathQA
    {

        @Override
        public void qa()
        {

        }
    }


}
