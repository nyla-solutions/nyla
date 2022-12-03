package nyla.solutions.core.io.converter;

import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConversionFileAuthorTest {
    private File file;
    private Converter<UserProfile, String> toHeader;
    private Converter<UserProfile, String> toRow;
    private ConversionFileAuthor<UserProfile> subject;
    private UserProfile userProfile = JavaBeanGeneratorCreator.of(UserProfile.class).create();

    @BeforeEach
    void setUp() {
        file = mock(File.class);
        toHeader = mock(Converter.class);
        toRow = mock(Converter.class);
        subject = new ConversionFileAuthor<UserProfile>(file,toHeader,toRow);
    }

    @Test
    void given_fileDoesNotExist_when_appendFile_then_File_created_withHeader() throws IOException {

        when(file.exists()).thenReturn(false);
        subject.appendFile(userProfile);

        verify(toHeader).convert(any());

    }

    @Test
    void given_fileDoesExist_when_appendFile_then_File_created_withOutHeader() throws IOException {

        when(file.exists()).thenReturn(true);
        subject.appendFile(userProfile);

        verify(toHeader,never()).convert(any());
    }

    @Test
    void given_file_when_appendFile_then_convertRow() throws IOException {

        when(file.exists()).thenReturn(true);
        subject.appendFile(userProfile);

        verify(toRow).convert(any());
    }
}