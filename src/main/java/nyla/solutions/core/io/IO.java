package nyla.solutions.core.io;

import nyla.solutions.core.data.Data;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static nyla.solutions.core.util.Config.settings;

/**
 * <pre>
 *  IO provides a set function related to system IO reads/writes.
 *
 *  This object is not thread safe
 *  &#064;see SynchronizedIO
 *
 * </pre>
 *
 * @author Gregory Green
 * @version 1.0
 */
public class IO
{
    /**
     * CHARSET_NM = "UTF-8"
     */
    public static final String CHARSET_NM = settings().getProperty(IO.class, "CHARSET", "UTF-8");

    /**
     * CHARSET = Charset.forName("UTF-8")
     */
    public static final Charset CHARSET = Charset.forName(CHARSET_NM);

    /**
     * File name cannot have characters certain special characters
     */
    public static final String DEFAULT_FILE_NM_INVALID_CHARACTERS_RE = "\\/|\\\\|:|\"|\\*|\\?|<|>|\\|";

    /**
     * Property "byte.buffer.size"
     *
     * @see IO
     */
    public static final String BYTE_BUFFER_SIZE_PROP = "byte.buffer.size";
    /**
     * 1024
     */
    public static final int FILE_IO_BATCH_SIZE = 1024;

    /**
     * NEWLINE = System.getProperty("line.separator")
     */
    public static final String NEWLINE = System.getProperty("line.separator");

    public static synchronized Date touch(File file)
    throws IOException
    {
        return touch(file, Calendar.getInstance().getTime());
    }//// --------------------------------------------------------------

    public static synchronized Date touch(File file, Date date)
    throws IOException
    {
        if (date == null)
            date = Calendar.getInstance().getTime();

        if (!file.exists())
        {
            //create file
            try (FileOutputStream fs = new FileOutputStream(file))
            {
            }

        }
        else if (!file.setLastModified(date.getTime()))
        {
            throw new IOException("Could not update time");
        }


        return date;
    }//------------------------------------------------

    /**
     * @param filePaths the list of the files paths
     * @param pattern   the search pattern (ex: *.log)
     * @return list of matching files
     */
    public static List<File> find(Collection<String> filePaths, String pattern)
    {
        if (filePaths == null)
            return null;

        ArrayList<File> results = new ArrayList<File>(filePaths.size());

        File file;

        WildCardFilter filter = new WildCardFilter(pattern);
        List<File> nested;
        for (String filePath : filePaths)
        {
            file = Paths.get(filePath).toFile();

            nested = find(file, filter);

            if (nested != null && !nested.isEmpty())
                results.addAll(nested);

        }

        if (results.isEmpty())
            return null;

        results.trimToSize();

        return results;

    }// ------------------------------------------------

    public static List<File> find(File file, WildCardFilter filter)
    {
        File[] fileArray;

        if (!file.exists())
            return null;

        ArrayList<File> results = new ArrayList<File>();

        if (file.isDirectory())
        {

            fileArray = file.listFiles();

            if (fileArray == null || fileArray.length == 0)
                return null;

            List<File> nesultedResults;
            for (File nestedFile : fileArray)
            {
                nesultedResults = find(nestedFile, filter);

                if (nesultedResults != null)
                {
                    results.addAll(nesultedResults);
                }
            }
        }
        else if (filter.accept(file, file.getName()))
        {
            results.add(file);
        }

        if (results.isEmpty())
            return null;

        return results;
    }

