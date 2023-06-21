package org.fancyjdbc.task.infrastructure.persistance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.fancyjdbc.project.domain.Project;
import org.fancyjdbc.task.domain.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JDBCTaskRepositoryTest {
    @Mock
    EntityManagerFactory entityManagerFactory;
    @Mock
    EntityManager entityManager;

    @Mock
    TypedQuery<Task> query;
    @Mock
    EntityTransaction transaction;

    @Test
    void should_create_task_in_database() {
        // arrange
        String projectId = "59811fcd-d2b4-4b3f-aca3-30e5c47d6621";
        String taskId = "967bae49-d745-46de-acda-c8c927b32470";
        JDBCTaskRepository jdbcTaskRepository = new JDBCTaskRepository(entityManagerFactory);

        // act
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        jdbcTaskRepository.addTask(taskId, projectId);

        // assert
        verify(entityManager).persist(new Task(taskId, projectId));
    }
    @Test
    void should_retrieve_tasks_by_project_id() {
        // arrange
        String projectId = "59811fcd-d2b4-4b3f-aca3-30e5c47d6621";
        String taskId = "967bae49-d745-46de-acda-c8c927b32470";

        JDBCTaskRepository jdbcTaskRepository = new JDBCTaskRepository(entityManagerFactory);
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.createQuery("select t from Task t where t.projectId = :projectId", Task.class)).thenReturn(query);
        when(query.setParameter("projectId", projectId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Task(taskId, projectId)));

        // act
        List<Task> actualTaskList = jdbcTaskRepository.getByProjectId(projectId);

        // assert
        List<Task> expectedTaskList = List.of(new Task(taskId, projectId));
        assertThat(actualTaskList).isEqualTo(expectedTaskList);
    }
}