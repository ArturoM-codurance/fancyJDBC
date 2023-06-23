package acceptance;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.result.DeleteResult;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.Morphia;
import org.fancyjdbc.project.application.ProjectService;
import org.fancyjdbc.project.domain.Project;
import org.fancyjdbc.project.domain.ProjectRepository;
import org.fancyjdbc.project.infrastructure.persistance.JDBCProjectRepository;
import org.fancyjdbc.project.infrastructure.http.ProjectController;
import org.fancyjdbc.task.application.TaskService;
import org.fancyjdbc.task.domain.Task;
import org.fancyjdbc.task.domain.TaskRepository;
import org.fancyjdbc.task.infrastructure.persistance.JDBCTaskRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectAcceptanceTests {
    private static Datastore datastore;
    private static MongoClient mongoClient;
    @Mock
    Request req;

    @Mock
    Response res;

    @BeforeAll
    static void establishConnectionAndCreateTables() {
        String uri = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(uri);

        datastore = Morphia.createDatastore(mongoClient, "test");
        datastore.getMapper().mapPackage("org.fancyjdbc.project.domain");
        datastore.getMapper().mapPackage("org.fancyjdbc.task.domain");
        datastore.ensureIndexes();
    }

    @AfterEach
    void clean() {
        datastore.find(Project.class).delete(new DeleteOptions().multi(true));
        datastore.find(Task.class).delete(new DeleteOptions().multi(true));
    }

    @Test
    void project_is_created_with_default_task() throws SQLException {
        // arrange
        String projectId = "95083561-bac0-4c90-9471-f8e442978f90";
        String taskId = "eb954d7b-9593-4183-b1b8-22c44a67ba80";
        ProjectRepository projectRepository = new JDBCProjectRepository(datastore);
        TaskRepository taskRepository = new JDBCTaskRepository(datastore);
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
