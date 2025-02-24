package nyla.solutions.xml;
// JAXP packages

import java.util.StringTokenizer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import nyla.solutions.core.util.Debugger;


/**
 * <b>XPathHandler<b> XML based XPATH Document Handler 
 * @author Gregory Green 
 */
class XPathHandler extends DefaultHandler
{
    /**
     * @param aSearchElement the XPATH search text
     */
    public XPathHandler(String aSearchElement)
    throws Exception
    {
        if( aSearchElement != null && aSearchElement.indexOf(ATTRIB_PATH) > -1 )
        {
            StringTokenizer tok = new StringTokenizer(ATTRIB_PATH,aSearchElement);
            the_se = tok.nextToken();
            the_attrib = tok.nextToken();
            Debugger.println(this,"search element="+the_se+" attrib="+the_attrib);
        }
           the_se = aSearchElement;
    }//-------------------------------------------------
    /**
     Parser calls this once at the beginning of a document
     */
    public void startDocument() throws SAXException {
    }
    /** Receive notification of character data. */
    public void characters(char[] ch, int start, int length) 
    {

       String val = new String(ch,start,start+length).toUpperCase();

       Debugger.println(this,"val="+val);

       //check if elementNM equals search text
       if(equalsPath())
       {
          the_result = val;
          throw new RuntimeException(val);
       }
    }//-------------------------------------------------------
    /**
      Parser calls this for each element in a document
     */
    public void startElement(String namespaceURI, String localName,
                             String rawName, Attributes atts)
	throws SAXException
    {
        if(localName == null )
           return;

        the_elementNM = the_elementNM+"/"+localName;

         Debugger.println(this,"elementNM="+the_elementNM);
         Debugger.println(this,"att="+Debugger.toString(atts));

        if(equalsPath() && the_attrib != null )
        {
            Debugger.println(this,"searching for attribute");
            String val = atts.getValue(the_attrib);
            if(val != null )
                found(val);
        }
    }//----------------------------------------------------------------
   /**
      Parser calls this for each ending element in a document
     */
    public void endElement(String ns, String localNM, String rawNM) throws SAXException
    {

       int li = the_elementNM.lastIndexOf("/");

       if(li <= 0 ) 
          the_elementNM = "";
        else
          the_elementNM = the_elementNM.substring(0,li);

    }//----------------------------------------------------------------
    /**
      @return the result of the XPATH expression 
     */
    public String getResult()
    {
       return the_result;
    }//-------------------------------------------------------------------
    public boolean hasResults()
    {
       return the_result != null;
    }//-------------------------------------------------------------------

    // Parser calls this once after parsing a document
    public void endDocument() throws SAXException {
    }
    public String toString()
    {
       return Debugger.toString(this);
    }//-----------------------------
    private void found(String val)
    {
          the_result = val;
          throw new RuntimeException(val);
    }//----------------------------------------------------------------
    private boolean equalsPath()
    {
       return the_elementNM != null && the_elementNM.equals(the_se);
    }//------------------------------------------------------------------

    // A Hashtable with tag names as keys and Integers as values
    private String the_elementNM = "";
    private String the_result    = null;
    private String the_se = null;
    private static final String ATTRIB_PATH = "@";
    private String the_attrib = null;

}
