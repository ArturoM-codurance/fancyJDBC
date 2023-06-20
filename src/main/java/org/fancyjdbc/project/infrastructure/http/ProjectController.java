package org.fancyjdbc.project.infrastructure.http;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.fancyjdbc.project.application.ProjectService;
import spark.Request;
import spark.Response;

public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    public String getProjectHandler(Request req, Response res) {
        throw new UnsupportedOperationException();
    }

    public String createProjectHandler(Request req, Response res) {
        JsonObject requestBody = Json.parse(req.body()).asObject();
        String projectId = requestBody.getString("projectId", null);
        String projectName = requestBody.getString("projectName", null);
        String taskId = requestBody.getString("initialTaskId", null);

        projectService.create(projectId, projectName, taskId);

        return null;
    }
}
