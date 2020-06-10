package todo.application.usecase;

import common.architecture.UseCase;
import todo.application.command.AddTodoCommand;
import todo.domain.exception.MaxNumberOfTodosExceedException;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.TodoId;

@UseCase
public interface AddTodo {

    TodoId addTodo(AddTodoCommand command) throws MaxNumberOfTodosExceedException, UserDoesNotExistException;
}
