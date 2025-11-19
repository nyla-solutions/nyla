package nyla.solutions.core.patterns.repository;

/**
 * @author Gregory Green
 */
public interface DataStoreRepository<T,ID> extends FindByIdRepository<T,ID>, SaveRepository<T> {

}
