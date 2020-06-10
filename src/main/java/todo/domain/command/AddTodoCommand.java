package todo.domain.command;

import common.architecture.Command;
import lombok.NonNull;
import lombok.Value;
import todo.domain.model.UserId;

@Command
@Value(staticConstructor = "of")
public class AddTodoCommand {

    @NonNull UserId userId;
    @NonNull String descripton;
}
