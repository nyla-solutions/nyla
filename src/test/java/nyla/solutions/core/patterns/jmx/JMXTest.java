package nyla.solutions.core.patterns.jmx;

import nyla.solutions.core.patterns.search.queryService.QuestDirector;
import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import java.io.IOException;
import java.lang.management.ClassLoadingMXBean;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class JMXTest
{
    private JMXConnector jmxc;
    private MBeanServerConnection connection;
    private String host = "localhost";
    int port = 6500;
    private JMX subject;
    private TabularData tabularData;
    private CompositeData compositeData;
    private String compositeDataValue = "compositeData";
    private ObjectName objectName;
    private String attributeName = "LoadedClassCount";
    private MBeanServerConnection mBeanServerConnection;
    private QueryExp queryExp =null;

    @BeforeEach
    void setUp() throws MalformedObjectNameException, IOException
    {
        queryExp = mock(QueryExp.class);
        this.objectName = new ObjectName("java.lang:type=ClassLoading");
        tabularData = mock(TabularData.class);
        compositeData = mock(CompositeData.class);
        when(compositeData.get(anyString())).thenReturn(compositeDataValue);
        jmxc = mock(JMXConnector.class);
        connection = mock(MBeanServerConnection.class);
        when(jmxc.getMBeanServerConnection()).thenReturn(connection);
        subject = new JMX(jmxc,connection,host,port);
    }

    @Nested
    class WhenGetSystemProperty
    {

        @Test
        void getSystemProperty() throws MalformedObjectNameException, InstanceNotFoundException, AttributeNotFoundException, MBeanException, ReflectionException, IOException
        {
            when(connection.getAttribute(any(), anyString()))
                    .thenReturn(tabularData);

            when(tabularData.get(any())).thenReturn(compositeData);

            String expectedKey = "key";
            String actual = subject.getSystemProperty(expectedKey);
            String expected = compositeDataValue;
            assertEquals(expected,actual);
        }


        @Test
        void getSystemProperty_WhenNotFound_ThenReturnNull() throws MalformedObjectNameException, InstanceNotFoundException
        {
            assertNull(subject.getSystemProperty("any"));
        }

        @Test
        void getSystemProperty_WhenTablarDataNull_ThenReturnNull() throws MalformedObjectNameException, InstanceNotFoundException, AttributeNotFoundException, MBeanException, ReflectionException, IOException
        {
            when(connection.getAttribute(any(), anyString()))
                    .thenReturn(tabularData);

            assertNull(subject.getSystemProperty("any"));
        }

    }



    @Test
    void invoke() throws ReflectionException, MBeanException, InstanceNotFoundException, IOException
    {
      /*
       if (interfaceClass != null)
                javax.management.JMX.newMBeanProxy(connection, objectName, interfaceClass);

            return this.connection.invoke(objectName, operationName, params, signature);
       */

        ObjectName objectName =  mock(ObjectName.class);

        String operationName = "testOp";
        Object[] params = {};
        String[] signature = {};
        subject.invoke(objectName,operationName,params,signature);

        Mockito.verify(connection).invoke(objectName,operationName,params,signature);
    }

    @Test
    void invoke_withinterfaces()
            throws ReflectionException, MBeanException, InstanceNotFoundException, IOException,
            ClassNotFoundException
    {
      /*
       if (interfaceClass != null)
                javax.management.JMX.newMBeanProxy(connection, objectName, interfaceClass);

            return this.connection.invoke(objectName, operationName, params, signature);
       */

        ObjectName objectName =  mock(ObjectName.class);

        String operationName = "testOp";
        Object[] params = {};
        String[] signature = {};
        Class<?> interfaces = Class.forName("com.sun.management.GarbageCollectorMXBean");
        subject.invoke(interfaces,objectName,operationName,params,signature);

        Mockito.verify(connection).invoke(objectName,operationName,params,signature);
    }


    @Test
    void newBean() throws MalformedObjectNameException
    {

        ClassLoadingMXBean actual = subject.newBean(ClassLoadingMXBean.class, objectName);
        assertNotNull(actual);
    }

    @Nested
    class GivenGetAttribute
    {

        @Test
        void whenGetAttribute_ThenReturn() throws InstanceNotFoundException, AttributeNotFoundException, MBeanException,
                ReflectionException, IOException
        {
            Object expected = "1212";
            when(connection.getAttribute(objectName,attributeName)).thenReturn(expected);
            Object actual = subject.getAttribute(objectName,attributeName);

            assertEquals(expected,actual);

        }

        @Test
        void whenInstanceNotFoundException() throws AttributeNotFoundException, MBeanException, ReflectionException,
                InstanceNotFoundException, IOException
        {
            when(connection.getAttribute(objectName,attributeName)).thenThrow(InstanceNotFoundException.class);
            assertThrows(InstanceNotFoundException.class,()-> subject.getAttribute(objectName,attributeName));
        }

        @Test
        void whenException() throws AttributeNotFoundException, MBeanException, ReflectionException,
                InstanceNotFoundException, IOException
        {
            when(connection.getAttribute(objectName,attributeName)).thenThrow(IOException.class);
            assertThrows(RuntimeException.class,()-> subject.getAttribute(objectName,attributeName));
        }
    }


    @Test
    void getLocalRuntimeArguments()
    {
        List<String> actual = JMX.getLocalRuntimeArguments();
        assertNotNull(actual);
    }

    @Test
    void getDomains() throws IOException
    {
        String[] expected = {"1","2"};
        when(connection.getDomains()).thenReturn(expected);
        String[] actual = subject.getDomains();

        assertEquals(expected,actual);
    }

    @Test
    void queryMBeans() throws IOException
    {

        ObjectInstance objectInstance = mock(ObjectInstance.class);
        Set<ObjectInstance>  expected = Organizer.toSet(objectInstance);
        when(connection.queryMBeans(objectName,queryExp)).thenReturn(expected);
        Set<ObjectInstance> actual = subject.queryMBeans(objectName, queryExp);

        assertEquals(expected,actual);
    }

    @Test
    void getDefaultDomain() throws IOException
    {
        String expected  = "hello";
        when(connection.getDefaultDomain()).thenReturn(expected);
        String actual = subject.getDefaultDomain();
        assertEquals(expected,actual);

    }

    @Test
    void getMBeanCount() throws IOException
    {
        Integer expected = 2;
        when(connection.getMBeanCount()).thenReturn(expected);
        Integer actual = subject.getMBeanCount();
        assertEquals(expected,actual);
    }

    @Test
    void searchObjectNames() throws IOException
    {
        String oName =  objectName.getCanonicalName();
        Set<ObjectName> expected = Organizer.toSet(objectName);
        when(connection.queryNames(any(ObjectName.class),any(QueryExp.class))).thenReturn(expected);
        Set<ObjectName> actual = subject.searchObjectNames(oName, queryExp);
        assertEquals(expected,actual);

    }

    @Test
    void testSearchObjectNames_whenObjectNameNull() throws IOException
    {
        String oName =  null;
        Set<ObjectName> expected = Organizer.toSet(objectName);
        when(connection.queryNames(Mockito.nullable(ObjectName.class),any(QueryExp.class))).thenReturn(expected);
        Set<ObjectName> actual = subject.searchObjectNames(oName, queryExp);
        assertEquals(expected,actual);
    }


    @Test
    void dispose() throws IOException
    {
        subject.dispose();

        verify(jmxc).close();
    }


    @Test
    void getHost()
    {
       assertEquals(host,subject.getHost());
    }

    @Test
    void getPort()
    {
      assertEquals(port,subject.getPort());
    }

    @Test
    void close() throws IOException
    {
       subject.close();

        verify(jmxc).close();
    }
}