package todo.application.service;

import io.hschwentner.dddbits.annotation.ApplicationService;
import lombok.NonNull;
import todo.domain.command.ReadTodosCommand;
import todo.application.usecase.ReadingTodos;
import todo.domain.event.TodoAddedEvent;
import todo.domain.event.TodoDoneEvent;
import todo.domain.event.TodoListCreatedEvent;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.Todo;
import todo.domain.model.UserId;
import todo.domain.port.LoadTodoListPort;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationService
public class ReadingTodosImpl implements ReadingTodos {

    private final Map<UserId, List<Todo>> undoneTodoRepository = new ConcurrentHashMap<>();

    private final LoadTodoListPort loadTodoListPort;

    public ReadingTodosImpl(final LoadTodoListPort loadTodoListPort){
        this.loadTodoListPort = loadTodoListPort;
        initUndoneTodoRepository(); // init state
    }

    private void initUndoneTodoRepository(){
        this.loadTodoListPort.findAll().forEach(list ->
                this.undoneTodoRepository.put(list.getUserId(), list.undoneTodos())
        );
    }

    @Override
    public List<Todo> getAllUndoneTodos(@NonNull final ReadTodosCommand command) throws UserDoesNotExistException {
        initUndoneTodoRepository(); // TODO remove this when using an event bus
        if( !this.undoneTodoRepository.containsKey(command.getUserId()) ) {
            throw new UserDoesNotExistException(command.getUserId());
        }
        return Collections.unmodifiableList(this.undoneTodoRepository.get(command.getUserId()));
    }

    public void on(final TodoAddedEvent event) {
        this.undoneTodoRepository.get(event.getUserId()).add(event.getTodo());
    }

    public void on(final TodoDoneEvent event) {
        final List<Todo> todos = this.undoneTodoRepository.get(event.getUserId());
        this.undoneTodoRepository.put(event.getUserId(), todos.stream()
                .filter(t -> !t.getId().equals(event.getTodoId()) )
                .collect(Collectors.toList()) );
    }

    public void on(final TodoListCreatedEvent event) {
        this.undoneTodoRepository.put(event.getUserId(), new LinkedList<>());
    }
}
