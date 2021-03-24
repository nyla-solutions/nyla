package nyla.solutions.core.patterns.creational.generator.json;

import nyla.solutions.core.data.json.JsonPropertySchema;
import nyla.solutions.core.data.json.JsonSchemaBluePrint;
import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.patterns.creational.Creator;

import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * <pre>
 *
 *     Example Usage
 *
 *     {@code
 *         Properties properties = new Properties();
 *         properties.setProperty("firstName","String");
 *         properties.setProperty("lastName","String");
 *         properties.setProperty("updateDate","String");
 *         properties.setProperty("cost","Number");
 *         properties.setProperty("count","Integer");
 *
 *         JsonGeneratorCreator c =
 *                 new JsonGeneratorCreator(DateTimeFormatter.ISO_DATE,
 *                         properties);
 *
 *         String json = c.create();
 *
 *
 *     }
 *
 * Example JSON output
 *  {@code
 *  {"firstName":"Keith","lastName":"Jackson","updateDate":"2020-07-04","cost":0.011549557733887772,"count":119629238}
* }
 *
 * </pre>
 * @author Gregory Green
 */
public class JsonGeneratorCreator implements Creator<String>
{
    private final JsonSchemaBluePrint jsonSchemeBluePrint;
    private final Converter<JsonPropertySchema,String> converter;

    public JsonGeneratorCreator(JsonSchemaBluePrint jsonSchemeBluePrint)
    {
        converter = new JsonPropertySchemaToJsonPropertyConverter(jsonSchemeBluePrint.getDateTimeFormatter());
        this.jsonSchemeBluePrint = jsonSchemeBluePrint;
    }

    public JsonGeneratorCreator(DateTimeFormatter dateTimeFormatter, Properties properties)
    {
        this(new JsonSchemaBluePrint(dateTimeFormatter,properties));
    }

    @Override
    public String create()
    {
        StringBuilder json = new StringBuilder();

        jsonSchemeBluePrint
                .getPropertySchemas()
                .forEach(
                        propertySchema ->
                        {
                            if (json.length() > 0)
                                json.append(",");
                            else
                                json.append("{");

                            json.append(
                                    converter.convert(propertySchema));
                        }
                );

        json.append("}");

        return json.toString();
    }//-------------------------------------------

}
