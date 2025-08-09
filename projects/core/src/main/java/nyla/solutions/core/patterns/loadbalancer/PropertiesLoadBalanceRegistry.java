package nyla.solutions.core.patterns.loadbalancer;

import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * Example Code usage
 * <pre>
 * {@code
 *
 *
 * }
 * </pre>
 *
 * @author Gregory Green
 */
public class PropertiesLoadBalanceRegistry implements LoadBalanceRegistry<String, String>
{
    private final RoundRobin<String> roundRobin = new RoundRobin<String>();
    private final Properties properties;
    private final String propertyFilePath;

    public PropertiesLoadBalanceRegistry(Properties properties,String propertyFilePath) {
        this.properties = properties;
        this.propertyFilePath = propertyFilePath;
    }


    /**
     * Lookup the location of a key
     *
     * @see nyla.solutions.core.patterns.loadbalancer.LoadBalanceRegistry#lookup(java.lang.Object)
     */
    @Override
    public synchronized String lookup(String id)
    {

        String associated = this.properties.getProperty(id);

        if (associated == null) {
            associated = this.next();

            if(associated == null)
                throw new SetupException("Cannot find the a next location for load balanced id:"+id+". You mus add values to the properties");

            //Est. new association
            register(id, associated);
        }

        return associated;

    }

    /**
     * Remove registration
     *
     * @see nyla.solutions.core.patterns.loadbalancer.LoadBalanceRegistry#unregister(java.lang.Object)
     */
    @Override
    public void unregister(String associated)
    {
        if (associated == null || associated.length() == 0)
            return; //nothing to unregister

        try {
            //remove all keys
            if (this.properties != null) {
                Set<Object> keySet = properties.keySet();
                String prop;
                String location;
                for (Iterator<Object> i = keySet.iterator(); i.hasNext(); ) {
                    prop = (String) i.next();
                    location = properties.getProperty(prop);

                    //remove key
                    if (associated.equals(location))
                        i.remove();
                }

                //save properties
                IO.writeProperties(this.propertyFilePath, properties);
            }

            //Return from round robin
            this.roundRobin.remove(associated);
        }
        catch (IOException e) {
            throw new SystemException("Unable to unregistry:" + associated + " ERROR:" + e.getMessage(), e);
        }

    }

    /**
     * @param id       the ID
     * @param location abstraction of the location to register
     */
    public synchronized void register(String id, String location)
    {
        try {
            properties.put(id, location);


            IO.writeProperties(propertyFilePath, properties);

            roundRobin.add(location);
        }
        catch (NullPointerException e) {
            throw new SetupException(e);
        }
        catch (IOException e) {
            throw new SetupException("Cannot write to property:\"" + propertyFilePath + "\" ERROR: " + e.getMessage()
					, e);
        }
    }

    /**
     * @see nyla.solutions.core.patterns.loadbalancer.LoadBalanceRegistry#next()
     */
    public synchronized String next()
    {
        return roundRobin.next();
    }

    /**
     * @see nyla.solutions.core.patterns.loadbalancer.LoadBalanceRegistry#listRegistered()
     */
    @Override
    public Collection<String> listRegistered()
    {
        return this.roundRobin.toCollection();
    }

    /**
     * Add new registered location
     *
     * @see nyla.solutions.core.patterns.loadbalancer.LoadBalanceRegistry#register(java.lang.Object)
     */
    public synchronized void register(String location)
    {
        register(location,location);
    }

    /**
     * @return the propertyFilePath
     */
    public String getPropertyFilePath()
    {
        return propertyFilePath;
    }

    protected Properties getProperties()
    {
        return properties;
    }
}
