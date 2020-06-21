package todo.adapter.in.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import todo.domain.model.Todo;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUndoneTodosResponse {

    private List<Todo> undoneTodos;
}
