package nyla.solutions.xml.xstream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.xml.XML;
import nyla.solutions.xml.XMLInterpreter;

/**
 * 
 * <b>XStreamInterpreter</b> is a wrapper for the xtream XML API(s) 
 * @author Gregory Green
 *
 */
public class XStreamInterpreter implements XMLInterpreter
{

   public XStreamInterpreter()
   {      
      xstream = new XStream();
   }// --------------------------------------------

   public void configure(String key, Object value)
   {
      if (!(value instanceof Class))
      {
         throw new IllegalArgumentException("Expected value to be of type java.lang.Class");
      }
      
      xstream.alias(key, (Class<?>)value);
   }// --------------------------------------------

   public Object toObject(String xml)
   {
      return xstream.fromXML(xml);
   }// --------------------------------------------
   
   /**
    * 
    *
    * @see nyla.solutions.xml.XMLInterpreter#toXML(java.lang.Object)
    */
   public String toXML(Object object)
   {

      String xml = xstream.toXML(object);
      
      if(!this.excludesHeader || xml == null || xml.length() == 0)
         return xml;
      
      return XML.stripHeader(xml);
   }//--------------------------------------------


   /**
    * 
    *
    * @see nyla.solutions.xml.XMLInterpreter#toXMLFile(java.lang.Object, java.io.File)
    */
   public void toXMLFile(Object object, File file)
   throws IOException
   {
      Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),IO.CHARSET));
      xstream.toXML(object, writer);
      
   }// --------------------------------------------
   /**
    * 
    *
    * @see nyla.solutions.xml.XMLInterpreter#toObject(java.io.File)
    */
   public Object toObject(File file)
   throws IOException
   {
	   Reader reader  = null;
      try
		{
			 reader = new InputStreamReader(new FileInputStream(file),IO.CHARSET);
			  return xstream.fromXML(reader);
		}
		finally
		{
			if(reader != null)
				reader.close();
		}
   }// --------------------------------------------

   /**
    * @return the excludesHeader
    */
   public boolean isExcludesHeader()
   {
      return excludesHeader;
   }//--------------------------------------------

   /**
    * @param excludesHeader the excludesHeader to set
    */
   public void setExcludesHeader(boolean excludesHeader)
   {
      this.excludesHeader = excludesHeader;
   }//--------------------------------------------
   private boolean excludesHeader = Config.getPropertyBoolean(XStreamInterpreter.class.getName()+".excludeHeader",true).booleanValue(); 
   private XStream xstream = null;
}
