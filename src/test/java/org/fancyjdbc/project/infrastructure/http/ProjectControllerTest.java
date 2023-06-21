package org.fancyjdbc.project.infrastructure.http;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.fancyjdbc.project.application.ProjectService;
import org.fancyjdbc.project.domain.Project;
import org.fancyjdbc.task.application.TaskService;
import org.fancyjdbc.task.domain.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    ProjectService projectService;
    @Mock
    Response response;
    @Mock
    Request request;
    @Mock
    TaskService taskService;

    @Test
    void should_call_project_service_create_project() throws SQLException {
        // arrange
        String projectId = "384d6480-65f2-45ea-87e2-75ae59b15915";
        String projectName = "Lorem ipsum project";
        String taskId = "6549df41-a338-49fb-a980-3daa83457e9b";
        ProjectController projectController = new ProjectController(projectService, taskService);
        when(request.body()).thenReturn(
                new JsonObject()
                        .add("projectName", projectName)
                        .add("projectId", projectId)
                        .add("initialTaskId", taskId)
                        .toString()
        );

        // act
        projectController.createProjectHandler(request, response);

        // assert
        verify(projectService).createWithDefaultTask(projectId, projectName, taskId);
    }

    @Test
    void should_return_response_with_status_201() throws SQLException {
        String projectId = "384d6480-65f2-45ea-87e2-75ae59b15915";
        String projectName = "Lorem ipsum project";
        String taskId = "6549df41-a338-49fb-a980-3daa83457e9b";
        ProjectController projectController = new ProjectController(projectService, taskService);
        when(request.body()).thenReturn(
                new JsonObject()
                        .add("projectName", projectName)
                        .add("projectId", projectId)
                        .add("initialTaskId", taskId)
                        .toString()
        );

        // act
        String creatingProjectResponse = projectController.createProjectHandler(request, response);

        // assert
        verify(response).status(201);
        assertThat(creatingProjectResponse).isNull();
    }

    @Test
    void should_call_project_service_get_project() throws SQLException {
        // arrange
        String projectId = "384d6480-65f2-45ea-87e2-75ae59b15915";
        ProjectController projectController = new ProjectController(projectService, taskService);
        when(request.params("projectId")).thenReturn(projectId);
        String projectName = "project name";
        Project project = new Project(projectId, projectName);
        when(projectService.getProject(projectId)).thenReturn(project);
        // act
        projectController.getProjectHandler(request, response);

        // assert
        verify(projectService).getProject(projectId);
        verify(taskService).getTasksFromProject(projectId);
    }
    @Test
    void should_return_project_with_tasks_when_get_project() throws SQLException {
        // arrange
        String projectId = "384d6480-65f2-45ea-87e2-75ae59b15915";
        ProjectController projectController = new ProjectController(projectService, taskService);
        String taskId = "task-id";
        String taskName = "Initial task";
        JsonObject jsonTask = new JsonObject()
                .add("id", taskId)
                .add("name", taskName);
        String projectName = "Test project";
        String expectedResponse = new JsonObject()
                .add("id", projectId)
                .add("name", projectName)
                .add("tasks", new JsonArray().add(jsonTask))
                .toString();
        when(request.params("projectId")).thenReturn(projectId);
        Project project = new Project(projectId, projectName);
        when(projectService.getProject(projectId)).thenReturn(project);
        List<Task> tasks = List.of(new Task(taskId, projectId));
        when(taskService.getTasksFromProject(projectId)).thenReturn(tasks);

        // act
        String handlerResponse = projectController.getProjectHandler(request, response);

        // assert
        assertThat(handlerResponse).isEqualTo(expectedResponse);
    }
}