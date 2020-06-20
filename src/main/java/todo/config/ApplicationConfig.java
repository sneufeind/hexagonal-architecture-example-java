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
import todo.domain.port.*;

@Configuration
public class ApplicationConfig {

    @Bean
    AddTodo addTodo(final LoadTodoListPort loadTodoListPort, final SaveTodoListPort saveTodoListPort, final SendTodoAddedEventPort sendTodoAddedEventPort){
        return new AddTodoImpl(loadTodoListPort, saveTodoListPort, sendTodoAddedEventPort);
    }

    @Bean
    CreateTodoList createTodoList(final LoadTodoListPort loadTodoListPort, final SaveTodoListPort saveTodoListPort, final SendTodoListCreatedEventPort sendTodoListCreatedEventPort){
        return new CreateTodoListImpl(loadTodoListPort, saveTodoListPort, sendTodoListCreatedEventPort);
    }

    @Bean
    GetTodoDone getTodoDone(final LoadTodoListPort loadTodoListPort, final SaveTodoListPort saveTodoListPort, final SendTodoDoneEventPort sendTodoDoneEventPort){
        return new GetTodoDoneImpl(loadTodoListPort, saveTodoListPort, sendTodoDoneEventPort);
    }

    @Bean
    ReadingTodos readingTodos(final LoadTodoListPort loadTodoListPort){
        return new ReadingTodosImpl(loadTodoListPort);
    }
}
