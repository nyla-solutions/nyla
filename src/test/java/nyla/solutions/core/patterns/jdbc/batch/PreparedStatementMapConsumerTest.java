package nyla.solutions.core.patterns.jdbc.batch;

import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.patterns.jdbc.BindVariableInterpreter;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PreparedStatementMapConsumerTest
{

    @Test
    void accept() throws SQLException
    {

        BindVariableInterpreter bindVariableInterpreter = mock(BindVariableInterpreter.class);
        Creator<PreparedStatement>  preparedStatementCreator = mock(Creator.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(preparedStatementCreator.create()).thenReturn(preparedStatement);

       PreparedStatementMapConsumer subject = new PreparedStatementMapConsumer(bindVariableInterpreter,preparedStatementCreator);
        Map<String, Object> map = new HashMap<>();
        map.put("vint",Integer.valueOf(1));
        map.put("vtext","hello");
        map.put("vdate", LocalDateTime.now());
        map.put("volddate", Calendar.getInstance().getTime());
        map.put("vdouble", 2.23);

        List<Map<String, ?>> list = Arrays.asList(map);
        subject.accept(list);

        verify(bindVariableInterpreter,atLeast(map.keySet().size())).indexOf(anyString());
        verify(preparedStatementCreator).create();
        verify(preparedStatement,atLeast(map.keySet().size())).setObject(anyInt(),any());
        verify(preparedStatement).addBatch();
        verify(preparedStatement).executeBatch();

    }

    @Test
    void close() throws SQLException
    {
        BindVariableInterpreter bindVariableInterpreter = mock(BindVariableInterpreter.class);
        Creator<PreparedStatement>  preparedStatementCreator = mock(Creator.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(preparedStatementCreator.create()).thenReturn(preparedStatement);
        PreparedStatementMapConsumer subject = new PreparedStatementMapConsumer(bindVariableInterpreter,preparedStatementCreator);

        subject.close();
        verify(preparedStatement).close();
    }
}