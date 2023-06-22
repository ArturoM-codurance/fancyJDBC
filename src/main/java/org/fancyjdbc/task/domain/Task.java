package org.fancyjdbc.task.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
@Entity
@Table (name = "task")
public class Task {

    @Id
    private String id;
    private String name;
    @Column(name = "project_id")
    private String projectId;
    @Column(name = "complexity_id")
    private int complexityId;
    private int cost;
    @Column(name ="tax_id")
    private String taxId;

    public Task() {
    }

    public Task(String id, String name, String projectId, int complexityId, int cost, String taxId) {
        this.id = id;
        this.name = name;
        this.projectId = projectId;
        this.complexityId = complexityId;
        this.cost = cost;
        this.taxId = taxId;
    }

    public Task(String id, String name) {
        this.id = id;
        this.name = name;
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
