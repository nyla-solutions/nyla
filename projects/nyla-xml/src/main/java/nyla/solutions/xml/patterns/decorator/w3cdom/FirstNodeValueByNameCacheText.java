package nyla.solutions.xml.patterns.decorator.w3cdom;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.xml.XML;

/**
 * Return the value of the first node in a document by the node name.
 * @author Gregory Green
 *
 */
public class FirstNodeValueByNameCacheText extends AbstractCacheText
{
	/**
	 * @return the node value of a give node name
	 */
	public String getText()
	{
		Document dom = this.getDocument();
		
		Debugger.println("DOM="+dom);
		
		if(this.name == null || this.name.length() == 0)
			throw new RequiredException(this.getClass().getName()+"name"); 
			
		NodeList nodeList = dom.getElementsByTagName(name);
		if(nodeList == null || nodeList.getLength() == 0)
			return "";
		
		Debugger.println(this,"Looking for node="+name);
		//get first element
		Node node = nodeList.item(0);
		return XML.getText(node);
		
	}// --------------------------------------------

	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	private String name = null;

	
}
