package nyla.solutions.core.patterns.memento;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.cache.CacheFarm;
import nyla.solutions.core.util.Debugger;

import java.io.File;

import static nyla.solutions.core.util.Config.settings;


/**
 * <pre>
 * File IO based Memento
 *
 *
 * Alert: Add the following to your configuration properties
 *
 * solutions.global.patterns.memento.FileMemento.rootPath=&lt;file-location&gt;
 *
 * </pre>
 *
 * @author Gregory Green
 */
public class FileMemento implements Memento
{
    // = Config.getProperty(this.getClass(),"rootPath")
    private final String rootPath;
    // = ")
    private final String fileExtension;

    public FileMemento(String rootPath, String fileExtension)
    {
        this.rootPath = rootPath;
        this.fileExtension = fileExtension;
    }

    public FileMemento(String rootPath)
    {
        this(rootPath,
                settings().getProperty(FileMemento.class, "fileExtension", ".memento"));
    }

    /**
     * Retrieve the de-serialized object
     *
     * @param savePoint the save point
     * @param objClass  the object class
     * @return the object the object
     */
    public <T> T restore(String savePoint, Class<?> objClass)
    {
        String location = whereIs(savePoint, objClass);

        Object cacheObject = CacheFarm.getCache().get(location);
        if (cacheObject != null)
            return (T) cacheObject;

        cacheObject = IO.deserialize(new File(location));
        CacheFarm.getCache().put(location, cacheObject);

        return (T) cacheObject;
    }

    /**
     * Store the serialized object
     *
     * @param savePoint the save point
     * @param obj       the object to save
     */
    public void store(String savePoint, Object obj)
    {
        if (savePoint == null)
            throw new RequiredException("savePoint");

        if (obj == null)
            throw new RequiredException("obj");

        String location = whereIs(savePoint, obj.getClass());

        Debugger.println(this, "Storing in " + location);

        IO.serializeToFile(obj, new File(location));
    }

    /**
     * @return the rootPath
     */
    public String getRootPath()
    {
        return rootPath;
    }

    private String whereIs(String savePoint, Class<?> objClass)
    {
        if (savePoint == null || savePoint.length() == 0)
            throw new RequiredException("savePoint");

        if(objClass == null)
            throw new RequiredException("Object's Class");

        StringBuilder text = new StringBuilder();

        text.append(this.rootPath).append("/")
            .append(objClass.getName()).append(".")
            .append(savePoint).append(fileExtension);

        return text.toString();
    }


    /**
     * @return the fileExtension
     */
    public String getFileExtension()
    {
        return fileExtension;
    }

}
