package org.fancyjdbc.project.infrastructure.persistance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.fancyjdbc.project.domain.Project;
import org.fancyjdbc.project.domain.ProjectRepository;

import java.util.List;

public class JDBCProjectRepository implements ProjectRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JDBCProjectRepository(EntityManagerFactory entityManagerFactory) {

        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void addProject(String projectId, String projectName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(new Project(projectId, projectName));
        entityManager.getTransaction().commit();
    }

    @Override
    public Project getProject(String projectId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Project> resultList = entityManager
                .createQuery("select p from Project p where id = :id", Project.class)
                .setParameter("id", projectId)
                .getResultList();
        entityManager.getTransaction().commit();
        return resultList.get(0);

    }
}
