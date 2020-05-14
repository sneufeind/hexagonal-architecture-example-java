package todo.domain.model;

import io.hschwentner.dddbits.annotation.AggregateRoot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AggregateRoot
@Value
public class TodoList {

    @Getter(AccessLevel.PRIVATE)
    private final List<Todo> todos = new ArrayList<>();
    private final UserId userId;

    private TodoList(@NonNull final UserId userId){
        this.userId = userId;
    }

    public static TodoList create(final UserId userId){
        return new TodoList(userId);
    }

    public void addTodo(@NonNull final Todo todo) {
        this.todos.add(todo);
    }

    public void getTodoDone(@NonNull final TodoId todoId) {
        findById(todoId).ifPresent(t -> t.done() );
    }

    public List<Todo> undoneTodos() {
        return this.todos.stream()
                .filter(t -> !t.isDone())
                .collect(Collectors.toList());
    }

    public int countUndoneTodos(){
        return undoneTodos().size();
    }

    private Optional<Todo> findById(final TodoId id){
        return this.todos.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }
}
