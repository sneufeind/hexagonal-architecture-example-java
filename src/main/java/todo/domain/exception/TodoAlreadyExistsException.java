package todo.domain.exception;

import todo.domain.model.TodoId;

public class TodoAlreadyExistsException extends Exception {

    public TodoAlreadyExistsException(final TodoId id) {
        super(String.format("Todo with id='%s' already exists!", id.getId().toString()));
    }
}
