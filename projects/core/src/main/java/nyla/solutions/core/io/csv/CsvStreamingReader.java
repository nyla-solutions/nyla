package nyla.solutions.core.io.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

/**
 * CSV reader that is iterable and can be used in a for-each loop.
 * This object to gear toward larger files.
 *
 * This class will read the CSV file line by line and return each row as a list of strings.
 * @author Gregory Green
 */
public class CsvStreamingReader implements Iterable<List<String>>, Iterator<List<String>>{
    private final BufferedReader bufferedReader;
    private String currentLine;
    private String nextLine;

    /**
     * Constructor for CsvReaderIterable.
     *  @param reader the reader to read the CSV file from
     * @param skipHeader whether to skip the first line of the CSV file (header)
     */
    public CsvStreamingReader(Reader reader, boolean skipHeader) {
        this.bufferedReader = new BufferedReader(reader);

        try {
            this.currentLine = bufferedReader.readLine();
            if(skipHeader)
                this.currentLine = bufferedReader.readLine();

            this.nextLine = bufferedReader.readLine();

        } catch (IOException e) {
            this.nextLine = null;
        }

    }


    @Override
    public Iterator<List<String>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {

        return this.currentLine != null || this.nextLine != null;
    }

    /**
     *
     * @return the next row of the CSV file as a list of strings
     */
    @Override
    public List<String> next() {
        var row = CsvReader.parse(this.currentLine);

        if (nextLine != null) {

            this.currentLine = nextLine;

            try {
                this.nextLine = bufferedReader.readLine();
            } catch (IOException e) {
                this.nextLine = null;
                this.currentLine = null;
            }
        }
        else {
            //there is no next line, so we are done
            currentLine = null;
        }
        return row;
    }

    /**
     * Implements the builder pattern for creating instances of CsvReaderIterable.
     *
     */
    public static class Builder {
        private Reader reader;
        private boolean skipHeader=false;

        Builder(){
        }

         public Builder reader(Reader reader) {
             this.reader = reader;
            return this;
        }


        public Builder skipHeader(boolean skipHeader) {
             this.skipHeader = skipHeader;
            return this;
        }

        public CsvStreamingReader build() {
            return new CsvStreamingReader(reader,skipHeader);
        }
    }
}
