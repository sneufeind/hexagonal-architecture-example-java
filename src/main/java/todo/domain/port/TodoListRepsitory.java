package todo.domain.port;

import common.architecture.Port;
import common.port.Repository;
import io.hschwentner.dddbits.annotation.DomainRepository;
import todo.domain.model.TodoList;
import todo.domain.model.UserId;

@DomainRepository
@Port
public interface TodoListRepsitory extends Repository<UserId,TodoList> {
}
