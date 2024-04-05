package nyla.solutions.core.patterns.jdbc;

import nyla.solutions.core.patterns.jdbc.dataSource.ConnectionCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nyla.solutions.core.util.Config.settings;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Junit test for ConnectionCreator
 * @author gregory green
 */
class ConnectionCreatorTest {

    private ConnectionCreator subject;

    private String driver = org.h2.Driver.class.getName();
    private String user;
    private char[] password;
    private String connectionURL;

    @BeforeEach
    void setUp() {

        driver = settings().getProperty("test.sql.driver","org.h2.Driver");
        connectionURL = settings().getProperty("test.sql.connectionURL");
        user = settings().getProperty("test.sql.user");
        password = settings().getPropertyPassword("test.sql.password","");
        subject = ConnectionCreator.builder()
                .driver(driver)
                .url(connectionURL)
                .user(user)
                .password(password)
                .build();

    }

    @Test
    void constructFromSettings(){
        var builder = ConnectionCreator.builder().constructFromSettings();

        assertNotNull(builder);
        assertNotNull(builder.build());
    }

    @Test
    void create() {
        assertNotNull(subject.create());
    }
}