package org.fancyjdbc.project.application;

import org.fancyjdbc.project.domain.ProjectRepository;
import org.fancyjdbc.task.domain.TaskRepository;

import java.sql.SQLException;

public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public void createWithDefaultTask(String projectId, String projectName, String taskId) throws SQLException {
        projectRepository.addProject(projectId, projectName);
        taskRepository.addTask(taskId, projectId);
    }
}
