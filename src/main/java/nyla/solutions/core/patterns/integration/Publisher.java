package nyla.solutions.core.patterns.integration;

/**
 * @author gregory green
 * @param <MessageType> the message type to send
 */
public interface Publisher<MessageType>{
    void send(MessageType payload);
}
