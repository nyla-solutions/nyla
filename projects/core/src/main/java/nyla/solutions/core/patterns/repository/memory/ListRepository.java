package nyla.solutions.core.patterns.repository.memory;

import nyla.solutions.core.patterns.repository.FindAllRepository;
import nyla.solutions.core.patterns.repository.SaveRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gregory Green
 */
public class ListRepository<T> implements FindAllRepository<T>, SaveRepository<T> {
    private final List<T> list;
    private final boolean saveUnique;

    public ListRepository(List<T> list) {
        this(list,false);
    }

    public ListRepository(List<T> list, boolean saveUnique) {
        this.list = list;
        this.saveUnique = saveUnique;
    }

    @Override
    public Iterable<T> findAll() {
        return new ArrayList<>(list);
    }

    @Override
    public <S extends T> S save(S entity) {

        if(saveUnique && list.contains(entity))
            return null;

        list.add(entity);

        return entity;
    }
}
