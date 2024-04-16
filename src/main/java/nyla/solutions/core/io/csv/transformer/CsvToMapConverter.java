package nyla.solutions.core.io.csv.transformer;

import nyla.solutions.core.io.csv.CsvReader;
import nyla.solutions.core.patterns.conversion.Converter;

import java.util.HashMap;
import java.util.Map;

public class CsvToMapConverter implements Converter<String, Map<?,?>> {
    private final String[] headers;
    public CsvToMapConverter(String... headers) {
        this.headers = headers;
    }

    @Override
    public Map<?, ?> convert(String csv) {
        var list = CsvReader.parse(csv);
        if(list == null || list.isEmpty())
            return null;

        Map<Object,Object> map = new HashMap<>(headers.length);

        final var listSize = list.size();

        for (int i = 0; i < headers.length; i++) {

            map.put(headers[i],i < listSize ? list.get(i): null);
        }
        return map;
    }
}
