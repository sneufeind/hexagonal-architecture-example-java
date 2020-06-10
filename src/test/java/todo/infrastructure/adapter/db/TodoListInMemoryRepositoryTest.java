package todo.infrastructure.adapter.db;

import org.junit.Before;
import org.junit.Test;
import todo.domain.model.TodoList;
import todo.domain.model.UserId;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TodoListInMemoryRepositoryTest {

    private TodoListListInMemoryRepository repsitory;

    @Before
    public void setUp() {
        this.repsitory = new TodoListListInMemoryRepository();
    }

    @Test
    public void findById() {
        // Given
        final UserId userId = UserId.create();
        final TodoList todoList = TodoList.create(userId);
        this.repsitory.save(todoList);
        // When
        final Optional<TodoList> opt = this.repsitory.findById(userId);
        // Then
        assertTrue(opt.isPresent());
        assertEquals(todoList, opt.get());
    }
}