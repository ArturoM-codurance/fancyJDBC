package org.fancyjdbc.project.infrastructure.http;

import com.eclipsesource.json.JsonObject;
import jdk.jfr.Frequency;
import org.fancyjdbc.project.application.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Request;
import spark.Response;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void should_call_project_service_create_project(){
        // arrange
        String projectId = "384d6480-65f2-45ea-87e2-75ae59b15915";
        String projectName = "Lorem ipsum project";
        String taskId = "6549df41-a338-49fb-a980-3daa83457e9b";
        ProjectController projectController = new ProjectController(projectService);
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
        verify(projectService).create(projectId, projectName, taskId);
    }
}