package org.fancyjdbc.project.infrastructure.persistance;

import org.fancyjdbc.project.domain.Project;
import org.fancyjdbc.project.domain.ProjectRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class JDBCProjectRepository implements ProjectRepository {
    private final SessionFactory sessionFactory;

    public JDBCProjectRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addProject(String projectId, String projectName) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(new Project(projectId, projectName));
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Project getProject(String projectId) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Project project = session.createQuery("select p from Project p where p.id = :id", Project.class).setParameter("id", projectId).getSingleResult();
        session.getTransaction().commit();
        session.close();

        return project;

    }
}
