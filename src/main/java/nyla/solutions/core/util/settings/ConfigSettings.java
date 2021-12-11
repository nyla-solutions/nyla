package nyla.solutions.core.util.settings;

import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.ConfigLockException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.FileMonitor;
import nyla.solutions.core.patterns.observer.SubjectObserver;
import nyla.solutions.core.patterns.observer.SubjectRegistry;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ConfigSettings extends AbstractSettings
{
    private final static ReentrantLock lock = new ReentrantLock();
    private static final long lockPeriodMs = 5000;
    private SubjectRegistry registry = new SubjectRegistry();
    private boolean mergeSystemProperties = true;
    private boolean mergeEnvProperties = true;
    private boolean setSystemProperties = false;

    private File file = null;
    private FileMonitor fileMonitor = null;
    private String configSourceLocation = null;
    private Properties properties = null; // configuration properties
    protected static final String registerObserver_POLLING_INTERVAL_MS_PROP = "CONFIG_FILE_WATCH_POLLING_INTERVAL_MS";
    protected static final String registerObserver_DELAY_MS_PROP = "CONFIG_FILE_WATCH_DELAY_MS";
    private long registerObserver_DELAY_MS_DEFAULT = 5000; //initial 5 seconds delay


    /**
     * Default construction
     */
    public ConfigSettings()
    {
    }

    public ConfigSettings(Map<Object, Object> properties)
    {
        this.setProperties(properties);
    }//------------------------------------------------
    /*
     * (non-Javadoc)
     *
     * @see nyla.solutions.core.util.Settings#getProperties()
     */
    @Override
    public synchronized Map<Object, Object> getProperties()
    {
        try
        {
            if (lock.tryLock(lockPeriodMs, TimeUnit.MILLISECONDS))
            {
                try
                {
                    if (properties == null || this.isAlwaysReload())
                    {
                        loadProperties();
                    }

                    // return copy

                    return new HashMap<Object, Object>(properties);
                }
                finally
                {
                    lock.unlock();
                }
            }
            else
            {
                throw new ConfigLockException("Setting settings");
            }
        }
        catch (InterruptedException e)
        {
            throw new ConfigException(e);
        }
    }// ------------------------------------------------------------

    /*
     * (non-Javadoc)
     *
     * @see
     * nyla.solutions.core.util.Settings#setProperties(java.util.Properties)
     */
    @Override
    public synchronized void setProperties(Map<Object, Object> properties)
    {
        if (this.properties == null)
            this.properties = new Properties();


        this.properties.putAll(properties);
    }// --------------------------------------------

    /*
     * (non-Javadoc)
     *
     * @see nyla.solutions.core.util.Settings#reLoad()
     */
    @Override
    public void reLoad()
    {
        loadProperties();
    }

    /**
     * Load the configuration properties from the properties file.
     * <p/>
     * <p/>
     * <p/>
     * Caller must test to ensure that properties is Non-null.
     *
     * @throws IllegalArgumentException Translates an IOException from reading
     *                                  <p/>
     *                                  the properties file into a run time exception.
     */

    private synchronized void loadProperties()
    {
        // If multiple threads are waiting to invoke this method only allow
        // the first one to do so. The rest should just return since the first
        // thread through took care of loading the properties.
        try
        {
            if (lock.tryLock(lockPeriodMs, TimeUnit.MILLISECONDS))
            {

                try
                {
                        loadWithoutLock();
                }
                finally
                {
                    lock.unlock();
                }
            }
            else
			{
				throw new ConfigLockException("Config settings loading");

			}


        }
        catch (ConfigException e)
        {
            throw e;
        }
        catch (FileNotFoundException e)
        {
            throw new ConfigException(e.getMessage(), e);
        }
        catch (Exception e)
        {
            throw new ConfigException(e.getMessage(), e);
        }


        try
        {
            this.registry.notify(this.getClass().getName(), this);
        }
        catch (Exception e)
        {
            Debugger.printWarn(e);
        }
    }// ------------------------------------------------------------

    private void loadWithoutLock()
    throws IOException
    {
        this.file = null;

        boolean alwaysReload = this.isAlwaysReload();

        boolean useFormatting = this.isUseFormatting();

        String filePath = getSystemPropertyFile();
        if (filePath != null && filePath.length() > 0)
        {
            // System.out.println("CONFIG: LOADING CONFIG properties from "+
            // file);
            this.file = Paths.get(filePath).toFile();
            FileInputStream fis = new FileInputStream(file);

            try
            {
                properties = new Properties();
                // Load the properties object from the properties file
                properties.load(fis);

                configSourceLocation = file.getAbsolutePath();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw new ConfigException(e.toString());
            }
            finally
            {
                if (fis != null)
                    fis.close(); // Always close the file, even on exception

            }

        }
        else
        {
            properties = new Properties();

            try
            {
                String bundleName = getBundleName();

                // try to get properties from resource bundle
                ResourceBundle rb = ResourceBundle.getBundle(bundleName);

                URL url = Config.class.getResource(bundleName + ".properties");

                if (url != null)
                    configSourceLocation = url.toString();
                else
                    configSourceLocation = bundleName + ".properties";

                Enumeration<?> keys = rb.getKeys();

                Object k = null;

                while (keys.hasMoreElements())
                {

                    k = keys.nextElement();

                    properties.put(k, rb.getString(k + ""));

                }
            } // end els load from resource bundle
            catch (MissingResourceException e)
            {
            }
        }//end else


        //Determine global settings
        String reloadBool = properties.getProperty(Config.class.getName() + ".alwaysReload");

        if (reloadBool == null || reloadBool.length() == 0)
        {

            alwaysReload = false;

        }
        else
        {
            alwaysReload = Boolean.valueOf(reloadBool).booleanValue();
        }

        this.setAlwaysReload(alwaysReload);

        // Merge SystemProperteis
        mergeSystemProperties = Boolean
                .valueOf(properties.getProperty(Config.class.getName() + ".mergeSystemProperties", "true"))
                .booleanValue();

        if (mergeSystemProperties)
        {
            // add system properties
            properties.putAll((Properties) System.getProperties().clone());
        }

        // Envinronment SystemProperteis
        mergeEnvProperties = Boolean
                .valueOf(properties.getProperty(Config.class.getName() + ".mergeEnvProperties", "true"))
                .booleanValue();

        if (mergeEnvProperties)
        {
            // add env properties
            properties.putAll(System.getenv());
        }

        // process formatting
        String propName = Config.class.getName() + ".useFormatting";
        String useFormattingText = properties.getProperty(propName);

        if (useFormattingText == null || useFormattingText.length() == 0)
        {
            useFormatting = false;
        }
        else
        {
            useFormatting = Boolean.valueOf(useFormattingText).booleanValue();
        }

        if (useFormatting)
        {
            Text.formatMap(properties);
        }

        propName = Config.class.getName() + ".setSystemProperties";
        setSystemProperties = Boolean.valueOf(properties.getProperty(propName, "false")).booleanValue();

        // Set system properties with values from configuration
        if (setSystemProperties)
        {
            Set<Object> keySet = properties.keySet();
            String key;
            String sysProp;
            for (Iterator<Object> i = keySet.iterator(); i.hasNext(); )
            {
                key = (String) i.next();
                sysProp = System.getProperty(key);

                if (sysProp != null && sysProp.length() > 0)
                    continue; // do not override values

                // set system property
                System.setProperty(key, properties.getProperty(key));
            }
        }
    }


    /**
     * @return the system property file
     */

    private String getSystemPropertyFile()
    {

        String file = System.getProperty(SYS_PROPERTY);
        if (file == null || file.length() == 0)
        {

            try
            {
                // file = (String) new InitialContext().lookup(SYS_PROPERTY);
            }
            catch (Throwable e)
            {
                throw new SystemException(e);
            }
        }

        return file;

    }// -----------------------------------------------------------

    /**
     * @return the configuration location
     */
    public String getLocation()
    {
        return configSourceLocation;
    }// --------------------------------------------------------

    @Override
    public synchronized void registerObserver(SubjectObserver<Settings> settingsObserver)
    {
        if (settingsObserver == null)
            throw new IllegalArgumentException("settingsObserver is required");

        if (file != null)
        {
            if (fileMonitor == null)
            {
                fileMonitor = new FileMonitor(
                        this.getPropertyLong(registerObserver_POLLING_INTERVAL_MS_PROP),
                        this.getPropertyLong(registerObserver_DELAY_MS_PROP, registerObserver_DELAY_MS_DEFAULT)
                );
            }

            fileMonitor.monitor(file.getParent(), file.getName(), false);
            //Observer fileEventBridge = (observable,o) -> {this.reLoad();};
            //fileMonitor.addObserver(fileEventBridge);
            fileMonitor.add((observable, o) -> this.reLoad()); //TODO: DEADLOCK bug
        }

        registry.register(getClass().getName(), settingsObserver);
    }


}
