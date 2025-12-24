package nyla.solutions.core.patterns.memento;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileMementoTest
{
    private FileMemento subject;

    @BeforeEach
    void setUp() {
        subject = new FileMemento(IO.tempDir(),".memento");
    }

    @Test
    void getFileExtension_defaultExtension()
    {
        assertEquals(new FileMemento(".").getFileExtension(),".memento");
    }

    @Test
    void restore() throws IOException
    {
        String rootPath = "target/memento";
        IO.dir().mkdir(rootPath);

        String fileExtension = ".txt";
        FileMemento subject = new FileMemento(rootPath, fileExtension);
        String savePoint = "test";
        Class<?> clz = UserProfile.class;
        UserProfile expected = new UserProfile();
        String expectedEmail = "email";
        expected.setEmail(expectedEmail);
        subject.store(savePoint,expected);
        UserProfile actual = subject.restore(savePoint,clz);
        assertEquals(expected,actual);
    }

    @Test
    void storeRequiredException() {

        assertThrows( RequiredException.class, () -> subject.store("savePoint", null));
        assertThrows( RequiredException.class, () -> subject.store("", new UserProfile()));
    }

    @Test
    void restoreRequiredException() {

        assertThrows( RequiredException.class, () -> subject.restore("savePoint", null));
        assertThrows( RequiredException.class, () -> subject.restore(null, UserProfile.class));
    }

    @Test
    void getRootPath()
    {

        String expected = "hello";
        assertEquals(expected, new FileMemento(expected).getRootPath());
    }

    @Test
    void getFileExtension()
    {
        String expected ="expected";
        assertEquals(expected, new FileMemento("",expected).getFileExtension());
    }
}