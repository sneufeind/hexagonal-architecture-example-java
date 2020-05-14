package todo.domain.model;

import io.hschwentner.dddbits.annotation.DomainEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

@DomainEntity
@Data
public class Todo {

    private final TodoId id;
    private final String description;
    @Setter(AccessLevel.PRIVATE)
    private boolean isDone;

    public Todo(final String description){
        this(TodoId.create(), description);
    }

    public Todo(@NonNull final TodoId id, @NonNull final String description){
        this.id = id;
        this.description = description;
        this.isDone = false;
    }

    public static Todo create(final String description){
        return new Todo(TodoId.create(), description);
    }

    public void done(){
        setDone(true);
    }
}
