package nyla.solutions.core.patterns.jdbc;

import nyla.solutions.core.util.Organizer;
import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for BindVariableInterpreter
 * @author Gregory Green
 */
class BindVariableInterpreterTest
{
    @Test
    void given_preparedStatementWithNAmeValue_WhenSetObject_ThenPreparedStatementSetObectCalled() throws SQLException
    {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        String sql= "update test_tbl set test1 = :test1, :test2";
        BindVariableInterpreter subject = new BindVariableInterpreter(sql);


        subject.setObject(preparedStatement,"test1","value");


        verify(preparedStatement).setObject(anyInt(),any());

    }

    @Test
    void given_nullPreparedStatementWithNAmeValue_WhenSetObject_ThenPreparedStatementSetObectNeverCalled() throws SQLException
    {

        String sql= "update test_tbl set test1 = :test1, :test2";
        BindVariableInterpreter subject = new BindVariableInterpreter(sql);



        assertDoesNotThrow(() -> {
            subject.setObject(null,"test1","value");
        });





    }

    @Test
    void indexOf()
    {
        String sql= "update test_tbl set test1 = :test1, :test2";
        BindVariableInterpreter bindVariableStatement = new BindVariableInterpreter(sql);
        Map<String,?> map = null;
        assertEquals(1,bindVariableStatement.indexOf("test1"));
        assertEquals(2,bindVariableStatement.indexOf("test2"));

    }

    @Test
    void indexesOf()
    {
        String sql= "insert into test_tbl(test1,test2) values (:test1, :test2) on conflict update set test1 = :test1, :test2";
        BindVariableInterpreter bindVariableStatement = new BindVariableInterpreter(sql);
        Map<String,?> map = null;
        List<Integer> expected = Organizer.change().toList(2,4);
        assertEquals(expected,bindVariableStatement.indexesOf("test2"));

    }

    @Test
    void indexOf_case_insensitive()
    {
        String sql= "delete from test_tbl where v1 = :v1 and v2 = :v2 or v3 = :v3";
        BindVariableInterpreter bindVariableStatement = new BindVariableInterpreter(sql);
        Map<String,?> map = null;
        assertEquals(1,bindVariableStatement.indexOf("V1"));
        assertEquals(2,bindVariableStatement.indexOf("V2"));
        assertEquals(3,bindVariableStatement.indexOf("V3"));

    }

    @Test
    void indexOf_given_invalidArg_Then_ReturnNullIndex()
    {
        String sql= "delete from test_tbl where v1 = :v1 and v2 = :v2 or v3 = :v3";
        BindVariableInterpreter bindVariableStatement = new BindVariableInterpreter(sql);
        Map<String,?> map = null;
        assertNull(bindVariableStatement.indexOfNullable("V4"));
    }

    @Test
    void formatStoredVariableName()
    {
        assertEquals("V1",BindVariableInterpreter.formatStoredVariableName("v1"));
        assertEquals("V1",BindVariableInterpreter.formatStoredVariableName(":v1"));
        assertEquals("V1_1",BindVariableInterpreter.formatStoredVariableName(":V1_1"));
        assertEquals("V1",BindVariableInterpreter.formatStoredVariableName("v1,"));
        assertEquals("V1",BindVariableInterpreter.formatStoredVariableName(" v1 "));
    }



    @Test
    void toPreparedStmtSql()
    {
        String sql= "update test_tbl set test1 = :test1, :test2";
        BindVariableInterpreter bindVariableStatement = new BindVariableInterpreter(sql);
        String actual = bindVariableStatement.toPreparedStmtSql();

       assertThat(actual).doesNotContain(":test1");
        assertThat(actual).doesNotContain(":test2");

        assertThat(actual).contains("?");
        assertEquals(2,Text.characterCount('?',actual));

    }

    @Test
    void toPreparedStmtSql_for_insert()
    {
        String sql= "insert into TARGET_Persons(PersonID,LastName) values (:PersonID,:LastName)";
        BindVariableInterpreter bindVariableStatement = new BindVariableInterpreter(sql);
        String actual = bindVariableStatement.toPreparedStmtSql();

        assertThat(actual).isEqualTo("insert into TARGET_Persons(PersonID,LastName) values (?,?)");

    }

    @Test
    void constructPreparedStatementWithMap() throws SQLException
    {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        Connection connection = mock(Connection.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        String sql= "insert into TARGET_Persons(email,firstName) values (:email,:firstName)";
        BindVariableInterpreter bindVariableStatement = new BindVariableInterpreter(sql);

        Map<?, ?> map = Organizer.change().toMap("email","imain@nyla.com","firstName","Josiah","lastName","Imani");

        PreparedStatement actual = bindVariableStatement.constructPreparedStatementWithMap(connection,map);

        assertNotNull(actual);
        verify(preparedStatement,times(2)).setObject(anyInt(),any());



    }

    @Test
    void constructPreparedStatementWithMap_When_repeatedVariable_Then_setMultipleTimes() throws SQLException
    {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        Connection connection = mock(Connection.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        String sql= "insert into TARGET_Persons(email,email) values (:email,:firstName) on conflict set UPDATE email =  :email";
        BindVariableInterpreter bindVariableStatement = new BindVariableInterpreter(sql);

        String preparedSql = bindVariableStatement.toPreparedStmtSql();
        System.out.println(preparedSql);
        assertEquals(3,Text.characterCount('?',preparedSql));

        Map<?, ?> map = Organizer.change().toMap("email","imain@nyla.com","firstName","Josiah","lastName","Imani");

        PreparedStatement actual = bindVariableStatement.constructPreparedStatementWithMap(connection,map);

        assertNotNull(actual);
        verify(preparedStatement,times(3)).setObject(anyInt(),any());



    }

    @Test
    void constructPreparedStatementWithMap_When_ExceptionThrown_Then_ClosePrepareStatement() throws SQLException
    {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        Connection connection = mock(Connection.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.doThrow(new SQLException("Test")).when(preparedStatement).setObject(anyInt(),any());

        String sql= "insert into TARGET_Persons(email,firstName) values (:email,:firstName)";
        BindVariableInterpreter bindVariableStatement = new BindVariableInterpreter(sql);

        Map<?, ?> map = Organizer.change().toMap("email","imain@nyla.com","firstName","Josiah","lastName","Imani");
        try {
            PreparedStatement actual = bindVariableStatement.constructPreparedStatementWithMap(connection,map);
            fail("Must throw exception");
        }
        catch (SQLException e)
        {
            verify(preparedStatement).close();
        }



    }

}