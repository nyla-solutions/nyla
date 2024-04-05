package nyla.solutions.core.patterns.jdbc.dataSource;

import nyla.solutions.core.patterns.creational.Creator;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Simple implementation of a JDBC Data Service
 *
 * @author gregory green
 */
public class ConnectionDataSource extends AbstractDataSource {
    private final Creator<Connection> creator;

    public ConnectionDataSource(){
        this(ConnectionCreator.builder().constructFromSettings().build());
    }
    public ConnectionDataSource(ConnectionCreator creator)
    {
        this.creator = creator;
    }

    public ConnectionDataSource(Creator<Connection> creator) {
        this.creator = creator;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return creator.create();
    }
}
