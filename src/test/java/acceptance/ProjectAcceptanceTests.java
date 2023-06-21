package acceptance;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.fancyjdbc.project.application.ProjectService;
import org.fancyjdbc.project.domain.ProjectRepository;
import org.fancyjdbc.project.infrastructure.persistance.JDBCProjectRepository;
import org.fancyjdbc.project.infrastructure.http.ProjectController;
import org.fancyjdbc.task.application.TaskService;
import org.fancyjdbc.task.domain.TaskComplexity;
import org.fancyjdbc.task.domain.TaskRepository;
import org.fancyjdbc.task.domain.TaskTax;
import org.fancyjdbc.task.infrastructure.persistance.JDBCTaskRepository;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
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
    private static EntityManagerFactory entityManagerFactory;
    @Mock
    Request req;

    @Mock
    Response res;

    @BeforeAll
    static void establishConnectionAndCreateTables() throws SQLException {
        Connection conn = DriverManager.getConnection(CONNECTION_STRING);
        Statement statement = conn.createStatement();
        statement.execute(CREATE_TABLES_QUERY);
        conn.close();
    }

    @BeforeAll
    static void setUp() {
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

    @BeforeEach
    void initDbConnectionAndPopulateTables() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(new TaskComplexity(1, "low"));
        entityManager.persist(new TaskComplexity(2, "medium"));
        entityManager.persist(new TaskComplexity(3, "high"));
        entityManager.persist(new TaskTax("spain", 21));
        entityManager.persist(new TaskTax("uk", 20));
        entityManager.persist(new TaskTax("portugal", 15));
        entityManager.getTransaction().commit();
    }

    @AfterEach
    void cleanAndCloseConnection() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from Project where id is not null").executeUpdate();
        entityManager.createQuery("delete from Task where id is not null").executeUpdate();
        entityManager.createQuery("delete from TaskComplexity where id is not null").executeUpdate();
        entityManager.createQuery("delete from TaskTax where id is not null").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Test
    void project_is_created_with_default_task() throws SQLException {
        // arrange
        String projectId = "95083561-bac0-4c90-9471-f8e442978f90";
        String taskId = "eb954d7b-9593-4183-b1b8-22c44a67ba80";
        ProjectRepository projectRepository = new JDBCProjectRepository(entityManagerFactory);
        TaskRepository taskRepository = new JDBCTaskRepository(entityManagerFactory);
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
