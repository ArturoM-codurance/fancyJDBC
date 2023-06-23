package org.fancyjdbc;

import com.mongodb.client.*;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.fancyjdbc.project.application.ProjectService;
import org.fancyjdbc.project.infrastructure.http.ProjectController;
import org.fancyjdbc.project.infrastructure.persistance.JDBCProjectRepository;
import org.fancyjdbc.task.application.TaskService;
import org.fancyjdbc.task.infrastructure.persistance.JDBCTaskRepository;


import static spark.Spark.get;
import static spark.Spark.post;

public class Main {
    private static Datastore datastore;

    public static void main(String[] args) {
        initializeConnection();
        JDBCTaskRepository taskRepository = new JDBCTaskRepository(datastore);
        JDBCProjectRepository projectRepository = new JDBCProjectRepository(datastore);
        ProjectService projectService = new ProjectService(projectRepository, taskRepository);
        TaskService taskService = new TaskService(taskRepository);
        ProjectController projectController = new ProjectController(projectService, taskService);

        post("/project", projectController::createProjectHandler);
        get("/project/:projectId", projectController::getProjectHandler);
    }


    private static void initializeConnection () {
        String uri = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(uri);

        datastore = Morphia.createDatastore(mongoClient, "test");
        datastore.getMapper().mapPackage("org.fancyjdbc.project.domain");
        datastore.getMapper().mapPackage("org.fancyjdbc.task.domain");
        datastore.ensureIndexes();
    }
}