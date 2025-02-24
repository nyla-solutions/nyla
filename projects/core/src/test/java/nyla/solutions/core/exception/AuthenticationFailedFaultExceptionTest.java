package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationFailedFaultExceptionTest {

    private String username = "imani";
    private Exception exception = new IllegalArgumentException("testing");

    @Test
    void getUsername() {

        var subject = new AuthenticationFailedFaultException(username);

        assertEquals(username, subject.getUsername());

        assertEquals(AuthenticationFailedFaultException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void construct() {
        var subject = new AuthenticationFailedFaultException(exception, username);

        assertEquals(exception, subject.getCause());
        assertEquals(AuthenticationFailedFaultException.DEFAULT_ERROR_CODE, subject.getCode());
    }
}