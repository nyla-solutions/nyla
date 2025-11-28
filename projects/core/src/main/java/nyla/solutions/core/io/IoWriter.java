package nyla.solutions.core.io;

import nyla.solutions.core.util.Debugger;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import static nyla.solutions.core.util.Config.settings;

/**
 * IO file writer utility
 * @author Gregory Green
 */
public class IoWriter {

    protected IoWriter(){}

    /**
     * Write binary file data
     *
     * @param filePath the file to write
     * @param data     the data to writer
     * @throws IOException when IO error occurs
     */
    public void writeFile(String filePath, byte[] data)
            throws IOException
    {
        writeFile(filePath, data, false);
    }

    /**
     * Write binary file data
     *
     * @param aFilePath the file
     * @param aData     the data to write
     * @param append    boolean to append or not
     * @throws IOException when an IO error occurs
     */
    public void writeFile(String aFilePath, byte[] aData, boolean append)
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

    }

    public void writeFile(String fileName, String text)
            throws IOException
    {
        writeFile(fileName, text, IO.CHARSET);
    }

    /**
     * Write string file data
     *
     * @param fileName the file to write
     * @param text     the text to write
     * @param charset  the character set
     * @throws IOException when IO error occurs
     */
    public void writeFile(String fileName, String text, Charset charset)
            throws IOException
    {
        writeFile(fileName, text, false, charset);
    }

    /**
     * Write string file data
     *
     * @param fileName the file
     * @param text     the text to write
     * @param append   boolean to append
     * @throws IOException when an IO error occurs
     */
    public void writeFile(String fileName, String text, boolean append)
            throws IOException
    {
        writeFile(fileName, text, append, IO.CHARSET);
    }

    /**
     * Write string file data
     *
     * @param fileName the file
     * @param text     the text to write
     * @param append   whether to append to a current file
     * @param charset  the character set
     * @throws IOException when IO error occurs
     */
    public void writeFile(String fileName, String text, boolean append, Charset charset)
            throws IOException
    {
        writeFile(new File(fileName), text, append, charset);
    }

    /**
     * Write string file data
     *
     * @param file the file
     * @param text the text to write
     * @throws IOException unknown error occurs
     */
    public void writeFile(File file, String text)
            throws IOException
    {
        writeFile(file, text, IO.CHARSET);
    }

    /**
     * Write string file data
     *
     * @param file    the file
     * @param text    the text to write
     * @param charset the character set
     * @throws IOException when an IO error occurs
     */
    public void writeFile(File file, String text, Charset charset)
            throws IOException
    {
        writeFile(file, text, false, charset);
    }

    /**
     * Write string file data
     *
     * @param file   the file to write to
     * @param text   the text to write
     * @param append boolean to determine if file must be appended
     * @throws IOException unknown IO error occurs
     */
    public void writeFile(File file, String text, boolean append)
            throws IOException
    {
        writeFile(file, text, append, IO.CHARSET);
    }

    /**
     * Write string file data
     *
     * @param file    the file
     * @param text    the text to write
     * @param append  to append to the file
     * @param charset the character set
     * @throws IOException
     */
    public void writeFile(File file, String text, boolean append, Charset charset)
            throws IOException
    {
        if (text == null)
            return; // nothing to write

        IO.dir().mkdir(file.getParentFile());

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file, append), charset.newEncoder()))
        {
            writer.write(text);
        }

    }

    /**
     * @param fileName the file to write
     * @param data     the data
     * @throws IOException when an unknown IO error occurs
     */
    public void writeAppend(String fileName, String data)
            throws IOException
    {
        writeAppend(fileName, data, IO.CHARSET);

    }

    /**
     * @param file the file to write
     * @param data the data
     * @throws IOException when an unknown IO error occurs
     */
    public void writeAppend(File file, String data)
            throws IOException
    {
        writeFile(file, data, true, IO.CHARSET);
    }

    /**
     * @param fileName the file to write
     * @param data     the data
     * @param charset  the character set
     * @throws IOException when an unknown IO error occurs
     */
    public void writeAppend(String fileName, String data, Charset charset)
            throws IOException
    {
        writeFile(new File(fileName), data, true, charset);
    }

    /**
     * Write binary file data
     *
     * @param file the file
     * @param data
     * @throws IOException when an IO error occurs
     */
    public void writeFile(File file, byte[] data)
            throws IOException
    {
        writeFile(file.getAbsolutePath(), data);
    }

    /**
     * Write data to property file
     *
     * @param filePath   the file to write
     * @param properties
     * @throws IOException when an unknown IO error occurs
     */
    public void writeProperties(String filePath, Properties properties)
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

    }

    /**
     * @param aFilePath    the file path the write
     * @param aInputStream the input stream data to write
     * @throws IOException
     */
    public void write(String aFilePath, InputStream aInputStream)
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
    }

    /**
     * Write output stream to input stream
     *
     * @param aOutputStream
     * @param aInputStream
     * @throws IOException
     */
    public void write(Writer aOutputStream, Reader aInputStream)
            throws IOException
    {
        int BYTE_BUFFER_SIZE = settings().getPropertyInteger(IO.BYTE_BUFFER_SIZE_PROP, IO.FILE_IO_BATCH_SIZE).intValue();

        char[] chars = new char[BYTE_BUFFER_SIZE]; // 5 KB
        // byte[] bytes = new byte[100]; //5 KB
        // int offset = 0;
        int cnt;

        while ((cnt = aInputStream.read(chars)) != -1)
        {
            aOutputStream.write(chars, 0, cnt);
        }
    }

    /**
     * Write output stream to input stream
     *
     * @param outputStream the output stream
     * @param aInputStream
     * @throws IOException
     */
    public void write(OutputStream outputStream, InputStream aInputStream)
            throws IOException
    {
        int BYTE_BUFFER_SIZE = settings().getPropertyInteger(IO.BYTE_BUFFER_SIZE_PROP, IO.FILE_IO_BATCH_SIZE).intValue();

        byte[] bytes = new byte[BYTE_BUFFER_SIZE]; // 5 KB
        // byte[] bytes = new byte[100]; //5 KB
        // int offset = 0;
        int cnt;

        while ((cnt = aInputStream.read(bytes)) != -1)
        {
            outputStream.write(bytes, 0, cnt);
        }

        // aOutputStream.flush();
    }


    public synchronized Date touch(File file)
            throws IOException
    {
        return touch(file, Calendar.getInstance().getTime());
    }

    public synchronized Date touch(File file, Date date)
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
    }

    /**
     * Write binary file data
     *
     * @param fileName the file name
     * @param url      the URL
     * @throws Exception
     */
    public void writeFile(String fileName, URL url)
            throws Exception
    {
        OutputStream os = null;
        InputStream is = null;

        try
        {
            os = new FileOutputStream(fileName);

            is = url.openStream();
            int BYTE_BUFFER_SIZE = settings().getPropertyInteger(IO.BYTE_BUFFER_SIZE_PROP, IO.FILE_IO_BATCH_SIZE).intValue();

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
    }

}
