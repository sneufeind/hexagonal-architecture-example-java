package todo.domain.event;

import io.hschwentner.dddbits.annotation.DomainEvent;
import lombok.Value;
import todo.domain.model.Todo;
import todo.domain.model.UserId;

@DomainEvent
@Value
public class TodoAddedEvent {

    private final Todo todo;
    private final UserId userId;
}
