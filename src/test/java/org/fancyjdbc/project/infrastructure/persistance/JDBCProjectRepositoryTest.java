package org.fancyjdbc.project.infrastructure.persistance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JDBCProjectRepositoryTest {

    @Mock
    Connection connection;
    @Mock
    Statement statement;

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

}