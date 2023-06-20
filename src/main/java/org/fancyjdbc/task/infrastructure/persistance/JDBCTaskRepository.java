package org.fancyjdbc.task.infrastructure.persistance;

import org.fancyjdbc.task.domain.Task;
import org.fancyjdbc.task.domain.TaskRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class JDBCTaskRepository implements TaskRepository {
    private final SessionFactory sessionFactory;

    public JDBCTaskRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addTask(String taskId, String projectId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.persist(new Task(taskId, projectId, projectId));

        session.getTransaction().commit();
    }

    @Override
    public List<Task> getByProjectId(String projectId){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Task> list = session.createQuery("select t from Task t where t.projectId = '%s'".formatted(projectId), Task.class).list();

        session.getTransaction().commit();

        return list;
    }
}
