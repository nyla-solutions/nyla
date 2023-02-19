package nyla.solutions.core.io;

import nyla.solutions.core.io.csv.CsvReader;
import nyla.solutions.core.io.csv.CsvWriter;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class IOTest
{
    @Test
    void deleteFiles()
    {

        File file = mock(File.class);
        IO.ops(file).deleteDirectoryFiles();
    }

    @Test
    void readFully() throws IOException
    {
        String expected = "1\n2\3";

        Reader reader = new StringReader(expected);
        String actual = IO.readFully(reader);


        assertEquals(expected,actual);
    }

    @Test
    public void test_readBinaryClassPath()
    throws IOException
    {
        String path = "truststore.jks";

        byte[] bytes = IO.readBinaryClassPath(path);

        assertNotNull(bytes);

        assertEquals(new File("src/test/resources/truststore.jks").length(), bytes.length);
    }

    @Test
    public void testFind()
    throws Exception
    {
        List<String> filePaths = null;
        String pattern = null;

        assertNull(IO.find(filePaths, pattern));

        List<File> results = null;

        File dir = Paths.get("target/runtime/io").toFile();
        System.out.println(dir.mkdirs());

        File file1 = Paths.get("target/runtime/io/1.txt").toFile();
        IO.writeFile(file1, "Test A\n  TestB");

        File file2 = Paths.get("target/runtime/io/2.txt").toFile();
        IO.writeFile(file2, "Test X\n  TestZ");

        pattern = "1.*";
        filePaths = new ArrayList<String>();
        filePaths.add(file1.getAbsolutePath());
        filePaths.add(file2.getAbsolutePath());
        results = IO.find(filePaths, pattern);
        assertTrue(results != null && !results.isEmpty());


        //test nested directories

        File dir1 = Paths.get("target/runtime/io/d1").toFile();
        System.out.println(dir1.mkdirs());

        IO.writeFile(dir1.getAbsolutePath() + "/d1f1.nested", "Test A\n  TestB");

        File dir2 = Paths.get("target/runtime/io/d2").toFile();
        System.out.println(dir2.mkdirs());

        IO.writeFile(dir1.getAbsolutePath() + "/d2f2.nested", "Test X\n  TestZ");

        filePaths.add("target/runtime/io");
        results = IO.find(filePaths, "*.nested");

        assertTrue(results != null && !results.isEmpty());

    }//------------------------------------------------

    @Test
    void readText_when_bufferReadernull_returns_null() throws IOException
    {
        BufferedReader reader = null;
        assertNull(IO.readText(reader));
    }

    @Test
    public void testGrep()
    throws Exception
    {

        List<File> filePaths = null;
        String pattern = null;

        assertNull(IO.grep(pattern, filePaths));

        Map<File, Collection<String>> results = null;

        File dir = Paths.get("target/runtime/io").toFile();
        System.out.println(dir.mkdirs());

        File file1 = Paths.get("target/runtime/io/1.txt").toFile();
        IO.writeFile(file1, "Test A\n  TestB");

        File file2 = Paths.get("target/runtime/io/2.txt").toFile();
        IO.writeFile(file2, "Test X\n  TestZ");

        pattern = "TestZ";
        filePaths = new ArrayList<File>();
        filePaths.add(file1);
        filePaths.add(file2);
        results = IO.grep(pattern, filePaths);
        assertTrue(results != null && !results.isEmpty());

    }//------------------------------------------------

    @Test
    public void testMergeFiles()
    throws IOException
    {
        if (!Paths.get("target/runtime/").toFile().mkdirs())
            System.out.println("Existing directoy deleted");

        File csv1 = new File("target/runtime/csv1.csv");
        System.out.println(csv1.delete());
        File csv2 = new File("target/runtime/csv2.csv");
        System.out.println(csv2.delete());

        CsvWriter csvWriter1 = new CsvWriter(csv1);

        csvWriter1.appendRow("1", "1-1", "1-2");

        CsvWriter csvWriter2 = new CsvWriter(csv2);

        csvWriter2.appendRow("2", "2-1", "2-2", "\"ny\"la");


        File[] filesToMerge = {csv1, csv2};
        File output = Paths.get("target/runtime/merged.csv").toFile();
        System.out.println(output.delete());

        assertFalse(output.exists());

        IO.mergeFiles(output, filesToMerge);

        assertTrue(output.exists());

        CsvReader reader = new CsvReader(output);

        assertEquals(2, reader.size());


        assertEquals(reader.row(0).get(0), "1");
        assertEquals(reader.row(0).get(1), "1-1");
        assertEquals(reader.row(0).get(2), "1-2");
        assertEquals(reader.row(1).get(0), "2");
        assertEquals(reader.row(1).get(1), "2-1");
        assertEquals(reader.row(1).get(2), "2-2");

        String result = reader.row(1).get(3);
        System.out.println("result:" + result);
        assertEquals("\"ny\"la", result);


    }//------------------------------------------------

    @Test
    public void testOverview()
    throws IOException
    {
        //Use mkdir to create entire directory paths
        File inputDirectory = new File("./runtime/tmp/input");
        IO.mkdir(inputDirectory);
        assertTrue(inputDirectory.exists());

        //Write text or binary files
        String fileName = inputDirectory.getAbsolutePath() + "/test.txt";
        IO.writeFile(fileName, "Hello" + IO.newline() + "world", IO.CHARSET);
        IO.writeFile(fileName, "Hello" + IO.newline() + "world");

        //Read text or binary files
        String output = IO.readFile(fileName);
        assertEquals("Hello" + IO.newline() + "world", output);

        //Copy entire Directories and files nested files
        File outputDirectory = new File("./runtime/tmp/output");
        IO.copyDirectory(inputDirectory, outputDirectory);
        assertTrue(outputDirectory.length() >= inputDirectory.length());

        //Use Wildcard pattern to list files
        assertTrue(IO.list(outputDirectory, "*.txt").length > 0);

        //Delete file or directory (all files are deleted from the given directory)
        IO.delete(inputDirectory);
        assertTrue(!inputDirectory.exists());

    }//------------------------------------------------

    @Test
    public void testListFileRecursive()
    throws Exception
    {
        String dir = "runtime/tmp/listfileRecursive";
        //File parent  = null ;
        assertNull(IO.listFileRecursive((File) null, null));
        IO.mkdir(dir);
        int LEN = 3;
        for (int i = 0; i < LEN; i++)
        {
            String nestedDir = dir + "/dir" + i;
            IO.mkdir(nestedDir);

            IO.writeFile(nestedDir + "/" + i + ".txt", String.valueOf("i"));

        }


        //parent = Paths.get(dir).toFile();
        Set<File> results = IO.listFileRecursive(dir, "*.txt");
        assertNotNull(results);
        assertEquals(LEN, results.size());

        for (File file : results)
        {
            assertTrue(file.getName().endsWith(".txt"));
        }

    }//------------------------------------------------

    @Test
    public void testlistFiles()
    throws Exception
    {


        File[] files = IO.listFiles(new File("src/test/resources/iotest"), "*");

        assertNotNull(files);
        assertEquals(4, files.length);
        files = IO.listFiles(new File("src/test/resources/iotest"), "*.xml");
        assertTrue(files.length == 2);

    }
    //private String directoryPath = "./runtime/tmp";
}