    /**
     * @param text     the search text
     * @param logFiles the list of files
     * @return the grep matches across all files
     * @throws IOException when an error occurs
     */
    public static Map<File, Collection<String>> grep(String text, List<File> logFiles)
    throws IOException
    {
        if (logFiles == null || logFiles.isEmpty())
            return null;

        HashMap<File, Collection<String>> map = new HashMap<File, Collection<String>>();

        for (File file : logFiles)
        {

            if (!file.exists())
            {
                throw new IllegalArgumentException("File:" + file.getAbsolutePath() + " does not exist");
            }

            String line;
            ArrayList<String> matches = new ArrayList<String>();

            try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8))
            {
                while ((line = reader.readLine()) != null)
                {
                    if (line.contains(text))
                        matches.add(line);
                }

                if (!matches.isEmpty())
                    map.put(file, matches);
            }
        }

        if (map.isEmpty())
            return null;

        return map;
    }// ------------------------------------------------

    /**
     * @return the System.getProperty("file.separator");
     */
    public static String fileSperator()
    {
        return System.getProperty("file.separator");
    }// --------------------------------------------------------

    public static String readText(BufferedReader bufferedReader)
    throws IOException
    {
        if(bufferedReader == null)
            return null;

        String text;
        StringBuilder builder = new StringBuilder();
        while ((text = bufferedReader.readLine()) != null)
        {
            builder.append(text).append(IO.newline());
        }

        return builder.toString();
    }

    /**
     * Write obj to file
     *
     * @param obj
     * @param file
     */
    public static void serializeToFile(Object obj, File file)
    {
        ObjectOutputStream stream = null;
        try
        {
            stream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

            stream.writeObject(obj);
        }
        catch (Exception e)
        {
            throw new SystemException(Debugger.stackTrace(e));
        }
        finally
        {
            if (stream != null)
                try
                {
                    stream.close();
                }
                catch (Exception e)
                {
                }

        }
    }// ----------------------------------------------

    /**
     * Convert the object to binary
     *
     * @param obj the object to write
     * @return the serialized bytes of the object
     */
    public static byte[] serializeToBytes(Object obj)
    {
        ObjectOutputStream stream = null;
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            stream = new ObjectOutputStream(new BufferedOutputStream(out));

            stream.writeObject(obj);
            stream.flush();

            return out.toByteArray();
        }
        catch (Exception e)
        {
            throw new SystemException(Debugger.stackTrace(e));
        }
        finally
        {
            if (stream != null)
                try
                {
                    stream.close();
                }
                catch (Exception e)
                {
                    Debugger.printWarn(e);
                }

        }
    }// ----------------------------------------------

    /**
     * Read object from file
     *
     * @param file the file that has the object information
     * @return the UN-serialized object
     */
    public static Object deserialize(File file)
    {
        ObjectInputStream stream = null;
        try
        {
            stream = new ObjectInputStream(new FileInputStream(file));
            return stream.readObject();
        }
        catch (Exception e)
        {
            throw new SystemException(Debugger.stackTrace(e));
        }
        finally
        {
            if (stream != null)
                try
                {
                    stream.close();
                }
                catch (Exception e)
                {
                    Debugger.printWarn(e);
                }
        }
    }// ----------------------------------------------

    /**
     * Merge multiple files
     *
     * @param output       the output file
     * @param filesToMerge the files to merge
     * @throws IOException and IP error occurs
     */
    public static void mergeFiles(File output, File... filesToMerge)
    throws IOException
    {
        if (output == null || filesToMerge == null || filesToMerge.length == 0)
            return;

        Path outFile = output.toPath();

        try (FileChannel out = FileChannel.open(outFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE))
        {
            for (int i = 0; i < filesToMerge.length; i++)
            {
                Path inFile = filesToMerge[i].toPath();

                try (FileChannel in = FileChannel.open(inFile, StandardOpenOption.READ))
                {

                    if(in != null )
                    {
                        long lineNumberSize = in.size();

                        for (long p = 0; p < lineNumberSize; )
                        {
                            p += in.transferTo(p, lineNumberSize - p, out);
                        }
                    }
                }
            }
        }
    }// ------------------------------------------------

    /**
     * Make a directory
     *
     * @param folder the folder/directory to create
     * @return true if the directory was created
     */
    public static boolean mkdir(File folder)
    {

        if (folder == null || folder.exists())
            return false;

        // check if parent directory exists
        File parent = folder.getParentFile();
        if (parent != null && !parent.exists())
            mkdir(parent); // recursively check parentb

        return folder.mkdir();

    }// ---------------------------------------------

    /**
     * @param folder the top folder
     * @return the nest folders in a director
     */
    public static File[] listFolders(File folder)
    {

        if (folder == null)
            return null;

        if (!folder.isDirectory())
            throw new RequiredException(folder.getAbsolutePath() + " is not a folder");

        return folder.listFiles(new FolderFilter());
    }// ---------------------------------------------

    /**
     * Delete the file o folder
     *
     * @param file the file/folder to delete
     * @return true if file as deleted
     * @throws IOException when an IO error occurs
     */
    public static boolean delete(File file)
    throws IOException
    {
        if (file == null)
            throw new RequiredException("file");

        if (!file.exists())
            return false;

        if (file.isDirectory())
            return deleteFolder(file);
        else
        {
            return file.delete();

        }
    }// ---------------------------------------------

    /**
     * @param aFilePath the file to check if it exists
     * @return the true is file exists
     */
    public static boolean exists(String aFilePath)
    {
        if (aFilePath == null)
            throw new IllegalArgumentException("aFilePath required in IO.exists");

        File file = new File(aFilePath);

        return file.exists();
    }// --------------------------------------------

    /**
     * Write binary file data
     *
     * @param file the file
     * @param data
     * @throws IOException when an IO error occurs
     */
    public static void writeFile(File file, byte[] data)
    throws IOException
    {
        writeFile(file.getAbsolutePath(), data);
    }// ---------------------------------------------

    /**
     * Write data to property file
     *
     * @param filePath   the file to write
     * @param properties
     * @throws IOException when an unknown IO error occurs
     */
    public static void writeProperties(String filePath, Properties properties)
    throws IOException
    {
        Writer writer = null;

        try
        {
            writer = new OutputStreamWriter(new FileOutputStream(filePath), IO.CHARSET);

            properties.store(writer, filePath);
        }
        finally
        {
            if (writer != null)
                try
                {
                    writer.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
        }

    }// --------------------------------------------------------

    /**
     * Read properties file
     *
     * @param filePath the file to read
     * @return the properties
     * @throws IOException when an unknown IO error occurs
     */
    public static Properties readProperties(String filePath)
    throws IOException
    {
        Reader reader = null;
        Properties properties = new Properties();

        try
        {
            reader = new InputStreamReader(new FileInputStream(filePath), CHARSET);

            properties.load(reader);

            return properties;
        }
        finally
        {
            if (reader != null)
                try
                {
                    reader.close();
                }
                catch (Exception e)
                {
                    Debugger.printWarn(e);
                }
        }

    }// --------------------------------------------------------

    /**
     * @param file the full file path of which to read
     * @return string data
     * @throws IOException when an unknown IO error occurs
     */
    public static String readFile(File file)
    throws IOException
    {
        if (file == null)
            throw new RequiredException("file to read");

        if (!file.exists())
            throw new RequiredException("file must exist " + file.getAbsolutePath());

        return readFile(file.getAbsolutePath());
    }// ---------------------------------------------

    /**
     * Read the text input
     *
     * @param inputStream      the input stream
     * @param closeInputStream
     * @return the file String content
     * @throws IOException when an unknown IO error occurs
     */
    public static String readText(InputStream inputStream, boolean closeInputStream)
    throws IOException
    {
        return readText(inputStream, closeInputStream, -1);
    }

    /**
     * Read the text input
     *
     * @param limit            the read limit
     * @param inputStream      the input stream
     * @param closeInputStream
     * @return the file String content
     * @throws IOException when an unknown IO error occurs
     */
    public static String readText(InputStream inputStream, boolean closeInputStream, int limit)
    throws IOException
    {
        Reader reader = null;

        try
        {
            reader = toReader(inputStream);

            BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputStream, IO.CHARSET));

            String tmp = buffreader.readLine();
            if (tmp == null)
                return null;

            StringBuilder line = new StringBuilder(tmp);

            while (tmp != null)
            {
                line.append("\n");
                tmp = buffreader.readLine();
                if (tmp != null)
                    line.append(tmp);

                if (limit > 0 && line.length() > limit)
                    return line.toString();

            }

            return line.toString();
        }
        finally
        {
            if (closeInputStream)
            {
                if (reader != null)
                    try
                    {
                        reader.close();
                    }
                    catch (Exception e)
                    {
                    }

                if (inputStream != null)
                    try
                    {
                        inputStream.close();
                    }
                    catch (Exception e)
                    {
                    }

            }
        }
    }// --------------------------------------------

    /**
     * @return System.getProperty(" line.separator ")
     */
    public static String newline()
    {
        return NEWLINE;
    }// --------------------------------------------

    /**
     * Copy a source folder to a destination folder
     *
     * @param sourceFolder      the source directory
     * @param destinationFolder the destination directory
     * @throws IOException when an unknown IO error occurs
     */
    public static void copyDirectory(File sourceFolder, File destinationFolder)
    throws IOException
    {
        if (sourceFolder == null || !sourceFolder.isDirectory())
            throw new RequiredException("sourceFolder");

        if (destinationFolder == null)
            throw new RequiredException("destinationFolder");

        if (!destinationFolder.exists())
            IO.mkdir(destinationFolder);

        if (!destinationFolder.isDirectory())
            throw new RequiredException("destinationFile.isDirectory() in IO");

        File[] sourceNestedFiles = sourceFolder.listFiles();

        if (sourceNestedFiles == null)
            return;

        for (int i = 0; i < sourceNestedFiles.length; i++)
        {
            // copy file
            if (sourceNestedFiles[i].isFile())
                copy(sourceNestedFiles[i], destinationFolder.getAbsolutePath());
            else
            {
                // copy directory
                copyDirectory(sourceNestedFiles[i],
                        new File(destinationFolder.getAbsolutePath() + File.separator + sourceNestedFiles[i].getName()));
            }
        }
    }// ---------------------------------------------

    public static void copyDirectory(String source, String destination, String pattern)
    throws IOException
    {
        File destinationFile = new File(destination);
        if (!destinationFile.exists())
            Debugger.println(IO.class, "mkdir:" + destinationFile.mkdir());

        if (!destinationFile.isDirectory())
            throw new RequiredException("destinationFile \"" + destinationFile + "\" is must a directory");

        File sourceFile = new File(source);

        File[] sourceNestedFiles = listFiles(sourceFile, pattern);

        for (int i = 0; i < sourceNestedFiles.length; i++)
        {
            // copy file
            if (sourceNestedFiles[i].isFile())
                copy(sourceNestedFiles[i], destinationFile.getAbsolutePath());
            else
            {
                // copy directory
                copyDirectory(sourceNestedFiles[i].getAbsolutePath(),
                        destinationFile.getAbsolutePath() + File.separator + sourceNestedFiles[i].getName(), pattern);
            }
        }
    }// ---------------------------------------------

    /**
     * Remove characters from file name
     *
     * @param aFileName the file name to format
     * @return the formatted file with special characters removed
     */
    public static String formatFileName(String aFileName)
    {
        if (aFileName == null || aFileName.length() == 0)
            return "";

        String invalidCharRE = settings().getProperty(IO.class.getName() + ".formatFile.invalidCharRE",
                DEFAULT_FILE_NM_INVALID_CHARACTERS_RE);
        String replaceText = settings().getProperty(IO.class.getName() + ".formatFile.replaceText", "");
        return Text.replaceForRegExprWith(aFileName, invalidCharRE, replaceText);

    }// --------------------------------------------

    /**
     * @param aReader the input reader
     */
    protected static String readFully(Reader aReader)
    throws IOException
    {
        if (aReader == null)
            throw new IllegalArgumentException("aReader required in IO.readFully");

        BufferedReader buffreader = new BufferedReader(aReader);
        String tmp = buffreader.readLine();

        if (tmp == null || tmp.length() == 0)
            return null;

        StringBuffer line = new StringBuffer(tmp);

        while (tmp != null )
        {

            tmp = buffreader.readLine();

            if (tmp != null)

                line.append("\n").append(tmp);
        }

        return line.toString();
    }// --------------------------------------------

    private static byte[] readBinary(InputStream inputStream)
    throws IOException
    {
        if (inputStream == null)
            throw new IllegalArgumentException("inputStream is required");

        byte[] bytes = new byte[IO.FILE_IO_BATCH_SIZE];

        ByteArrayOutputStream ba = new ByteArrayOutputStream(FILE_IO_BATCH_SIZE);
        int len;
        while ((len = inputStream.read(bytes)) > 0)
        {
            ba.write(bytes, 0, len);
        }

        return ba.toByteArray();
    }// --------------------------------------------

    /**
     * Read Class Path resource
     *
     * @param path the classpath location i.e. /properties/config.properties
     * @return the string content from the class location
     * @throws IOException when IO error occurs
     */
    public static String readClassPath(String path)
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

        return readFully(new InputStreamReader(is, CHARSET));
    }// -------------------------------------------

    public static byte[] readBinaryClassPath(String path)
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
    }// -------------------------------------------

    public static ClassLoader getDefaultClassLoader()
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
    }// ------------------------------------------------

    /**
     * @param location the list path
     * @param pattern  the search patttern
     * @return array of files
     */
    public static File[] listFiles(String location, String pattern)
    {
        return listFiles(new File(location), pattern);
    }// --------------------------------------------

    public static String[] list(String location, String pattern)
    {
        return list(new File(location), pattern);
    }// --------------------------------------------

    /**
     * List nested files
     *
     * @param location the location of the top folder
     * @return the files
     */
    public static File[] listFiles(String location)
    {
        if (location == null || location.length() == 0)
            throw new RequiredException("location in IO");

        File folder = new File(location);

        return listFiles(folder);
    }// --------------------------------------------

    public static File[] listFiles(File folder)
    {
        if (folder == null)
            return null;

        if (!folder.isDirectory())
            throw new RequiredException(folder.getAbsolutePath() + " is not a directory");

        return folder.listFiles();
    }// ---------------------------------------------

    /**
     * List the file under a given directory
     *
     * @param directory the directory
     * @param pattern   the file pattern
     * @return the list of file names
     */
    public static String[] list(File directory, String pattern)
    {
        if (pattern == null)
            return directory.list();

        WildCardFilter filter = createFilter(directory, pattern);

        return directory.list(filter);
    }// --------------------------------------------

    public static Set<File> listFileRecursive(String dir, String pattern)
    {
        if (dir == null || dir.length() == 0)
            dir = ".";

        return listFileRecursive(Paths.get(dir).toFile(), pattern);
    }//------------------------------------------------

    /**
     * List the file under a given directory
     *
     * @param directory the directory
     * @param pattern   the file pattern
     * @return the list of files
     */
    public static Set<File> listFileRecursive(File directory, String pattern)
    {
        File[] files = listFiles(directory, pattern);

        Set<File> set = new HashSet<>(FILE_IO_BATCH_SIZE);

        if (files != null && files.length > 0)
            set.addAll(Arrays.asList(files));

        //list directories

        File[] folders = listFolders(directory);


        if (folders != null && folders.length > 0)
        {

            for (File folder : folders)
            {

                Set<File> nested = listFileRecursive(folder, pattern);

                if (nested != null)
                    set.addAll(nested);

            }
        }

        if (set.isEmpty())
            return null;

        return set;
    }

    /**
     * List the file under a given directory
     *
     * @param directory the directory
     * @param pattern   the file pattern
     * @return the list of files
     */
    public static File[] listFiles(File directory, String pattern)
    {

        if (pattern == null || pattern.length() == 0)
            return null;

        validateDirectory(directory);

        // check for /
        int indexofSlash = pattern.indexOf("/");
        if (indexofSlash > 0)
        {
            // get text up still /
            String suffix = pattern.substring(0, indexofSlash);
            pattern = pattern.substring(indexofSlash + 1);
            // append directory
            directory = new File(directory.getAbsolutePath() + "/" + suffix);
            validateDirectory(directory);

        }

        WildCardFilter filter = createFilter(directory, pattern);

        return directory.listFiles(filter);
    }// --------------------------------------------

    /**
     * Common function to build file list filter
     *
     * @param directory the directory to filter
     * @param pattern   the file patter (i.e. *.*)
     * @return the WildCard filter
     */
    private static WildCardFilter createFilter(File directory, String pattern)
    {

        if (pattern == null || pattern.length() == 0)
            throw new IllegalArgumentException("pattern required in list");

        return new WildCardFilter(pattern);
    }// --------------------------------------------

    private static void validateDirectory(File directory)
    {
        if (directory == null)
            throw new RequiredException("directory in IO.list");

        if (!directory.exists())
            throw new IllegalArgumentException("Directory does not exist " + directory.getAbsolutePath());

        if (!directory.isDirectory())
        {
            throw new IllegalArgumentException("Must provide a directory " + directory.getAbsolutePath());
        }
    }

    /**
     * @param urlAddress the URL to read form
     * @return the URL text content
     * @throws IOException when an IO error occurs
     */
    public static String readURL(String urlAddress)
    throws IOException
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
        finally
        {
            if (reader != null)
                reader.close();
        }

    }// --------------------------------------------

    /**
     * @param aPath the path content
     * @return the fixed path with / and trimmed spaces
     */
    public static String fixPath(String aPath)
    {
        if (aPath == null || aPath.length() == 0)
            throw new IllegalArgumentException("aPath required in Documentum.fixPath");

        aPath = aPath.replace('\\', '/');

        return aPath;
    }// --------------------------------------------

    /**
     * Return the length of a given file
     *
     * @param aFilePath the location of the file
     * @return the file length
     * @throws IllegalArgumentException
     */
    public static long getFileSize(String aFilePath)
    throws IllegalArgumentException
    {
        if (aFilePath == null)
            throw new IllegalArgumentException("Cannot obtain file size, File Path not provided");

        return new File(aFilePath).length();
    }// -------------------------

    /**
     * @param aFilePath the file name/path
     * @return Map version of property file
     * @throws IOException           when an IO error occurs
     * @throws FileNotFoundException when the file does not exist
     */
    @SuppressWarnings("rawtypes")
    public static Map readMap(String aFilePath)
    throws IOException, FileNotFoundException
    {
        InputStream in = getFileInputStream(aFilePath);

        Properties prop = new Properties();
        prop.load(in);

        return prop;
    }// --------------------------------------------

    /**
     * Retrieve contents of specified file. Retry reads the specified number of
     * times with the specified delay when errors occur
     *
     * @param aFile         the file
     * @param aRetryCount   number of times to retry read
     * @param aRetryDelayMS delay in between read failures
     * @return file byte content
     * @throws IOException when an unknown IO error occurs
     */
    public static byte[] readBinaryFile(File aFile, int aRetryCount, long aRetryDelayMS)
    throws IOException
    {
        for (int i = 0; i <= aRetryCount; i++)
        {
            try
            {
                return IO.readBinaryFile(aFile);
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
        throw new IOException(aFile.getAbsolutePath());
    }// ----------------------------------------------------

    /**
     * @param aFilePath the file path
     * @return the bytes of the file
     * @throws FileNotFoundException when the file is not found
     * @throws IOException           when an unknown IO error occurs
     */
    public static byte[] readBinaryFile(String aFilePath)
    throws FileNotFoundException, IOException
    {
        return readBinaryFile(new File(aFilePath));
    }// -----------------------------------------------

    /**
     * @param file the file to read
     * @return binary file content
     * @throws FileNotFoundException when file not found
     * @throws IOException           and IO exception occurs
     */
    public static byte[] readBinaryFile(File file)
    throws FileNotFoundException, IOException
    {
        BufferedInputStream in = null;
        try
        {
            in = new BufferedInputStream(new FileInputStream(file));

            byte[] b = new byte[Long.valueOf(file.length()).intValue()];

            int cnt = in.read(b);
            if (cnt <= 0)
                return null;

            return b;
        }
        finally
        {
            if (in != null)
                try
                {
                    in.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
        }
    }// --------------------------------

    /**
     * Retrieve contents of specified file. Retry reads the specified number of
     * times with the specified delay when errors occur
     *
     * @param aFilePath     the file path
     * @param aRetryCount   number of times to retry read
     * @param aRetryDelayMS delay in between read failures
     * @param charset       the character set
     * @return file string content
     * @throws IOException
     */
    public static String readFile(String aFilePath, int aRetryCount, long aRetryDelayMS, Charset charset)
    throws IOException
    {
        for (int i = 0; i <= aRetryCount; i++)
        {
            try
            {
                return IO.readFile(aFilePath, charset);
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
        throw new IOException(aFilePath);
    }// ----------------------------------------------------

    /**
     * @param fileName the full file path of which to read
     * @return string data
     * @throws IOException
     */
    public static String readFile(String fileName)
    throws IOException
    {
        return readFile(fileName, IO.CHARSET);
    }// --------------------------------------------------------

    /**
     * @param fileName the full file path of which to read
     * @param charSet  the CHAR SET
     * @return string data
     * @throws IOException
     */
    public static String readFile(String fileName, Charset charSet)
    throws IOException
    {
        if (fileName == null || fileName.length() == 0)
            return null;

        Path path = Paths.get(fileName);
        File file = path.toFile();

        if (!file.exists())
        {
            throw new IllegalArgumentException("File:" + file.getAbsolutePath() + " does not exist");
        }

        StringBuilder stringBuilder = new StringBuilder(Long.valueOf(file.length()).intValue());

        String line;
        boolean firstTime = true;
        try (BufferedReader reader = Files.newBufferedReader(path, charSet))
        {
            while ((line = reader.readLine()) != null)
            {
                if (!firstTime)
                {
                    stringBuilder.append(NEWLINE);
                }
                firstTime = false;

                stringBuilder.append(line);

            }
        }
        return stringBuilder.toString();
    }// --------------------------------------------------

    /**
     * @param aFileNM the full file path of which to read
     * @param charset the character set
     * @return string data
     * @throws IOException
     */
    public static String[] readLines(String aFileNM, Charset charset)
    throws IOException
    {
        if (Data.isNull(aFileNM))
            throw new IllegalArgumentException("file name not provided");

        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader buffreader = null;
        try
        {
            buffreader = new BufferedReader(new InputStreamReader(new FileInputStream(aFileNM), charset));

            String tmp = buffreader.readLine();

            if (tmp == null)
                throw new IOException(aFileNM + " empty file");

            lines.add(tmp);

            while (tmp != null)
            {
                tmp = buffreader.readLine();
                if (tmp != null)
                    lines.add(tmp);
            }

            String[] lineArray = new String[lines.size()];
            lines.toArray(lineArray);
            return lineArray;
        }
        finally
        {
            if (buffreader != null)
                try
                {
                    buffreader.close();
                }
                catch (Exception e)
                {
                    Debugger.printWarn(e);
                }
        }
    }// --------------------------------------------------

    public static void copy(File aFile, String aDestinationPath)
    throws FileNotFoundException, IOException
    {
        InputStream in = null;

        try
        {
            in = new BufferedInputStream(new FileInputStream(aFile));
            write(aDestinationPath + File.separator + aFile.getName(), in);
        }
        finally
        {
            if (in != null)
                try
                {
                    in.close();
                }
                catch (Exception e)
                {
                }
        }

    }// ---------------------------------------------------

    /**
     * Write binary file data
     *
     * @param fileName the file name
     * @param url      the URL
     * @throws Exception
     */
    public static void writeFile(String fileName, URL url)
    throws Exception
    {
        OutputStream os = null;
        InputStream is = null;

        try
        {
            os = new FileOutputStream(fileName);

            is = url.openStream();
            int BYTE_BUFFER_SIZE = settings().getPropertyInteger(BYTE_BUFFER_SIZE_PROP, FILE_IO_BATCH_SIZE).intValue();

            byte[] bytes = new byte[BYTE_BUFFER_SIZE]; // 5 KB
            int cnt;
            // while((cnt= is.read(bytes,0,(int)bytes.length)) != -1)
            while ((cnt = is.read(bytes)) != -1)
            {
                os.write(bytes, 0, cnt);
            }
        }
        finally
        {
            if (os != null)
                try
                {
                    os.close();
                }
                catch (Exception e)
                {
                }
            if (is != null)
                try
                {
                    is.close();
                }
                catch (Exception e)
                {
                }
        }
    }// -----------------------------------------------

    public static IOFileOperation ops(File file)
    {
        return new IOFileOperation(file);
    }


    public String readInputStream(InputStream aInputStream)
    throws IOException
    {
        Reader reader = null;
        try
        {
            reader = toReader(aInputStream);

            return readFully(reader);
        }
        finally
        {
            if (reader != null)
                reader.close();
        }
    }// --------------------------------------------

    /**
     * Write output stream to input stream
     *
     * @param aOutputStream
     * @param aInputStream
     * @throws IOException
     */
    public static void write(OutputStream aOutputStream, InputStream aInputStream)
    throws IOException
    {
        int BYTE_BUFFER_SIZE = settings().getPropertyInteger(BYTE_BUFFER_SIZE_PROP, FILE_IO_BATCH_SIZE).intValue();

        byte[] bytes = new byte[BYTE_BUFFER_SIZE]; // 5 KB
        // byte[] bytes = new byte[100]; //5 KB
        // int offset = 0;
        int cnt;

        while ((cnt = aInputStream.read(bytes)) != -1)
        {
            aOutputStream.write(bytes, 0, cnt);
        }

        // aOutputStream.flush();
    }// ---------------------------------------------

    /**
     * Write output stream to input stream
     *
     * @param aOutputStream
     * @param aInputStream
     * @throws IOException
     */
    public static void write(Writer aOutputStream, Reader aInputStream)
    throws IOException
    {
        int BYTE_BUFFER_SIZE = settings().getPropertyInteger(BYTE_BUFFER_SIZE_PROP, FILE_IO_BATCH_SIZE).intValue();

        char[] chars = new char[BYTE_BUFFER_SIZE]; // 5 KB
        // byte[] bytes = new byte[100]; //5 KB
        // int offset = 0;
        int cnt;

        while ((cnt = aInputStream.read(chars)) != -1)
        {
            aOutputStream.write(chars, 0, cnt);
        }
    }// ---------------------------------------------

    /**
     * @param aInputStream
     * @return the inputStream Reader
     */
    public static Reader toReader(InputStream aInputStream)
    {
        return new java.io.InputStreamReader(aInputStream, CHARSET);

    }// ---------------------------------------------

    /**
     * @param aFilePath    the file path the write
     * @param aInputStream the input stream data to write
     * @throws IOException
     */
    public static void write(String aFilePath, InputStream aInputStream)
    throws IOException
    {
        FileOutputStream os = null;
        try
        {
            os = new FileOutputStream(aFilePath);
            write(os, aInputStream);
        }
        finally
        {
            if (os != null)
                try
                {
                    os.close();
                }
                catch (Exception e)
                {
                }
        }
    }// ---------------------------------------------

    /**
     * @param aFilePath the file path
     * @return file input stream
     * @throws FileNotFoundException when the file is not found
     */
    public static InputStream getFileInputStream(String aFilePath)
    throws FileNotFoundException
    {
        return new FileInputStream(aFilePath);
    }// ------------------------------------------------

    /**
     * Hide the file name extension test.doc = test and test = test
     *
     * @param aFileName the file name
     * @return the file name without the extension
     */
    public static String hideExtension(String aFileName)
    {
        if (aFileName == null || aFileName.length() == 0)
            throw new IllegalArgumentException("File Name required in IO hideExtension");

        int lastPeriodIndex = aFileName.lastIndexOf(".");

        if (lastPeriodIndex > -1)
        {
            return aFileName.substring(0, lastPeriodIndex);
        }

        return aFileName;
    }// --------------------------------------------

    /**
     * @param aFileName the file name
     * @return file extension (null if file does not have an extension
     */
    public static String parseFileExtension(String aFileName)
    {
        if (aFileName == null)
            throw new IllegalArgumentException("aFileName required in parseFileExtension");

        int len = aFileName.length();
        if (len == 0)
            throw new IllegalArgumentException("aFileName required in parseFileExtension");

        int lastPeriodIndex = aFileName.lastIndexOf(".");

        if (lastPeriodIndex > -1)
        {
            return aFileName.substring(lastPeriodIndex + 1, len);
        }

        return null;
    }// --------------------------------------------

    /**
     * Parse folder path
     *
     * @param filePath
     * @return absolute path of root directory
     */
    public static String parseFolderPath(String filePath)
    {
        if (filePath == null || filePath.length() == 0)
            throw new RequiredException("filePath");
        File file = new File(filePath);

        return file.getParentFile().getAbsolutePath();
    }// ----------------------------------------------

    /**
     * @param aFolderPath
     * @return file name of the path
     */
    public static String parseFileName(String aFolderPath)
    {

        int lastFSIndex = aFolderPath.lastIndexOf("/");
        int lastBSIndex = aFolderPath.lastIndexOf("\\");
        int lastSeparatorIndex = lastFSIndex;
        if (lastBSIndex > lastSeparatorIndex)
            lastSeparatorIndex = lastBSIndex;

        if (lastSeparatorIndex > -1)
            return aFolderPath.substring(lastSeparatorIndex + 1);

        return aFolderPath;
    }// ------------------------------------------------

    /**
     * Write binary file data
     *
     * @param filePath the file to write
     * @param data     the data to writer
     * @throws IOException when IO error occurs
     */
    public static void writeFile(String filePath, byte[] data)
    throws IOException
    {
        writeFile(filePath, data, false);
    }// --------------------------------------------

    /**
     * Write binary file data
     *
     * @param aFilePath the file
     * @param aData     the data to write
     * @param append    boolean to append or not
     * @throws IOException when an IO error occurs
     */
    public static void writeFile(String aFilePath, byte[] aData, boolean append)
    throws IOException
    {
        if (aData == null)
            throw new IOException("No bytes provided for file " + aFilePath);

        if (aFilePath == null || aFilePath.length() == 0)
            throw new IllegalArgumentException("aFilePath required in writeFile");

        OutputStream os = null;
        try
        {
            os = new FileOutputStream(aFilePath, append);
            os.write(aData);

            os.flush();
        }
        catch (FileNotFoundException e)
        {
            throw new IOException(Debugger.stackTrace(e) + " path=" + aFilePath);
        }
        finally
        {
            if (os != null)
                try
                {
                    os.close();
                }
                catch (Exception e)
                {
                }
        }

    }// -----------------------------------------------

    public static void writeFile(String fileName, String text)
    throws IOException
    {
        writeFile(fileName, text, IO.CHARSET);
    }// --------------------------------------------------------

    /**
     * Write string file data
     *
     * @param fileName the file to write
     * @param text     the text to write
     * @param charset  the character set
     * @throws IOException when IO error occurs
     */
    public static void writeFile(String fileName, String text, Charset charset)
    throws IOException
    {
        writeFile(fileName, text, false, charset);
    }// ---------------------------------------------

    /**
     * Write string file data
     *
     * @param fileName the file
     * @param text     the text to write
     * @param append   boolean to append
     * @throws IOException when an IO error occurs
     */
    public static void writeFile(String fileName, String text, boolean append)
    throws IOException
    {
        writeFile(fileName, text, append, IO.CHARSET);
    }// --------------------------------------------------------

    /**
     * Write string file data
     *
     * @param fileName the file
     * @param text     the text to write
     * @param append   whether to append to a current file
     * @param charset  the character set
     * @throws IOException when IO error occurs
     */
    public static void writeFile(String fileName, String text, boolean append, Charset charset)
    throws IOException
    {
        writeFile(new File(fileName), text, append, charset);
    }// --------------------------------------------------------

    /**
     * Write string file data
     *
     * @param file the file
     * @param text the text to write
     * @throws IOException unknown error occurs
     */
    public static void writeFile(File file, String text)
    throws IOException
    {
        writeFile(file, text, IO.CHARSET);
    }// --------------------------------------------------------

    /**
     * Write string file data
     *
     * @param file    the file
     * @param text    the text to write
     * @param charset the character set
     * @throws IOException when an IO error occurs
     */
    public static void writeFile(File file, String text, Charset charset)
    throws IOException
    {
        writeFile(file, text, false, charset);
    }// --------------------------------------------------------

    /**
     * Write string file data
     *
     * @param file   the file to write to
     * @param text   the text to write
     * @param append boolean to determine if file must be appended
     * @throws IOException unknown IO error occurs
     */
    public static void writeFile(File file, String text, boolean append)
    throws IOException
    {
        writeFile(file, text, append, IO.CHARSET);
    }// --------------------------------------------------------

    /**
     * Write string file data
     *
     * @param file    the file
     * @param text    the text to write
     * @param append  to append to the file
     * @param charset the character set
     * @throws IOException
     */
    public static void writeFile(File file, String text, boolean append, Charset charset)
    throws IOException
    {
        if (text == null)
            return; // nothing to write

        mkdir(file.getParentFile());

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file, append), charset.newEncoder()))
        {
            writer.write(text);
        }

    }// ------------------------------------------------------

    /**
     * @param fileName the file to write
     * @param data     the data
     * @throws IOException when an unknown IO error occurs
     */
    public static void writeAppend(String fileName, String data)
    throws IOException
    {
        IO.writeAppend(fileName, data, IO.CHARSET);

    }// --------------------------------------------------------

    /**
     * @param file the file to write
     * @param data the data
     * @throws IOException when an unknown IO error occurs
     */
    public static void writeAppend(File file, String data)
    throws IOException
    {
        IO.writeFile(file, data, true, IO.CHARSET);
    }

    /**
     * @param fileName the file to write
     * @param data     the data
     * @param charset  the character set
     * @throws IOException when an unknown IO error occurs
     */
    public static void writeAppend(String fileName, String data, Charset charset)
    throws IOException
    {
        writeFile(new File(fileName), data, true, charset);
    }// --------------------------------------------

    /**
     * Delete a given the directory
     *
     * @param file the directory to delete
     */
    private static boolean deleteFolder(File file)
    throws IOException
    {
        emptyFolder(file);

        return file.delete();
    }// ---------------------------------------------

    /**
     * Delete all files in a given folder
     *
     * @param directory the directory to empty
     * @throws IOException when an unknown IO error occurs
     */
    public static void emptyFolder(File directory)
    throws IOException
    {
        File[] files = directory.listFiles();

        if (files != null && files.length > 0)
        {
            for (int i = 0; i < files.length; i++)
                delete(files[i]);
        }
    }//------------------------------------------------

    /**
     * @param directory the directory to make
     * @throws IOException when an unexpected IO error occurs
     */
    public static void mkdir(String directory)
    throws IOException
    {
        if (directory == null || directory.length() == 0)
            return;

        mkdir(Paths.get(directory).toFile());

    }//------------------------------------------------

    /**
     * The temporary directory
     *
     * @return System.getProperty(" java.io.tmpdir ")
     */
    public static String tempDir()
    {
        return System.getProperty("java.io.tmpdir");
    }//------------------------------------------------

}
