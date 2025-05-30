package nyla.solutions.core.patterns.creational.generator.json;

import nyla.solutions.core.data.json.JsonPropertySchema;
import nyla.solutions.core.data.json.JsonPropertyType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonPropertySchemaTest
{

    @Test
    void constructor()
    {
        assertEquals("test",new JsonPropertySchema("test",null).getPropertyName());
        assertEquals(JsonPropertyType.String,new JsonPropertySchema("test",JsonPropertyType.String).getPropertyType());
        assertEquals(JsonPropertyType.Number,new JsonPropertySchema("test",JsonPropertyType.Number).getPropertyType());
    }
}