package todo.domain.port.in;

import common.architecture.Port;
import todo.domain.event.TodoAddedEvent;

@Port
public interface ReceiveTodoAddedEventPort {

    void receive(TodoAddedEvent event);
}
