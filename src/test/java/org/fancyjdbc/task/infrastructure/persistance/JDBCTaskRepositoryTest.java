package org.fancyjdbc.task.infrastructure.persistance;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import org.fancyjdbc.task.domain.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JDBCTaskRepositoryTest {
    @Mock
    Datastore datastore;
    @Mock
    Query<Task> query;
    @Mock
    Stream<Task> stream;

    @Test
    void should_create_task_in_database() {
        // arrange
        String projectId = "59811fcd-d2b4-4b3f-aca3-30e5c47d6621";
        String taskId = "967bae49-d745-46de-acda-c8c927b32470";
        JDBCTaskRepository jdbcTaskRepository = new JDBCTaskRepository(datastore);
        String defaultName = "Initial task";
        int defaultComplexity = 1;
        int defaultCost = 0;
        String defaultTaxCountry = "spain";

        // act
        jdbcTaskRepository.addTask(taskId, projectId);

        // assert
        verify(datastore).insert(new Task(taskId, projectId, defaultName, defaultComplexity, defaultCost, defaultTaxCountry));
    }
    @Test
    void should_retrieve_tasks_by_project_id() {
        // arrange
        String projectId = "59811fcd-d2b4-4b3f-aca3-30e5c47d6627";
        JDBCTaskRepository jdbcTaskRepository = new JDBCTaskRepository(datastore);

        when(datastore.find(Task.class)).thenReturn(query);
        when(query.filter(any(Filter.class))).thenReturn(query);
        when(query.stream()).thenReturn(stream);
        when(stream.toList()).thenReturn(List.of(new Task("task1-id", "task1"), new Task("task2-id", "task2")));

        // act
        List<Task> actualTaskList = jdbcTaskRepository.getByProjectId(projectId);

        // assert
        List<Task> expectedTaskList = List.of(new Task("task1-id", "task1"), new Task ("task2-id", "task2"));
        assertThat(actualTaskList).isEqualTo(expectedTaskList);
    }
}