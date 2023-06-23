package org.fancyjdbc.project.infrastructure.persistance;

import com.eclipsesource.json.JsonObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.fancyjdbc.project.domain.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.mongodb.client.model.Filters.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JDBCProjectRepositoryTest {
    @Mock
    ResultSet resultSet;
    @Mock
    MongoCollection<Document> collection;
    @Mock
    FindIterable<Document> iterable;
    @Mock
    Document doc;

    @Test
    void should_create_project_in_database() {
        // arrange
        String projectId = "d3aaccd2-5a12-4c2f-868d-e788dc544cec";
        String projectName = "Project name";
        JDBCProjectRepository jdbcProjectRepository = new JDBCProjectRepository(collection);

        // act
        jdbcProjectRepository.addProject(projectId, projectName);

        // assert
        Document expectedDocument = new Document()
                .append("id", projectId)
                .append("name", projectName);
        verify(collection).insertOne(expectedDocument);
    }
    @Test
    void should_return_project_from_database() {
        // arrange
        String projectId = "projectId";
        String projectName = "projectName";
        Project expectedProject = new Project(projectId, projectName);
        when(collection.find(eq("id", projectId))).thenReturn(iterable);
        when(iterable.first()).thenReturn(doc);
        when(doc.toJson()).thenReturn(new JsonObject().add("id", projectId).add("name", projectName).toString());

        // act
        JDBCProjectRepository jdbcProjectRepository = new JDBCProjectRepository(collection);

        Project actualProject = jdbcProjectRepository.getProject(projectId);
        // assert
        assertThat(actualProject).isEqualTo(expectedProject);
    }

}