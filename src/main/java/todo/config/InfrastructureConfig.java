package todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import todo.domain.port.LoadTodoListPort;
import todo.domain.port.SaveTodoListPort;
import todo.infrastructure.adapter.db.TodoListListInMemoryRepository;

@Configuration
public class InfrastructureConfig {

    private final TodoListListInMemoryRepository repository;

    public InfrastructureConfig(){
        this.repository = new TodoListListInMemoryRepository();
    }

    @Bean
    LoadTodoListPort loadTodoListPort(){
        return this.repository;
    }

    @Bean
    SaveTodoListPort saveTodoListPort(){
        return this.repository;
    }
}
