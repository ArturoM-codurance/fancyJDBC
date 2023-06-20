package org.fancyjdbc.project.application;

import org.fancyjdbc.project.domain.ProjectRepository;
import org.fancyjdbc.task.domain.TaskRepository;

public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
    }

    public void create(String projectId, String projectName, String taskId) {
    }
}
