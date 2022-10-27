package nyla.solutions.core.io.csv;

import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.io.csv.formulas.CsvFormula;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

/**
 * CSV reader utility class
 *
 * @author Gregory Green
 */
public class CsvReader implements Iterable<List<String>>
{
    public static enum DataType
    {String, Long};

    //private final File file;
    private final ArrayList<List<String>> data;

    public CsvSelectBuilder selectBuilder() {
        return new CsvSelectBuilder(this);
    }

    protected List<List<String>> getData() {
        return (List)this.data;
    }



    /**
     * Read based on a reader
     *
     * @param reader the input reader
     * @throws IOException when a reading error occurs
     */
    public CsvReader(Reader reader)
    throws IOException
    {
        data = new ArrayList<List<String>>(10);

        try (BufferedReader r = new BufferedReader(reader))
        {
            String line = null;
            while ((line = r.readLine()) != null)
            {
                this.data.add(parse(line));
            }
        }
    }

    /**
     * Constructor for a file
     *
     * @param file the file input
     * @throws IOException
     */
    public CsvReader(File file)
    throws IOException
    {
        if (file == null)
            throw new IllegalArgumentException("file is required");

        if (!file.exists())
        {
            throw new IllegalArgumentException("File:" + file.getAbsolutePath() + " does not exist");
        }

        String line = null;
        data = new ArrayList<List<String>>();
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8))
        {
            while ((line = reader.readLine()) != null)
            {
                this.data.add(parse(line));
            }
        }
    }//------------------------------------------------

    public Stream<List<String>> stream()
    {
        return data.stream();
    }
    /**
     * Data typing enum
     */
    @Override
    public Iterator<List<String>> iterator()
    {

		return data.listIterator();
	}


    /**
     *
     * @param row row starting at 0
     * @param col cole starting at 0
     * @param dataType The Data type to return
     * @return the value cast to data type
     * @param <T> the Value data type
     */
    public <T> T get(int row, int col, DataType dataType)
    {
        List<String> rowList = row(row);

        if (rowList == null || rowList.isEmpty() || col >= rowList.size())
            return nullFor(dataType);


        String cell = rowList.get(col);


        T results = toType(cell, dataType);
        return results;
    }//------------------------------------------------

    @SuppressWarnings("unchecked")
    private <T> T nullFor(DataType dataType)
    {
        switch (dataType)
        {
            case Long:
                return (T) Long.valueOf(-1);
            default:
                return null;
        }
    }//------------------------------------------------

    @SuppressWarnings("unchecked")
    private <T> T toType(String cell, DataType dataType)
    {

        switch (dataType)
        {
            case Long:
                return cell != null && cell.length() > 0 ? (T) Long.valueOf(cell) : (T) Long.valueOf(-1);
            default:
                return (T) cell;
        }
    }//------------------------------------------------

    /**
     * @return this.data.isEmpty()
     */
    public boolean isEmpty()
    {
        return this.data.isEmpty();
    }

    //------------------------------------------------
    public void calc(CsvFormula csvFormula)
    {
        csvFormula.calc(this);
    }

    public static List<String> parse(String line)
    {
        if (line == null || line.length() == 0)
            return null;

        final short START = 2;
        final short TERM = 3;
        final short QUOTED_TERM = 4;
        final short start_ESCAPEDDOUBLEQUOTE = 5;

        short state = START;

        int length = line.length();
        ArrayList<String> tokens = new ArrayList<>(10);

        StringBuilder buffer = new StringBuilder();

        char currentChar;
        try
        {
            for (int i = 0; i < length; i++)
            {
                currentChar = line.charAt(i);

                switch (state)
                {
                    case START:
                        if (currentChar == '"')
                        {
                            state = QUOTED_TERM;
                        }
                        else if (currentChar != ',')
                        {
                            state = TERM;
                            buffer.append(currentChar);
                        }

                        break;
                    case QUOTED_TERM:
                        if (currentChar != '"')
                        {
                            buffer.append(currentChar);
                        }
                        else
                        {
                            if (line.length() > (i + 1) && line.charAt(i + 1) != '"')
                            {
                                state = START;
                                tokens.add(buffer.toString());
                                buffer.setLength(0);
                            }
                            else if (line.length() > (i + 1) && line.charAt(i + 1) == '"')
                            {
                                state = start_ESCAPEDDOUBLEQUOTE;
                            }
                        }
                        break;
                    case start_ESCAPEDDOUBLEQUOTE:

                        buffer.append(currentChar);

                        state = QUOTED_TERM;
                        break;
                    case TERM:
                        if (currentChar == ',')
                        {
                            state = START;

                            if (buffer.length() > 0)
                            {
                                tokens.add(buffer.toString());
                                buffer.setLength(0);
                            }

                        }
                        else if (currentChar == '"' && line.charAt(i + 1) == '"')
                        {
                            state = start_ESCAPEDDOUBLEQUOTE;
                        }
                        else
                            buffer.append(line.charAt(i));
                        break;
                    default:
                        throw new RuntimeException("Unknown parse state:" + state);
                }
            }
        }
        catch (StringIndexOutOfBoundsException e)
        {
            throw new FormatException("ERROR:"+e+" line:" + line);
        }


        if (buffer.length() > 0)
            tokens.add(buffer.toString());

        tokens.trimToSize();

        if (tokens.isEmpty())
            return null;

        return tokens;

    }//------------------------------------------------

    /**
     * @param rowNumber the row number to get
     * @return get the row (start at zero)
     */
    public List<String> row(int rowNumber)
    {
        List<String> row = data.get(rowNumber);

        if (row == null)
            return null;

        //return a copy
        return new ArrayList<String>(row);
    }//------------------------------------------------

    public void sortRowsForIndexByType(int index, DataType dataType)
    {
        Comparator<List<String>> comparator = null;

        switch (dataType)
        {

            case Long:
            {
                comparator = (list1, list2) ->
                        Long.valueOf(list1.get(0)).compareTo(Long.valueOf(list2.get(0)));
            }
            break;
            default:
                comparator = (list1, list2) ->
                        list1.get(0).compareTo(list2.get(0));
        }

        sortRows(comparator);
    }//------------------------------------------------


    /**
     * Sort the rows by comparator
     *
     * @param comparator the comparator used for sorting
     */
    public void sortRows(Comparator<List<String>> comparator)
    {
        if (data.isEmpty())
            return;

        Collections.sort(data, comparator);
    }//------------------------------------------------


    public int size()
    {
        return data.size();
    }
}
