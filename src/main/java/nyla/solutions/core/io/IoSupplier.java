package nyla.solutions.core.io;

import java.io.IOException;

public interface IoSupplier <T> {

        /**
         * Gets a result.
         *
         * @return a result
         */
        T get() throws IOException;
}
