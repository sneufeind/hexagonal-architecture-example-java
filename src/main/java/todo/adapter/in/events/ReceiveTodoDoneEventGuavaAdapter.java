package todo.adapter.in.events;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import common.architecture.Adapter;
import common.eventbus.impl.AbstractEventReceiverImpl;
import todo.domain.event.TodoDoneEvent;
import todo.domain.port.in.ReceiveTodoDoneEventPort;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.function.Consumer;

@Adapter
public class ReceiveTodoDoneEventGuavaAdapter extends AbstractEventReceiverImpl<TodoDoneEvent> implements ReceiveTodoDoneEventPort {

    private final Consumer<TodoDoneEvent> eventConsumer;

    public ReceiveTodoDoneEventGuavaAdapter(final EventBus eventBus, final Consumer<TodoDoneEvent> eventConsumer){
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
    public void receive(final TodoDoneEvent event) {
        this.eventConsumer.accept(event);
    }
}
