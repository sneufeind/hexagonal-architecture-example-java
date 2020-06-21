package todo.adapter.out.events;

import com.google.common.eventbus.EventBus;
import common.architecture.Adapter;
import common.eventbus.impl.EventPublisherImpl;
import todo.domain.event.TodoListCreatedEvent;
import todo.domain.port.SendTodoListCreatedEventPort;

@Adapter
public class SendTodoListCreatedEventGuavaAdapter extends EventPublisherImpl<TodoListCreatedEvent> implements SendTodoListCreatedEventPort {

    public SendTodoListCreatedEventGuavaAdapter(final EventBus eventBus){
        super(eventBus);
    }

    @Override
    public void send(final TodoListCreatedEvent event) {
        publish(event);
    }
}
