package fr.univtln.mapare.dao;

import jakarta.persistence.EntityManager;

import java.lang.reflect.ParameterizedType;

/**
 * The type Generic id dao.
 *
 * @param <E> the type parameter
 */
public abstract class GenericIdDAO<E> extends DAO<E> {
    /**
     * The Entity class.
     */
    @SuppressWarnings("unchecked")
    protected final Class<E> entityClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    /**
     * Instantiates a new Generic id dao.
     *
     * @param entityManager the entity manager
     */
    protected GenericIdDAO(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * Remove.
     *
     * @param id the id
     */
    public void remove(int id) {
        entityManager.getTransaction().begin();
        entityManager.remove(findById(id));
        entityManager.getTransaction().commit();
    }


    /**
     * Find by id e.
     *
     * @param id the id
     * @return the e
     */
    public E findById(int id) {
        return entityManager.find(entityClass, id);
    }
}
