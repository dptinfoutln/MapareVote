package fr.univtln.mapare.dao;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@AllArgsConstructor
public abstract class DAO<E> {
    protected EntityManager entityManager;

    public void persist(E entity) {
        entityManager.persist(entity);
    }

    public void update(E entity) {
        entityManager.merge(entity);
    }

    public void remove(E entity) {
        entityManager.remove(entity);
    }


    abstract List<E> findAll();
}