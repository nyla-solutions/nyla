package nyla.solutions.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.JavaBean;
import nyla.solutions.core.util.Text;
import nyla.solutions.xml.xstream.XStreamInterpreter;

/**
 * 
 * @author Gregory Green
 *
 */
public class DOM4J extends XML
{
	/**
	 * Construct from w3c document
	 * @param document
	 */
	public DOM4J(org.w3c.dom.Document document)
	throws Exception
	{
		this(DOM4J.toXML(document));	
	}// --------------------------------------------
	public DOM4J(File file)
	throws IOException, Exception
	{
		this(IO.readFile(file));
		
	}//------------------------------------------------
	/**
	 * 
	 * @param aXml
	 *            input XML
	 * @throws Exception
	 */
	public DOM4J(String aXml) throws Exception
      {
	   if (aXml == null || aXml.length() == 0)
		throw new RequiredException("xml");
	   
	   this.xml = aXml;

		try
      {
         // Set an ErrorHandler before parsing
         //xmlReader.setErrorHandler(new CommandErrorHandler(System.err));
         // Tell the XMLReader to parse the XML document
         InputSource is = new InputSource(new StringReader(aXml));
         org.dom4j.io.SAXReader reader = new SAXReader();
         document = reader.read(is);
      }
      catch (Exception e)
      {
         throw new SystemException(" XML="+aXml+" ERROR:"+Debugger.stackTrace(e));
      }

	}//-------------------------------------
	
	public static String getText(org.w3c.dom.Node node)
	{
		String value = node.getNodeValue();
		
		if(value != null && value.length() > 0)
			return value;
		
		org.w3c.dom.Node firstChild  = node.getFirstChild();
		
		value = firstChild.getNodeValue();
		
		if(value != null && value.length() > 0)
			return value;
				
		return ""; //no value found
	}// --------------------------------------------
	/**
	 * 
	 * @param url the url where to read input source
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
       aElementName = DOM4J.escapeElementEntities(aElementName);
       
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
      
     ResultSetMetaData metaData = aResultSet.getMetaData();
     Map<Integer,String> columnNames = new Hashtable<Integer,String>();
     Map<Integer,String> columnClassNames = new Hashtable<Integer,String>();
     Map<Integer,String> columnTypeNames = new Hashtable<Integer,String>();
  
     Integer columnNumber = null;
     for(int i=1;i <=metaData.getColumnCount();i++)
     {
        columnNumber = Integer.valueOf(i);
        
        columnNames.put(columnNumber,metaData.getColumnLabel(i));
        columnClassNames.put(columnNumber,metaData.getColumnClassName(i));
        columnTypeNames.put(columnNumber,metaData.getColumnTypeName(i));
        //columnDisplaySizes.put(columnNumber,new Integer(metaData.getColumnDisplaySize(i)));
     }
     
     org.dom4j.DocumentFactory factory = org.dom4j.DocumentFactory.getInstance();
     
     org.dom4j.Element root = factory.createElement("ResultSet");
     org.dom4j.Element child = null;
     String value =null;
     org.dom4j.Element rowElement = null;
     while(aResultSet.next())
     {
        rowElement = factory.createElement("Row");
        for(Map.Entry<Integer,String> entry : columnNames.entrySet())
        {
           columnNumber = entry.getKey();
           
           value = String.valueOf(aResultSet.getObject(columnNumber.intValue()));
           child = factory.createElement(entry.getValue());
           
           child.add(factory.createAttribute(child,"type",
                 (String)columnTypeNames.get(columnNumber)));
           
           child.addText(value);
           rowElement.add(child);
        }
        root.add(rowElement);
     }
     
     return convertToXML(root);
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

	/**
	 * 
	 * @param aXpath
	 *            the XPATH element i.e. "/root/element@attribName"
	 * @return the node that matches the XPATH
	 * @throws Exception
	 *             when an internal errors
	 */
	public org.dom4j.Node retrieveNode(String aXpath) throws Exception {
		return this.document.selectSingleNode(aXpath);
	}//--------------------------------------

