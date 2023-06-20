package org.fancyjdbc.task.infrastructure.persistance;

import org.fancyjdbc.project.infrastructure.persistance.JDBCProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JDBCTaskRepositoryTest {

    @Mock
    Connection connection;
    @Mock
    Statement statement;

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
    void name_later(){
        // arrange

        // act

        // assert
        assertTrue(true);
    }
}