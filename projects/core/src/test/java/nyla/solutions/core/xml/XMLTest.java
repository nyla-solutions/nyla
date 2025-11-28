package nyla.solutions.core.xml;

import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class XMLTest
{
    private Document document;
    private String expectedId="hi";
    private String xml = "<hello><word id=\"" + expectedId + "\">HI</word></hello>";
    private XML subject;
    private File file;


    @BeforeEach
    void setUp() throws IOException, ParserConfigurationException, SAXException
    {
        file = Paths.get("target/xml/text.xml").toFile();
        IO.writer().writeFile(file, xml);
        document = XML.toDocument(file);

        subject = new XML(xml);
    }

    @Test
    void toDocumentXML() throws IOException, ParserConfigurationException, SAXException
    {
        assertNotNull(XML.toDocument(xml));
    }

    @Test
    void findNodes()
    {
        String expectedElementName  = "word";

        NodeList actual = subject.findNodes(expectedElementName);
        assertNotNull(actual);
        assertEquals(1,actual.getLength());
    }

    @Test
    void getDocument() throws IOException, SAXException, ParserConfigurationException
    {
        assertEquals(document,new XML(document).getDocument());
    }

    @Test
    void toDocument() throws IOException
    {
        assertNotNull(document);

        String expectedValue = "HI";
        NodeList actual = document.getElementsByTagName("word");

        assertNotNull(actual);
        assertEquals(1,actual.getLength());
    }

    @Test
    void findAttrByRegExp()
    {
        String attributeName = "id";
        String actual = XML.findAttrByRegExp(attributeName,document);
        assertEquals(expectedId,actual);

        actual = subject.findAttrByRegExp(attributeName);
        assertEquals(expectedId,actual);

        actual = XML.findAttrByRegExp("[iI][dD]",document);
        assertEquals(expectedId,actual);

        actual = subject.findAttrByRegExp("[iI][dD]");
        assertEquals(expectedId,actual);
    }

    @Test
    void searchNodesXPath()
    {
        String expression = "//word";
        NodeList actual = XML.searchNodesXPath(expression, document);
        assertEquals(1,actual.getLength());

        actual = subject.searchNodesXPath(expression);
        assertEquals(1,actual.getLength());
    }

    @Test
    void searchNodeTextByXPath()
    {
        String expression = "//word";
        assertEquals("HI",XML.searchNodeTextByXPath(expression, document));
        assertNull(XML.searchNodeTextByXPath("SDSDS", document));
    }

    @Test
    void findElementsByName()
    {
        String elementName = "word";
        Collection<Node> actual = XML.findElementsByName(elementName,document);

        assertNotNull(actual);
        assertEquals(1,actual.size());

        actual = subject.findElementsByName(elementName);

        assertNotNull(actual);
        assertEquals(1,actual.size());
    }

    @Test
    void findElementByName()
    {
        String elementName = "word";
        Node node= XML.findElementByName(elementName,document);
        assertNotNull(node);
        String expected = "HI";
        assertEquals(expected,node.getTextContent());
    }


}