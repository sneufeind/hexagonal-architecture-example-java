package todo.application.usecase;

import common.architecture.UseCase;
import todo.domain.exception.MaxNumberOfTodosExceedException;
import todo.domain.exception.TodoAlreadyExistsException;
import todo.domain.model.TodoId;
import todo.domain.model.UserId;

@UseCase
public interface AddTodo {

    TodoId addTodo(UserId userId, String description) throws TodoAlreadyExistsException, MaxNumberOfTodosExceedException;
}
