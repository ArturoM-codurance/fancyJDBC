package org.fancyjdbc.task.infrastructure.persistance;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.fancyjdbc.task.domain.Task;
import org.fancyjdbc.task.domain.TaskRepository;


import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class JDBCTaskRepository implements TaskRepository {
    private final MongoCollection<Document> collection;

    public JDBCTaskRepository(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public void addTask(String taskId, String projectId) {
        String defaultName = "Initial task";
        int defaultComplexity = 1;
        int defaultCost = 0;
        String defaultTaxCountry = "spain";

        Document newTask = new Document()
                .append("id", taskId)
                .append("projectId", projectId)
                .append("name", defaultName)
                .append("complexity", defaultComplexity)
                .append("cost", defaultCost)
                .append("tax", defaultTaxCountry);
        collection.insertOne(newTask);
    }

    @Override
    public List<Task> getByProjectId(String projectId) {
        List<Task> tasks = new ArrayList<>();

        MongoCursor<Document> cursor = collection.find(eq("projectId", projectId)).iterator();

        while (cursor.hasNext()){
            JsonObject taskObject = Json.parse(cursor.next().toJson()).asObject();
            String id = taskObject.getString("id", null);
            String name = taskObject.getString("name", null);
            tasks.add(new Task(id, name));
        }

        return tasks;
    }
}
