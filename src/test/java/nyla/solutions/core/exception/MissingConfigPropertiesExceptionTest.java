package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MissingConfigPropertiesExceptionTest
{
    @Test
    void verifyMessage()
    {

        MissingConfigPropertiesException subject = new MissingConfigPropertiesException("key");
        String actual = subject.toString();

        assertThat(actual).contains("Config.loadArgs(String[] args)");
    }
}