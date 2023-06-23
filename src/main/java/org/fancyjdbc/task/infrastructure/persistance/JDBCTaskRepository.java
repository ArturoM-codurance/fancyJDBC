package org.fancyjdbc.task.infrastructure.persistance;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import org.fancyjdbc.task.domain.Task;
import org.fancyjdbc.task.domain.TaskRepository;


import java.util.List;
import java.util.stream.Stream;


public class JDBCTaskRepository implements TaskRepository {
    private final Datastore datastore;

    public JDBCTaskRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public void addTask(String taskId, String projectId) {
        String defaultName = "Initial task";
        int defaultComplexity = 1;
        int defaultCost = 0;
        String defaultTaxCountry = "spain";

        datastore.insert(new Task(taskId, projectId, defaultName, defaultComplexity, defaultCost, defaultTaxCountry));
    }

    @Override
    public List<Task> getByProjectId(String projectId) {
        return datastore.find(Task.class).filter(Filters.eq("projectId", projectId)).stream().toList();
    }
}
