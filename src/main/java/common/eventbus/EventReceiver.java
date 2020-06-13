package common.eventbus;

public interface EventReceiver<E> {

    void receive(E event);

    void subscribe();

    void unsubscribe();
}
