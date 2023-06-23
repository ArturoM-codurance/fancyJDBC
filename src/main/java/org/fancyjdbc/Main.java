package org.fancyjdbc;

import com.mongodb.client.*;
import org.bson.Document;
import org.fancyjdbc.project.application.ProjectService;
import org.fancyjdbc.project.infrastructure.http.ProjectController;
import org.fancyjdbc.project.infrastructure.persistance.JDBCProjectRepository;
import org.fancyjdbc.task.application.TaskService;
import org.fancyjdbc.task.infrastructure.persistance.JDBCTaskRepository;

import java.sql.Connection;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {
    private static MongoCollection<Document> projectsCollection;
    private static MongoCollection<Document> taskCollection;

    public static void main(String[] args) {
        initializeConnection();
        JDBCTaskRepository taskRepository = new JDBCTaskRepository(taskCollection);
        JDBCProjectRepository projectRepository = new JDBCProjectRepository(projectsCollection);
        ProjectService projectService = new ProjectService(projectRepository, taskRepository);
        TaskService taskService = new TaskService(taskRepository);
        ProjectController projectController = new ProjectController(projectService, taskService);

        post("/project", projectController::createProjectHandler);
        get("/project/:projectId", projectController::getProjectHandler);
    }


    private static void initializeConnection () {
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase db = mongoClient.getDatabase("test");
            projectsCollection = db.getCollection("projects");
            taskCollection = db.getCollection("task");
        }
    }
}