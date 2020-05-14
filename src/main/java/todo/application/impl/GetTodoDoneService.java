package todo.application.impl;

import io.hschwentner.dddbits.annotation.ApplicationService;
import todo.application.GetTodoDone;
import todo.domain.exception.TodoDoesNotExistException;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.TodoId;
import todo.domain.model.UserId;
import todo.domain.service.TodoService;

@ApplicationService
public class GetTodoDoneService implements GetTodoDone {

    private final TodoService todoService;

    public GetTodoDoneService(final TodoService todoService){
        this.todoService = todoService;
    }

    @Override
    public void getTodoDone(final UserId userId, final TodoId todoId) throws TodoDoesNotExistException, UserDoesNotExistException {
        // no need for orchestration here
        this.todoService.getTodoDone(userId, todoId);
    }
}
