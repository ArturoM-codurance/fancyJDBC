package org.fancyjdbc.project.infrastructure.persistance;

import org.fancyjdbc.project.domain.ProjectRepository;

public class JDBCProjectRepository implements ProjectRepository {
    private final String connectionString;

    public JDBCProjectRepository(String connectionString) {
        this.connectionString = connectionString;
    }
}
