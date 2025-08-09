package nyla.solutions.core.patterns.jdbc;

import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.patterns.jdbc.dataSource.ConnectionDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConnectionDataSourceTest {


    private ConnectionDataSource subject;
    @Mock
    private Connection expected;
    @Mock
    private Creator<Connection> creator;


    @BeforeEach
    void setUp() {
        subject = new ConnectionDataSource(creator);
    }

    @Test
    void creationConnection() throws SQLException {

        when(creator.create()).thenReturn(expected);

        var actual = subject.getConnection();

        assertThat(actual).isEqualTo(expected);

    }
}