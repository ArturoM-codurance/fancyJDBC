package org.fancyjdbc;

import org.fancyjdbc.project.application.ProjectService;
import org.fancyjdbc.project.infrastructure.http.ProjectController;
import org.fancyjdbc.project.infrastructure.persistance.JDBCProjectRepository;
import org.fancyjdbc.task.infrastructure.persistance.JDBCTaskRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {
    private static Connection conn;

    public static void main(String[] args) {
        try {
            initializeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        JDBCTaskRepository taskRepository = new JDBCTaskRepository(conn);
        JDBCProjectRepository projectRepository = new JDBCProjectRepository(conn);
        ProjectService projectService = new ProjectService(projectRepository, taskRepository);
        ProjectController projectController = new ProjectController(projectService);

        post("/project", (req, res) -> projectController.createProjectHandler(req, res));
        get("/project/:id", (req, res) -> projectController.getProjectHandler(req, res));
    }


    private static void initializeConnection () throws SQLException {
        String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/postgres?user=user&password=password";
        conn = DriverManager.getConnection(CONNECTION_STRING);
    }
}