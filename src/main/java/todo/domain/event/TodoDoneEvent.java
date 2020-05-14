package todo.domain.event;

import io.hschwentner.dddbits.annotation.DomainEvent;
import lombok.Value;
import todo.domain.model.TodoId;
import todo.domain.model.UserId;

@DomainEvent
@Value
public class TodoDoneEvent {

    private final UserId userId;
    private final TodoId todoId;
}
