package nyla.solutions.core.operations;

import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.exception.fault.ClassNotFoundFaultException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Debugger;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * Class path utility class
 *
 * @author Gregory Green
 */
public class ClassPath extends ClassLoader
{
    public ClassPath()
    {
        super(ClassPath.class.getClassLoader());
    }

    /**
     * @return System.getProperty(" java.class.path ")
     */
    public static String getClassPathText()
    {
        return System.getProperty("java.class.path");
    }

    /**
     * @param className the class name
     * @return the class
     */

    public static Class<?> toClass(String className)
    {

        try
        {

            return Class.forName(className);

        }
        catch (ClassNotFoundException e)
        {

            //check for primitive

            if (int.class.getName().equals(className))

                return int.class;

            else if (long.class.getName().equals(className))

                return long.class;

            else if (short.class.getName().equals(className))

                return short.class;

            else if (double.class.getName().equals(className))

                return double.class;

            else if (float.class.getName().equals(className))

                return float.class;


            //not a primitive, throw exceptions

            throw new ClassNotFoundFaultException
                    ("\"" + className + "\"");

        }

    }

    public static boolean isPrimitive(Field field)
    {
        if (field == null)
            return false;

        String fieldName = field.getName();

        if ("serialVersionUID".equals(fieldName))
            return false;

        return isPrimitive(field.getType());


    }

    public static boolean isPrimitive(Class<?> aClass)
    {
        if (aClass == null)
            return false;

        String className = aClass.getName();
        return className.matches("(float|char|short|double|int|long|byte|boolean|(java.lang.(Long|Integer|String|Float|Double|Short|Byte|Boolean)))");

    }

    /**
     * Use a constructor of the a class to create an instance
     *
     * @param className the class the create
     * @param <T>       the type
     * @return the initiate object
     */
    public static <T> T newInstance(String className)
    {
        return newInstance(toClass(className));
    }

    /**
     * Use a constructor of the a class to create an instance
     *
     * @param className     the class the create
     * @param parameterType the parameter type
     * @param initarg       the initial constructor argument
     * @param <T>           the type to be created
     * @return the initiate object
     * @throws SetupException the setup error
     */
    public static <T> T newInstance(String className, Class<?> parameterType, Object initarg)
    throws SetupException
    {
        try
        {
            return newInstance(Class.forName(className), parameterType, initarg);
        }
        catch (ClassNotFoundException e)
        {
            throw new SetupException(e);

        }
    }

    /**
     * Use a constructor of the a class to create an instance
     *
     * @param aClass        the class object
     * @param parameterType the parameter type for the constructor
     * @param initarg       the initial constructor argument
     * @param <T>           the class type
     * @return the new created object
     * @throws SetupException when an setup error occurs
     */
    public static <T> T newInstance(Class<?> aClass, Class<?> parameterType, Object initarg)
    throws SetupException
    {
        Class<?>[] paramTypes = {parameterType};
        Object[] initArgs = {initarg};

        return newInstance(aClass, paramTypes, initArgs);
    }
    /**
     * Create a new instance of a given object
     *
     * @param className the class the created
     * @param initargs  the initial constructor arguments
     * @param <T>       the class type
     * @return the created instance
     * @throws SetupException when an initialize issue occurs
     */
    public static <T> T newInstance(String className, Object... initargs)
    throws SetupException
    {
        return newInstance(toClass(className), initargs);
    }//------------------------------------------------

    /**
     * Create a new instance of a given object
     *
     * @param aClass   the class the create
     * @param initargs the initial constructor arguments
     * @param <T>      the type class
     * @return the create instance
     * @throws SetupException when an initialize issue occurs
     */
    public static <T> T newInstance(Class<?> aClass, Object... initargs)
    throws SetupException
    {
        Class<?>[] parameterTypes = null;

        if (initargs != null && initargs.length > 0)
        {
            parameterTypes = new Class<?>[initargs.length];

            for (int i = 0; i < initargs.length; i++)
            {
                if (initargs[i] == null)
                    continue; //skip

                parameterTypes[i] = initargs[i].getClass();
            }
        }

        return newInstance(aClass, parameterTypes, initargs);
    }//------------------------------------------------

