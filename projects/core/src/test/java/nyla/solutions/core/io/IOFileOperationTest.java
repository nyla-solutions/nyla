package nyla.solutions.core.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IOFileOperationTest
{
    private File expectedFile;
    private File expectedNested;

    @BeforeEach
    public void setUp()
    {
        expectedFile = mock(File.class);
        expectedNested = mock(File.class);
    }

    @Test
    void deleteDirFiles_dir()
    {
        when(expectedFile.isDirectory()).thenReturn(true);

        File[] mockFiles = {expectedNested};


        when(expectedFile.listFiles()).thenReturn(mockFiles);
        IOFileOperation subject = new IOFileOperation(expectedFile);


        subject.deleteDirectoryFiles();


        verify(expectedFile).isDirectory();
        verify(expectedFile).listFiles();
        verify(expectedNested).delete();

    }

    @Test
    void deleteDirFiles_file()
    {

        File[] mockFiles = {expectedNested};
        when(expectedFile.isDirectory()).thenReturn(false);
        IOFileOperation subject = new IOFileOperation(expectedFile);

        subject.deleteDirectoryFiles();


        verify(expectedFile, times(1)).isDirectory();
        verify(expectedFile, never()).listFiles();
        verify(expectedNested, never()).delete();

    }

    @Test
    void mkParentDir()
    {
        Path expectFileWithParent = Paths.get("build/tmp/parent/file.txt");

        expectFileWithParent.getParent().toFile().delete();
        assertFalse(expectFileWithParent.getParent().toFile().exists());

        IOFileOperation subject = new IOFileOperation(expectFileWithParent.toFile());
        File actual = subject.mkParentDir();
        assertNotNull(actual);
        assertTrue(actual.exists());

        assertTrue(expectFileWithParent.getParent().toFile().exists());

    }

    @Test
    void given_null_then_return_nulls()
    {
       assertThrows(NullPointerException.class,() -> new IOFileOperation(null));
    }
}