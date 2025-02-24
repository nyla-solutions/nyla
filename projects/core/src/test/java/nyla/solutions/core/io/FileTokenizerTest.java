package nyla.solutions.core.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileTokenizerTest {

    private File file;
    private String separateChars = "||";
    private String fileName = "text-1.txt";

    @BeforeEach
    void setUp() {
        file = mock(File.class);
    }

    @Test
    void getFile() {
        when(file.getName()).thenReturn(fileName);

        var subject = new FileTokenizer(file);
        assertEquals(file, subject.getFile());
    }

    @Test
    void setFile() {
        when(file.getName()).thenReturn(fileName);

        var subject = new FileTokenizer();
        subject.setFile(file);
        assertEquals(file, subject.getFile());
    }

    @Test
    void getSeparatorChars() {
        var subject = new FileTokenizer();
        subject.setSeparatorChars(separateChars);
        assertEquals(separateChars, subject.getSeparatorChars());
    }

    @Test
    void getFileNameTokens() {

        when(file.getName()).thenReturn(fileName);

        var subject = new FileTokenizer();
        subject.setFile(file);

        var actual = subject.getFileNameTokens();

        assertThat(actual).contains("text-1");
        assertThat(actual).contains("txt");

    }

    @Test
    void getName() {
        when(file.getName()).thenReturn(fileName);

        var subject = new FileTokenizer();
        subject.setFile(file);

        assertEquals(fileName, subject.getName());
    }
}