    /**
     * Use a constructor of the a class to create an instance
     *
     * @param aClass         the class the create
     * @param parameterTypes the constructor parameter types
     * @param initargs       the constructor initial arguments
     * @param <T>            the class type
     * @return the initiate object
     * @throws SetupException when internal error occurs
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> aClass, Class<?>[] parameterTypes, Object[] initargs)
    throws SetupException
    {
        try
        {
            //Get constructor
            Constructor<?> constructor = aClass.getConstructor(parameterTypes);

            return (T) constructor.newInstance(initargs);
        }
        catch(NoSuchMethodException noSuchMethodException){
            var constructors = aClass.getConstructors();
            for(var constructor:constructors){
                if(constructor.getParameterCount() == parameterTypes.length) {
                    try {
                        return (T) constructor.newInstance(initargs);
                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException ex) {
                        throw new SetupException(noSuchMethodException);
                    }
                }
            }
            throw new SetupException(noSuchMethodException);
        }
        catch (Exception e)
        {
            throw new SetupException(e);
        }
    }

    /**
     * Use a constructor of the a class to create an instance
     *
     * @param creationClass the class the create
     * @param <T>    the type class
     * @return the initiate object
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> creationClass)
    {
        if (creationClass == null)
            return null;


        String className = creationClass.getName();
        try
        {
//
            if (String.class.equals(creationClass))
            {
                return (T) "";
            }
            else if (Integer.class.equals(creationClass))
            {
                return (T) Integer.valueOf(0);
            }
            else if (Date.class.equals(creationClass))
            {
                return (T) Calendar.getInstance().getTime();
            }
            else if (Long.class.equals(creationClass) ||
                    long.class.equals(creationClass))
            {
                return (T) Long.valueOf(0);
            }
            else if (Character.class.equals(creationClass) ||
                    char.class.equals(creationClass))
            {
                return (T) Character.valueOf('\0');
            }
            else if (Float.class.equals(creationClass) ||
                    float.class.equals(creationClass))
            {
                return (T) Float.valueOf(0);
            }
            else if (Double.class.equals(creationClass) ||
                    double.class.equals(creationClass))
            {
                return (T) Double.valueOf(0);
            }
            else if(creationClass.isEnum() && creationClass.getEnumConstants().length > 0)
            {
                return (T)creationClass.getEnumConstants()[0];
            }
            else if (SortedSet.class.equals(creationClass) || Set.class.equals(creationClass))
            {
                return (T)new TreeSet<>();
            }
            else if (Collection.class.equals(creationClass)
                    || List.class.equals(creationClass))
            {
                return (T)new ArrayList();
            }
            else if (Map.class.equals(creationClass))
            {
                return (T)new HashMap<>();
            }
            else if (!className.startsWith("java.util.") &&
                    Modifier.isAbstract(creationClass.getModifiers()))
                return null;

            Constructor<T> constructor = (Constructor<T>) creationClass.getDeclaredConstructor();


            return (T) constructor.newInstance();

        }
        catch (IllegalAccessException e)
        {
            throw new SetupException("Trying to create " + creationClass.getName() + " " + e.getMessage());
        }
        catch (NoSuchMethodException | InvocationTargetException | InstantiationException e)
        {
            //Get constructor
            Constructor<?>[] constructors = creationClass.getConstructors();

            int len = constructors.length;
            for (int i = 0; i < len; i++)
            {
                Class<?>[] parameterTypes = constructors[i].getParameterTypes();

                if (parameterTypes == null || parameterTypes.length == 0)
                    throw new SetupException("Trying to create " + creationClass.getName() + " " + e.getMessage()); //has a default constructor but not able to create it

                //Construct arguments
                Object[] initargs = new Object[parameterTypes.length];

                for (int argsIndex = 0; argsIndex < initargs.length; argsIndex++)
                {
                    initargs[argsIndex] = newInstance(parameterTypes[argsIndex]);
                }

                try
                {
                    return (T) constructors[i].newInstance(initargs);
                }
                catch (Exception exception)
                {
                }
            }//end for constructors

            //not able to success use another constructor
            throw new SetupException("Trying to create class with name:" + creationClass.getName() + " ERROR:" + e.getMessage());
        }//end catch
    }

    /**
     * @param className the class name
     * @return the loaded class
     * @throws ClassNotFoundException when the class not found
     */
    public Class<?> loadClass(String className)
    throws ClassNotFoundException
    {
        return findClass(className);
    }

