package org.fancyjdbc.task.application;

import org.fancyjdbc.task.domain.Task;
import org.fancyjdbc.task.domain.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceShould {

    @Mock
    private TaskRepository taskRepository;

    @Test
    void call_repository_getTasksFromProject() throws SQLException {
        //Arrange
        String projectId = "project-id";
        TaskService taskService = new TaskService(taskRepository);

        //Act
        taskService.getTasksFromProject(projectId);

        //Assert
        verify(taskRepository).getByProjectId(projectId);
    }

    @Test
    void retrieve_specific_task_list_given_project_id() throws SQLException {
        //Arrange
        String projectId = "project-id";
        List<Task> expectedList = List.of();
        when(taskRepository.getByProjectId(projectId)).thenReturn(expectedList);
        TaskService taskService = new TaskService(taskRepository);

        //Act
        List<Task> actualList = taskService.getTasksFromProject(projectId);

        //Assert
        assertThat(actualList).isEqualTo(expectedList);
    }

}