package nyla.solutions.core.exception;

import java.io.IOException;

/**
 * @author Gregory Green
 */
public class IoException extends CommunicationException{
    public IoException(IOException e) {
        super(e);
    }

    public IoException(String message) {
        super(message);
    }

    public IoException(String message, Exception e) {
        super(message,e);
    }
}
