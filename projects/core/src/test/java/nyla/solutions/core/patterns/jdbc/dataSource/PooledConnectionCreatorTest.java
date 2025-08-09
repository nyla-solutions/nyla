package nyla.solutions.core.patterns.jdbc.dataSource;

import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.patterns.pooling.Pool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PooledConnectionCreatorTest {

    private PooledConnectionCreator subject;

    @Mock
    private Creator<Connection> creator;

    @Mock
    private Pool<Connection> pool;

    @Mock
    private Connection connection;

    @BeforeEach
    void setUp() {
        subject = new PooledConnectionCreator(creator,pool);
    }


    @Test
    void create_throws() {

        assertThrows(IllegalArgumentException.class, () -> subject.create());

    }

    @Test
    void create() {

        when(creator.create()).thenReturn(connection);
        var actual = subject.create();
       assertThat(actual).isInstanceOf(PoolConnection.class);

    }
}