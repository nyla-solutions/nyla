package nyla.solutions.core.patterns.jdbc;

import nyla.solutions.core.exception.CommunicationException;
import nyla.solutions.core.patterns.conversion.Converter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts to result sets to maps
 * @author Gregory Green
 */
public class ResultSetToMapConverter implements Converter<ResultSet, Map<String, ?>>
{

    @Override
    public Map<String, ?> convert(ResultSet resultSet)
    {
        ResultSetMetaData meta = null;
        try
        {
            meta = resultSet.getMetaData();

            int columnCount = meta.getColumnCount();

            String columnName;

            HashMap<String, Object> rowMap = new HashMap<>();

            for (int i = 0; i < columnCount; i++)
            {

                columnName = meta.getColumnName(i + 1);

                if (columnName == null)
                    columnName = "col" + (i+1);

                Object value = resultSet.getObject(i + 1);
                rowMap.put(columnName, value);
            }

            return rowMap;
        }
        catch (SQLException e)
        {
           throw new CommunicationException(e.getMessage(),e);
        }
    }
}
