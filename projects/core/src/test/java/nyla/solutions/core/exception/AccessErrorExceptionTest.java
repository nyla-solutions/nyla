package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccessErrorExceptionTest {

    @Test
    void messageMatch() {
        assertThat(new AccessErrorException("message").getMessage()).isEqualTo("message");
    }

    @Test
    void codeMatch() {
        assertThat(new AccessErrorException("error").getCode()).isEqualTo("ACCESS_ERROR");
    }
}