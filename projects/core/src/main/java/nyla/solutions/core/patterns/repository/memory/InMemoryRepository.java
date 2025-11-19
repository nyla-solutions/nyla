package nyla.solutions.core.patterns.repository.memory;

import nyla.solutions.core.patterns.repository.DataStoreRepository;
import nyla.solutions.core.util.JavaBean;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Gregory Green
 */
public class InMemoryRepository<T,ID> implements DataStoreRepository<T,ID> {

    private final Map<ID,T> map;
    private final String keyPropertyId;

    public InMemoryRepository(Map<ID, T> store, String keyPropertyId) {
        this.map = store;
        this.keyPropertyId = keyPropertyId;
    }

    public static<T,ID> InMemoryRepositoryBuilder builder() {
        return new InMemoryRepositoryBuilder();
    }

    @Override
    public Optional<T> findById(ID id) {
        var value = map.get(id);

        return value != null ? Optional.of(value) : Optional.empty();
    }

    @Override
    public <S extends T> S save(S entity) {
        return (S) map.put((ID)JavaBean.getProperty(entity,keyPropertyId),entity);
    }

    public static class InMemoryRepositoryBuilder {
        private String idPropertyName;
        public InMemoryRepositoryBuilder  withIdProperty(String idPropertyName) {
            this.idPropertyName = idPropertyName;
            return this;
        }

        public <T,ID> InMemoryRepository<T, ID> build() {
            return new InMemoryRepository<>(new     ConcurrentHashMap<>(),idPropertyName);
        }
    }
}
