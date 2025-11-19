package nyla.solutions.core.exception;

/**
 * Access error exception
 * @author Gregory Green
 */
public class AccessErrorException extends SecurityException{

    public AccessErrorException(String message) {
        super(message);
        setCode("ACCESS_ERROR");
    }
}
