package nyla.solutions.core.exception;

/**
 * <pre>
 * SetupException exception occurred setup initialization error
 * </pre>
 *
 * @author Gregory Green
 * @version 1.0
 */
public class SetupException extends ConfigException {

    public static final String DEFAULT_ERROR_CODE = "SUP00";
    public static final String DEFAULT_ERROR_CATEGORY = "SETUP";
    public static final String SETUP_ERROR_MSG = "Set up exception";

    /**
     * Constructor for SetupException initializes internal
     * data settings.
     */
    public SetupException() {
        super(SETUP_ERROR_MSG);
        this.setCode(DEFAULT_ERROR_CODE);
        this.setCategory(DEFAULT_ERROR_CATEGORY);
    }

    /**
     * Constructor for SetupException initializes internal
     * data settings.
     *
     * @param message the error message
     */
    public SetupException(String message) {

        super(message);

        this.setCode(DEFAULT_ERROR_CODE);
        this.setCategory(DEFAULT_ERROR_CATEGORY);
    }


    /**
     * Constructor for SetupException initializes internal
     * data settings.
     *
     * @param exception the nested exception
     */
    public SetupException(Throwable exception) {

        super(exception);

        this.setCode(DEFAULT_ERROR_CODE);
        this.setCategory(DEFAULT_ERROR_CATEGORY);
    }

    /**
     * Constructor for SetupException initializes internal
     * data settings.
     *
     * @param message   the error message
     * @param exception the cause exception
     */
    public SetupException(String message, Throwable exception) {

        super(message, exception);

        this.setCode(DEFAULT_ERROR_CODE);
        this.setCategory(DEFAULT_ERROR_CATEGORY);

    }

    static final long serialVersionUID = SetupException.class.getName()
            .hashCode();
}
