package nyla.solutions.core.patterns.integration;

import java.util.function.Consumer;

/**
 * @author Gregory Green
 */
@FunctionalInterface
public interface Subscriber<T> extends Consumer<T> {
    void receive(T payload);

    @Override
    default void accept(T payload) {
        receive(payload);
    }
}
