package org.fancyjdbc.task.infrastructure.persistance;

import org.fancyjdbc.task.domain.Task;
import org.fancyjdbc.task.domain.TaskRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Task> getByProjectId(String projectId) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM task WHERE project_id = '%s'", projectId));
        List<Task> tasks = new ArrayList<>();
        while (resultSet.next()){
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            tasks.add(new Task(id, name));
        }
        return tasks;
    }
}
