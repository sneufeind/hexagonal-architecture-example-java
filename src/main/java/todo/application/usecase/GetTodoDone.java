package todo.application.usecase;

import common.architecture.UseCase;
import todo.application.command.GetTodoDoneCommand;
import todo.domain.exception.UserDoesNotExistException;

@UseCase
public interface GetTodoDone {

    void getTodoDone(GetTodoDoneCommand command) throws UserDoesNotExistException;
}
