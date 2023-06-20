package org.fancyjdbc.task.domain;

public class Task {

    private final String id;
    private final String name;

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
}
