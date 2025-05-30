package nyla.solutions.core.patterns.jdbc.dataSource;

import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.patterns.pooling.Pool;

import java.sql.Connection;

/**
 * @author gregory Green
 */
public class PooledConnectionCreator implements Creator<Connection> {


    private final Creator<Connection> connectorCreator;
    private final Pool<Connection> pool;

    PooledConnectionCreator(Creator<Connection> connectorCreator,Pool<Connection> pool) {
        this.connectorCreator = connectorCreator;
        this.pool = pool;
    }

    @Override
    public Connection create() {
        return new PoolConnection(connectorCreator.create(),pool);
    }

}
