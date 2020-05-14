package common.port;

import java.util.Collection;
import java.util.Optional;

public interface Repository<ID,T> {

    void save(T obj);

    Collection<T> findAll();

    Optional<T> findById(ID id);

    void deleteById(ID id);
}
