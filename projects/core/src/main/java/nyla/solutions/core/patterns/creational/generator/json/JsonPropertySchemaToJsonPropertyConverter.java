package nyla.solutions.core.patterns.creational.generator.json;

import nyla.solutions.core.data.json.JsonPropertySchema;
import nyla.solutions.core.data.json.JsonPropertyType;
import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.patterns.creational.generator.CreatorFactoryByPropertyName;
import nyla.solutions.core.util.Digits;

import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * @author Gregory Green
 */
public class JsonPropertySchemaToJsonPropertyConverter
        implements Converter<JsonPropertySchema, String>
{
    private Random random = new Random(System.currentTimeMillis());
    private final CreatorFactoryByPropertyName factory;

    public JsonPropertySchemaToJsonPropertyConverter(DateTimeFormatter dateTimeformatter)
    {
        this.factory = new CreatorFactoryByPropertyName(dateTimeformatter);
    }

    @Override
    public String convert(JsonPropertySchema jsonPropertySchema)
    {
        if (jsonPropertySchema == null)
            return null;

        return new StringBuilder()
                .append(formatPropertyName(jsonPropertySchema.getPropertyName()))
                .append(":")
                .append(
                        generateForTypeWithName(jsonPropertySchema.getPropertyType(),
                                jsonPropertySchema.getPropertyName()))
                .toString();
    }

    private String generateForTypeWithName(JsonPropertyType propertyType,
                                           String propertName)
    {
        if(propertyType == null)
            return "\"\"";

        switch (propertyType)
        {
            case Boolean:
                return String.valueOf(random.nextBoolean());
            case Integer:
                return String.valueOf(new Digits().generateInteger());
            case Number:
                return String.valueOf(random.nextDouble());
            default:
                return new StringBuilder()
                .append("\"")
                .append(factory.forProperty(propertName).create())
                .append("\"").toString();
        }
    }

    private String formatPropertyName(String propertyName)
    {
        return new StringBuilder().append("\"")
                .append(propertyName)
                .append("\"").toString();
    }
}
