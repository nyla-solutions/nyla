package nyla.solutions.core.data.clock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimingAuditMappableTest {

    private TimingAuditMappable<String, String> subject;

    @BeforeEach
    void setUp() {
        subject = new TimingAuditMappable<String,String>();
    }

    @Test
    void getKey() {

        String expected = "Hello";
        subject.setKey(expected);
        var actual = subject.getKey();

        assertEquals(expected, actual);
    }

    @Test
    void getName() {
        String expected = "Hello Name";
        subject.setName(expected);
        var actual = subject.getName();

        assertEquals(expected, actual);
    }

    @Test
    void getTime() {

        Time expected = new Time();
        subject.setTime(expected);
        var actual = subject.getTime();

        assertEquals(expected, actual);
    }

    @Test
    void getValue() {
        String expected = "value";
        subject.setValue(expected);
        var actual = subject.getValue();

        assertEquals(expected, actual);
    }

    @Test
    void getFrom() {
        String expected = "From";
        subject.setFrom(expected);
        var actual = subject.getFrom();

        assertEquals(expected, actual);
    }


    @Test
    void getTo() {
        String expected = "To";
        subject.setTo(expected);
        var actual = subject.getTo();

        assertEquals(expected, actual);
    }

    @Test
    void getId() {
        String expected = "Id";
        subject.setId(expected);
        var actual = subject.getId();

        assertEquals(expected, actual);
    }


    @Test
    void getSystem() {
        String expected = "System";
        subject.setSystem(expected);
        var actual = subject.getSystem();

        assertEquals(expected, actual);
    }


    @Test
    void getText() {
        String expected = "Text";
        subject.setText(expected);
        var actual = subject.getText();

        assertEquals(expected, actual);
    }

    @Test
    void getHost() {
        String expected = "Host";
        subject.setHost(expected);
        var actual = subject.getHost();

        assertEquals(expected, actual);
    }


    @Test
    void getProcessId() {
        int expected = 1;//"ProcessId";
        subject.setProcessId(expected);
        var actual = subject.getProcessId();

        assertEquals(expected, actual);
    }


    @Test
    void getOperation() {
        String expected = "Operation";
        subject.setOperation(expected);
        var actual = subject.getOperation();

        assertEquals(expected, actual);
    }


    @Test
    void getDataName() {
        String expected = "DataName";
        subject.setDataName(expected);
        var actual = subject.getDataName();

        assertEquals(expected, actual);
    }

    @Test
    void setDataName() {
    }
}