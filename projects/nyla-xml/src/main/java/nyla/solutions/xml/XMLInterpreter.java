package nyla.solutions.xml;

import java.io.File;
import java.io.IOException;

/**
 * 
 * <b>XMLInterpreter</b> supports object to XML conversions
 * @author Gregory Green
 *
 */
public interface XMLInterpreter
{
   /**
    * Configuration settings for the interpreter
    * @param key the configuration key
    * @param value the configuration 
    */
   void configure(String key, Object value);
   
   /**
    * Convert the XML data to an object
    * @param xml the XML object data
    * @return the object
    */
   Object toObject(String xml);
   
   /**
    * Convert the XML file data to an object
    * @param file the file
    * @return the object
    */
   Object toObject(File file)
   throws IOException;
   
   /**
    * Convert the object to an XML value
    * @param object the object information
    * @return in memory XML data
    */
   String toXML(Object object);
   
   /**
    * Write XML data to file
    * @param object the object
    * @param file the file to store the information
    * @throws IOException
    */
   void toXMLFile(Object object, File file)
   throws IOException;
}
