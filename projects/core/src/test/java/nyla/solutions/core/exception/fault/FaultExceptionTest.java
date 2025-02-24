package nyla.solutions.core.exception.fault;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FaultExceptionTest {

    private String message = "error";
    private Throwable e = new Exception(message);

    @Test
    void stackTrace() {
        assertNotNull(FaultException.stackTrace(e));
    }



    @Test
    void getOperation() {
    }

    @Test
    void setOperation() {
    }

    @Test
    void getCategory() {
    }

    @Test
    void getCode() {
    }

    @Test
    void getModule() {
    }

    @Test
    void setModule() {
    }

    @Test
    void setCategory() {
    }

    @Test
    void setCode() {
    }

    @Test
    void getNotes() {
    }

    @Test
    void setNotes() {
    }

    @Test
    void copy() {
    }

    @Test
    void formatMessage() {
    }

    @Test
    void setMessage() {
    }

    @Test
    void getArgument() {
    }

    @Test
    void setArgument() {
    }

    @Test
    void getMessage() {
    }

    @Test
    void getErrorStackTrace() {
    }

    @Test
    void getArgumentId() {
    }

    @Test
    void setArgumentId() {
    }

    @Test
    void getArgumentType() {
    }

    @Test
    void setArgumentType() {
    }

    @Test
    void getSource() {
    }

    @Test
    void setSource() {
    }
}