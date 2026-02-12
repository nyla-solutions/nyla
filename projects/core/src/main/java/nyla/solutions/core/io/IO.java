package nyla.solutions.core.io;

import nyla.solutions.core.exception.IoException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
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
    public static final String CHARSET_NM = "UTF-8";

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
    public static final String NEWLINE = System.lineSeparator();
    private static final IoDir ioDir = new IoDir();
    private static final IoReader ioReader = new IoReader();
    private static final IoWriter ioWriter = new IoWriter();


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

    }

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
     */
    public static Map<File, Collection<String>> grep(String text, List<File> logFiles)
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
            catch(IOException e)
            {
                throw new SystemException(Debugger.stackTrace(e));
            }
        }

        if (map.isEmpty())
            return null;

        return map;
    }

    /**
     * @return the System.getProperty("file.separator");
     */
    public static String fileSeparator()
    {
        return FileSystems.getDefault().getSeparator();
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
    }

    /**
     * Convert the object to binary
     *
     * @param obj the object to write
     * @return the serialized bytes of the object
     */
    public static byte[] serializeToBytes(Object obj)
    {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(new BufferedOutputStream(out)))
        {

            stream.writeObject(obj);
            stream.flush();

            return out.toByteArray();
        }
        catch (Exception e)
        {
            throw new SystemException(Debugger.stackTrace(e));
        }
    }

    /**
     * Read object from file
     *
     * @param file the file that has the object information
     * @return the UN-serialized object
     */
    public static<T> T deserialize(File file)
    {
        try(ObjectInputStream stream =  new ObjectInputStream(new FileInputStream(file)))
        {
            return (T)stream.readObject();
        }
        catch (Exception e)
        {
            throw new SystemException(Debugger.stackTrace(e));
        }

    }

    /**
     * Merge multiple files
     *
     * @param output       the output file
     * @param filesToMerge the files to merge
     */
    public static void mergeFiles(File output, File... filesToMerge)
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
        catch (IOException e)
        {
            throw new IoException(e);
        }
    }


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
    }



    /**
     * @return System.getProperty(" line.separator ")
     */
    public static String newline()
    {
        return NEWLINE;
    }

    /**
     * Copy a source folder to a destination folder
     *
     * @param sourceFolder      the source directory
     * @param destinationFolder the destination directory
     */
    public static void copyDirectory(File sourceFolder, File destinationFolder)
    {
        if (sourceFolder == null || !sourceFolder.isDirectory())
            throw new RequiredException("sourceFolder");

        if (destinationFolder == null)
            throw new RequiredException("destinationFolder");

        if (!destinationFolder.exists())
            IO.dir().mkdir(destinationFolder);

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
    }

    public static void copyDirectory(String source, String destination, String pattern)
    {
        File destinationFile = new File(destination);
        if (!destinationFile.exists())
            Debugger.println(IO.class, "mkdir:" + destinationFile.mkdir());

        if (!destinationFile.isDirectory())
            throw new RequiredException("destinationFile \"" + destinationFile + "\" is must a directory");

        File sourceFile = new File(source);

        File[] sourceNestedFiles = dir().listFiles(sourceFile, pattern);

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
    }

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
        return Text.editor().replaceForRegExprWith(aFileName, invalidCharRE, replaceText);

    }







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
    }






    /**
     *
     * @return the IoReader
     */
    public static IoReader reader() {
        return ioReader;
    }

    public static void copy(File aFile, String aDestinationPath)
    {
        InputStream in = null;

        try
        {
            in = new BufferedInputStream(new FileInputStream(aFile));
            writer().write(aDestinationPath + File.separator + aFile.getName(), in);
        }
        catch (IOException e)
        {
            throw new IoException(e);
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

    }


    public static IOFileOperation ops(File file)
    {
        return new IOFileOperation(file);
    }

    public static <T> T deserialize(byte[] bytes)
    {
        try(ObjectInputStream  stream = new ObjectInputStream(new ByteArrayInputStream(bytes)))
        {
            return (T)stream.readObject();
        }
        catch (Exception e)
        {
            throw new RuntimeException(Debugger.stackTrace(e));
        }
    }

    public static File tempDirFile() {
        return Paths.get(tempDir()).toFile();
    }

    /**
     * Return the  IoDir instance
     * @return the system temp directory
     */
    public static IoDir dir() {
        return ioDir;
    }





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
    }

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
    }

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
    }

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
    }





    /**
     * The temporary directory
     *
     * @return System.getProperty(" java.io.tmpdir ")
     */
    public static String tempDir()
    {
        return System.getProperty("java.io.tmpdir");
    }

    public static IoWriter writer() {
        return ioWriter;
    }
}
