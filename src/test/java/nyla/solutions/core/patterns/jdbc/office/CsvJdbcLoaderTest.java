package nyla.solutions.core.patterns.jdbc.office;

import nyla.solutions.core.patterns.creational.Creator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CsvJdbcLoaderTest
{

    private Reader noHeaderReader;
    private Reader headerReader;
    private String sql ="insert into test1(col1, col2) values(?,?)";
    private CsvJdbcLoader subject;
    private PreparedStatement preparedStatement;

    @BeforeEach
    void setUp() throws IOException
    {
        noHeaderReader = new FileReader("src/test/resources/csv/csv_db_test.csv");
        headerReader = new FileReader("src/test/resources/csv/csv_db_test_header.csv");
        preparedStatement = mock(PreparedStatement.class);
        subject = new CsvJdbcLoader();
    }

    @Test
    void loadCreator() throws SQLException, IOException
    {
        Connection connection = mock(Connection.class);
        Creator<Connection> creator = mock(Creator.class);
        when(creator.create()).thenReturn(connection);
        when(connection.prepareStatement(sql)).thenReturn(preparedStatement);

        subject.load(creator, noHeaderReader,sql,false);
        verify(creator).create();
        verify(connection).prepareStatement(sql);
    }

    @Test
    void load() throws IOException, SQLException
    {

        int expected =  3;
        //when(preparedStatement.executeUpdate()).thenReturn(expected);


        long actual = subject.load(preparedStatement, noHeaderReader,sql,false);
        //verify(preparedStatement,times(3)).execute();
        assertEquals(expected,actual);

    }

    @Test
    void load_headerRow() throws IOException, SQLException
    {

        int[] expectedReturn = {2};
        when(preparedStatement.executeBatch()).thenReturn(expectedReturn);

        long actual = subject.load(preparedStatement, headerReader,sql,true);
        //verify(preparedStatement,times(2)).execute();
        long expected =  2;
        assertEquals(expected,actual);

    }
}