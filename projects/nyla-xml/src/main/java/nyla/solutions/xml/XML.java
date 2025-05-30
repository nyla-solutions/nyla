package nyla.solutions.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

// Imported TraX classes
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.xml.xstream.XStreamInterpreter;

/**
 * Wrapper for XML operations
 * @author Gregory Green
 *
 */
public class XML 
{
	protected XML()
	{
	}//------------------------------------------------
	/**
	 * Extract the xpath results
	 * @param xml the xml 
	 * @param xpathExpress the XPATH
	 * @return the results of the XPATH
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	public static String getXPath(String xml, String xpathExpress) 
	throws ParserConfigurationException, SAXException, IOException, XPathExpressionException
	{
		 DocumentBuilder builder =
	               DocumentBuilderFactory.newInstance().newDocumentBuilder();
	      
		 Document document = builder.parse(toInputSource(xml));
	   
	     XPath xpath = XPathFactory.newInstance().newXPath();
	      
	     // Next, obtain the element as a String
	     return (String)xpath.evaluate(xpathExpress, document, XPathConstants.STRING);
	 }// -----------------------------------------------

	/**
	 * 
	 * @param xml the xml to convert
	 * @return the created InputSource
	 */
	public static InputSource toInputSource(String xml)
	{		
		return new InputSource(new BufferedReader(new StringReader(xml)));
	}// --------------------------------------------
	
	
	public static String getText(org.w3c.dom.Node node)
	{
		String value = node.getNodeValue();
		
		if(value != null && value.length() > 0)
			return value;
		
		org.w3c.dom.Node firstChild  = node.getFirstChild();
		
		value = firstChild.getNodeValue();
		
		if(value != null && value.length() > 0)
			return value;
		
		Debugger.println(XML.class,"value="+value);
		return ""; //no value found
	}// --------------------------------------------
	/**
	 * 
	 * @param url the URL that contains the input source
	 * @return the created InputSource
	 */
	public static InputSource toInputSource(URL url)
	{
		try
		{
			return new InputSource(url.openStream());
		}
		catch (Exception e)
		{
			throw new SystemException(Debugger.stackTrace(e));
		}
	}// --------------------------------------------
	public static InputSource toInputSource(InputStream inputStream)
	{
		try
		{
			return new InputSource(inputStream);
		}
		catch (Exception e)
		{
			throw new SystemException(Debugger.stackTrace(e));
		}
	}// --------------------------------------------
	/**
	 * 
	 * @return XStreamInterpreter
	 */
	public static XMLInterpreter getInterpreter()
	{
	   return new XStreamInterpreter();
	}// --------------------------------------------
    public static String toElementText(String aElementName, String aText)
    {
       aElementName = XML.escapeElementEntities(aElementName);
       
       StringBuffer element = new StringBuffer("<").append(aElementName).append(">");
       
       element.append(aText);
       element.append("</").append(aElementName).append(">");
       
       return element.toString();
    }// --------------------------------------------

    /**
     * 
     * @param xml
     * @return the striped the XML heade
     */
    public static String stripHeader(String xml)
    {
	 if (xml == null)
	   throw new RequiredException("xml");
	 
       //remove header
       int start = xml.indexOf("<?xml");
       
       if(start < 0) // does not contain header
          return xml;
       
       //find ?>
       int end = xml.indexOf("?>", start);
       
       if(end < start) //does not contain end
          return xml;
       
       return xml.substring(end+"?>".length()).trim();
    }//--------------------------------------------
   /***
    * Return XML version of a result set
    * @param aResultSet the result set
    * @return the XML version
    * @throws java.sql.SQLException
    * @throws Exception
    */
   public static String toXML(ResultSet aResultSet)
   throws java.sql.SQLException, Exception
   {
      
     return DOM4J.toXML(aResultSet);
   }//--------------------------------------
	/**
	 * Escape all special characters in the input string
	 * 
	 * @param str
	 *            string to escape
	 * @return String with all special characters encoded
	 */
	public static String escapeAttributeEntities(String str) {
		if (str == null) {
			return "";
		}

		StringBuffer buffer = null;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			String entity;
			switch (ch) {
			case 60: // '<'
				entity = "&lt;";
				break;

			case 62: // '>'
				entity = "&gt;";
				break;

			case 34: // '"'
				entity = "&quot;";
				break;

			case 38: // '&'
				entity = "&amp;";
				break;

			default:
				entity = null;
				break;
			}
			if (buffer == null) {
				if (entity != null) {
					buffer = new StringBuffer(str.length() + 20);
					buffer.append(str.substring(0, i));
					buffer.append(entity);
				}
			} else if (entity == null)
				buffer.append(ch);
			else
				buffer.append(entity);
		}

