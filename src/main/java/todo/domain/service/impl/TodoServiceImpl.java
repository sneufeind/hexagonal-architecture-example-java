package todo.domain.service.impl;

import io.hschwentner.dddbits.annotation.DomainService;
import lombok.NonNull;
import todo.domain.event.TodoAddedEvent;
import todo.domain.event.TodoDoneEvent;
import todo.domain.event.TodoListCreatedEvent;
import todo.domain.exception.MaxNumberOfTodosExceedException;
import todo.domain.exception.TodoAlreadyExistsException;
import todo.domain.exception.TodoDoesNotExistException;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.Todo;
import todo.domain.model.TodoId;
import todo.domain.model.TodoList;
import todo.domain.model.UserId;
import todo.domain.port.TodoListRepsitory;
import todo.domain.service.TodoService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@DomainService
public class TodoServiceImpl implements TodoService {

    private static final int MAX_NUMBER_OF_TODOS = 5;

    private final Map<UserId, List<Todo>> undoneTodoRepository = new ConcurrentHashMap<>();
    private final TodoListRepsitory todoListRepository;

    public TodoServiceImpl(final TodoListRepsitory todoListRepository){
        this.todoListRepository = todoListRepository;
        initUndoneTodoRepository();
    }

    private void initUndoneTodoRepository(){
        this.todoListRepository.findAll().forEach(list -> {
            if( !this.undoneTodoRepository.containsKey(list.getUserId()) )
                this.undoneTodoRepository.put(list.getUserId(), new LinkedList<>());
            this.undoneTodoRepository.get(list.getUserId()).addAll(list.undoneTodos());
        });
    }

    @Override
    public void initTodoList(final UserId userId) {
        final Optional<TodoList> todoListOpt = this.todoListRepository.findById(userId);
        if(todoListOpt.isEmpty()) {
            final TodoList todoList = TodoList.create(userId);
            this.todoListRepository.save(todoList);
            publishTodoListCreatedEvent(new TodoListCreatedEvent(userId, todoList));
        }
    }

    @Override
    public TodoId addTodo(final UserId userId, final String description) throws MaxNumberOfTodosExceedException, TodoAlreadyExistsException, UserDoesNotExistException {
        final TodoList todoList = todoList(userId);
        checkIfMaxNumberOfUndoneTodosExceeded(todoList.countUndoneTodos());

        final Todo newTodo = Todo.create(description);
        todoList.addTodo(newTodo);
        this.todoListRepository.save(todoList);
        publishTodoAddedEvent( new TodoAddedEvent(newTodo, userId) );

        return newTodo.getId();
    }

    @Override
    public void getTodoDone(final UserId userId, final TodoId todoId) throws TodoDoesNotExistException, UserDoesNotExistException {
        final TodoList todoList = todoList(userId);
        todoList.getTodoDone(todoId);
        this.todoListRepository.save(todoList);
        publishTodoDoneEvent(new TodoDoneEvent(userId, todoId) );
    }

    @Override
    public List<Todo> undoneTodos(@NonNull final UserId userId) {
        return this.undoneTodoRepository.getOrDefault(userId, Collections.emptyList());
    }

    private TodoList todoList(final UserId userId) throws UserDoesNotExistException {
        final Optional<TodoList> opt = this.todoListRepository.findById(userId);
        if( opt.isEmpty() )
            throw new UserDoesNotExistException(userId);
        return opt.get();
    }

    private static void checkIfMaxNumberOfUndoneTodosExceeded(final int numberOfUndoneTodos) throws MaxNumberOfTodosExceedException {
        if( numberOfUndoneTodos >= MAX_NUMBER_OF_TODOS )
            throw new MaxNumberOfTodosExceedException(MAX_NUMBER_OF_TODOS);
    }

    private void publishTodoListCreatedEvent(final TodoListCreatedEvent event) {
        receiveTodoListCreatedEvent(event); // TODO use Event Bus here
    }
    private void receiveTodoListCreatedEvent(final TodoListCreatedEvent event) {
        this.undoneTodoRepository.put(event.getUserId(), new ArrayList<>());
    }

    private void publishTodoAddedEvent(final TodoAddedEvent event) {
        receiveTodoAddedEvent(event); // TODO use Event Bus here
    }
    private void receiveTodoAddedEvent(final TodoAddedEvent event) {
        this.undoneTodoRepository.get(event.getUserId()).add(event.getTodo());
    }

    private void publishTodoDoneEvent(final TodoDoneEvent event) {
        receiveTodoDoneEvent(event); // TODO use Event Bus here
    }
    private void receiveTodoDoneEvent(final TodoDoneEvent event) {
        final List<Todo> todos = this.undoneTodoRepository.get(event.getUserId());
        this.undoneTodoRepository.put(event.getUserId(), todos.stream()
                .filter(t -> !t.getId().equals(event.getTodoId()) )
                .collect(Collectors.toList()) );
    }
}
