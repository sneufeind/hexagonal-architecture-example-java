package todo.application.service;

import io.hschwentner.dddbits.annotation.ApplicationService;
import lombok.NonNull;
import todo.domain.command.AddTodoCommand;
import todo.application.usecase.AddTodo;
import todo.domain.event.TodoAddedEvent;
import todo.domain.exception.MaxNumberOfTodosExceedException;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.Todo;
import todo.domain.model.TodoId;
import todo.domain.model.TodoList;
import todo.domain.model.UserId;
import todo.domain.port.LoadTodoListPort;
import todo.domain.port.SaveTodoListPort;

import java.util.Optional;

@ApplicationService
public class TodoAddImpl implements AddTodo {

    private static final int MAX_NUMBER_OF_TODOS = 5; // application specific rule

    private final LoadTodoListPort loadTodoListPort;
    private final SaveTodoListPort saveTodoListPort;

    public TodoAddImpl(final LoadTodoListPort loadTodoListPort, final SaveTodoListPort saveTodoListPort) {
        this.loadTodoListPort = loadTodoListPort;
        this.saveTodoListPort = saveTodoListPort;
    }

    @Override
    public TodoId addTodo(@NonNull final AddTodoCommand command) throws MaxNumberOfTodosExceedException, UserDoesNotExistException {
        final TodoList todoList = todoList(command.getUserId());
        checkIfMaxNumberOfUndoneTodosExceeded(todoList.countUndoneTodos());

        final Todo newTodo = Todo.create(command.getDescripton());
        todoList.addTodo(newTodo);

        this.saveTodoListPort.save(todoList);
        publishTodoAddedEvent( new TodoAddedEvent(newTodo, command.getUserId()) );

        return newTodo.getId();
    }

    private TodoList todoList(final UserId userId) throws UserDoesNotExistException {
        final Optional<TodoList> opt = this.loadTodoListPort.findById(userId);
        if (opt.isEmpty()) {
            throw new UserDoesNotExistException(userId);
        }
        return opt.get();
    }

    private void publishTodoAddedEvent(final TodoAddedEvent event) {
        // TODO use Event Bus here
    }

    private static void checkIfMaxNumberOfUndoneTodosExceeded(final int numberOfUndoneTodos) throws MaxNumberOfTodosExceedException {
        if( numberOfUndoneTodos >= MAX_NUMBER_OF_TODOS )
            throw new MaxNumberOfTodosExceedException(MAX_NUMBER_OF_TODOS);
    }
}
