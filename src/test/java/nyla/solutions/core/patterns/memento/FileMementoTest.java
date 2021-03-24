package nyla.solutions.core.patterns.memento;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileMementoTest
{
    @Test
    void getFileExtension_defaultExtension()
    {
        assertEquals(new FileMemento(".").getFileExtension(),".memento");
    }

    @Test
    void restore() throws IOException
    {
        String rootPath = "target/memento";
        IO.mkdir(rootPath);

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