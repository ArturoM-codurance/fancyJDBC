package org.fancyjdbc.task.domain;

import java.sql.SQLException;

public interface TaskRepository {
    void addTask(String taskId, String projectId) throws SQLException;
}
