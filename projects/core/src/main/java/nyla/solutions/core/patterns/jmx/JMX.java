package nyla.solutions.core.patterns.jmx;

import nyla.solutions.core.patterns.Disposable;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.*;

import static nyla.solutions.core.util.Config.settings;


/**
 * Wrapper class that interfaces with JMX
 * <p>
 * Sample URL
 * "service:jmx:rmi:///jndi/rmi://:9999/jmxrmi";
 * String urlText = "service:jmx:rmi:///jndi/rmi://host:port/jmxrmi";
 * <p>
 * <p>
 * This object is based JMX object in the NYLA Java framework
 * <p>
 * https://github.com/ggreen/nyla/blob/master/dev/Solutions
 * .Global/src/main/java/nyla/solutions/global/patterns/jmx/JMX.java
 *
 * @author Gregory Green
 */
public class JMX implements AutoCloseable, Disposable
{
    /**
     * ThreadMX_NAME = "java.lang:type=Threading"
     */
    private static final String ThreadMX_NAME = "java.lang:type=Threading";
    private static final String JMX_USERNAME_PROP = "jmx-username";
    private static final String JMX_PASSOWRD_PROP = "jmx-password";
    /**
     * MemoryMX_NAME = "java.lang:type=Memory"
     */
    private static final String MemoryMX_NAME = "java.lang:type=Memory";

    private final JMXConnector jmxc;
    private boolean disposed = false;
    private final MBeanServerConnection connection;
    private final String host;
    private final int port;

    protected JMX(JMXConnector jmxc, MBeanServerConnection connection, String host, int port)
    {
        this.jmxc = jmxc;
        this.connection = connection;
        this.host = host;
        this.port = port;
    }


    /**
     * @param key the key
     * @return RuntimeMXBean.getSystemProperties
     *
     * @throws InstanceNotFoundException    the instance not found exception
     * @throws MalformedObjectNameException the malformed object name
     */
    public String getSystemProperty(String key)
            throws MalformedObjectNameException, InstanceNotFoundException
    {
        TabularData td = getAttribute(new ObjectName("java.lang:type=Runtime"), "SystemProperties");
        if(td == null)
            return null;

        String[] keyArray = {key};

        CompositeData cd = td.get(keyArray);

        return toText(cd);

    }

    private String toText(CompositeData cd)
    {
        if (cd == null)
            return null;

        Object property = cd.get("value");

        return String.valueOf(property);
    }

    /**
     * <p>Invokes an operation on an MBean.</p>
     *
     * @param objectName
     * @param operationName
     * @param params
     * @param signature
     * @return the return object from the method call
     */
    public Object invoke(ObjectName objectName, String operationName, Object[] params, String[] signature)
    {
        return invoke(null, objectName, operationName, params, signature);
    }

