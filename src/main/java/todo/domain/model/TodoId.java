package todo.domain.model;

import io.hschwentner.dddbits.annotation.ValueObject;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@ValueObject
@Value
public class TodoId {

    private final UUID id;

    private TodoId(@NonNull final UUID id){
        this.id = id;
    }

    public static TodoId create(){
        return new TodoId(UUID.randomUUID());
    }

}
