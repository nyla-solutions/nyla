package nyla.solutions.core.data.json;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Gregory Green
 */
public class JsonSchemaBluePrint
{
    private ArrayList<JsonPropertySchema> propertySchemas = new ArrayList<>(10);
    private final DateTimeFormatter dateTimeFormatter;

    public JsonSchemaBluePrint(DateTimeFormatter dateTimeFormatter)
    {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public JsonSchemaBluePrint(DateTimeFormatter dateTimeFormatter, Properties properties)
    {
        this(dateTimeFormatter);

        properties.forEach((k,p) -> {
            this.addPropertySchema(new JsonPropertySchema(
                    (String)k,
                    JsonPropertyType.valueOf((String)p)));
        });
    }

    public void addPropertySchema(JsonPropertySchema jsonPropertySchema)
    {
        propertySchemas.add(jsonPropertySchema);
    }

    public List<JsonPropertySchema> getPropertySchemas()
    {
        propertySchemas.trimToSize();
        return propertySchemas;
    }

    public DateTimeFormatter getDateTimeFormatter()
    {
        return dateTimeFormatter;
    }
}
