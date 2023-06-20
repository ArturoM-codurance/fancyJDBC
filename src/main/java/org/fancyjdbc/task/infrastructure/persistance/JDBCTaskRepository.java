package org.fancyjdbc.task.infrastructure.persistance;

import org.fancyjdbc.task.domain.TaskRepository;

public class JDBCTaskRepository implements TaskRepository {
    public JDBCTaskRepository(String connectionString) {
    }
}
