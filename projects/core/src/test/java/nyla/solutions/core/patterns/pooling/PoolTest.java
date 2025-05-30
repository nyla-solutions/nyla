package nyla.solutions.core.patterns.pooling;

import nyla.solutions.core.patterns.creational.Creator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PoolTest {

    private Pool<Connection> subject;
    private int size = 2;
    @Mock
    private Queue queue = new ConcurrentLinkedQueue();
    private Creator<Connection> creator;
    @Mock
    private Connection connection;

    @BeforeEach
    void setUp() {
        creator = () -> connection;

        subject = new Pool(size,creator);
    }

    @Test
    void acquire() {

        Connection actual = subject.acquire();
        assertNotNull(actual);

        subject.release(actual);

    }
}