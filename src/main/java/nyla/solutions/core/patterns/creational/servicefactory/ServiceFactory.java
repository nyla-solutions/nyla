package nyla.solutions.core.patterns.creational.servicefactory;

import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.util.Config;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;


/**
 * 
 * <pre>
 * ServiceFactory provides a factory method implement
 * to obtain system service.
 * 
 * 
 * Override the default service factory with the following config.properties entry
 * 
 * ServiceFactory.config=someOtherFactory
 * #example
 * 
 * ServiceFactory.config=nyla.solutions.core.patterns.creational.servicefactory.ConfigServiceFactory
 * 
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public abstract class ServiceFactory
{
   /**
    * SERVICE_FACTORY_PROP_NM = ServiceFactory.class.getName()
    */
   public final static String SERVICE_FACTORY_PROP_NM = ServiceFactory.class.getName();
   
   /**
    * DEFAULT_SERVICE_FACTORY = SpringFactory.class.getName()
    */
   public static final String DEFAULT_SERVICE_FACTORY = ConfigServiceFactory.class.getName();

   
   /**
    * SERVICE_FACTORY_CONFIG_PROP = "ServiceFactory.config"
    */
   public static final String SERVICE_FACTORY_CONFIG_PROP = "ServiceFactory.config";
   
   /**
    * DEFAULT_CONFIG_PROP_VALUE = "service.factory.xml"
    */
   public static final String DEFAULT_CONFIG_PROP_VALUE = "service.factory.xml";
   
   /**
    * 
    * @return Config.getProperty("ServiceFactory.config")
    */
   public static String getConfigProperty()
   {
	  String property = Config.getProperty(SERVICE_FACTORY_CONFIG_PROP,"");
	  
	  if(property.length() == 0)
	  {
		  //check if System property is set
		  property = System.getProperty(SERVICE_FACTORY_CONFIG_PROP,DEFAULT_CONFIG_PROP_VALUE);
	  }
	  
	  return property;
   }
   /**
    * Singleton factory method
    * @return a single instance of the ServiceFactory object 
    * for the JVM
    */
   public synchronized static ServiceFactory getInstance()
   {
      return getInstance(null,null);
      
   }// --------------------------------------------
   /**
    * Singleton factory method
    * for the JVM
    * @param configurationPath the configuration details
    * @return a single instance of the ServiceFactory object 
    */
   public synchronized static ServiceFactory getInstance(String configurationPath)
   {
	   return getInstance(null,configurationPath);
   }// --------------------------------------------------------
   /**
    * Singleton factory method
    * @return a single instance of the ServiceFactory object 
    * for the JVM
    * @param aClass the class implementation
    */
   public synchronized static ServiceFactory getInstance(Class<?> aClass)
   {
	   return getInstance(aClass,null);
   }// --------------------------------------------------------
      /**
       * Singleton factory method
       * @param aClass the class implements
       * @param configurationPath the path to the configuration details
       * @return a single instance of the ServiceFactory object 
       * for the JVM
       */
      public synchronized static ServiceFactory getInstance(Class<?> aClass, String configurationPath)
      {
         if(configurationPath == null)
         {
            configurationPath = "";   
         } 
         
         if (instances.get(configurationPath) == null)
         {
            instances.put(configurationPath, createServiceFactory(aClass,configurationPath));
         }         
         
         return (ServiceFactory)instances.get(configurationPath);
      }//--------------------------------------------
      private synchronized static ServiceFactory createServiceFactory(Class<?> aClass, String configurationFile)
      {
    	  String factoryClassName = null;
    	  if(aClass != null)
    	  {
    		  factoryClassName = Config.getProperty(new StringBuilder(ServiceFactory.class.getName()).append(".").append(aClass.getName()).toString());    
    	  }
    	  
    	  //check to use default
    	  if(factoryClassName == null || factoryClassName.length() == 0)
    		  factoryClassName = Config.getProperty(SERVICE_FACTORY_PROP_NM,DEFAULT_SERVICE_FACTORY);
        
         
         //Debugger.println(ServiceFactory.class,"factoryClassName="+factoryClassName);
         
         try
         {
                        
              Class<?> factoryClass =Class.forName(factoryClassName);
              
              if(configurationFile  == null || configurationFile.length() == 0)
              {
                 return (ServiceFactory)ClassPath.newInstance(factoryClassName);
              }
              //Called passing configuration file to constructor
              Object[] inputs = { configurationFile };
              Class<?> [] parameterTypes = { String.class};
               
              return (ServiceFactory)factoryClass.getConstructor(parameterTypes).newInstance(inputs);                
         }
         catch (SetupException e)
         {
        	 throw e;
         }
         catch (Exception e)
         {
        	 
            throw new SetupException(e.getMessage(),e);
         }
      }// --------------------------------------------

      /**
       * Create object based on the object's full type name
       * @param aClass the object/service name
       * @param <T> the create class type
       * @return an instance of the given class
       */
      public abstract <T> T create(Class<?> aClass);
      

      /**
       * Create object and check if object is a sub class of serviceClass
       * @param serviceClass the service interface
       * @param <T> the type class
       * @param name the object name
       * @return the create instance
       */
      public abstract <T> T create(Class<?> serviceClass, String name);
      
   /**
    * @param <T> the type class
    * @param aName the object/service name
    * @return an instance of the given class
    */
   public abstract <T> T create(String aName);
   
   
   /**
    * Example USage:
    * <code>
    *  StructureFinder[] finders  = new StructureFinder[sources.length];	
	   ServiceFactory.getInstance().createForNames(sources,finders);
	  </code>	
    * @param names the object/service names
    * @param objectOutput array to place results
    * @param <T> the type class
    */
   public <T> void createForNames(String[] names, T[] objectOutput)
   {
	   
	   if(objectOutput == null || objectOutput.length == 0)
				throw new IllegalArgumentException("Non empty objectOutput");
	   
	   if(names ==null || names.length == 0)
	   {
		   return;
	   }
	  
	   String name = null;
	   Object obj = null;
	   try
	   {
		   for(int i=0; i < names.length;i++)
		   {
			   name = names[i];
			   obj = this.create(name);
			   objectOutput[i] =  this.create(name);
		   }
	   }
	   catch (ArrayStoreException e)
	   {   
		   if(obj == null)
			   throw e;
		   
		   throw new SystemException("Cannot assign bean \""+name+"\" class:"+obj.getClass().getName()
				   +" to array of objectOutput arrray class:"+Arrays.asList(objectOutput),e);
	   }
   }// --------------------------------------------------------
   

   /**
    *  @param <T> the type class
    * @param name the object/service name
    * @param params the constructor parameters
    * @return an instance of the given class
    */
   public abstract <T> T create(String name,Object [] params );
   
   /**
    * @param <T> the type class
    * @param name the object/service name
    * @param param the constructor parameters 
    * @return an instance of the given class
    */
   public abstract <T> T create(String name,Object param );
   
   private static Map<String,ServiceFactory>  instances = new Hashtable<String,ServiceFactory>();

}
