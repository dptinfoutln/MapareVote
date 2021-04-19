package fr.univtln.mapare.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public abstract class DAO<E> {
    protected EntityManager entityManager;

    public void persist(E entity) {
        EntityTransaction transaction =  entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(entity);
        transaction.commit();
    }

    public void update(E entity) {
        EntityTransaction transaction =  entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(entity);
        transaction.commit();
    }

    public void remove(E entity) {
        entityManager.remove(entity);
    }


    public abstract List<E> findAll();
}