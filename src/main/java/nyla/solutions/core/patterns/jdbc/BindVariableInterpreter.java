package nyla.solutions.core.patterns.jdbc;

import nyla.solutions.core.util.Organizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Understands Bind Variables in SQL statements to use in a prepared statement
 *
 * Example: insert into TARGET_Persons(PersonID,LastName) values (:PersonID,:LastName)
 *
 * @author gregory green
 */
public class BindVariableInterpreter
{
    private final String sql;
    private final Map<String,List<Integer>> fieldPositionMap;
    private final static String BIND_VAR_EXP = "(\\:+([a-zA-Z0-9]*[\\_]*[a-zA-Z0-9]*)*)";

    public BindVariableInterpreter(String sql)
    {

        StringBuilder preparedSql = new StringBuilder();
        fieldPositionMap = new HashMap<>();

        Pattern pattern = Pattern.compile(BIND_VAR_EXP);
        Matcher matcher = pattern.matcher(sql);

        int i =1;
        String formattedVariable;
        List<Integer> indexes;
        while(matcher.find()) {
            String variable = matcher.group();
            formattedVariable = formatStoredVariableName(variable);
            indexes = fieldPositionMap.get(formattedVariable);
            if(indexes == null)
            {
                fieldPositionMap.put(formattedVariable, Organizer.toList(i++));
            }
            else
            {
                indexes.add(i++);
                fieldPositionMap.put(formattedVariable,indexes);
            }
        }

        this.sql = matcher.replaceAll("?");
    }

    protected static String formatStoredVariableName(String varName)
    {
        return varName.trim().replace(":","")
                      .toUpperCase().replace(",","");
    }

    /**
     * Get first index of the variable
     * @param variable the variable
     * @return the first index of the variable
     */
    public Integer indexOf(String variable)
    {
        if(variable == null)
            throw new NullPointerException("Variable name not provided");

        Integer position = indexOfNullable(variable);

        if(position == null)
            throw new NullPointerException("Variable: "+variable+" not found in variables:"+fieldPositionMap.keySet());

        return position;
    }

    /**
     *
     * @param variable the variable to get first index
     * @return the index
     */
    public Integer indexOfNullable(String variable)
    {

        List<Integer> results = indexesOf(variable);

        if(results == null || results.isEmpty())
            return null;

        return results.iterator().next();
    }

    /**
     * Get multiple indexes of the variable
     * @param variable the bind variable name
     * @return
     */
    public List<Integer> indexesOf(String variable)
    {
        if(variable == null)
            return null;

        return fieldPositionMap.get(variable.toUpperCase());
    }

    /**
     *
     * @return the SQL the bind variables to question marks
     */
    public String toPreparedStmtSql()
    {
        return sql;
    }

    /**
     *
     * @param connection the connection
     * @param map the key/value to set
     * @return the create and initialized prepared stateemtn
     */
    public PreparedStatement constructPreparedStatementWithMap(Connection connection, Map<?, ?> map)
    throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement(toPreparedStmtSql());

        List<Integer> indexes;
        Integer index = null;
        for (Map.Entry<?,?> entry: map.entrySet())
        {
            setObject(preparedStatement,String.valueOf(entry.getKey()),entry.getValue());
        }

        return preparedStatement;
    }

    /**
     * Set the object on the given preparedStatement
     * @param preparedStatement the prepared statement
     * @param name the variable name
     * @param value the value
     * @throws SQLException when an exception occurs
     */
    public void setObject(PreparedStatement preparedStatement, String name, Object value) throws SQLException
    {
        if(preparedStatement == null)
            return;

        Integer index = null;
        List<Integer>indexes = indexesOf(name);
        if(indexes == null || indexes.isEmpty())
            return;

        try {

            Iterator<Integer> iterator = indexes.iterator();
            while (iterator.hasNext()) {
                index = iterator.next();
                preparedStatement.setObject(index,value);
            }
        }
        catch (SQLException e)
        {
            try{ preparedStatement.close();} catch(Exception errorClosing){}
            throw new SQLException("Cannot set index:"+index+" in indexes:"+indexes+"  with value:"+value+" ERROR:"+e.getMessage(),e);
        }
    }


}
