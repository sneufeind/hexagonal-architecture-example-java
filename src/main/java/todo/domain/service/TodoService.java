package todo.domain.service;

import todo.domain.exception.MaxNumberOfTodosExceedException;
import todo.domain.exception.TodoAlreadyExistsException;
import todo.domain.exception.TodoDoesNotExistException;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.Todo;
import todo.domain.model.TodoId;
import todo.domain.model.UserId;

import java.util.List;

public interface TodoService {

    void initTodoList(UserId userId);

    TodoId addTodo(UserId userId, String description) throws TodoAlreadyExistsException, MaxNumberOfTodosExceedException, UserDoesNotExistException;

    void getTodoDone(UserId userId, TodoId todoId) throws TodoDoesNotExistException, UserDoesNotExistException;

    List<Todo> undoneTodos(UserId userId);

}
