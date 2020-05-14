package todo.application;

import common.architecture.UseCase;
import todo.domain.model.Todo;
import todo.domain.model.UserId;

import java.util.List;

@UseCase
public interface ReadingTodos {

    List<Todo> getAllUndoneTodos(UserId userId);
}
