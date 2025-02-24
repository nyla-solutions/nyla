package nyla.solutions.core.patterns.jdbc.batch;

import nyla.solutions.core.exception.DataException;
import nyla.solutions.core.patterns.batch.BatchReport;
import nyla.solutions.core.patterns.jdbc.BindVariableInterpreter;
import nyla.solutions.core.patterns.jdbc.ResultSetToMapConverter;
import nyla.solutions.core.patterns.jdbc.Sql;
import nyla.solutions.core.patterns.jdbc.SqlTest;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JdbcBatchTest
{
    @Test
    void executeFromConnection() throws SQLException
    {
        String sourceDdl = "CREATE TABLE SRC_Persons (\n" +
                "    PersonID int,\n" +
                "    LastName varchar(255)\n" +
                ");";



        //Set source
        String sourceSelectSql = "select * FROM SRC_Persons";
        Connection sourceConnection = SqlTest.openConnection();
        Statement sourceStatement = sourceConnection.createStatement();
        sourceStatement.execute(sourceDdl);
        String sourceInsertSql = "insert into SRC_Persons(PersonID,LastName) values (1, 'Test');";
        sourceStatement.execute(sourceInsertSql);
        ResultSet sourceResultSet = sourceStatement.executeQuery(sourceSelectSql);

        //Setup target
        String targetDdl = "CREATE TABLE TARGET_Persons (\n" +
                "    PersonID int,\n" +
                "    LastName varchar(255)\n" +
                ");";
        Connection targetConnection = SqlTest.openConnection();
        Statement targetStatement = targetConnection.createStatement();
        targetStatement.execute(targetDdl);

        ResultSetSupplier resultSetSupplier = new ResultSetSupplier(sourceResultSet);

        String targetBindInsert = "insert into TARGET_Persons(PersonID,LastName) values (:PersonID, :LastName)";
        BindVariableInterpreter bindVariableInterpreter = new BindVariableInterpreter(targetBindInsert);

        PreparedStatementMapConsumer preparedStatementMapConsumer
                = new PreparedStatementMapConsumer(bindVariableInterpreter,
                 () ->
                 {
                     try {
                         return targetConnection.prepareStatement(bindVariableInterpreter.toPreparedStmtSql());
                     }
                     catch (SQLException throwables) {
                         throw new DataException(throwables.getMessage(),throwables);
                     }
                 }
                 );


        int batchChunkSize = 2;

        JdbcBatch jdbcBatch = new JdbcBatch(resultSetSupplier,
                new ResultSetToMapConverter(),
                preparedStatementMapConsumer,
                batchChunkSize);


        BatchReport report= jdbcBatch.execute();
        assertNotNull(report);
        assertEquals(1,report.countInput());
        assertEquals(1,report.countOutput());

         Sql sql = new Sql();
         long actualTargetCount = sql.queryForColumn(targetConnection,"select count(*) from TARGET_Persons",1, Long.class);

         assertEquals(1,actualTargetCount);




        targetStatement.close();
        targetConnection.close();
        sourceStatement.close();
        sourceConnection.close();

    }


    @Test
    void execute() throws SQLException
    {
        int batchChunkSize = 100;
        ResultSetSupplier resultSetSupplier = mock(ResultSetSupplier.class);
        ResultSet resultSet= mock(ResultSet.class);

        when(resultSetSupplier.get()).thenReturn(resultSet).thenReturn(null);
        ResultSetToMapConverter resultSetToMapConverter = mock(ResultSetToMapConverter.class);
        Map<String, ?> map = mock(Map.class);
        when(resultSetToMapConverter.convert(any())).thenReturn((Map)map).thenReturn(null);
        PreparedStatementMapConsumer  preparedStatementMapConsumer = mock(PreparedStatementMapConsumer.class);

        JdbcBatch jdbcBatch = new JdbcBatch(resultSetSupplier,
                resultSetToMapConverter,
                preparedStatementMapConsumer,
                batchChunkSize);

        BatchReport report = jdbcBatch.execute();

        verify(resultSetSupplier,atLeast(1)).get();
        verify(resultSetToMapConverter).convert(any());
        verify(preparedStatementMapConsumer).accept(any());


        assertNotNull(report);
    }

}