package fr.univtln.mapare.dao;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@AllArgsConstructor
public abstract class DAO<E> {
    @SuppressWarnings("unchecked")
    protected final Class<E> entityClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    protected EntityManager entityManager;

    public void persist(E entity) {
        entityManager.persist(entity);
    }

    public void remove(long id) {
        entityManager.remove(findById(id));
    }

    public void remove(E entity) {
        entityManager.remove(entity);
    }

    public E findById(long id) {
        return entityManager.find(entityClass, id);
    }

    abstract List<E> findAll();
}