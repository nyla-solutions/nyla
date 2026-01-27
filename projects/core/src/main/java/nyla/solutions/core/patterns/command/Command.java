package nyla.solutions.core.patterns.command;

import java.util.function.Function;

/**
 * @author Gregory Green
 */
@FunctionalInterface
public interface Command<O,I> extends Function<I,O> {
    O execute(I input);

    default O apply(I input) {
        return execute(input);
    }
}
