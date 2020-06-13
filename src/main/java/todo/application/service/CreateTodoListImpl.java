package todo.application.service;

import io.hschwentner.dddbits.annotation.ApplicationService;
import lombok.NonNull;
import todo.domain.command.CreateTodoListCommand;
import todo.application.usecase.CreateTodoList;
import todo.domain.event.TodoListCreatedEvent;
import todo.domain.exception.TodoListAlreadyExistsException;
import todo.domain.model.TodoList;
import todo.domain.model.UserId;
import todo.domain.port.LoadTodoListPort;
import todo.domain.port.SaveTodoListPort;
import todo.domain.port.SendTodoListCreatedEventPort;

@ApplicationService
public class CreateTodoListImpl implements CreateTodoList {

    private final LoadTodoListPort loadTodoListPort;
    private final SaveTodoListPort saveTodoListPort;
    private final SendTodoListCreatedEventPort sendTodoListCreatedEventPort;

    public CreateTodoListImpl(final LoadTodoListPort loadTodoListPort, final SaveTodoListPort saveTodoListPort, final SendTodoListCreatedEventPort sendTodoListCreatedEventPort){
        this.loadTodoListPort = loadTodoListPort;
        this.saveTodoListPort = saveTodoListPort;
        this.sendTodoListCreatedEventPort = sendTodoListCreatedEventPort;
    }

    @Override
    public void createTodoList(@NonNull final CreateTodoListCommand command) throws TodoListAlreadyExistsException {
        checkIfUserHasNoTodoListYet(command.getUserId());

        final TodoList newTodoList = TodoList.create(command.getUserId());

        this.saveTodoListPort.save(newTodoList);
        publishTodoListCreatedEvent(new TodoListCreatedEvent(command.getUserId()));
    }

    private void checkIfUserHasNoTodoListYet(@NonNull final UserId userId) throws TodoListAlreadyExistsException {
        if (this.loadTodoListPort.findById(userId).isPresent()) {
            throw new TodoListAlreadyExistsException(userId);
        }
    }

    private void publishTodoListCreatedEvent(final TodoListCreatedEvent event) {
        this.sendTodoListCreatedEventPort.send(event);
    }
}
