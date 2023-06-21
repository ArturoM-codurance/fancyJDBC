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
    @Column(name = "complexity_id")
    private int complexityId;
    private int cost;
    @Column(name = "tax_id")
    private String taxTerritory;

    public Task(String id, String projectId, String name, int complexityId, int cost, String taxTerritory) {
        this.id = id;
        this.name = name;
        this.projectId = projectId;
        this.complexityId = complexityId;
        this.cost = cost;
        this.taxTerritory = taxTerritory;
    }

    public Task(String taskId, String projectId) {
        id = taskId;
        this.projectId = projectId;
        this.name = "Initial task";
        this.complexityId = 1;
        this.cost = 0;
        this.taxTerritory = "spain";
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
        return complexityId == task.complexityId && cost == task.cost && Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(projectId, task.projectId) && Objects.equals(taxTerritory, task.taxTerritory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, projectId, complexityId, cost, taxTerritory);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", projectId='" + projectId + '\'' +
                ", complexityId=" + complexityId +
                ", cost=" + cost +
                ", taxTerritory='" + taxTerritory + '\'' +
                '}';
    }
}
