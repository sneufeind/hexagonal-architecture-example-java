package todo.domain.port;

import common.architecture.Port;
import todo.domain.event.TodoListCreatedEvent;

@Port
public interface ReceiveTodoListCreatedEventPort {

    void receive(TodoListCreatedEvent event);
}
