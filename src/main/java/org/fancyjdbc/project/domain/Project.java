package org.fancyjdbc.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "project")
public class Project {
    @Id
    private String id;
    private String name;

    public Project(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Project() {

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id) && Objects.equals(name, project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
