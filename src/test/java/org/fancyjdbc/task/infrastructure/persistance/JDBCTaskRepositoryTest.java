package org.fancyjdbc.task.infrastructure.persistance;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JDBCTaskRepositoryTest {

    @Mock
    Connection connection;
    @Mock
    Statement statement;
    @Mock
    private ResultSet resultSet;

    @Test
    void should_create_task_in_database() throws SQLException {
        // arrange
        String projectId = "59811fcd-d2b4-4b3f-aca3-30e5c47d6621";
        String taskId = "967bae49-d745-46de-acda-c8c927b32470";
        JDBCTaskRepository jdbcTaskRepository = new JDBCTaskRepository(connection);

        // act
        when(connection.createStatement()).thenReturn(statement);
        jdbcTaskRepository.addTask(taskId, projectId);

        // assert
        verify(statement).execute(String.format("INSERT INTO task (id, project_id, name, complexity_id, cost, tax_id) VALUES ('%s', '%s', 'Initial task', 1, 0, 'spain')", taskId, projectId));
    }
    @Test
    void should_retrieve_tasks_by_project_id() throws SQLException {
        // arrange
        String projectId = "59811fcd-d2b4-4b3f-aca3-30e5c47d6627";
        JDBCTaskRepository jdbcTaskRepository = new JDBCTaskRepository(connection);
        String query = String.format("SELECT * FROM task WHERE project_id = '%s'", projectId);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(query)).thenReturn(resultSet);
        when(resultSet.getString("id")).thenReturn("task1-id", "task2-id");
        when(resultSet.getString("name")).thenReturn("task1", "task2");
        when(resultSet.next()).thenReturn(true, true, false);

        // act
        List<Task> actualTaskList = jdbcTaskRepository.getByProjectId(projectId);

        // assert
        verify(statement).executeQuery(query);
        List<Task> expectedTaskList = List.of(new Task("task1-id", "task1"), new Task ("task2-id", "task2"));
        assertThat(actualTaskList).isEqualTo(expectedTaskList);
    }
}