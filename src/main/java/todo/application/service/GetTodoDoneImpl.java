package todo.application.service;

import io.hschwentner.dddbits.annotation.ApplicationService;
import todo.application.usecase.GetTodoDone;
import todo.domain.command.GetTodoDoneCommand;
import todo.domain.event.TodoDoneEvent;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.TodoList;
import todo.domain.model.UserId;
import todo.domain.port.LoadTodoListPort;
import todo.domain.port.SaveTodoListPort;
import todo.domain.port.SendTodoDoneEventPort;

import java.util.Optional;

@ApplicationService
public class GetTodoDoneImpl implements GetTodoDone {

    private final LoadTodoListPort loadTodoListPort;
    private final SaveTodoListPort saveTodoListPort;
    private final SendTodoDoneEventPort sendTodoDoneEventPort;

    public GetTodoDoneImpl(final LoadTodoListPort loadTodoListPort, final SaveTodoListPort saveTodoListPort, final SendTodoDoneEventPort sendTodoDoneEventPort){
        this.loadTodoListPort = loadTodoListPort;
        this.saveTodoListPort = saveTodoListPort;
        this.sendTodoDoneEventPort = sendTodoDoneEventPort;
    }

    @Override
    public void getTodoDone(final GetTodoDoneCommand command) throws UserDoesNotExistException {
        final TodoList todoList = todoList(command.getUserId());

        todoList.getTodoDone(command.getTodoId());

        this.saveTodoListPort.save(todoList);
        publishTodoDoneEvent(new TodoDoneEvent(command.getUserId(), command.getTodoId()) );
    }

    private void publishTodoDoneEvent(final TodoDoneEvent event) {
        this.sendTodoDoneEventPort.send(event);
    }

    private TodoList todoList(final UserId userId) throws UserDoesNotExistException {
        final Optional<TodoList> opt = this.loadTodoListPort.findById(userId);
        if( opt.isEmpty() )
            throw new UserDoesNotExistException(userId);
        return opt.get();
    }
}
