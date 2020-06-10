package todo.infrastructure.adapter.db;

import common.architecture.Adapter;
import todo.domain.model.TodoList;
import todo.domain.model.UserId;
import todo.domain.port.LoadTodoListPort;
import todo.domain.port.SaveTodoListPort;

import java.util.*;

@Adapter
public class TodoListListInMemoryRepository implements LoadTodoListPort, SaveTodoListPort {

    private final Map<UserId, TodoList> repository = new HashMap<>();

    public TodoListListInMemoryRepository(){}

    @Override
    public Collection<TodoList> findAll() {
        return Collections.unmodifiableCollection(this.repository.values());
    }

    @Override
    public Optional<TodoList> findById(final UserId id) {
        return Optional.ofNullable(this.repository.getOrDefault(id, null));
    }

    @Override
    public void save(final TodoList list) {
        this.repository.put(list.getUserId(), list);
    }
}
