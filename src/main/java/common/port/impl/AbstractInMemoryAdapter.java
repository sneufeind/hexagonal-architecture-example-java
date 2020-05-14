package common.port.impl;

import common.port.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractInMemoryAdapter<ID,T> implements Repository<ID,T> {

    private final Map<ID,T> toDoRepo = new HashMap<>();

    protected AbstractInMemoryAdapter(){}

    protected abstract ID extractId(T obj);

    @Override
    public void save(final T obj) {
        this.toDoRepo.put(extractId(obj), obj);
    }

    @Override
    public Collection<T> findAll() {
        return this.toDoRepo.values();
    }

    @Override
    public Optional<T> findById(final ID id) {
        return Optional.ofNullable(this.toDoRepo.get(id));
    }

    @Override
    public void deleteById(final ID id) {
        this.toDoRepo.remove(id);
    }
}
