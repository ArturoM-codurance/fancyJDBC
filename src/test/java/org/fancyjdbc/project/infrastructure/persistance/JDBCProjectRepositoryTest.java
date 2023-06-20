package org.fancyjdbc.project.infrastructure.persistance;

import org.fancyjdbc.project.domain.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JDBCProjectRepositoryTest {

    @Mock
    Connection connection;
    @Mock
    Statement statement;
    @Mock
    ResultSet resultSet;

    @Test
    void should_create_project_in_database() throws SQLException {
        // arrange
        String projectId = "d3aaccd2-5a12-4c2f-868d-e788dc544cec";
        String projectName = "Project name";
        JDBCProjectRepository jdbcProjectRepository = new JDBCProjectRepository(connection);

        // act
        when(connection.createStatement()).thenReturn(statement);
        jdbcProjectRepository.addProject(projectId, projectName);

        // assert
        verify(statement).execute(String.format("INSERT INTO project (id, name) VALUES ('%s', '%s')", projectId, projectName));
    }
    @Test
    void should_return_project_from_database() throws SQLException {
        // arrange
        String projectId = "projectId";
        String projectName = "projectName";
        Project expectedProject = new Project(projectId, projectName);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT * FROM project WHERE id = 'projectId'")).thenReturn(resultSet);
        when(resultSet.getString("id")).thenReturn(projectId);
        when(resultSet.getString("name")).thenReturn(projectName);
        // act
        JDBCProjectRepository jdbcProjectRepository = new JDBCProjectRepository(connection);

        Project actualProject = jdbcProjectRepository.getProject(projectId);
        // assert
        assertThat(actualProject).isEqualTo(expectedProject);
    }

}