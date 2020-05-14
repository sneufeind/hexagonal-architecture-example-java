package todo.application.impl;

import io.hschwentner.dddbits.annotation.ApplicationService;
import todo.application.AddTodo;
import todo.domain.exception.MaxNumberOfTodosExceedException;
import todo.domain.exception.TodoAlreadyExistsException;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.TodoId;
import todo.domain.model.UserId;
import todo.domain.service.TodoService;

@ApplicationService
public class TodoAddService implements AddTodo {

    private final TodoService todoService;

    public TodoAddService(final TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    public TodoId addTodo(final UserId userId, final String description) throws MaxNumberOfTodosExceedException, TodoAlreadyExistsException {
        // no need for orchestration here
        try {
            return this.todoService.addTodo(userId, description);
        } catch (final UserDoesNotExistException e) {
            this.todoService.initTodoList(userId);
            return addTodo(userId, description);
        }
    }

}
