package nyla.solutions.core.patterns.functions;

import java.util.Queue;
import java.util.function.Supplier;

public class PollQueueSupplier<T> implements Supplier<T> {

    private final Queue<T> queue;

    public PollQueueSupplier(Queue<T> queue) {
        this.queue = queue;
    }

    @Override
    public T get() {
        return queue.poll();
    }
}
