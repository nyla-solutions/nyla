package nyla.solutions.xml;
// Imported java classes
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Debugger;

/**
 *  Use the TraX interface to perform a transformation in the simplest manner possible
 *  (3 statements).
 */
public class XSL
{
   /**
    * Main utility
    * @param args
    * @throws TransformerException
    * @throws TransformerConfigurationException
    * @throws FileNotFoundException
    * @throws IOException
    */
	public static void main(String[] args)
    throws TransformerException, TransformerConfigurationException, 
           FileNotFoundException, IOException
  { 

   try
   {
     if(args.length != 2 )
     {
         throw new Exception("args file.xml file.xsl");
     } 
  // Use the static TransformerFactory.newInstance() method to instantiate 
  // a TransformerFactory. The javax.xml.transform.TransformerFactory 
  // system property setting determines the actual class to instantiate --
  // org.apache.xalan.transformer.TransformerImpl.
	TransformerFactory tFactory = TransformerFactory.newInstance();
	
	// Use the TransformerFactory to instantiate a Transformer that will work with  
	// the stylesheet you specify. This method call also processes the stylesheet
  // into a compiled Templates object.
	Transformer transformer = tFactory.newTransformer(new StreamSource(args[1]));

	// Use the Transformer to apply the associated Templates object to an XML document
	// (foo.xml) and write the output to a file (foo.out).
	transformer.transform(new StreamSource(args[0]), new StreamResult(new FileOutputStream("xsl.out")));
	
	System.out.println("************* The result is in xsl.out *************");

   }
   catch(Exception e)
   {
      e.printStackTrace();
   }
  }//--------------------------------------------

  /**
   * Convert Text into an in memory stream source
   * @param text the text XSL
   * @return the stream source
   */
  public static StreamSource toStreamSource(String text)
  {
     return new StreamSource(new StringReader(text));
  }// ----------------------------------------------
    /**
     * Transform a given XML document
     * @param xls the XLS location
     * @param xml the xml string
     * @return the resulting transformation output
     */
    public static String transform(File xls, String xml)
    throws TransformerException
    {
       return transform(new StreamSource(xls), xml);
    }// --------------------------------------------

    /**
     * Transform a given XML document
     * @param xls the XLS location
     * @param xml the xml string
     * @return the resulting transformation output
     */
    public static String transform(URL xls, String xml)    
    {
       try
      {
         if(xls == null)
            throw new RequiredException("xls in XSL.transform");
          
          return transform(new StreamSource(xls.openStream()), xml);
      }
      catch (Exception e)
      {
         throw new SystemException("IO exception xsl="+xls+" \nerror="+Debugger.stackTrace(e)+" \n xml="+xml);
      }
    }// --------------------------------------------
	/**
	 * Transform a given XML document
	 * @param xls the XLS location
	 * @param xml the xml string
	 * @return the resulting transformation output
	 */
	public static String transform(StreamSource xls, String xml)	
	{
	   if (xml == null || xml.length() == 0)
		throw new RequiredException("xml");
	   
	   
	   try
	   {
	      // Use the static TransformerFactory.newInstance() method to instantiate 
	       // a TransformerFactory. The javax.xml.transform.TransformerFactory 
	       // system property setting determines the actual class to instantiate --
	       // org.apache.xalan.transformer.TransformerImpl.
	         TransformerFactory tFactory = TransformerFactory.newInstance();
	         
	         // Use the TransformerFactory to instantiate a Transformer that will work with  
	         // the stylesheet you specify. This method call also processes the stylesheet
	       // into a compiled Templates object.
	         Transformer transformer = tFactory.newTransformer(xls);

	         StreamSource xmlSource = new StreamSource(new StringReader(xml));
	         
	         StringWriter writer = new StringWriter();
	         
	         StreamResult outputTarget = new StreamResult(writer);
	         transformer.transform(xmlSource, outputTarget);
	         
	         
	         return writer.getBuffer().toString();
	   }
	   catch(TransformerException e)
	   {
	      throw new SystemException(Debugger.stackTrace(e));
	   }
	}//--------------------------------------------
  
}
