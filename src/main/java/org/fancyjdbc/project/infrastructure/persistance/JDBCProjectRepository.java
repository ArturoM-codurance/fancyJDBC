package org.fancyjdbc.project.infrastructure.persistance;

import org.fancyjdbc.project.domain.Project;
import org.fancyjdbc.project.domain.ProjectRepository;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCProjectRepository implements ProjectRepository {
    private final Connection connection;

    public JDBCProjectRepository(SessionFactory sessionFactory) {
        this.connection = sessionFactory;
    }

    @Override
    public void addProject(String projectId, String projectName) throws SQLException {
        Statement statement = connection.createStatement();
        String insertProjectQuery = String.format("INSERT INTO project (id, name) VALUES ('%s', '%s')", projectId, projectName);
        statement.execute(insertProjectQuery);
    }

    @Override
    public Project getProject(String projectId) throws SQLException {
        Statement statement = connection.createStatement();
        String getProjectByIdQuery = String.format("SELECT * FROM project WHERE id = '%s'", projectId);
        ResultSet resultSet = statement.executeQuery(getProjectByIdQuery);
        resultSet.next();
        String id = resultSet.getString("id");
        String projectName = resultSet.getString("name");
        return new Project(id, projectName);

    }
}
