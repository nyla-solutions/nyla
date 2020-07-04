package nyla.solutions.core.data.json;

/**
 * @author Gregory Green
 */
public class JsonPropertySchema
{
    private final String propertyName;
    private final JsonPropertyType propertyType;

    public JsonPropertySchema(String propertyName, JsonPropertyType propertyType)
    {
        this.propertyName = propertyName;
        this.propertyType = propertyType;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public JsonPropertyType getPropertyType()
    {
        return propertyType;
    }
}
