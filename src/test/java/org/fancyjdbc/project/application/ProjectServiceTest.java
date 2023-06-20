package org.fancyjdbc.project.application;

import org.fancyjdbc.project.domain.ProjectRepository;
import org.fancyjdbc.task.domain.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;
    @Mock
    TaskRepository taskRepository;

    @Test
    void should_call_add_project_and_add_task_when_creating_project() throws SQLException {
        // arrange
        String projectId = "";
        String projectName = "";
        String taskId = "";
        ProjectService projectService = new ProjectService(projectRepository, taskRepository);

        // act
        projectService.createWithDefaultTask(projectId, projectName, taskId);

        // assert
        verify(projectRepository).addProject(projectId, projectName);
        verify(taskRepository).addTask(taskId, projectId);
    }

}