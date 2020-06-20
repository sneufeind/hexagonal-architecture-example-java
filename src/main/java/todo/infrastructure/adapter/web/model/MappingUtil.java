package todo.infrastructure.adapter.web.model;

import todo.domain.model.TodoId;
import todo.domain.model.UserId;

import java.util.UUID;

public class MappingUtil {

    public static UUID uuid(final String id){
        return UUID.fromString(id);
    }

    public static UserId userId(final String id){
        return UserId.of(uuid(id));
    }

    public static TodoId todoId(String id) {
        return TodoId.of(uuid(id));
    }
}
