package nyla.solutions.core.patterns.integration;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class SubscriberTest {

    @Test
    void defaultAccept_calls_receive() {
        AtomicReference<String> holder = new AtomicReference<>();

        Subscriber<String> sub = new Subscriber<>() {
            @Override
            public void receive(String payload) {
                holder.set(payload);
            }
        };

        // call accept (default method) should delegate to receive
        sub.accept("hello");
        assertEquals("hello", holder.get());

        // call receive directly
        sub.receive("direct");
        assertEquals("direct", holder.get());
    }

    @Test
    void lambdaSubscriber_works_as_functional_interface_and_as_consumer() {
        AtomicReference<Integer> holder = new AtomicReference<>();

        // Subscriber is a functional interface (receive method implemented by lambda)
        Subscriber<Integer> sub = payload -> holder.set(payload);

        // call accept through Consumer reference
        Consumer<Integer> consumer = sub;
        consumer.accept(42);
        assertEquals(Integer.valueOf(42), holder.get());

        // call receive directly
        sub.receive(7);
        assertEquals(Integer.valueOf(7), holder.get());
    }

    @Test
    void null_payload_is_allowed_and_passed_through() {
        AtomicReference<Object> holder = new AtomicReference<>("init");

        Subscriber<Object> sub = holder::set; // method reference to set the value

        sub.accept(null);
        assertNull(holder.get());

        sub.receive("not-null");
        assertEquals("not-null", holder.get());
    }
}