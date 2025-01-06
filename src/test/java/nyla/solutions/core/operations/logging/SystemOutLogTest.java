package nyla.solutions.core.operations.logging;

import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SystemOutLogTest {

    @Mock
    private PrintStream out;
    @Mock
    private PrintStream error;

    private SystemOutLog subject;

    @BeforeEach
    void setUp() {
        subject = new SystemOutLog(out,error);
    }

    @Test
    void setLoggingClass() {

        subject.setLoggingClass(UserProfile.class);

        assertEquals(UserProfile.class.getName(), subject.getLoggingClassName());

    }

    @Test
    void debug() {
        subject.debug("expected");

        verify(out).println(anyString());
    }

    @Test
    void debug_withException() {
        subject.debug("expected",new SystemException());

        verify(out).println(anyString());
    }

    @Test
    void info() {
        subject.info("expected");

        verify(out).println(anyString());
    }

    @Test
    void error() {
        subject.error("expected");

        verify(error).println(anyString());
    }

    @Test
    void error_withException() {
        subject.error("expected",new SystemException());

        verify(error).println(anyString());
    }

    @Test
    void fatal() {
        subject.fatal("expected");

        verify(error).println(anyString());
    }

    @Test
    void fatal_with_Exception() {
        subject.fatal("expected",new SystemException());

        verify(error).println(anyString());
    }

    @Test
    void warn() {
        subject.warn("expected");

        verify(out).println(anyString());
    }

    @Test
    void warn_with_exception() {
        subject.warn("expected",new SystemException());

        verify(out).println(anyString());
    }
}