		return buffer != null ? buffer.toString() : str;
	}//--------------------------------------------------------

	public static String escapeElementEntities(String str) {
		if (str == null) {
			return "";
		}

		StringBuffer buffer = null;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			String entity;
			switch (ch) {
			case 60: // '<'
				entity = "&lt;";
				break;

			case 62: // '>'
				entity = "&gt;";
				break;

			case 38: // '&'
				entity = "&amp;";
				break;

			default:
				entity = null;
				break;
			}
			if (buffer == null) {
				if (entity != null) {
					buffer = new StringBuffer(str.length() + 20);
					buffer.append(str.substring(0, i));
					buffer.append(entity);
				}
			} else if (entity == null)
				buffer.append(ch);
			else
				buffer.append(entity);
		}

		return buffer != null ? buffer.toString() : str;
	}//-------------------------------------------------------

	public static String transform(String xml, String aXslUrl) throws TransformerException,
			TransformerConfigurationException, IOException {

		try
		{
		   if (xml == null)
		      throw new RequiredException("xml");
		   		   
		   //new StreamSource(		   		aXslUrl));

		   return XSL.transform(new URL(aXslUrl), xml);
		   
		} 
		catch (Exception e)
		{
		   
		   throw new TransformerConfigurationException("URL="+aXslUrl+" "+e);
		}
	}//----------------------------------------------------------------------

	public static String toXML(org.w3c.dom.Node node) 
	{
		try 
		{
	            Source source = new DOMSource(node);
	            StringWriter stringWriter = new StringWriter();
	            Result result = new StreamResult(stringWriter);
	            TransformerFactory factory = TransformerFactory.newInstance();
	            Transformer transformer = factory.newTransformer();
	            transformer.transform(source, result);
	            return stringWriter.getBuffer().toString();
	        } 
		catch (Exception e) 
		{
			throw new SystemException(Debugger.stackTrace(e));
		}
	}// --------------------------------------------


	public static Date getDateFromW3CDateTime(String dateStr) throws Exception {
		if (dateStr.indexOf("Z") != -1) {
			dateStr = dateStr.substring(0, dateStr.indexOf("Z")) + "+00:00";
		}
		int msIdx = dateStr.indexOf(".", dateStr.indexOf("T"));
		int signIdx = dateStr.indexOf("-", dateStr.indexOf("T"));
		int fracSecLen = 0;
		if (signIdx == -1)
			signIdx = dateStr.indexOf("+");
		if (signIdx != -1) {
			String front = dateStr.substring(0, dateStr.indexOf(":", signIdx));
			String back = dateStr.substring(dateStr.indexOf(":", signIdx) + 1,
					dateStr.length());
			dateStr = front + back;
			if (msIdx != -1)
				fracSecLen = signIdx - msIdx;
		} else {
			if (msIdx != -1)
				fracSecLen = dateStr.length() - msIdx;
		}

		StringBuffer sb = new StringBuffer();
		String dot = ".";
		for (int i = 0; i < fracSecLen; i++) {
			sb.append(dot);
			sb.append("S");
			dot = "";
		}
		if (signIdx != -1)
			sb.append("Z");
		String dfStr = "yyyy-MM-dd'T'HH:mm:ss" + sb.toString();

		SimpleDateFormat sdf = new SimpleDateFormat(dfStr);
		Date date = sdf.parse(dateStr);
		return date;
	}// ------------------------------------------------
}