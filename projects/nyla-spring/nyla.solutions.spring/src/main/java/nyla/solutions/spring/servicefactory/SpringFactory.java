package nyla.solutions.spring.servicefactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.patterns.servicefactory.ServiceFactory;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.spring.servicefactory.exceptions.SpringXmlFileNotFoundException;


/**
 * 
 * <pre>
 * SpringFactory is a service factory based on Spring.
 * By default the spring factory looks for a file named service.factory.xml in the default package of the 
 * CLASSPATH. A configuration property or system property can be set to indicate a different location for 
 * spring XML configuration.
 * 
 * Sample system properties;
 * -Dspring.xml.url=file:///projects/solutions/config/servicefactory.xml
 * -Dspring.xml.url=classpath:META-INF/spring-int-context.xml
 * 
 * Sample Configuration Property
 * spring.xml.url=file:///C:/Projects/Medco/EDF/dev/Bridge/runtime/config/service.factory.xml
 * 
 * 
 * Config Property
 * solutions.global.patterns.servicefactory.ServiceFactory.SERVICE_FACTORY_CONFIG=myapp.xml
 * 
 * Example Wiring
 * <import resource="other_services.xml"/>
 * <bean id="exampleBean" class="examples.ExampleBean">
 * <property name="beanOne"  singleton="false"><ref bean="anotherExampleBean"/></property>
 * <property name="beanTwo"><ref bean="yetAnotherBean"/></property>
 * <property name="integerProperty"><value>1</value></property>
 * </bean>
 * 
 * <bean id="anotherExampleBean" class="examples.AnotherBean"/>
 * <bean id="yetAnotherBean" class="examples.YetAnotherBean"/>
 * 
 * Sample
 * 
  <?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN" "http://www.springframework.org/dtd/spring-beans.dtd">
<beans>
   <bean id="org.solutions.postit.PostItService" class="org.solutions.postit.mail.MailPostItService"/>
   <bean id="org.solutions.postit.PostItDAO" class="org.solutions.postit.mail.JDBCMailPostItDAO"/>
   	<bean id="lightGray" class="java.awt.Color" singleton="false">
		<constructor-arg type="int" value="192"/>
		<constructor-arg type="int" value="192"/>
		<constructor-arg type="int" value="192"/>
	</bean>
 </beans>
 
 
<bean id="moreComplexObject" class="example.ComplexObject">
 <constructor-arg value="10"/>
<!-- results in a setAdminEmails(java.util.Properties) call -->
<property name="adminEmails">
<props>
<prop key="administrator">	
administrator@somecompany.org</prop>
<prop key="support">support@somecompany.org</prop>
<prop key="development">development@somecompany.org</prop>
</props>
</property>
<!-- results in a setSomeList(java.util.List) call -->
   <property name="someList">
      <list>
         <value>a list element followed by a reference</value>
         <ref bean="myDataSource" />
      </list>
   </property>
<!-- results in a setSomeMap(java.util.Map) call -->
<property name="someMap">
   <map>
   <entry>
      <key><value>yup an entry</value></key>
      <value>just some string</value>
   </entry>
   <entry>
<key><value>yup a ref</value></key>
<ref bean="myDataSource"/>
</entry>
</map>
</property>
<!-- results in a setSomeSet(java.util.Set) call -->
<property name="someSet">
<set>
<value>just some string</value>
<ref bean="myDataSource" />
</set>
The IoC container

Spring 

SPEL

<bean id="numberGuess" class="org.spring.samples.NumberGuess">
<property name="randomNumber" value="#{ T(java.lang.Math).random() * 100.0 }"/>
<!-- other properties -->
</bean>


singleton

Scopes a single bean definition to a single object instance per Spring IoC container.

prototype

Scopes a single bean definition to any number of object instances.

request

Scopes a single bean definition to the lifecycle of a single HTTP request; that is each and every HTTP request will have its own instance of a bean created off the back of a single bean definition. Only valid in the context of a web-aware Spring ApplicationContext.

session

Scopes a single bean definition to the lifecycle of a HTTP Session. Only valid in the context of a web-aware Spring ApplicationContext.

global session

Scopes a single bean definition to the lifecycle of a global HTTP Session. Typically only valid when used in a portlet context. Only valid in the context of a web-aware Spring ApplicationContext.
 *
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class SpringFactory extends ServiceFactory
{
	
	/**
	 * SPRING_XML_URL_PROP = "spring.xml.url"
	 */
	public static final String SPRING_XML_URL_PROP = "spring.xml.url";
   /**
    * 
    * Constructor for ServiceFactory initializes internal 
    * data settings.
    */
   public SpringFactory()
   {
	   //check system property default
	   String url= System.getProperty(SPRING_XML_URL_PROP);
	   
	   if(url == null || url.length() == 0)
	   {
		   url = Config.getProperty(SPRING_XML_URL_PROP,"");   
	   }
	   
	   if(url == null || url.length() == 0 )
	   {
			  
		   try
		   {
		   		factory = new ClassPathXmlApplicationContext(ServiceFactory.getConfigProperty());
		   }
		   catch(RuntimeException e)
		   {
			   if(e.getMessage() != null && e.getMessage().contains("because it does not exist"))
			   {
				   throw new SpringXmlFileNotFoundException();
			   }
			   throw e;
		   }

		   
	   }
	   else
	   {
		   //get by resource
		   factory = new FileSystemXmlApplicationContext(url);
		   
		   Debugger.println(this,"LOADED url="+url);
	   }
   }// --------------------------------------------
   /**
    * 
    * @param serviceFactoryXMLPath the location of the XML configuration file
    */
   public SpringFactory(String serviceFactoryXMLPath)
   {
      if(serviceFactoryXMLPath == null || serviceFactoryXMLPath.length() == 0)
      {
         serviceFactoryXMLPath = Config.getProperty("spring.xml.path", ServiceFactory.getConfigProperty());
      }
      
      //Resource resource = new ClassPathResource(serviceFactoryXMLPath);
      factory = new ClassPathXmlApplicationContext(serviceFactoryXMLPath);
      
      /*
      try
      {
         Debugger.printInfo(this,"Read service factory xml="+resource.getFile().getAbsolutePath());   
      }
      catch(Exception e)
      {
         Debugger.printWarn(e);
      }
      
      
      factory =  new XmlBeanFactory(resource);
      */
   }// --------------------------------------------

      /**
       * Create object based on the object's full type name
     * @param <T> the type to create
       * @param aClass the object/service name
       * @return an instance of the given class
       */
      public <T> T create(Class<?> aClass)
      {
         return create(aClass.getName());
      }// --------------------------------------------

   /**
    * Create the service type 
	 * @param aName the name of the name
	 * @param <T>  the service type
    * @return an instance of the given class
    */
   @SuppressWarnings("unchecked")
