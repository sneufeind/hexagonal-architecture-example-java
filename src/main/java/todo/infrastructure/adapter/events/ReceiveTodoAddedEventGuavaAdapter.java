package todo.infrastructure.adapter.events;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import common.architecture.Adapter;
import common.eventbus.impl.AbstractEventReceiverImpl;
import todo.domain.event.TodoAddedEvent;
import todo.domain.port.ReceiveTodoAddedEventPort;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.function.Consumer;

@Adapter
public class ReceiveTodoAddedEventGuavaAdapter extends AbstractEventReceiverImpl<TodoAddedEvent> implements ReceiveTodoAddedEventPort {

    private final Consumer<TodoAddedEvent> eventConsumer;

    public ReceiveTodoAddedEventGuavaAdapter(final EventBus eventBus, final Consumer<TodoAddedEvent> eventConsumer){
        super(eventBus);
        this.eventConsumer = eventConsumer;
        subscribeAfterInit(); // TODO should be solved via annotations -> use spring
    }

    @PostConstruct
    private void subscribeAfterInit(){
        subscribe();
    }

    @PreDestroy
    private void unsubscribeBeforeDestroy(){
        unsubscribe();
    }

    @Subscribe
    @Override
    public void receive(final TodoAddedEvent event) {
        this.eventConsumer.accept(event);
    }
}
