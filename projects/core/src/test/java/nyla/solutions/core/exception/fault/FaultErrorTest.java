package nyla.solutions.core.exception.fault;

import nyla.solutions.core.exception.SetupException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FaultErrorTest {

    private FaultError subject;
    private String errorCode = "001";
    private String errorCategory = "CAT";

    @BeforeEach
    void setUp() {
        subject = new FaultError(errorCode, errorCategory);
    }

    @Test
    void getCategory() {
        assertEquals(errorCategory, subject.getCategory());
    }

    @Test
    void getErrorCode() {
        assertEquals(errorCode, subject.getCode());
    }

    @Test
    void setErrorCode() {
        String expectedCode = "012";
        subject.setCode(expectedCode);
        assertEquals(expectedCode, subject.getCode());
    }

    @Test
    void setErrorCategory() {
        String expectedCategory = "ERR1";
        subject.setCategory(expectedCategory);
        assertEquals(expectedCategory, subject.getCategory());
    }
    @Test
    void setErrorCategory_throws_SetupException() {
        assertThrows(SetupException.class,() -> subject.setCategory("12323232323232"));
    }

    @Test
    @DisplayName("Given too long error code when Set Error Code then Throw Exception")
    void setErrorCode_throws_SetupException() {
        assertThrows(SetupException.class,() -> subject.setCode("233232323232"));
    }
}