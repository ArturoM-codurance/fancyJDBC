package org.fancyjdbc.project.infrastructure.persistance;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.fancyjdbc.project.domain.Project;
import org.fancyjdbc.project.domain.ProjectRepository;

import static com.mongodb.client.model.Filters.eq;

public class JDBCProjectRepository implements ProjectRepository {
    private final MongoCollection<Document> collection;

    public JDBCProjectRepository(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public void addProject(String projectId, String projectName) {
        Document newProject = new Document()
                .append("id", projectId)
                .append("name", projectName);

        collection.insertOne(newProject);
    }

    @Override
    public Project getProject(String projectId) {
        JsonObject documentJson = Json.parse(collection.find(eq("id", projectId)).first().toJson()).asObject();

        String id = documentJson.getString("id", null);
        String projectName = documentJson.getString("name", null);
        return new Project(id, projectName);
    }
}
