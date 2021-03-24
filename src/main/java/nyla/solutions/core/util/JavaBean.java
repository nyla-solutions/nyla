package nyla.solutions.core.util;

import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.conversion.PropertyConverter;
import nyla.solutions.core.patterns.reflection.JavaBeanVisitor;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;



/**
 * 
 * <pre>
 * JavaBean provides a set of functions to manage JavaBeans.
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class JavaBean
{
	/**
	 * Create new instance of the object
	 * @param aValues the map value
	 * @param aClass the class to create
	 * @param <T> the type of the bean
	 * @return the create object instance
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	 public static <T> T newBean(Map<?,?> aValues, Class<?> aClass)
	 throws InstantiationException, IllegalAccessException
	 {
		 return newBean(aValues,aClass,null);
	 }// --------------------------------------------------------
	  
    /**
	 * Create new instance of the object
	 * @param aValues the map value
	 * @param aClass the class to create
	 * @param <T> the type class
	 * @param convert the conversion implementation
	 * @return the create object instance
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	 public static <T> T newBean(Map<?,?> aValues, Class<?> aClass, PropertyConverter<Object,Object> convert)
	 throws InstantiationException, IllegalAccessException
	{
		   T obj = ClassPath.newInstance(aClass);
		   
		   populate(aValues,obj,convert);
		   
		   return obj;
		   
	 }// --------------------------------------------------------
	 /**
	  * Support nested map objects
	  * @param values the key/value properties
	  * @param bean the object to write to
	  * 
	  */
   public static void populate(Map<?,?> values, Object bean)
   {
	  populate(values,bean,null);
   }
	 // --------------------------------------------------------
   
   /**
	  * Support nested map objects
	  * @param aValues the key/value properties
	  * @param bean the object to write to
	  * @param valueConverter the value converter strategy object
	  * 
	  */
   public static void populate(Map<?,?> aValues, Object bean, PropertyConverter<Object, Object> valueConverter)
   {
        Object key = null;
	    
	    Object keyValue;
	    try
		{

			for(Map.Entry<?, ?> entry : aValues.entrySet())
			 {
			     key = entry.getKey();
			     
			   	 keyValue = entry.getValue();
			    	 
			    	 PropertyDescriptor desc =  getPropertyDescriptor(bean, String.valueOf(key));
			    	 if(desc == null)
		    			 continue; //skip
			    	 
			    	 if(desc.getWriteMethod() == null)
			    		 continue;
			    	 
			    	 if(valueConverter!= null)
			    	 {
			    		 Class<?> aClass = desc.getPropertyType();
			    		 
			    		 keyValue = valueConverter.convert(keyValue,aClass);
			    	 }
			    
			    	 Method invoke = desc.getWriteMethod();
			    	 invoke.invoke(bean, keyValue);
		  }
	}
	catch (Exception e)
	{
		throw new FormatException(e.getMessage()+" values:"+aValues,e);
	}
 }//-------------------------------------------
	
	
   /***
    * Given a given object, use reflection to determine qualities.
    * Call the appropriate visitor method to provide the given attributes
    * @param bean the object to derive information
    * @param visitor object/operation that will be provided the needed information
    */
   public static void acceptVisitor(Object bean, JavaBeanVisitor visitor)
   {
	   
	   if(bean == null)
		   return;
	   
	   Class<?> beanClass = bean.getClass();
	   
	   if(ClassPath.isPrimitive(beanClass))
	   {
		   visitor.visitClass(beanClass, bean);
		   
		   return; //no properties
	   }
	   
 	  PropertyDescriptor descriptors[] = getPropertyDescriptors(bean);
       
       Object value = null;
       for(int i = 0; i < descriptors.length; i++)
       {
               String name = descriptors[i].getName();
               if("class".equals(name))
               {
                        visitor.visitClass((Class<?>)getProperty(bean, name),bean);
                        continue;
                        
               }
                
               if(descriptors[i].getReadMethod() != null)
               {
                  value = getProperty(bean, name);
                  
                 visitor.visitProperty(name, value,bean);                 
               }
                 
       }
 	  
   }// --------------------------------------------------------
   /**
    * Set property
    * @param bean the bean
    * @param propertyName the property name
    * @param value the value to set
    */
   public static void setProperty(Object bean, String propertyName, Object value)
   {
	    setProperty(bean,propertyName,value,true);
   }//------------------------------------------------
   /**
    * Set property
    * @param bean the bean
    * @param propertyName the property name
    * @param value the value to set
    * @param throwExceptionForMissingProperty determine if throw an exception if the property does not exist 
    */
   public static void setProperty(Object bean, String propertyName, Object value,boolean throwExceptionForMissingProperty)
   {
      try
      {
         //find key ignoreClase
         //PropertyUtils.setProperty(aBean, aPropName, aValue);
    	  PropertyDescriptor propertyDescriptor = getPropertyDescriptor(bean,propertyName);
    	  if(propertyDescriptor == null)
    	  {
    		  throw new IllegalArgumentException("propertyName:"+propertyName+" not found for bean:"+bean);
    	  }
    	  Method writeMethod = propertyDescriptor.getWriteMethod();
    	  
    	  if(writeMethod == null)
    	  {
    		  if(throwExceptionForMissingProperty)
    			  throw new IllegalArgumentException("Write method not found for propertyName:"+propertyName+" for bean:"+bean);
    		  else
    			  return; //ignore missing set method for property
    	  }
    	  writeMethod.invoke(bean, value);
    	  
      }
      catch(IllegalArgumentException e)
      {
    	  if(!(value instanceof String))
    		  throw e;
    		  
    	try
  		{
    	  //convert value
    	  PropertyDescriptor desc = getPropertyDescriptor(bean, propertyName);
    	  
    	  String text = (String)value;
    	  
    
			value = Text.toObject(text, desc.getPropertyType().getName());
			  
			  //try again
			  PropertyDescriptor propertyDescriptor = getPropertyDescriptor(bean,propertyName);
	    	  propertyDescriptor.getWriteMethod().invoke(bean, value);
		}
		catch(Exception innerError)
		{
			//failed conversion
			//throw original exception
			throw e;
		}
    	  
      }
      catch(NoSuchMethodException e)
      {
         try
         {
            //find key ignores         
            Object key = Organizer.findByTextIgnoreCase(keySet(bean),propertyName );
           
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(bean,String.valueOf(key));
      	  propertyDescriptor.getWriteMethod().invoke(bean, value);
         }
         catch (Exception e1)
         {
            String typeName = "null";
            
            if(value != null)
               typeName = value.getClass().getName();
            
            throw new SystemException("type="+typeName+" "+Debugger.toString(e));
         }
      }
      catch(Exception e)
      {
         String typeName = "null";
         
         if(value != null)
            typeName = value.getClass().getName();
         
         throw new SystemException("type="+typeName+" propery:"+propertyName+" ERROR:"+e.getMessage(),e);
      }
   }// --------------------------------------------
   /**
    * 
    * @param aBean the object
    * @return the key set from map verison of object
    */
   public static Set<Object> keySet(Object aBean)
   {
      if(aBean == null)
         return new HashSet<Object>();
      
      Map<Object,Object> beanMap = toMap(aBean);
      
      return beanMap.keySet();

   }// --------------------------------------------


   /**
    * 
    * @param bean the bean object version of object
    * @return the map version of the of the object 
    */
  public static Map<Object, Object> toMap(Object bean)
  {
      if(bean == null)
          throw new IllegalArgumentException("No bean specified");
      

	   if(ClassPath.isPrimitive(bean.getClass()))
		{
		   HashMap<Object,Object> wrapMap = new HashMap<Object,Object>();
		   wrapMap.put(bean,null);
		   
		   return wrapMap;
		}
			
       
      Map<Object, Object> description = new HashMap<Object, Object>();
      
      PropertyDescriptor descriptors[] = getPropertyDescriptors(bean);
       
      Object value = null;
      for(int i = 0; i < descriptors.length; i++)
      {
              String name = descriptors[i].getName();
              if("class".equals(name))
              {
                       continue;
              }
               
              if(descriptors[i].getReadMethod() != null)
              {
                 value = getProperty(bean, name);
                 
                 if(value == null )
                 {
                    //create empty string
                    value = "";
                 }
                 
                 description.put(name, value);                 
              }
                
      }
             
      return description;
  }//---------------------------------------------------------------------    
    /**
     * 
     * @param bean the bean object
     * @return map version of object
     * @throws Exception
     */
   public static Map<?,?> toNestedMap(Object bean)
       throws Exception
   {
       if(bean == null)
           throw new IllegalArgumentException("No bean specified");
       
       Map<Object,Object> description = new HashMap<Object,Object>();
      
       PropertyDescriptor descriptors[] = getPropertyDescriptors(bean);
       
       Object value = null;
       for(int i = 0; i < descriptors.length; i++)
       {
               String name = descriptors[i].getName();              
               
               if("class".equals(name))
               {
                        continue;
               }
               
               if(descriptors[i].getReadMethod() != null)
               {
                  value = getProperty(bean, name);
                  if(isSimple(value))
                  {
                     description.put(name, value);
                  }                   
                  else if(value instanceof Collection)
                  {
                     description.put(name, toCollectionMap((Collection<?>)value));                     
                  }
                  else
                  {
                     description.put(name, toNestedMap(value));
                  }
               }
                
       }
             
       return description;
   }//---------------------------------------------------------------------
   public static Collection<Object> toCollectionMap(Collection<?> aCollection)
   throws Exception
   {
      if(aCollection == null )
         return null;
         
      List<Object> listMap = new ArrayList<Object>(aCollection.size());  

      Object nestedObj = null;
      for(Iterator<?> i= aCollection.iterator();i.hasNext();)
      {
                
          nestedObj = i.next();
            listMap.add(toNestedMap(nestedObj));
      }
              
      return listMap;
   } //--------------------------------------------------------   
   public static boolean isSimple(Object aObject)
   {
      if(aObject == null || 
         aObject.getClass() ==null || 
         aObject.getClass().getName() == null )       
         return true;
         
      return aObject instanceof Date ||                     
      aObject instanceof Number ||
      aObject instanceof String ||
      aObject instanceof Character ||
      aObject instanceof StringBuffer ||
      aObject.getClass().getName().indexOf("java.lang") > -1;
   }//----------------------------------------------------------
   /**
    * 
    * @param beanClass the bean classe to get descriptors
    * @return the property descriptor
    */
   private static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass)
   {
       if(beanClass == null)
           throw new IllegalArgumentException("No bean class specified");
       PropertyDescriptor descriptors[] = null;
       
       
       BeanInfo beanInfo = null;
       try
       {
           beanInfo = Introspector.getBeanInfo(beanClass);
       }
       catch(IntrospectionException e)
       {
           return new PropertyDescriptor[0];
       }
       descriptors = beanInfo.getPropertyDescriptors();
       if(descriptors == null)
           descriptors = new PropertyDescriptor[0];
       
       return descriptors;
   }//---------------------------------------------------------------------
   private static PropertyDescriptor[] getPropertyDescriptors(Object bean)
   {
       if(bean == null)
           throw new IllegalArgumentException("No bean specified");
       else
           return getPropertyDescriptors(bean.getClass());
   }
   /**
    * Retrieve the bean property
    * @param bean the bean
    * @param name the property name
    * @param <T> the type class of the property for casting
    * @return the property
    * @throws SystemException the an unknown error occurs
    */
   @SuppressWarnings("unchecked")
   public static <T> T getProperty(Object bean, String name)
       throws SystemException
   {
      try
      {
         return (T)getNestedProperty(bean, name);  
      }
      catch (Exception e)
      {
        throw new SystemException("Get property \""+name+"\" ERROR:"+e.getMessage(),e);
      }
   }//-------------------------------------------
   /**
    * 
    * @param bean the bean to get the property
    * @param name the name
    * @return the nested object
    * @throws IllegalAccessException the illegal access exception
    * @throws InvocationTargetException the invocation
    * @throws NoSuchMethodException no such method
    * @throws Exception when an unknown error occurs
    */
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public static Object getNestedProperty(Object bean, String name)
       throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception
   {
       if(bean == null)
           return null;
       
  
       if(name == null || name.length() == 0)
					throw new IllegalArgumentException("name required");
       
       if(Collection.class.isAssignableFrom(bean.getClass()))
       {
        	   return getCollectionProperties((Collection<?>)bean,name);
       }
       
       int indexOfINDEXED_DELIM = -1;
       int indexOfMAPPED_DELIM = -1;
       int indexOfMAPPED_DELIM2 = -1;
       int indexOfNESTED_DELIM = -1;
       do
       {
           
           indexOfNESTED_DELIM = name.indexOf('.');
           indexOfMAPPED_DELIM = name.indexOf('(');
           indexOfMAPPED_DELIM2 = name.indexOf(')');
           if(indexOfMAPPED_DELIM2 >= 0 && indexOfMAPPED_DELIM >= 0 && (indexOfNESTED_DELIM < 0 || indexOfNESTED_DELIM > indexOfMAPPED_DELIM))
               indexOfNESTED_DELIM = name.indexOf('.', indexOfMAPPED_DELIM2);
           else
               indexOfNESTED_DELIM = name.indexOf('.');
           if(indexOfNESTED_DELIM < 0)
               break;
           String next = name.substring(0, indexOfNESTED_DELIM);
           indexOfINDEXED_DELIM = next.indexOf('[');
           indexOfMAPPED_DELIM = next.indexOf('(');
           if(bean instanceof Map)
               bean = ((Map<Object,Object>)bean).get(next);
           else
           if(indexOfMAPPED_DELIM >= 0)
               bean = getMappedProperty(bean, next);
           else
           if(indexOfINDEXED_DELIM >= 0)
               bean = getIndexedProperty(bean, next);
           else
               bean = getSimpleProperty(bean, next);
           
           if(bean == null)
        	   return null;
           
           name = name.substring(indexOfNESTED_DELIM + 1);
           
           if(Collection.class.isAssignableFrom(bean.getClass()))
           {
            	   return getCollectionProperties((Collection<?>)bean,name);
           }

       } while(true);
       indexOfINDEXED_DELIM = name.indexOf('[');
       indexOfMAPPED_DELIM = name.indexOf('(');
       if(bean instanceof Map)
           bean = ((Map)bean).get(name);
       else
       if(indexOfMAPPED_DELIM >= 0)
           bean = getMappedProperty(bean, name);
       else
       if(indexOfINDEXED_DELIM >= 0)
           bean = getIndexedProperty(bean, name);
       else
           bean = getSimpleProperty(bean, name);
       
       return bean;
   }//--------------------------------------------------------
   /**
    * Retrieve the list of properties that exists in a given collection
    * @param collection the collection of object
    * @param name the name of property
    * @return collection of objects
    * @throws Exception when an unknown error occurs
    */
   public static Collection<Object> getCollectionProperties(Collection<?> collection,String name)
   throws Exception
   {
	   if(collection == null)
				throw new IllegalArgumentException("collection, name");
	   
	   ArrayList<Object> list = new ArrayList<Object>(collection.size());
	   
	   for (Object bean : collection)
		{
		   list.add(getNestedProperty(bean, name));
		}
	   
	   return list;
   }//--------------------------------------------------------
   public static Object getMappedProperty(Object bean, String name)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception
    {
        if(bean == null)
            throw new IllegalArgumentException("No bean specified");
        if(name == null)
            throw new IllegalArgumentException("No name specified");
        int delim = name.indexOf('(');
        int delim2 = name.indexOf(')');
        if(delim < 0 || delim2 <= delim)
        {
            throw new IllegalArgumentException("Invalid mapped property '" + name + "'");
        } else
        {
            String key = name.substring(delim + 1, delim2);
            name = name.substring(0, delim);
            return getMappedProperty(bean, name, key);
        }
    }
    
   public static Object getIndexedProperty(Object bean, String name)
       throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception
   {
       if(bean == null)
           throw new IllegalArgumentException("No bean specified");
       if(name == null)
           throw new IllegalArgumentException("No name specified");
       int delim = name.indexOf('[');
       int delim2 = name.indexOf(']');
       if(delim < 0 || delim2 <= delim)
           throw new IllegalArgumentException("Invalid indexed property '" + name + "'");
       int index = -1;
       try
       {
           String subscript = name.substring(delim + 1, delim2);
           index = Integer.parseInt(subscript);
       }
       catch(NumberFormatException e)
       {
           throw new IllegalArgumentException("Invalid indexed property '" + name + "'");
       }
       name = name.substring(0, delim);
       return getIndexedProperty(bean, name, index);
   }
   public static Object getSimpleProperty(Object bean, String name)
       throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,Exception
   {
       if(bean == null)
           throw new IllegalArgumentException("No bean specified");
       if(name == null)
           throw new IllegalArgumentException("No name specified");
       if(name.indexOf('.') >= 0)
           throw new IllegalArgumentException("Nested property names are not allowed");
       if(name.indexOf('[') >= 0)
           throw new IllegalArgumentException("Indexed property names are not allowed");
       if(name.indexOf('(') >= 0)
           throw new IllegalArgumentException("Mapped property names are not allowed");
       PropertyDescriptor descriptor;
       
       descriptor = getPropertyDescriptor(bean, name);
       if(descriptor == null)
           throw new NoSuchMethodException("Unknown property '" + name + "' in bean "+bean);
       Method readMethod = getReadMethod(descriptor);
       if(readMethod == null)
       {
           throw new NoSuchMethodException("Property '" + name + "' has no getter method");
       } else
       {
           Object value = readMethod.invoke(bean, new Object[0]);
           return value;
       }
   }
   
   public static Object getIndexedProperty(Object bean, String name, int index)
       throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception
   {
     Method readMethod;
      PropertyDescriptor descriptor;
      try
      {
          Object subscript[];
          if(bean == null)
              throw new IllegalArgumentException("No bean specified");
          if(name == null)
              throw new IllegalArgumentException("No name specified");
         
          descriptor = getPropertyDescriptor(bean, name);
          if(descriptor == null)
              throw new NoSuchMethodException("Unknown property '" + name + "'");
          if(!(descriptor instanceof IndexedPropertyDescriptor))
            throw new Exception();
            
          readMethod = ((IndexedPropertyDescriptor)descriptor).getIndexedReadMethod();
          if(readMethod == null)
              throw new Exception();
          subscript = new Object[1];
          subscript[0] = Integer.valueOf(index);
          return readMethod.invoke(bean, subscript);
      }
      
      catch (Exception e)
      {
         if(e instanceof ArrayIndexOutOfBoundsException)
         {     
              throw (ArrayIndexOutOfBoundsException)e;
         }
         
         throw e;
      }
       
   
   }//----------------------------------------------------
   
   @SuppressWarnings("rawtypes")
