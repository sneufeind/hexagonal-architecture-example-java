package todo.application.command;

import common.architecture.Command;
import lombok.NonNull;
import lombok.Value;
import todo.domain.model.TodoId;
import todo.domain.model.UserId;

@Command
@Value(staticConstructor = "of")
public class GetTodoDoneCommand {

    @NonNull UserId userId;
    @NonNull TodoId todoId;
}
