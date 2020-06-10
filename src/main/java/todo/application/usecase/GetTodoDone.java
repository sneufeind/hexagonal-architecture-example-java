package todo.application.usecase;

import common.architecture.UseCase;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.TodoId;
import todo.domain.model.UserId;

@UseCase
public interface GetTodoDone {

    void getTodoDone(UserId userId, TodoId todoId) throws UserDoesNotExistException;
}
