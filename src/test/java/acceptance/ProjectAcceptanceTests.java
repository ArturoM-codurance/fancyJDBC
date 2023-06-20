package acceptance;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
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

    public static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/postgres?user=user&password=password";
    public static final String CREATE_TABLES_QUERY = """
                    -- Tables -------------------------------------------------------
                    DROP TABLE IF EXISTS task;
                    DROP TABLE IF EXISTS project;
                    DROP TABLE IF EXISTS complexity;
                    DROP TABLE IF EXISTS tax;
                    
                    CREATE TABLE project (
                        id text PRIMARY KEY,
                        name text NOT NULL
                    );
                    
                    CREATE TABLE task (
                        id text PRIMARY KEY,
                        name text NOT NULL,
                        project_id text NOT NULL,
                        complexity_id integer NOT NULL,
                        cost integer NOT NULL,
                        tax_id text NOT NULL,
                        main_task_id text
                    );
                    
                    CREATE TABLE complexity (
                        id integer PRIMARY KEY,
                        value text NOT NULL
                    );
                    
                    CREATE TABLE tax (
                        id text PRIMARY KEY,
                        value integer NOT NULL
                    );
            """;
    @Mock
    Request req;

    @Mock
    Response res;
    static private Connection conn;

    @BeforeAll
    static void establishConnectionAndCreateTables() throws SQLException {
        conn = DriverManager.getConnection(CONNECTION_STRING);
        Statement statement = conn.createStatement();
        statement.execute(CREATE_TABLES_QUERY);
    }

    @AfterAll
    static void closeConnection() throws SQLException {
        conn.close();
    }

    @BeforeEach
    void initDbConnectionAndPopulateTables() throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute("INSERT INTO complexity (id, value) VALUES (1, 'low'), (2, 'medium'), (3, 'high')");
        statement.execute("INSERT INTO tax (id, value) VALUES ('spain', 21), ('uk', 20), ('portugal', 15)");
    }

    @AfterEach
    void cleanAndCloseConnection() throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute("DELETE FROM complexity");
        statement.execute("DELETE FROM tax");
    }

    @Test
    void project_is_created_with_default_task(){
        // arrange
        String projectId = "95083561-bac0-4c90-9471-f8e442978f90";
        String taskId = "eb954d7b-9593-4183-b1b8-22c44a67ba80";
        ProjectRepository projectRepository = new JDBCProjectRepository(CONNECTION_STRING);
        TaskRepository taskRepository = new JDBCTaskRepository(CONNECTION_STRING);
        ProjectService projectService = new ProjectService(projectRepository, taskRepository);
        ProjectController projectController = new ProjectController(projectService);

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
