package org.fancyjdbc;

import jakarta.persistence.EntityManagerFactory;
import org.fancyjdbc.project.application.ProjectService;
import org.fancyjdbc.project.infrastructure.http.ProjectController;
import org.fancyjdbc.project.infrastructure.persistance.JDBCProjectRepository;
import org.fancyjdbc.task.application.TaskService;
import org.fancyjdbc.task.infrastructure.persistance.JDBCTaskRepository;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {
    private static Connection conn;
    private static EntityManagerFactory entityManagerFactory;

    public static void main(String[] args) {
        setUp();

        JDBCTaskRepository taskRepository = new JDBCTaskRepository(entityManagerFactory);
        JDBCProjectRepository projectRepository = new JDBCProjectRepository(entityManagerFactory);

        ProjectService projectService = new ProjectService(projectRepository, taskRepository);
        TaskService taskService = new TaskService(taskRepository);

        ProjectController projectController = new ProjectController(projectService, taskService);

        post("/project", projectController::createProjectHandler);
        get("/project/:projectId", projectController::getProjectHandler);
    }


    private static void initializeConnection () throws SQLException {
        String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/postgres?user=user&password=password";
        conn = DriverManager.getConnection(CONNECTION_STRING);
    }

    protected static void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            entityManagerFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }
}