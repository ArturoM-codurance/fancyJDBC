package org.fancyjdbc.project.domain;

import java.sql.SQLException;

public interface ProjectRepository {
    void addProject(String projectId, String projectName) throws SQLException;
}
