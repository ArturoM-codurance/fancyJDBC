package org.fancyjdbc.task.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "complexity")
public class TaskComplexity {
    @Id
    private Integer id;

    private String value;

    public TaskComplexity() {
    }

    public TaskComplexity(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
