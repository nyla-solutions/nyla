package nyla.solutions.core.patterns.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultSetToMapConverterTest
{

    ResultSetToMapConverter resultSetToMapConverter;
    @Mock
    private ResultSet resultSet;
    @Mock
    private ResultSetMetaData metaData;

    @BeforeEach
    void setUp()
    throws SQLException
    {
        resultSetToMapConverter = new ResultSetToMapConverter();
    }

    @Test
    void test_convert()
    throws SQLException
    {
        String expectValue = "expected";
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(resultSet.getObject(anyString())).thenReturn(expectValue);
        when(resultSet.getObject(anyInt())).thenReturn(expectValue);


        when(metaData.getColumnCount()).thenReturn(1);
        String expectedCol = "name";
        when(metaData.getColumnName(anyInt())).thenReturn(expectedCol);

        Map<String,Object> expected = new HashMap<>();
        expected.put(expectedCol,expectValue);

        Map<String,?> actual = resultSetToMapConverter.convert(resultSet);
        assertEquals(expected,actual);

    }

    @Test
    void test_convert_withcolN()
    throws SQLException
    {
        String expectValue = "expected";
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(resultSet.getObject(anyString())).thenReturn(expectValue);
        when(resultSet.getObject(anyInt())).thenReturn(expectValue);


        when(metaData.getColumnCount()).thenReturn(1);
        String expectedCol = "col1";
        when(metaData.getColumnName(anyInt())).thenReturn(null);

        Map<String,Object> expected = new HashMap<>();
        expected.put(expectedCol,expectValue);

        Map<String,?> actual = resultSetToMapConverter.convert(resultSet);
        assertEquals(expected,actual);

    }
}