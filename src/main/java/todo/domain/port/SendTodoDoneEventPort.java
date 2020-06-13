package todo.domain.port;

import common.architecture.Port;
import todo.domain.event.TodoDoneEvent;

@Port
public interface SendTodoDoneEventPort {

    void send(TodoDoneEvent event);
}
