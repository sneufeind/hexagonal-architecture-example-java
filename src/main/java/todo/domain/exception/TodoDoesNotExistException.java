package todo.domain.exception;

import todo.domain.model.TodoId;

public class TodoDoesNotExistException extends Exception {

    public TodoDoesNotExistException(final TodoId id) {
        super(String.format("Todo with id='%s' already exists!", id.getId().toString()));
    }
}
