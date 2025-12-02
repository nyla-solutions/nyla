package nyla.solutions.core.patterns.repository;

/**
 * @author Gregory Green
 */
public interface FindAllRepository<T> {
    Iterable<T> findAll();
}
