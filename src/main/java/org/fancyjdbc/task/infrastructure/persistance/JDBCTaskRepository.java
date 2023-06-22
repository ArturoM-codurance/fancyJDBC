package org.fancyjdbc.task.infrastructure.persistance;

import org.fancyjdbc.project.domain.Project;
import org.fancyjdbc.task.domain.Task;
import org.fancyjdbc.task.domain.TaskRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCTaskRepository implements TaskRepository {
    private final SessionFactory sessionFactory;

    public JDBCTaskRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addTask(String taskId, String projectId) {
        String defaultName = "Initial task";
        int defaultComplexity = 1;
        int defaultCost = 0;
        String defaultTaxCountry = "spain";

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(new Task(taskId, defaultName, projectId, defaultComplexity, defaultCost, defaultTaxCountry));
        session.getTransaction().commit();
        session.close();

    }

    @Override
    public List<Task> getByProjectId(String projectId) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Task> tasks = session.createQuery("select t from Task t where projectId = :id", Task.class).setParameter("projectId", projectId).list();
        session.getTransaction().commit();
        session.close();

        return tasks;
    }
}
