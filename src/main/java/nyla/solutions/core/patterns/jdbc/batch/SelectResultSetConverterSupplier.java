package nyla.solutions.core.patterns.jdbc.batch;

import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.exception.DataException;
import nyla.solutions.core.patterns.Connectable;
import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.security.user.data.UserProfile;

import java.sql.*;
import java.util.function.Supplier;

public class SelectResultSetConverterSupplier<T> implements Supplier<T>, Connectable
{
    private Connection connection;
    private final Supplier<Connection> connections;
    private final Converter<ResultSet, T> converter;
    private String sql;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private Object[] parameters;

    public SelectResultSetConverterSupplier(Supplier<Connection> connections, Converter<ResultSet, T> converter,
                                            String sql) throws SQLException
    {
        this(connections,converter,sql,null);
    }

    public SelectResultSetConverterSupplier(Supplier<Connection> connections, Converter<ResultSet, T> converter, String sql, Object[] parameters)
    {
        this.converter = converter;
        this.sql = sql;
        this.connections = connections;
    }


    @Override
    public T get()
    {
        if (resultSet == null)
            this.connect();

        try {
            if (!resultSet.next())
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
            this.statement = this.connection.prepareStatement(sql);

            if(sql.indexOf("?") > -1 && this.parameters != null && this.parameters.length > 0)
            {
                for (int i = 0; i < this.parameters.length; i++) {
                    this.statement.setObject(i+1,parameters[i]);
                }
            }

            this.resultSet = this.statement.executeQuery();
        }
        catch (SQLException e) {
            throw new DataException(e);
        }
    }

    @Override
    public void dispose()
    {
        if (this.connection != null) {
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

    public Object[] getParameters()
    {
        return parameters;
    }

    public void setParameters(Object... parameters)
    {
        this.parameters = parameters;
    }

    public String getSql()
    {
        return sql;
    }

    public void setSql(String sql)
    {
        this.sql = sql;
    }
}
