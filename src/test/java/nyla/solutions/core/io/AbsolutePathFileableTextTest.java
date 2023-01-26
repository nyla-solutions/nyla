package nyla.solutions.core.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for AbsolutePathFileableText
 * @author gregory green
 */
class AbsolutePathFileableTextTest {

    private Fileable fileable ;
    private File file;

    @BeforeEach
    void setUp() {
        fileable = mock(Fileable.class);
        file = mock(File.class);
    }

    @Test
    void getText() {
        String expected = "file";
        when(fileable.getFile()).thenReturn(file);
        when(file.getAbsolutePath()).thenReturn(expected);

        var subject = new AbsolutePathFileableText();

        subject.setFileable(fileable);

        assertEquals(expected, subject.getText());
    }

    @Test
    void getFileable() {
        var subject = new AbsolutePathFileableText();

        subject.setFileable(fileable);
        assertEquals(fileable, subject.getFileable());
    }


}