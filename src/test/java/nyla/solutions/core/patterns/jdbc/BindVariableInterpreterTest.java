package nyla.solutions.core.patterns.jdbc;

import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BindVariableInterpreterTest
{
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

    //
}