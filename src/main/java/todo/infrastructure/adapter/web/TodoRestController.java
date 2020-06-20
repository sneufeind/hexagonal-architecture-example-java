package todo.infrastructure.adapter.web;

import common.architecture.Adapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.application.usecase.AddTodo;
import todo.application.usecase.CreateTodoList;
import todo.application.usecase.GetTodoDone;
import todo.application.usecase.ReadingTodos;
import todo.domain.command.AddTodoCommand;
import todo.domain.command.CreateTodoListCommand;
import todo.domain.command.GetTodoDoneCommand;
import todo.domain.command.ReadTodosCommand;
import todo.domain.exception.MaxNumberOfTodosExceedException;
import todo.domain.exception.TodoListAlreadyExistsException;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.Todo;
import todo.domain.model.TodoId;
import todo.infrastructure.adapter.web.model.*;

import java.util.List;
import java.util.function.Supplier;

@Adapter
@RestController
@RequestMapping( value = "/api/todolist"
        , consumes = MediaType.APPLICATION_JSON_VALUE
        , produces = MediaType.APPLICATION_JSON_VALUE
)
@Slf4j
public class TodoRestController {

    private final AddTodo addTodoService;
    private final CreateTodoList createTodoListService;
    private final GetTodoDone getTodoDoneService;
    private final ReadingTodos readingTodosService;

    @Autowired
    TodoRestController(
            final AddTodo addTodo,
            final CreateTodoList createTodoList,
            final GetTodoDone getTodoDone,
            final ReadingTodos readingTodos
    ){

        this.addTodoService = addTodo;
        this.createTodoListService = createTodoList;
        this.getTodoDoneService = getTodoDone;
        this.readingTodosService = readingTodos;
    }

    private static <T> ResponseEntity<T> handleRuntimeExceptions(final Supplier<ResponseEntity<T>> function){
        try {
            return function.get();
        } catch (final IllegalArgumentException e) {
            log.error("The request seems to be invalid!", e);
            return ResponseEntity.badRequest().build();
        } catch (final Exception e) {
            log.error("Oops, something went wrong...", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(path = "/{userId}/todo")
    ResponseEntity<AddTodoResponse> addTodo(
            @PathVariable(name = "userId") final String userId,
            @RequestBody final AddTodoRequest request
    ) {
        return handleRuntimeExceptions(() -> {
            try {
                final TodoId todoId = this.addTodoService.addTodo(
                        AddTodoCommand.of(MappingUtil.userId(userId), request.getDescription())
                );
                return ResponseEntity.ok(new AddTodoResponse(todoId.getId()));
            } catch (final MaxNumberOfTodosExceedException e) {
                log.error(String.format("The maximum number of todos of user %s is exceed!", userId), e);
                return ResponseEntity.unprocessableEntity().build();
            } catch (final UserDoesNotExistException e) {
                log.error(String.format("User %s does not exist!", userId), e);
                return ResponseEntity.notFound().build();
            }
        });
    }

    @PostMapping(path = "")
    ResponseEntity<Void> createTodolist(@RequestBody final CreateTodolistRequest request) {
        return handleRuntimeExceptions(() -> {
            try {
                this.createTodoListService.createTodoList(CreateTodoListCommand.of(request.toUserId()));
                return ResponseEntity.ok().build();
            } catch (final TodoListAlreadyExistsException e) {
                log.error(String.format("Todolist for user %s already exists!", request.getUserId()), e);
                return ResponseEntity.badRequest().build();
            }
        });
    }

    @PostMapping(path = "/{userId}/todo/{todoId}/done")
    ResponseEntity<Void> getTodoDone(
            @PathVariable(name = "userId") final String userId,
            @PathVariable(name = "todoId") final String todoId
    ) {
        return handleRuntimeExceptions(() -> {
            try {
                this.getTodoDoneService.getTodoDone(
                        GetTodoDoneCommand.of(MappingUtil.userId(userId), MappingUtil.todoId(todoId))
                );
                return ResponseEntity.ok().build();
            } catch (final UserDoesNotExistException e) {
                log.error(String.format("User %s does not exist!", userId), e);
                return ResponseEntity.badRequest().build();
            }
        });
    }


    @GetMapping(path = "/{userId}/todo")
    ResponseEntity<GetAllUndoneTodosResponse> getAllUndoneTodos(@PathVariable(name = "userId") final String userId) {
        return handleRuntimeExceptions(() -> {
            try {
                final List<Todo> undoneTodos = this.readingTodosService.getAllUndoneTodos(ReadTodosCommand.of(MappingUtil.userId(userId)));
                return ResponseEntity.ok(new GetAllUndoneTodosResponse(undoneTodos));
            } catch (final UserDoesNotExistException e) {
                log.error(String.format("User %s does not exist!", userId), e);
                return ResponseEntity.badRequest().build();
            }
        });
    }
}
