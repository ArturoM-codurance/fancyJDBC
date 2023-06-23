package org.fancyjdbc.project.infrastructure.persistance;


import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filter;
import org.fancyjdbc.project.domain.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JDBCProjectRepositoryTest {
    @Mock
    Datastore datastore;
    @Mock
    Query<Project> query;

    @Test
    void should_create_project_in_database() {
        // arrange
        String projectId = "d3aaccd2-5a12-4c2f-868d-e788dc544cec";
        String projectName = "Project name";
        JDBCProjectRepository jdbcProjectRepository = new JDBCProjectRepository(datastore);

        // act
        jdbcProjectRepository.addProject(projectId, projectName);

        // assert
        verify(datastore).insert(new Project(projectId, projectName));
    }
    @Test
    void should_return_project_from_database() {
        // arrange
        String projectId = "projectId";
        String projectName = "projectName";
        Project expectedProject = new Project(projectId, projectName);
        when(datastore.find(Project.class)).thenReturn(query);
        when(query.filter(any(Filter.class))).thenReturn(query);
        when(query.first()).thenReturn(new Project(projectId, projectName));

        // act
        JDBCProjectRepository jdbcProjectRepository = new JDBCProjectRepository(datastore);

        Project actualProject = jdbcProjectRepository.getProject(projectId);
        // assert
        assertThat(actualProject).isEqualTo(expectedProject);
    }

}