package todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import todo.application.service.AddTodoImpl;
import todo.application.service.CreateTodoListImpl;
import todo.application.service.GetTodoDoneImpl;
import todo.application.service.ReadingTodosImpl;
import todo.application.usecase.AddTodo;
import todo.application.usecase.CreateTodoList;
import todo.application.usecase.GetTodoDone;
import todo.application.usecase.ReadingTodos;
import todo.domain.port.LoadTodoListPort;
import todo.domain.port.SaveTodoListPort;

@Configuration
public class ApplicationConfig {

    @Bean
    AddTodo addTodo(final LoadTodoListPort loadTodoListPort, final SaveTodoListPort saveTodoListPort){
        return new AddTodoImpl(loadTodoListPort, saveTodoListPort);
    }

    @Bean
    CreateTodoList createTodoList(final LoadTodoListPort loadTodoListPort, final SaveTodoListPort saveTodoListPort){
        return new CreateTodoListImpl(loadTodoListPort, saveTodoListPort);
    }

    @Bean
    GetTodoDone getTodoDone(final LoadTodoListPort loadTodoListPort, final SaveTodoListPort saveTodoListPort){
        return new GetTodoDoneImpl(loadTodoListPort, saveTodoListPort);
    }

    @Bean
    ReadingTodos readingTodos(final LoadTodoListPort loadTodoListPort){
        return new ReadingTodosImpl(loadTodoListPort);
    }
}
