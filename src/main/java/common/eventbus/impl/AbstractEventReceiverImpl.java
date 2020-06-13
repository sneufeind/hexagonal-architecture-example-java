package common.eventbus.impl;

import com.google.common.eventbus.EventBus;
import common.eventbus.EventReceiver;

public abstract class AbstractEventReceiverImpl<E> implements EventReceiver<E> {

    private final EventBus eventBus;

    protected AbstractEventReceiverImpl(final EventBus eventBus){
        this.eventBus = eventBus;
    }

    public abstract void receive(final E event);

    @Override
    public void subscribe() {
        this.eventBus.register(this);
    }

    @Override
    public void unsubscribe() {
        this.eventBus.unregister(this);
    }
}
