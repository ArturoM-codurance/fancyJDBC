package org.fancyjdbc;

import org.fancyjdbc.project.application.ProjectService;
import org.fancyjdbc.project.infrastructure.http.ProjectController;
import org.fancyjdbc.project.infrastructure.persistance.JDBCProjectRepository;
import org.fancyjdbc.task.infrastructure.persistance.JDBCTaskRepository;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {
    public static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/postgres?user=user&password=password";
    public static void main(String[] args) {
        JDBCTaskRepository taskRepository = new JDBCTaskRepository(CONNECTION_STRING);
        JDBCProjectRepository projectRepository = new JDBCProjectRepository(CONNECTION_STRING);
        ProjectService projectService = new ProjectService(projectRepository, taskRepository);
        ProjectController projectController = new ProjectController(projectService);

        post("/project", (req, res) -> projectController.createProjectHandler(req, res));
        get("/project/:id", (req, res) -> projectController.getProjectHandler(req, res));
    }
}