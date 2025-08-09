package nyla.solutions.core.patterns.jdbc.dataSource;

import nyla.solutions.core.patterns.pooling.Pool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PoolConnectionTest {

    private PoolConnection subject;
    @Mock
    private Connection connection;
    @Mock
    private Pool<Connection> pool;

    @BeforeEach
    void setUp() {
        subject = new PoolConnection(connection,pool);
    }

    @Test
    void close() throws SQLException {
        subject.close();

        verify(pool).release(connection);

    }
}