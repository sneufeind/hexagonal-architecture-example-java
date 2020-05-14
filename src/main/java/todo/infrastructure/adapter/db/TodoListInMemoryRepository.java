package todo.infrastructure.adapter.db;

import common.architecture.Adapter;
import common.port.impl.AbstractInMemoryAdapter;
import lombok.NonNull;
import todo.domain.model.TodoList;
import todo.domain.model.UserId;
import todo.domain.port.TodoListRepsitory;

@Adapter
public class TodoListInMemoryRepository extends AbstractInMemoryAdapter<UserId, TodoList> implements TodoListRepsitory {

    public TodoListInMemoryRepository(){}

    @Override
    protected UserId extractId(@NonNull final TodoList list) {
        return list.getUserId();
    }

}
