package acceptance;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.fancyjdbc.project.application.ProjectService;
import org.fancyjdbc.project.domain.ProjectRepository;
import org.fancyjdbc.project.infrastructure.persistance.JDBCProjectRepository;
import org.fancyjdbc.project.infrastructure.http.ProjectController;
import org.fancyjdbc.task.application.TaskService;
import org.fancyjdbc.task.domain.TaskRepository;
import org.fancyjdbc.task.infrastructure.persistance.JDBCTaskRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectAcceptanceTests {
    private static MongoCollection<Document> projectsCollection;
    private static MongoCollection<Document> taskCollection;
    private static MongoClient mongoClient;
    @Mock
    Request req;

    @Mock
    Response res;

    @BeforeAll
    static void establishConnectionAndCreateTables() {
        String uri = "mongodb://localhost:27017";
        mongoClient = MongoClients.create(uri);
        MongoDatabase db = mongoClient.getDatabase("test");
        projectsCollection = db.getCollection("projects");
        taskCollection = db.getCollection("task");

    }

    @AfterAll
    static void closeConnection() {
        mongoClient.close();
    }

    @AfterEach
    void cleanAndCloseConnection() {
        projectsCollection.deleteMany(new Document());
        taskCollection.deleteMany(new Document());
    }

    @Test
    void project_is_created_with_default_task() throws SQLException {
        // arrange
        String projectId = "95083561-bac0-4c90-9471-f8e442978f90";
        String taskId = "eb954d7b-9593-4183-b1b8-22c44a67ba80";
        ProjectRepository projectRepository = new JDBCProjectRepository(projectsCollection);
        TaskRepository taskRepository = new JDBCTaskRepository(taskCollection);
        ProjectService projectService = new ProjectService(projectRepository, taskRepository);
        TaskService taskService = new TaskService(taskRepository);
        ProjectController projectController = new ProjectController(projectService, taskService);

        when(req.body()).thenReturn(
                new JsonObject()
                        .add("projectName", "Test project")
                        .add("projectId", projectId)
                        .add("initialTaskId", taskId)
                        .toString()
        );
        projectController.createProjectHandler(req, res);

        // act
        when(req.params("projectId")).thenReturn(projectId);
        String controllerResponse = projectController.getProjectHandler(req, res);

        // assert
        JsonObject jsonTask = new JsonObject()
                .add("id", taskId)
                .add("name", "Initial task");

        String expectedResponse = new JsonObject()
                .add("id", projectId)
                .add("name", "Test project")
                .add("tasks", new JsonArray().add(jsonTask))
                .toString();

        assertThat(controllerResponse).isEqualTo(expectedResponse);
    }
}
