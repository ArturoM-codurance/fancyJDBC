package org.fancyjdbc.task.domain;

import java.sql.SQLException;
import java.util.List;

public interface TaskRepository {
    void addTask(String taskId, String projectId) throws SQLException;

    List<Task> getByProjectId(String projectId) throws SQLException;
}
