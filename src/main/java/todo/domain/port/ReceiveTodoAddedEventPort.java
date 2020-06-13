package todo.domain.port;

import common.architecture.Port;
import todo.domain.event.TodoAddedEvent;

@Port
public interface ReceiveTodoAddedEventPort {

    void receive(TodoAddedEvent event);
}
