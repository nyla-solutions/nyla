package nyla.solutions.core.patterns.repository;

public interface SaveRepository<T> {
    <S extends T> S save(S entity);
}
