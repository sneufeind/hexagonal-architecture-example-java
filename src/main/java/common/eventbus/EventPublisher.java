package common.eventbus;

public interface EventPublisher<E> {

    void publish(E event);
}
