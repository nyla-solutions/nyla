package nyla.solutions.core.patterns.repository;

import java.util.Optional;

public interface FindByIdRepository<T,ID> {

    Optional<T> findById(ID id);
}
