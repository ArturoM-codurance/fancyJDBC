package org.fancyjdbc.project.domain;

public class Project {
    private final String id;
    private final String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Project(String id, String name) {

        this.id = id;
        this.name = name;
    }
}
