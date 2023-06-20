package org.fancyjdbc.project.application;

import org.fancyjdbc.project.domain.Project;
import org.fancyjdbc.project.domain.ProjectRepository;
import org.fancyjdbc.task.domain.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    @Test
    void should_return_project_from_given_repository_when_getting_project() throws SQLException {
        // arrange
        String projectId = "project-id";
        String projectName = "project name";
        ProjectService projectService = new ProjectService(projectRepository, taskRepository);
        Project expectedProject = new Project(projectId, projectName);
        when(projectRepository.getProject(projectId)).thenReturn(expectedProject);

        // act
        Project actualProject = projectService.getProject(projectId);

        // assert
        verify(projectRepository).getProject(projectId);
        assertThat(actualProject).isEqualTo(expectedProject);

    }

}