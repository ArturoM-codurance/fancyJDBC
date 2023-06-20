package org.fancyjdbc.project.infrastructure.http;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.fancyjdbc.project.application.ProjectService;
import org.fancyjdbc.project.domain.Project;
import org.fancyjdbc.task.application.TaskService;
import org.fancyjdbc.task.domain.Task;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.List;

public class ProjectController {
    private final ProjectService projectService;
    private final TaskService taskService;

    public ProjectController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    public String getProjectHandler(Request req, Response res) throws SQLException {

        String projectId = req.params("projectId");

        Project project = projectService.getProject(projectId);
        JsonArray jsonTasks = new JsonArray();
        List<Task> tasksFromProject = taskService.getTasksFromProject(projectId);
        for (Task task : tasksFromProject) {
            JsonObject jsonTask = new JsonObject()
                    .add("id", task.getId())
                    .add("name", task.getName());
            jsonTasks.add(jsonTask);
        }
        return new JsonObject()
                .add("id", project.getId())
                .add("name", project.getName())
                .add("tasks", jsonTasks)
                .toString();
    }

    public String createProjectHandler(Request req, Response res) throws SQLException {
        JsonObject requestBody = Json.parse(req.body()).asObject();

        String projectId = requestBody.getString("projectId", null);
        String projectName = requestBody.getString("projectName", null);
        String taskId = requestBody.getString("initialTaskId", null);

        projectService.createWithDefaultTask(projectId, projectName, taskId);

        res.status(201);
        return null;
    }
}
