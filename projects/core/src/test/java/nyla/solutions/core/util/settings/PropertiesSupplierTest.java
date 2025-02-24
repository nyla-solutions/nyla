package nyla.solutions.core.util.settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PropertiesSupplierTest {

    private PropertiesSupplier subject;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("GIVEN url when get then load properties from URL")
    @Test
    void loadPropertiesUrl() throws IOException {
        var expected = "world";
        var expectedProp = "prop2";

        var path = Path.of("src/test/resources/config/url.properties").toFile().getAbsolutePath();
        var url = new URL("file://"+path).toString();

        subject = new PropertiesSupplier(url);

        var actual = subject.get();
        assertNotNull(actual);

        //prop2=world
        assertEquals(expected, actual.getProperty(expectedProp));
    }

    @DisplayName("GIVEN url when get then load properties from URL")
    @Test
    void loadPropertiesFile() throws IOException {
        var expected = "world";
        var expectedProp = "prop2";

        var path = "src/test/resources/config/url.properties";
        subject = new PropertiesSupplier(path);

        var actual = subject.get();
        assertNotNull(actual);

        //prop2=world
        assertEquals(expected, actual.getProperty(expectedProp));
    }

    @DisplayName("GIVEN path is empty WHEN get then load properties from URL")
    @Test
    void loadPropertiesClassPath() throws IOException {
        //application.name=Solutions.core
        var expected = "Solutions.core";
        var expectedProp = "application.name";

        subject = new PropertiesSupplier();

        var actual = subject.get();
        assertNotNull(actual);

        //prop2=world
        assertEquals(expected, actual.getProperty(expectedProp));
    }
}