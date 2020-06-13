package todo.domain.port;

import common.architecture.Port;
import todo.domain.event.TodoAddedEvent;

@Port
public interface SendTodoAddedEventPort {

    void send(TodoAddedEvent event);
}
