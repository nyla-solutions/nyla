package nyla.solutions.core.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

public class XML
{
	public static Document toDocument(File file)
	throws IOException
	{
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.parse(file);
		}
		catch (Exception e)
		{
			throw new IOException(e.getMessage(), e);
		}

	}// ------------------------------------------------

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
	}// ------------------------------------------------

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
	}// ------------------------------------------------

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

	}// ------------------------------------------------

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

}
