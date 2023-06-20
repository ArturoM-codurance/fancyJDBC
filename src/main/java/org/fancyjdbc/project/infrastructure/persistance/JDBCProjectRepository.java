package org.fancyjdbc.project.infrastructure.persistance;

import org.fancyjdbc.project.domain.ProjectRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCProjectRepository implements ProjectRepository {
    private final Connection connection;

    public JDBCProjectRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addProject(String projectId, String projectName) throws SQLException {
        Statement statement = connection.createStatement();
        String insertProjectQuery = String.format("INSERT INTO project (id, name) VALUES ('%s', '%s')", projectId, projectName);
        statement.execute(insertProjectQuery);
    }
}
