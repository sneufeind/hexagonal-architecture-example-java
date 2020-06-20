package todo.infrastructure.adapter.web.model;

import lombok.Data;
import todo.domain.model.UserId;

@Data
public class CreateTodolistRequest {

    private String userId;

    public UserId toUserId(){
        return MappingUtil.userId(this.userId);
    }
}