    /**
     * <p>Invokes an operation on an MBean.</p>
     *
     * @param objectName
     * @param operationName
     * @param params
     * @param signature
     * @param interfaceClass the interface class
     * @return the return object from the method call
     */
    public Object invoke(Class<?> interfaceClass, ObjectName objectName, String operationName, Object[] params,
                         String[] signature)
    {
        try {
            if (interfaceClass != null)
                javax.management.JMX.newMBeanProxy(connection, objectName, interfaceClass);

            return this.connection.invoke(objectName, operationName, params, signature);
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to invoke objectName:" + objectName + " operationName:" + operationName, e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T newBean(Class<?> interfaceClass, ObjectName objectName)
    {
        if (interfaceClass == null)
            return null;

        return (T) javax.management.JMX.newMBeanProxy(connection, objectName, interfaceClass);
    }

    /**
     * @param <T>        the type
     * @param objectName the object name
     * @param attribute  the attribute
     * @return the attributes for the given object name
     *
     * @throws InstanceNotFoundException when the instance if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(ObjectName objectName, String attribute)
            throws InstanceNotFoundException
    {
        try {
            return (T) this.connection.getAttribute(objectName, attribute);
        }
        catch (InstanceNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to get attributes objectName:" + objectName + " attribute:" + attribute, e);
        }
    }



    public static List<String> getLocalRuntimeArguments()
    {
        return ManagementFactory.getRuntimeMXBean().getInputArguments();
    }// -----------------------------------------------

    /**
     * @param host the JMX host
     * @param port the jmx port
     * @return JMX the connected instance
     */
    public static JMX connect(String host, int port)
    {
        return connect(host, port, settings().getProperty(JMX_USERNAME_PROP),
                settings().getPropertyPassword(JMX_PASSOWRD_PROP));

    }

    public static JMX connect(String host, int port, String user, char[] password)
    {
        String connectionURL = String.format("service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi", host, port);

        if (connectionURL == null || connectionURL.length() == 0)
            throw new IllegalArgumentException("connectionURL");

        try {
            JMXServiceURL url = new JMXServiceURL(connectionURL);


            Map<String, String[]> map = null;

            //Populate credentials
            if (user != null && user.length() > 0) {
                map = new HashMap<String, String[]>();

                if (password == null || password.length == 0)
                    throw new SecurityException("password required");

                String[] credentials = new String[]{user, new String(password)};

                map.put(JMXConnector.CREDENTIALS, credentials);
            }

            JMXConnector jmxc = JMXConnectorFactory.connect(url, map);

            MBeanServerConnection connection = jmxc.getMBeanServerConnection();

            return new JMX(jmxc,connection,host,port);
        }
        catch (SecurityException e) {
            throw new JMXSecurityException("Cannot authenticate username:" + user + " to connectionURL=" + connectionURL + " error:" + e.getMessage(), e);
        }
        catch (Exception e) {
            throw new JMXConnectionException("Cannot connect to URL=" + connectionURL + " ERROR:" + e.getMessage(), e);
        }

    }

    /**
     * @return domains from connection
     *
     * @throws IOException
     * @see javax.management.MBeanServerConnection#getDomains()
     */
    public String[] getDomains() throws IOException
    {
        return connection.getDomains();
    }// ----------------------------------------------

    public Set<ObjectInstance> queryMBeans(ObjectName objectName, QueryExp queryExp)
            throws IOException
    {
        return this.jmxc.getMBeanServerConnection().queryMBeans(objectName, queryExp);
    }

    /**
     * @param args (URL) (user) (password)
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        String host = null;
        int port;
        String userName = null;
        char[] password = null;
        if (args.length < 2) {
            System.out.println("Usage java " + JMX.class.getCanonicalName() + " host port (userName password)?");
            System.exit(-1);
        }

        host = args[0];
        port = Integer.parseInt(args[1]);

        if (args.length > 2) {
            if (args.length < 4) {
                System.out.println("Usage java " + JMX.class.getCanonicalName() + " host port userName password");
                System.exit(-1);
            }


            userName = args[2];
            password = args[3].toCharArray();
        }


        JMX jmx = JMX.connect(host, port, userName, password);

        // Get domains from MBeanServer
        //
        System.out.println("\nDomains:");
        String[] domains = jmx.getDomains();
        Arrays.sort(domains);
        for (int i = 0; i < domains.length; i++) {
            System.out.println("\tDomain = " + domains[i]);
        }

        //waitForEnterPressed();

        // Get MBeanServer's default domain
        //
        System.out.println("\nMBeanServer default domain = " + jmx.getDefaultDomain());

        // Get MBean count
        //
        System.out.println("\nMBean count = " + jmx.getMBeanCount());

        // Query MBean names
        //
        System.out.println("\nQuery MBeanServer MBeans:");
        Set<ObjectName> names = jmx.searchObjectNames(null);


        ObjectName objectName = null;
        for (Iterator<ObjectName> itera = names.iterator(); itera.hasNext(); ) {
            objectName = itera.next();

            System.out.println("getCanonicalName=" + objectName.getCanonicalName());

        }

        System.out.println("=========Memory Usage=========");

        MemoryMXBean memory = jmx.getMemory();
        System.out.println("memory.getObjectPendingFinalizationCount=" + memory.getObjectPendingFinalizationCount());
        MemoryUsage memoryUsage = memory.getHeapMemoryUsage();
        System.out.println("memory.getCommitted=" + memoryUsage.getCommitted());
        System.out.println("memory.getUsed=" + memoryUsage.getUsed());


        System.out.println("=========Thread Usage=========");

        ThreadMXBean thread = jmx.getThread();
        System.out.println("thread.getPeakThreadCount=" + thread.getPeakThreadCount());

        System.out.println("thread.getDaemonThreadCount=" + thread.getDaemonThreadCount());
        System.out.println("thread.getThreadCount=" + thread.getThreadCount());
        System.out.println("thread.getTotalStartedThreadCount=" + thread.getTotalStartedThreadCount());


        //jmx.registerMemoryNotifications(new ClientListener(), null);

        System.out.println("Enter key to exit");

        System.in.read();

    }// ----------------------------------------------

//    public static class ClientListener implements NotificationListener
//    {
//        public void handleNotification(Notification notification,
//                                       Object handback)
//        {
//            System.out.println("\nReceived notification:");
//            System.out.println("\tClassName: " + notification.getClass().getName());
//            System.out.println("\tSource: " + notification.getSource());
//            System.out.println("\tType: " + notification.getType());
//            System.out.println("\tMessage: " + notification.getMessage());
//            if (notification instanceof AttributeChangeNotification) {
//                AttributeChangeNotification acn =
//                        (AttributeChangeNotification) notification;
//                System.out.println("\tAttributeName: " + acn.getAttributeName());
//                System.out.println("\tAttributeType: " + acn.getAttributeType());
//                System.out.println("\tNewValue: " + acn.getNewValue());
//                System.out.println("\tOldValue: " + acn.getOldValue());
//            }
//        }
//    }

    /**
     * @return default domain
     *
     * @throws IOException
     * @see javax.management.MBeanServerConnection#getDefaultDomain()
     */
    public String getDefaultDomain() throws IOException
    {
        return connection.getDefaultDomain();
    }

    /**
     * @return connection.getMBeanCount()
     *
     * @throws IOException
     * @see javax.management.MBeanServerConnection#getMBeanCount()
     */
    public Integer getMBeanCount() throws IOException
    {
        return connection.getMBeanCount();
    }

    /**
     * @param objectNamePattern ex: GemFire:*,name=*
     * @return set of the matching object names
     *
     * @see javax.management.MBeanServerConnection#queryNames(javax.management.ObjectName, javax.management.QueryExp)
     */
    public Set<ObjectName> searchObjectNames(String objectNamePattern)
    {
        return searchObjectNames(objectNamePattern, null);
    }

    /**
     * Example Query
     * <p>
     * QueryExp query =
     * Query.and(Query.eq(Query.attr("Enabled"), Query.value(true)),
     * Query.eq(Query.attr("Owner"), Query.value("Duke")));
     *
     * @param objectNamePattern the object names to look for
     * @param queryExp          the query express
     * @return set the object names
     */
    public Set<ObjectName> searchObjectNames(String objectNamePattern, QueryExp queryExp)
    {
        try {
            ObjectName objectName = null;

            if (objectNamePattern != null)
                objectName = new ObjectName(objectNamePattern);

            return connection.queryNames(objectName, queryExp);
        }
        catch (MalformedObjectNameException e) {
            throw new RuntimeException("Invalid objectNamePattern" + objectNamePattern, e);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to query names with objectNamePattern:" + objectNamePattern, e);
        }
    }

    /**
     * @return MemoryMXBean referred by java.lang:type=Memory
     */
    public MemoryMXBean getMemory()
    {
        try {
            return (MemoryMXBean) ManagementFactory.newPlatformMXBeanProxy(connection, MemoryMX_NAME,
                    MemoryMXBean.class);

        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }// ----------------------------------------------

    /**
     * @return MBean with TheadMX_NAME = "java.lang:type=Threading"
     */
    private ThreadMXBean getThread()
    {
        try {
            return (ThreadMXBean) ManagementFactory.newPlatformMXBeanProxy(connection, ThreadMX_NAME,
                    ThreadMXBean.class);

        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }// ----------------------------------------------

    /**
     * Implements the disposable interface to close the JMX connection
     */
    public void dispose()
    {
        if (!disposed) {
            try {
                jmxc.close();
            }
            catch (Exception e) {
            }
        }

        disposed = true;
    }// ----------------------------------------------

    /**
     * Adds a listener to a registered MBean. Notifications emitted by the MBean will be forwarded to the listener.
     *
     * @param objectName           - The name of the MBean on which the listener should be added.
     * @param notificationListener - The listener object which will handle the notifications emitted by the registered
     *                            MBean.
     * @param notificationFilter   - The filter object. If filter is null, no filtering will be performed before
     *                             handling notifications.
     * @param handback             - The context to be sent to the listener when a notification is emitted.
     * @throws InstanceNotFoundException
     * @throws IOException
     */
    private void addNotificationListener(ObjectName objectName,
                                        NotificationListener notificationListener,
                                        NotificationFilter notificationFilter, Object handback)
            throws InstanceNotFoundException, IOException
    {
        connection.addNotificationListener(objectName, notificationListener, notificationFilter, handback);
    }// ----------------------------------------------

    /**
     * Allows a listener to be registered within the MemoryMXBean as a notification listener
     * usage threshold exceeded notification - for notifying that the memory usage of a memory pool is increased
     * and has reached or exceeded its usage threshold value.
     * <p>
     * collection usage threshold exceeded notification - for notifying that the memory usage
     * of a memory pool is greater than or equal to its collection usage threshold
     * after the Java virtual machine has expended effort in recycling unused objects in that memory pool.
     * <p>
     * The notification emitted is a Notification instance whose user data is set to a CompositeData that
     * represents a MemoryNotificationInfo object containing information about the memory pool when the
     * notification was constructed. The CompositeData contains the attributes as described in MemoryNotificationInfo.
     *
     * @param notificationListener listener to be alerted
     * @param handback             object to be passed back to notification listener when notification occurs
     */
    private void registerMemoryNotifications(NotificationListener notificationListener, Object handback)
    {
        NotificationEmitter emitter = (NotificationEmitter) this.getMemory();
        emitter.addNotificationListener(notificationListener, null, handback);

    }// ----------------------------------------------

    /**
     * @return the host
     */
    public String getHost()
    {
        return host;
    }

    /**
     * @return the port
     */
    public int getPort()
    {
        return port;
    }

    @Override
    public void close()
    {

        this.dispose();
    }



}