public <T> T create(String aName)
   {
      return (T)factory.getBean(aName);
      
   }// --------------------------------------------
   /**
    * 
    * @param <T> the type
    * @param aName the object/service name
    * @param aParams the parameters to passed to the constructor
    * @return an instance of the given class
    */
   @SuppressWarnings("unchecked")
   public <T> T create(final String aName,Object [] aParams )
   {
      return (T)factory.getBean(aName, aParams);
      
   }// --------------------------------------------
   /**
    * 
    * @param <T> teh type
    * @param aName the object/service name
    * @param aParam the constructor parameters to pass 
    * @return an instance of the given class
    */
   @SuppressWarnings("unchecked")
   public <T> T create(String aName,Object aParam )
   {
      Object []  params = { aParam };
      
      return (T)factory.getBean(aName, params);
      
   }// --------------------------------------------
   /**
    * 
    *
    * @see nyla.solutions.core.patterns.servicefactory.ServiceFactory#create(java.lang.Class, java.lang.String)
    */
   @SuppressWarnings("unchecked")
  public <T> T create(Class<?> serviceClass, String name)
   {
      if(serviceClass == null)
         throw new RequiredException("serviceClass in SpringFactory.create");
      if (name == null || name.length() == 0)
         throw new RequiredException("name required in create");
      
      Object service = create(name);
      
      //try
      //{
         //check if object is a sub class of serviceClass
         //throw ClassCastException if not valid
         //TODO: service.getClass().asSubclass(serviceClass);
         
      //}
      //catch(ClassCastException e)
      //{
      //   throw new SetupException("Service object \""+name+" is not an instance of "+serviceClass.getName());
      //}
      
      return (T)service;
   }// --------------------------------------------

   private ApplicationContext factory = null;
   //private XmlBeanFactory factory = null;


}
