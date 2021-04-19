package fr.univtln.mapare.dao;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public abstract class DAO<E> {
    protected EntityManager entityManager;

    public void persist(E entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    public void update(E entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    public void remove(E entity) {
        entityManager.remove(entity);
    }


    public abstract List<E> findAll();
}