package org.fancyjdbc.task.infrastructure.persistance;

import com.eclipsesource.json.JsonObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.fancyjdbc.project.infrastructure.persistance.JDBCProjectRepository;
import org.fancyjdbc.task.domain.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JDBCTaskRepositoryTest {
    @Mock
    private ResultSet resultSet;
    @Mock
    MongoCollection<Document> collection;
    @Mock
    FindIterable<Document> iterable;
    @Mock
    MongoCursor<Document> cursor;
    @Mock
    Document firstDocument;
    @Mock
    Document secondDocument;

    @Test
    void should_create_task_in_database() {
        // arrange
        String projectId = "59811fcd-d2b4-4b3f-aca3-30e5c47d6621";
        String taskId = "967bae49-d745-46de-acda-c8c927b32470";
        JDBCTaskRepository jdbcTaskRepository = new JDBCTaskRepository(collection);
        String defaultName = "Initial task";
        int defaultComplexity = 1;
        int defaultCost = 0;
        String defaultTaxCountry = "spain";

        // act
        jdbcTaskRepository.addTask(taskId, projectId);

        // assert
        Document expectedDocument = new Document()
                .append("id", taskId)
                .append("projectId", projectId)
                .append("name", defaultName)
                .append("complexity", defaultComplexity)
                .append("cost", defaultCost)
                .append("tax", defaultTaxCountry);

        verify(collection).insertOne(expectedDocument);
    }
    @Test
    void should_retrieve_tasks_by_project_id() {
        // arrange
        String projectId = "59811fcd-d2b4-4b3f-aca3-30e5c47d6627";
        JDBCTaskRepository jdbcTaskRepository = new JDBCTaskRepository(collection);
        String query = String.format("SELECT * FROM task WHERE project_id = '%s'", projectId);
        when(collection.find(eq("projectId", projectId))).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext()).thenReturn(true, true, false);
        when(cursor.next()).thenReturn(firstDocument, secondDocument);
        when(firstDocument.toJson()).thenReturn(new JsonObject().add("id", "task1-id").add("name", "task1").toString());
        when(secondDocument.toJson()).thenReturn(new JsonObject().add("id", "task2-id").add("name", "task2").toString());

        // act
        List<Task> actualTaskList = jdbcTaskRepository.getByProjectId(projectId);

        // assert
        List<Task> expectedTaskList = List.of(new Task("task1-id", "task1"), new Task ("task2-id", "task2"));
        assertThat(actualTaskList).isEqualTo(expectedTaskList);
    }
}