	/**
	 * The root name should exist in the Map with key "class"
	 * 
	 * @param map
	 *            return root class name from the map
	 * @return
	 */
	private static <K,V> String getRootName(Map<K,V> map) {
		String root = String.valueOf(map.get("class"));

		int lastIndex = root.lastIndexOf(".");
		if (lastIndex < 0)
			return root;
		else
			return root.substring(lastIndex + 1, root.length());

	}
	//-----------------------------------------------------------

	/**
	 * lightweight XML parse method for retrieving XML elements
	 * 
	 * @param aXpath
	 *            the full XPATH expression (attribs not supported)
	 * @return the element from the aXml document
	 */
	public String retrieveElementValue(String aXpath) throws Exception {

		//treeWalk(document);

		Object object = document.selectObject(aXpath);
		if (object instanceof Node) {
			Node n = (Node) object;

			return n.getStringValue().trim();

		}

		if (object == null)
			return "";
		else
			return object.toString();

	}//-------------------------------------------------------------

	/**
	 * Print XML content to standard out
	 *  
	 */
	public void dump() {
		DOM4J.treeWalk(this.document);

	}//------------------------------------------------

	/**
	 * Print XML content to standard out
	 *  
	 */
	public static void treeWalk(org.dom4j.Document document) {
		treeWalk(document.getRootElement());
	}//-------------------------------------------------------

	/**
	 * Print XML content to standard out
	 *  
	 */
	@SuppressWarnings("unchecked")
	public static void treeWalk(org.dom4j.Element element) {

		System.out.println(element.getPath());

		//Print attributes
		org.dom4j.Attribute attribute = null;
		for (Iterator<org.dom4j.Attribute> i = element.attributeIterator(); i.hasNext();) {
			attribute = (org.dom4j.Attribute) i.next();
			System.out.print(attribute.getPath());
		}

		// Print others

		for (int i = 0, size = element.nodeCount(); i < size; i++) {
			org.dom4j.Node node = element.node(i);
			if (node instanceof org.dom4j.Element) {
				treeWalk((org.dom4j.Element) node);
			} else {
				//System.out.print(node.getPath());
			}
		}
	}//-------------------------------------------------------

	/**
	 * validation the XML against a MAP of XPATH elements/Regular expressions
	 * pairs
	 * 
	 * @throws Exception
	 *             when an XML parsing XML occurs
	 */
	public void validate(Map<Object,String> aValidations) 
	throws IllegalArgumentException,Exception 
	{
		String rexpr = null;
		String xml_val = null;
		Object key = null;



		for(Map.Entry<Object,String> entry : aValidations.entrySet()) 
		{

			key = entry.getKey();

			rexpr = String.valueOf(entry.getValue());

			xml_val = retrieveElementValue(String.valueOf(key));

			Debugger.println("xml_val=" + xml_val);


			
			if (!Text.matches(xml_val,rexpr)) {
				throw new IllegalArgumentException("XML element \"" + key
						+ "\" value \"" + xml_val
						+ "\" does not match regular expression \"" + rexpr
						+ "\"");
			}

		}//end while
	}//-------------------------------------------------------------

	/**
	 * Convert XML to DOM object
	 * 
	 * @param aXML
	 *            the input XML
	 * @return DOM object for the XML
	 * @throws org.dom4j.DocumentException
	 */
	public static org.dom4j.Document toDocument(String aXML)
			throws org.dom4j.DocumentException 
	{
		org.dom4j.io.SAXReader saxReader = new org.dom4j.io.SAXReader();
		return saxReader.read(new ByteArrayInputStream(aXML.getBytes(IO.CHARSET)));
	}//-------------------------------------------------------------


