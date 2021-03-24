package nyla.solutions.core.patterns.creational.generator.json;

import nyla.solutions.core.data.json.JsonPropertySchema;
import nyla.solutions.core.data.json.JsonPropertyType;
import nyla.solutions.core.data.json.JsonSchemaBluePrint;
import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class JsonGeneratorCreatorTest
{
    @Test
    public void test_generator()
    {
        JsonSchemaBluePrint jsonSchemeBluePrint = new JsonSchemaBluePrint(DateTimeFormatter.ISO_DATE);
        String expectedPropertyName = "name";
        JsonPropertyType expectedPropertyType = JsonPropertyType.String;
        jsonSchemeBluePrint.addPropertySchema(new JsonPropertySchema(expectedPropertyName, expectedPropertyType));
        JsonGeneratorCreator c =
                new JsonGeneratorCreator(jsonSchemeBluePrint);

        String json = c.create();

        System.out.println(json);
        assertNotNull(json);
        assertTrue(json.contains(expectedPropertyName));
        assertEquals(4, Text.characterCount('"', json));

    }

    @Test
    public void test_boolean()
    {
        JsonSchemaBluePrint jsonSchemeBluePrint = new JsonSchemaBluePrint(DateTimeFormatter.ISO_DATE);

        jsonSchemeBluePrint.addPropertySchema(new JsonPropertySchema("nyla", JsonPropertyType.Boolean));
        JsonGeneratorCreator c =
                new JsonGeneratorCreator(jsonSchemeBluePrint);

        String json = c.create();

        System.out.println(json);
        assertNotNull(json);
        assertEquals(2, Text.characterCount('"', json));

        assertTrue(json.matches("..*(true|false).*"));
    }


    @Test
    public void test_multiple_properties()
    {

        JsonSchemaBluePrint jsonSchemaBluePrint = new JsonSchemaBluePrint(DateTimeFormatter.ISO_DATE);

        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("isTrue", JsonPropertyType.Boolean));
        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("cost", JsonPropertyType.Number));
        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("count", JsonPropertyType.Integer));
        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("name", JsonPropertyType.String));
        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("date", JsonPropertyType.String));
        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("firstName", JsonPropertyType.String));
        jsonSchemaBluePrint.addPropertySchema(new JsonPropertySchema("lastName", JsonPropertyType.String));

        JsonGeneratorCreator c =
                new JsonGeneratorCreator(jsonSchemaBluePrint);

        String json = c.create();

        System.out.println(json);
        assertNotNull(json);
        assertEquals(22, Text.characterCount('"', json));

        assertTrue(json.matches("..*(true|false).*"));
    }

    @Test
    void fromProperties()
    {
        Properties properties = new Properties();
        properties.setProperty("firstName","String");
        properties.setProperty("lastName","String");
        properties.setProperty("updateDate","String");
        properties.setProperty("cost","Number");
        properties.setProperty("count","Integer");

        JsonGeneratorCreator c =
                new JsonGeneratorCreator(DateTimeFormatter.ISO_DATE,
                        properties);

        String json = c.create();

        System.out.println(json);

       properties.forEach((k,v) -> {
           assertTrue(json.contains((String)k));
       });

    }
}