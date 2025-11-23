package nyla.solutions.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * IO File Reader utility
 * @author Gregory Green
 */
public class IoReader {

    /**
     *
     * @param filePath the file path
     * @return the file content
     * @throws IOException when IO exceptions occur
     */
    public String readTextFile( Path filePath) throws IOException {
        if(filePath == null)
            return null;

        return Files.readString(filePath);
    }

    /**
     * Read text lines
     * @param path the file path
     * @return the lines of text
     * @throws IOException when an error occurs
     */
    public List<String> readTextLines(Path path) throws IOException {

        return Files.readAllLines(path);
    }

    /**
     * Read text from input stream
     * @param inputStream the input stream
     * @return the text
     * @throws IOException when an IO error occurs
     */
    public String readText(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes());
    }

    /**
     * @param filePath the file name/path
     * @return Map version of property file
     * @throws IOException           when an IO error occurs
     * @throws FileNotFoundException when the file does not exist
     */
    @SuppressWarnings("rawtypes")
    public  Map readMap(Path filePath)
            throws IOException, FileNotFoundException
    {

        Properties prop = new Properties();
        prop.load(Files.newInputStream(filePath));

        return prop;
    }

    /**
     * Read text from input stream
     * @param inputStream the input stream
     * @return the text
     * @throws IOException when an IO error occurs
     */
    public String readTextInputStream(InputStream inputStream)
            throws IOException
    {
       return new String(inputStream.readAllBytes());
    }


    /**
     * Read the properties file
     *
     * @param filePath the file to read
     * @return the properties
     * @throws IOException when an unknown IO error occurs
     */
    public Properties readProperties(String filePath)
            throws IOException
    {
        Properties properties = new Properties();

        try(InputStream reader = Files.newInputStream(Paths.get(filePath)))
        {
            properties.load(reader);
            return properties;
        }
    }

    /**
     * Read text file
     * @param path the file path
     * @return the file content
     */
    public String readTextFile(String path) throws IOException {
        if(path == null)
            return null;
        return readTextFile(Paths.get(path));
    }


    /**
     * Retrieve the contents of specified file. Retry reads the specified number of
     * times with the specified delay when errors occur
     *
     * @param aFile         the file
     * @param aRetryCount   number of times to retry read
     * @param aRetryDelayMS delay in between read failures
     * @return file byte content
     * @throws IOException when an unknown IO error occurs
     */
    public byte[] readBinaryFile(Path aFile, int aRetryCount, long aRetryDelayMS)
            throws IOException
    {
        for (int i = 0; i <= aRetryCount; i++)
        {
            try
            {
                return readBinaryFile(aFile);
            }
            catch (Exception e)
            {
                try
                {
                    Thread.sleep(aRetryDelayMS);
                }
                catch (Exception interruptE)
                {
                }
            }
        }
        throw new IOException(aFile.toFile().getAbsolutePath());
    }

    /**
     * Read the binary file
     * @param filePath the file path
     * @return the bytes of the file
     * @throws FileNotFoundException when the file is not found
     * @throws IOException           when an unknown IO error occurs
     */
    public byte[] readBinaryFile(String filePath)
            throws FileNotFoundException, IOException
    {
        return readBinaryFile(Paths.get(filePath));
    }

    /**
     * @param file the file to read
     * @return binary file content
     * @throws FileNotFoundException when file not found
     * @throws IOException           and IO exception occurs
     */
    public byte[] readBinaryFile(Path file)
            throws FileNotFoundException, IOException
    {
        return Files.readAllBytes(file);
    }

    /**
     * Retrieve contents of specified file. Retry reads the specified number of
     * times with the specified delay when errors occur
     *
     * @param filePath     the file path
     * @param aRetryCount   number of times to retry read
     * @param aRetryDelayMS delay in between read failures
     * @return file string content
     * @throws IOException when an IO error occurs
     */
    public String readFile(Path filePath, int aRetryCount, long aRetryDelayMS)
            throws IOException
    {
        for (int i = 0; i <= aRetryCount; i++)
        {
            try
            {
                return readTextFile(filePath);
            }
            catch (Exception e)
            {
                try
                {
                    Thread.sleep(aRetryDelayMS);
                }
                catch (Exception ignored)
                {
                }
            }
        }
        throw new IOException(String.valueOf(filePath));
    }
}
