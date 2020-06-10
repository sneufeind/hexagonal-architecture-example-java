package todo.application.usecase;

import common.architecture.UseCase;
import todo.domain.command.ReadTodosCommand;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.Todo;

import java.util.List;

@UseCase
public interface ReadingTodos {

    List<Todo> getAllUndoneTodos(ReadTodosCommand command) throws UserDoesNotExistException;
}
