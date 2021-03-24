package nyla.solutions.core.patterns.reflection;

import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.patterns.search.ReLookup;
import nyla.solutions.core.security.user.data.UserProfile;
import nyla.solutions.core.util.Config;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Gregory Green
 *
 */
public class ObjectClassSizer
{
	private int defaultStringSizeBytes = Config.getPropertyInteger(ObjectClassSizer.class,"defaultStringSizeBytes", 16).intValue();
	private ReLookup<Long> fieldDefaultSizes = new ReLookup<Long>();
	private Map<Class<?>,Long> defaultTypeSizes;
	private static volatile Instrumentation globalInstrumentation;

	public static void premain(final String agentArgs, final Instrumentation inst) {
		globalInstrumentation = inst;
	}

	public static long getObjectSize(final Object object) {
		if (globalInstrumentation == null) {
			throw new IllegalStateException("Agent not initialized.");
		}
		return globalInstrumentation.getObjectSize(object);
	}
	/**
	 * Construct object with a default object
	 * class size map.
	 * <pre>
	 * {@code
	 * 		this.defaultTypeSizes = new HashMap<Class<?>, Long>();
		this.defaultTypeSiz`es.put(String.class, Long.valueOf(defaultStringSizeBytes));  //100 KB
		this.defaultTypeSizes.put(boolean.class,Long.valueOf(1));
		this.defaultTypeSizes.put(Boolean.class,Long.valueOf(2));
		this.defaultTypeSizes.put(byte.class,Long.valueOf(1));
		this.defaultTypeSizes.put(Byte.class,Long.valueOf(2));
		this.defaultTypeSizes.put(char.class, Long.valueOf(2));
		this.defaultTypeSizes.put(Character.class, Long.valueOf(4));
		this.defaultTypeSizes.put( short.class,Long.valueOf(2));
		this.defaultTypeSizes.put( Short.class,Long.valueOf(4));
		this.defaultTypeSizes.put( int.class,Long.valueOf(4));
		this.defaultTypeSizes.put( Integer.class,Long.valueOf(8));
		this.defaultTypeSizes.put(long.class, Long.valueOf(8));
		this.defaultTypeSizes.put(Long.class, Long.valueOf(18));
		this.defaultTypeSizes.put(float.class, Long.valueOf(4));
		this.defaultTypeSizes.put(Float.class, Long.valueOf(8));
		this.defaultTypeSizes.put(double.class, Long.valueOf(8));
		this.defaultTypeSizes.put(Double.class, Long.valueOf(16));
        this.defaultTypeSizes.put(Void.class, Long.valueOf(0));
        this.defaultTypeSizes.put(BigDecimal.class, Long.valueOf(32));
        this.defaultTypeSizes.put(BigInteger.class, Long.valueOf(56));
	}
	 </pre>
	 */
	public ObjectClassSizer()
	{
		this.defaultTypeSizes = new HashMap<Class<?>, Long>();
		this.defaultTypeSizes.put(String.class, Long.valueOf(defaultStringSizeBytes));  //100 KB
		this.defaultTypeSizes.put(boolean.class,Long.valueOf(1));
		this.defaultTypeSizes.put(Boolean.class,Long.valueOf(2));
		this.defaultTypeSizes.put(byte.class,Long.valueOf(1));
		this.defaultTypeSizes.put(Byte.class,Long.valueOf(2));
		this.defaultTypeSizes.put(char.class, Long.valueOf(2));
		this.defaultTypeSizes.put(Character.class, Long.valueOf(4));
		this.defaultTypeSizes.put( short.class,Long.valueOf(2));
		this.defaultTypeSizes.put( Short.class,Long.valueOf(4));
		this.defaultTypeSizes.put( int.class,Long.valueOf(4));
		this.defaultTypeSizes.put( Integer.class,Long.valueOf(8));
		this.defaultTypeSizes.put(long.class, Long.valueOf(8));
		this.defaultTypeSizes.put(Long.class, Long.valueOf(18));
		this.defaultTypeSizes.put(float.class, Long.valueOf(4));
		this.defaultTypeSizes.put(Float.class, Long.valueOf(8));
		this.defaultTypeSizes.put(double.class, Long.valueOf(8));
		this.defaultTypeSizes.put(Double.class, Long.valueOf(16));
		this.defaultTypeSizes.put(Date.class, Long.valueOf(32));
        this.defaultTypeSizes.put(Void.class, Long.valueOf(0));
        this.defaultTypeSizes.put(BigDecimal.class, Long.valueOf(32));
        this.defaultTypeSizes.put(BigInteger.class, Long.valueOf(56));

	}// --------------------------------------------------------

