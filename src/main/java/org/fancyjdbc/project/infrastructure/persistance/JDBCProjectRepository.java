package org.fancyjdbc.project.infrastructure.persistance;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import org.fancyjdbc.project.domain.Project;
import org.fancyjdbc.project.domain.ProjectRepository;

public class JDBCProjectRepository implements ProjectRepository {
    private final Datastore datastore;

    public JDBCProjectRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public void addProject(String projectId, String projectName) {
        datastore.insert(new Project(projectId, projectName));
    }

    @Override
    public Project getProject(String projectId) {
        return datastore.find(Project.class).filter(Filters.eq("id", projectId)).first();
    }
}
