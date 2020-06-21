package todo.adapter.out.events;

import com.google.common.eventbus.EventBus;
import common.architecture.Adapter;
import common.eventbus.impl.EventPublisherImpl;
import todo.domain.event.TodoDoneEvent;
import todo.domain.port.SendTodoDoneEventPort;

@Adapter
public class SendTodoDoneEventGuavaAdapter extends EventPublisherImpl<TodoDoneEvent> implements SendTodoDoneEventPort {

    public SendTodoDoneEventGuavaAdapter(final EventBus eventBus){
        super(eventBus);
    }

    @Override
    public void send(final TodoDoneEvent event) {
        publish(event);
    }
}
