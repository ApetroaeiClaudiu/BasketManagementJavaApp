package Repository;

import Domain.Entity;

public interface CRUDRepository<ID,E extends Entity<ID>> {
    void add(E entity);
    void delete(ID id);
    void update(ID id,E entity);
    int size();
    E findOne(ID id);
    Iterable<E> findAll();
}
