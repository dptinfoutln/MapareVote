package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Main {
    public static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("maparevotedb");

    public static void main(String[] args) {

        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        User personne = new User("test@test.com",
                "TEST",
                "test",
                "AZERTY",
                true,
                false,
                false,
                false);

        entityManager.persist(personne);

        entityManager.flush();

        transaction.commit();

        entityManager.close();
    }
}
