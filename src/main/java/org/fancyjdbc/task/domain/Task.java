package org.fancyjdbc.task.domain;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.Objects;

@Entity("tasks")
public class Task {

    @Id
    private String _id;
    private String id;
    private String projectId;
    private String name;
    private int complexity;
    private int cost;
    private String taxCountry;

    public Task(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Task() {
    }

    public Task(String id, String projectId, String name, int complexity, int cost, String taxCountry) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.complexity = complexity;
        this.cost = cost;
        this.taxCountry = taxCountry;
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
