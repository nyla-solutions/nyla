package nyla.solutions.core.patterns.memento;

import java.io.File;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.cache.CacheFarm;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;



/**
 * <pre>
 * File IO based Memento
 * 
 * 
 * Alert: Add the following to your configuration properties
 * 
 * solutions.global.patterns.memento.FileMemento.rootPath=<file-location>
 * 
 * </pre>
 * @author Gregory Green
 *
 */
public class FileMemento implements Memento
{
   /**
    * Retrieve the de-serialized object
    * @param savePoint the save point
    * @param objClass the object class
    * @return the object the object
    */
   public Object restore(String savePoint, Class<?> objClass)
   {
	String location = whereIs(savePoint, objClass);
	
	Object cacheObject = CacheFarm.getCache().get(location);
	if(cacheObject  != null)
	   return cacheObject;
	
	cacheObject = IO.deserialize(new File(location));
	CacheFarm.getCache().put(location, cacheObject);
	
	return cacheObject;
   }// ----------------------------------------------

   /**
    * Store the serialized object
    * @param savePoint the save point
    * @param obj the object to save
    */
   public void store(String savePoint, Object obj)
   {
	if (savePoint == null)
	   throw new RequiredException("savePoint");
	
	if (obj == null)
	   throw new RequiredException("obj");
	
	String location = whereIs(savePoint, obj.getClass());

	Debugger.println(this,"Storing in "+location);
	
	IO.serializeToFile(obj, new File(location));
   }// ----------------------------------------------
   
   
   /**
    * @return the rootPath
    */
   public String getRootPath()
   {
      return rootPath;
   }

   /**
    * @param rootPath the rootPath to set
    */
   public void setRootPath(String rootPath)
   {
      this.rootPath = rootPath;
   }// ----------------------------------------------
   private String whereIs(String savePoint, Class<?> objClass)
   {
	if (savePoint == null || savePoint.length() == 0)
	   throw new RequiredException("savePoint");
	
	StringBuffer text = new StringBuffer();
	
	text.append(this.rootPath).append("/")
	.append(objClass.getName()).append(".")
	.append(savePoint).append(fileExtension);
	
	return text.toString();
   }// ----------------------------------------------

   
   /**
    * @return the fileExtension
    */
   public String getFileExtension()
   {
      return fileExtension;
   }

   /**
    * @param fileExtension the fileExtension to set
    */
   public void setFileExtension(String fileExtension)
   {
      this.fileExtension = fileExtension;
   }


   private String fileExtension = Config.getProperty(this.getClass(),"fileExtension",".memento");
   private String rootPath = Config.getProperty(this.getClass(),"rootPath");
}
