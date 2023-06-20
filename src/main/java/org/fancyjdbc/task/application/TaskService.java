package org.fancyjdbc.task.application;

import org.fancyjdbc.task.domain.Task;
import org.fancyjdbc.task.domain.TaskRepository;

import java.sql.SQLException;
import java.util.List;

public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {

        this.taskRepository = taskRepository;
    }

    public List<Task> getTasksFromProject(String projectId) throws SQLException {

        return taskRepository.getByProjectId(projectId);
    }
}
