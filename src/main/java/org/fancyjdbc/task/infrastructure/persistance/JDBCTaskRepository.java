package org.fancyjdbc.task.infrastructure.persistance;

import org.fancyjdbc.task.domain.TaskRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTaskRepository implements TaskRepository {
    private final Connection connection;

    public JDBCTaskRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addTask(String taskId, String projectId) throws SQLException {
        Statement statement = connection.createStatement();
        String defaultName = "Initial task";
        int defaultComplexity = 1;
        int defaultCost = 0;
        String defaultTaxCountry = "spain";
        String insertProjectQuery = String.format("INSERT INTO task (id, project_id, name, complexity_id, cost, tax_id) VALUES ('%s', '%s', '%s', %d, %d, '%s')", taskId, projectId, defaultName, defaultComplexity, defaultCost, defaultTaxCountry);
        statement.execute(insertProjectQuery);
    }
}
