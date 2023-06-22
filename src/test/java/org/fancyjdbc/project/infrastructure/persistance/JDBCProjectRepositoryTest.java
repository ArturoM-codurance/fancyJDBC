package org.fancyjdbc.project.infrastructure.persistance;

import org.fancyjdbc.project.domain.Project;
import org.fancyjdbc.task.domain.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
    SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Transaction transaction
            ;

    @Test
    void should_create_project_in_database() {
        // arrange
        String projectId = "d3aaccd2-5a12-4c2f-868d-e788dc544cec";
        String projectName = "Project name";
        JDBCProjectRepository jdbcProjectRepository = new JDBCProjectRepository(sessionFactory);

        // act
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        jdbcProjectRepository.addProject(projectId, projectName);

        // assert
        verify(session).persist(new Project(projectId, projectName));
    }
    @Test
    void should_return_project_from_database() {
        // arrange
        String projectId = "projectId";
        String projectName = "projectName";
        Project expectedProject = new Project(projectId, projectName);
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        when(session.createQuery("select p from Project p where p.id = :id", Project.class).setParameter("id", projectId).getSingleResult()).thenReturn(expectedProject);

        // act
        JDBCProjectRepository jdbcProjectRepository = new JDBCProjectRepository(sessionFactory);

        Project actualProject = jdbcProjectRepository.getProject(projectId);
        // assert
        assertThat(actualProject).isEqualTo(expectedProject);
    }

}