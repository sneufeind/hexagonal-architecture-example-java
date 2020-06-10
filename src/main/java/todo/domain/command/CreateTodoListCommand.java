package todo.domain.command;

import common.architecture.Command;
import lombok.NonNull;
import lombok.Value;
import todo.domain.model.UserId;

@Command
@Value(staticConstructor = "of")
public class CreateTodoListCommand {

    @NonNull UserId userId;
}
