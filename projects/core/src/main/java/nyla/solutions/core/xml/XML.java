package nyla.solutions.core.xml;

import nyla.solutions.core.exception.IoException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * XML utility class
 * @author Gregory Green
 *
 */
public class XML
{
	private final Document document;

	/**
	 * Create an instance
	 * @param xml the XML text
	 * @throws ParserConfigurationException when parsing exception occurs
	 * @throws SAXException when SAX exception occurs
	 */
	public XML(String xml) throws ParserConfigurationException,SAXException
	{
		this(toDocument(xml));
	}

	public XML(Document document)
	{
		this.document = document;
	}

	public static Document toDocument(File file)
	{
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.parse(file);
		}
		catch (Exception e)
		{
			throw new IoException(e.getMessage(), e);
		}

	}

	public static String findAttrByRegExp(String attributeNameExp, Node node)
	{
		if (
			node == null ||
			node.getNodeType() == Node.COMMENT_NODE ||
			node.getNodeType() == Node.TEXT_NODE
		)
			return null;

		Pattern pattern = Pattern.compile(attributeNameExp);
		String results = getAttribute(node, pattern);

		if (results != null)
			return results;

		NodeList nodeList = node.getChildNodes();

		if (nodeList == null)
			return null;

		int len = nodeList.getLength();

		Node child;
		for (int i = 0; i < len; i++)
		{
			child = nodeList.item(i);

			if (child.getNodeType() == Node.TEXT_NODE)
				continue;

			results = getAttribute(child, pattern);

			if (results != null)
				return results;

			// check for next
			NodeList grands = child.getChildNodes();
			if (grands != null)
			{
				int grandsCnt = grands.getLength();
				for (int x = 0; x < grandsCnt; x++)
				{
					String grandResults = findAttrByRegExp(attributeNameExp, grands.item(x));

					if (grandResults != null)
						return grandResults;
				}
			}

		}

		return null;
	}

	public static Document toDocument(String xml) {
        try {
            return DocumentBuilderFactory.newInstance()
                                  .newDocumentBuilder()
                                  .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

	public static String searchNodeTextByXPath(String expression, Node document)
	{
		NodeList nodeList = searchNodesXPath(expression, document);
		if(nodeList == null || nodeList.getLength() < 1)
			return null;

		Node node = nodeList.item(0);
		if(node == null)
			return null;

		return node.getTextContent();
	}

	public String findAttrByRegExp(String attributeName)
	{
		return findAttrByRegExp(attributeName,document);
	}

	public static NodeList searchNodesXPath(String expression, Node doc)
	{
		try
		{
			XPath xPath = XPathFactory.newInstance().newXPath();

			return (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
		}
		catch (XPathExpressionException e)
		{
			throw new IllegalArgumentException("expression:" + expression + " error" + e.getMessage(), e);
		}
	}

	public NodeList searchNodesXPath(String expression)
	{
		return XML.searchNodesXPath(expression,document);
	}

	public Collection<Node> findElementsByName(String elementName)
	{
		return XML.findElementsByName(elementName,document);
	}
	public static Collection<Node> findElementsByName(String elementName, Node node)
	{
		if ( node == null ||
			node.getNodeType() == Node.COMMENT_NODE ||
			node.getNodeType() == Node.TEXT_NODE )
			return null;

		HashSet<Node> results = new HashSet<Node>();

		if (elementName.equals(node.getNodeName()))
		{
			results.add(node);
		}

		NodeList nodeList = node.getChildNodes();

		if (nodeList == null)
			return null;

		int len = nodeList.getLength();

		Node child;

		for (int i = 0; i < len; i++)
		{
			child = nodeList.item(i);

			if (child.getNodeType() == Node.TEXT_NODE)
				continue;

			if (elementName.equals(child.getNodeName()))
				 results.add(child);

			// check for next
			NodeList grands = child.getChildNodes();
			if (grands != null)
			{
				int grandsCnt = grands.getLength();
				for (int x = 0; x < grandsCnt; x++)
				{
					Collection<Node> grandResults = findElementsByName(elementName, grands.item(x));

					if (grandResults != null)
						results.addAll(grandResults);
				}
			}
		}

		return results.isEmpty()? null : results;

	}

	public static Node findElementByName(String elementName, Node node)
	{

		if (
			node == null ||
			node.getNodeType() == Node.COMMENT_NODE ||
			node.getNodeType() == Node.TEXT_NODE
		)
			return null;

		if (elementName.equals(node.getNodeName()))
		{
			return node;
		}

		NodeList nodeList = node.getChildNodes();

		if (nodeList == null)
			return null;

		int len = nodeList.getLength();

		Node child;

		for (int i = 0; i < len; i++)
		{
			child = nodeList.item(i);

			if (child.getNodeType() == Node.TEXT_NODE)
				continue;

			if (elementName.equals(child.getNodeName()))
				return child;

			// check for next
			NodeList grands = child.getChildNodes();
			if (grands != null)
			{
				int grandsCnt = grands.getLength();
				for (int x = 0; x < grandsCnt; x++)
				{
					Node grandResults = findElementByName(elementName, grands.item(x));

					if (grandResults != null)
						return grandResults;
				}
			}
		}

		return null;
	}

	private static String getAttribute(Node child, Pattern pattern)
	{
		NamedNodeMap attributes;
		attributes = child.getAttributes();

		if (attributes != null)
		{
			int attribLen = attributes.getLength();
			for (int j = 0; j < attribLen; j++)
			{
				Node cNode = attributes.item(j);

				if (pattern.matcher(cNode.getNodeName()).matches())
					return cNode.getNodeValue();
			}
		}
		return null;
	}


	public NodeList findNodes(String elementName)
	{
		return document.getElementsByTagName(elementName);
	}


	public Document getDocument()
	{
		return this.document;
	}
}
