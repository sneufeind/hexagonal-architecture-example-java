package todo.domain.model;

import io.hschwentner.dddbits.annotation.ValueObject;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@ValueObject
@Value
public class UserId {

    private final UUID id;

    private UserId(@NonNull final UUID id){
        this.id = id;
    }

    public static UserId create(){
        return new UserId(UUID.randomUUID());
    }
}
