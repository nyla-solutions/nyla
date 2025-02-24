package nyla.solutions.core.patterns.creational.generator.json;

import nyla.solutions.core.data.json.JsonPropertySchema;
import nyla.solutions.core.data.json.JsonPropertyType;
import nyla.solutions.core.data.json.JsonSchemaBluePrint;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonSchemaBluePrintTest
{
    @Test
    void addPropertySchema()
    {
        JsonSchemaBluePrint jsonSchemeBluePrint = new JsonSchemaBluePrint(DateTimeFormatter.ISO_LOCAL_DATE);
        String expectedName= "name";
        JsonPropertyType expectType = JsonPropertyType.Boolean;
        jsonSchemeBluePrint.addPropertySchema(new JsonPropertySchema(expectedName,expectType));

        List<JsonPropertySchema> actual = jsonSchemeBluePrint.getPropertySchemas();
        assertNotNull(actual);
        assertEquals(expectedName,actual.iterator().next().getPropertyName());
        assertEquals(expectType,actual.iterator().next().getPropertyType());
    }

    @Test
    void formProperties_string()
    {
        Properties properties = new Properties();
        String expectPropertyName = "name";

        properties.setProperty(expectPropertyName,"String");
        JsonSchemaBluePrint schema = new JsonSchemaBluePrint(
                DateTimeFormatter.ISO_DATE_TIME,
                properties);

        assertEquals(1,schema.getPropertySchemas().size());
        assertEquals(expectPropertyName,schema.getPropertySchemas().iterator().next().getPropertyName());
        assertEquals(JsonPropertyType.String,schema.getPropertySchemas().iterator().next().getPropertyType());
    }

    @Test
    void formProperties_number()
    {
        Properties properties = new Properties();
        String expectPropertyName = "name";
        JsonPropertyType expectedType = JsonPropertyType.Number;

        properties.setProperty(expectPropertyName,expectedType.toString());
        JsonSchemaBluePrint schema = new JsonSchemaBluePrint(
                DateTimeFormatter.ISO_DATE_TIME,
                properties);

        assertEquals(expectPropertyName,schema.getPropertySchemas().iterator().next().getPropertyName());
        assertEquals(expectedType,schema.getPropertySchemas().iterator().next().getPropertyType());
    }
    @Test
    void formProperties_integer()
    {
        Properties properties = new Properties();
        String expectPropertyName = "name";
        JsonPropertyType expectedType = JsonPropertyType.Integer;

        properties.setProperty(expectPropertyName,expectedType.toString());
        JsonSchemaBluePrint schema = new JsonSchemaBluePrint(
                DateTimeFormatter.ISO_DATE_TIME,
                properties);

        assertEquals(expectPropertyName,schema.getPropertySchemas().iterator().next().getPropertyName());
        assertEquals(expectedType,schema.getPropertySchemas().iterator().next().getPropertyType());
    }
    @Test
    void formProperties_boolean()
    {
        Properties properties = new Properties();
        String expectPropertyName = "name";
        JsonPropertyType expectedType = JsonPropertyType.Boolean;

        properties.setProperty(expectPropertyName,expectedType.toString());
        JsonSchemaBluePrint schema = new JsonSchemaBluePrint(
                DateTimeFormatter.ISO_DATE_TIME,
                properties);

        assertEquals(expectPropertyName,schema.getPropertySchemas().iterator().next().getPropertyName());
        assertEquals(expectedType,schema.getPropertySchemas().iterator().next().getPropertyType());
    }
}