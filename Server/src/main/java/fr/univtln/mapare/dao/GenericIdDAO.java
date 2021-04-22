package fr.univtln.mapare.dao;

import jakarta.persistence.EntityManager;

import java.lang.reflect.ParameterizedType;

public abstract class GenericIdDAO<E> extends DAO<E> {
    @SuppressWarnings("unchecked")
    protected final Class<E> entityClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public GenericIdDAO(EntityManager entityManager) {
        super(entityManager);
    }

    public void remove(int id) {
        entityManager.getTransaction().begin();
        entityManager.remove(findById(id));
        entityManager.getTransaction().commit();
    }


    public E findById(int id) {
        return entityManager.find(entityClass, id);
    }
}
