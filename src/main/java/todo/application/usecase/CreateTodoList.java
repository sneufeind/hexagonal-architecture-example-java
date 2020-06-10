package todo.application.usecase;

import common.architecture.UseCase;
import todo.application.command.CreateTodoListCommand;
import todo.domain.exception.TodoListAlreadyExistsException;

@UseCase
public interface CreateTodoList {

    void createTodoList(CreateTodoListCommand command) throws TodoListAlreadyExistsException;
}
