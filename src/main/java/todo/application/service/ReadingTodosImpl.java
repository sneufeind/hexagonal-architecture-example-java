package todo.application.service;

import io.hschwentner.dddbits.annotation.ApplicationService;
import todo.application.usecase.ReadingTodos;
import todo.domain.event.TodoAddedEvent;
import todo.domain.event.TodoDoneEvent;
import todo.domain.event.TodoListCreatedEvent;
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
    public List<Todo> getAllUndoneTodos(final UserId userId) {
        initUndoneTodoRepository(); // TODO remove this when using an event bus
        return Collections.unmodifiableList(this.undoneTodoRepository.get(userId));
    }

    // TODO subscribe to an eventbus
    private void receiveTodoAddedEvent(final TodoAddedEvent event) {
        this.undoneTodoRepository.get(event.getUserId()).add(event.getTodo());
    }

    // TODO subscribe to an eventbus
    private void receiveTodoDoneEvent(final TodoDoneEvent event) {
        final List<Todo> todos = this.undoneTodoRepository.get(event.getUserId());
        this.undoneTodoRepository.put(event.getUserId(), todos.stream()
                .filter(t -> !t.getId().equals(event.getTodoId()) )
                .collect(Collectors.toList()) );
    }

    // TODO subscribe to an eventbus
    private void receiveTodoListCreatedEvent(final TodoListCreatedEvent event) {
        this.undoneTodoRepository.put(event.getUserId(), new LinkedList<>());
    }
}
