package nyla.solutions.core.io;

import nyla.solutions.core.exception.IoException;

public interface IoSupplier <T> {

        /**
         * Gets a result.
         *
         * @return a result
         */
        T get() throws IoException;
}
