package todo.infrastructure.adapter.events;

import com.google.common.eventbus.EventBus;
import common.architecture.Adapter;
import common.eventbus.impl.EventPublisherImpl;
import todo.domain.event.TodoAddedEvent;
import todo.domain.port.SendTodoAddedEventPort;

@Adapter
public class SendTodoAddedEventGuavaAdapter extends EventPublisherImpl<TodoAddedEvent> implements SendTodoAddedEventPort {

    public SendTodoAddedEventGuavaAdapter(final EventBus eventBus){
        super(eventBus);
    }

    @Override
    public void send(final TodoAddedEvent event) {
        publish(event);
    }
}
