package nyla.solutions.core.patterns.creational.generator;


import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.util.Digits;
import nyla.solutions.core.util.JavaBean;
import nyla.solutions.core.util.Text;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <pre>
 *
 * UserProfile protoype = new UserProfile();
 * protoype.setFirstName("Imani");
 * protoype.setLastName("Green");
 *
 * JavaBeanGeneratorCreator<UserProfile> creator = new JavaBeanGeneratorCreator<UserProfile>(protoype);
 *
 * creator.randomizeProperty("email");
 *
 * UserProfile u1 = creator.create();
 * assertEquals(u1.getFirstName(),protoype.getFirstName());
 * assertEquals(u1.getLastName(),protoype.getLastName());
 * assertNotNull(u1.getEmail());
 * assertTrue(u1.getEmail().length() &gt; 0);
 * </pre>
 *
 * @param <T> the object type to create
 * @author Gregory Green
 */
public class JavaBeanGeneratorCreator<T> implements Creator<T>
{
    private Set<Class<?>> generateNestedClassSet = null;
    private boolean mustGenerateNestedAll = false;
    private Digits digits = new Digits();
    private Set<String> randomizeProperties = new HashSet<String>();
    private final Class<T> creationClass;
    private final T prototype;
    private Set<String> fixSet = null;
    private boolean mustRandomizeAll = false;
    private boolean throwExceptionForMissingProperty = false;
    private final Map<String, Creator<?>> creatorForClassMap;
    private DateTimeFormatter textDateFormat = DateTimeFormatter.ISO_DATE;
    private final CreatorFactoryByPropertyName creatorFactoryByPropertyName;

    private JavaBeanGeneratorCreator(Class<T> creationClass,
                                     Set<Class<?>> generateNestedClassSet,
                                     boolean mustGenerateNestedAll,
                                     Digits digits,
                                     Set<String> fixSet,
                                     boolean mustRandomizeAll,
                                     Map<String, Creator<?>> creatorForClassMap)
    {
        this(creationClass, null);
        this.generateNestedClassSet = generateNestedClassSet;
        this.mustGenerateNestedAll = mustGenerateNestedAll;
        this.digits = digits;
        this.fixSet = fixSet;
        this.mustRandomizeAll = mustRandomizeAll;
        if (mustRandomizeAll)
            this.randomizeAll();



        this.creatorForClassMap.putAll(creatorForClassMap);
    }

    /**
     * @param prototype the prototype
     */
    @SuppressWarnings("unchecked")
    public JavaBeanGeneratorCreator(T prototype)
    {
        this((Class<T>) prototype.getClass(), prototype);
    }

    /**
     * @param clz the class
     */
    public JavaBeanGeneratorCreator(Class<T> clz)
    {
        this(clz, null);
    }

