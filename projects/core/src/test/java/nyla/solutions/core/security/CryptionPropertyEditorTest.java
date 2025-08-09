package nyla.solutions.core.security;

import nyla.solutions.core.util.Cryption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Junit test for CryptionPropertyEditor
 * @author gregory green
 */
class CryptionPropertyEditorTest {

    private Cryption cryption;
    private CryptionPropertyEditor subject;

    @BeforeEach
    void setUp() {
        cryption = mock(Cryption.class);
        subject = new CryptionPropertyEditor(cryption);
    }


    @DisplayName("Given Cryption and Encryption value WHEN getValue THEN Return_DecryptedValue")
    @Test
    void given_cryptionAndEncryptionValue_when_GetValue_Then_Return_DecryptedValue() {

        var subject = new CryptionPropertyEditor(cryption);

        String expected = "plainSecret";
        subject.setValue(expected);


        when(cryption.interpretText(anyString())).thenReturn(expected);

        var actual = subject.getValue();

        assertEquals(expected, actual);
    }
}