package nyla.solutions.xml.patterns.decorator.w3cdom;


import org.w3c.dom.Document;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.cache.CacheFarm;
import nyla.solutions.core.util.Config;
import nyla.solutions.xml.DOM4J;


/**
 * Abstract textable objects to get text from cached document object.
 * @author Gregory Green
 *
 */
public abstract class AbstractCacheText implements Textable
{

	/**
	 * 
	 * @return (Document)CacheFarm.getCache().get(this.cacheKey)
	 */
	protected Document getDocument()
	{
		Document document = (Document)CacheFarm.getCache().get(this.cacheKey);
		
		if(document == null)
			throw new SystemException("Document not found in cache with key ="+this.cacheKey);
		
		return document;
	}// --------------------------------------------
	
	protected DOM4J getXML()
	{
		DOM4J xml = (DOM4J)CacheFarm.getCache().get(this.xmlCacheKey);
		
		if(xml == null)
			throw new SystemException("XML not found in cache with key ="+this.xmlCacheKey);
		
		return xml;
	}// --------------------------------------------
	
	/**
	 * @return the cacheKey
	 */
	public String getCacheKey()
	{
		return cacheKey;
	}

	/**
	 * @param cacheKey the cacheKey to set
	 */
	public void setCacheKey(String cacheKey)
	{
		this.cacheKey = cacheKey;
	}

	private String xmlCacheKey = Config.getProperty(this.getClass(),"xmlCacheKey","xml");
	private String cacheKey = Config.getProperty(this.getClass(),"cacheKey","document");
}
