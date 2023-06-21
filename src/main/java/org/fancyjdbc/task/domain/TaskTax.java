package org.fancyjdbc.task.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tax")
public class TaskTax {
    @Id
    private String id;
    private int value;

    public TaskTax() {
    }

    public TaskTax(String id, int value) {
        this.id = id;
        this.value = value;
    }
}
