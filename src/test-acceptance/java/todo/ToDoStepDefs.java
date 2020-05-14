package todo;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import todo.application.AddTodo;
import todo.application.GetTodoDone;
import todo.application.ReadingTodos;
import todo.application.impl.GetTodoDoneService;
import todo.application.impl.ReadingTodosService;
import todo.application.impl.TodoAddService;
import todo.domain.service.TodoService;
import todo.domain.exception.MaxNumberOfTodosExceedException;
import todo.domain.exception.TodoAlreadyExistsException;
import todo.domain.exception.TodoDoesNotExistException;
import todo.domain.exception.UserDoesNotExistException;
import todo.domain.model.Todo;
import todo.domain.model.TodoId;
import todo.domain.model.TodoList;
import todo.domain.model.UserId;
import todo.domain.port.TodoListRepsitory;
import todo.domain.service.impl.TodoServiceImpl;
import todo.infrastructure.adapter.db.TodoListInMemoryRepository;

import java.util.function.Function;

import static org.junit.Assert.*;

public class ToDoStepDefs {

    private final TodoListRepsitory todoListRepository;
    private final TodoService domainService;
    private final AddTodo addTodoUseCase;
    private final GetTodoDone getTodoDoneUseCase;
    private final ReadingTodos readingTodoUseCase;
    private final UserId userId;

    private Todo todo;

    public ToDoStepDefs(){
        this.todoListRepository = new TodoListInMemoryRepository();
        this.domainService = new TodoServiceImpl(this.todoListRepository);
        this.addTodoUseCase = new TodoAddService(this.domainService);
        this.getTodoDoneUseCase = new GetTodoDoneService(this.domainService);
        this.readingTodoUseCase = new ReadingTodosService(this.domainService);
        this.userId = UserId.create();
    }

    @Given("an empty list")
    public void anEmptyList() {
        this.domainService.initTodoList(this.userId);
        assertTrue(this.readingTodoUseCase.getAllUndoneTodos(this.userId).isEmpty());
    }

    @Given("a list with the following todos:")
    public void aListWithTheFollowingTodos(final DataTable table) {
        this.domainService.initTodoList(this.userId);
        table.asList().forEach(data -> {
            try {
                this.addTodoUseCase.addTodo(this.userId, data);
            } catch (final MaxNumberOfTodosExceedException | TodoAlreadyExistsException e) {
                throw new RuntimeException(e);
            }
        });
        assertNumberOfToDos(table.asList().size());
    }

    @When("user adds a new todo")
    public void userAddsANewTodo() throws MaxNumberOfTodosExceedException, TodoAlreadyExistsException {
        this.todo = findById( this.addTodoUseCase.addTodo(this.userId, "Create a good Example") );
    }

    @And("tries to add this todo again")
    public void triesToAddThisTodoAgain() throws MaxNumberOfTodosExceedException, TodoAlreadyExistsException {
        this.addTodoUseCase.addTodo(this.userId, this.todo.getDescription());
    }

    @When("the user asks for his todos")
    public void theUserAsksForHisTodos() {
        this.readingTodoUseCase.getAllUndoneTodos(this.userId);
    }

    @When("the todo {string} is done")
    public void theTodoIsDone(final String todoDescription) throws TodoDoesNotExistException, UserDoesNotExistException {
        final Todo todo = findByDescription(todoDescription);
        this.getTodoDoneUseCase.getTodoDone(this.userId, todo.getId());
    }

    @Then("this todo will be added to the list")
    public void thisTodoWillBeAddedToTheList() {
        assertNotNull( findById(this.todo.getId()) );
    }

    @Then("this todo will be added twice")
    public void thisTodoWillBeAddedTwice() {
        final TodoList todoList = this.todoListRepository.findById(this.userId).get();
        final long count = todoList.undoneTodos().stream()
                .filter(e -> e.getDescription().equals(this.todo.getDescription()))
                .count();
        assertEquals(2, count);
    }

    @Then("the list contains {int} todos")
    public void theListContainsTodos(final int expectedNumberOfToDos) {
        assertNumberOfToDos(expectedNumberOfToDos);
    }

    @And("this todo will be unchecked")
    public void thisTodoWillBeUnchecked() {
        assertFalse(this.todo.isDone());
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
        assertEquals(expectedNumber, this.domainService.undoneTodos(this.userId).size());
    }
}