    /**
     * Load all classes in the fileJar
     *
     * @param fileJar
     * @throws IOException
     */
    public void loadJar(File fileJar)
    throws IOException
    {
        JarFile jar = null;

        try
        {
            jar = new JarFile(fileJar);

            Enumeration<JarEntry> enumerations = jar.entries();
            JarEntry entry = null;
            byte[] classByte = null;
            ByteArrayOutputStream byteStream = null;

            String fileName;
            String className;
            Class<?> jarClass;
            while (enumerations.hasMoreElements())
            {
                entry = (JarEntry) enumerations.nextElement();

                if (entry.isDirectory())
                    continue;  //skip directories

                fileName = entry.getName();

                if (!fileName.endsWith(".class"))
                    continue; //skip non classes
                InputStream is = jar.getInputStream(entry);
                byteStream = new ByteArrayOutputStream();

                IO.writer().write(byteStream, is);
                classByte = byteStream.toByteArray();

                className = formatClassName(fileName);

                Debugger.println(this, "className=" + className);
                jarClass = defineClass(className, classByte, 0, classByte.length, null);
                classes.put(className, jarClass);

                classByte = null;
                byteStream = null;
            }//end while
        }
        finally
        {
            if (jar != null)
            {
                try
                {
                    jar.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Example &quot;pos/solution.TestObj&quot; will return &quot;solution.TestObj&quot;
     *
     * @param fileName the file name
     * @return the class path
     */
    public static String formatClassName(String fileName)
    {
        if (fileName == null || fileName.length() == 0)
            return null;

        String className = fileName.replace('/', '.');
        return className.replace(".class", "");
    }

    /**
     * Find the class by it name
     *
     * @param className the class name
     * @return the existing of loaded class
     */
    public Class<?> findClass(String className)
    {

        Class<?> result = null;

        // look in hash map
        result = (Class<?>) classes.get(className);
        if (result != null)
        {
            return result;
        }

        try
        {
            return findSystemClass(className);
        }
        catch (Exception e)
        {
            Debugger.printWarn(e);
        }

        try
        {
            URL resourceURL = ClassLoader.getSystemResource(className.replace(
                    '.', File.separatorChar) + ".class");

            if (resourceURL == null)
                return null;

            String classPath = resourceURL.getFile();

            classPath = classPath.substring(1);

            return loadClass(className, new File(classPath));
        }
        catch (Exception e)
        {
            Debugger.printError(e);
            return null;
        }
    }

    public Class<?> loadClass(String className, File classPathFile)
    throws IOException, ClassFormatError
    {
        byte[] classBytes = loadClassBytes(classPathFile);

        Class<?> result = defineClass(className, classBytes, 0, classBytes.length,
                null);

        classes.put(className, result);
        return result;
    }

    /**
     * Load the class name
     *
     * @param classFile the path of the class
     * @return the byte of the class
     * @throws IOException
     */
    private byte[] loadClassBytes(File classFile)
    throws IOException
    {

        int size = (int) classFile.length();
        byte buff[] = new byte[size];

        FileInputStream fis = new FileInputStream(classFile);
        DataInputStream dis = null;

        try
        {
            dis = new DataInputStream(fis);
            dis.readFully(buff);
        }
        finally
        {
            if (dis != null)
                dis.close();
        }

        return buff;
    }

    private Hashtable<String, Class<?>> classes = new Hashtable<String, Class<?>>();

}
