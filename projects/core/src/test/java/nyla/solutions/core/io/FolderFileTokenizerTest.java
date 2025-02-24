package nyla.solutions.core.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FilenameFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FolderFileTokenizerTest {

    private File folder;
    private File file;
    private String fileName = "text.txt";

    @BeforeEach
    void setUp() {
        folder = mock(File.class);
        file = mock(File.class);
    }

    @Test
    void getFileTokenizer() {

        File[] expectedFiles = {file};

        when(file.getName()).thenReturn(fileName);
        when(folder.isDirectory()).thenReturn(true);
        when(folder.exists()).thenReturn(true);
        when(folder.listFiles(any(FilenameFilter.class))).thenReturn(expectedFiles);

        var subject = new FolderFileTokenizer();

        subject.setFolder(folder);

        var actual = subject.getFileTokenizer();
        assertNotNull(actual);

    }
}