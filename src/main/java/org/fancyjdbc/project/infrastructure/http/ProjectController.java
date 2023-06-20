package org.fancyjdbc.project.infrastructure.http;

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

    public void createProjectHandler(Request req, Response res) {
        throw new UnsupportedOperationException();
    }
}
