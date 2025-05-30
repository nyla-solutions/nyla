package nyla.solutions.core.exception;

import nyla.solutions.core.exception.fault.FaultException;

public class CapacityException extends FaultException {
    public CapacityException(String message) {
        super(message);
    }

    public CapacityException() {
       this("Capacity Exception");
    }
}
