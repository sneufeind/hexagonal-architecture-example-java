package todo.domain.port.out;

import common.architecture.Port;
import todo.domain.model.TodoList;

@Port
public interface SaveTodoListPort {

    void save(TodoList list);
}