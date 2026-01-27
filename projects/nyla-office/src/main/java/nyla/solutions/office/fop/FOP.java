package nyla.solutions.office.fop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;


/**
 * <pre>
 * FOP is a wrapper for Apache FOP API
 * 
 * <strong>Use Case</strong>
 * 
 * String fo = IO.readClassPath("pdf/example.fop");
		
File file = new File("src/test/resources/pdf/test.pdf");
file.delete();
		
FOP.writePDF(fo, file);
Assert.assertTrue(file.exists());
	
 * 
  <strong>Another example FO</strong>
  
  &lt;?xml version="1.0" encoding="ISO-8859-1"?&gt;
&lt;fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"&gt;

&lt;fo:layout-master-set&gt;
  &lt;fo:simple-page-master master-name="A4"&gt;
    &lt;fo:region-body /&gt;
  &lt;/fo:simple-page-master&gt;
&lt;/fo:layout-master-set&gt;

&lt;fo:page-sequence master-reference="A4"&gt;
  &lt;fo:flow flow-name="xsl-region-body"&gt;
    &lt;fo:block&gt;Hello World&lt;/fo:block&gt;
  &lt;/fo:flow&gt;
&lt;/fo:page-sequence&gt;
&lt;/fo:root&gt;


<strong>Add images</strong>
 *   &lt;fo:block&gt;
    &lt;fo:external-graphic src="../graphics/xml_feather_transparent.gif"/&gt;
  &lt;/fo:block&gt;
  
  
Also see <a href="http://w3schools.sinsixx.com/xslfo/default.asp.htm">http://w3schools.sinsixx.com/xslfo/default.asp.htm</a>
  </pre>
 * @author Gregory Green
 *
 */

public class FOP
{

	private static int byteBufferSize = Config.settings().getPropertyInteger(FOP.class,"byteBufferSize",50000).intValue();

	/**
	 * PDF_FORMAT = MimeConstants.MIME_PDF (application/pdf)
	 */
	public static final String PDF_FORMAT = MimeConstants.MIME_PDF;
	
      /**
       * Converts an FO file to a PDF file using FOP
       * @param fo the FO file
       * @return the PDF bytes
       * @throws IOException In case of an I/O problem
       * @throws FOPException In case of a FOP problem
       */
      public static byte[] toPDF(String fo) throws IOException, FOPException 
      {    
    	  return toBinary(fo, MimeConstants.MIME_PDF);
      }
      /**
       * Create a PDF document
       * @param fo the FO-XML
       * @param file the output file
       * @throws FileNotFoundException
       */
      public static void writePDF(String fo, File file)
      throws FileNotFoundException
      {
    	  FileOutputStream fileOut = null;
    	  
    	  try
    	  {
    		  fileOut= new FileOutputStream(file);
    		  writeOutputStream(fo, PDF_FORMAT, fileOut);
    	  } 
    	  catch (Exception e)
    	  {	
    		  throw new SystemException(Debugger.stackTrace(e));
		  }    	
    	  finally
    	  {
    		  if(fileOut != null)
    			  try{ fileOut.close(); } catch(Exception e){}
    	  }    	  
      }
      public static void writePDF(String fo, FileOutputStream fileOut)
      throws FileNotFoundException
      {
    	  try
    	  {    	
    		  writeOutputStream(fo, PDF_FORMAT, fileOut);
    	  } 
    	  catch (Exception e)
    	  {	
    		  throw new SystemException(Debugger.stackTrace(e));
		  }    	
    	  finally
    	  {    		
    	  }    	  
      }
          /**
           * Converts an FO file to a PDF file using FOP
           * @param fo the FO file
           * @throws IOException In case of an I/O problem
           * @throws FOPException In case of a FOP problem
           */
      private static byte[] toBinary(String fo, String outputFormat) throws IOException, FOPException 
      {  
    	  ByteArrayOutputStream pdfOUT = null;
    	  //BufferedOutputStream  out = null;
    	  InputStream templateIS = null;
    	  
          try 
          {
              // configure foUserAgent as desired
      
              // Setup output stream.  Note: Using BufferedOutputStream
              // for performance reasons (helpful with FileOutputStreams).
              
              pdfOUT = new ByteArrayOutputStream(byteBufferSize);
              templateIS = writeOutputStream(fo, outputFormat, pdfOUT);
             
              return pdfOUT.toByteArray();

          } 
          catch (Exception e) 
          {
              Debugger.printError(e);
              throw new SystemException(Debugger.stackTrace(e));
          } 
          finally 
          {
        	  if( pdfOUT != null)
        		  try{ pdfOUT.close();} catch(Exception e){}
        		  
        	  //if(out != null)
        	//	  try{out.close(); } catch(Exception e){} 
        		  
        	  if(templateIS != null)
        		  try{ templateIS.close(); } catch(Exception e){}
        		                
          }
      }
      public static InputStream writeOutputStream(String fo,String outputFormat, OutputStream pdfOUT)
      throws  TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException
      {
    	  try
    	  {
	      	  FopFactory fopFactory = FopFactory.newInstance();
	    	  
	          FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
	          // configure foUserAgent as desired

          
			InputStream templateIS;
			//out = new BufferedOutputStream(pdfOUT);

              // Construct FOP with desired output format
              //private FopFactory fopFactory = FopFactory.newInstance();
              Fop fop = fopFactory.newFop(outputFormat, foUserAgent,pdfOUT );

              // Setup JAXP using identity transformer
              TransformerFactory factory = TransformerFactory.newInstance();
              Transformer transformer = factory.newTransformer(); // identity transformer
              
              // Setup input stream
              templateIS = Text.format().toInputStream(fo);
              Source src = new StreamSource(templateIS);

              // Resulting SAX events (the generated FO) must be piped through to FOP
              Result res = new SAXResult(fop.getDefaultHandler());
              
              // Start XSLT transformation and FOP processing
              transformer.transform(src, res);
			return templateIS;
    	  }
    	  catch(FOPException e)
    	  {
    		  throw new SystemException(e.getMessage(),e);
    	  }
		}


      
}
