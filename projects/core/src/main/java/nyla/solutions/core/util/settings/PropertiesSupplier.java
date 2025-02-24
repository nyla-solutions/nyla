package nyla.solutions.core.util.settings;

import nyla.solutions.core.io.IoSupplier;
import nyla.solutions.core.util.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import static nyla.solutions.core.util.settings.AbstractSettings.RESOURCE_BUNDLE_NAME;

/**
 * Strategy object to load properties
 * @author gregory green
 */
public class PropertiesSupplier implements IoSupplier<Properties> {

    private final String path;
    private final PathType pathType;
    private String configSourceLocation;

    public PropertiesSupplier() {
        this(null);
    }

    enum PathType{
        URL,
        FILE
        , CLASSPATH
    }

    public PropertiesSupplier(String path) {
        this.path = path;

        if(path ==  null || path.length() ==0)
            pathType = PathType.CLASSPATH;
        else
        {
            path = path.toLowerCase();
            if(path.startsWith("http:") || path.startsWith("https:") || path.startsWith("file:"))
                pathType = PathType.URL;
            else
                pathType = PathType.FILE;
        }
    }

    public Properties get() throws IOException {

        Properties properties = switch(this.pathType)
        {
            case URL -> {
                yield loadFromUrl();
            }
            case FILE -> {
                yield loadFromFile();
            }
            case CLASSPATH -> {
                yield loadFromClassPath();
            }
        };

        return properties;
    }

    private Properties loadFromClassPath() {

        var properties = new Properties();

        try
        {
            // try to get properties from resource bundle
            var rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
            var url = Config.class.getResource(RESOURCE_BUNDLE_NAME + ".properties");

            if (url != null)
                configSourceLocation = url.toString();
            else
                configSourceLocation = RESOURCE_BUNDLE_NAME + ".properties";

            Enumeration<?> keys = rb.getKeys();

            Object key = null;

            while (keys.hasMoreElements()) {
                key = keys.nextElement();
                properties.put(key, rb.getString(key + ""));
            }
        }
        catch(MissingResourceException e)
        {
            //do nothing if resource bundle is missing
        }

        return properties;
    }

    private Properties loadFromFile() throws IOException {
        var file = Paths.get(path).toFile();
        try(var propertiesInputStream = new FileInputStream(file))
        {
            var properties = new Properties();
            // Load the properties object from the properties file
            properties.load(propertiesInputStream);
            configSourceLocation = file.getAbsolutePath();

            return properties;
        }
    }

    private Properties loadFromUrl() throws IOException {
        URL configUrl = null; // this would check for the protocol
        try {
            configUrl = new URL(path);

            configUrl.toURI(); // does the extra checking required for validation of URI

            try(var propertiesInputStream = configUrl.openStream())
            {
                var properties = new Properties();
                // Load the properties object from the properties file
                properties.load(propertiesInputStream);

                configSourceLocation = configUrl.toString();
                return properties;
            }
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
