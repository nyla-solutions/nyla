package nyla.solutions.core.patterns.jdbc.batch;

import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.exception.DataException;
import nyla.solutions.core.patterns.Connectable;
import nyla.solutions.core.patterns.conversion.Converter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Supplier;

public class SelectResultSetConverterSupplier<T> implements Supplier<T>, Connectable
{
    private Connection connection;
    private final Supplier<Connection> connections;
    private final Converter<ResultSet, T> converter;
    private final String sql;
    private Statement statement;
    private ResultSet resultSet;

    public SelectResultSetConverterSupplier(Supplier<Connection> connections, Converter<ResultSet, T> converter,
                                            String sql) throws SQLException
    {

        this.converter = converter;
        this.sql = sql;
        this.connections = connections;

    }


    @Override
    public T get()
    {
        if(resultSet == null)
            this.connect();

        try {
            if(!resultSet.next())
                return null;

            return this.converter.convert(resultSet);

        }
        catch (SQLException e) {
            throw new DataException(e);
        }
    }

    @Override
    public void connect() throws ConnectionException
    {
        this.connection = connections.get();

        try {
            this.statement = this.connection.createStatement();
            this.resultSet = this.statement.executeQuery(sql);
        }
        catch (SQLException e) {
            throw new DataException(e);
        }

    }

    @Override
    public void dispose()
    {
        if(this.connection != null) {
            try {
                this.resultSet.close();
                this.statement.close();

            }
            catch (SQLException e) {
                throw new ConnectionException(e);
            }
            finally {
                try {
                    this.connection.close();
                }
                catch (SQLException e) {
                    throw new ConnectionException(e);
                }
            }


        }
        this.connection = null;
        this.statement = null;
        this.resultSet = null;
    }
}