public static Object getMappedProperty(Object bean, String name, String key)
       throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,Exception
   {
       if(bean == null)
           throw new IllegalArgumentException("No bean specified");
       if(name == null)
           throw new IllegalArgumentException("No name specified");
       if(key == null)
           throw new IllegalArgumentException("No key specified");
       
       Object result = null;
       PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
       if(descriptor == null)
           throw new NoSuchMethodException("Unknown property '" + name + "'");
       if(descriptor instanceof JBPropertyDescriber)
       {
           Method readMethod = ((JBPropertyDescriber)descriptor).getMappedReadMethod();
           if(readMethod != null)
           {
               Object keyArray[] = new Object[1];
               keyArray[0] = key;
               result = readMethod.invoke(bean, keyArray);
           } else
           {
               throw new NoSuchMethodException("Property '" + name + "' has no mapped getter method");
           }
       } else
       {
           Method readMethod = descriptor.getReadMethod();
           if(readMethod != null)
           {
               Object invokeResult = readMethod.invoke(bean, new Object[0]);
               if(invokeResult instanceof Map)
                   result = ((Map)invokeResult).get(key);
           } else
           {
               throw new NoSuchMethodException("Property '" + name + "' has no mapped getter method");
           }
       }
       return result;
   }
   public static PropertyDescriptor getPropertyDescriptor(Object bean, String name)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception
    {
        if(bean == null)
            throw new IllegalArgumentException("No bean specified");
        if(name == null)
            throw new IllegalArgumentException("No name specified");
        do
        {
            int period = name.indexOf('.');
            if(period < 0)
                break;
            String next = name.substring(0, period);
            int indexOfINDEXED_DELIM = next.indexOf('[');
            int indexOfMAPPED_DELIM = next.indexOf('(');
            if(indexOfMAPPED_DELIM >= 0 && (indexOfINDEXED_DELIM < 0 || indexOfMAPPED_DELIM < indexOfINDEXED_DELIM))
                bean = getMappedProperty(bean, next);
            else
            if(indexOfINDEXED_DELIM >= 0)
                bean = getIndexedProperty(bean, next);
            else
                bean = getSimpleProperty(bean, next);
            if(bean == null)
                throw new IllegalArgumentException("Null property value for '" + name.substring(0, period) + "'");
            name = name.substring(period + 1);
        } while(true);
        int left = name.indexOf('[');
        if(left >= 0)
            name = name.substring(0, left);
        left = name.indexOf('(');
        if(left >= 0)
            name = name.substring(0, left);

        PropertyDescriptor descriptors[] = getPropertyDescriptors(bean);
        if(descriptors != null)
        {
            for(int i = 0; i < descriptors.length; i++)
             {
            	if(name.equals(descriptors[i].getName()))
            	    return descriptors[i];
              
            	
             }
        }
        PropertyDescriptor result = null;
        
        
            try
            {
                result = new JBPropertyDescriber(name, bean.getClass());
            }
            catch(IntrospectionException ie) { }
           
        
        return result;
    }
   private static Method getReadMethod(PropertyDescriptor descriptor)
   {
       return MethodAdapter.getAccessibleMethod(descriptor.getReadMethod());
   }
   //-----------------------------------------------------
   protected static class MethodAdapter
   {

       public MethodAdapter()
       {
       }

       public static Object invokeMethod(Object object, String methodName, Object arg)
           throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, Exception
       {
           Object args[] = {
               arg
           };
           return invokeMethod(object, methodName, args);
       }

       public static Object invokeMethod(Object object, String methodName, Object args[])
           throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,Exception
       {
           if(args == null)
               args = emptyObjectArray;
           int arguments = args.length;
           Class<?> parameterTypes[] = new Class[arguments];
           for(int i = 0; i < arguments; i++)
               parameterTypes[i] = args[i].getClass();

           return invokeMethod(object, methodName, args, parameterTypes);
       }

       public static Object invokeMethod(Object object, String methodName, Object args[], Class<?> parameterTypes[])
           throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, Exception
       {
           if(parameterTypes == null)
               parameterTypes = emptyClassArray;
           if(args == null)
               args = emptyObjectArray;
           Method method = getMatchingAccessibleMethod(object.getClass(), methodName, parameterTypes);
           return method.invoke(object, args);
       }

       public static Object invokeExactMethod(Object object, String methodName, Object arg)
           throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
       {
           Object args[] = {
               arg
           };
           return invokeExactMethod(object, methodName, args);
       }

       public static Object invokeExactMethod(Object object, String methodName, Object args[])
           throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
       {
           if(args == null)
               args = emptyObjectArray;
           int arguments = args.length;
           Class<?> parameterTypes[] = new Class[arguments];
           for(int i = 0; i < arguments; i++)
               parameterTypes[i] = args[i].getClass();

           return invokeExactMethod(object, methodName, args, parameterTypes);
       }

       public static Object invokeExactMethod(Object object, String methodName, Object args[], Class<?> parameterTypes[])
           throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
       {
           if(args == null)
               args = emptyObjectArray;
           if(parameterTypes == null)
               parameterTypes = emptyClassArray;
           Method method = getAccessibleMethod(object.getClass(), methodName, parameterTypes);
           if(method == null)
               throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
           else
               return method.invoke(object, args);
       }

       public static Method getAccessibleMethod(Class<?> clazz, String methodName, Class<?> parameterType)
       throws NoSuchMethodException
       {
           Class<?> parameterTypes[] = {
               parameterType
           };
           return getAccessibleMethod(clazz, methodName, parameterTypes);
       }

       public static Method getAccessibleMethod(Class<?> clazz, String methodName, Class<?> parameterTypes[])
       throws NoSuchMethodException
       {
           return getAccessibleMethod(clazz.getMethod(methodName, parameterTypes));
       
       }

       public static Method getAccessibleMethod(Method method)
       {
           if(method == null)
               return null;
           if(!Modifier.isPublic(method.getModifiers()))
               return null;
           Class<?> clazz = method.getDeclaringClass();
           if(Modifier.isPublic(clazz.getModifiers()))
           {
               return method;
           } else
           {
               method = getAccessibleMethodFromInterfaceNest(clazz, method.getName(), method.getParameterTypes());
               return method;
           }
       }

       private static Method getAccessibleMethodFromInterfaceNest(Class<?> clazz, String methodName, Class<?> parameterTypes[])
       {
           Method method = null;
           for(; clazz != null; clazz = clazz.getSuperclass())
           {
               Class<?> interfaces[] = clazz.getInterfaces();
               for(int i = 0; i < interfaces.length; i++)
               {
                   if(!Modifier.isPublic(interfaces[i].getModifiers()))
                       continue;
                   try
                   {
                       method = interfaces[i].getDeclaredMethod(methodName, parameterTypes);
                   }
                   catch(NoSuchMethodException e) {Debugger.println(e.getMessage()); }
                   if(method != null)
                       break;
                   method = getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
                   if(method != null)
                       break;
               }

           }

           if(method != null)
               return method;
           else
               return null;
       }

       public static Method getMatchingAccessibleMethod(Class<?> clazz, String methodName, Class<?> parameterTypes[])
       throws Exception
       {
 
           Method method;
           method = clazz.getMethod(methodName, parameterTypes);
   
           try
           {
               method.setAccessible(true);
           }
           catch(SecurityException se)
           {
              se.printStackTrace();
           }
           return method;
       
       }

       protected static final boolean isAssignmentCompatible(Class<?> parameterType, Class<?> parameterization)
       {
           if(parameterType.isAssignableFrom(parameterization))
               return true;
           if(parameterType.isPrimitive())
           {
               if(Boolean.TYPE.equals(parameterType))
                   return (java.lang.Boolean.class).equals(parameterization);
               if(Float.TYPE.equals(parameterType))
                   return (java.lang.Float.class).equals(parameterization);
               if(Long.TYPE.equals(parameterType))
                   return (java.lang.Long.class).equals(parameterization);
               if(Integer.TYPE.equals(parameterType))
                   return (java.lang.Integer.class).equals(parameterization);
               if(Double.TYPE.equals(parameterType))
                   return (java.lang.Double.class).equals(parameterization);
           }
           return false;
       }


       //private static boolean loggedAccessibleWarning = false;
       private static final Class<?> emptyClassArray[] = new Class[0];
       private static final Object emptyObjectArray[] = new Object[0];

    
   }
   //------------------------------------------------------------------
  protected static class JBPropertyDescriber extends PropertyDescriptor
  {

      public JBPropertyDescriber(String propertyName, Class<?> beanClass)
          throws IntrospectionException
      {
          super(propertyName, null, null);
          if(propertyName == null || propertyName.length() == 0)
              throw new IntrospectionException("bad property name: " + propertyName + " on class: " + beanClass.getClass().getName());
          setName(propertyName);
          String base = capitalizePropertyName(propertyName);
          try
          {
              mappedReadMethod = findMethod(beanClass, "get" + base, 1, stringClassArray);
              Class<?> params[] = {
                  java.lang.String.class, mappedReadMethod.getReturnType()
              };
              mappedWriteMethod = findMethod(beanClass, "set" + base, 2, params);
          }
          catch(IntrospectionException e) { Debugger.println(e.getMessage());}
          if(mappedReadMethod == null)
              mappedWriteMethod = findMethod(beanClass, "set" + base, 2);
          if(mappedReadMethod == null && mappedWriteMethod == null)
          {
              throw new IntrospectionException("Property '" + propertyName + "' not found on " + beanClass.getName());
          } else
          {
              findMappedPropertyType();
              return;
          }
      }

      public JBPropertyDescriber(String propertyName, Class<?> beanClass, String mappedGetterName, String mappedSetterName)
          throws IntrospectionException
      {
          super(propertyName, null, null);
          if(propertyName == null || propertyName.length() == 0)
              throw new IntrospectionException("bad property name: " + propertyName);
          setName(propertyName);
          mappedReadMethod = findMethod(beanClass, mappedGetterName, 1, stringClassArray);
          if(mappedReadMethod != null)
          {
              Class<?> params[] = {
                  java.lang.String.class, mappedReadMethod.getReturnType()
              };
              mappedWriteMethod = findMethod(beanClass, mappedSetterName, 2, params);
          } else
          {
              mappedWriteMethod = findMethod(beanClass, mappedSetterName, 2);
          }
          findMappedPropertyType();
      }

      public JBPropertyDescriber(String propertyName, Method mappedGetter, Method mappedSetter)
          throws IntrospectionException
      {
          super(propertyName, mappedGetter, mappedSetter);
          if(propertyName == null || propertyName.length() == 0)
          {
              throw new IntrospectionException("bad property name: " + propertyName);
          } else
          {
              setName(propertyName);
              mappedReadMethod = mappedGetter;
              mappedWriteMethod = mappedSetter;
              findMappedPropertyType();
              return;
          }
      }

      public Class<?> getMappedPropertyType()
      {
          return mappedPropertyType;
      }

      public Method getMappedReadMethod()
      {
          return mappedReadMethod;
      }

      public void setMappedReadMethod(Method mappedGetter)
          throws IntrospectionException
      {
          mappedReadMethod = mappedGetter;
          findMappedPropertyType();
      }

      public Method getMappedWriteMethod()
      {
          return mappedWriteMethod;
      }

      public void setMappedWriteMethod(Method mappedSetter)
          throws IntrospectionException
      {
          mappedWriteMethod = mappedSetter;
          findMappedPropertyType();
      }

      private void findMappedPropertyType()
          throws IntrospectionException
      {
          try
          {
              mappedPropertyType = null;
              if(mappedReadMethod != null)
              {
                  if(mappedReadMethod.getParameterTypes().length != 1)
                      throw new IntrospectionException("bad mapped read method arg count");
                  mappedPropertyType = mappedReadMethod.getReturnType();
                  if(mappedPropertyType == Void.TYPE)
                      throw new IntrospectionException("mapped read method " + mappedReadMethod.getName() + " returns void");
              }
              if(mappedWriteMethod != null)
              {
                  Class<?> params[] = mappedWriteMethod.getParameterTypes();
                  if(params.length != 2)
                      throw new IntrospectionException("bad mapped write method arg count");
                  if(mappedPropertyType != null && mappedPropertyType != params[1])
                      throw new IntrospectionException("type mismatch between mapped read and write methods");
                  mappedPropertyType = params[1];
              }
          }
          catch(IntrospectionException ex)
          {
              throw ex;
          }
      }

      private static String capitalizePropertyName(String s)
      {
          if(s.length() == 0)
          {
              return s;
          } else
          {
              char chars[] = s.toCharArray();
              chars[0] = Character.toUpperCase(chars[0]);
              return new String(chars);
          }
      }

      @SuppressWarnings({ "unchecked", "rawtypes" })
	private static synchronized Method[] getPublicDeclaredMethods(Class<?> clz)
      {
          final Class<?> fclz = clz;
          Method result[] = (Method[])declaredMethodCache.get(fclz);
          if(result != null)
              return result;
          result = (Method[])AccessController.doPrivileged(new PrivilegedAction() {

              public Object run()
              {
                  return fclz.getDeclaredMethods();
              }

          });
          for(int i = 0; i < result.length; i++)
          {
              Method method = result[i];
              int mods = method.getModifiers();
              if(!Modifier.isPublic(mods))
                  result[i] = null;
          }

          declaredMethodCache.put(clz, result);
          return result;
      }

      private static Method internalFindMethod(Class<?> start, String methodName, int argCount)
      {
          for(Class<?> cl = start; cl != null; cl = cl.getSuperclass())
          {
              Method methods[] = getPublicDeclaredMethods(cl);
              for(int i = 0; i < methods.length; i++)
              {
                  Method method = methods[i];
                  if(method != null)
                  {
                      int mods = method.getModifiers();
                      if(!Modifier.isStatic(mods) && method.getName().equals(methodName) && method.getParameterTypes().length == argCount)
                          return method;
                  }
              }

          }

          Class<?> ifcs[] = start.getInterfaces();
          for(int i = 0; i < ifcs.length; i++)
          {
              Method m = internalFindMethod(ifcs[i], methodName, argCount);
              if(m != null)
                  return m;
          }

          return null;
      }

      private static Method internalFindMethod(Class<?> start, String methodName, int argCount, Class<?> args[])
      {
          for(Class<?> cl = start; cl != null; cl = cl.getSuperclass())
          {
              Method methods[] = getPublicDeclaredMethods(cl);
              for(int i = 0; i < methods.length; i++)
              {
                  Method method = methods[i];
                  if(method == null)
                      continue;
                  int mods = method.getModifiers();
                  if(Modifier.isStatic(mods))
                      continue;
                  Class<?> params[] = method.getParameterTypes();
                  if(!method.getName().equals(methodName) || params.length != argCount)
                      continue;
                  boolean different = false;
                  if(argCount > 0)
                  {
                      for(int j = 0; j < argCount; j++)
                          if(params[j] != args[j])
                              different = true;

                      if(different)
                          continue;
                  }
                  return method;
              }

          }

          Class<?> ifcs[] = start.getInterfaces();
          for(int i = 0; i < ifcs.length; i++)
          {
              Method m = internalFindMethod(ifcs[i], methodName, argCount);
              if(m != null)
                  return m;
          }

          return null;
      }

      static Method findMethod(Class<?>cls, String methodName, int argCount)
          throws IntrospectionException
      {
          if(methodName == null)
              return null;
          Method m = internalFindMethod(cls, methodName, argCount);
          if(m != null)
              return m;
          else
              throw new IntrospectionException("No method \"" + methodName + "\" with " + argCount + " arg(s)");
      }

      static Method findMethod(Class<?> cls, String methodName, int argCount, Class<?> args[])
          throws IntrospectionException
      {
          if(methodName == null)
              return null;
          Method m = internalFindMethod(cls, methodName, argCount, args);
          if(m != null)
              return m;
          else
              throw new IntrospectionException("No method \"" + methodName + "\" with " + argCount + " arg(s) of matching types.");
      }

      static boolean isSubclass(Class<?> a, Class<?> b)
      {
          if(a == b)
              return true;
          if(a == null || b == null)
              return false;
          for(Class<?> x = a; x != null; x = x.getSuperclass())
          {
              if(x == b)
                  return true;
              if(b.isInterface())
              {
                  Class<?> interfaces[] = x.getInterfaces();
                  for(int i = 0; i < interfaces.length; i++)
                      if(isSubclass(interfaces[i], b))
                          return true;

              }
          }

          return false;
      }

/*      private boolean throwsException(Method method, Class exception)
      {
          Class exs[] = method.getExceptionTypes();
          for(int i = 0; i < exs.length; i++)
              if(exs[i] == exception)
                  return true;

          return false;
      }*/

      private Class<?> mappedPropertyType;
      private Method mappedReadMethod;
      private Method mappedWriteMethod;
      private static final Class<?> stringClassArray[];
      
    
      /**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((mappedPropertyType == null) ? 0 : mappedPropertyType
						.hashCode());
		result = prime
				* result
				+ ((mappedReadMethod == null) ? 0 : mappedReadMethod.hashCode());
		result = prime
				* result
				+ ((mappedWriteMethod == null) ? 0 : mappedWriteMethod
						.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JBPropertyDescriber other = (JBPropertyDescriber) obj;
		if (mappedPropertyType == null)
		{
			if (other.mappedPropertyType != null)
				return false;
		}
		else if (!mappedPropertyType.equals(other.mappedPropertyType))
			return false;
		if (mappedReadMethod == null)
		{
			if (other.mappedReadMethod != null)
				return false;
		}
		else if (!mappedReadMethod.equals(other.mappedReadMethod))
			return false;
		if (mappedWriteMethod == null)
		{
			if (other.mappedWriteMethod != null)
				return false;
		}
		else if (!mappedWriteMethod.equals(other.mappedWriteMethod))
			return false;
		return true;
	}

	private static Hashtable<Object,Object> declaredMethodCache = new Hashtable<Object,Object>();

      static 
      {
          stringClassArray = (new Class[] {
              java.lang.String.class
          });
      }
  }//------------------------------------------------
  /**
   * 
   * @param bean the bean or class
   * @return set of property names
   */
	public static Set<String> getPropertyNames(Object bean)
	{
		if(bean == null)
	          return null;
	     
		Class<?> clz = null;
		if(bean instanceof Class<?>)
			clz = (Class<?>)bean;
		else
			clz = bean.getClass();
		
		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clz);
		if(propertyDescriptors == null || propertyDescriptors.length ==0)
			return null;
		HashSet<String> names = new HashSet<>(propertyDescriptors.length);
		String name  = null;
		for (int i = 0; i < propertyDescriptors.length; i++)
		{
			name = propertyDescriptors[i].getName();
			
			if("class".equals(name))
				continue;
			
			names.add(name);
		}
		
		return names;
	}

   
}
