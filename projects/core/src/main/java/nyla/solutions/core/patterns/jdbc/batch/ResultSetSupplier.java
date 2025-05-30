package nyla.solutions.core.patterns.jdbc.batch;

import nyla.solutions.core.exception.DataException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public class ResultSetSupplier implements Supplier<ResultSet>, AutoCloseable
{
    private final ResultSet resultSet;

    public ResultSetSupplier(ResultSet resultSet)
    {
        this.resultSet = resultSet;
    }


    @Override
    public ResultSet get()
    {
        try {
            if(resultSet.next())
                return resultSet;

            return null;
        }
        catch (SQLException e) {
            throw new DataException("ERROR: "+ e.getMessage(),e);
        }
    }

    public void close()
    {

        try {
            resultSet.close();
        }
        catch (SQLException e) {
            throw new DataException(e.getMessage(),e);
        }
    }
}
