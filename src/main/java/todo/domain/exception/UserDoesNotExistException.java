package todo.domain.exception;

import todo.domain.model.UserId;

public class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException(final UserId id) {
        super(String.format("User with id='%s' does not exist!", id.getId().toString()));
    }
}
