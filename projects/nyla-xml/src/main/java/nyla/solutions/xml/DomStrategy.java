package nyla.solutions.xml;

import java.io.InputStream;
import java.net.URL;
import org.w3c.dom.Document;


public interface DomStrategy
{

	/**
	 * Convert XML to DOM object
	 * 
	 * @param aXML
	 *            the input XML
	 * @return DOM object for the XML
	 * @throws org.dom4j.DocumentException
	 */
	public Document toDocument(String aXML);
	
	/**
	 * 
	 * @param inputStream the input stream
	 * @return document
	 */
	public Document toDocument(InputStream inputStream);
	
	/**
	 * Convert url to DOM object
	 * 
	 * @param url the input URL XML
	 * @return DOM object for the XML
	 * @throws org.dom4j.DocumentException
	 */
	public Document toDocument(URL url);
}
