package fr.univtln.mapare.dao;

import fr.univtln.mapare.exceptions.BusinessException;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * The type Dao.
 *
 * @param <E> the type parameter
 */
@AllArgsConstructor
public abstract class DAO<E> {
    /**
     * The Entity manager.
     */
    protected EntityManager entityManager;

    /**
     * Persist.
     *
     * @param entity the entity
     * @throws BusinessException the business exception
     */
    public void persist(E entity) throws BusinessException {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    /**
     * Update.
     *
     * @param entity the entity
     */
    public void update(E entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    /**
     * Remove.
     *
     * @param entity the entity
     */
    public void remove(E entity) {
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }


    /**
     * Find all list.
     *
     * @return the list
     */
    public abstract List<E> findAll();

//    public void refresh(EntityManager entityManager) {
//        entityManager.refresh();
//    }
}