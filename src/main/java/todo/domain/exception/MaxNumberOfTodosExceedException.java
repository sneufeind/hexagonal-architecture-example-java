package todo.domain.exception;

public class MaxNumberOfTodosExceedException extends Exception {

    public MaxNumberOfTodosExceedException(final int max){
        super(String.format("The maximum number (%d) of todos is exceeded!", max));
    }
}
