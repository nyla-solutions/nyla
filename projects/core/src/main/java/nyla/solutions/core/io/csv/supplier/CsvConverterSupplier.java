package nyla.solutions.core.io.csv.supplier;

import nyla.solutions.core.io.csv.CsvReader;
import nyla.solutions.core.patterns.conversion.Converter;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author gregory green
 * @param <T> the type to convert to
 */
public class CsvConverterSupplier<T> implements Supplier<T> {
    private final Iterator<List<String>> reader;
    private final Converter<List<String>, T> converter;
    private int skipCnt = 0;
    private boolean notStarted = true;

    public CsvConverterSupplier(Iterator<List<String>> reader, Converter<List<String>, T> converter) {
        this.reader = reader;
        this.converter = converter;
    }
    public CsvConverterSupplier(CsvReader reader, Converter<List<String>, T> converter) {
        this(reader.iterator(),converter);
    }

    @Override
    public T get() {

        skipFirstLines();

        if(!reader.hasNext())
            return null;

        return converter.convert(reader.next());
    }

    private void skipFirstLines() {
        if(notStarted && skipCnt > 0)
        {
            for(int i =0 ; i < skipCnt;i++)
            {
                if(reader.hasNext())
                {
                    reader.next(); //skipped
                }
            }
        }

        notStarted = false;
    }

    public void skipLines(int skipCount) {
        this.skipCnt = skipCount;
    }
}
