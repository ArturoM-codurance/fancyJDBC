package org.fancyjdbc.task.infrastructure.persistance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.fancyjdbc.task.domain.Task;
import org.fancyjdbc.task.domain.TaskRepository;

import java.util.List;

public class JDBCTaskRepository implements TaskRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JDBCTaskRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void addTask(String taskId, String projectId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.persist(new Task(taskId, projectId));

        entityManager.getTransaction().commit();
    }

    @Override
    public List<Task> getByProjectId(String projectId){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Task> list = entityManager
                .createQuery("select t from Task t where t.projectId = :projectId", Task.class)
                .setParameter("projectId", projectId)
                .getResultList();

        entityManager.getTransaction().commit();

        return list;
    }
}
