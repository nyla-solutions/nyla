package nyla.solutions.core.io;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;


/**
 * 
 * <b>SynchronizedIO</b> provides a thread safe way to write files. All writes are synchronized
 * by the location of the file. 
 * This object is a vast improvement over blindly synchronizing entire objects
 * that may write to totally different file locations.
 * 
 * This object can be memory intensive. It is a singleton. 
 * It stores all file path locations in a hash table.
 * This hash map contains one lock for each saved file location. 
 * 
 * This class uses the IO object for all core writes.
 * 
 * @author Gregory Green
 * @version 1.0
 *
 */
public final class SynchronizedIO
{

   private Map<String,Object> lockMap = new Hashtable<String,Object> ();
   private static SynchronizedIO instance = null;
   /**
    * 
    * Constructor for SynchronizedIO initializes internal
    */
   private SynchronizedIO(){}
   
   /**
    * Singleton factory method
    * @return a single instance of the SynchronizedIO object 
    * for the JVM
    */
   public synchronized static SynchronizedIO getInstance()
   {
      if(instance == null)
         instance = new SynchronizedIO();
      
      return instance;
   }
   /**
    * 
    * @param aFilePath the file path the write
    * @param aInputStream the input stream data to write
    * @throws IOException
    */
   public void write(String aFilePath, InputStream aInputStream) throws IOException
   {
      Object lock = retrieveLock(aFilePath);      
      synchronized (lock) 
      {
         IO.write(aFilePath, aInputStream);
      }
   }
   /**
    * 
    * @param fileName the file to write
    * @param data the data
    * @throws IOException
    */
   public void writeAppend(String fileName, String data)throws IOException
   {   
      Object lock = retrieveLock(fileName);      
      synchronized (lock) 
      {
         IO.writeAppend(fileName, data,IO.CHARSET);
      }
   }
   /**
    * Write binary file data
    * 
    * @param filePath the file path to write
    * @param data the write data
    * @throws IOException when IO error occurs
    */
   public void writeFile(String filePath, byte[] data) throws IOException
   {
      Object lock = retrieveLock(filePath);      
      synchronized (lock) 
      {
         IO.writeFile(filePath, data);
      }
   }
   /**
    * Write binary file data
    * 
    * @param aFilePath the file path to write
    * @param aData the data to write
    * @param append to append or not
    * @throws IOException when an IO error occurs
    */
   public void writeFile(String aFilePath, byte[] aData, boolean append) throws IOException
   {
      Object lock = retrieveLock(aFilePath);      
      synchronized (lock) 
      {
         IO.writeFile(aFilePath, aData,append);
      }
   
   }
   /**
    * Write string file data
    * 
    * @param aFileName the file name
    * @param aData the data to write
    * @throws IOException when an IO error occurs
    */
   public void writeFile(String aFileName, String aData) throws IOException
   {
      Object lock = retrieveLock(aFileName);      
      synchronized (lock) 
      {
         IO.writeFile(aFileName, aData,IO.CHARSET);
      }

   }
   /**
    * Write binary file data
    * 
    * @param aFileName
    * @param aData
    * @throws Exception
    */
   public void writeFile(String aFileName, URL aData) throws Exception
   {
      
      Object lock = retrieveLock(aFileName);      
      synchronized (lock) 
      {
         IO.writeFile(aFileName, aData);
      }
   }
   /**
    * 
    * @param filePath the file to read
    * @return read a given file
    * @throws Exception
    */
   public String readFile(String filePath) throws IOException
   {
      
      Object lock = retrieveLock(filePath);      
      synchronized (lock) 
      {
         return IO.reader().readTextFile(filePath);
      }
   }
   /**
    * this.lockMap.clear()
    */
   public synchronized void clearLocks()
   {
      this.lockMap.clear();
   }
   /**
    * Retrieve the lock object for a given key
    * @param key the key for the lock
    * @return the object that will be used as a lock
    */
   private Object retrieveLock(String key)
   {
      Object lock = this.lockMap.get(key);
      if(lock == null)
      {
         lock = key;
         this.lockMap.put(key, lock);
      }
      return lock;
   }
   

}
