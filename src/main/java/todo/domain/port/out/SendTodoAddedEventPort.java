package todo.domain.port.out;

import common.architecture.Port;
import todo.domain.event.TodoAddedEvent;

@Port
public interface SendTodoAddedEventPort {

    void send(TodoAddedEvent event);
}