    /**
     * @param prototype the prototype to use for created objects
     * @param clz       the class
     */
    public JavaBeanGeneratorCreator(Class<T> clz, T prototype)
    {
        this.creationClass = clz;
        this.prototype = prototype;
        this.creatorForClassMap = new HashMap<>();

        this.creatorFactoryByPropertyName = new CreatorFactoryByPropertyName(this.textDateFormat);

        creatorForClassMap.put(String.class.getName(), () -> Text.generator().generateId());
        creatorForClassMap.put(Integer.class.getName(), () -> digits.generateInteger());
        creatorForClassMap.put(int.class.getName(), () -> digits.generateInteger());
        creatorForClassMap.put(Long.class.getName(), () -> digits.generateLong());
        creatorForClassMap.put(long.class.getName(), () -> digits.generateLong());
        creatorForClassMap.put(short.class.getName(), () -> digits.generateShort());
        creatorForClassMap.put(Short.class.getName(), () -> digits.generateShort());
        creatorForClassMap.put(Double.class.getName(), () -> digits.generateDouble());
        creatorForClassMap.put(double.class.getName(), () -> digits.generateDouble());
        creatorForClassMap.put(char.class.getName(), () -> Text.generator().generateId().charAt(0));
        creatorForClassMap.put(Character.class.getName(), () -> Text.generator().generateId().charAt(0));
        creatorForClassMap.put(float.class.getName(), () -> digits.generateFloat());
        creatorForClassMap.put(Float.class.getName(), () -> digits.generateFloat());
        creatorForClassMap.put(byte.class.getName(), () -> Text.generator().generateId().getBytes(IO.CHARSET)[0]);
        creatorForClassMap.put(Byte.class.getName(), () -> Text.generator().generateId().getBytes(IO.CHARSET)[0]);
        creatorForClassMap.put(Currency.class.getName(), () -> Currency.getInstance(Locale.US));

        Creator<Boolean> booleanCreator = () ->
        {
            if (Calendar.getInstance().getTime().getTime() % 2 == 0)
                return true;
            else
                return false;
        };

        creatorForClassMap.put(Boolean.class.getName(), booleanCreator);
        creatorForClassMap.put(boolean.class.getName(), booleanCreator);
        creatorForClassMap.put(BigDecimal.class.getName(), () -> digits.generateBigDecimal());
        creatorForClassMap.put(java.sql.Time.class.getName(), () -> new java.sql.Time(
                Calendar.getInstance().getTime().getTime()));

        creatorForClassMap.put(java.sql.Date.class.getName(), () -> new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        creatorForClassMap.put(java.util.Date.class.getName(), () -> Calendar.getInstance().getTime());
        creatorForClassMap.put(java.sql.Timestamp.class.getName(), () -> new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        creatorForClassMap.put(java.util.Calendar.class.getName(), () -> Calendar.getInstance());
        creatorForClassMap.put(LocalTime.class.getName(), () -> LocalTime.now());
        creatorForClassMap.put(LocalDate.class.getName(), () -> LocalDate.now());
        creatorForClassMap.put(LocalDateTime.class.getName(), () -> LocalDateTime.now());
        creatorForClassMap.put(BigInteger.class.getName(), () -> BigInteger.ONE);
        creatorForClassMap.put(java.time.Duration.class.getName(), ()-> Duration.ofMillis(System.currentTimeMillis()));

    }

    public static<T> JavaBeanGeneratorCreator<T> of(Class<T> objClass)
    {
        return new JavaBeanGeneratorCreator(objClass).randomizeAll().generateNestedAll();
    }

    public JavaBeanGeneratorCreator setTextDateFormat(DateTimeFormatter textDateFormat)
    {
        this.textDateFormat = textDateFormat;
        return this;
    }

    /**
     * Create a new instance on the settings
     *
     * @return the created instance
     */
    @Override
    public T create()
    {
        try
        {
            if(java.lang.Record.class.isAssignableFrom(creationClass))
                return createFromRecord(creationClass);

            T obj = ClassPath.newInstance(creationClass);


            //Copy prototype properties
            if (this.prototype != null)
                JavaBean.populate(JavaBean.toMap(prototype), obj);

            PropertyDescriptor pd;
            Class<?> clz;


            for (String property : randomizeProperties)
            {
                try
                {
                    pd = JavaBean.getPropertyDescriptor(obj, property);
                    if(pd == null)
                        continue; //skip

                    clz = pd.getPropertyType();


                    Creator<?> creator = determineCreator(clz,pd);


                    if (creator != null)
                    {
                        Object value;

                        try
                        {
                            value = creator.create();
                        }
                        catch (RuntimeException e)
                        {
                            throw new IllegalArgumentException("Cannot create class:" + clz.getName()
                                    + " property:" + property
                                    + " for object:" + obj, e);
                        }

                        JavaBean.setProperty(obj, property, value, throwExceptionForMissingProperty);
                        continue;
                    }


                    if (clz.isAssignableFrom(Class.class))
                        continue; //skip enum  and nested same obj

                    if (clz.isEnum())
                    {
                        Object myenum = Enum.valueOf((Class) clz, clz.getEnumConstants()[0].toString());
                        JavaBean.setProperty(obj, property, myenum, throwExceptionForMissingProperty);
                        continue;
                    }
                    else if (!clz.getName().startsWith("java.") && (mustGenerateNestedAll || (generateNestedClassSet != null && generateNestedClassSet.contains(clz))))
                    {
                        JavaBeanGeneratorCreator<?> javaBeanGeneratorCreator = this.cloneForClass(clz);
                        JavaBean.setProperty(obj, property, javaBeanGeneratorCreator.create(), throwExceptionForMissingProperty);
                    }
                    else
                    {
                        JavaBean.setProperty(obj, property, ClassPath.newInstance(clz), throwExceptionForMissingProperty);
                    }
                }
                catch (Exception e)
                {
                    throw new SetupException("Cannot create property:" + property + " for object class:"
                            + this.creationClass.getName() + "  ERROR:" + e.getMessage(), e);
                }
            }

            return obj;
        }
        catch (RuntimeException e)
        {
            throw e;

        }
        catch (
                Exception e)

        {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    private T createFromRecord(Class<T> creationClass) {

        var constructors = creationClass.getConstructors();

        var parameters = constructors[0].getParameters();

        Object[] args = new Object[parameters.length];
        Parameter parameter = null;
        Creator<?> creator;
        Class<?> parameterType;

        for (int i=0; i <  parameters.length;i++) {
            parameter = parameters[i];

            parameterType = parameter.getType();

            creator = determineCreator(parameterType, parameter.getName());


            if(creator == null && !parameterType.getName().startsWith("java.") && (mustGenerateNestedAll || (generateNestedClassSet != null && generateNestedClassSet.contains(parameterType)))) {
                creator = this.cloneForClass(parameterType);
            }

            if(creator != null)
                args[i] = creator.create();
            else
            {
                args[i] = ClassPath.newInstance(parameterType);
            }
        }

        //newInstance(Class<?> aClass, Class<?>[] parameterTypes, Object[] initargs)
        return ClassPath.newInstance(creationClass,constructors[0].getParameterTypes(),args);
    }

    protected Creator<?> determineCreator(Class<?> clz, PropertyDescriptor pd)
    {
        return determineCreator(clz,pd.getName());
    }
    protected Creator<?> determineCreator(Class<?> clz, String propertyName)
    {
        propertyName = propertyName.toLowerCase();

        Creator<?> creator = creatorForClassMap.get(propertyName);
        if(creator != null)
            return creator;

        String cacheMapKey = new StringBuilder()
                .append(clz.getName())
                .append(".")
                .append(propertyName).toString();

        creator = creatorForClassMap.get(cacheMapKey);

        if(creator != null)
            return creator;

        if(String.class.equals(clz))
        {
            if(propertyName != null ){
                String lowerProperty = propertyName.toLowerCase();

                creator = creatorFactoryByPropertyName.forProperty(lowerProperty);
                this.creatorForClassMap.put(cacheMapKey,creator);

                return creator;


            }
        }
        return this.creatorForClassMap.get(clz.getName());
    }



    /**
     * Setup property to generate a random value
     *
     * @param property the property randomize
     * @return the creator instance
     */
    public JavaBeanGeneratorCreator<T> randomizeProperty(String property)
    {
        if (property == null || property.length() == 0)
            return this;

        this.randomizeProperties.add(property);

        return this;
    }

    public JavaBeanGeneratorCreator<T> randomizeAll()
    {
        this.mustRandomizeAll = true;
        this.randomizeProperties = JavaBean.getPropertyNames(this.creationClass);
        return this;
    }

    /**
     * Randomized to records that are not in fixed list
     *
     * @param fixedPropertyNames the fixed list of property names
     * @return the creator factory
     */
    public JavaBeanGeneratorCreator<T> fixedProperties(String... fixedPropertyNames)
    {
        if (fixedPropertyNames == null || fixedPropertyNames.length == 0)
        {
            return this;
        }

        fixSet = new HashSet<>(Arrays.asList(fixedPropertyNames));

        Map<Object, Object> map = null;

        if (this.prototype != null)
        {
            map = JavaBean.toMap(this.prototype);
        }
        else
            map = JavaBean.toMap(ClassPath.newInstance(this.creationClass));

        if (map == null || map.isEmpty())
            return this;

        String propertyName = null;
        for (Object propertyNameObject : map.keySet())
        {
            propertyName = String.valueOf(propertyNameObject);
            if (!fixSet.contains(propertyName))
            {
                this.randomizeProperty(propertyName);
            }
        }

        return this;
    }

    public JavaBeanGeneratorCreator<T> generateNestedClass(Class<?> aClass)
    {
        if (this.generateNestedClassSet == null)
            this.generateNestedClassSet = new HashSet<>();

        generateNestedClassSet.add(aClass);
        return this;
    }

    /**
     * @return creator the generates all nested complex objects
     */
    public JavaBeanGeneratorCreator<T> generateNestedAll()
    {
        this.mustGenerateNestedAll = true;
        return this;
    }


    public <NewType> JavaBeanGeneratorCreator<NewType> cloneForClass(Class<NewType> newClass)
    {
        return new
                JavaBeanGeneratorCreator(newClass,
                this.generateNestedClassSet,
                this.mustGenerateNestedAll,
                this.digits,
                this.fixSet,
                this.mustRandomizeAll,
                this.creatorForClassMap
        );
    }

    /**
     * @param aClass       the class to use supplier
     * @param creator      the supplier implementation
     * @param <ObjectType> the class type
     * @return the JavaBeanGeneratorCreator with supplier
     */
    public <ObjectType> JavaBeanGeneratorCreator<T> creatorForClass(Class<ObjectType> aClass, Creator<ObjectType> creator)
    {
        creatorForClassMap.put(aClass.getName(), creator);
        return this;
    }

    protected Map<String, Creator<?>> getCreatorForClassMap()
    {
        return creatorForClassMap;
    }

    /**
     * @return the randomizeProperties
     */
    protected Set<String> getRandomizeProperties()
    {
        return randomizeProperties;
    }


    /**
     * Create a collection of objects
     * @param count the collection count
     * @return the collection of created objects
     */
    public Collection<T> createCollection(int count)
    {
        if(count <= 0)
            return Collections.emptyList();

        ArrayList<T> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(create());
        }
        return list;
    }

    public <ObjectType> JavaBeanGeneratorCreator<T> creatorForProperty(String propertyName, Creator<ObjectType> customCreator) {
        this.randomizeProperty(propertyName);
        this.creatorForClassMap.put(propertyName,customCreator);
        return this;
    }
}
