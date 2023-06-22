package org.fancyjdbc.task.infrastructure.persistance;

import org.fancyjdbc.project.infrastructure.persistance.JDBCProjectRepository;
import org.fancyjdbc.task.domain.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.hibernate.Transaction;

@ExtendWith(MockitoExtension.class)
class JDBCTaskRepositoryTest {

    @Mock
    Connection connection;
    @Mock
    Statement statement;
    @Mock
    private ResultSet resultSet;
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    Transaction transaction;

    @Test
    void should_create_task_in_database() throws SQLException {
        // arrange
        String projectId = "59811fcd-d2b4-4b3f-aca3-30e5c47d6621";
        String taskId = "967bae49-d745-46de-acda-c8c927b32470";
        String defaultName = "Initial task";
        int defaultComplexity = 1;
        int defaultCost = 0;
        String defaultTaxCountry = "spain";

        JDBCTaskRepository jdbcTaskRepository = new JDBCTaskRepository(sessionFactory);
        when(sessionFactory.openSession()).thenReturn(session);

        // act
        jdbcTaskRepository.addTask(taskId, projectId);

        // assert
        verify(session).persist(new Task(taskId, defaultName, projectId, defaultComplexity, defaultCost, defaultTaxCountry));
    }
    @Test
    void should_retrieve_tasks_by_project_id() throws SQLException {
        // arrange
        String projectId = "59811fcd-d2b4-4b3f-aca3-30e5c47d6627";
        String taskId = "967bae49-d745-46de-acda-c8c927b32479";
        String defaultName = "Initial task";
        int defaultComplexity = 1;
        int defaultCost = 0;
        String defaultTaxCountry = "spain";
        JDBCTaskRepository jdbcTaskRepository = new JDBCTaskRepository(sessionFactory);

        when(sessionFactory.openSession()).thenReturn(session);
        Task task1 = new Task(taskId, "task1", projectId,defaultComplexity, defaultCost, taskId);
        Task task2 = new Task(taskId, "task2", projectId,defaultComplexity, defaultCost, taskId);
        List<Task> expectedTaskList = List.of(task1, task2);
        when(session.createQuery("select t from Task t where projectId = :id", Task.class).setParameter("projectId", projectId).list()).thenReturn(expectedTaskList);
        when(session.getTransaction()).thenReturn(transaction);

        // act
        List<Task> actualTaskList = jdbcTaskRepository.getByProjectId(projectId);

        // assert
        assertThat(actualTaskList).isEqualTo(expectedTaskList);
    }
}