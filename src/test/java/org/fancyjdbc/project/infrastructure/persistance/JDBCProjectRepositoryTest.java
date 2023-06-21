package org.fancyjdbc.project.infrastructure.persistance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.fancyjdbc.project.domain.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JDBCProjectRepositoryTest {
    @Mock
    EntityManagerFactory entityManagerFactory;
    @Mock
    EntityManager entityManager;
    @Mock
    TypedQuery<Project> query;
    @Mock
    EntityTransaction transaction;

    @Test
    void should_create_project_in_database() {
        // arrange
        String projectId = "d3aaccd2-5a12-4c2f-868d-e788dc544cec";
        String projectName = "Project name";
        JDBCProjectRepository jdbcProjectRepository = new JDBCProjectRepository(entityManagerFactory);

        // act
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        jdbcProjectRepository.addProject(projectId, projectName);

        // assert
        verify(entityManager).persist(new Project(projectId, projectName));
    }
    @Test
    void should_return_project_from_database() {
        // arrange
        String projectId = "projectId";
        String projectName = "projectName";
        Project expectedProject = new Project(projectId, projectName);
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.createQuery("select p from Project p where id = :id", Project.class)).thenReturn(query);
        when(query.setParameter("id", projectId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Project(projectId, projectName)));

        // act
        JDBCProjectRepository jdbcProjectRepository = new JDBCProjectRepository(entityManagerFactory);

        // assert
        Project actualProject = jdbcProjectRepository.getProject(projectId);
        assertThat(actualProject).isEqualTo(expectedProject);
    }

}