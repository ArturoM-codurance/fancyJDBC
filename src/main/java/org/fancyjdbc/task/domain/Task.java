package org.fancyjdbc.task.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "task")
public class Task {

    @Id
    private String id;
    private String name;

    @Column(name = "project_id")
    private String projectId;

    public Task(String id, String name, String projectId) {
        this.id = id;
        this.name = name;
        this.projectId = projectId;
    }

    public Task() {

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
