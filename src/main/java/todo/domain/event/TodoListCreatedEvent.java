package todo.domain.event;

import io.hschwentner.dddbits.annotation.DomainEvent;
import lombok.Value;
import todo.domain.model.TodoList;
import todo.domain.model.UserId;

@DomainEvent
@Value
public class TodoListCreatedEvent {

    private final UserId userId;
    private final TodoList todoList;
}
