package nyla.solutions.core.xml;

import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class XMLTest
{
    private Document document;
    private String expectedId="hi";;

    @BeforeEach
    void setUp() throws IOException
    {
        File file = Paths.get("target/xml/text.xml").toFile();
        String xml = "<hello><word id=\""+expectedId+"\">HI</word></hello>";
        IO.writeFile(file,xml);
        document = XML.toDocument(file);
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

        actual = XML.findAttrByRegExp("[iI][dD]",document);
        assertEquals(expectedId,actual);
    }

    @Test
    void searchNodesXPath()
    {
        String expression = "//word";
        NodeList actual = XML.searchNodesXPath(expression, document);
        assertEquals(1,actual.getLength());
    }

    @Test
    void findElementsByName()
    {
        String elementName = "word";
        Collection<Node> actual = XML.findElementsByName(elementName,document);

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