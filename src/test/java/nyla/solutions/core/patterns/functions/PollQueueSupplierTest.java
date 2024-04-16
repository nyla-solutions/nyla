package nyla.solutions.core.patterns.functions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PollQueueSupplierTest {

    @Mock
    private Queue queue;
    private PollQueueSupplier<String> subject;

    @BeforeEach
    void setUp() {
        subject = new PollQueueSupplier<>(queue);
    }

    @Test
    void get() {
        String expected = "hello";

        when(queue.poll()).thenReturn(expected);

        var actual = subject.get();


        assertEquals(expected, actual);
    }
}