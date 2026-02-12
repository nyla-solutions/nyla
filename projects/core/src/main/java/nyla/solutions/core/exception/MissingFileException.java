package nyla.solutions.core.exception;

import java.io.FileNotFoundException;

/**
 * @author Gregory Green
 */
public class MissingFileException extends IoException {
    public MissingFileException(FileNotFoundException e) {
        super(e);
    }

    public MissingFileException(String message) {
        super(message);
    }
}
