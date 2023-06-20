package org.fancyjdbc.task.application;

import org.fancyjdbc.task.domain.TaskRepository;

public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {

        this.taskRepository = taskRepository;
    }
}
