package nyla.solutions.core.patterns.creational.generator.json;

import nyla.solutions.core.data.json.JsonPropertySchema;
import nyla.solutions.core.data.json.JsonPropertyType;
import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class JsonPropertySchemaToJsonPropertyConverterTest
{

    @Test
    void convert()
    {
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        JsonPropertySchemaToJsonPropertyConverter subject = new JsonPropertySchemaToJsonPropertyConverter(dateTimeformatter);
        assertNull(subject.convert(null));
        assertNotNull(subject.convert(new JsonPropertySchema("test", JsonPropertyType.String)));
        assertNotNull(subject.convert(new JsonPropertySchema("", JsonPropertyType.String)));
        assertNotNull(subject.convert(new JsonPropertySchema(null, JsonPropertyType.String)));
        assertNotNull(subject.convert(new JsonPropertySchema("",null)));
    }
    @Test
    void convert_boolean()
    {
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        JsonPropertySchemaToJsonPropertyConverter subject = new JsonPropertySchemaToJsonPropertyConverter(dateTimeformatter);
        assertNull(subject.convert(null));
        String value = subject.convert(new JsonPropertySchema("test", JsonPropertyType.Boolean));
        assertTrue(value.contains("true")|| value.contains("false"));
    }

    @Test
    void convert_number()
    {
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        JsonPropertySchemaToJsonPropertyConverter subject = new JsonPropertySchemaToJsonPropertyConverter(dateTimeformatter);
        assertNull(subject.convert(null));
        String value = subject.convert(new JsonPropertySchema("test", JsonPropertyType.Number));
        assertEquals(2, Text.characterCount('"',value));
    }
    @Test
    void convert_integer()
    {
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        JsonPropertySchemaToJsonPropertyConverter subject = new JsonPropertySchemaToJsonPropertyConverter(dateTimeformatter);
        assertNull(subject.convert(null));
        String value = subject.convert(new JsonPropertySchema("test", JsonPropertyType.Integer));
        assertEquals(2, Text.characterCount('"',value));
    }

}