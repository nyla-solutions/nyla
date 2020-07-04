package nyla.solutions.core.patterns.loadbalancer;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.SetUpable;
import nyla.solutions.core.util.Config;

/**
 * Example Code usage
 * <pre>
 * {@code
 * ServiceFactory factory = ConfigServiceFactory.getConfigServiceFactoryInstance();
		
		PropertiesLoadBalanceRegistry registry = factory.create(LoadBalanceRegistry.class);
		
		String filePath = registry.getPropertyFilePath();
		
		Assert.assertTrue(filePath != null &amp;&amp; filePath.length() &gt; 0);
		
		Assert.assertEquals("./runtime/tmp/loadbalance.properties", filePath);
		
		String name = RealSingleRouteCommand.class.getName()+".findUsers";
		Command<Collection<User>,Criteria> cmd = CommasServiceFactory.getCommasServiceFactory().createCommand(name);
		
		Criteria input = new Criteria("123");
		Collection<User> users = cmd.execute(input);
		
		Assert.assertNotNull(users);
	}
 </pre>
 * @author Gregory Green
 *
 */
public class PropertiesLoadBalanceRegistry implements LoadBalanceRegistry<String, String>, SetUpable
{
	/**
	 * 
	 */
	public PropertiesLoadBalanceRegistry()
	{
		setUp();		
	}// --------------------------------------------------------
	/**
	 * 
	 * @see nyla.solutions.core.patterns.SetUpable#setUp()
	 */
	public void setUp()
	{
		if(this.properties != null)
			return;
		
		try
		{
			if(this.propertyFilePath != null && this.propertyFilePath.length() > 0)
				this.properties = IO.readProperties(this.propertyFilePath);
		}
		catch (IOException e)
		{
			throw new SetupException("Cannot load properties :"+propertyFilePath,e);
		}
			
	}// --------------------------------------------------------
	/**
	 * Lookup the location of a key
	 * @see nyla.solutions.core.patterns.loadbalancer.LoadBalanceRegistry#lookup(java.lang.Object)
	 */
	@Override
	public synchronized String lookup(String id)
	{
		if(this.properties == null)
			this.setUp();
		
		String associated = this.properties.getProperty(id);
		
		if(associated == null)
		{
			associated = this.next();
			
			//Est. new association
			register(id,associated);
		}
		
		return associated;
			
	}// --------------------------------------------------------
	/**
	 * Remove registration
	 * @see nyla.solutions.core.patterns.loadbalancer.LoadBalanceRegistry#unregister(java.lang.Object)
	 */
	@Override
	public void unregister(String associated)
	{
		if(associated == null || associated.length() == 0)
			return; //nothing to unregister
		
		try
		{
			//remove all keys
			if(this.properties != null)
			{
				Set<Object> keySet = properties.keySet();
				String prop;
				String location;
				for (Iterator<Object> i = keySet.iterator();i.hasNext();)
				{
					prop = (String)i.next();
					location = properties.getProperty(prop);
					
					//remove key
					if(associated.equals(location))
						i.remove();
				}
				
				//save properties
				IO.writeProperties(this.propertyFilePath, properties);
			}
			
			//Return from round robin
			this.roundRobin.remove(associated);
		}
		catch (IOException e)
		{
			throw new SystemException("Unable to unregistry:"+associated+" ERROR:"+e.getMessage(),e);
		}
		
	}// --------------------------------------------------------
	/**
	 * 
	 * @param id the ID 
	 *  @param location abstraction of the location to register
	 */
	public synchronized void register(String id, String location )
	{
		if(properties == null)
			setUp();
		try
		{
			properties.put(id, location);
			

			IO.writeProperties(propertyFilePath, properties);
		}
		catch(NullPointerException e)
		{
			throw new SetupException(e);
		}
		catch (IOException e)
		{
			throw new SetupException("Cannot write to property:\""+propertyFilePath+"\" ERROR: "+e.getMessage(),e);
		}
			
				
	}// --------------------------------------------------------
	/**
	 * 
	 * @see nyla.solutions.core.patterns.loadbalancer.LoadBalanceRegistry#next()
	 */
	public synchronized String next()
	{
		if(roundRobin == null)
			return null;
		
		return roundRobin.next();
	}// --------------------------------------------------------
	/**
	 * 
	 * @see nyla.solutions.core.patterns.loadbalancer.LoadBalanceRegistry#listRegistered()
	 */
	@Override
	public Collection<String> listRegistered()
	{
		return this.roundRobin.toCollection();
	}// --------------------------------------------------------
	/**
	 * Add new registered location
	 * @see nyla.solutions.core.patterns.loadbalancer.LoadBalanceRegistry#register(java.lang.Object)
	 */
	public synchronized void register(String location)
	{
       roundRobin.add(location);
	}
	
	/**
	 * @return the propertyFilePath
	 */
	public String getPropertyFilePath()
	{
		return propertyFilePath;
	}
	/**
	 * @param propertyFilePath the propertyFilePath to set
	 */
	public void setPropertyFilePath(String propertyFilePath)
	{
		this.propertyFilePath = propertyFilePath;
	}
	



	private RoundRobin<String> roundRobin = new RoundRobin<String>();
	private Properties properties;
	private String propertyFilePath = Config.getProperty(PropertiesLoadBalanceRegistry.class,"propertyFilePath");
	
}
