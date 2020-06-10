package todo.domain.exception;

import todo.domain.model.UserId;

public class TodoListAlreadyExistsException extends Exception {

    public TodoListAlreadyExistsException(final UserId userId) {
        super(String.format("Todo list for user with userId='%s' already exists!", userId.getId().toString()));
    }
}
