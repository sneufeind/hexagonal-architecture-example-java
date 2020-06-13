package todo.infrastructure.adapter.events;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import common.architecture.Adapter;
import common.eventbus.impl.AbstractEventReceiverImpl;
import todo.domain.event.TodoDoneEvent;
import todo.domain.event.TodoListCreatedEvent;
import todo.domain.port.ReceiveTodoDoneEventPort;
import todo.domain.port.ReceiveTodoListCreatedEventPort;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.function.Consumer;

@Adapter
public class ReceiveTodoListCreatedEventGuavaAdapter extends AbstractEventReceiverImpl<TodoListCreatedEvent> implements ReceiveTodoListCreatedEventPort {

    private final Consumer<TodoListCreatedEvent> eventConsumer;

    public ReceiveTodoListCreatedEventGuavaAdapter(final EventBus eventBus, final Consumer<TodoListCreatedEvent> eventConsumer){
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
    public void receive(final TodoListCreatedEvent event) {
        this.eventConsumer.accept(event);
    }
}
