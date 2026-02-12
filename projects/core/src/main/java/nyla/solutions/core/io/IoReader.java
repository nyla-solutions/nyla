package nyla.solutions.core.io;

import nyla.solutions.core.exception.IoException;
import nyla.solutions.core.exception.MissingFileException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

    protected IoReader(){}

    /**
     *
     * @param filePath the file path
     * @return the file content
     */
    public String readTextFile( Path filePath)  {
        if(filePath == null)
            return null;

        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    /**
     * Read text lines
     * @param path the file path
     * @return the lines of text
     */
    public List<String> readTextLines(Path path) {

        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    /**
     * Read text from input stream
     * @param inputStream the input stream
     * @return the text
     */
    public String readText(InputStream inputStream)  {
        try {
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    /**
     * @param filePath the file name/path
     * @return Map version of property file
     */
    @SuppressWarnings("rawtypes")
    public  Map readMap(Path filePath)
    {

        Properties prop = new Properties();
        try {
            prop.load(Files.newInputStream(filePath));
        } catch (IOException e) {
            throw new IoException(e);
        }

        return prop;
    }

    /**
     * Read text from input stream
     * @param inputStream the input stream
     * @return the text
     */
    public String readTextInputStream(InputStream inputStream)
    {
        try {
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    public String readText(BufferedReader bufferedReader)
    {
        if(bufferedReader == null)
            return null;

        String text;
        StringBuilder builder = new StringBuilder();
        try{
            while ((text = bufferedReader.readLine()) != null)
            {
                builder.append(text).append(IO.newline());
            }

            return builder.toString();
        }
        catch (IOException e)
        {
            throw new IoException(e);
        }
    }
    /**
     * Read the properties file
     *
     * @param filePath the file to read
     * @return the properties
     */
    public Properties readProperties(String filePath)
    {
        Properties properties = new Properties();

        try(InputStream reader = Files.newInputStream(Paths.get(filePath)))
        {
            properties.load(reader);
            return properties;
        }
        catch(IOException e)
        {
            throw new IoException(e);
        }
    }

    /**
     * Read text file
     * @param path the file path
     * @return the file content
     */
    public String readTextFile(String path) {
        if(path == null)
            return null;
        return readTextFile(Paths.get(path));
    }

    /**
     * @param aFilePath the file path
     * @return file input stream
     * @throws FileNotFoundException when the file is not found
     */
    public InputStream getFileInputStream(String aFilePath)
            throws FileNotFoundException
    {
        return new FileInputStream(aFilePath);
    }


    /**
     * @param aReader the input reader
     */
    protected String readFully(Reader aReader)
    {
        if (aReader == null)
            throw new IllegalArgumentException("aReader required in IO.readFully");

        try {
            BufferedReader buffreader = new BufferedReader(aReader);
            String tmp = buffreader.readLine();

            if (tmp == null || tmp.length() == 0)
                return null;

            StringBuffer line = new StringBuffer(tmp);

            while (tmp != null) {

                tmp = buffreader.readLine();

                if (tmp != null)

                    line.append("\n").append(tmp);
            }

            return line.toString();
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    /**
     * @param urlAddress the URL to read form
     * @return the URL text content
     */
    public String readURL(String urlAddress)
    {
        if (urlAddress == null)
            throw new RequiredException("url in IO.readURL");

        Reader reader = null;
        try
        {

            URL url = new URL(urlAddress);

            URLConnection connection = url.openConnection();

            reader = toReader(connection.getInputStream());

            return readFully(reader);
        }
        catch (MalformedURLException e)
        {
            throw new SystemException("URL=" + urlAddress + " " + e);
        }
        catch(IOException e)
        {
            throw new IoException(e);
        }
        finally
        {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new IoException(e);
                }
            }
        }

    }

    /**
     * Return the length of a given file
     *
     * @param aFilePath the location of the file
     * @return the file length
     * @throws IllegalArgumentException
     */
    public long getFileSize(String aFilePath)
            throws IllegalArgumentException
    {
        if (aFilePath == null)
            throw new IllegalArgumentException("Cannot obtain file size, File Path not provided");

        return new File(aFilePath).length();
    }
    /**
     * @param aInputStream
     * @return the inputStream Reader
     */
    public Reader toReader(InputStream aInputStream)
    {
        return new java.io.InputStreamReader(aInputStream, IO.CHARSET);

    }


    /**
     * Retrieve the contents of specified file. Retry reads the specified number of
     * times with the specified delay when errors occur
     *
     * @param aFile         the file
     * @param aRetryCount   number of times to retry read
     * @param aRetryDelayMS delay in between read failures
     * @return file byte content
     */
    public byte[] readBinaryFile(Path aFile, int aRetryCount, long aRetryDelayMS)
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
        throw new IoException(aFile.toFile().getAbsolutePath());
    }

    /**
     * Read the binary file
     * @param filePath the file path
     * @return the bytes of the file
     * @throws FileNotFoundException when the file is not found
     */
    public byte[] readBinaryFile(String filePath)
    {
        return readBinaryFile(Paths.get(filePath));
    }

    /**
     * @param file the file to read
     * @return binary file content
     * @throws FileNotFoundException when file not found
     */
    public byte[] readBinaryFile(Path file)
    {
        try {
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new IoException(e);
        }
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


    private byte[] readBinary(InputStream inputStream)
            throws IOException
    {
        if (inputStream == null)
            throw new IllegalArgumentException("inputStream is required");

        byte[] bytes = new byte[IO.FILE_IO_BATCH_SIZE];

        ByteArrayOutputStream ba = new ByteArrayOutputStream(IO.FILE_IO_BATCH_SIZE);
        int len;
        while ((len = inputStream.read(bytes)) > 0)
        {
            ba.write(bytes, 0, len);
        }

        return ba.toByteArray();
    }

    /**
     * Read Class Path resource
     *
     * @param path the classpath location i.e. /properties/config.properties
     * @return the string content from the class location
     * @throws IOException when IO error occurs
     */
    public String readClassPath(String path)
    {

        ClassLoader classLoader = getDefaultClassLoader();

        InputStream is;

        if (classLoader != null)
            is = classLoader.getResourceAsStream(path);
        else
            is = ClassLoader.getSystemResourceAsStream(path);
        if (is == null)
            throw new MissingFileException(
                    (new StringBuilder()).append(path).append(" cannot be opened because it does not exist").toString());

        return readFully(new InputStreamReader(is, IO.CHARSET));
    }

    public byte[] readBinaryClassPath(String path)
            throws IOException
    {
        ClassLoader classLoader = getDefaultClassLoader();

        InputStream is;

        if (classLoader != null)
            is = classLoader.getResourceAsStream(path);
        else
            is = ClassLoader.getSystemResourceAsStream(path);
        if (is == null)
            throw new FileNotFoundException(
                    (new StringBuilder()).append(path).append(" cannot be opened because it does not exist").toString());


        return readBinary(is);
    }

    public ClassLoader getDefaultClassLoader()
    {
        ClassLoader cl = null;
        try
        {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable throwable)
        {
        }
        if (cl == null)
        {
            cl = IO.class.getClassLoader();
            if (cl == null)
                try
                {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable throwable1)
                {
                }
        }
        return cl;
    }

}