	/**
	 *
	 * @param aClass the class to estimate
	 * @return the long size
	 */
    public long sizeInBytes(Class<?> aClass)
    {
    	 	if(!String.class.equals(aClass) && ClassPath.isPrimitive(aClass) )
         {

         	Long sizeLong = this.defaultTypeSizes.get(aClass);
         	return sizeLong.longValue();
         }


    	 //test if is enum
    	 if(aClass.isEnum())
    	 {
    		Object[] enumConstants =  aClass.getEnumConstants();
    		long enumEntyrSize = defaultTypeSizes.get(int.class).longValue();

    		if(enumConstants == null || enumConstants.length == 0)
    			return enumEntyrSize;
    		else
    			return enumEntyrSize * enumConstants.length + 1;

    	 }

    	 //check if in default type size
    	 Long defaultTypeSize = defaultTypeSizes.get(aClass);

    	 if(defaultTypeSize != null)
    		 return defaultTypeSize.longValue();

        Field fields[] = aClass.getDeclaredFields();
        Field arr[] = fields;
        int len = arr.length;
        long totalSize = 0;

        for(int i = 0; i < len; i++)
        {
            Field field = arr[i];

            //check if field name has a default size
            String fieldName = field.getName();
           //check if field name has a size

            Long fieldNameLengthBytes = fieldDefaultSizes.get(fieldName);

            Class<?> fieldClass  = field.getType();

            if(fieldNameLengthBytes != null)
            {
            	//Total by field name
            	totalSize += fieldNameLengthBytes.longValue();
            }
            else if(String.class.equals(fieldClass) || char[].class.equals(fieldClass))
            {
            	totalSize += defaultStringSizeBytes;

            }
            else if(ClassPath.isPrimitive(fieldClass) )
            {

            	Long sizeLong = this.defaultTypeSizes.get(fieldClass);
            	totalSize +=  sizeLong.longValue();
            }
            else
            {
            	//nested call
            	totalSize += sizeInBytes(field.getType());
            }

        }

        return totalSize;
    }// --------------------------------------------------------

	/**
	 * @return the defaultStringSizeBytes
	 */
	public int getDefaultStringSizeBytes() {
		return defaultStringSizeBytes;
	}
	/**
	 * @param defaultStringSizeBytes the defaultStringSizeBytes to set
	 */
	public void setDefaultStringSizeBytes(int defaultStringSizeBytes) {
		this.defaultStringSizeBytes = defaultStringSizeBytes;
	}
	/**
	 * @return the fieldDefaultSizes
	 */
	public ReLookup<Long> getFieldDefaultSizes() {
		return fieldDefaultSizes;
	}
	/**
	 * @param fieldDefaultSizes the fieldDefaultSizes to set
	 */
	public void setFieldDefaultSizes(ReLookup<Long> fieldDefaultSizes) {
		this.fieldDefaultSizes = fieldDefaultSizes;
	}
	/**
	 * @return the defaultTypeSizes
	 */
	public Map<Class<?>, Long> getDefaultTypeSizes() {
		return defaultTypeSizes;
	}

	/**
	 * @param defaultTypeSizes the defaultTypeSizes to set
	 */
	public void setDefaultTypeSizes(Map<Class<?>, Long> defaultTypeSizes) {
		this.defaultTypeSizes = defaultTypeSizes;
	}


	public static void main(String[] args)
	{
		UserProfile userProfile = new JavaBeanGeneratorCreator<>(UserProfile.class)
				.randomizeAll().create();
		System.out.println("Object type: " + userProfile.getClass() +
				", size: " + ObjectClassSizer.getObjectSize(userProfile) + " bytes");
	}

}
