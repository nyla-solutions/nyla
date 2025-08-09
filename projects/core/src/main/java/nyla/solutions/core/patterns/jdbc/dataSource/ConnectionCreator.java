package nyla.solutions.core.patterns.jdbc.dataSource;

import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.patterns.jdbc.Sql;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.settings.Settings;

import java.sql.Connection;

/**
 * Creates new JDBC connections
 * @author gregory Green
 */
public class ConnectionCreator implements Creator<Connection> {

    private final String driver;
    private final String user;
    private final char[] password;
    private final String url;

    /**
     * Constructor
     * @param driver JDBC driver class
     * @param url JDBC connection URL
     * @param user JDBC user
     * @param password JDBC password
     */
    ConnectionCreator(String driver, String url, String user, char[] password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Connection create() {
        return  Sql.createConnection(driver,url,user,password);
    }



    public static class Builder {
        private static final String JDBC_USER = "SQL_JDBC_USER";
        private static final String JDBC_PASSWORD = "SQL_JDBC_PASSWORD";
        private static final String JDBC_URL = "SQL_JDBC_URL";
        private static final String JDBC_DRIVER = "SQL_JDBC_DRIVER";
        private String driver;
        private String url;
        private String user;
        private char[] password;

        public ConnectionCreator build() {
            return new ConnectionCreator(driver,url,user,password);
        }

        public Builder driver(String driver) {

            this.driver = driver;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder password(char[] password) {
            this.password = password;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder constructFromSettings()
        {
            return constructFromSettings(Config.settings());
        }
        public Builder constructFromSettings(Settings settings) {
            return
                    this.user(settings.getProperty(JDBC_USER,""))
                            .password(settings.getPropertyPassword(JDBC_PASSWORD,""))
                            .url(settings.getProperty(JDBC_URL))
                            .driver(settings.getProperty(JDBC_DRIVER));
        }
    }
}
