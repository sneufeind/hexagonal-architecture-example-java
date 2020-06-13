package todo;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import todo.application.service.CreateTodoListImpl;
import todo.application.service.GetTodoDoneImpl;
import todo.application.service.ReadingTodosImpl;
import todo.application.service.TodoAddImpl;
import todo.application.usecase.AddTodo;
import todo.application.usecase.CreateTodoList;
import todo.application.usecase.GetTodoDone;
import todo.domain.command.AddTodoCommand;
import todo.domain.command.CreateTodoListCommand;
import todo.domain.command.GetTodoDoneCommand;
import todo.domain.command.ReadTodosCommand;
import todo.domain.exception.MaxNumberOfTodosExceedException;
import todo.domain.exception.TodoListAlreadyExistsException;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.Todo;
import todo.domain.model.TodoId;
import todo.domain.model.TodoList;
import todo.domain.model.UserId;
import todo.domain.port.*;
import todo.infrastructure.adapter.db.TodoListListInMemoryRepository;
import todo.infrastructure.adapter.events.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.Assert.*;

public class ToDoStepDefs {

    private final AtomicInteger deadEventCounter = new AtomicInteger(0);
    private final TodoListListInMemoryRepository todoListRepository;
    private final AddTodo addTodoUseCase;
    private final CreateTodoList createTodoListUseCase;
    private final GetTodoDone getTodoDoneUseCase;
    private final ReadingTodosImpl readingTodoUseCase;
    private final UserId userId;

    private Todo todo;

    public ToDoStepDefs(){
        final EventBus eventBus = new EventBus();
        eventBus.register(this); // for dead events

        this.todoListRepository = new TodoListListInMemoryRepository();

        this.readingTodoUseCase = new ReadingTodosImpl(this.todoListRepository);

        final SendTodoAddedEventPort sendTodoAddedEventPort = new SendTodoAddedEventGuavaAdapter(eventBus);
        final SendTodoDoneEventPort sendTodoDoneEventPort = new SendTodoDoneEventGuavaAdapter(eventBus);
        final SendTodoListCreatedEventPort sendTodoListCreatedEventPort = new SendTodoListCreatedEventGuavaAdapter(eventBus);

        final ReceiveTodoAddedEventPort receiveTodoAddedEventPort = new ReceiveTodoAddedEventGuavaAdapter(eventBus, this.readingTodoUseCase::on);
        final ReceiveTodoDoneEventPort receiveTodoDoneEventPort = new ReceiveTodoDoneEventGuavaAdapter(eventBus, this.readingTodoUseCase::on);
        final ReceiveTodoListCreatedEventPort receiveTodoListCreatedEventPort = new ReceiveTodoListCreatedEventGuavaAdapter(eventBus, this.readingTodoUseCase::on);

        this.addTodoUseCase = new TodoAddImpl(this.todoListRepository, this.todoListRepository, sendTodoAddedEventPort);
        this.createTodoListUseCase = new CreateTodoListImpl(this.todoListRepository, this.todoListRepository, sendTodoListCreatedEventPort);
        this.getTodoDoneUseCase = new GetTodoDoneImpl(this.todoListRepository, this.todoListRepository, sendTodoDoneEventPort);
        this.userId = UserId.create();
    }

    @Subscribe
    public void on(final DeadEvent event){
        this.deadEventCounter.incrementAndGet();
    }

    @Given("an empty list")
    public void anEmptyList() throws TodoListAlreadyExistsException, UserDoesNotExistException {
        initTodoList(this.userId);
        assertTrue(this.readingTodoUseCase.getAllUndoneTodos(ReadTodosCommand.of(this.userId)).isEmpty());
    }

    @Given("a list with the following todos:")
    public void aListWithTheFollowingTodos(final DataTable table) {
        this.todoListRepository.save(TodoList.create(this.userId));
        table.asList().forEach(data -> {
            try {
                this.addTodoUseCase.addTodo(AddTodoCommand.of(this.userId, data));
            } catch (final MaxNumberOfTodosExceedException | UserDoesNotExistException e) {
                throw new RuntimeException(e);
            }
        });
        assertNumberOfToDos(table.asList().size());
    }

    @When("user adds a new todo")
    public void userAddsANewTodo() throws MaxNumberOfTodosExceedException, UserDoesNotExistException {
        this.todo = findById( this.addTodoUseCase.addTodo(AddTodoCommand.of(this.userId, "Create a good Example")) );
    }

    @And("tries to add this todo again")
    public void triesToAddThisTodoAgain() throws MaxNumberOfTodosExceedException, UserDoesNotExistException {
        this.addTodoUseCase.addTodo(AddTodoCommand.of(this.userId, this.todo.getDescription()));
    }

    @When("the user asks for his todos")
    public void theUserAsksForHisTodos() throws UserDoesNotExistException {
        this.readingTodoUseCase.getAllUndoneTodos(ReadTodosCommand.of(this.userId));
    }

    @When("the todo {string} is done")
    public void theTodoIsDone(final String todoDescription) throws UserDoesNotExistException {
        final Todo todo = findByDescription(todoDescription);
        this.getTodoDoneUseCase.getTodoDone(GetTodoDoneCommand.of(this.userId, todo.getId()));
    }

    @Then("this todo will be added to the list")
    public void thisTodoWillBeAddedToTheList() {
        assertNotNull( findById(this.todo.getId()) );
        ensureNoDomainEventsHadBeenIgnored();
    }

    @Then("this todo will be added twice")
    public void thisTodoWillBeAddedTwice() {
        final TodoList todoList = this.todoListRepository.findById(this.userId).get();
        final long count = todoList.undoneTodos().stream()
                .filter(e -> e.getDescription().equals(this.todo.getDescription()))
                .count();
        assertEquals(2, count);
        ensureNoDomainEventsHadBeenIgnored();
    }

    @Then("the list contains {int} todos")
    public void theListContainsTodos(final int expectedNumberOfToDos) {
        assertNumberOfToDos(expectedNumberOfToDos);
        ensureNoDomainEventsHadBeenIgnored();
    }

    @And("this todo will be unchecked")
    public void thisTodoWillBeUnchecked() {
        assertFalse(this.todo.isDone());
    }

    private void ensureNoDomainEventsHadBeenIgnored(){
        assertEquals(0, this.deadEventCounter.get());
    }

    private void initTodoList(final UserId userId) throws TodoListAlreadyExistsException {
        this.createTodoListUseCase.createTodoList(CreateTodoListCommand.of(userId));
        assertTrue(this.todoListRepository.findById(userId).isPresent());
    }

    private Todo findById(final TodoId id){
        return findBy(t -> t.getId().equals(id));
    }

    private Todo findByDescription(final String description){
        return findBy(t -> t.getDescription().equals(description));
    }

    private Todo findBy(final Function<Todo,Boolean> filter){
        return this.todoListRepository.findAll().stream()
                .flatMap(list -> list.undoneTodos().stream())
                .filter(t -> filter.apply(t))
                .findFirst()
                .get();
    }

    private void assertNumberOfToDos(final int expectedNumber){
        assertEquals(expectedNumber, this.todoListRepository.findById(this.userId).get().countUndoneTodos());
    }
}
