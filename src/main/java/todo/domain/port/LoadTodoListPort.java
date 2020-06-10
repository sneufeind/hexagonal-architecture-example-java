package todo.domain.port;

import java.util.Collection;
import java.util.Optional;

import common.architecture.Port;
import todo.domain.model.TodoList;
import todo.domain.model.UserId;

@Port
public interface LoadTodoListPort {

    Collection<TodoList> findAll();

    Optional<TodoList> findById(UserId id);
}