package common.eventbus.impl;

import com.google.common.eventbus.EventBus;
import common.eventbus.EventPublisher;

public class EventPublisherImpl<E> implements EventPublisher<E> {

    private final EventBus eventBus;

    public EventPublisherImpl(final EventBus eventBus){
        this.eventBus = eventBus;
    }

    @Override
    public void publish(final E event) {
        this.eventBus.post(event);
    }
}
