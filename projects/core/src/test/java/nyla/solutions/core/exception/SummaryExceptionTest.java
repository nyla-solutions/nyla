package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SummaryExceptionTest {


    private Exception exception;
    private String message = "Hello world";

    @Test
    void constructor_with_msg() {

        var subject = new SummaryException(message);

        assertEquals(message, subject.getMessage());
    }

    @Test
    void constructor_with_exception() {

        var subject = new SummaryException(exception);

        assertEquals(exception, subject.getCause());
        assertEquals(exception, subject.getSummary().iterator().next());
    }

    @Test
    void constructor_with_message_exception() {

        var subject = new SummaryException(message,exception);

        assertEquals(message, subject.getMessage());
        assertEquals(exception, subject.getCause());
        assertEquals(exception, subject.getSummary().iterator().next());
    }


    @Test
    void getSummary() {

        var subject = new SummaryException();
        subject.addException(exception);
        assertEquals(exception, subject.getSummary().iterator().next());
    }

    @Test
    void setSummary() {
        var subject = new SummaryException();
        subject.setSummary(Collections.singleton(exception));

        assertEquals(exception, subject.getSummary().iterator().next());

    }

    @Test
    void isEmpty() {
        var subject = new SummaryException();
        assertTrue(subject.isEmpty());
    }

    @Test
    void size() {

        var subject = new SummaryException();
        subject.addException(exception);
        assertEquals(1, subject.size());
    }
}