	public static void main(String[] args) {

		if (args.length < 1) {
			throw new RuntimeException("Usage XMLAdapter xmlFile");
		}

		try {
			System.out.println("Reading file=" + args[0]);
			String xml = IO.readFile(args[0]);

			treeWalk(toDocument(xml));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//-----------------------------------------------------------------------


	/**
	 * Convert a single object to XML
	 */
	public static String toXML(Object obj) throws Exception {

		return convertToXML(toElement(obj));
	}//---------------------------------------------------

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static org.dom4j.Element toElement(Object obj) throws Exception {
		Map map = org.apache.commons.beanutils.PropertyUtils.describe(obj);
		
		
		return toElementForMap(map);
	} //--------------------------------------------------------

	/**
	 * Initialize the bean property for the Element
	 */
	public void populate(Object aObject) throws Exception {
		populate(aObject, this.document.getRootElement());
	}//-------------------------------------

	/**
	 * Initialize the bean property for the Element Note that the object's
	 * simple properties cannot be NULL.
	 */
	@SuppressWarnings("unchecked")
	public static void populate(Object obj, Element element) throws Exception {
		org.dom4j.Element child = null;
		for (Iterator<org.dom4j.Element> i = element.elementIterator(); i.hasNext();) {

			child = (org.dom4j.Element) i.next();

			Object p = null;
			try {
				p = JavaBean.getProperty(obj, child.getName());
				Class<?> c = org.apache.commons.beanutils.PropertyUtils.getPropertyType(obj, child.getName());
				String classType = c.getName();

				if (p instanceof Integer) {

					org.apache.commons.beanutils.PropertyUtils.setProperty(obj, child.getName(),
							Integer.valueOf(child.getStringValue()));
				} else if (p instanceof Double) {
					org.apache.commons.beanutils.PropertyUtils.setProperty(obj, child.getName(), Double.valueOf(
							child.getStringValue()));
				} else if (p instanceof Short) {
					org.apache.commons.beanutils.PropertyUtils.setProperty(obj, child.getName(),  Short.valueOf(
							child.getStringValue()));
				} else if (p instanceof Boolean) {
					org.apache.commons.beanutils.PropertyUtils.setProperty(obj, child.getName(),
							 Boolean.valueOf(child.getStringValue()));
				//} else if (p instanceof Object[]) {
				} else if (c.isArray()) {
					
					String className = classType.substring("[L".length(), classType.indexOf(";"));
					Class<?> thisClass = Class.forName(className);
					ArrayList<Object> list = new ArrayList<Object>();
					
					Object o = null;
					for (Iterator<Object> i1 = child.elementIterator(); i1.hasNext();) {
						o = thisClass.newInstance();
						populate(o, (org.dom4j.Element)i1.next());
						list.add(o);
					}
					Object[] array = (Object[]) Array.newInstance(thisClass, list.size());
					/*
					 * System.out.println(array.getClass().getName());
					 * System.out.println(className);
					 * System.out.println(child.getName());
					 */
					PropertyUtils.setProperty(obj, child.getName(), list.toArray(array));
					
					
				} else if (classType.equals("java.util.Date")) {
					Date date = getDateFromW3CDateTime(child.getStringValue());
					PropertyUtils.setProperty(obj, child.getName(), date);

				} else if (classType.equals("java.util.GregorianCalendar")
						|| classType.equals("java.util.Calendar")) {
					Date date = getDateFromW3CDateTime(child.getStringValue());
					GregorianCalendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					PropertyUtils.setProperty(obj, child.getName(), calendar);
				} else {
					PropertyUtils.setProperty(obj, child.getName(), child
							.getStringValue());
				}
			} 
         catch(NoSuchMethodException e)
         {
         }
         catch (Exception e) {
				throw e;
			}
		}
	}//-------------------------------------------------------

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
	}

	public void walk(org.dom4j.Document document) {
		walk(document.getRootElement());
	}//-------------------------------------------------------

	public void walk(org.dom4j.Element element) {
		for (int i = 0, size = element.nodeCount(); i < size; i++) {
			Node node = element.node(i);
			if (node instanceof Element) {
				walk((Element) node);
			}
		}
	}//-------------------------------------------

	/**
	 * Convert DOM element to an XML element
	 * 
	 * @param root
	 *            the top XML element
	 * @return XML version of element
	 * @throws IOException
	 *             when an internal error occurs
	 * @throws UnsupportedEncodingException
	 *             when an internal error occurs
	 */
	private static String convertToXML(org.dom4j.Element root) throws IOException,
			UnsupportedEncodingException {
		org.dom4j.Document doc = factory.createDocument(root);

		java.io.ByteArrayOutputStream out = new ByteArrayOutputStream();
		org.dom4j.io.XMLWriter writer = new org.dom4j.io.XMLWriter(new OutputStreamWriter(out, IO.CHARSET));

		writer.write(doc);
		writer.flush();
		String text = out.toString(IO.CHARSET_NM);

		// Remove top "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
		if (text.indexOf("<?xml") > -1) {
			int end = text.indexOf(">");
			text = text.substring(end + 1).trim();
		}

		return text;

	}//---------------------------------------------------------

	private static String getElementNameFromClass(Object aObject) {
		String name = aObject.getClass().getName();
		int i = name.lastIndexOf(".");

		return name.substring(i + 1);
	}
	//------------------------------------------------------

	@SuppressWarnings("unchecked")
	private static org.dom4j.Element toElementForMap(Map<Object,Object> map) throws Exception {

		org.dom4j.Element root = factory.createElement(getRootName(map));
		//Debugger.toString(map.get("class")));

		Set<Object> kset = map.keySet();
		String key = null;
		Object value = null;
		org.dom4j.Element element = null;

		Collection<Object> nestedCollection = null;
		Object nestedObj = null;
		Object[] nestedArray = null;
		for (Iterator<Object> i = kset.iterator(); i.hasNext();) {
			key = String.valueOf(i.next());

			if ("class".equals(key)) {
				continue;
			}

			value = map.get(key);

			element = factory.createElement(key);

			if (value instanceof Collection) {
				nestedCollection = (Collection<Object>) value;

				for (Iterator<Object> nestedI = nestedCollection.iterator(); nestedI
						.hasNext();) {
					//add nested object
					nestedObj = nestedI.next();
					element.add(toElement(PropertyUtils.describe(nestedObj)));
				}
			} else if (value instanceof Object[]) {
				nestedArray = (Object[]) value;

				for (int c = 0; c < nestedArray.length; c++) {
					//add nested object
					nestedObj = nestedArray[c];

					if (nestedObj == null)
						continue;

					if (nestedObj.getClass().getName().indexOf("java.lang") > -1) {
						Element e = factory
								.createElement(getElementNameFromClass(nestedObj));
						e.addText(nestedObj.toString());
						element.add(e);
					} else
						element
								.add(toElement(PropertyUtils
										.describe(nestedObj)));
				}
			} else {
				element.add(factory.createText(toString(value)));
			}

			root.add(element);
		}

		return root;
	}
	//--------------------------------------------------------

	/**
	 * 
	 * @param o
	 * @return "" if the object is null
	 */
	private static String toString(Object o) {
		if (o == null)
			return "";
		if (o instanceof java.util.Date)
			return getISO8601DateTime((Date) o);
		if (o instanceof Calendar)
			return getISO8601DateTime(((Calendar) o).getTime());
		return String.valueOf(o);
	}
	//---------------------------------------

	

	private static String getISO8601DateTime(Date date) {
		SimpleDateFormat ISO8601Local = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
		TimeZone timeZone = TimeZone.getDefault();
		ISO8601Local.setTimeZone(timeZone);
		DecimalFormat twoDigits = new DecimalFormat("00");

		int offset = ISO8601Local.getTimeZone().getOffset(date.getTime());
		String sign = "+";
		if (offset < 0) {
			offset = -offset;
			sign = "-";
		}
		int hours = offset / 3600000;
		int minutes = (offset - hours * 3600000) / 60000;

		String ISO8601Now = ISO8601Local.format(date) + sign
				+ twoDigits.format(hours) + ":" + twoDigits.format(minutes);
		return ISO8601Now;
	}
	/**
	 * @return the XML
	 */
	public String getXml()
	{
		return xml;
	}
	/**
	 * @param xml the xml to set
	 */
	public void setXml(String xml)
	{
		this.xml = xml;
	}
	private static org.dom4j.DocumentFactory factory = org.dom4j.DocumentFactory.getInstance();

	private org.dom4j.Document document = null;

	private String xml = null;
	
}