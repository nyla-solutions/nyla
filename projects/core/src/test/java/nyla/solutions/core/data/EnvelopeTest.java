package nyla.solutions.core.data;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static nyla.solutions.core.util.Organizer.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnvelopeTest {

    @Test
    void getHeader() {

        Map<Object, Object> expected = toMap("k1","v1");
        var subject = new Envelope<String>();

        subject.setHeader(expected);
        assertEquals(expected, subject.getHeader());
    }

    @Test
    void getHeader_from_header() {

        Map<Object, Object> header = toMap("k1","v1");
        String payload = "payload";
        var subject = new Envelope<String>(header,payload);

        subject.setHeader(header);
        assertEquals(header, subject.getHeader());
        assertEquals(payload, subject.getPayload());
    }

    @Test
    void getPayload() {
        String expected = "payload";
        var subject = new Envelope<String>();

        subject.setPayload(expected);

        assertEquals(expected, subject.getPayload());

    }

    @Test
    void setPayload() {
    }


}